/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package meteordevelopment.meteorclient.scripting;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.command.CommandSource;

public class ScriptCommand extends Command {
    private final Runnable func;

    public ScriptCommand(String name, Runnable func) {
        super(name, "Potion addon command.");
        this.func = func;
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            try {
                func.run();
            } catch (Exception e) {
                error("Script error: %s", e.getMessage());
            }

            return SINGLE_SUCCESS;
        });
    }
}
