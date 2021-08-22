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
    private final ChunkProtectCommand chunkProtectCommand;

    protected ChunkProtectSubCommand(ExecutorLevel executorLevel, String permission, ChunkProtect chunkProtect, ChunkProtectCommand chunkProtectCommand) {
        super(executorLevel, permission);
        this.chunkProtect = chunkProtect;
        this.chunkProtectCommand = chunkProtectCommand;
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

    public ChunkProtectCommand getChunkProtectCommand() {
        return chunkProtectCommand;
    }
}
