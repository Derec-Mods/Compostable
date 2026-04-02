package io.github.derec4.compostable.listeners;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Levelled;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ComposterOnInteract implements Listener {
    @EventHandler
    public void onPlayerCompost(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Block block = event.getClickedBlock();
        if (block == null || block.getType() != Material.COMPOSTER) {
            return;
        }

        ItemStack item = event.getItem();
        if (item == null || !customCompostables.containsKey(item.getType())) {
            return;
        }

        // We found a custom item! Cancel vanilla logic.
        event.setCancelled(true);
        BlockData blockData = block.getBlockData();
        if (blockData instanceof Levelled composter) {
            // pattern variable 4.1.2026
            // Max level is 7 (8 is when it's ready to collect, we don't add to 8)
            if (composter.getLevel() >= composter.getMaximumLevel() - 1) {
                return;
            }

            item.setAmount(item.getAmount() - 1);

            // Roll the dice to see if the compost level goes up
            double chance = customCompostables.get(item.getType());
            if (Math.random() <= chance) {
                composter.setLevel(composter.getLevel() + 1);
                block.setBlockData(composter);

                block.getWorld().playSound(block.getLocation(), Sound.BLOCK_COMPOSTER_FILL_SUCCESS, 1.0f, 1.0f);
                // TODO: particle

                // If it hit level 7, it's ready to become bone meal! 8 is bonemeal composter state
                if (composter.getLevel() == 7) {
                    composter.setLevel(8);
                    block.setBlockData(composter);
                    block.getWorld().playSound(block.getLocation(), Sound.BLOCK_COMPOSTER_READY, 1.0f, 1.0f);
                }
            } else {
                // Play fail sound (item consumed, but no level up)
                block.getWorld().playSound(block.getLocation(), Sound.BLOCK_COMPOSTER_FILL, 1.0f, 1.0f);
            }
        }
    }
}
