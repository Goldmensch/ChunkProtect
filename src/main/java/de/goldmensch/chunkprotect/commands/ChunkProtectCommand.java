package de.goldmensch.chunkprotect.commands;

import de.goldmensch.chunkprotect.commands.subs.*;
import de.goldmensch.chunkprotect.commands.subs.staff.BypassSub;
import de.goldmensch.chunkprotect.commands.subs.staff.StaffUnclaimAllSub;
import de.goldmensch.chunkprotect.commands.subs.staff.StaffUnclaimSub;
import de.goldmensch.chunkprotect.core.ChunkProtect;
import de.goldmensch.chunkprotect.utils.message.MessageBuilder;
import de.goldmensch.commanddispatcher.command.ArgValuedSubCommand;
import de.goldmensch.commanddispatcher.command.SmartCommand;
import de.goldmensch.smartutils.localizer.Replacement;
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

        registerSubCommand(AboutSub.newAboutSub(chunkProtect, this), "about");
        registerSubCommand(new ClaimSub(chunkProtect, this), "claim");
        registerSubCommand(new InfoSub(chunkProtect, this), "info");
        registerSubCommand(new UnclaimSub(chunkProtect, this), "unclaim");
        registerSubCommand(new TrustSub(chunkProtect, this), "trust");
        registerSubCommand(new UntrustSub(chunkProtect, this), "untrust");
        registerSubCommand(new UnclaimAllSub(chunkProtect, this), "unclaimAll");
        registerSubCommand(new HelpSub(chunkProtect, this), "help");

        registerSubCommand(new StaffUnclaimAllSub(chunkProtect, this), staff("unclaimAll"));
        registerSubCommand(new BypassSub(chunkProtect, this), staff("bypass"));
        registerSubCommand(new StaffUnclaimSub(chunkProtect, this), staff("unclaim"));
    }

    private String[] staff(String... sub) {
        return (String[]) ArrayUtils.addAll(new String[]{"staff"}, sub);
    }

    @Override
    public boolean noSubFound(String[] strings, CommandSender commandSender, Command command, String s) {
        sendHelp(commandSender);
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
        String msg = chunkProtect.getServer().spigot().getPaperConfig().getString("messages.no-permission");
        commandSender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(msg));
    }

    public void sendHelp(CommandSender sender) {
        MessageBuilder builder = MessageBuilder.builder();
        builder.appendLine(chunkProtect.getMessenger().prepare("help-header"));
        getAllSubFor(sender).keySet().forEach(cmd -> {
                    System.out.println(de.goldmensch.commanddispatcher.util.ArrayUtils.buildString(cmd));
                    builder.appendLine(chunkProtect.getMessenger()
                            .prepare("help-entry",
                                    Replacement.create("command",
                                            "/"+de.goldmensch.commanddispatcher.util.ArrayUtils.buildString(cmd))));
                }
        );

        sender.sendMessage(builder.build());
    }
}
