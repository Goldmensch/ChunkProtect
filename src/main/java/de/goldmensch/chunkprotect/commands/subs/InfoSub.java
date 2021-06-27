package de.goldmensch.chunkprotect.commands.subs;

import de.goldmensch.chunkprotect.commands.ChunkProtectSubCommand;
import de.goldmensch.chunkprotect.core.ChunkProtect;
import de.goldmensch.chunkprotect.core.chunk.ChunkLocation;
import de.goldmensch.chunkprotect.core.chunk.ClaimableChunk;
import de.goldmensch.chunkprotect.core.chunk.ClaimedChunk;
import de.goldmensch.chunkprotect.core.chunk.util.ChunkUtil;
import de.goldmensch.chunkprotect.utils.message.MessageBuilder;
import de.goldmensch.commanddispatcher.ExecutorLevel;
import de.goldmensch.smartutils.localizer.Replacement;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InfoSub extends ChunkProtectSubCommand {

    public InfoSub(ChunkProtect chunkProtect) {
        super(ExecutorLevel.PLAYER, chunkProtect.getPermission("info"), chunkProtect);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        ClaimableChunk claimableChunk = ChunkUtil.chunkFromSenderUnsafe(sender, getDataService());
        if(claimableChunk.isClaimed()) {
            ClaimedChunk chunk = claimableChunk.getChunk();

            String x = String.valueOf(chunk.getLocation().getX());
            String z = String.valueOf(chunk.getLocation().getZ());

            sender.sendMessage(MessageBuilder.builder()
                    .appendLine(getMessenger()
                            .prepare("chunk-info-location", Replacement.create("x", x), Replacement.create("z", z)))
                    .appendLine(getMessenger()
                            .prepare("chunk-info-holder", Replacement.create("holder", chunk.getHolder().getName())))
                    .build());
        }else {
            getMessenger().send(sender, "chunk-not-claimed");
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return null;
    }
}
