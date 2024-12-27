package io.papermc.paper;

import org.bukkit.craftbukkit.damage.CraftDamageEffect;
import org.bukkit.damage.DamageEffect;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class PaperUnsafeValuesProvider implements InternalAPIBridge {
    public static final PaperUnsafeValuesProvider INSTANCE = new PaperUnsafeValuesProvider();

    @Override
    public DamageEffect getDamageEffect(final String mojangKey) {
        return CraftDamageEffect.getById(mojangKey);
    }
}
