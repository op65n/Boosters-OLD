package com.splicegames.sgboosters.message;

import me.mattstudios.mfmsg.base.MessageOptions;
import me.mattstudios.mfmsg.base.internal.Format;
import me.mattstudios.mfmsg.bukkit.BukkitMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public final class Message {

    private static final BukkitMessage BUKKIT_MESSAGE = BukkitMessage.create(MessageOptions.builder().removeFormat(Format.ITALIC).build());

    public static void send(final CommandSender player, final List<String> text) {
        if (!(player instanceof Player)) return;

        text.stream().map(BUKKIT_MESSAGE::parse).forEach(it -> it.sendMessage((Player) player));
    }

}
