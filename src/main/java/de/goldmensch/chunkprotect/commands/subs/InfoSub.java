package de.goldmensch.chunkprotect.commands.subs;

import de.goldmensch.chunkprotect.commands.ChunkProtectCommand;
import de.goldmensch.chunkprotect.commands.ChunkProtectSubCommand;
import de.goldmensch.chunkprotect.core.ChunkProtect;
import de.goldmensch.chunkprotect.core.chunk.ClaimableChunk;
import de.goldmensch.chunkprotect.core.chunk.ClaimedChunk;
import de.goldmensch.chunkprotect.core.holder.ChunkHolder;
import de.goldmensch.chunkprotect.utils.ChunkUtil;
import de.goldmensch.chunkprotect.utils.message.MessageBuilder;
import de.goldmensch.commanddispatcher.ExecutorLevel;
import de.goldmensch.smartutils.localizer.Replacement;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class InfoSub extends ChunkProtectSubCommand {

    public InfoSub(ChunkProtect chunkProtect, ChunkProtectCommand command) {
        super(ExecutorLevel.PLAYER, chunkProtect.getPermission("info"), chunkProtect, command);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(args[0]);
            if (player != null) {
                CompletableFuture.supplyAsync(() -> {
                    ChunkHolder holder = getDataService().holderFromUUID(player.getUniqueId());
                    return new InfoValue(holder, holder.getTrustedAllChunks().stream().map(uuid ->
                            getDataService().holderFromUUID(uuid).getName()).collect(Collectors.toSet()));
                }).thenAccept(infoValue -> {
                    ChunkHolder holder = infoValue.getHolder();
                    sender.sendMessage(MessageBuilder.builder()
                            .appendLine(getMessenger()
                                    .prepare("player-info-name", Replacement.create("name", holder.getName())))
                            .appendLine(getMessenger()
                                    .prepare("player-info-uuid", Replacement.create("uuid", holder.getUuid().toString())))
                            .appendLine(getMessenger()
                                    .prepare("player-info-claimamount", Replacement.create("amount", String.valueOf(holder.getClaimAmount()))))
                            .appendLine(getMessenger()
                                    .prepare("player-info-on-all-trusted", Replacement.create("names", infoValue.getTrusted().toString())))
                            .build());
                });
            } else {
                getMessenger().send(sender, "player-not-found", Replacement.create("player", args[0]));
            }
            return true;
        }

        ClaimableChunk claimableChunk = ChunkUtil.chunkFromSenderUnsafe(sender, getDataService());
        if (claimableChunk.isClaimed()) {
            ClaimedChunk chunk = claimableChunk.getChunk();

            CompletableFuture.supplyAsync(() ->
                            chunk.getTrustedPlayer().stream().map(uuid ->
                                    getDataService().holderFromUUID(uuid).getName()).collect(Collectors.toSet()),
                    getChunkProtect().getService()).thenAccept(chunkHolders ->
                    sender.sendMessage(MessageBuilder.builder()
                            .appendLine(getMessenger()
                                    .prepare("chunk-info-location",
                                            Replacement.create("x", String.valueOf(chunk.getLocation().getX())),
                                            Replacement.create("z", String.valueOf(chunk.getLocation().getZ())),
                                            Replacement.create("world", chunk.getLocation().getWorld())
                                    ))
                            .appendLine(getMessenger()
                                    .prepare("chunk-info-holder", Replacement.create("holder", chunk.getHolder().getName())))
                            .appendLine(getMessenger()
                                    .prepare("chunk-info-trusted", Replacement.create("playerlist", chunkHolders.toString())))
                            .build())
            );
        } else {
            getMessenger().send(sender, "chunk-not-claimed");
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return null;
    }

    private static class InfoValue {
        private final ChunkHolder holder;
        private final Set<String> trusted;

        InfoValue(ChunkHolder holder, Set<String> trusted) {
            this.holder = holder;
            this.trusted = trusted;
        }

        public ChunkHolder getHolder() {
            return holder;
        }

        public Set<String> getTrusted() {
            return trusted;
        }
    }
}
