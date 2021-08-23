package de.goldmensch.chunkprotect.commands;

import de.goldmensch.chunkprotect.ChunkProtectPlugin;
import de.goldmensch.chunkprotect.message.Messenger;
import de.goldmensch.chunkprotect.storage.services.DataService;
import de.goldmensch.commanddispatcher.ExecutorLevel;
import de.goldmensch.commanddispatcher.subcommand.SmartSubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class ChunkProtectSubCommand extends SmartSubCommand {

    private final ChunkProtectPlugin chunkProtectPlugin;
    private final ChunkProtectCommand chunkProtectCommand;

    protected ChunkProtectSubCommand(ExecutorLevel executorLevel, String permission, ChunkProtectPlugin chunkProtectPlugin, ChunkProtectCommand chunkProtectCommand) {
        super(executorLevel, permission);
        this.chunkProtectPlugin = chunkProtectPlugin;
        this.chunkProtectCommand = chunkProtectCommand;
    }

    public ChunkProtectPlugin getChunkProtect() {
        return chunkProtectPlugin;
    }

    public DataService getDataService() {
        return chunkProtectPlugin.getDataService();
    }

    public Messenger getMessenger() {
        return chunkProtectPlugin.getMessenger();
    }

    public Player toPlayer(CommandSender sender) {
        return (Player) sender;
    }

    public ChunkProtectCommand getChunkProtectCommand() {
        return chunkProtectCommand;
    }
}
