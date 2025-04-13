package org.bukkit.craftbukkit.inventory;

import com.google.common.collect.ImmutableMap.Builder;
import java.util.Map;
import java.util.Objects;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.FireworkExplosion;
import org.bukkit.FireworkEffect;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.inventory.meta.FireworkEffectMeta;

@DelegateDeserialization(SerializableMeta.class)
class CraftMetaCharge extends CraftMetaItem implements FireworkEffectMeta {
    static final ItemMetaKeyType<FireworkExplosion> EXPLOSION = new ItemMetaKeyType<>(DataComponents.FIREWORK_EXPLOSION, "firework-effect");

    private FireworkEffect effect;

    CraftMetaCharge(CraftMetaItem meta) {
        super(meta);

        if (meta instanceof final CraftMetaCharge chargeMeta) {
            this.effect = chargeMeta.effect;
        }
    }

    CraftMetaCharge(Map<String, Object> map) {
        super(map);

        this.setEffect(SerializableMeta.getObject(FireworkEffect.class, map, CraftMetaCharge.EXPLOSION.BUKKIT, true));
    }

    CraftMetaCharge(DataComponentPatch tag, java.util.Set<net.minecraft.core.component.DataComponentType<?>> extraHandledDcts) {
        super(tag, extraHandledDcts);

        getOrEmpty(tag, CraftMetaCharge.EXPLOSION).ifPresent((f) -> {
            try {
                this.effect = CraftMetaFirework.getEffect(f);
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
        return this.effect != null;
    }

    @Override
    public FireworkEffect getEffect() {
        return this.effect;
    }

    @Override
    void applyToItem(CraftMetaItem.Applicator tag) {
        super.applyToItem(tag);

        if (this.hasEffect()) {
            tag.put(CraftMetaCharge.EXPLOSION, CraftMetaFirework.getExplosion(this.effect));
        }
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && !this.hasChargeMeta();
    }

    boolean hasChargeMeta() {
        return this.hasEffect();
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof final CraftMetaCharge other) {
            return Objects.equals(this.effect, other.effect);
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaCharge || !this.hasChargeMeta());
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();

        if (this.hasEffect()) {
            hash = 61 * hash + this.effect.hashCode();
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

        if (this.hasEffect()) {
            builder.put(CraftMetaCharge.EXPLOSION.BUKKIT, this.effect);
        }

        return builder;
    }
}
