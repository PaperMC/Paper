package org.bukkit.craftbukkit.inventory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.apache.commons.lang.Validate;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.inventory.CraftMetaItem.SerializableMeta;
import org.bukkit.craftbukkit.potion.CraftPotionUtil;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

@DelegateDeserialization(SerializableMeta.class)
class CraftMetaPotion extends CraftMetaItem implements PotionMeta {

    private static final Set<Material> POTION_MATERIALS = Sets.newHashSet(
            Material.POTION,
            Material.SPLASH_POTION,
            Material.LINGERING_POTION,
            Material.TIPPED_ARROW
    );

    static final ItemMetaKey AMPLIFIER = new ItemMetaKey("Amplifier", "amplifier");
    static final ItemMetaKey AMBIENT = new ItemMetaKey("Ambient", "ambient");
    static final ItemMetaKey DURATION = new ItemMetaKey("Duration", "duration");
    static final ItemMetaKey SHOW_PARTICLES = new ItemMetaKey("ShowParticles", "has-particles");
    static final ItemMetaKey SHOW_ICON = new ItemMetaKey("ShowIcon", "has-icon");
    static final ItemMetaKey POTION_EFFECTS = new ItemMetaKey("CustomPotionEffects", "custom-effects");
    static final ItemMetaKey POTION_COLOR = new ItemMetaKey("CustomPotionColor", "custom-color");
    static final ItemMetaKey ID = new ItemMetaKey("Id", "potion-id");
    static final ItemMetaKey DEFAULT_POTION = new ItemMetaKey("Potion", "potion-type");

    // Having an initial "state" in ItemMeta seems bit dirty but the UNCRAFTABLE potion type
    // is treated as the empty form of the meta because it represents an empty potion with no effect
    private PotionData type = new PotionData(PotionType.UNCRAFTABLE, false, false);
    private List<PotionEffect> customEffects;
    private Color color;

    CraftMetaPotion(CraftMetaItem meta) {
        super(meta);
        if (!(meta instanceof CraftMetaPotion)) {
            return;
        }
        CraftMetaPotion potionMeta = (CraftMetaPotion) meta;
        this.type = potionMeta.type;
        this.color = potionMeta.color;
        if (potionMeta.hasCustomEffects()) {
            this.customEffects = new ArrayList<PotionEffect>(potionMeta.customEffects);
        }
    }

    CraftMetaPotion(NBTTagCompound tag) {
        super(tag);
        if (tag.contains(DEFAULT_POTION.NBT)) {
            type = CraftPotionUtil.toBukkit(tag.getString(DEFAULT_POTION.NBT));
        }
        if (tag.contains(POTION_COLOR.NBT)) {
            try {
                color = Color.fromRGB(tag.getInt(POTION_COLOR.NBT));
            } catch (IllegalArgumentException ex) {
                // Invalid colour
            }
        }
        if (tag.contains(POTION_EFFECTS.NBT)) {
            NBTTagList list = tag.getList(POTION_EFFECTS.NBT, CraftMagicNumbers.NBT.TAG_COMPOUND);
            int length = list.size();
            customEffects = new ArrayList<PotionEffect>(length);

            for (int i = 0; i < length; i++) {
                NBTTagCompound effect = list.getCompound(i);
                PotionEffectType type = PotionEffectType.getById(effect.getByte(ID.NBT));
                // SPIGOT-4047: Vanilla just disregards these
                if (type == null) {
                    continue;
                }

                int amp = effect.getByte(AMPLIFIER.NBT);
                int duration = effect.getInt(DURATION.NBT);
                boolean ambient = effect.getBoolean(AMBIENT.NBT);
                boolean particles = effect.contains(SHOW_PARTICLES.NBT, CraftMagicNumbers.NBT.TAG_BYTE) ? effect.getBoolean(SHOW_PARTICLES.NBT) : true;
                boolean icon = effect.contains(SHOW_ICON.NBT, CraftMagicNumbers.NBT.TAG_BYTE) ? effect.getBoolean(SHOW_ICON.NBT) : particles;
                customEffects.add(new PotionEffect(type, duration, amp, ambient, particles, icon));
            }
        }
    }

    CraftMetaPotion(Map<String, Object> map) {
        super(map);
        type = CraftPotionUtil.toBukkit(SerializableMeta.getString(map, DEFAULT_POTION.BUKKIT, true));

        Color color = SerializableMeta.getObject(Color.class, map, POTION_COLOR.BUKKIT, true);
        if (color != null) {
            setColor(color);
        }

        Iterable<?> rawEffectList = SerializableMeta.getObject(Iterable.class, map, POTION_EFFECTS.BUKKIT, true);
        if (rawEffectList == null) {
            return;
        }

        for (Object obj : rawEffectList) {
            if (!(obj instanceof PotionEffect)) {
                throw new IllegalArgumentException("Object in effect list is not valid. " + obj.getClass());
            }
            addCustomEffect((PotionEffect) obj, true);
        }
    }

