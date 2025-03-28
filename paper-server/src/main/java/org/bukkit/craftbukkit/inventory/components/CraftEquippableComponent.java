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
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.equipment.EquipmentAssets;
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
        net.minecraft.world.entity.EquipmentSlot slot = CraftEquipmentSlot.getNMS(EquipmentSlot.valueOf(SerializableMeta.getString(map, "slot", false)));

        Sound equipSound = null;
        String snd = SerializableMeta.getString(map, "equip-sound", true);
        if (snd != null) {
            equipSound = Registry.SOUNDS.get(NamespacedKey.fromString(snd));
        }

        String model = SerializableMeta.getString(map, "model", true);
        String cameraOverlay = SerializableMeta.getString(map, "camera-overlay", true);

        HolderSet<net.minecraft.world.entity.EntityType<?>> allowedEntities = null;
        Object allowed = SerializableMeta.getObject(Object.class, map, "allowed-entities", true);
        if (allowed != null) {
            allowedEntities = CraftHolderUtil.parse(allowed, Registries.ENTITY_TYPE, BuiltInRegistries.ENTITY_TYPE);
        }

        Boolean dispensable = SerializableMeta.getObject(Boolean.class, map, "dispensable", true);
        Boolean swappable = SerializableMeta.getObject(Boolean.class, map, "swappable", true);
        Boolean damageOnHurt = SerializableMeta.getObject(Boolean.class, map, "damage-on-hurt", true);

        this.handle = new Equippable(slot,
                (equipSound != null) ? CraftSound.bukkitToMinecraftHolder(equipSound) : SoundEvents.ARMOR_EQUIP_GENERIC,
                Optional.ofNullable(model).map(ResourceLocation::parse).map((k) -> ResourceKey.create(EquipmentAssets.ROOT_ID, k)),
                Optional.ofNullable(cameraOverlay).map(ResourceLocation::parse),
                Optional.ofNullable(allowedEntities),
                (dispensable != null) ? dispensable : true,
                (swappable != null) ? swappable : true,
                (damageOnHurt != null) ? damageOnHurt : true
        );
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("slot", this.getSlot().name());

        NamespacedKey sound = Registry.SOUNDS.getKey(this.getEquipSound());
        if (sound != null) {
            result.put("equip-sound", sound.toString());
        }

        NamespacedKey model = this.getModel();
        if (model != null) {
            result.put("model", model.toString());
        }

        NamespacedKey cameraOverlay = this.getCameraOverlay();
        if (cameraOverlay != null) {
            result.put("camera-overlay", cameraOverlay.toString());
        }

        Optional<HolderSet<net.minecraft.world.entity.EntityType<?>>> allowed = this.handle.allowedEntities();
        if (allowed.isPresent()) {
            CraftHolderUtil.serialize(result, "allowed-entities", allowed.get());
        }

        result.put("dispensable", this.isDispensable());
        result.put("swappable", this.isSwappable());
        result.put("damage-on-hurt", this.isDamageOnHurt());

        return result;
    }

    public Equippable getHandle() {
        return this.handle;
    }

    @Override
    public EquipmentSlot getSlot() {
        return CraftEquipmentSlot.getSlot(this.handle.slot());
    }

    @Override
    public void setSlot(EquipmentSlot slot) {
        this.handle = new Equippable(CraftEquipmentSlot.getNMS(slot), this.handle.equipSound(), this.handle.assetId(), this.handle.cameraOverlay(), this.handle.allowedEntities(), this.handle.dispensable(), this.handle.swappable(), this.handle.damageOnHurt());
    }

    @Override
    public Sound getEquipSound() {
        return CraftSound.minecraftToBukkit(this.handle.equipSound().value());
    }

    @Override
    public void setEquipSound(Sound sound) {
        this.handle = new Equippable(this.handle.slot(), (sound != null) ? CraftSound.bukkitToMinecraftHolder(sound) : SoundEvents.ARMOR_EQUIP_GENERIC, this.handle.assetId(), this.handle.cameraOverlay(), this.handle.allowedEntities(), this.handle.dispensable(), this.handle.swappable(), this.handle.damageOnHurt());
    }

    @Override
    public NamespacedKey getModel() {
        return this.handle.assetId().map((a) -> CraftNamespacedKey.fromMinecraft(a.location())).orElse(null);
    }

    @Override
    public void setModel(NamespacedKey key) {
        this.handle = new Equippable(this.handle.slot(), this.handle.equipSound(), Optional.ofNullable(key).map(CraftNamespacedKey::toMinecraft).map((k) -> ResourceKey.create(EquipmentAssets.ROOT_ID, k)), this.handle.cameraOverlay(), this.handle.allowedEntities(), this.handle.dispensable(), this.handle.swappable(), this.handle.damageOnHurt());
    }

    @Override
    public NamespacedKey getCameraOverlay() {
        return this.handle.cameraOverlay().map(CraftNamespacedKey::fromMinecraft).orElse(null);
    }

    @Override
    public void setCameraOverlay(NamespacedKey key) {
        this.handle = new Equippable(this.handle.slot(), this.handle.equipSound(), this.handle.assetId(), Optional.ofNullable(key).map(CraftNamespacedKey::toMinecraft), this.handle.allowedEntities(), this.handle.dispensable(), this.handle.swappable(), this.handle.damageOnHurt());
    }

    @Override
    public Collection<EntityType> getAllowedEntities() {
        return this.handle.allowedEntities().map(HolderSet::stream).map((stream) -> stream.map(Holder::value).map(CraftEntityType::minecraftToBukkit).collect(Collectors.toList())).orElse(null);
    }

    @Override
    public void setAllowedEntities(EntityType entities) {
        this.handle = new Equippable(this.handle.slot(), this.handle.equipSound(), this.handle.assetId(), this.handle.cameraOverlay(),
                (entities != null) ? Optional.of(HolderSet.direct(CraftEntityType.bukkitToMinecraftHolder(entities))) : Optional.empty(),
                this.handle.dispensable(), this.handle.swappable(), this.handle.damageOnHurt()
        );
    }

    @Override
    public void setAllowedEntities(Collection<EntityType> entities) {
        this.handle = new Equippable(this.handle.slot(), this.handle.equipSound(), this.handle.assetId(), this.handle.cameraOverlay(),
                (entities != null) ? Optional.of(HolderSet.direct(entities.stream().map(CraftEntityType::bukkitToMinecraftHolder).collect(Collectors.toList()))) : Optional.empty(),
                this.handle.dispensable(), this.handle.swappable(), this.handle.damageOnHurt()
        );
    }

    @Override
    public void setAllowedEntities(Tag<EntityType> tag) {
        Preconditions.checkArgument(tag == null || tag instanceof CraftEntityTag, "tag must be an entity tag"); // Paper

        this.handle = new Equippable(this.handle.slot(), this.handle.equipSound(), this.handle.assetId(), this.handle.cameraOverlay(),
                (tag != null) ? Optional.of(((CraftEntityTag) tag).getHandle()) : Optional.empty(),
                this.handle.dispensable(), this.handle.swappable(), this.handle.damageOnHurt()
        );
    }

    @Override
    public boolean isDispensable() {
        return this.handle.dispensable();
    }

    @Override
    public void setDispensable(boolean dispensable) {
        this.handle = new Equippable(this.handle.slot(), this.handle.equipSound(), this.handle.assetId(), this.handle.cameraOverlay(), this.handle.allowedEntities(), dispensable, this.handle.swappable(), this.handle.damageOnHurt());
    }

    @Override
    public boolean isSwappable() {
        return this.handle.swappable();
    }

    @Override
    public void setSwappable(boolean swappable) {
        this.handle = new Equippable(this.handle.slot(), this.handle.equipSound(), this.handle.assetId(), this.handle.cameraOverlay(), this.handle.allowedEntities(), this.handle.dispensable(), swappable, this.handle.damageOnHurt());
    }

    @Override
    public boolean isDamageOnHurt() {
        return this.handle.damageOnHurt();
    }

    @Override
    public void setDamageOnHurt(boolean damage) {
        this.handle = new Equippable(this.handle.slot(), this.handle.equipSound(), this.handle.assetId(), this.handle.cameraOverlay(), this.handle.allowedEntities(), this.handle.dispensable(), this.handle.swappable(), damage);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
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
        return "CraftEquippableComponent{" + "handle=" + this.handle + '}';
    }
}
