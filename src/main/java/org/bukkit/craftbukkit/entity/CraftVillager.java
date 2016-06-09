package org.bukkit.craftbukkit.entity;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityVillager;
import net.minecraft.server.MerchantRecipeList;
import org.apache.commons.lang.Validate;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftMerchantRecipe;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.MerchantRecipe;

public class CraftVillager extends CraftAgeable implements Villager, InventoryHolder {

    public CraftVillager(CraftServer server, EntityVillager entity) {
        super(server, entity);
    }

    @Override
    public EntityVillager getHandle() {
        return (EntityVillager) entity;
    }

    @Override
    public String toString() {
        return "CraftVillager";
    }

    public EntityType getType() {
        return EntityType.VILLAGER;
    }

    public Profession getProfession() {
        return Profession.values()[getHandle().getProfession()];
    }

    public void setProfession(Profession profession) {
        Validate.notNull(profession);
        getHandle().setProfession(profession.ordinal());
    }

    @Override
    public Inventory getInventory() {
        return new CraftInventory(getHandle().inventory);
    }

    @Override
    public List<MerchantRecipe> getRecipes() {
        return Collections.unmodifiableList(Lists.transform(getHandle().getOffers(null), new Function<net.minecraft.server.MerchantRecipe, MerchantRecipe>() {
            @Override
            public MerchantRecipe apply(net.minecraft.server.MerchantRecipe recipe) {
                return recipe.asBukkit();
            }
        }));
    }

    @Override
    public void setRecipes(List<MerchantRecipe> list) {
        MerchantRecipeList recipes = getHandle().getOffers(null);
        recipes.clear();
        for (MerchantRecipe m : list) {
            recipes.add(CraftMerchantRecipe.fromBukkit(m).toMinecraft());
        }
    }

    @Override
    public MerchantRecipe getRecipe(int i) {
        return getHandle().getOffers(null).get(i).asBukkit();
    }

    @Override
    public void setRecipe(int i, MerchantRecipe merchantRecipe) {
        getHandle().getOffers(null).set(i, CraftMerchantRecipe.fromBukkit(merchantRecipe).toMinecraft());
    }

    @Override
    public int getRecipeCount() {
        return getHandle().getOffers(null).size();
    }

    @Override
    public boolean isTrading() {
        return getTrader() != null;
    }

    @Override
    public HumanEntity getTrader() {
        EntityHuman eh = getHandle().getTrader();
        return eh == null ? null : eh.getBukkitEntity();
    }

    @Override
    public int getRiches() {
        return getHandle().riches;
    }

    @Override
    public void setRiches(int riches) {
        getHandle().riches = riches;
    }
}
