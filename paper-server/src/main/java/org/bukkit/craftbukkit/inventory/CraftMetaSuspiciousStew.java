package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    private List<io.papermc.paper.potion.SuspiciousEffectEntry> customEffects;

    CraftMetaSuspiciousStew(CraftMetaItem meta) {
        super(meta);
        if (!(meta instanceof CraftMetaSuspiciousStew suspiciousStewMeta)) {
            return;
        }
        if (suspiciousStewMeta.hasCustomEffects()) {
            this.customEffects = new ArrayList<>(suspiciousStewMeta.customEffects);
        }
    }

    CraftMetaSuspiciousStew(DataComponentPatch tag, java.util.Set<net.minecraft.core.component.DataComponentType<?>> extraHandledDcts) {
        super(tag, extraHandledDcts);
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
                this.customEffects.add(io.papermc.paper.potion.SuspiciousEffectEntry.create(type, duration));
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

            for (io.papermc.paper.potion.SuspiciousEffectEntry effect : this.customEffects) {
                effectList.add(new net.minecraft.world.item.component.SuspiciousStewEffects.Entry(CraftPotionEffectType.bukkitToMinecraftHolder(effect.effect()), effect.duration()));
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
            return this.customEffects.stream().map(suspiciousEffectEntry -> suspiciousEffectEntry.effect().createEffect(suspiciousEffectEntry.duration(), 0)).toList();
        }
        return ImmutableList.of();
    }

    @Override
    public boolean addCustomEffect(PotionEffect effect, boolean overwrite) {
        Preconditions.checkArgument(effect != null, "Potion effect cannot be null");
        return addCustomEffect(io.papermc.paper.potion.SuspiciousEffectEntry.create(effect.getType(), effect.getDuration()), overwrite);
    }

    @Override
    public boolean addCustomEffect(final io.papermc.paper.potion.SuspiciousEffectEntry suspiciousEffectEntry, final boolean overwrite) {
        Preconditions.checkArgument(suspiciousEffectEntry != null, "Suspicious effect entry cannot be null");
        if (this.hasCustomEffects()) {
            final List<io.papermc.paper.potion.SuspiciousEffectEntry> matchingEffects = this.customEffects.stream().filter(
                entry -> entry.effect() == suspiciousEffectEntry.effect()
            ).toList();
            if (!matchingEffects.isEmpty()) {
                if (overwrite) {
                    boolean foundMatchingDuration = false;
                    boolean mutated = false;
                    for (final io.papermc.paper.potion.SuspiciousEffectEntry matchingEffect : matchingEffects) {
                        if (matchingEffect.duration() != suspiciousEffectEntry.duration()) {
                            this.customEffects.remove(suspiciousEffectEntry);
                            mutated = true;
                        } else {
                            foundMatchingDuration = true;
                        }
                    }
                    if (foundMatchingDuration && !mutated) {
                        return false;
                    } else if (!foundMatchingDuration) {
                        this.customEffects.add(suspiciousEffectEntry);
                    }
                    return true;
                } else  {
                    return false;
                }
            }
        }
        if (this.customEffects == null) {
            this.customEffects = new ArrayList<>();
        }
        this.customEffects.add(suspiciousEffectEntry);
        return true;
    }

    @Override
    public boolean removeCustomEffect(PotionEffectType type) {
        Preconditions.checkArgument(type != null, "Potion effect type cannot be null");

        if (!this.hasCustomEffects()) {
            return false;
        }

        boolean changed = false;
        Iterator<io.papermc.paper.potion.SuspiciousEffectEntry> iterator = this.customEffects.iterator();
        while (iterator.hasNext()) {
            io.papermc.paper.potion.SuspiciousEffectEntry effect = iterator.next();
            if (type.equals(effect.effect())) {
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
            if (this.customEffects.get(i).effect().equals(type)) {
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
        if (meta instanceof CraftMetaSuspiciousStew other) {
            return Objects.equals(this.customEffects, other.customEffects);
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
            builder.put(CraftMetaSuspiciousStew.EFFECTS.BUKKIT, ImmutableList.copyOf(com.google.common.collect.Lists.transform(this.customEffects, s -> new PotionEffect(s.effect(), s.duration(), 0)))); // convert back to potion effect for bukkit legacy item serialisation to maintain backwards compatibility for the written format.
        }

        return builder;
    }
}
