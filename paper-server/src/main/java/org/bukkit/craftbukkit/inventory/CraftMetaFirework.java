package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap.Builder;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.FireworkExplosion;
import net.minecraft.world.item.component.Fireworks;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.inventory.CraftMetaItem.ItemMetaKey.Specific;
import org.bukkit.craftbukkit.inventory.CraftMetaItem.ItemMetaKey.Specific.To;
import org.bukkit.inventory.meta.FireworkMeta;

@DelegateDeserialization(SerializableMeta.class)
class CraftMetaFirework extends CraftMetaItem implements FireworkMeta {
    /*
       "Fireworks", "Explosion", "Explosions", "Flight", "Type", "Trail", "Flicker", "Colors", "FadeColors";

        Fireworks
        - Compound: Fireworks
        -- Byte: Flight
        -- List: Explosions
        --- Compound: Explosion
        ---- IntArray: Colors
        ---- Byte: Type
        ---- Boolean: Trail
        ---- Boolean: Flicker
        ---- IntArray: FadeColors
     */

    @Specific(To.NBT)
    static final ItemMetaKeyType<Fireworks> FIREWORKS = new ItemMetaKeyType<>(DataComponents.FIREWORKS, "Fireworks");
    static final ItemMetaKey FLIGHT = new ItemMetaKey("power");
    static final ItemMetaKey EXPLOSIONS = new ItemMetaKey("firework-effects");

    private List<FireworkEffect> effects;
    private int power;

    CraftMetaFirework(CraftMetaItem meta) {
        super(meta);

        if (!(meta instanceof CraftMetaFirework that)) {
            return;
        }

        this.power = that.power;

        if (that.hasEffects()) {
            this.effects = new ArrayList<>(that.effects);
        }
    }

    CraftMetaFirework(DataComponentPatch tag) {
        super(tag);

        getOrEmpty(tag, FIREWORKS).ifPresent((fireworks) -> {
            power = fireworks.flightDuration();

            List<FireworkExplosion> fireworkEffects = fireworks.explosions();
            List<FireworkEffect> effects = this.effects = new ArrayList<FireworkEffect>(fireworkEffects.size());

            for (int i = 0; i < fireworkEffects.size(); i++) {
                try {
                    effects.add(getEffect(fireworkEffects.get(i)));
                } catch (IllegalArgumentException ex) {
                    // Ignore invalid effects
                }
            }
        });
    }

    static FireworkEffect getEffect(FireworkExplosion explosion) {
        FireworkEffect.Builder effect = FireworkEffect.builder()
                .flicker(explosion.hasTwinkle())
                .trail(explosion.hasTrail())
                .with(getEffectType(explosion.shape()));

        IntList colors = explosion.colors();
        // People using buggy command generators specify a list rather than an int here, so recover with dummy data.
        // Wrong: Colors: [1234]
        // Right: Colors: [I;1234]
        if (colors.isEmpty()) {
            effect.withColor(Color.WHITE);
        }

        for (int color : colors) {
            effect.withColor(Color.fromRGB(color));
        }

        for (int color : explosion.fadeColors()) {
            effect.withFade(Color.fromRGB(color));
        }

        return effect.build();
    }

    static FireworkExplosion getExplosion(FireworkEffect effect) {
        IntList colors = addColors(effect.getColors());
        IntList fadeColors = addColors(effect.getFadeColors());

        return new FireworkExplosion(getNBT(effect.getType()), colors, fadeColors, effect.hasTrail(), effect.hasFlicker());
    }

    static FireworkExplosion.a getNBT(Type type) {
        switch (type) {
            case BALL:
                return FireworkExplosion.a.SMALL_BALL;
            case BALL_LARGE:
                return FireworkExplosion.a.LARGE_BALL;
            case STAR:
                return FireworkExplosion.a.STAR;
            case CREEPER:
                return FireworkExplosion.a.CREEPER;
            case BURST:
                return FireworkExplosion.a.BURST;
            default:
                throw new IllegalArgumentException("Unknown effect type " + type);
        }
    }

    static Type getEffectType(FireworkExplosion.a nbt) {
        switch (nbt) {
            case SMALL_BALL:
                return Type.BALL;
            case LARGE_BALL:
                return Type.BALL_LARGE;
            case STAR:
                return Type.STAR;
            case CREEPER:
                return Type.CREEPER;
            case BURST:
                return Type.BURST;
            default:
                throw new IllegalArgumentException("Unknown effect type " + nbt);
        }
    }

    CraftMetaFirework(Map<String, Object> map) {
        super(map);

        Integer power = SerializableMeta.getObject(Integer.class, map, FLIGHT.BUKKIT, true);
        if (power != null) {
            this.power = power;
        }

        Iterable<?> effects = SerializableMeta.getObject(Iterable.class, map, EXPLOSIONS.BUKKIT, true);
        safelyAddEffects(effects);
    }

