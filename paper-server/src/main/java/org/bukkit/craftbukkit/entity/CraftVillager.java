package org.bukkit.craftbukkit.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.server.EntityVillager;
import org.apache.commons.lang.Validate;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftMerchant;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.MerchantRecipe;

public class CraftVillager extends CraftAgeable implements Villager, InventoryHolder {

    private static final Map<Career, Integer> careerIDMap = new HashMap<>();
    private CraftMerchant merchant;

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
        return Profession.values()[getHandle().getProfession() + 1]; // Offset by 1 from the zombie types
    }

    public void setProfession(Profession profession) {
        Validate.notNull(profession);
        Validate.isTrue(!profession.isZombie(), "Profession is reserved for Zombies: ", profession);
        getHandle().setProfession(profession.ordinal() - 1);
    }

    @Override
    public Career getCareer() {
        return getCareer(getProfession(), getHandle().bK);
    }

    @Override
    public void setCareer(Career career) {
        setCareer(career, true);
    }

    @Override
    public void setCareer(Career career, boolean resetTrades) {
        if (career == null) {
            getHandle().bK = 0; // reset career
        } else {
            Validate.isTrue(career.getProfession() == getProfession(), "Career assignment mismatch. Found (" + getProfession() + ") Required (" + career.getProfession() + ")");
            getHandle().bK = getCareerID(career);
        }

        if (resetTrades) {
            getHandle().trades = null;
            getHandle().dx();
        }
    }

    @Override
    public Inventory getInventory() {
        return new CraftInventory(getHandle().inventory);
    }

    private CraftMerchant getMerchant() {
        return (merchant == null) ? merchant = new CraftMerchant(getHandle()) : merchant;
    }

    @Override
    public List<MerchantRecipe> getRecipes() {
        return getMerchant().getRecipes();
    }

    @Override
    public void setRecipes(List<MerchantRecipe> recipes) {
        this.getMerchant().setRecipes(recipes);
    }

    @Override
    public MerchantRecipe getRecipe(int i) {
        return getMerchant().getRecipe(i);
    }

    @Override
    public void setRecipe(int i, MerchantRecipe merchantRecipe) {
        getMerchant().setRecipe(i, merchantRecipe);
    }

    @Override
    public int getRecipeCount() {
        return getMerchant().getRecipeCount();
    }

    @Override
    public boolean isTrading() {
        return getTrader() != null;
    }

    @Override
    public HumanEntity getTrader() {
        return getMerchant().getTrader();
    }

    @Override
    public int getRiches() {
        return getHandle().riches;
    }

    @Override
    public void setRiches(int riches) {
        getHandle().riches = riches;
    }

    @Nullable
    private static Career getCareer(Profession profession, int id) {
        Validate.isTrue(id > 0, "Career id must be greater than 0");

        List<Career> careers = profession.getCareers();
        for (Career c : careers) {
            if (careerIDMap.containsKey(c) && careerIDMap.get(c) == id) {
                return c;
            }
        }

        return null;
    }

    private static int getCareerID(Career career) {
        return careerIDMap.getOrDefault(career, 0);
    }

    static {
        // build Career -> ID map
        int id = 0;
        for (Profession prof : Profession.values()) {
            List<Career> careers = prof.getCareers();
            if (!careers.isEmpty()) {
                for (Career c : careers) {
                    careerIDMap.put(c, ++id);
                }
            }

            Validate.isTrue(id == careers.size(), "Career id registration mismatch");
            id = 0;
        }
    }
}
