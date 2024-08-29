package dev.velix.imperat.help;

import dev.velix.imperat.Imperat;
import dev.velix.imperat.Source;
import dev.velix.imperat.caption.CaptionKey;
import dev.velix.imperat.command.Command;
import dev.velix.imperat.command.CommandUsage;
import dev.velix.imperat.context.Context;
import dev.velix.imperat.util.text.PaginatedText;
import dev.velix.imperat.util.text.TextPage;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;

@ApiStatus.AvailableSince("1.0.0")
public abstract class CommandHelp<C> {

    private final Imperat<C> dispatcher;
    private final Context<C> context;
    private final Command<C> command;
    private final CommandUsage<C> usage;
    private final HelpTemplate template;

    public CommandHelp(
            Imperat<C> dispatcher,
            Command<C> command,
            Context<C> context,
            CommandUsage<C> usage
    ) {
        this.dispatcher = dispatcher;
        this.command = command;
        this.template = dispatcher.getHelpTemplate();
        this.context = context;
        this.usage = usage;
    }

    public void display(Source<C> source) {
        display(source, 1);
    }

    public void display(Source<C> source, int page) {

        if (template instanceof PaginatedHelpTemplate paginatedTemplate) {
            displayPaginated(source, paginatedTemplate, page);
        } else {
            displayNormal(source);
        }

    }

    private void displayPaginated(
            Source<C> source,
            PaginatedHelpTemplate template,
            int page
    ) {
        if (template == null) {
            dispatcher.sendCaption(CaptionKey.NO_HELP_AVAILABLE_CAPTION,
                    command, source, context, usage, null);
            return;
        }

        PaginatedText<CommandUsage<C>> text = new PaginatedText<>(template.syntaxesPerPage());

        for (var usage : command.getUsages()) {
            if (usage.isDefault()) continue;
            text.add(usage);
        }

        text.paginate();
        if (text.getMaxPages() == 0) {
            dispatcher.sendCaption(CaptionKey.NO_HELP_AVAILABLE_CAPTION,
                    command, source, context, usage, null);
            return;
        }

        TextPage<CommandUsage<C>> textPage = text.getPage(page);

        if (textPage == null) {
            dispatcher.sendCaption(CaptionKey.NO_HELP_PAGE_AVAILABLE_CAPTION,
                    command, source, context, usage, null);
            return;
        }
        source.reply(template.fullHeader(command, page, text.getMaxPages()));

        template.getUsagesDisplayer().display(dispatcher, command, source,
                template.getUsageFormatter(), textPage.asList());

        source.reply(template.getFooter(command));
    }

    private void displayNormal(Source<C> source) {
        if (template == null) {
            dispatcher.sendCaption(CaptionKey.NO_HELP_AVAILABLE_CAPTION,
                    command, source, context, usage, null);
            return;
        }

        final int maxUsages = command.getUsages().size();
        if (maxUsages == 0) {
            dispatcher.sendCaption(CaptionKey.NO_HELP_AVAILABLE_CAPTION,
                    command, source, context, usage, null);
            return;
        }

        source.reply(template.getHeader(command));
        template.getUsagesDisplayer().display(dispatcher, command, source,
                template.getUsageFormatter(), new ArrayList<>(command.getUsages()));

        source.reply(template.getFooter(command));
    }

}
