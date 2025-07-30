package com.coloryr.allmusic.server.side.fabric;

import com.coloryr.allmusic.server.core.command.CommandEX;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public class CommandFabric implements Command<ServerCommandSource>, Predicate<ServerCommandSource>, SuggestionProvider<ServerCommandSource> {
    public static CommandFabric instance = new CommandFabric();

    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(((CommandManager.literal("music").requires(this)).executes(this))
                .then(CommandManager.argument("args", StringArgumentType.greedyString())
                        .suggests(this).executes(this)));
    }

    @Override
    public int run(CommandContext<ServerCommandSource> context) {
        var item = context.getSource();

        var input = context.getInput();
        var temp = input.split(" ");
        var arg = new String[temp.length - 1];
        System.arraycopy(temp, 1, arg, 0, arg.length);

        CommandEX.execute(item, context.getSource().getName(), arg);

        return 0;
    }

    @Override
    public boolean test(ServerCommandSource stack) {
        return true;
    }

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
        var item = context.getSource();

        var input = context.getInput();
        var temp = input.split(" ");
        var arg = new String[input.endsWith(" ") ? temp.length : temp.length - 1];
        System.arraycopy(temp, 1, arg, 0, temp.length - 1);

        builder = builder.createOffset(builder.getInput().lastIndexOf(32) + 1);

        List<String> results = CommandEX.getTabList(item, item.getName(), arg);

        for (String s : results) {
            builder.suggest(s);
        }

        return builder.buildFuture();
    }
}