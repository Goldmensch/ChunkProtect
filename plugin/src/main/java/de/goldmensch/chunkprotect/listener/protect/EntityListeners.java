package de.goldmensch.chunkprotect.listener.protect;

import de.goldmensch.chunkprotect.ChunkProtectPlugin;
import de.goldmensch.chunkprotect.Chunks;
import de.goldmensch.chunkprotect.storage.services.DataService;
import java.util.concurrent.atomic.AtomicBoolean;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;

public class EntityListeners extends BlockListeners {

  public EntityListeners(DataService dataService, ChunkProtectPlugin chunkProtectPlugin) {
    super(dataService, chunkProtectPlugin);
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void handleHangingDestroy(HangingBreakByEntityEvent event) {
    if (entityDamage(event.getEntity(), event.getRemover())) {
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void handleEntityDamageByEntity(EntityDamageByEntityEvent event) {
    if (entityDamage(event.getEntity(), event.getDamager())) {
      event.setCancelled(true);
    }
  }

  private boolean entityDamage(Entity entity, Entity other) {
    AtomicBoolean returnValue = new AtomicBoolean(false);
    unwrapPlayer(other).ifPresent(
        player -> Chunks.getChunk(entity.getChunk(), dataService).ifClaimedOr(chunk -> {
          if (entitiesConfiguration.getProtection(entity.getType()).getDamage().isClaimed() &&
              forbidden(player, chunk)) {
            returnValue.set(true);
          }
        }, () -> {
          if (entitiesConfiguration.getProtection(entity.getType()).getDamage().isUnclaimed() &&
              hasNoBypass(player)) {
            sendYouCantDoThat(player);
            returnValue.set(true);
          }
        }));
    return returnValue.get();
  }

  @EventHandler
  public void handleVehicleDamage(VehicleDamageEvent event) {
    if (entityDamage(event.getVehicle().getVehicle(), event.getAttacker())) {
      event.setCancelled(true);
    }
  }

  /*
  Explosion protection
   */
  @EventHandler(priority = EventPriority.HIGH)
  public void handleEntityExplosion(EntityExplodeEvent event) {
    onExplodeEvent(event.blockList());
  }
}
