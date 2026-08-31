/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package meteordevelopment.meteorclient.commands.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.command.CommandSource;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IpCommand extends Command {
    public IpCommand() {
        super("ip", "Shows the IP address of the server you're currently connected to.");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            ServerInfo server = mc.getCurrentServerEntry();

            if (server == null) {
                info("You are not connected to a server.");
                return SINGLE_SUCCESS;
            }

            String host = server.address.contains(":") ? server.address.substring(0, server.address.indexOf(':')) : server.address;
            String resolved = null;

            try {
                resolved = InetAddress.getByName(host).getHostAddress();
            } catch (UnknownHostException ignored) {}

            if (resolved == null || resolved.equals(host)) info("Server IP: %s", server.address);
            else info("Server IP: %s (%s)", server.address, resolved);

            return SINGLE_SUCCESS;
        });
    }
}
