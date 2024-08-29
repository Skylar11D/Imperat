package dev.velix.imperat.resolvers;

import dev.velix.imperat.Source;
import dev.velix.imperat.command.CommandUsage;
import dev.velix.imperat.command.parameters.CommandParameter;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a functional way of checking for the permissions
 * of the command source/sender.
 */
@ApiStatus.AvailableSince("1.0.0")
public interface PermissionResolver<C> {

    /**
     * @param source     the source of the command (console or other)
     * @param permission the permission
     * @return whether this command source/sender has a specific permission
     */
    boolean hasPermission(@NotNull Source<C> source, @Nullable String permission);

    default boolean hasUsagePermission(Source<C> source, @Nullable CommandUsage<C> usage) {
        if (usage == null) {
            return true;
        }
        if (!hasPermission(source, usage.getPermission())) return false;
        for (CommandParameter parameter : usage.getParameters()) {
            if (!parameter.isCommand()) continue;
            if (!hasPermission(source, parameter.asCommand().getPermission()))
                return false;
        }
        return true;
    }

}
