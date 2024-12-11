package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.potion.CraftPotionEffectType;
import org.bukkit.inventory.meta.SuspiciousStewMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@DelegateDeserialization(SerializableMeta.class)
public class CraftMetaSuspiciousStew extends CraftMetaItem implements SuspiciousStewMeta {

    static final ItemMetaKeyType<SuspiciousStewEffects> EFFECTS = new ItemMetaKeyType<>(DataComponents.SUSPICIOUS_STEW_EFFECTS, "effects");

    private List<PotionEffect> customEffects;

    CraftMetaSuspiciousStew(CraftMetaItem meta) {
        super(meta);
        if (!(meta instanceof CraftMetaSuspiciousStew stewMeta)) {
            return;
        }
        if (stewMeta.hasCustomEffects()) {
            this.customEffects = new ArrayList<>(stewMeta.customEffects);
        }
    }

    CraftMetaSuspiciousStew(DataComponentPatch tag) {
        super(tag);
        getOrEmpty(tag, CraftMetaSuspiciousStew.EFFECTS).ifPresent((suspiciousStewEffects) -> {
            List<SuspiciousStewEffects.Entry> list = suspiciousStewEffects.effects();
            int length = list.size();
            this.customEffects = new ArrayList<>(length);

            for (int i = 0; i < length; i++) {
                SuspiciousStewEffects.Entry effect = list.get(i);
                PotionEffectType type = CraftPotionEffectType.minecraftHolderToBukkit(effect.effect());
                if (type == null) {
                    continue;
                }
                int duration = effect.duration();
                this.customEffects.add(new PotionEffect(type, duration, 0));
            }
        });
    }

    CraftMetaSuspiciousStew(Map<String, Object> map) {
        super(map);

        Iterable<?> rawEffectList = SerializableMeta.getObject(Iterable.class, map, CraftMetaSuspiciousStew.EFFECTS.BUKKIT, true);
        if (rawEffectList == null) {
            return;
        }

        for (Object obj : rawEffectList) {
            Preconditions.checkArgument(obj instanceof PotionEffect, "Object (%s) in effect list is not valid", obj.getClass());
            this.addCustomEffect((PotionEffect) obj, true);
        }
    }

    @Override
    void applyToItem(CraftMetaItem.Applicator tag) {
        super.applyToItem(tag);

        if (this.customEffects != null) {
            List<SuspiciousStewEffects.Entry> effectList = new ArrayList<>();

            for (PotionEffect effect : this.customEffects) {
                effectList.add(new net.minecraft.world.item.component.SuspiciousStewEffects.Entry(CraftPotionEffectType.bukkitToMinecraftHolder(effect.getType()), effect.getDuration()));
            }
            tag.put(CraftMetaSuspiciousStew.EFFECTS, new SuspiciousStewEffects(effectList));
        }
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.isStewEmpty();
    }

    boolean isStewEmpty() {
        return !this.hasCustomEffects();
    }

    @Override
    public CraftMetaSuspiciousStew clone() {
        CraftMetaSuspiciousStew clone = ((CraftMetaSuspiciousStew) super.clone());
        if (this.customEffects != null) {
            clone.customEffects = new ArrayList<>(this.customEffects);
        }
        return clone;
    }

    @Override
    public boolean hasCustomEffects() {
        return this.customEffects != null;
    }

    @Override
    public List<PotionEffect> getCustomEffects() {
        if (this.hasCustomEffects()) {
            return ImmutableList.copyOf(this.customEffects);
        }
        return ImmutableList.of();
    }

    @Override
    public boolean addCustomEffect(PotionEffect effect, boolean overwrite) {
        Preconditions.checkArgument(effect != null, "Potion effect cannot be null");

        int index = this.indexOfEffect(effect.getType());
        if (index != -1) {
            if (overwrite) {
                PotionEffect old = this.customEffects.get(index);
                if (old.getDuration() == effect.getDuration()) {
                    return false;
                }
                this.customEffects.set(index, effect);
                return true;
            } else {
                return false;
            }
        } else {
            if (this.customEffects == null) {
                this.customEffects = new ArrayList<>();
            }
            this.customEffects.add(effect);
            return true;
        }
    }

    @Override
    public boolean removeCustomEffect(PotionEffectType type) {
        Preconditions.checkArgument(type != null, "Potion effect type cannot be null");

        if (!this.hasCustomEffects()) {
            return false;
        }

        boolean changed = false;
        Iterator<PotionEffect> iterator = this.customEffects.iterator();
        while (iterator.hasNext()) {
            PotionEffect effect = iterator.next();
            if (type.equals(effect.getType())) {
                iterator.remove();
                changed = true;
            }
        }
        if (this.customEffects.isEmpty()) {
            this.customEffects = null;
        }
        return changed;
    }

    @Override
    public boolean hasCustomEffect(PotionEffectType type) {
        Preconditions.checkArgument(type != null, "Potion effect type cannot be null");
        return this.indexOfEffect(type) != -1;
    }

    private int indexOfEffect(PotionEffectType type) {
        if (!this.hasCustomEffects()) {
            return -1;
        }

        for (int i = 0; i < this.customEffects.size(); i++) {
            if (this.customEffects.get(i).getType().equals(type)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean clearCustomEffects() {
        boolean changed = this.hasCustomEffects();
        this.customEffects = null;
        return changed;
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();
        if (this.hasCustomEffects()) {
            hash = 73 * hash + this.customEffects.hashCode();
        }
        return original != hash ? CraftMetaSuspiciousStew.class.hashCode() ^ hash : hash;
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaSuspiciousStew that) {
            return (this.hasCustomEffects() ? that.hasCustomEffects() && this.customEffects.equals(that.customEffects) : !that.hasCustomEffects());
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaSuspiciousStew || this.isStewEmpty());
    }

    @Override
    Builder<String, Object> serialize(Builder<String, Object> builder) {
        super.serialize(builder);

        if (this.hasCustomEffects()) {
            builder.put(CraftMetaSuspiciousStew.EFFECTS.BUKKIT, ImmutableList.copyOf(this.customEffects));
        }

        return builder;
    }
}
