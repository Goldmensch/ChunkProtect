package de.goldmensch.chunkprotect.commands;

import de.goldmensch.chunkprotect.commands.subs.*;
import de.goldmensch.chunkprotect.commands.subs.staff.BypassSub;
import de.goldmensch.chunkprotect.commands.subs.staff.StaffUnclaimAllSub;
import de.goldmensch.chunkprotect.commands.subs.staff.StaffUnclaimSub;
import de.goldmensch.chunkprotect.ChunkProtectPlugin;
import de.goldmensch.chunkprotect.message.MessageBuilder;
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

    private final ChunkProtectPlugin chunkProtectPlugin;

    public ChunkProtectCommand(ChunkProtectPlugin chunkProtectPlugin) {
        this.chunkProtectPlugin = chunkProtectPlugin;

        registerSubCommand(AboutSub.newAboutSub(chunkProtectPlugin, this), "about");
        registerSubCommand(new ClaimSub(chunkProtectPlugin, this), "claim");
        registerSubCommand(new InfoSub(chunkProtectPlugin, this), "info");
        registerSubCommand(new UnclaimSub(chunkProtectPlugin, this), "unclaim");
        registerSubCommand(new TrustSub(chunkProtectPlugin, this), "trust");
        registerSubCommand(new UntrustSub(chunkProtectPlugin, this), "untrust");
        registerSubCommand(new UnclaimAllSub(chunkProtectPlugin, this), "unclaimAll");
        registerSubCommand(new HelpSub(chunkProtectPlugin, this), "help");

        registerSubCommand(new StaffUnclaimAllSub(chunkProtectPlugin, this), staff("unclaimAll"));
        registerSubCommand(new BypassSub(chunkProtectPlugin, this), staff("bypass"));
        registerSubCommand(new StaffUnclaimSub(chunkProtectPlugin, this), staff("unclaim"));
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
        Component message = switch (argValuedSubCommand.getCommand().getExecutorLevel()) {
            case PLAYER -> Component.text("Only Player can execute this command.");
            case CONSOLE -> Component.text("Only Console can execute this command.");
            default -> Component.empty();
        };
        commandSender.sendMessage(message.color(NamedTextColor.RED));
    }

    @Override
    public void noPermission(ArgValuedSubCommand argValuedSubCommand, CommandSender commandSender) {
        String msg = chunkProtectPlugin.getServer().spigot().getPaperConfig().getString("messages.no-permission");
        commandSender.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(msg != null ? msg : "no message found"));
    }

    public void sendHelp(CommandSender sender) {
        MessageBuilder builder = MessageBuilder.builder();
        builder.appendLine(chunkProtectPlugin.getMessenger().prepare("help.header"));
        getAllSubFor(sender).keySet().forEach(cmd ->
                builder.appendLine(chunkProtectPlugin.getMessenger()
                        .prepare("help.entry",
                                Replacement.create("command",
                                        "/" + de.goldmensch.commanddispatcher.util.ArrayUtils.buildString(cmd))))
        );

        sender.sendMessage(builder.build());
    }
}
