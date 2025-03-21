package io.papermc.paper;

import io.papermc.paper.entity.PaperPoiType;
import io.papermc.paper.entity.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
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

    @Override
    public PoiType.Occupancy createOccupancy(final String enumNameEntry) {
        return new PaperPoiType.PaperOccupancy(PoiManager.Occupancy.valueOf(enumNameEntry));
    }
}
