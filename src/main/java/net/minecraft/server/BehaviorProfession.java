package net.minecraft.server;

import com.google.common.collect.ImmutableMap;

public class BehaviorProfession extends Behavior<EntityVillager> {

    public BehaviorProfession() {
        super(ImmutableMap.of(MemoryModuleType.JOB_SITE, MemoryStatus.VALUE_ABSENT));
    }

    protected boolean a(WorldServer worldserver, EntityVillager entityvillager) {
        VillagerData villagerdata = entityvillager.getVillagerData();

        return villagerdata.getProfession() != VillagerProfession.NONE && villagerdata.getProfession() != VillagerProfession.NITWIT && entityvillager.getExperience() == 0 && villagerdata.getLevel() <= 1;
    }

    protected void a(WorldServer worldserver, EntityVillager entityvillager, long i) {
        entityvillager.setVillagerData(entityvillager.getVillagerData().withProfession(VillagerProfession.NONE));
        entityvillager.c(worldserver);
    }
}
