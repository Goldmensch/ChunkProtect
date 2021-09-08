package de.goldmensch.chunkprotect.configuration.protection;

import de.goldmensch.chunkprotect.configuration.Configuration;
import java.nio.file.Path;

public class ProtectionConfig extends Configuration<ProtectionFile> {

  public ProtectionConfig(Path path) {
    super(path, new ProtectionFile());
  }
}
