package de.goldmensch.chunkprotect.utils;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

public class EventUtil {
    public static boolean isBucketEvent(PlayerInteractEvent event) {
        return event.hasItem()
                && ((event.getItem().getType() == Material.WATER_BUCKET)
                || (event.getItem().getType() == Material.LAVA_BUCKET));
    }
}
