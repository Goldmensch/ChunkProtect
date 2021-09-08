package de.goldmensch.chunkprotect.commands.subs.staff;

import de.goldmensch.chunkprotect.ChunkLocation;
import de.goldmensch.chunkprotect.ChunkProtectPlugin;
import de.goldmensch.chunkprotect.Status;
import de.goldmensch.chunkprotect.commands.ChunkProtectCommand;
import de.goldmensch.chunkprotect.commands.ChunkProtectSubCommand;
import de.goldmensch.commanddispatcher.ExecutorLevel;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StaffUnclaimSub extends ChunkProtectSubCommand {

  public StaffUnclaimSub(ChunkProtectPlugin chunkProtectPlugin, ChunkProtectCommand command) {
    super(ExecutorLevel.PLAYER, chunkProtectPlugin.getPermission("staff", "unclaim"),
        chunkProtectPlugin, command);
  }

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                           @NotNull String label, @NotNull String[] args) {
    Player player = toPlayer(sender);
    Status status = getDataService().unclaimChunk(ChunkLocation.fromChunk(player.getChunk()));
    if (status == Status.CANCELLED) {
      return true;
    }
    if (status == Status.POSITIVE) {
      getMessenger().send(sender, "chunk.unclaimed");
    } else {
      getMessenger().send(sender, "chunk.notClaimed");
    }
    return true;
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender sender,
                                              @NotNull Command command, @NotNull String alias,
                                              @NotNull String[] args) {
    return null;
  }
}
