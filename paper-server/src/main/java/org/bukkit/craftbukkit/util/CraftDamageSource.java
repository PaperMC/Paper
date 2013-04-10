package org.bukkit.craftbukkit.util;

import net.minecraft.server.DamageSource;

// Util class to create custom DamageSources.
public final class CraftDamageSource extends DamageSource {
    public static DamageSource copyOf(final DamageSource original) {
        CraftDamageSource newSource = new CraftDamageSource(original.translationIndex);

        // Check ignoresArmor
        if (original.ignoresArmor()) {
            newSource.j();
        }

        // Check magic
        if (original.q()) {
            newSource.r();
        }

        // Check fire
        if (original.c()) {
            newSource.l();
        }

        return newSource;
    }

    private CraftDamageSource(String identifier) {
        super(identifier);
    }
}