    @Override
    public boolean hasEffects() {
        return !(effects == null || effects.isEmpty());
    }

    void safelyAddEffects(Iterable<?> collection) {
        if (collection == null || (collection instanceof Collection && ((Collection<?>) collection).isEmpty())) {
            return;
        }

        List<FireworkEffect> effects = this.effects;
        if (effects == null) {
            effects = this.effects = new ArrayList<>();
        }

        for (Object obj : collection) {
            Preconditions.checkArgument(obj instanceof FireworkEffect, "%s in %s is not a FireworkEffect", obj, collection);
            effects.add((FireworkEffect) obj);
        }
    }

    @Override
    void applyToItem(CraftMetaItem.Applicator itemTag) {
        super.applyToItem(itemTag);
        if (isFireworkEmpty()) {
            return;
        }

        List<FireworkExplosion> effects = new ArrayList<>();
        for (FireworkEffect effect : this.effects) {
            effects.add(getExplosion(effect));
        }

        itemTag.put(FIREWORKS, new Fireworks(power, effects));
    }

    static IntList addColors(List<Color> colors) {
        if (colors.isEmpty()) {
            return IntList.of();
        }

        final int[] colorArray = new int[colors.size()];
        int i = 0;
        for (Color color : colors) {
            colorArray[i++] = color.asRGB();
        }

        return IntList.of(colorArray);
    }

    @Override
    boolean applicableTo(Material type) {
        return type == Material.FIREWORK_ROCKET;
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && isFireworkEmpty();
    }

    boolean isFireworkEmpty() {
        return !(hasEffects() || hasPower());
    }

    boolean hasPower() {
        return power != 0;
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }

        if (meta instanceof CraftMetaFirework that) {

            return (hasPower() ? that.hasPower() && this.power == that.power : !that.hasPower())
                    && (hasEffects() ? that.hasEffects() && this.effects.equals(that.effects) : !that.hasEffects());
        }

        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaFirework || isFireworkEmpty());
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();
        if (hasPower()) {
            hash = 61 * hash + power;
        }
        if (hasEffects()) {
            hash = 61 * hash + 13 * effects.hashCode();
        }
        return hash != original ? CraftMetaFirework.class.hashCode() ^ hash : hash;
    }

    @Override
    Builder<String, Object> serialize(Builder<String, Object> builder) {
        super.serialize(builder);

        if (hasEffects()) {
            builder.put(EXPLOSIONS.BUKKIT, ImmutableList.copyOf(effects));
        }

        if (hasPower()) {
            builder.put(FLIGHT.BUKKIT, power);
        }

        return builder;
    }

    @Override
    public CraftMetaFirework clone() {
        CraftMetaFirework meta = (CraftMetaFirework) super.clone();

        if (this.effects != null) {
            meta.effects = new ArrayList<FireworkEffect>(this.effects);
        }

        return meta;
    }

    @Override
    public void addEffect(FireworkEffect effect) {
        Preconditions.checkArgument(effect != null, "FireworkEffect cannot be null");
        if (this.effects == null) {
            this.effects = new ArrayList<FireworkEffect>();
        }
        this.effects.add(effect);
    }

    @Override
    public void addEffects(FireworkEffect... effects) {
        Preconditions.checkArgument(effects != null, "effects cannot be null");
        if (effects.length == 0) {
            return;
        }

        List<FireworkEffect> list = this.effects;
        if (list == null) {
            list = this.effects = new ArrayList<FireworkEffect>();
        }

        for (FireworkEffect effect : effects) {
            Preconditions.checkArgument(effect != null, "effects cannot contain null FireworkEffect");
            list.add(effect);
        }
    }

    @Override
    public void addEffects(Iterable<FireworkEffect> effects) {
        Preconditions.checkArgument(effects != null, "effects cannot be null");
        safelyAddEffects(effects);
    }

    @Override
    public List<FireworkEffect> getEffects() {
        return this.effects == null ? ImmutableList.<FireworkEffect>of() : ImmutableList.copyOf(this.effects);
    }

    @Override
    public int getEffectsSize() {
        return this.effects == null ? 0 : this.effects.size();
    }

    @Override
    public void removeEffect(int index) {
        if (this.effects == null) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: 0");
        } else {
            this.effects.remove(index);
        }
    }

    @Override
    public void clearEffects() {
        this.effects = null;
    }

    @Override
    public int getPower() {
        return hasPower() ? this.power : 0;
    }

    @Override
    public void setPower(int power) {
        Preconditions.checkArgument(power >= 0, "power cannot be less than zero: %s", power);
        Preconditions.checkArgument(power < 0x80, "power cannot be more than 127: %s", power);
        this.power = power;
    }
}
