/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package meteordevelopment.meteorclient.scripting;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.command.CommandSource;

import java.util.function.Consumer;

/**
 * A ".<cmd> [args]" command whose handler receives the raw trailing text as a String
 * (empty string if none was given).
 */
public class ScriptArgCommand extends Command {
    private final Consumer<String> func;

    public ScriptArgCommand(String name, Consumer<String> func) {
        super(name, "Potion addon command.");
        this.func = func;
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            run("");
            return SINGLE_SUCCESS;
        });

        builder.then(argument("args", StringArgumentType.greedyString()).executes(context -> {
            run(StringArgumentType.getString(context, "args"));
            return SINGLE_SUCCESS;
        }));
    }

    private void run(String args) {
        try {
            func.accept(args);
        } catch (Exception e) {
            error("Script error: %s", e.getMessage());
        }
    }
}
