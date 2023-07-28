package coloryr.allmusic.side.forge;

import coloryr.allmusic.core.command.CommandEX;
import coloryr.allmusic.core.command.TabCommand;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public class CommandForge implements Command<CommandSourceStack>, Predicate<CommandSourceStack>, SuggestionProvider<CommandSourceStack> {
    public static CommandForge instance = new CommandForge();

    public void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(((Commands.literal("music").requires(this)).executes(this))
                .then(Commands.argument("args", StringArgumentType.greedyString())
                        .suggests(this).executes(this)));
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        var item = context.getSource();
        var source = item.source;

        var input = context.getInput();
        var temp = input.split(" ");
        var arg = new String[temp.length - 1];
        System.arraycopy(temp, 1, arg, 0, arg.length);

        CommandEX.ex(source, context.getSource().getTextName(), arg);

        return 0;
    }

    @Override
    public boolean test(CommandSourceStack stack) {
        return true;
    }

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        var input = context.getInput();
        var temp = input.split(" ");
        var arg = new String[input.endsWith(" ") ? temp.length : temp.length - 1];
        System.arraycopy(temp, 1, arg, 0, temp.length - 1);

        if (input.endsWith(" "))
            builder = builder.createOffset(builder.getInput().lastIndexOf(32) + 1);

        List<String> results = TabCommand.getTabList(context.getSource().getTextName(), arg);

        for (String s : results) {
            builder.suggest(s);
        }

        return builder.buildFuture();
    }
}
