package dev.velix.imperat.commands.annotations;

import dev.velix.imperat.TestSource;
import dev.velix.imperat.annotations.types.*;

@Command("test")
@Inherit(FirstSub.class)
public class TestCommand {

    //TODO test command methods
    @Usage
    public void defaultExec(TestSource source) {
        source.reply("Default execution of test(root) command");
    }

    @Usage
    public void cmdUsage(TestSource source, @Named("arg1") String arg1) {
        source.reply("Executing usage in test's main usage, arg1= " + arg1);
    }

    @SubCommand("othersub")
    public void doOtherSub(TestSource source, @Named("otherArg") String otherArg) {
        source.reply("Other-arg= " + otherArg);
    }


}
