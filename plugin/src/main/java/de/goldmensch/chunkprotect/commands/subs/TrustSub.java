package de.goldmensch.chunkprotect.commands.subs;

import com.google.common.collect.Lists;
import de.goldmensch.chunkprotect.ChunkProtectPlugin;
import de.goldmensch.chunkprotect.commands.ChunkProtectCommand;
import de.goldmensch.chunkprotect.commands.ChunkProtectSubCommand;
import de.goldmensch.chunkprotect.core.chunk.ClaimableChunk;
import de.goldmensch.chunkprotect.core.chunk.ClaimedChunk;
import de.goldmensch.chunkprotect.core.holder.ChunkHolder;
import de.goldmensch.chunkprotect.message.IMessenger;
import de.goldmensch.chunkprotect.Chunks;
import de.goldmensch.commanddispatcher.ExecutorLevel;
import de.goldmensch.smartutils.localizer.Replacement;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public class TrustSub extends ChunkProtectSubCommand {
    public TrustSub(ChunkProtectPlugin chunkProtectPlugin, ChunkProtectCommand command) {
        super(ExecutorLevel.PLAYER, chunkProtectPlugin.getPermission("trust"), chunkProtectPlugin, command);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if ((args.length == 0) || (args.length > 2)) {
            getChunkProtectCommand().sendHelp(sender);
            return true;
        }
        Player player = (Player) sender;
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            getMessenger().send(sender, "player-offline", Replacement.create(IMessenger.PLAYER_LITERAL, args[0]));
            return true;
        }

        if ((args.length == 2) && "this".equalsIgnoreCase(args[1])) {
            ClaimableChunk claimableChunk = Chunks.chunkFromPlayer(player, getDataService());
            if (Chunks.isClaimedAndHolder(claimableChunk, player, getChunkProtect())) return true;
            ClaimedChunk claimedChunk = claimableChunk.getChunk();

            if (claimedChunk.getTrustedPlayer().add(target.getUniqueId())) {
                getMessenger().send(sender, "player-trusted", Replacement.create(IMessenger.PLAYER_LITERAL, target.getName()));
            } else {
                getMessenger().send(sender, "player-already-trusted", Replacement.create(IMessenger.PLAYER_LITERAL, target.getName()));
            }
            return true;
        }

        ChunkHolder holder = getDataService().holderFromUUID(player.getUniqueId());
        if (holder.getTrustedAllChunks().add(target.getUniqueId())) {
            getMessenger().send(sender, "player-trusted-on-all", Replacement.create(IMessenger.PLAYER_LITERAL, target.getName()));
        } else {
            getMessenger().send(sender, "player-already-trusted-all", Replacement.create(IMessenger.PLAYER_LITERAL, target.getName()));
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            ClaimableChunk chunk = Chunks.chunkFromSenderUnsafe(sender, getDataService());
            if (chunk.isClaimed()) {
                return Bukkit.getOnlinePlayers().stream()
                        .map(HumanEntity::getName)
                        .filter(s -> s.startsWith(args[0]))
                        .collect(Collectors.toList());
            }
        }

        if (args.length == 2 && "this".startsWith(args[1])) {
            return Lists.newArrayList("this");
        }
        return null;
    }
}
