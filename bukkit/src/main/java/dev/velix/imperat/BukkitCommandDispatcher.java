package dev.velix.imperat;

import dev.velix.imperat.brigadier.BukkitBrigadierManager;
import dev.velix.imperat.caption.Messages;
import dev.velix.imperat.command.BaseCommandDispatcher;
import dev.velix.imperat.command.Command;
import dev.velix.imperat.command.CommandUsage;
import dev.velix.imperat.context.Context;
import dev.velix.imperat.exceptions.context.ContextResolveException;
import dev.velix.imperat.help.CommandHelp;
import dev.velix.imperat.resolvers.BukkitPermissionResolver;
import dev.velix.imperat.resolvers.PermissionResolver;
import dev.velix.imperat.util.Preconditions;
import dev.velix.imperat.util.TypeUtility;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.UUID;

public class BukkitCommandDispatcher extends BaseCommandDispatcher<CommandSender> {

    private final Plugin plugin;
    private final AudienceProvider provider;
    
    private Map<String, org.bukkit.command.Command> bukkitOGMapping;
    
    private BukkitBrigadierManager brigadierManager;
    
    private final static BukkitPermissionResolver DEFAULT_PERMISSION_RESOLVER = new BukkitPermissionResolver();
    
    private final static LegacyComponentSerializer LEGACY_COMPONENT_SERIALIZER = LegacyComponentSerializer.builder()
            .character('&')
            .hexColors()
            .hexCharacter('#')
            .build();

    @SuppressWarnings("unchecked")
    private BukkitCommandDispatcher(Plugin plugin, AudienceProvider audienceProvider, @NotNull PermissionResolver<CommandSender> permissionResolver) {
        super(permissionResolver);
        this.plugin = plugin;
        CommandDebugger.setLogger(plugin.getLogger());
        if(BukkitUtil.KNOWN_COMMANDS != null) {
            try {
                bukkitOGMapping = (Map<String, org.bukkit.command.Command>) BukkitUtil.KNOWN_COMMANDS.get(BukkitUtil.COMMAND_MAP);
            } catch (IllegalAccessException e) {
                CommandDebugger.warning("Failed to access the internal command map");
            }
        }
        this.provider = audienceProvider == null ? BukkitAudiences.create(plugin) : audienceProvider;
        registerValueResolvers();
    }
    
    /**
     * Creates a bukkit command dispatcher instance
     * @param plugin the plugin
     * @param audiences the kyori adventure audience provider
     * @param permissionResolver the permission resolver
     * @param override whether to override the existing instance in the bukkit server
     *                 or just use an already existing instance in the server loaded
     *                 from any other plugin
     * @return the new or existing bukkit command dispatcher instance in the bukkit server
     */
    public static BukkitCommandDispatcher create(
            @NotNull Plugin plugin,
            @Nullable AudienceProvider audiences,
            @NotNull PermissionResolver<CommandSender> permissionResolver,
            boolean override
    ) {
        Preconditions.notNull(plugin, "plugin cannot be null !");
        RegisteredServiceProvider<BukkitCommandDispatcher> provider =
                Bukkit.getServicesManager().getRegistration(BukkitCommandDispatcher.class);
        if (provider == null || override) {
            BukkitCommandDispatcher dispatcher = new BukkitCommandDispatcher(plugin, audiences, permissionResolver);
            Bukkit.getServicesManager().register(BukkitCommandDispatcher.class, dispatcher, plugin, ServicePriority.Normal);
            return dispatcher;
        }
        return provider.getProvider();
    }
    
    public static BukkitCommandDispatcher create(
            @NotNull Plugin plugin,
            @Nullable AudienceProvider audiences,
            @NotNull PermissionResolver<CommandSender> permissionResolver
    ) {
        return create(plugin, audiences, permissionResolver, false);
    }

