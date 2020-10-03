package net.minecraft.server;

import com.google.common.collect.ImmutableMap;
// CraftBukkit start
import org.bukkit.craftbukkit.entity.CraftVillager;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.VillagerCareerChangeEvent;
// CraftBukkit end

public class BehaviorProfession extends Behavior<EntityVillager> {

    public BehaviorProfession() {
        super(ImmutableMap.of(MemoryModuleType.JOB_SITE, MemoryStatus.VALUE_ABSENT));
    }

    protected boolean a(WorldServer worldserver, EntityVillager entityvillager) {
        VillagerData villagerdata = entityvillager.getVillagerData();

        return villagerdata.getProfession() != VillagerProfession.NONE && villagerdata.getProfession() != VillagerProfession.NITWIT && entityvillager.getExperience() == 0 && villagerdata.getLevel() <= 1;
    }

    protected void a(WorldServer worldserver, EntityVillager entityvillager, long i) {
        // CraftBukkit start
        VillagerCareerChangeEvent event = CraftEventFactory.callVillagerCareerChangeEvent(entityvillager, CraftVillager.nmsToBukkitProfession(VillagerProfession.NONE), VillagerCareerChangeEvent.ChangeReason.EMPLOYED);
        if (event.isCancelled()) {
            return;
        }

        entityvillager.setVillagerData(entityvillager.getVillagerData().withProfession(CraftVillager.bukkitToNmsProfession(event.getProfession())));
        // CraftBukkit end
        entityvillager.c(worldserver);
    }
}
