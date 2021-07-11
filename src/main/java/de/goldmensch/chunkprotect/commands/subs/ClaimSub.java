package de.goldmensch.chunkprotect.commands.subs;

import de.goldmensch.chunkprotect.commands.ChunkProtectCommand;
import de.goldmensch.chunkprotect.commands.ChunkProtectSubCommand;
import de.goldmensch.chunkprotect.core.ChunkProtect;
import de.goldmensch.chunkprotect.core.chunk.ChunkLocation;
import de.goldmensch.chunkprotect.utils.Util;
import de.goldmensch.commanddispatcher.ExecutorLevel;
import de.goldmensch.smartutils.localizer.Replacement;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ClaimSub extends ChunkProtectSubCommand {

    public ClaimSub(ChunkProtect chunkProtect, ChunkProtectCommand command) {
        super(ExecutorLevel.PLAYER, chunkProtect.getPermission("claim"), chunkProtect, command);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = toPlayer(sender);
        Chunk chunk = player.getChunk();

        ChunkLocation location = ChunkLocation.fromChunk(chunk);
        if(getDataService().claimChunk(location, player.getUniqueId())) {
            getMessenger().send(sender, "chunk-claimed");
            Util.showChunkBorders(player, getChunkProtect());
        }else {
            getMessenger().send(sender, "chunk-already-claimed", Replacement.create("holder",
                    getDataService().getChunkAt(location).getChunk().getHolder().getName()));
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return null;
    }
}
