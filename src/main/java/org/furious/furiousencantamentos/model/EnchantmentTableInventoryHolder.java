package org.furious.furiousencantamentos.model;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class EnchantmentTableInventoryHolder  implements InventoryHolder {
    private Inventory inventory;
    @Override
    public Inventory getInventory() {
        return inventory;
    }
}

