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

public class Util {

    public static <T> T executeAndReturn(T t, Consumer<T> function) {
        function.accept(t);
        return t;
    }

    public static void showChunkBorders(Player player, Plugin plugin) {
        Chunk chunk = player.getChunk();

        Location pointer = new Location(chunk.getWorld(),  chunk.getX()*16, player.getLocation().getY()+1, chunk.getZ()*16);
        int tick = Bukkit.getCurrentTick();
        for(int i = 0; i < 4; i++) {
            for(int a = 0; a < 16; a++) {
                switch (i) {
                    case 0 -> pointer = pointer.add(1, 0 ,0);
                    case 1 -> pointer = pointer.add(0, 0, 1);
                    case 2 -> pointer = pointer.add(-1, 0, 0);
                    case 3 -> pointer = pointer.add(0, 0, -1);
                }

                Location finalPointer = new Location(pointer.getWorld(), pointer.getBlockX(), pointer.getBlockY(), pointer.getBlockZ());
                Bukkit.getScheduler().runTaskTimer(plugin, bukkitTask -> {
                    if(((Bukkit.getCurrentTick()/20) - (tick/20)) > 5) bukkitTask.cancel();
                    player.spawnParticle(Particle.GLOW, finalPointer, 3);
                }, 0, 20);
            }
        }
    }

    public static void copyResource(String from, Path to) throws IOException {
        try(InputStream in = Util.class.getResourceAsStream("/" + from)) {
            Files.copy(in, to);
        }
    }

    public static String build(String prefix, String... args) {
        StringBuilder builder = new StringBuilder(prefix+".");
        for(String c : args) {
            builder.append(c);
            builder.append(".");
        }
        return builder.substring(0, builder.length()-1);
    }
}
