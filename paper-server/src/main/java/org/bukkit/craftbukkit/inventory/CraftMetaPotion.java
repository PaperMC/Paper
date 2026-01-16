package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import org.bukkit.Color;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.potion.CraftPotionEffectType;
import org.bukkit.craftbukkit.potion.CraftPotionType;
import org.bukkit.craftbukkit.potion.CraftPotionUtil;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

@DelegateDeserialization(SerializableMeta.class)
class CraftMetaPotion extends CraftMetaItem implements PotionMeta {

    @ItemMetaKey.Specific(ItemMetaKey.Specific.To.NBT)
    static final ItemMetaKeyType<PotionContents> POTION_CONTENTS = new ItemMetaKeyType<>(DataComponents.POTION_CONTENTS);
    static final ItemMetaKey POTION_EFFECTS = new ItemMetaKey("custom-effects");
    static final ItemMetaKey POTION_COLOR = new ItemMetaKey("custom-color");
    static final ItemMetaKey CUSTOM_NAME = new ItemMetaKey("custom-name");
    static final ItemMetaKey DEFAULT_POTION = new ItemMetaKey("potion-type");

    private PotionType type;
    private List<PotionEffect> customEffects;
    private Integer color; // Paper - keep color component consistent with vanilla (top byte is ignored)
    private String customName;

    CraftMetaPotion(CraftMetaItem meta) {
        super(meta);
        if (!(meta instanceof CraftMetaPotion potionMeta)) {
            return;
        }
        this.type = potionMeta.type;
        this.color = potionMeta.color;
        this.customName = potionMeta.customName;
        if (potionMeta.hasCustomEffects()) {
            this.customEffects = new ArrayList<>(potionMeta.customEffects);
        }
    }

    CraftMetaPotion(DataComponentPatch tag, java.util.Set<net.minecraft.core.component.DataComponentType<?>> extraHandledDcts) {
        super(tag, extraHandledDcts);
        getOrEmpty(tag, CraftMetaPotion.POTION_CONTENTS).ifPresent((potionContents) -> {
            potionContents.potion().ifPresent((potion) -> {
                this.type = CraftPotionType.minecraftHolderToBukkit(potion);
            });

            potionContents.customColor().ifPresent((customColor) -> {
                try {
                    this.color = customColor;
                } catch (IllegalArgumentException ex) {
                    // Invalid colour
                }
            });

            potionContents.customName().ifPresent((name) -> {
                this.customName = name;
            });

            List<MobEffectInstance> list = potionContents.customEffects();
            int length = list.size();
            this.customEffects = new ArrayList<>(length);

            for (int i = 0; i < length; i++) {
                MobEffectInstance effect = list.get(i);
                PotionEffectType type = CraftPotionEffectType.minecraftHolderToBukkit(effect.getEffect());
                // SPIGOT-4047: Vanilla just disregards these
                if (type == null) {
                    continue;
                }

                int amp = effect.getAmplifier();
                int duration = effect.getDuration();
                boolean ambient = effect.isAmbient();
                boolean particles = effect.isVisible();
                boolean icon = effect.showIcon();
                this.customEffects.add(new PotionEffect(type, duration, amp, ambient, particles, icon));
            }
        });
    }

    CraftMetaPotion(Map<String, Object> map) {
        super(map);
        String typeString = SerializableMeta.getString(map, CraftMetaPotion.DEFAULT_POTION.BUKKIT, true);
        if (typeString != null) {
            this.type = CraftPotionType.stringToBukkit(typeString);
        }

        Color color = SerializableMeta.getObject(Color.class, map, CraftMetaPotion.POTION_COLOR.BUKKIT, true);
        if (color != null) {
            this.setColor(color);
        }

        String name = SerializableMeta.getString(map, CraftMetaPotion.CUSTOM_NAME.BUKKIT, true);
        if (name != null) {
            this.setCustomPotionName(name);
        }

        Iterable<?> rawEffectList = SerializableMeta.getObject(Iterable.class, map, CraftMetaPotion.POTION_EFFECTS.BUKKIT, true);
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

        if (this.isPotionEmpty()) {
            return;
        }

        Optional<Holder<Potion>> defaultPotion = (this.hasBasePotionType()) ? Optional.of(CraftPotionType.bukkitToMinecraftHolder(this.type)) : Optional.empty();
        Optional<Integer> potionColor = (this.hasColor()) ? Optional.of(this.color) : Optional.empty();
        Optional<String> customName = Optional.ofNullable(this.customName);

        List<MobEffectInstance> effectList = new ArrayList<>();
        if (this.customEffects != null) {
            for (PotionEffect effect : this.customEffects) {
                effectList.add(new MobEffectInstance(CraftPotionEffectType.bukkitToMinecraftHolder(effect.getType()), effect.getDuration(), effect.getAmplifier(), effect.isAmbient(), effect.hasParticles(), effect.hasIcon()));
            }
        }

        tag.put(CraftMetaPotion.POTION_CONTENTS, new PotionContents(defaultPotion, potionColor, effectList, customName));
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.isPotionEmpty();
    }

    boolean isPotionEmpty() {
        return (this.type == null) && !(this.hasCustomEffects() || this.hasColor() || this.hasCustomPotionName());
    }

