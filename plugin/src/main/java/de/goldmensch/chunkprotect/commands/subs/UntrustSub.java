package de.goldmensch.chunkprotect.commands.subs;

import de.goldmensch.chunkprotect.commands.ChunkProtectCommand;
import de.goldmensch.chunkprotect.commands.ChunkProtectSubCommand;
import de.goldmensch.chunkprotect.ChunkProtectPlugin;
import de.goldmensch.chunkprotect.core.chunk.ClaimableChunk;
import de.goldmensch.chunkprotect.core.chunk.ClaimedChunk;
import de.goldmensch.chunkprotect.core.holder.ChunkHolder;
import de.goldmensch.chunkprotect.message.Messenger;
import de.goldmensch.chunkprotect.Chunks;
import de.goldmensch.commanddispatcher.ExecutorLevel;
import de.goldmensch.smartutils.localizer.Replacement;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public class UntrustSub extends ChunkProtectSubCommand {

    public UntrustSub(ChunkProtectPlugin chunkProtectPlugin, ChunkProtectCommand command) {
        super(ExecutorLevel.PLAYER, chunkProtectPlugin.getPermission("untrust"), chunkProtectPlugin, command);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if ((args.length == 0) || (args.length > 2)) {
            getChunkProtectCommand().sendHelp(sender);
            return true;
        }
        Player player = (Player) sender;
        OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(args[0]);
        if (target == null) {
            getMessenger().send(sender, "player-not-found", Replacement.create(Messenger.PLAYER_LITERAL, args[0]));
            return true;
        }

        if ((args.length == 2) && "this".equalsIgnoreCase(args[1])) {
            ClaimableChunk claimableChunk = Chunks.chunkFromPlayer(player, getDataService());
            if (Chunks.isClaimedAndHolder(claimableChunk, player, getChunkProtect())) return true;
            ClaimedChunk claimedChunk = claimableChunk.getChunk();

            if (claimedChunk.getTrustedPlayer().remove(target.getUniqueId())) {
                getMessenger().send(sender, "player-untrusted", Replacement.create(Messenger.PLAYER_LITERAL, target.getName()));
            } else {
                getMessenger().send(sender, "player-not-trusted", Replacement.create(Messenger.PLAYER_LITERAL, target.getName()));
            }
            return true;
        }

        ChunkHolder holder = getDataService().holderFromUUID(player.getUniqueId());
        if (holder.getTrustedAllChunks().remove(target.getUniqueId())) {
            getMessenger().send(player, "player-untrusted-all", Replacement.create(Messenger.PLAYER_LITERAL, target.getName()));
        } else {
            getMessenger().send(player, "player-not-trusted-all", Replacement.create(Messenger.PLAYER_LITERAL, target.getName()));
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        ClaimableChunk chunk = Chunks.chunkFromSenderUnsafe(sender, getDataService());
        if (args.length == 1 && chunk.isClaimed()) {
            return chunk.getChunk().getTrustedPlayer().stream()
                    .map(uuid -> getDataService().holderFromUUID(uuid).getName())
                    .filter(s -> s.startsWith(args[0]))
                    .collect(Collectors.toList());
        }
        return null;
    }
}