package de.goldmensch.chunkprotect.commands.subs.staff;

import de.goldmensch.chunkprotect.ChunkProtectPlugin;
import de.goldmensch.chunkprotect.commands.ChunkProtectCommand;
import de.goldmensch.chunkprotect.commands.ChunkProtectSubCommand;
import de.goldmensch.chunkprotect.core.holder.ChunkHolder;
import de.goldmensch.commanddispatcher.ExecutorLevel;
import de.goldmensch.smartutils.localizer.Replacement;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StaffUnclaimAllSub extends ChunkProtectSubCommand {

  public StaffUnclaimAllSub(ChunkProtectPlugin chunkProtectPlugin, ChunkProtectCommand command) {
    super(ExecutorLevel.CONSOLE_PLAYER, chunkProtectPlugin.getPermission("staff", "unclaimAll"),
        chunkProtectPlugin, command);
  }

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                           @NotNull String label, @NotNull String[] args) {
    if (args.length != 1) {
      getChunkProtectCommand().sendHelp(sender);
      return true;
    }

    OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(args[0]);
    if (target == null) {
      getMessenger().send(sender, "player.notFound", Replacement.create("player", args[0]));
      return true;
    }

    CompletableFuture.supplyAsync(() -> {
      ChunkHolder holder = getDataService().holderFromUUID(target.getUniqueId());
      int originalClaims = holder.getClaimAmount();
      holder.getClaimedChunks().forEach(location -> {
        getDataService().loadChunkIfUnloaded(location);
        getDataService().unclaimChunk(location);
      });
      return originalClaims;
    }).thenAccept(unclaims ->
        getMessenger().send(sender, "stuff.allUnclaimed",
            Replacement.create("amount", String.valueOf(unclaims)),
            Replacement.create("player", target.getName())));
    return true;
  }

  @Override
  public @Nullable List<String> onTabComplete(@NotNull CommandSender sender,
                                              @NotNull Command command, @NotNull String alias,
                                              @NotNull String[] args) {
    if (args.length == 1) {
      return Arrays.stream(Bukkit.getOfflinePlayers())
          .map(OfflinePlayer::getName)
          .filter(Objects::nonNull)
          .filter(name -> name.startsWith(args[0]))
          .collect(Collectors.toList());
    }
    return null;
  }
}