    @Override
    public CraftMetaPotion clone() {
        CraftMetaPotion clone = (CraftMetaPotion) super.clone();
        clone.type = this.type;
        if (this.customEffects != null) {
            clone.customEffects = new ArrayList<>(this.customEffects);
        }
        return clone;
    }

    @Override
    public void setBasePotionData(PotionData data) {
        this.setBasePotionType(CraftPotionUtil.fromBukkit(data));
    }

    @Override
    public PotionData getBasePotionData() {
        return CraftPotionUtil.toBukkit(this.getBasePotionType());
    }

    @Override
    public void setBasePotionType(PotionType potionType) {
        this.type = potionType;
    }

    @Override
    public PotionType getBasePotionType() {
        return this.type;
    }

    @Override
    public boolean hasBasePotionType() {
        return this.type != null;
    }

    @Override
    public boolean hasCustomEffects() {
        return this.customEffects != null && !this.customEffects.isEmpty();
    }

    @Override
    public List<PotionEffect> getCustomEffects() {
        if (this.hasCustomEffects()) {
            return ImmutableList.copyOf(this.customEffects);
        }
        return ImmutableList.of();
    }

    @Override
    @NotNull
    public List<PotionEffect> getAllEffects() {
        final ImmutableList.Builder<PotionEffect> builder = ImmutableList.builder();
        if (this.hasBasePotionType()) {
            builder.addAll(this.getBasePotionType().getPotionEffects());
        }
        if (this.hasCustomEffects()) {
            builder.addAll(this.customEffects);
        }
        return builder.build();
    }

    @Override
    public boolean addCustomEffect(PotionEffect effect, boolean overwrite) {
        Preconditions.checkArgument(effect != null, "Potion effect cannot be null");

        int index = this.indexOfEffect(effect.getType());
        if (index != -1) {
            if (overwrite) {
                PotionEffect old = this.customEffects.get(index);
                if (old.getAmplifier() == effect.getAmplifier() && old.getDuration() == effect.getDuration() && old.isAmbient() == effect.isAmbient()) {
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

    @Override
    public boolean setMainEffect(PotionEffectType type) {
        Preconditions.checkArgument(type != null, "Potion effect type cannot be null");
        int index = this.indexOfEffect(type);
        if (index == -1 || index == 0) {
            return false;
        }

        PotionEffect old = this.customEffects.get(0);
        this.customEffects.set(0, this.customEffects.get(index));
        this.customEffects.set(index, old);
        return true;
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
    public boolean hasColor() {
        return this.color != null;
    }

    @Override
    public Color getColor() {
        return this.color == null ? null : Color.fromRGB(this.color & 0x00FFFFFF);
    }

    @Override
    public void setColor(Color color) {
        this.color = color == null ? null : color.asRGB();
    }

    @Override
    @NotNull
    public Color computeEffectiveColor() {
        if (this.hasColor()) return this.getColor();

        return Color.fromRGB(
            PotionContents.getColorOptional(Collections2.transform(getAllEffects(), CraftPotionUtil::fromBukkit))
                .orElse(PotionContents.BASE_POTION_COLOR) & 0x00FFFFFF
        );
    }

    @Override
    public boolean hasCustomPotionName() {
        return this.customName != null;
    }

    @Override
    public String getCustomPotionName() {
        return this.customName;
    }

    @Override
    public void setCustomPotionName(String customName) {
        Preconditions.checkArgument(customName == null || customName.length() <= 32767, "Custom name is longer than 32767 characters");
        this.customName = customName;
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();
        if (this.type != null) {
            hash = 73 * hash + this.type.hashCode();
        }
        if (this.hasColor()) {
            hash = 73 * hash + this.color.hashCode();
        }
        if (this.hasCustomPotionName()) {
            hash = 73 * hash + this.customName.hashCode();
        }
        if (this.hasCustomEffects()) {
            hash = 73 * hash + this.customEffects.hashCode();
        }
        return original != hash ? CraftMetaPotion.class.hashCode() ^ hash : hash;
    }

    @Override
    public boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof final CraftMetaPotion other) {
            return Objects.equals(this.type, other.type)
                    && (this.hasCustomEffects() ? other.hasCustomEffects() && this.customEffects.equals(other.customEffects) : !other.hasCustomEffects())
                    && Objects.equals(this.color, other.color)
                    && Objects.equals(this.customName, other.customName);
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaPotion || this.isPotionEmpty());
    }

    @Override
    Builder<String, Object> serialize(Builder<String, Object> builder) {
        super.serialize(builder);
        if (this.type != null) {
            builder.put(CraftMetaPotion.DEFAULT_POTION.BUKKIT, CraftPotionType.bukkitToString(this.type));
        }

        if (this.hasColor()) {
            builder.put(CraftMetaPotion.POTION_COLOR.BUKKIT, this.getColor());
        }

        if (this.hasCustomPotionName()) {
            builder.put(CraftMetaPotion.CUSTOM_NAME.BUKKIT, this.getCustomPotionName());
        }

        if (this.hasCustomEffects()) {
            builder.put(CraftMetaPotion.POTION_EFFECTS.BUKKIT, ImmutableList.copyOf(this.customEffects));
        }

        return builder;
    }
}
