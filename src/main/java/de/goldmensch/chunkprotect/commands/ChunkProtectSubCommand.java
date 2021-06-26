package de.goldmensch.chunkprotect.commands;

import de.goldmensch.chunkprotect.core.ChunkProtect;
import de.goldmensch.chunkprotect.message.Messenger;
import de.goldmensch.chunkprotect.storage.services.DataService;
import de.goldmensch.commanddispatcher.ExecutorLevel;
import de.goldmensch.commanddispatcher.subcommand.SmartSubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class ChunkProtectSubCommand extends SmartSubCommand {

    private final ChunkProtect chunkProtect;

    public ChunkProtectSubCommand(ExecutorLevel executorLevel, String permission, ChunkProtect chunkProtect) {
        super(executorLevel, permission);
        this.chunkProtect = chunkProtect;
    }

    public ChunkProtect getChunkProtect() {
        return chunkProtect;
    }

    public DataService getDataService() {
        return chunkProtect.getDataService();
    }

    public Messenger getMessenger() {
        return chunkProtect.getMessenger();
    }

    public Player toPlayer(CommandSender sender) {
        return (Player) sender;
    }
}
