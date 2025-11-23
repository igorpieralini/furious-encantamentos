package org.furious.furiousencantamentos.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.enchantments.Enchantment;

@Data
@AllArgsConstructor
public class CustomEnchantment {
    private Enchantment enchantment;
    private int level;
    private int xpCost;
}
