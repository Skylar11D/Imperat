package dev.velix.imperat.command;

import dev.velix.imperat.annotations.base.element.ParameterElement;
import dev.velix.imperat.context.Source;
import dev.velix.imperat.resolvers.ContextResolver;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

/**
 * Represents a context resolver factory
 * that is responsible for creating {@link ContextResolver}
 *
 * @param <S> the command-sender type
 */
public interface ContextResolverFactory<S extends Source> {
	
	/**
	 * Creates a context resolver based on the parameter
	 *
	 * @param parameter the parameter (null if used classic way)
	 * @return the {@link ContextResolver} specific for that parameter
	 */
	@Nullable
	<T> ContextResolver<S, T> create(Type type, @Nullable ParameterElement parameter);
	
}
