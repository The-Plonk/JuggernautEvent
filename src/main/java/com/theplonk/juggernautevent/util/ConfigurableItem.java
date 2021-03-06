package com.theplonk.juggernautevent.util;

import lombok.Getter;
import com.theplonk.juggernautevent.Core;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ConfigurableItem {

    @Getter private static ItemStack item;
    public static final Core plugin = Core.getPlugin();

    public static ItemStack build(Map<?, ?> section) {
        Material type = Material.getMaterial((String) Objects.requireNonNull(section.get("type")));
        int amount = 1;
        Map<String, Integer> enchantmentsHolder = new HashMap<>();
        String potionEffect = null;
        boolean potionExtended = false;
        boolean potionUpgraded = false;
        String displayName = null;

        if (section.get("amount") != null) {
            amount = (int) section.get("amount");
        }

        if (section.get("display-name") != null) {
            displayName = LegacyComponentSerializer.legacySection().serialize(MiniMessage.get().parse((String) section.get("display-name")));
        }

        if (section.get("flags") != null) {
            Map<?, ?> flags = (Map<?, ?>) section.get("flags");
            if (flags.get("enchantments") != null) {
                Map<?, ?> enchantments = (Map<?, ?>) ((Map<?, ?>) section.get("flags")).get("enchantments");
                enchantments.forEach((k, v) -> enchantmentsHolder.put((String) k, (int) v));
            }

            if (flags.get("potion") != null) {
                Map<?, ?> potionAttributes = (Map<?, ?>) flags.get("potion");
                potionEffect = (String) potionAttributes.get("effect");
                potionExtended = (boolean) potionAttributes.get("extended");
                potionUpgraded = (boolean) potionAttributes.get("upgraded");
            }
        }

        item = new ItemBuilder(type, amount)
                .setDisplayName(displayName)
                .addEnchantments(enchantmentsHolder)
                .setPotionEffect(potionEffect, potionExtended, potionUpgraded)
                .build();

        return item;
    }
}
