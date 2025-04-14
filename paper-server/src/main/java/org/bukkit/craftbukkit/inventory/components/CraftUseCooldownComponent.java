package org.bukkit.craftbukkit.inventory.components;

import com.google.common.base.Preconditions;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.component.UseCooldown;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.craftbukkit.inventory.SerializableMeta;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.inventory.meta.components.UseCooldownComponent;

@SerializableAs("UseCooldown")
public final class CraftUseCooldownComponent implements UseCooldownComponent {

    private UseCooldown handle;

    public CraftUseCooldownComponent(UseCooldown cooldown) {
        this.handle = cooldown;
    }

    public CraftUseCooldownComponent(CraftUseCooldownComponent food) {
        this.handle = food.handle;
    }

    public CraftUseCooldownComponent(Map<String, Object> map) {
        Float seconds = SerializableMeta.getObject(Float.class, map, "seconds", false);
        String cooldownGroup = SerializableMeta.getString(map, "cooldown-group", true);

        this.handle = new UseCooldown(seconds, Optional.ofNullable(cooldownGroup).map(ResourceLocation::parse));
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("seconds", this.getCooldownSeconds());
        if (this.getCooldownGroup() != null) {
            result.put("cooldown-group", this.getCooldownGroup().toString());
        }

        return result;
    }

    public UseCooldown getHandle() {
        return this.handle;
    }

    @Override
    public float getCooldownSeconds() {
        return this.handle.seconds();
    }

    @Override
    public void setCooldownSeconds(float cooldown) {
        Preconditions.checkArgument(cooldown > 0, "cooldown must be greater than 0");

        this.handle = new UseCooldown(cooldown, this.handle.cooldownGroup());
    }

    @Override
    public NamespacedKey getCooldownGroup() {
        return this.handle.cooldownGroup().map(CraftNamespacedKey::fromMinecraft).orElse(null);
    }

    @Override
    public void setCooldownGroup(NamespacedKey song) {
        this.handle = new UseCooldown(this.handle.seconds(), Optional.ofNullable(song).map(CraftNamespacedKey::toMinecraft));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + this.handle.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        final CraftUseCooldownComponent other = (CraftUseCooldownComponent) obj;
        return this.handle.equals(other.handle);
    }

    @Override
    public String toString() {
        return "CraftUseCooldownComponent{component=" + this.handle + '}';
    }
}
