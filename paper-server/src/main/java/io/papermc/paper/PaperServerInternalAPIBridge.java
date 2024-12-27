package io.papermc.paper;

import org.bukkit.craftbukkit.damage.CraftDamageEffect;
import org.bukkit.damage.DamageEffect;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class PaperServerInternalAPIBridge implements InternalAPIBridge {
    public static final PaperServerInternalAPIBridge INSTANCE = new PaperServerInternalAPIBridge();

    @Override
    public DamageEffect getDamageEffect(final String key) {
        return CraftDamageEffect.getById(key);
    }
}
