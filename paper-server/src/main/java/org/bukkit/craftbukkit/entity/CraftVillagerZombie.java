package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.world.effect.MobEffects;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Villager;
import org.bukkit.entity.ZombieVillager;

public class CraftVillagerZombie extends CraftZombie implements ZombieVillager {

    public CraftVillagerZombie(CraftServer server, net.minecraft.world.entity.monster.ZombieVillager entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.monster.ZombieVillager getHandle() {
        return (net.minecraft.world.entity.monster.ZombieVillager) this.entity;
    }

    @Override
    public Villager.Profession getVillagerProfession() {
        return CraftVillager.CraftProfession.minecraftHolderToBukkit(this.getHandle().getVillagerData().profession());
    }

    @Override
    public void setVillagerProfession(Villager.Profession profession) {
        Preconditions.checkArgument(profession != null, "Villager.Profession cannot be null");
        this.getHandle().setVillagerData(this.getHandle().getVillagerData().withProfession(CraftVillager.CraftProfession.bukkitToMinecraftHolder(profession)));
    }

    @Override
    public Villager.Type getVillagerType() {
        return CraftVillager.CraftType.minecraftHolderToBukkit(this.getHandle().getVillagerData().type());
    }

    @Override
    public void setVillagerType(Villager.Type type) {
        Preconditions.checkArgument(type != null, "Villager.Type cannot be null");
        this.getHandle().setVillagerData(this.getHandle().getVillagerData().withType(CraftVillager.CraftType.bukkitToMinecraftHolder(type)));
    }

    @Override
    public boolean isConverting() {
        return this.getHandle().isConverting();
    }

    @Override
    public int getConversionTime() {
        Preconditions.checkState(this.isConverting(), "Entity not converting");

        return this.getHandle().villagerConversionTime;
    }

    @Override
    public void setConversionTime(int time) {
    // Paper start - missing entity behaviour api - converting without entity event
        this.setConversionTime(time, true);
    }

    @Override
    public void setConversionTime(int time, boolean broadcastEntityEvent) {
    // Paper end - missing entity behaviour api - converting without entity event
        if (time < 0) {
            this.getHandle().villagerConversionTime = -1;
            this.getHandle().getEntityData().set(net.minecraft.world.entity.monster.ZombieVillager.DATA_CONVERTING_ID, false);
            this.getHandle().conversionStarter = null;
            this.getHandle().removeEffect(MobEffects.STRENGTH, org.bukkit.event.entity.EntityPotionEffectEvent.Cause.CONVERSION);
        } else {
            this.getHandle().startConverting(null, time, broadcastEntityEvent); // Paper - missing entity behaviour api - converting without entity event
        }
    }

    @Override
    public OfflinePlayer getConversionPlayer() {
        return (this.getHandle().conversionStarter == null) ? null : Bukkit.getOfflinePlayer(this.getHandle().conversionStarter);
    }

    @Override
    public void setConversionPlayer(OfflinePlayer conversionPlayer) {
        if (!this.isConverting()) return;
        this.getHandle().conversionStarter = (conversionPlayer == null) ? null : conversionPlayer.getUniqueId();
    }
}
