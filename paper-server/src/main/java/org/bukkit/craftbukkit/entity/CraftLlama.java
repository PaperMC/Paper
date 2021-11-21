package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.world.entity.animal.horse.EntityLlama;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftInventoryLlama;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Llama.Color;
import org.bukkit.inventory.LlamaInventory;

public class CraftLlama extends CraftChestedHorse implements Llama {

    public CraftLlama(CraftServer server, EntityLlama entity) {
        super(server, entity);
    }

    @Override
    public EntityLlama getHandle() {
        return (EntityLlama) super.getHandle();
    }

    @Override
    public Color getColor() {
        return Color.values()[getHandle().getVariant()];
    }

    @Override
    public void setColor(Color color) {
        Preconditions.checkArgument(color != null, "color");

        getHandle().setVariant(color.ordinal());
    }

    @Override
    public LlamaInventory getInventory() {
        return new CraftInventoryLlama(getHandle().inventory);
    }

    @Override
    public int getStrength() {
       return getHandle().getStrength();
    }

    @Override
    public void setStrength(int strength) {
        Preconditions.checkArgument(1 <= strength && strength <= 5, "strength must be [1,5]");
        if (strength == getStrength()) return;
        getHandle().setStrength(strength);
        getHandle().createInventory();
    }

    @Override
    public Horse.Variant getVariant() {
        return Horse.Variant.LLAMA;
    }

    @Override
    public String toString() {
        return "CraftLlama";
    }

    @Override
    public EntityType getType() {
        return EntityType.LLAMA;
    }
}
