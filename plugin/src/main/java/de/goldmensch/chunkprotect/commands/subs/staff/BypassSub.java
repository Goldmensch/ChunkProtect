package de.goldmensch.chunkprotect.commands.subs.staff;

import de.goldmensch.chunkprotect.commands.ChunkProtectCommand;
import de.goldmensch.chunkprotect.commands.ChunkProtectSubCommand;
import de.goldmensch.chunkprotect.ChunkProtectPlugin;
import de.goldmensch.commanddispatcher.ExecutorLevel;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BypassSub extends ChunkProtectSubCommand {

    public BypassSub(ChunkProtectPlugin chunkProtectPlugin, ChunkProtectCommand command) {
        super(ExecutorLevel.PLAYER, chunkProtectPlugin.getPermission("stuff", "bypass"), chunkProtectPlugin, command);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (getChunkProtect().getProtectionBypass().toggle(((Player) sender).getUniqueId())) {
            getMessenger().send(sender, "stuff.bypass.enable");
        } else {
            getMessenger().send(sender, "stuff.bypass.disable");
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return null;
    }
}
