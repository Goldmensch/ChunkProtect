package de.goldmensch.chunkprotect.message;

import de.goldmensch.chunkprotect.utils.Util;
import de.goldmensch.smartutils.localizer.MiniMessageAdapter;
import de.goldmensch.smartutils.localizer.Replacement;
import de.goldmensch.smartutils.localizer.SmartLocalizer;
import de.goldmensch.smartutils.localizer.adapter.ComponentLocalizerAdapter;
import de.goldmensch.smartutils.localizer.adapter.LocalizerAdapter;
import de.goldmensch.smartutils.localizer.reader.resourcebundle.ResourceBundleReader;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

public final class Messenger {

    public static final String PLAYER_LITERAL = "player";

    private final SmartLocalizer<Component> localizer;
    private final Component prefix;
    private final boolean actionBar;

    private Messenger(Component prefix, SmartLocalizer<Component> localizer, boolean actionBar) {
        this.localizer = localizer;
        this.prefix = prefix;
        this.actionBar = actionBar;
    }

    public static Messenger setupMessenger(Plugin plugin, boolean miniMessage, boolean actionBar) throws IOException {
        Path path = plugin.getDataFolder().toPath().resolve("messages.properties");

        if (Files.notExists(path)) {
            Util.copyResource("messages.properties", path);
        }

        LocalizerAdapter<Component> adapter;
        if (miniMessage) {
            adapter = new MiniMessageAdapter();
        } else {
            adapter = new ComponentLocalizerAdapter();
        }

        SmartLocalizer<Component> localizer = SmartLocalizer.newLocalizer(Locale.ENGLISH, adapter)
                .fromReader(ResourceBundleReader.fromPropertiesFile(path));

        return new Messenger(localizer.localize("prefix"), localizer, actionBar);
    }

    public void send(CommandSender receiver, String key, Replacement... replacements) {
        if (actionBar) {
            receiver.sendActionBar(prepare(key, replacements));
        } else {
            receiver.sendMessage(prepare(key, replacements));
        }
    }

    public Component prepare(String key, Replacement... replacements) {
        return Component.text()
                .append(prefix)
                .append(localizer.localize(key, replacements))
                .build();
    }
}
