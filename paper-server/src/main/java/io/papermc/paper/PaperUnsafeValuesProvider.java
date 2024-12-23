package io.papermc.paper;

import com.google.common.base.Preconditions;
import org.bukkit.craftbukkit.damage.CraftDamageEffect;
import org.bukkit.damage.DamageEffect;

public class PaperUnsafeValuesProvider implements StaticUnsafeValues.UnsafeValuesProvider {
    public static final PaperUnsafeValuesProvider INSTANCE = new PaperUnsafeValuesProvider();

    @Override
    public DamageEffect getDamageEffect(String key) {
        Preconditions.checkArgument(key != null, "key cannot be null");
        return CraftDamageEffect.getById(key);
    }
}
