package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.world.item.Item;
import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftItemType;
import org.bukkit.entity.Piglin;
import org.bukkit.inventory.Inventory;

public class CraftPiglin extends CraftPiglinAbstract implements Piglin, com.destroystokyo.paper.entity.CraftRangedEntity<net.minecraft.world.entity.monster.piglin.Piglin> { // Paper

    public CraftPiglin(CraftServer server, net.minecraft.world.entity.monster.piglin.Piglin entity) {
        super(server, entity);
    }

    @Override
    public boolean isAbleToHunt() {
        return this.getHandle().cannotHunt;
    }

    @Override
    public void setIsAbleToHunt(boolean flag) {
        this.getHandle().cannotHunt = flag;
    }

    @Override
    public boolean addBarterMaterial(Material material) {
        Preconditions.checkArgument(material != null, "material cannot be null");

        Item item = CraftItemType.bukkitToMinecraft(material);
        return this.getHandle().allowedBarterItems.add(item);
    }

    @Override
    public boolean removeBarterMaterial(Material material) {
        Preconditions.checkArgument(material != null, "material cannot be null");

        Item item = CraftItemType.bukkitToMinecraft(material);
        return this.getHandle().allowedBarterItems.remove(item);
    }

    @Override
    public boolean addMaterialOfInterest(Material material) {
        Preconditions.checkArgument(material != null, "material cannot be null");

        Item item = CraftItemType.bukkitToMinecraft(material);
        return this.getHandle().interestItems.add(item);
    }

    @Override
    public boolean removeMaterialOfInterest(Material material) {
        Preconditions.checkArgument(material != null, "material cannot be null");

        Item item = CraftItemType.bukkitToMinecraft(material);
        return this.getHandle().interestItems.remove(item);
    }

    @Override
    public Set<Material> getInterestList() {
        return Collections.unmodifiableSet(this.getHandle().interestItems.stream().map(CraftItemType::minecraftToBukkit).collect(Collectors.toSet()));
    }

    @Override
    public Set<Material> getBarterList() {
        return Collections.unmodifiableSet(this.getHandle().allowedBarterItems.stream().map(CraftItemType::minecraftToBukkit).collect(Collectors.toSet()));
    }

    @Override
    public Inventory getInventory() {
        return new CraftInventory(this.getHandle().inventory);
    }

    @Override
    public net.minecraft.world.entity.monster.piglin.Piglin getHandle() {
        return (net.minecraft.world.entity.monster.piglin.Piglin) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftPiglin";
    }
}
