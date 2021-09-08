package de.goldmensch.chunkprotect.commands.subs;

import de.goldmensch.chunkprotect.ChunkProtectPlugin;
import de.goldmensch.chunkprotect.commands.ChunkProtectCommand;
import de.goldmensch.chunkprotect.commands.ChunkProtectSubCommand;
import de.goldmensch.chunkprotect.core.holder.ChunkHolder;
import de.goldmensch.commanddispatcher.ExecutorLevel;
import de.goldmensch.smartutils.localizer.Replacement;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UnclaimAllSub extends ChunkProtectSubCommand {

  public UnclaimAllSub(ChunkProtectPlugin chunkProtectPlugin, ChunkProtectCommand command) {
    super(ExecutorLevel.PLAYER, chunkProtectPlugin.getPermission("unclaimAll"), chunkProtectPlugin,
        command);
  }

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                           @NotNull String label, @NotNull String[] args) {
    CompletableFuture.supplyAsync(() -> {
      ChunkHolder holder = getDataService().holderFromUUID(toPlayer(sender).getUniqueId());
      int originalClaims = holder.getClaimAmount();
      holder.getClaimedChunks().forEach(location -> {
        getDataService().loadChunkIfUnloaded(location);
        getDataService().unclaimChunk(location);
      });
      return originalClaims;
    }).thenAccept(unclaims ->
        getMessenger().send(sender, "chunk.allUnclaimed",
            Replacement.create("amount", String.valueOf(unclaims))));
    return true;
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender sender,
                                              @NotNull Command command, @NotNull String alias,
                                              @NotNull String[] args) {
    return null;
  }
}
