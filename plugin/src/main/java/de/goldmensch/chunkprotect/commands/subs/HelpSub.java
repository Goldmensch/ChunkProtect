package de.goldmensch.chunkprotect.commands.subs;

import de.goldmensch.chunkprotect.commands.ChunkProtectCommand;
import de.goldmensch.chunkprotect.commands.ChunkProtectSubCommand;
import de.goldmensch.chunkprotect.ChunkProtectPlugin;
import de.goldmensch.commanddispatcher.ExecutorLevel;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HelpSub extends ChunkProtectSubCommand {

    public HelpSub(ChunkProtectPlugin chunkProtectPlugin, ChunkProtectCommand chunkProtectCommand) {
        super(ExecutorLevel.CONSOLE_PLAYER, chunkProtectPlugin.getPermission("help"), chunkProtectPlugin, chunkProtectCommand);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        getChunkProtectCommand().sendHelp(sender);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return null;
    }
}