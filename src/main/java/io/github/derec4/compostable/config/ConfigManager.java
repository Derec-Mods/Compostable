package io.github.derec4.compostable.config;

import io.github.derec4.compostable.Compostable;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import static io.github.derec4.compostable.Compostable.customCompostables;

public class ConfigManager {
    private final Compostable plugin;

    public ConfigManager(Compostable plugin) {
        this.plugin = plugin;
    }

    public void loadCustomCompostables() {
        FileConfiguration config = plugin.getConfig();
        customCompostables.clear();

        ConfigurationSection items = config.getConfigurationSection("items");
        if (items == null) {
            plugin.getLogger().warning("No items section found in config.yml.");
            return;
        }

        for (String key : items.getKeys(false)) {
            Material material = Material.matchMaterial(key);
            if (material == null) {
                plugin.getLogger().warning("Skipping invalid material in config.yml: " + key);
                continue;
            }

            double chance = items.getDouble(key, -1.0);
            if (chance < 0.0 || chance > 1.0) {
                plugin.getLogger().warning("Skipping invalid compost chance for " + key + ": " + chance);
                continue;
            }

            customCompostables.put(material, chance);
        }
    }
}
