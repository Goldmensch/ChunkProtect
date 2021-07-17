package de.goldmensch.chunkprotect.listener.protect;

import de.goldmensch.chunkprotect.configuration.entities.EntitiesConfiguration;
import de.goldmensch.chunkprotect.configuration.protection.ProtectionFile;
import de.goldmensch.chunkprotect.core.ChunkProtect;
import de.goldmensch.chunkprotect.core.chunk.ClaimedChunk;
import de.goldmensch.chunkprotect.storage.services.DataService;
import de.goldmensch.chunkprotect.utils.ChunkUtil;
import de.goldmensch.smartutils.localizer.Replacement;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Listener;

import java.util.Optional;

public class ListenerData implements Listener {
    protected final DataService dataService;
    protected final ChunkProtect chunkProtect;

    protected final EntitiesConfiguration entitiesConfiguration;

    protected final ProtectionFile protectionFile;

    public ListenerData(DataService dataService, ChunkProtect chunkProtect) {
        this.dataService = dataService;
        this.chunkProtect = chunkProtect;
        this.entitiesConfiguration = chunkProtect.getEntitiesConfiguration();
        this.protectionFile = chunkProtect.getProtectionConfig().getConfigFile();
    }

    protected boolean forbidden(HumanEntity player, ClaimedChunk chunk) {
        if (chunkProtect.getProtectionBypass().hasBypass(player.getUniqueId())) return false;
        if (!ChunkUtil.hasAccess(chunk, player.getUniqueId())) {
            sendNoAccess(player, chunk);
            return true;
        }
        return false;
    }

    protected void sendNoAccess(CommandSender sender, ClaimedChunk chunk) {
        chunkProtect.getMessenger().send(sender, "chunk-protected",
                Replacement.create("holder", chunk.getHolder().getName()));
    }

    protected void sendYouCantDoThat(CommandSender sender) {
        chunkProtect.getMessenger().send(sender, "you-cant-do-that");
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
