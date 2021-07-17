package de.goldmensch.chunkprotect.utils;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

public final class Util {

    private Util() {
    }

    public static <T> T executeAndReturn(T t, Consumer<T> function) {
        function.accept(t);
        return t;
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

    public static void copyResource(String from, Path to) throws IOException {
        try (InputStream in = Util.class.getResourceAsStream("/" + from)) {
            Files.copy(in, to);
        }
    }

    public static String build(String prefix, String... args) {
        StringBuilder builder = new StringBuilder(prefix + ".");
        for (String c : args) {
            builder.append(c);
            builder.append(".");
        }
        return builder.substring(0, builder.length() - 1);
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
