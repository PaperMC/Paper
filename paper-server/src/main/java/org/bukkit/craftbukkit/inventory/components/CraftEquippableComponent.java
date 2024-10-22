package org.bukkit.craftbukkit.inventory.components;

import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.sounds.SoundEffects;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.item.equipment.Equippable;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.Tag;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.craftbukkit.CraftEquipmentSlot;
import org.bukkit.craftbukkit.CraftSound;
import org.bukkit.craftbukkit.entity.CraftEntityType;
import org.bukkit.craftbukkit.inventory.SerializableMeta;
import org.bukkit.craftbukkit.tag.CraftEntityTag;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.components.EquippableComponent;

@SerializableAs("Equippable")
public final class CraftEquippableComponent implements EquippableComponent {

    private Equippable handle;

    public CraftEquippableComponent(Equippable handle) {
        this.handle = handle;
    }

    public CraftEquippableComponent(CraftEquippableComponent craft) {
        this.handle = craft.handle;
    }

    public CraftEquippableComponent(Map<String, Object> map) {
        EnumItemSlot slot = CraftEquipmentSlot.getNMS(EquipmentSlot.valueOf(SerializableMeta.getString(map, "slot", false)));

        Sound equipSound = null;
        String snd = SerializableMeta.getString(map, "equip-sound", true);
        if (snd != null) {
            equipSound = Registry.SOUNDS.get(NamespacedKey.fromString(snd));
        }

        String model = SerializableMeta.getString(map, "model", true);
        String cameraOverlay = SerializableMeta.getString(map, "camera-overlay", true);

        HolderSet<EntityTypes<?>> allowedEntities = null;
        Object allowed = SerializableMeta.getObject(Object.class, map, "allowed-entities", true);
        if (allowed != null) {
            allowedEntities = CraftHolderUtil.parse(allowed, Registries.ENTITY_TYPE, BuiltInRegistries.ENTITY_TYPE);
        }

        Boolean dispensable = SerializableMeta.getObject(Boolean.class, map, "dispensable", true);
        Boolean swappable = SerializableMeta.getObject(Boolean.class, map, "swappable", true);
        Boolean damageOnHurt = SerializableMeta.getObject(Boolean.class, map, "damage-on-hurt", true);

        this.handle = new Equippable(slot,
                (equipSound != null) ? CraftSound.bukkitToMinecraftHolder(equipSound) : SoundEffects.ARMOR_EQUIP_GENERIC,
                Optional.ofNullable(model).map(MinecraftKey::parse),
                Optional.ofNullable(cameraOverlay).map(MinecraftKey::parse),
                Optional.ofNullable(allowedEntities),
                (dispensable != null) ? dispensable : true,
                (swappable != null) ? swappable : true,
                (damageOnHurt != null) ? damageOnHurt : true
        );
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("slot", getSlot().name());
        result.put("equip-sound", getEquipSound().getKey().toString());

        NamespacedKey model = getModel();
        if (model != null) {
            result.put("model", model.toString());
        }

        NamespacedKey cameraOverlay = getCameraOverlay();
        if (cameraOverlay != null) {
            result.put("camera-overlay", cameraOverlay.toString());
        }

        Optional<HolderSet<EntityTypes<?>>> allowed = handle.allowedEntities();
        if (allowed.isPresent()) {
            CraftHolderUtil.serialize(result, "allowed-entities", allowed.get());
        }

        result.put("dispensable", isDispensable());
        result.put("swappable", isSwappable());
        result.put("damage-on-hurt", isDamageOnHurt());

        return result;
    }

    public Equippable getHandle() {
        return handle;
    }

    @Override
    public EquipmentSlot getSlot() {
        return CraftEquipmentSlot.getSlot(handle.slot());
    }

    @Override
    public void setSlot(EquipmentSlot slot) {
        handle = new Equippable(CraftEquipmentSlot.getNMS(slot), handle.equipSound(), handle.model(), handle.cameraOverlay(), handle.allowedEntities(), handle.dispensable(), handle.swappable(), handle.damageOnHurt());
    }

    @Override
    public Sound getEquipSound() {
        return CraftSound.minecraftToBukkit(handle.equipSound().value());
    }

