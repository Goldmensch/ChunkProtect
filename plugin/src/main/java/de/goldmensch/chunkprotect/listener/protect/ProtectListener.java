package de.goldmensch.chunkprotect.listener.protect;

import de.goldmensch.chunkprotect.configuration.entities.EntitiesConfiguration;
import de.goldmensch.chunkprotect.configuration.protection.ProtectionFile;
import de.goldmensch.chunkprotect.ChunkProtectPlugin;
import de.goldmensch.chunkprotect.core.chunk.ClaimedChunk;
import de.goldmensch.chunkprotect.storage.services.DataService;
import de.goldmensch.chunkprotect.Chunks;
import de.goldmensch.smartutils.localizer.Replacement;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Listener;

import java.util.Optional;

public class ProtectListener implements Listener {
    protected final DataService dataService;
    protected final ChunkProtectPlugin chunkProtectPlugin;

    protected final EntitiesConfiguration entitiesConfiguration;

    protected final ProtectionFile protectionFile;

    public ProtectListener(DataService dataService, ChunkProtectPlugin chunkProtectPlugin) {
        this.dataService = dataService;
        this.chunkProtectPlugin = chunkProtectPlugin;
        this.entitiesConfiguration = chunkProtectPlugin.getEntitiesConfiguration();
        this.protectionFile = chunkProtectPlugin.getProtectionConfig().getConfigFile();
    }

    protected boolean forbidden(HumanEntity player, ClaimedChunk chunk) {
        if (!hasNoBypass(player)) return false;
        if (!Chunks.hasAccess(chunk, player.getUniqueId())) {
            sendNoAccess(player, chunk);
            return true;
        }
        return false;
    }

    protected boolean hasNoBypass(HumanEntity player) {
        return !chunkProtectPlugin.getProtectionBypass().hasBypass(player.getUniqueId());
    }

    protected void sendNoAccess(CommandSender sender, ClaimedChunk chunk) {
        chunkProtectPlugin.getMessenger().send(sender, "chunk-protected",
                Replacement.create("holder", chunk.getHolder().getName()));
    }

    protected void sendYouCantDoThat(CommandSender sender) {
        chunkProtectPlugin.getMessenger().send(sender, "you-cant-do-that");
    }

    protected Optional<Player> unwrapPlayer(Entity possiblePlayer) {
        if (possiblePlayer instanceof Player player) {
            return Optional.of(player);
        }
        if ((possiblePlayer instanceof Projectile projectile) && (projectile.getShooter() instanceof Player player)) {
            return Optional.of(player);
        }
        return Optional.empty();
    }
}
