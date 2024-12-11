package org.bukkit.craftbukkit.inventory.view;

import net.minecraft.world.inventory.BeaconMenu;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.craftbukkit.potion.CraftPotionEffectType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.BeaconInventory;
import org.bukkit.inventory.view.BeaconView;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.Nullable;

public class CraftBeaconView extends CraftInventoryView<BeaconMenu, BeaconInventory> implements BeaconView {

    public CraftBeaconView(final HumanEntity player, final BeaconInventory viewing, final BeaconMenu container) {
        super(player, viewing, container);
    }

    @Override
    public int getTier() {
        return this.container.getLevels();
    }

    @Nullable
    @Override
    public PotionEffectType getPrimaryEffect() {
        return this.container.getPrimaryEffect() != null ? CraftPotionEffectType.minecraftHolderToBukkit(this.container.getPrimaryEffect()) : null;
    }

    @Nullable
    @Override
    public PotionEffectType getSecondaryEffect() {
        return this.container.getSecondaryEffect() != null ? CraftPotionEffectType.minecraftHolderToBukkit(this.container.getSecondaryEffect()) : null;
    }

    @Override
    public void setPrimaryEffect(@Nullable final PotionEffectType effectType) {
        this.container.setData(BeaconBlockEntity.DATA_PRIMARY, BeaconMenu.encodeEffect((effectType == null) ? null : CraftPotionEffectType.bukkitToMinecraftHolder(effectType)));
    }

    @Override
    public void setSecondaryEffect(@Nullable final PotionEffectType effectType) {
        this.container.setData(BeaconBlockEntity.DATA_SECONDARY, BeaconMenu.encodeEffect((effectType == null) ? null : CraftPotionEffectType.bukkitToMinecraftHolder(effectType)));
    }
}
