package de.goldmensch.chunkprotect.message;

import de.goldmensch.smartutils.localizer.Replacement;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

public interface IMessenger {
    String PLAYER_LITERAL = "player";

    void send(CommandSender receiver, String key, Replacement... replacements);
    Component prepare(String key, Replacement... replacements);
}
