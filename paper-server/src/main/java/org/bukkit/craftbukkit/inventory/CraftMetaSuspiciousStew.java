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
import org.bukkit.Material;
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
        getOrEmpty(tag, EFFECTS).ifPresent((suspiciousStewEffects) -> {
            List<SuspiciousStewEffects.a> list = suspiciousStewEffects.effects();
            int length = list.size();
            customEffects = new ArrayList<>(length);

            for (int i = 0; i < length; i++) {
                SuspiciousStewEffects.a effect = list.get(i);
                PotionEffectType type = CraftPotionEffectType.minecraftHolderToBukkit(effect.effect());
                if (type == null) {
                    continue;
                }
                int duration = effect.duration();
                customEffects.add(new PotionEffect(type, duration, 0));
            }
        });
    }

    CraftMetaSuspiciousStew(Map<String, Object> map) {
        super(map);

        Iterable<?> rawEffectList = SerializableMeta.getObject(Iterable.class, map, EFFECTS.BUKKIT, true);
        if (rawEffectList == null) {
            return;
        }

        for (Object obj : rawEffectList) {
            Preconditions.checkArgument(obj instanceof PotionEffect, "Object (%s) in effect list is not valid", obj.getClass());
            addCustomEffect((PotionEffect) obj, true);
        }
    }

    @Override
    void applyToItem(CraftMetaItem.Applicator tag) {
        super.applyToItem(tag);

        if (customEffects != null) {
            List<SuspiciousStewEffects.a> effectList = new ArrayList<>();

            for (PotionEffect effect : customEffects) {
                effectList.add(new net.minecraft.world.item.component.SuspiciousStewEffects.a(CraftPotionEffectType.bukkitToMinecraftHolder(effect.getType()), effect.getDuration()));
            }
            tag.put(EFFECTS, new SuspiciousStewEffects(effectList));
        }
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && isStewEmpty();
    }

    boolean isStewEmpty() {
        return !hasCustomEffects();
    }

    @Override
    boolean applicableTo(Material type) {
        return type == Material.SUSPICIOUS_STEW;
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
        return customEffects != null;
    }

    @Override
    public List<PotionEffect> getCustomEffects() {
        if (hasCustomEffects()) {
            return ImmutableList.copyOf(customEffects);
        }
        return ImmutableList.of();
    }

    @Override
    public boolean addCustomEffect(PotionEffect effect, boolean overwrite) {
        Preconditions.checkArgument(effect != null, "Potion effect cannot be null");

        int index = indexOfEffect(effect.getType());
        if (index != -1) {
            if (overwrite) {
                PotionEffect old = customEffects.get(index);
                if (old.getDuration() == effect.getDuration()) {
                    return false;
                }
                customEffects.set(index, effect);
                return true;
            } else {
                return false;
            }
        } else {
            if (customEffects == null) {
                customEffects = new ArrayList<>();
            }
            customEffects.add(effect);
            return true;
        }
    }

    @Override
    public boolean removeCustomEffect(PotionEffectType type) {
        Preconditions.checkArgument(type != null, "Potion effect type cannot be null");

        if (!hasCustomEffects()) {
            return false;
        }

        boolean changed = false;
        Iterator<PotionEffect> iterator = customEffects.iterator();
        while (iterator.hasNext()) {
            PotionEffect effect = iterator.next();
            if (type.equals(effect.getType())) {
                iterator.remove();
                changed = true;
            }
        }
        if (customEffects.isEmpty()) {
            customEffects = null;
        }
        return changed;
    }

    @Override
    public boolean hasCustomEffect(PotionEffectType type) {
        Preconditions.checkArgument(type != null, "Potion effect type cannot be null");
        return indexOfEffect(type) != -1;
    }

    private int indexOfEffect(PotionEffectType type) {
        if (!hasCustomEffects()) {
            return -1;
        }

        for (int i = 0; i < customEffects.size(); i++) {
            if (customEffects.get(i).getType().equals(type)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean clearCustomEffects() {
        boolean changed = hasCustomEffects();
        customEffects = null;
        return changed;
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();
        if (hasCustomEffects()) {
            hash = 73 * hash + customEffects.hashCode();
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
        return super.notUncommon(meta) && (meta instanceof CraftMetaSuspiciousStew || isStewEmpty());
    }

    @Override
    Builder<String, Object> serialize(Builder<String, Object> builder) {
        super.serialize(builder);

        if (hasCustomEffects()) {
            builder.put(EFFECTS.BUKKIT, ImmutableList.copyOf(this.customEffects));
        }

        return builder;
    }
}
