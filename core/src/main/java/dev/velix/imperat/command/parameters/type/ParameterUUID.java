package dev.velix.imperat.command.parameters.type;

import dev.velix.imperat.command.parameters.CommandParameter;
import dev.velix.imperat.context.ExecutionContext;
import dev.velix.imperat.context.Source;
import dev.velix.imperat.context.internal.CommandInputStream;
import dev.velix.imperat.exception.ImperatException;
import dev.velix.imperat.exception.InvalidUUIDException;
import dev.velix.imperat.util.TypeWrap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public final class ParameterUUID<S extends Source> extends BaseParameterType<S, UUID> {
    public ParameterUUID() {
        super(TypeWrap.of(UUID.class));
    }

    @Override
    public @Nullable UUID resolve(
        ExecutionContext<S> context,
        @NotNull CommandInputStream<S> commandInputStream
    ) throws ImperatException {
        String raw = commandInputStream.currentRaw();
        if (raw == null) {
            return null;
        }
        try {
            return UUID.fromString(raw);
        } catch (Exception ex) {
            throw new InvalidUUIDException(raw);
        }
    }

    @Override
    public boolean matchesInput(String input, CommandParameter<S> parameter) {
        try {
            UUID.fromString(input);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
