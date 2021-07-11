package de.goldmensch.chunkprotect.listener.protect;

import de.goldmensch.chunkprotect.configuration.entities.EntitiesConfiguration;
import de.goldmensch.chunkprotect.core.ChunkProtect;
import de.goldmensch.chunkprotect.core.chunk.ChunkLocation;
import de.goldmensch.chunkprotect.core.chunk.ClaimableChunk;
import de.goldmensch.chunkprotect.core.chunk.ClaimedChunk;
import de.goldmensch.chunkprotect.utils.ChunkUtil;
import de.goldmensch.chunkprotect.storage.services.DataService;
import de.goldmensch.smartutils.localizer.Replacement;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class ListenerData implements Listener {

    protected final Map<UUID, Location> lastClicked = new HashMap<>();

    protected final DataService dataService;
    protected final ChunkProtect chunkProtect;

    protected final EntitiesConfiguration entitiesConfiguration;

    public ListenerData(DataService dataService, ChunkProtect chunkProtect) {
        this.dataService = dataService;
        this.chunkProtect = chunkProtect;
        this.entitiesConfiguration = chunkProtect.getEntitiesConfiguration();
    }

    protected boolean forbidden(HumanEntity player, Chunk chunk) {
        if(chunkProtect.getProtectionBypass().hasBypass(player.getUniqueId())) return false;
        ClaimableChunk claimableChunk = dataService.getChunkAt(ChunkLocation.fromChunk(chunk));
        if(claimableChunk.isClaimed()) {
            ClaimedChunk claimedChunk = claimableChunk.getChunk();
            if(!ChunkUtil.hasAccess(claimedChunk, player.getUniqueId())) {
                sendNoAccess(player, claimedChunk);
                return true;
            }
        }
        return false;
    }

    protected void sendNoAccess(HumanEntity player, ClaimedChunk chunk) {
        chunkProtect.getMessenger().send(player, "chunk-protected",
                Replacement.create("holder", chunk.getHolder().getName()));
    }

    protected Optional<Player> unwrapPlayer(Entity possiblePlayer) {
        Player player = null;
        if(possiblePlayer instanceof Player) {
            player = (Player) possiblePlayer;
        }else if (possiblePlayer instanceof Projectile projectile) {
            if (projectile.getShooter() instanceof Player) {
                player = (Player) projectile.getShooter();
            }
        }
        return Optional.ofNullable(player);
    }

    protected boolean checkFromTo(ClaimableChunk from, ClaimableChunk to) {
        if(to.isClaimed() && from.isClaimed()) {
            return !ChunkUtil.sameHolder(to.getChunk(), from.getChunk());
        }
        return to.isClaimed();
    }
}
