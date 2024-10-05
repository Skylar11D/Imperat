package dev.velix.imperat.commodore;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.velix.imperat.WrappedBukkitCommand;
import dev.velix.imperat.command.Command;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Objects;
import java.util.function.Predicate;

@SuppressWarnings("ALL")
final class ModernPaperCommodore extends AbstractCommodore<WrappedBukkitCommand> {

    private final Plugin plugin;
    private final LifecycleEventManager<Plugin> manager;
    ModernPaperCommodore(Plugin plugin) throws ClassNotFoundException {
        Class.forName("io.papermc.paper.command.brigadier.Commands");
        this.plugin = plugin;
        this.manager = plugin.getLifecycleManager();
    }

    /**
     * Registers the provided argument data to the dispatcher, against all
     * aliases defined for the {@code command}.
     *
     * <p>Additionally applies the CraftBukkit {@link SuggestionProvider}
     * to all arguments within the node, so ASK_SERVER suggestions can continue
     * to function for the command.</p>
     *
     * <p>Players will only be sent argument data if they pass the provided
     * {@code permissionTest}.</p>
     *
     * @param command        the command to read aliases from
     * @param node           the argument data
     * @param permissionTest the predicate to check whether players should be sent argument data
     */
    @Override
    public void register(WrappedBukkitCommand command, LiteralCommandNode<?> node, Predicate<? super Player> permissionTest) {
        Objects.requireNonNull(command, "command");
        Objects.requireNonNull(node, "node");
        Objects.requireNonNull(permissionTest, "permissionTest");

        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            Commands registrar = event.registrar();

            String desc = command == null ? null : command.description().toString();

            registrar.register(
                plugin.getPluginMeta(), (LiteralCommandNode<CommandSourceStack>) node,
                desc, getAliases(command)
            );

        });
    }

    /**
     * Registers the provided argument data to the dispatcher.
     *
     * <p>Equivalent to calling
     * {@link CommandDispatcher#register(LiteralArgumentBuilder)}.</p>
     *
     * <p>Prefer using {@link #register(Command, LiteralCommandNode)}.</p>
     *
     * @param node the argument data
     */
    @Override
    public void register(LiteralCommandNode<?> node) {
        register(null, node);
    }

    @Override
    public CommandSender wrapNMSCommandSource(Object nmsCmdSource) {
        if (nmsCmdSource instanceof CommandSourceStack stack) {
            return stack.getSender();
        }
        throw new UnsupportedOperationException();
    }

}
