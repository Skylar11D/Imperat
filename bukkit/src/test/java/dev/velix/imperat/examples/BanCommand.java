package dev.velix.imperat.examples;

import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.annotations.types.Command;
import dev.velix.imperat.annotations.types.Description;
import dev.velix.imperat.annotations.types.Permission;
import dev.velix.imperat.annotations.types.methods.DefaultUsage;
import dev.velix.imperat.annotations.types.methods.Usage;
import dev.velix.imperat.annotations.types.parameters.*;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.Nullable;

@Command("ban")
@Permission("command.ban")
@Description("Main command for banning players")
public final class BanCommand {

    @DefaultUsage
    public void showUsage(BukkitSource source) {
        source.reply("/ban <player> [-silent] [duration] [reason...]");
    }

    @Usage
    public void banPlayer(
            BukkitSource source,
            @Named("player") OfflinePlayer player,
            @Switch({"silent", "s"}) boolean silent,
            @Named("duration") @Optional @Nullable String duration,
            @Named("reason") @Optional @DefaultValue("Breaking server laws") @Greedy String reason
    ) {
        //TODO actual ban logic
        String durationFormat = duration == null ? "FOREVER" : "for " + duration;
        String msg = "Banning " + player.getName() + " " + durationFormat + " due to " + reason;
        if (!silent)
            Bukkit.broadcastMessage(msg);
        else
            source.reply(msg);
    }

}
