package de.goldmensch.chunkprotect.commands.subs;

import de.goldmensch.chunkprotect.commands.ChunkProtectSubCommand;
import de.goldmensch.chunkprotect.commands.util.CmdUtil;
import de.goldmensch.chunkprotect.core.ChunkProtect;
import de.goldmensch.chunkprotect.core.chunk.ClaimableChunk;
import de.goldmensch.chunkprotect.core.chunk.ClaimedChunk;
import de.goldmensch.chunkprotect.core.chunk.util.ChunkUtil;
import de.goldmensch.commanddispatcher.ExecutorLevel;
import de.goldmensch.smartutils.localizer.Replacement;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class UnclaimSub extends ChunkProtectSubCommand {

    public UnclaimSub(ChunkProtect chunkProtect) {
        super(ExecutorLevel.PLAYER, chunkProtect.getPermission("unclaim"), chunkProtect);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = toPlayer(sender);

        ClaimableChunk claimableChunk = ChunkUtil.chunkFromSenderUnsafe(sender, getDataService());

        if(CmdUtil.isClaimedAndHolder(claimableChunk, player, getChunkProtect())) return true;
        ClaimedChunk claimedChunk = claimableChunk.getChunk();

        getDataService().unclaimChunk(claimedChunk.getLocation());
        getMessenger().send(sender, "chunk-unclaimed");
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return null;
    }
}
