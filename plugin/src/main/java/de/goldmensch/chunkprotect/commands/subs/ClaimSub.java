package de.goldmensch.chunkprotect.commands.subs;

import de.goldmensch.chunkprotect.Status;
import de.goldmensch.chunkprotect.Borders;
import de.goldmensch.chunkprotect.ChunkLocation;
import de.goldmensch.chunkprotect.ChunkProtectPlugin;
import de.goldmensch.chunkprotect.commands.ChunkProtectCommand;
import de.goldmensch.chunkprotect.commands.ChunkProtectSubCommand;
import de.goldmensch.commanddispatcher.ExecutorLevel;
import de.goldmensch.smartutils.localizer.Replacement;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ClaimSub extends ChunkProtectSubCommand {

    public ClaimSub(ChunkProtectPlugin chunkProtectPlugin, ChunkProtectCommand command) {
        super(ExecutorLevel.PLAYER, chunkProtectPlugin.getPermission("claim"), chunkProtectPlugin, command);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = toPlayer(sender);
        Chunk chunk = player.getChunk();

        ChunkLocation location = ChunkLocation.fromChunk(chunk);
        Status status = getDataService().claimChunk(location, player.getUniqueId());
        if(status == Status.CANCELLED) return true;
        if (status == Status.POSITIVE) {
            getMessenger().send(sender, "chunk-claimed");
            Borders.showChunkBorders(player, getChunkProtect());
        } else {
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
