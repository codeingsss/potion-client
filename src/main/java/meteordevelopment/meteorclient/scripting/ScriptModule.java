/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package meteordevelopment.meteorclient.scripting;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;

public class ScriptModule extends Module {
    private final Runnable func;
    private boolean erroredOut;

    public ScriptModule(Category category, String name, Runnable func) {
        super(category, name, "Potion addon module.");
        this.func = func;
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (erroredOut) return;

        try {
            func.run();
        } catch (Throwable e) {
            // Throwable, not Exception: a native addon's callback can throw UnsatisfiedLinkError
            // (e.g. it never bound invokeTick via RegisterNatives) or another Error subtype, and
            // those must not escape into the tick event bus and take other modules down with them.
            erroredOut = true;
            ChatUtils.error("Potion script error in '%s': %s", title, e.getMessage());
            toggle();
        }
    }
}
