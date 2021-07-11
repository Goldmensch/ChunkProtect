package de.goldmensch.chunkprotect.core;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ProtectionBypass {
    private final Set<UUID> players = new HashSet<>();

    public boolean toggle(UUID uuid) {
        if(players.contains(uuid)) {
            players.remove(uuid);
        }else {
            players.add(uuid);
        }
        return players.contains(uuid);
    }

    public boolean hasBypass(UUID uuid) {
        return players.contains(uuid);
    }

    public void remove(UUID uuid) {
        players.remove(uuid);
    }
}
