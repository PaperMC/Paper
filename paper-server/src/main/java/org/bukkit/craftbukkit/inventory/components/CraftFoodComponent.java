package org.bukkit.craftbukkit.inventory.components;

import com.google.common.base.Preconditions;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import net.minecraft.world.food.FoodInfo;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.craftbukkit.inventory.SerializableMeta;
import org.bukkit.inventory.meta.components.FoodComponent;

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

        this.handle = new FoodInfo(nutrition, saturationModifier, canAlwaysEat);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("nutrition", getNutrition());
        result.put("saturation", getSaturation());
        result.put("can-always-eat", canAlwaysEat());

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
        handle = new FoodInfo(nutrition, handle.saturation(), handle.canAlwaysEat());
    }

    @Override
    public float getSaturation() {
        return handle.saturation();
    }

    @Override
    public void setSaturation(float saturation) {
        handle = new FoodInfo(handle.nutrition(), saturation, handle.canAlwaysEat());
    }

    @Override
    public boolean canAlwaysEat() {
        return handle.canAlwaysEat();
    }

    @Override
    public void setCanAlwaysEat(boolean canAlwaysEat) {
        handle = new FoodInfo(handle.nutrition(), handle.saturation(), canAlwaysEat);
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
}
