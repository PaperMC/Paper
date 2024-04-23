package org.bukkit.craftbukkit.inventory.components;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import net.minecraft.world.food.FoodInfo;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.craftbukkit.inventory.SerializableMeta;
import org.bukkit.craftbukkit.potion.CraftPotionUtil;
import org.bukkit.inventory.meta.components.FoodComponent;
import org.bukkit.potion.PotionEffect;

@SerializableAs("Food")
public final class CraftFoodComponent implements FoodComponent {

    private FoodInfo handle;

    public CraftFoodComponent(FoodInfo food) {
        this.handle = food;
    }

    public CraftFoodComponent(CraftFoodComponent food) {
        this.handle = food.handle;
    }

    public CraftFoodComponent(Map<String, Object> map) {
        Integer nutrition = SerializableMeta.getObject(Integer.class, map, "nutrition", false);
        Float saturationModifier = SerializableMeta.getObject(Float.class, map, "saturation", false);
        Boolean canAlwaysEat = SerializableMeta.getBoolean(map, "can-always-eat");

        Float eatSeconds = SerializableMeta.getObject(Float.class, map, "eat-seconds", true);
        if (eatSeconds == null) {
            eatSeconds = 1.6f;
        }

        ImmutableList.Builder<FoodEffect> effects = ImmutableList.builder();
        Iterable<?> rawEffectList = SerializableMeta.getObject(Iterable.class, map, "effects", true);
        if (rawEffectList != null) {
            for (Object obj : rawEffectList) {
                Preconditions.checkArgument(obj instanceof FoodEffect, "Object (%s) in effect list is not valid", obj.getClass());
                effects.add(new CraftFoodEffect((FoodEffect) obj));
            }
        }

        this.handle = new FoodInfo(nutrition, saturationModifier, canAlwaysEat, eatSeconds, effects.build().stream().map(CraftFoodEffect::new).map(CraftFoodEffect::getHandle).toList());
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("nutrition", getNutrition());
        result.put("saturation", getSaturation());
        result.put("can-always-eat", canAlwaysEat());
        result.put("eat-seconds", getEatSeconds());
        result.put("effects", getEffects());
        return result;
    }

    public FoodInfo getHandle() {
        return handle;
    }

    @Override
    public int getNutrition() {
        return handle.nutrition();
    }

    @Override
    public void setNutrition(int nutrition) {
        Preconditions.checkArgument(nutrition >= 0, "Nutrition cannot be negative");
        handle = new FoodInfo(nutrition, handle.saturation(), handle.canAlwaysEat(), handle.eatSeconds(), handle.effects());
    }

    @Override
    public float getSaturation() {
        return handle.saturation();
    }

    @Override
    public void setSaturation(float saturation) {
        handle = new FoodInfo(handle.nutrition(), saturation, handle.canAlwaysEat(), handle.eatSeconds(), handle.effects());
    }

    @Override
    public boolean canAlwaysEat() {
        return handle.canAlwaysEat();
    }

    @Override
    public void setCanAlwaysEat(boolean canAlwaysEat) {
        handle = new FoodInfo(handle.nutrition(), handle.saturation(), canAlwaysEat, handle.eatSeconds(), handle.effects());
    }

    @Override
    public float getEatSeconds() {
        return handle.eatSeconds();
    }

    @Override
    public void setEatSeconds(float eatSeconds) {
        handle = new FoodInfo(handle.nutrition(), handle.saturation(), handle.canAlwaysEat(), eatSeconds, handle.effects());
    }

    @Override
    public List<FoodEffect> getEffects() {
        return handle.effects().stream().map(CraftFoodEffect::new).collect(Collectors.toList());
    }

    @Override
    public void setEffects(List<FoodEffect> effects) {
        handle = new FoodInfo(handle.nutrition(), handle.saturation(), handle.canAlwaysEat(), handle.eatSeconds(), effects.stream().map(CraftFoodEffect::new).map(CraftFoodEffect::getHandle).toList());
    }

    @Override
    public FoodEffect addEffect(PotionEffect effect, float probability) {
        List<FoodInfo.b> effects = new ArrayList<>(handle.effects());

        FoodInfo.b newEffect = new net.minecraft.world.food.FoodInfo.b(CraftPotionUtil.fromBukkit(effect), probability);
        effects.add(newEffect);

        handle = new FoodInfo(handle.nutrition(), handle.saturation(), handle.canAlwaysEat(), handle.eatSeconds(), effects);

        return new CraftFoodEffect(newEffect);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.handle);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CraftFoodComponent other = (CraftFoodComponent) obj;
        return Objects.equals(this.handle, other.handle);
    }

    @Override
    public String toString() {
        return "CraftFoodComponent{" + "handle=" + handle + '}';
    }

    @SerializableAs("FoodEffect")
    public static class CraftFoodEffect implements FoodEffect {

        private FoodInfo.b handle;

        public CraftFoodEffect(FoodInfo.b handle) {
            this.handle = handle;
        }

        public CraftFoodEffect(FoodEffect bukkit) {
            this.handle = new net.minecraft.world.food.FoodInfo.b(CraftPotionUtil.fromBukkit(bukkit.getEffect()), bukkit.getProbability());
        }

        public CraftFoodEffect(Map<String, Object> map) {
            PotionEffect effect = SerializableMeta.getObject(PotionEffect.class, map, "effect", false);

            Float probability = SerializableMeta.getObject(Float.class, map, "probability", true);
            if (probability == null) {
                probability = 1.0f;
            }

            this.handle = new net.minecraft.world.food.FoodInfo.b(CraftPotionUtil.fromBukkit(effect), probability);
        }

        @Override
        public Map<String, Object> serialize() {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("effect", getEffect());
            result.put("probability", getProbability());
            return result;
        }

        public FoodInfo.b getHandle() {
            return handle;
        }

        @Override
        public PotionEffect getEffect() {
            return CraftPotionUtil.toBukkit(handle.effect());
        }

        @Override
        public void setEffect(PotionEffect effect) {
            handle = new net.minecraft.world.food.FoodInfo.b(CraftPotionUtil.fromBukkit(effect), handle.probability());
        }

        @Override
        public float getProbability() {
            return handle.probability();
        }

        @Override
        public void setProbability(float probability) {
            Preconditions.checkArgument(0 <= probability && probability <= 1, "Probability cannot be outside range [0,1]");
            handle = new net.minecraft.world.food.FoodInfo.b(handle.effect(), probability);
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 97 * hash + Objects.hashCode(this.handle);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final CraftFoodEffect other = (CraftFoodEffect) obj;
            return Objects.equals(this.handle, other.handle);
        }

        @Override
        public String toString() {
            return "CraftFoodEffect{" + "handle=" + handle + '}';
        }
    }
}
