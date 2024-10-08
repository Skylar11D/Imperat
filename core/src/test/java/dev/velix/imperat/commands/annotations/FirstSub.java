package dev.velix.imperat.commands.annotations;

import dev.velix.imperat.TestSource;
import dev.velix.imperat.annotations.*;

@SubCommand("first")
@Inherit(SecondSub.class)
public final class FirstSub {
    
    @Usage
    public void defaultUsage(TestSource source) {
        source.reply("Default execution of first sub-command");
    }
    
    @Usage
    public void cmdUsage(TestSource source, @Named("otherText") String otherText, @Named("arg1") @Suggest({"x", "y", "z", "sexy"}) String arg1) {
        source.reply("Executing usage in first's main usage, otherText=" + otherText + ", arg1= " + arg1);
    }
    
    
}
