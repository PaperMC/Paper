package io.papermc.paper.datacomponent.item;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.set.PaperRegistrySets;
import io.papermc.paper.registry.set.RegistryKeySet;
import java.util.Optional;
import net.kyori.adventure.key.Key;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import org.bukkit.craftbukkit.CraftEquipmentSlot;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.checkerframework.checker.nullness.qual.Nullable;

public record PaperEquippable(
    net.minecraft.world.item.equipment.Equippable impl
) implements Equippable, Handleable<net.minecraft.world.item.equipment.Equippable> {

    @Override
    public net.minecraft.world.item.equipment.Equippable getHandle() {
        return this.impl;
    }

    @Override
    public EquipmentSlot slot() {
        return CraftEquipmentSlot.getSlot(this.impl.slot());
    }

    @Override
    public Key equipSound() {
        return PaperAdventure.asAdventure(this.impl.equipSound().value().location());
    }

    @Override
    public @Nullable Key assetId() {
        return this.impl.assetId()
            .map(PaperAdventure::asAdventureKey)
            .orElse(null);
    }

    @Override
    public @Nullable Key cameraOverlay() {
        return this.impl.cameraOverlay()
            .map(PaperAdventure::asAdventure)
            .orElse(null);
    }

    @Override
    public @Nullable RegistryKeySet<EntityType> allowedEntities() {
        return this.impl.allowedEntities()
            .map((set) -> PaperRegistrySets.convertToApi(RegistryKey.ENTITY_TYPE, set))
            .orElse(null);
    }

    @Override
    public boolean dispensable() {
        return this.impl.dispensable();
    }

    @Override
    public boolean swappable() {
        return this.impl.swappable();
    }

    @Override
    public boolean damageOnHurt() {
        return this.impl.damageOnHurt();
    }

    @Override
    public boolean equipOnInteract() {
        return this.impl.equipOnInteract();
    }

    @Override
    public Builder toBuilder() {
        return new BuilderImpl(this.slot())
            .equipSound(this.equipSound())
            .assetId(this.assetId())
            .cameraOverlay(this.cameraOverlay())
            .allowedEntities(this.allowedEntities())
            .dispensable(this.dispensable())
            .swappable(this.swappable())
            .damageOnHurt(this.damageOnHurt())
            .equipOnInteract(this.equipOnInteract());
    }


    static final class BuilderImpl implements Builder {

        private final net.minecraft.world.entity.EquipmentSlot equipmentSlot;
        private Holder<SoundEvent> equipSound = SoundEvents.ARMOR_EQUIP_GENERIC;
        private Optional<ResourceKey<EquipmentAsset>> assetId = Optional.empty();
        private Optional<ResourceLocation> cameraOverlay = Optional.empty();
        private Optional<HolderSet<net.minecraft.world.entity.EntityType<?>>> allowedEntities = Optional.empty();
        private boolean dispensable = true;
        private boolean swappable = true;
        private boolean damageOnHurt = true;
        private boolean equipOnInteract;

        BuilderImpl(final EquipmentSlot equipmentSlot) {
            this.equipmentSlot = CraftEquipmentSlot.getNMS(equipmentSlot);
        }

        @Override
        public Builder equipSound(final Key sound) {
            this.equipSound = PaperAdventure.resolveSound(sound);
            return this;
        }

        @Override
        public Builder assetId(final @Nullable Key assetId) {
            this.assetId = Optional.ofNullable(assetId)
                .map(key -> PaperAdventure.asVanilla(EquipmentAssets.ROOT_ID, key));

            return this;
        }

        @Override
        public Builder cameraOverlay(@Nullable final Key cameraOverlay) {
            this.cameraOverlay = Optional.ofNullable(cameraOverlay)
                .map(PaperAdventure::asVanilla);

            return this;
        }

        @Override
        public Builder allowedEntities(final @Nullable RegistryKeySet<EntityType> allowedEntities) {
            this.allowedEntities = Optional.ofNullable(allowedEntities)
                .map((set) -> PaperRegistrySets.convertToNms(Registries.ENTITY_TYPE, BuiltInRegistries.BUILT_IN_CONVERSIONS.lookup(), set));
            return this;
        }

        @Override
        public Builder dispensable(final boolean dispensable) {
            this.dispensable = dispensable;
            return this;
        }

        @Override
        public Builder swappable(final boolean swappable) {
            this.swappable = swappable;
            return this;
        }

        @Override
        public Builder damageOnHurt(final boolean damageOnHurt) {
            this.damageOnHurt = damageOnHurt;
            return this;
        }

        @Override
        public Builder equipOnInteract(final boolean equipOnInteract) {
            this.equipOnInteract = equipOnInteract;
            return this;
        }

        @Override
        public Equippable build() {
            return new PaperEquippable(
                new net.minecraft.world.item.equipment.Equippable(
                    this.equipmentSlot,
                    this.equipSound,
                    this.assetId,
                    this.cameraOverlay,
                    this.allowedEntities,
                    this.dispensable,
                    this.swappable,
                    this.damageOnHurt,
                    this.equipOnInteract,
                    false, // TODO
                    Holder.direct(SoundEvents.GOAT_SCREAMING_DEATH) // TODO
                )
            );
        }
    }
}
