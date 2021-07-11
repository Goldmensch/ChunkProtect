package de.goldmensch.chunkprotect.commands;

import de.goldmensch.chunkprotect.commands.subs.*;
import de.goldmensch.chunkprotect.commands.subs.staff.BypassSub;
import de.goldmensch.chunkprotect.commands.subs.staff.StaffUnclaimAllSub;
import de.goldmensch.chunkprotect.commands.subs.staff.StaffUnclaimSub;
import de.goldmensch.chunkprotect.core.ChunkProtect;
import de.goldmensch.commanddispatcher.command.ArgValuedSubCommand;
import de.goldmensch.commanddispatcher.command.SmartCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ChunkProtectCommand extends SmartCommand {

    private final ChunkProtect chunkProtect;

    public ChunkProtectCommand(ChunkProtect chunkProtect) {
        this.chunkProtect = chunkProtect;

        registerSubCommand(AboutSub.newAboutSub(chunkProtect), "about");
        registerSubCommand(new ClaimSub(chunkProtect), "claim");
        registerSubCommand(new InfoSub(chunkProtect), "info");
        registerSubCommand(new UnclaimSub(chunkProtect), "unclaim");
        registerSubCommand(new TrustSub(chunkProtect), "trust");
        registerSubCommand(new UntrustSub(chunkProtect), "untrust");
        registerSubCommand(new UnclaimAllSub(chunkProtect), "unclaimAll");

        registerSubCommand(new StaffUnclaimAllSub(chunkProtect), staff("unclaimAll"));
        registerSubCommand(new BypassSub(chunkProtect), staff("bypass"));
        registerSubCommand(new StaffUnclaimSub(chunkProtect), staff("unclaim"));
    }

    private String[] staff(String... sub) {
        return (String[]) ArrayUtils.addAll(new String[]{"staff"}, sub);
    }

    @Override
    public boolean noSubFound(String[] strings, CommandSender commandSender, Command command, String s) {
        commandSender.sendMessage("no sub founds for: " + s);
        return false;
    }

    @Override
    public void wrongExecutorLevel(ArgValuedSubCommand argValuedSubCommand, CommandSender commandSender) {
        Component message;
        switch (argValuedSubCommand.getCommand().getExecutorLevel()) {
            case PLAYER -> message = Component.text("Only Player can execute this command.");
            case CONSOLE -> message = Component.text("Only Console can execute this command.");
            default -> message = Component.empty();
        }
        commandSender.sendMessage(message.color(NamedTextColor.RED));
    }

    @Override
    public void noPermission(ArgValuedSubCommand argValuedSubCommand, CommandSender commandSender) {
        String msg = chunkProtect.getServer().spigot().getPaperConfig().getString("message.no-permission");
        commandSender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(msg));
    }
}
