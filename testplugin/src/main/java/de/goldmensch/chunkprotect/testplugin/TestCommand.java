package de.goldmensch.chunkprotect.testplugin;

import de.goldmensch.chunkprotect.ChunkLocation;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TestCommand implements CommandExecutor {
    private final TestPlugin plugin;

    public TestCommand(TestPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)) return false;
        sender.sendMessage(plugin.getChunkProtect().getChunkService().getChunkAt(ChunkLocation.fromChunk(player.getChunk())).toString());
        return true;
    }
}