    @Override
    public void setEquipSound(Sound sound) {
        handle = new Equippable(handle.slot(), (sound != null) ? CraftSound.bukkitToMinecraftHolder(sound) : SoundEffects.ARMOR_EQUIP_GENERIC, handle.model(), handle.cameraOverlay(), handle.allowedEntities(), handle.dispensable(), handle.swappable(), handle.damageOnHurt());
    }

    @Override
    public NamespacedKey getModel() {
        return handle.model().map(CraftNamespacedKey::fromMinecraft).orElse(null);
    }

    @Override
    public void setModel(NamespacedKey key) {
        handle = new Equippable(handle.slot(), handle.equipSound(), Optional.ofNullable(key).map(CraftNamespacedKey::toMinecraft), handle.cameraOverlay(), handle.allowedEntities(), handle.dispensable(), handle.swappable(), handle.damageOnHurt());
    }

    @Override
    public NamespacedKey getCameraOverlay() {
        return handle.cameraOverlay().map(CraftNamespacedKey::fromMinecraft).orElse(null);
    }

    @Override
    public void setCameraOverlay(NamespacedKey key) {
        handle = new Equippable(handle.slot(), handle.equipSound(), handle.model(), Optional.ofNullable(key).map(CraftNamespacedKey::toMinecraft), handle.allowedEntities(), handle.dispensable(), handle.swappable(), handle.damageOnHurt());
    }

    @Override
    public Collection<EntityType> getAllowedEntities() {
        return handle.allowedEntities().map(HolderSet::stream).map((stream) -> stream.map(Holder::value).map(CraftEntityType::minecraftToBukkit).collect(Collectors.toList())).orElse(null);
    }

    @Override
    public void setAllowedEntities(EntityType entities) {
        handle = new Equippable(handle.slot(), handle.equipSound(), handle.model(), handle.cameraOverlay(),
                (entities != null) ? Optional.of(HolderSet.direct(CraftEntityType.bukkitToMinecraftHolder(entities))) : Optional.empty(),
                handle.dispensable(), handle.swappable(), handle.damageOnHurt()
        );
    }

    @Override
    public void setAllowedEntities(Collection<EntityType> entities) {
        handle = new Equippable(handle.slot(), handle.equipSound(), handle.model(), handle.cameraOverlay(),
                (entities != null) ? Optional.of(HolderSet.direct(entities.stream().map(CraftEntityType::bukkitToMinecraftHolder).collect(Collectors.toList()))) : Optional.empty(),
                handle.dispensable(), handle.swappable(), handle.damageOnHurt()
        );
    }

    @Override
    public void setAllowedEntities(Tag<EntityType> tag) {
        Preconditions.checkArgument(tag instanceof CraftEntityTag, "tag must be an entity tag");

        handle = new Equippable(handle.slot(), handle.equipSound(), handle.model(), handle.cameraOverlay(),
                (tag != null) ? Optional.of(((CraftEntityTag) tag).getHandle()) : Optional.empty(),
                handle.dispensable(), handle.swappable(), handle.damageOnHurt()
        );
    }

    @Override
    public boolean isDispensable() {
        return handle.dispensable();
    }

    @Override
    public void setDispensable(boolean dispensable) {
        handle = new Equippable(handle.slot(), handle.equipSound(), handle.model(), handle.cameraOverlay(), handle.allowedEntities(), dispensable, handle.swappable(), handle.damageOnHurt());
    }

    @Override
    public boolean isSwappable() {
        return handle.swappable();
    }

    @Override
    public void setSwappable(boolean swappable) {
        handle = new Equippable(handle.slot(), handle.equipSound(), handle.model(), handle.cameraOverlay(), handle.allowedEntities(), handle.dispensable(), swappable, handle.damageOnHurt());
    }

    @Override
    public boolean isDamageOnHurt() {
        return handle.damageOnHurt();
    }

    @Override
    public void setDamageOnHurt(boolean damage) {
        handle = new Equippable(handle.slot(), handle.equipSound(), handle.model(), handle.cameraOverlay(), handle.allowedEntities(), handle.dispensable(), handle.swappable(), damage);
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
        final CraftEquippableComponent other = (CraftEquippableComponent) obj;
        return Objects.equals(this.handle, other.handle);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + Objects.hashCode(this.handle);
        return hash;
    }

    @Override
    public String toString() {
        return "CraftEquippableComponent{" + "handle=" + handle + '}';
    }
}
