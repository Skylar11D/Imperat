package dev.velix.imperat.command.processors;

import dev.velix.imperat.Imperat;
import dev.velix.imperat.context.Context;
import dev.velix.imperat.context.ResolvedContext;
import dev.velix.imperat.context.Source;
import dev.velix.imperat.exceptions.CommandException;

/**
 * Defines a functional interface that processes a {@link Context}
 * AFTER the resolving of the arguments into values.
 *
 * @param <S> the command sender type
 */
public interface CommandPostProcessor<S extends Source> {

    /**
     * Processes context AFTER the resolving operation.
     *
     * @param imperat the api
     * @param context the context
     * @throws CommandException the exception to throw if something happens
     */
    void process(
            Imperat<S> imperat,
            ResolvedContext<S> context
    ) throws CommandException;

}
