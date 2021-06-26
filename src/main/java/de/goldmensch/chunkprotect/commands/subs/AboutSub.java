package de.goldmensch.chunkprotect.commands.subs;

import de.goldmensch.chunkprotect.commands.ChunkProtectSubCommand;
import de.goldmensch.chunkprotect.core.ChunkProtect;
import de.goldmensch.chunkprotect.utils.message.MessageBuilder;
import de.goldmensch.chunkprotect.utils.message.MessageUtils;
import de.goldmensch.commanddispatcher.ExecutorLevel;
import de.goldmensch.smartutils.localizer.Replacement;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AboutSub extends ChunkProtectSubCommand {

    private final Component message;

    private AboutSub(ExecutorLevel executorLevel, String permission, ChunkProtect chunkProtect, Component message) {
        super(executorLevel, permission, chunkProtect);
        this.message = message;
    }

    public static AboutSub newAboutSub(ChunkProtect chunkProtect) {
        return new AboutSub(ExecutorLevel.CONSOLE_PLAYER, "", chunkProtect, MessageBuilder.builder()
                .appendLine(chunkProtect.getMessenger()
                        .prepare("about-name", Replacement.create("name", chunkProtect.getName())))
                .appendLine(chunkProtect.getMessenger()
                        .prepare("about-version", Replacement.create("version", chunkProtect.getDescription().getVersion())))
                .appendLine(chunkProtect.getMessenger()
                        .prepare("about-authors", Replacement.create("authors",
                                MessageUtils.formatList(chunkProtect.getDescription().getAuthors()))))
                .build()
        );
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        sender.sendMessage(message);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return null;
    }
}
