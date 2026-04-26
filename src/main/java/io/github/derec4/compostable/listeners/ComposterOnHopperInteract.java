package io.github.derec4.compostable.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Levelled;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.ItemStack;

import static io.github.derec4.compostable.Compostable.customCompostables;

public class ComposterOnHopperInteract implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onHopperCompost(InventoryMoveItemEvent event) {
        if (event.getDestination().getType() != org.bukkit.event.inventory.InventoryType.COMPOSTER) {
            return;
        }

        ItemStack movedItem = event.getItem();
        if (movedItem == null || !customCompostables.containsKey(movedItem.getType())) {
            return;
        }

        Block block = event.getDestination().getLocation() == null ? null : event.getDestination().getLocation().getBlock();
        if (block == null || block.getType() != Material.COMPOSTER) {
            return;
        }

        BlockData blockData = block.getBlockData();
        if (!(blockData instanceof Levelled composter) || composter.getLevel() >= composter.getMaximumLevel() - 1) {
            return;
        }

        event.setCancelled(true);
        ItemStack oneItem = movedItem.clone();
        oneItem.setAmount(1);
        if (!event.getSource().removeItem(oneItem).isEmpty()) {
            return;
        }

        double chance = customCompostables.get(movedItem.getType());
        if (Math.random() <= chance) {
            composter.setLevel(composter.getLevel() + 1);
            block.setBlockData(composter);

            if (composter.getLevel() == 7) {
                composter.setLevel(8);
                block.setBlockData(composter);
            }
        }
    }
}
