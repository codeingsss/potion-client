/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package meteordevelopment.meteorclient.systems.modules.misc;

import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import net.minecraft.client.network.ServerInfo;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IpModule extends Module {
    public IpModule() {
        super(Categories.Potion, "ip", "Shows the IP address of the server you're currently connected to.");
    }

    @Override
    public void onActivate() {
        ServerInfo server = mc.getCurrentServerEntry();

        if (server == null) {
            ChatUtils.info("You are not connected to a server.");
            return;
        }

        String host = server.address.contains(":") ? server.address.substring(0, server.address.indexOf(':')) : server.address;
        String resolved = null;

        try {
            resolved = InetAddress.getByName(host).getHostAddress();
        } catch (UnknownHostException ignored) {}

        if (resolved == null || resolved.equals(host)) ChatUtils.info("Server IP: %s", server.address);
        else ChatUtils.info("Server IP: %s (%s)", server.address, resolved);
    }
}