    public static BukkitCommandDispatcher create(Plugin plugin, @Nullable AudienceProvider audienceProvider) {
        return create(plugin, audienceProvider, DEFAULT_PERMISSION_RESOLVER);
    }
    public static BukkitCommandDispatcher create(Plugin plugin, @NotNull PermissionResolver<CommandSender> permissionResolver) {
        return create(plugin, null, permissionResolver);
    }
    
    public static BukkitCommandDispatcher create(Plugin plugin) {
        return create(plugin, null, DEFAULT_PERMISSION_RESOLVER);
    }
    

    /**
     * Wraps the sender into a built-in command-sender type
     *
     * @param sender the sender's actual value
     * @return the wrapped command-sender type
     */
    @Override
    public CommandSource<CommandSender> wrapSender(CommandSender sender) {
        return new BukkitCommandSource(sender, provider);
    }

    /**
     * @return the platform of the module
     */
    @Override
    public Plugin getPlatform() {
        return plugin;
    }

    /**
     * Creates an instance of {@link CommandHelp}
     *
     * @param command       the command
     * @param context       the context
     * @param detectedUsage the usage
     * @return {@link CommandHelp} for the command usage used in a certain context
     */
    @Override
    public CommandHelp<CommandSender> createCommandHelp(
            Command<CommandSender> command,
            Context<CommandSender> context,
            CommandUsage<CommandSender> detectedUsage
    ) {
        return BukkitCommandHelp.create(this, command, context, detectedUsage);
    }

    @Override
    public void shutdownPlatform() {
        Bukkit.getPluginManager().disablePlugin(plugin);
    }


    @Override
    public final String commandPrefix() {
        return "/";
    }

    /**
     * Registering a command into the dispatcher
     *
     * @param command the command to register
     */
    @Override
    public final void registerCommand(Command<CommandSender> command) {
        super.registerCommand(command);
        var internalCmd = new InternalBukkitCommand(this, command);
        if(BukkitUtil.KNOWN_COMMANDS != null) {
            bukkitOGMapping.put(command.getName(), internalCmd);
        }else {
            BukkitUtil.COMMAND_MAP.register(command.getName(), internalCmd);
        }
        if(brigadierManager != null) {
            System.out.println("BrigadierManager is not null !");
            brigadierManager.registerBukkitCommand(internalCmd, command);
        }
    }
    

    protected void registerValueResolvers() {

        this.registerValueResolver(Player.class, ((source, context, raw, parameter) -> {
            Player player = Bukkit.getPlayer(raw.toLowerCase());
            if (player != null) return player;
            throw new ContextResolveException("Player '" + raw + "' is offline or invalid");
        }));

        this.registerValueResolver(OfflinePlayer.class, (source, context, raw, parameter) -> {
	        if(raw.length() > 16) {
              throw new ContextResolveException("Invalid or Unknown offline player '%s'", raw);
          }
          return Bukkit.getOfflinePlayer(raw);
        });

        this.registerValueResolver(World.class, (source, context, raw, parameter) -> {
            World world = Bukkit.getWorld(raw.toLowerCase());
            if (world != null) return world;
            throw new ContextResolveException("Invalid world '%s'", raw);
        });

        registerValueResolver(UUID.class, (source, context, raw, parameter)-> {
            try {
	            return UUID.fromString(raw);
            }catch (IllegalArgumentException ex) {
                throw new ContextResolveException("Invalid uuid-format '%s'", raw);
            }
        });
        
        registerValueResolver(Component.class, (source, context, raw, parameter) -> {
            String result = Messages.MINI_MESSAGE.stripTags(raw);
            if (result.length() == raw.length()) {
                return Messages.getMsg(raw);
            }
            return LEGACY_COMPONENT_SERIALIZER.deserialize(raw);
        });
    }

    public void applyBrigadier() {
        brigadierManager = BukkitBrigadierManager.load(this);
        //TODO apply on all currently registered commands
    }
    
    @SuppressWarnings("unchecked")
    public static Class<? extends Entity> getSelectedEntity(@NotNull Type selectorType) {
        return (Class<? extends Entity>) TypeUtility.getInsideGeneric(selectorType, Entity.class);
    }
}
