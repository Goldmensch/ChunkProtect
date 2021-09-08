package de.goldmensch.chunkprotect.configuration.plugin;

import de.goldmensch.chunkprotect.configuration.Configuration;
import java.nio.file.Path;

public class PluginConfig extends Configuration<ConfigFile> {

  public PluginConfig(Path path) {
    super(path, new ConfigFile());
  }
}
