package dev.velix.imperat.help.templates;

import dev.velix.imperat.caption.Messages;
import dev.velix.imperat.command.Command;
import dev.velix.imperat.help.HelpTemplate;
import dev.velix.imperat.help.UsageFormatter;
import dev.velix.imperat.help.UsageDisplayer;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class DefaultTemplate implements HelpTemplate {


    private UsageFormatter formatter = new DefaultFormatter();
    private UsageDisplayer displayer = UsageDisplayer.plain();

    public DefaultTemplate() {
    }

    /**
     * @return the header
     */
    @Override
    public Component getHeader(Command<?> command) {
        return Messages.getMsg("<dark_gray><bold><strikethrough>=================== <dark_green>" + command.getName() + "'s help</dark_green> ===================");
    }

    /**
     * @return the footer
     */
    @Override
    public Component getFooter(Command<?> command) {
        return Messages.getMsg("<dark_gray><bold><strikethrough>======================================");
    }

    /**
     * The usage formatter for the help-template
     *
     * @return the formatter of the usage
     */
    @Override
    public UsageFormatter getUsageFormatter() {
        return formatter;
    }

    /**
     * sets the usage help-formatter
     *
     * @param formatter the instance of the formatter to set to
     */
    @Override
    public void setUsageFormatter(UsageFormatter formatter) {
        this.formatter = formatter;
    }

    /**
     * @return the usage displayer
     */
    @Override
    public UsageDisplayer getUsagesDisplayer() {
        return displayer;
    }

    /**
     * Sets the usage displayer instance
     * to a new one
     *
     * @param displayer the displayer instance to set
     */
    @Override
    public void setUsageDisplayer(UsageDisplayer displayer) {
        this.displayer = displayer;
    }


}
