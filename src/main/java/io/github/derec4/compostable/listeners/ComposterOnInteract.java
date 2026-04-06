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

import java.util.Random;

import static io.github.derec4.compostable.Compostable.customCompostables;

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

                // 4.6.2026 added composter particles! Algorithm meant to match vanilla, credits to Mojang
                Random random = new Random();
                double centerHeight = 0.5 + 0.03125; // approximation of vanilla center height
                for (int i = 0; i < 10; i++) {
                    double xa = random.nextGaussian() * 0.02;
                    double ya = random.nextGaussian() * 0.02;
                    double za = random.nextGaussian() * 0.02;
                    double x = block.getX() + 0.1875 + 0.625 * random.nextFloat();
                    double y = block.getY() + centerHeight + random.nextFloat() * (1.0 - centerHeight);
                    double z = block.getZ() + 0.1875 + 0.625 * random.nextFloat();
                    // spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, extra)
                    block.getWorld().spawnParticle(org.bukkit.Particle.COMPOSTER, x, y, z, 1, xa, ya, za, 0.0);
                }

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
