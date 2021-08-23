package de.goldmensch.chunkprotect;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Borders {

    private Borders() {
    }

    public static void showChunkBorders(Player player, Plugin plugin) {
        Chunk chunk = player.getChunk();

        Location pointer = new Location(chunk.getWorld(), chunk.getX() * 16D, player.getLocation().getY() + 1D, chunk.getZ() * 16D);
        int tick = Bukkit.getCurrentTick();

        PointerSide side = PointerSide.NORTH;
        do {
            for (int a = 0; a < 16; a++) {
                pointer = switch (side) {
                    case NORTH -> pointer.add(1, 0, 0);
                    case EAST -> pointer.add(0, 0, 1);
                    case SOUTH -> pointer.add(-1, 0, 0);
                    case WEST -> pointer.add(0, 0, -1);
                };

                Location finalPointer = new Location(pointer.getWorld(), pointer.getBlockX(), pointer.getBlockY(), pointer.getBlockZ());
                Bukkit.getScheduler().runTaskTimer(plugin, bukkitTask -> {
                    if (((Bukkit.getCurrentTick() / 20) - (tick / 20)) > 5) bukkitTask.cancel();
                    player.spawnParticle(Particle.GLOW, finalPointer, 3);
                }, 0, 20);
            }
            side = PointerSide.next(side);
        } while (side != PointerSide.NORTH);
    }

    private enum PointerSide {
        NORTH,
        EAST,
        SOUTH,
        WEST;

        public static PointerSide next(PointerSide side) {
            return switch (side) {
                case NORTH -> PointerSide.EAST;
                case EAST -> PointerSide.SOUTH;
                case SOUTH -> PointerSide.WEST;
                case WEST -> PointerSide.NORTH;
            };
        }
    }
}