    @Override
    void applyToItem(NBTTagCompound tag) {
        super.applyToItem(tag);

        tag.putString(DEFAULT_POTION.NBT, CraftPotionUtil.fromBukkit(type));

        if (hasColor()) {
            tag.putInt(POTION_COLOR.NBT, color.asRGB());
        }

        if (customEffects != null) {
            NBTTagList effectList = new NBTTagList();
            tag.put(POTION_EFFECTS.NBT, effectList);

            for (PotionEffect effect : customEffects) {
                NBTTagCompound effectData = new NBTTagCompound();
                effectData.putByte(ID.NBT, (byte) effect.getType().getId());
                effectData.putByte(AMPLIFIER.NBT, (byte) effect.getAmplifier());
                effectData.putInt(DURATION.NBT, effect.getDuration());
                effectData.putBoolean(AMBIENT.NBT, effect.isAmbient());
                effectData.putBoolean(SHOW_PARTICLES.NBT, effect.hasParticles());
                effectData.putBoolean(SHOW_ICON.NBT, effect.hasIcon());
                effectList.add(effectData);
            }
        }
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && isPotionEmpty();
    }

    boolean isPotionEmpty() {
        return (type.getType() == PotionType.UNCRAFTABLE) && !(hasCustomEffects() || hasColor());
    }

    @Override
    boolean applicableTo(Material type) {
        return POTION_MATERIALS.contains(type);
    }

    @Override
    public CraftMetaPotion clone() {
        CraftMetaPotion clone = (CraftMetaPotion) super.clone();
        clone.type = type;
        if (this.customEffects != null) {
            clone.customEffects = new ArrayList<PotionEffect>(this.customEffects);
        }
        return clone;
    }

    @Override
    public void setBasePotionData(PotionData data) {
        Validate.notNull(data, "PotionData cannot be null");
        this.type = data;
    }

    @Override
    public PotionData getBasePotionData() {
        return type;
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
        Validate.notNull(effect, "Potion effect must not be null");

        int index = indexOfEffect(effect.getType());
        if (index != -1) {
            if (overwrite) {
                PotionEffect old = customEffects.get(index);
                if (old.getAmplifier() == effect.getAmplifier() && old.getDuration() == effect.getDuration() && old.isAmbient() == effect.isAmbient()) {
                    return false;
                }
                customEffects.set(index, effect);
                return true;
            } else {
                return false;
            }
        } else {
            if (customEffects == null) {
                customEffects = new ArrayList<PotionEffect>();
            }
            customEffects.add(effect);
            return true;
        }
    }

    @Override
    public boolean removeCustomEffect(PotionEffectType type) {
        Validate.notNull(type, "Potion effect type must not be null");

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
        Validate.notNull(type, "Potion effect type must not be null");
        return indexOfEffect(type) != -1;
    }

    @Override
    public boolean setMainEffect(PotionEffectType type) {
        Validate.notNull(type, "Potion effect type must not be null");
        int index = indexOfEffect(type);
        if (index == -1 || index == 0) {
            return false;
        }

        PotionEffect old = customEffects.get(0);
        customEffects.set(0, customEffects.get(index));
        customEffects.set(index, old);
        return true;
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
    public boolean hasColor() {
        return color != null;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();
        if (type.getType() != PotionType.UNCRAFTABLE) {
            hash = 73 * hash + type.hashCode();
        }
        if (hasColor()) {
            hash = 73 * hash + color.hashCode();
        }
        if (hasCustomEffects()) {
            hash = 73 * hash + customEffects.hashCode();
        }
        return original != hash ? CraftMetaPotion.class.hashCode() ^ hash : hash;
    }

    @Override
    public boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaPotion) {
            CraftMetaPotion that = (CraftMetaPotion) meta;

            return type.equals(that.type)
                    && (this.hasCustomEffects() ? that.hasCustomEffects() && this.customEffects.equals(that.customEffects) : !that.hasCustomEffects())
                    && (this.hasColor() ? that.hasColor() && this.color.equals(that.color) : !that.hasColor());
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaPotion || isPotionEmpty());
    }

    @Override
    Builder<String, Object> serialize(Builder<String, Object> builder) {
        super.serialize(builder);
        if (type.getType() != PotionType.UNCRAFTABLE) {
            builder.put(DEFAULT_POTION.BUKKIT, CraftPotionUtil.fromBukkit(type));
        }

        if (hasColor()) {
            builder.put(POTION_COLOR.BUKKIT, getColor());
        }

        if (hasCustomEffects()) {
            builder.put(POTION_EFFECTS.BUKKIT, ImmutableList.copyOf(this.customEffects));
        }

        return builder;
    }
}
