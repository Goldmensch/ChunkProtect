package de.goldmensch.chunkprotect;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;

public final class Events {

  private Events() {
  }

  public static boolean isBucketEvent(PlayerInteractEvent event) {
    return event.hasItem()
        && ((event.getItem().getType() == Material.WATER_BUCKET)
        || (event.getItem().getType() == Material.LAVA_BUCKET));
  }

  public static <T extends Event & Cancellable> boolean callAndCheckCancelled(T event) {
    Bukkit.getPluginManager().callEvent(event);
    return event.isCancelled();
  }
}
