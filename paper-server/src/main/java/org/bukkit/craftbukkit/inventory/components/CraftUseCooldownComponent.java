package org.bukkit.craftbukkit.inventory.components;

import com.google.common.base.Preconditions;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.resources.MinecraftKey;
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

        this.handle = new UseCooldown(seconds, Optional.ofNullable(cooldownGroup).map(MinecraftKey::parse));
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("seconds", getCooldownSeconds());
        if (getCooldownGroup() != null) {
            result.put("cooldown-group", getCooldownGroup());
        }

        return result;
    }

    public UseCooldown getHandle() {
        return handle;
    }

    @Override
    public float getCooldownSeconds() {
        return handle.seconds();
    }

    @Override
    public void setCooldownSeconds(float cooldown) {
        Preconditions.checkArgument(cooldown > 0, "cooldown must be greater than 0");

        handle = new UseCooldown(cooldown, handle.cooldownGroup());
    }

    @Override
    public NamespacedKey getCooldownGroup() {
        return handle.cooldownGroup().map(CraftNamespacedKey::fromMinecraft).orElse(null);
    }

    @Override
    public void setCooldownGroup(NamespacedKey song) {
        handle = new UseCooldown(handle.seconds(), Optional.ofNullable(song).map(CraftNamespacedKey::toMinecraft));
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
        final CraftUseCooldownComponent other = (CraftUseCooldownComponent) obj;
        return Objects.equals(this.handle, other.handle);
    }

    @Override
    public String toString() {
        return "CraftUseCooldownComponent{" + "handle=" + handle + '}';
    }
}
