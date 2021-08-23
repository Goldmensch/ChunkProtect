package de.goldmensch.chunkprotect.listener;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

public final class Events {

    private Events() {
    }

    public static boolean isBucketEvent(PlayerInteractEvent event) {
        return event.hasItem()
                && ((event.getItem().getType() == Material.WATER_BUCKET)
                || (event.getItem().getType() == Material.LAVA_BUCKET));
    }
}
