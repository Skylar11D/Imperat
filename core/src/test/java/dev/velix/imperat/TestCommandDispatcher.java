package dev.velix.imperat;

import dev.velix.imperat.command.BaseCommandDispatcher;
import dev.velix.imperat.command.Command;
import dev.velix.imperat.command.CommandUsage;
import dev.velix.imperat.context.Context;
import dev.velix.imperat.help.CommandHelp;
import dev.velix.imperat.resolvers.PermissionResolver;

public final class TestCommandDispatcher extends BaseCommandDispatcher<TestSender> {
	
	TestCommandDispatcher() {
		super();
	}
	
	@Override
	public String commandPrefix() {
		return "";
	}
	
	/**
	 * @return {@link PermissionResolver} for the dispatcher
	 */
	@Override
	public PermissionResolver<TestSender> getPermissionResolver() {
		return null;
	}
	
	/**
	 * Wraps the sender into a built-in command-sender type
	 *
	 * @param sender the sender's actual value
	 * @return the wrapped command-sender type
	 */
	@Override
	public CommandSource<TestSender> wrapSender(TestSender sender) {
		return new TestCommandSource(sender);
	}
	
	/**
	 * @return the platform of the module
	 */
	@Override
	public Object getPlatform() {
		return null;
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
	public CommandHelp<TestSender> createCommandHelp(Command<TestSender> command, Context<TestSender> context, CommandUsage<TestSender> detectedUsage) {
		return null;
	}
	
	/**
	 *
	 */
	@Override
	public void shutdownPlatform() {
	
	}
}
