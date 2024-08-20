package dev.velix.imperat.context.internal;

import dev.velix.imperat.CommandDispatcher;
import dev.velix.imperat.CommandSource;
import dev.velix.imperat.command.Command;
import dev.velix.imperat.command.CommandUsage;
import dev.velix.imperat.context.ArgumentQueue;
import dev.velix.imperat.context.Context;
import dev.velix.imperat.context.ResolvedContext;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a way for defining
 * how the context is created
 *
 * @param <C>
 */
@ApiStatus.AvailableSince("1.0.0")
public interface ContextFactory<C> {

    static <C> ContextFactory<C> defaultFactory() {
        return new DefaultContextFactory<>();
    }


    /**
     * @param dispatcher    the dispatcher
     * @param commandSource the sender/source of this command execution
     * @param command       the command label used
     * @param queue         the args input
     * @return new context from the command and args used by {@link CommandSource}
     */
    @NotNull
    Context<C> createContext(
            @NotNull CommandDispatcher<C> dispatcher,
            @NotNull CommandSource<C> commandSource,
            @NotNull String command,
            @NotNull ArgumentQueue queue
    );

    /**
     * @param dispatcher   the dispatcher
     * @param command      the command that's running
     * @param plainContext the context plain
     * @param usage        the command usage
     * @return the context after resolving args into values for
     * later on parsing it into the execution
     */
    ResolvedContext<C> createResolvedContext(
            @NotNull CommandDispatcher<C> dispatcher,
            @NotNull Command<C> command,
            @NotNull Context<C> plainContext,
            @NotNull CommandUsage<C> usage
    );

}
