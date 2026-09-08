package org.bukkit.craftbukkit.event.entity;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import org.bukkit.craftbukkit.entity.CraftVillager;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.VillagerCareerChangeEvent;

public class CraftVillagerCareerChangeEvent extends CraftEntityEvent implements VillagerCareerChangeEvent {

    private Profession profession;
    private final ChangeReason reason;

    private boolean cancelled;

    public CraftVillagerCareerChangeEvent(final Villager villager, final Profession profession, final ChangeReason reason) {
        super(villager);
        this.profession = profession;
        this.reason = reason;
    }

    public CraftVillagerCareerChangeEvent(final net.minecraft.world.entity.npc.villager.Villager villager, final Holder<VillagerProfession> profession, final ChangeReason reason) {
        this((Villager) villager.getBukkitEntity(), CraftVillager.CraftProfession.minecraftHolderToBukkit(profession), reason);
    }

    public CraftVillagerCareerChangeEvent(final net.minecraft.world.entity.npc.villager.Villager villager, final ResourceKey<VillagerProfession> profession, final ChangeReason reason) {
        this(villager, BuiltInRegistries.VILLAGER_PROFESSION.getOrThrow(profession), reason);
    }

    @Override
    public Villager getEntity() {
        return (Villager) this.entity;
    }

    @Override
    public Profession getProfession() {
        return this.profession;
    }

    @Override
    public void setProfession(final Profession profession) {
        this.profession = profession;
    }

    @Override
    public ChangeReason getReason() {
        return this.reason;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return VillagerCareerChangeEvent.getHandlerList();
    }
}
