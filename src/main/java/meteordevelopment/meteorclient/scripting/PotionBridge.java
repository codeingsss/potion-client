/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package meteordevelopment.meteorclient.scripting;

import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.commands.Commands;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.meteorclient.utils.world.TickRate;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.item.Items;

import java.util.function.Consumer;

import static meteordevelopment.meteorclient.MeteorClient.mc;

/**
 * Exposed to Potion addon scripts (BeanShell, "*.bsh") as the "po" variable.
 */
public class PotionBridge {
    public void set(String category, String name, Runnable func) {
        if (name == null || name.isBlank()) {
            ChatUtils.error("Potion addon error: 'name' is required.");
            return;
        }

        if (func == null) {
            ChatUtils.error("Potion addon error: 'func' must be a function.");
            return;
        }

        Modules.get().add(ScriptModulePool.create(findOrCreateCategory(category), name, func));
    }

    public void command(String cmd, Runnable func) {
        if (cmd == null || cmd.isBlank()) {
            ChatUtils.error("Potion addon error: 'cmd' is required.");
            return;
        }

        if (func == null) {
            ChatUtils.error("Potion addon error: 'func' must be a function.");
            return;
        }

        Commands.add(new ScriptCommand(cmd, func));
    }

    /**
     * Like command(), but the handler receives the trailing text after the command name as a
     * String argument (empty string if none was given), e.g. ".say hello world" -> "hello world".
     */
    public void commandWithArgs(String cmd, Consumer<String> func) {
        if (cmd == null || cmd.isBlank()) {
            ChatUtils.error("Potion addon error: 'cmd' is required.");
            return;
        }

        if (func == null) {
            ChatUtils.error("Potion addon error: 'func' must be a function.");
            return;
        }

        Commands.add(new ScriptArgCommand(cmd, func));
    }

    public Object java(String code) {
        return JavaEval.run(code);
    }

    public void chat(String message) {
        ChatUtils.info("%s", message);
    }

    public void error(String message) {
        ChatUtils.error("%s", message);
    }

    public void log(String message) {
        MeteorClient.LOG.info(message);
    }

    public String ip() {
        ServerInfo server = mc.getCurrentServerEntry();
        return server == null ? "singleplayer" : server.address;
    }

    public boolean inSingleplayer() {
        return mc.isIntegratedServerRunning();
    }

    public String username() {
        return mc.player == null ? null : mc.player.getName().getString();
    }

    public Float health() {
        return mc.player == null ? null : mc.player.getHealth();
    }

    public Float maxHealth() {
        return mc.player == null ? null : mc.player.getMaxHealth();
    }

    public Integer hunger() {
        return mc.player == null ? null : mc.player.getHungerManager().getFoodLevel();
    }

    public Double x() {
        return mc.player == null ? null : mc.player.getX();
    }

    public Double y() {
        return mc.player == null ? null : mc.player.getY();
    }

    public Double z() {
        return mc.player == null ? null : mc.player.getZ();
    }

    public Float yaw() {
        return mc.player == null ? null : mc.player.getYaw();
    }

    public Float pitch() {
        return mc.player == null ? null : mc.player.getPitch();
    }

    public String gamemode() {
        return mc.interactionManager == null ? null : mc.interactionManager.getCurrentGameMode().getId();
    }

    public String dimension() {
        return mc.world == null ? null : mc.world.getRegistryKey().getValue().toString();
    }

    public Integer ping() {
        if (mc.player == null || mc.getNetworkHandler() == null) return null;
        PlayerListEntry entry = mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid());
        return entry == null ? null : entry.getLatency();
    }

    public int fps() {
        return mc.getCurrentFps();
    }

    public double tps() {
        return TickRate.INSTANCE.getTickRate();
    }

    public Boolean isSneaking() {
        return mc.player == null ? null : mc.player.isSneaking();
    }

    public Boolean isSprinting() {
        return mc.player == null ? null : mc.player.isSprinting();
    }

    public Boolean isOnGround() {
        return mc.player == null ? null : mc.player.isOnGround();
    }

    public Boolean isSubmergedInWater() {
        return mc.player == null ? null : mc.player.isSubmergedInWater();
    }

    public Integer experienceLevel() {
        return mc.player == null ? null : mc.player.experienceLevel;
    }

    public String heldItem() {
        return mc.player == null ? null : mc.player.getMainHandStack().getItem().toString();
    }

    public Long worldTime() {
        return mc.world == null ? null : mc.world.getTimeOfDay();
    }

    public Boolean isRaining() {
        return mc.world == null ? null : mc.world.isRaining();
    }

    public Boolean isThundering() {
        return mc.world == null ? null : mc.world.isThundering();
    }

    public String difficulty() {
        return mc.world == null ? null : mc.world.getDifficulty().asString();
    }

    // Module control (works on both built-in and other script-registered modules)

    public boolean isModuleActive(String name) {
        Module module = Modules.get().get(name);
        return module != null && module.isActive();
    }

    public void toggleModule(String name) {
        Module module = Modules.get().get(name);
        if (module == null) {
            ChatUtils.error("Potion addon error: no module named '%s'.", name);
            return;
        }
        module.toggle();
    }

    public void enableModule(String name) {
        Module module = Modules.get().get(name);
        if (module == null) {
            ChatUtils.error("Potion addon error: no module named '%s'.", name);
            return;
        }
        if (!module.isActive()) module.toggle();
    }

    public void disableModule(String name) {
        Module module = Modules.get().get(name);
        if (module == null) {
            ChatUtils.error("Potion addon error: no module named '%s'.", name);
            return;
        }
        if (module.isActive()) module.toggle();
    }

    private Category findOrCreateCategory(String name) {
        if (name == null || name.isBlank()) return Categories.Potion;

        for (Category category : Modules.loopCategories()) {
            if (category.name.equalsIgnoreCase(name)) return category;
        }

        Category category = new Category(name, Items.SPLASH_POTION.getDefaultStack());

        Categories.REGISTERING = true;
        try {
            Modules.registerCategory(category);
        } finally {
            Categories.REGISTERING = false;
        }

        return category;
    }
}
