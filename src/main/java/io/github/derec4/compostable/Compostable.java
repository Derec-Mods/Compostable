package io.github.derec4.compostable;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public final class Compostable extends JavaPlugin {

    /**
     * Map of an item, and the rate you want to compost it by. Will read this in from config
     */
    public static final Map<Material, Double> customCompostables = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        String version = getDescription().getVersion();
        @NotNull ConsoleCommandSender console = Bukkit.getConsoleSender();
        console.sendMessage(Component.text(""));
        console.sendMessage(Component.text("  |_______|                             ").color(NamedTextColor.GREEN));
        console.sendMessage(Component.text("  | Derex |     Compostable Plugin v" + version).color(NamedTextColor.GREEN));
        console.sendMessage(Component.text("  |_______|     Running on " + Bukkit.getName() + " - " + Bukkit.getVersion()).color(NamedTextColor.GREEN));
        console.sendMessage(Component.text(""));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    public void loadItems() {
        customCompostables.put(Material.ROTTEN_FLESH, 0.3);
        // Add more here later!
    }
}
