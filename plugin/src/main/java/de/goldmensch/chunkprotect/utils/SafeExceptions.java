package de.goldmensch.chunkprotect.utils;

import de.goldmensch.chunkprotect.utils.functions.ReturnThrowingFunction;
import de.goldmensch.chunkprotect.utils.functions.ThrowingFunction;
import org.bukkit.plugin.Plugin;

import java.io.IOException;

public final class SafeExceptions {

    private SafeExceptions() {
    }

    public static <T> T safeIOException(Plugin plugin, ReturnThrowingFunction<T, IOException> function) {
        try {
            return function.apply();
        } catch (IOException e) {
            e.printStackTrace();
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
        return null;
    }

    public static void safeIOException(Plugin plugin, ThrowingFunction<IOException> function) {
        try {
            function.apply();
        } catch (IOException e) {
            e.printStackTrace();
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
    }

}
