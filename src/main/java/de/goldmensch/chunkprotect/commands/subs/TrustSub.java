package de.goldmensch.chunkprotect.commands.subs;

import de.goldmensch.chunkprotect.commands.ChunkProtectSubCommand;
import de.goldmensch.chunkprotect.commands.util.CmdUtil;
import de.goldmensch.chunkprotect.core.ChunkProtect;
import de.goldmensch.chunkprotect.core.chunk.ClaimableChunk;
import de.goldmensch.chunkprotect.core.chunk.ClaimedChunk;
import de.goldmensch.chunkprotect.core.chunk.util.ChunkUtil;
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
    public TrustSub(ChunkProtect chunkProtect) {
        super(ExecutorLevel.PLAYER, chunkProtect.getPermission("trust"), chunkProtect);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length != 1) {
            sender.sendMessage("TODO: help");
            return true;
        }
        Player player = (Player)sender;
        ClaimableChunk claimableChunk = ChunkUtil.chunkFromPlayer(player, getDataService());

        if(CmdUtil.isClaimedAndHolder(claimableChunk, player, getChunkProtect())) return true;
        ClaimedChunk claimedChunk = claimableChunk.getChunk();
        Player target = Bukkit.getPlayer(args[0]);
        if(target == null) {
            getMessenger().send(sender, "player-offline", Replacement.create("player", args[0]));
            return true;
        }

        if(claimedChunk.getTrustedPlayer().add(player.getUniqueId())) {
            getMessenger().send(sender, "player-trusted", Replacement.create("player", target.getName()));
            getDataService().updateChunk(claimedChunk);
        }else {
            getMessenger().send(sender, "player-already-trusted", Replacement.create("player", target.getName()));
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(args.length == 1) {
            ClaimableChunk chunk = ChunkUtil.chunkFromSenderUnsafe(sender, getDataService());
            if(chunk.isClaimed()) {
                ClaimedChunk claimedChunk = chunk.getChunk();
                return Bukkit.getOnlinePlayers().stream()
                        .filter(player -> !claimedChunk.getTrustedPlayer().contains(player.getUniqueId()))
                        .map(HumanEntity::getName)
                        .filter(s -> s.startsWith(args[0]))
                        .collect(Collectors.toList());
            }
        }
        return null;
    }
}
