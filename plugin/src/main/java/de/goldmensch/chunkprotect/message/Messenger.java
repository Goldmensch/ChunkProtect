package de.goldmensch.chunkprotect.message;

import de.goldmensch.smartutils.localizer.MiniMessageAdapter;
import de.goldmensch.smartutils.localizer.Replacement;
import de.goldmensch.smartutils.localizer.SmartLocalizer;
import de.goldmensch.smartutils.localizer.adapter.ComponentLocalizerAdapter;
import de.goldmensch.smartutils.localizer.adapter.LocalizerAdapter;
import de.goldmensch.smartutils.localizer.reader.resourcebundle.ResourceBundleReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public final class Messenger implements IMessenger {

  private final SmartLocalizer<Component> localizer;
  private final Component prefix;
  private final boolean actionBar;

  private Messenger(Component prefix, SmartLocalizer<Component> localizer, boolean actionBar) {
    this.localizer = localizer;
    this.prefix = prefix;
    this.actionBar = actionBar;
  }

  public static IMessenger setupMessenger(Plugin plugin, boolean miniMessage, boolean actionBar)
      throws IOException {
    Path path = plugin.getDataFolder().toPath().resolve("messages.properties");

    updateResources(path, "messages.properties");

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

  private static void updateResources(Path path, String resource) throws IOException {

    if (Files.notExists(path)) {
      Files.createFile(path);
    }

    Map<String, String> oldLocalizations = ResourceBundleReader.fromPropertiesFile(path)
        .getLocalizations();
    Map<String, String> newLocalizations = ResourceBundleReader.fromBundle(
            new PropertyResourceBundle(Messenger.class.getResourceAsStream("/messages.properties")))
        .getLocalizations();

    oldLocalizations.entrySet()
        .stream()
        .filter(entry -> newLocalizations.containsKey(entry.getKey()))
        .forEach(entry -> newLocalizations.put(entry.getKey(), entry.getValue()));

    Files.write(path, newLocalizations.entrySet()
        .stream()
        .map(entry -> entry.getKey() + "=" + entry.getValue())
        .collect(Collectors.toUnmodifiableSet()));
  }

  @Override
  public void send(CommandSender receiver, String key, Replacement... replacements) {
    if (actionBar) {
      receiver.sendActionBar(prepare(key, replacements));
    } else {
      receiver.sendMessage(prepare(key, replacements));
    }
  }

  @Override
  public Component prepare(String key, Replacement... replacements) {
    return Component.text()
        .append(prefix)
        .append(localizer.localize(key, replacements))
        .build();
  }
}
