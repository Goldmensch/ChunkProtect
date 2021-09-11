package de.goldmensch.chunkprotect.commands.subs;

import de.goldmensch.chunkprotect.ChunkProtectPlugin;
import de.goldmensch.chunkprotect.commands.ChunkProtectCommand;
import de.goldmensch.chunkprotect.commands.ChunkProtectSubCommand;
import de.goldmensch.chunkprotect.message.Messages;
import de.goldmensch.commanddispatcher.ExecutorLevel;
import de.goldmensch.smartutils.localizer.Replacement;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class AboutSub extends ChunkProtectSubCommand {

  private final Component message;

  private AboutSub(ChunkProtectPlugin chunkProtectPlugin, ChunkProtectCommand chunkProtectCommand,
                   Component message) {
    super(ExecutorLevel.CONSOLE_PLAYER, "", chunkProtectPlugin, chunkProtectCommand);
    this.message = message;
  }

  public static AboutSub newAboutSub(ChunkProtectPlugin chunkProtectPlugin,
                                     ChunkProtectCommand command) {
    return new AboutSub(chunkProtectPlugin, command, Messages.builder()
        .appendLine(chunkProtectPlugin.getMessenger()
            .prepare("about.name", Replacement.create("name", chunkProtectPlugin.getName())))
        .appendLine(chunkProtectPlugin.getMessenger()
            .prepare("about.version",
                Replacement.create("version", chunkProtectPlugin.getDescription()
                    .getVersion())))
        .appendLine(chunkProtectPlugin.getMessenger()
            .prepare("about.authors", Replacement.create("authors",
                Messages.formatList(chunkProtectPlugin.getDescription().getAuthors()))))
        .build()
    );
  }

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                           @NotNull String label, @NotNull String[] args) {
    sender.sendMessage(message);
    return true;
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender sender,
                                              @NotNull Command command, @NotNull String alias,
                                              @NotNull String[] args) {
    return null;
  }
}
