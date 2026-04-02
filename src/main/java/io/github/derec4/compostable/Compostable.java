package io.github.derec4.compostable;

import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public final class Compostable extends JavaPlugin {

    /**
     * Map of an item, and the rate you want to compost it by. Will read this in from config
     */
    private final Map<Material, Double> customCompostables = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic

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
