package dev.velix.imperat.commands.annotations.examples;

import dev.velix.imperat.TestSource;
import dev.velix.imperat.command.parameters.CommandParameter;
import dev.velix.imperat.context.Context;
import dev.velix.imperat.context.internal.sur.Cursor;
import dev.velix.imperat.exception.ImperatException;
import dev.velix.imperat.exception.SourceAnswerException;
import dev.velix.imperat.resolvers.ValueResolver;

public final class GroupValueResolver implements ValueResolver<TestSource, Group> {
    
    @Override
    public Group resolve(
            TestSource source,
            Context<TestSource> context,
            String raw,
            Cursor cursor,
            CommandParameter parameter
    ) throws ImperatException {
        /*if (sender.isConsole()) {
            throw new SenderErrorException("Invalid group '%s'", raw);
        }*/
        var group = GroupRegistry.getInstance()
                .getData(raw);
        if (group.isEmpty()) {
            throw new SourceAnswerException("Invalid group '%s'", raw);
        }
        return group.get();
    }
}
