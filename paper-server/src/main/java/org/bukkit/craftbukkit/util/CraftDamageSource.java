package org.bukkit.craftbukkit.util;

import net.minecraft.server.DamageSource;

// Util class to create custom DamageSources.
public final class CraftDamageSource extends DamageSource {
    public static DamageSource copyOf(final DamageSource original) {
        CraftDamageSource newSource = new CraftDamageSource(original.translationIndex);

        // Check ignoresArmor
        if (original.ignoresArmor()) {
            newSource.k();
        }

        // Check magic
        if (original.s()) {
            newSource.t();
        }

        // Check fire
        if (original.c()) {
            newSource.n();
        }

        return newSource;
    }

    private CraftDamageSource(String identifier) {
        super(identifier);
    }
}
