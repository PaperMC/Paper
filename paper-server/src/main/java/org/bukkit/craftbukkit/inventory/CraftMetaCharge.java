package org.bukkit.craftbukkit.inventory;

import com.google.common.collect.ImmutableMap.Builder;
import java.util.Map;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.FireworkExplosion;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.inventory.meta.FireworkEffectMeta;

@DelegateDeserialization(SerializableMeta.class)
class CraftMetaCharge extends CraftMetaItem implements FireworkEffectMeta {
    static final ItemMetaKeyType<FireworkExplosion> EXPLOSION = new ItemMetaKeyType<>(DataComponents.FIREWORK_EXPLOSION, "firework-effect");

    private FireworkEffect effect;

    CraftMetaCharge(CraftMetaItem meta) {
        super(meta);

        if (meta instanceof CraftMetaCharge) {
            effect = ((CraftMetaCharge) meta).effect;
        }
    }

    CraftMetaCharge(Map<String, Object> map) {
        super(map);

        setEffect(SerializableMeta.getObject(FireworkEffect.class, map, EXPLOSION.BUKKIT, true));
    }

    CraftMetaCharge(DataComponentPatch tag) {
        super(tag);

        getOrEmpty(tag, EXPLOSION).ifPresent((f) -> {
            try {
                effect = CraftMetaFirework.getEffect(f);
            } catch (IllegalArgumentException ex) {
                // Ignore invalid effects
            }
        });
    }

    @Override
    public void setEffect(FireworkEffect effect) {
        this.effect = effect;
    }

    @Override
    public boolean hasEffect() {
        return effect != null;
    }

    @Override
    public FireworkEffect getEffect() {
        return effect;
    }

    @Override
    void applyToItem(CraftMetaItem.Applicator itemTag) {
        super.applyToItem(itemTag);

        if (hasEffect()) {
            itemTag.put(EXPLOSION, CraftMetaFirework.getExplosion(effect));
        }
    }

    @Override
    boolean applicableTo(Material type) {
        return type == Material.FIREWORK_STAR;
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && !hasChargeMeta();
    }

    boolean hasChargeMeta() {
        return hasEffect();
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaCharge) {
            CraftMetaCharge that = (CraftMetaCharge) meta;

            return (hasEffect() ? that.hasEffect() && this.effect.equals(that.effect) : !that.hasEffect());
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaCharge || !hasChargeMeta());
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();

        if (hasEffect()) {
            hash = 61 * hash + effect.hashCode();
        }

        return hash != original ? CraftMetaCharge.class.hashCode() ^ hash : hash;
    }

    @Override
    public CraftMetaCharge clone() {
        return (CraftMetaCharge) super.clone();
    }

    @Override
    Builder<String, Object> serialize(Builder<String, Object> builder) {
        super.serialize(builder);

        if (hasEffect()) {
            builder.put(EXPLOSION.BUKKIT, effect);
        }

        return builder;
    }
}
