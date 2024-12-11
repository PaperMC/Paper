package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftInventoryLlama;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Llama.Color;
import org.bukkit.inventory.LlamaInventory;

public class CraftLlama extends CraftChestedHorse implements Llama {

    public CraftLlama(CraftServer server, net.minecraft.world.entity.animal.horse.Llama entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.animal.horse.Llama getHandle() {
        return (net.minecraft.world.entity.animal.horse.Llama) super.getHandle();
    }

    @Override
    public Color getColor() {
        return Color.values()[this.getHandle().getVariant().ordinal()];
    }

    @Override
    public void setColor(Color color) {
        Preconditions.checkArgument(color != null, "color");

        this.getHandle().setVariant(net.minecraft.world.entity.animal.horse.Llama.Variant.byId(color.ordinal()));
    }

    @Override
    public LlamaInventory getInventory() {
        return new CraftInventoryLlama(this.getHandle().inventory, this.getHandle().getBodyArmorAccess());
    }

    @Override
    public int getStrength() {
       return this.getHandle().getStrength();
    }

    @Override
    public void setStrength(int strength) {
        Preconditions.checkArgument(1 <= strength && strength <= 5, "strength must be [1,5]");
        if (strength == this.getStrength()) return;
        this.getHandle().setStrengthPublic(strength);
        this.getHandle().createInventory();
    }

    @Override
    public Horse.Variant getVariant() {
        return Horse.Variant.LLAMA;
    }

    @Override
    public String toString() {
        return "CraftLlama";
    }
}
