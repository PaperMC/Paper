package io.papermc.paper.entity.activation;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.entity.animal.AgeableWaterCreature;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.phys.AABB;

public enum ActivationType {
    WATER,
    FLYING_MONSTER,
    VILLAGER,
    MONSTER,
    ANIMAL,
    RAIDER,
    MISC;

    AABB boundingBox = new AABB(0, 0, 0, 0, 0, 0);

    /**
     * Returns the activation type for the given entity.
     *
     * @param entity entity to get the activation type for
     * @return activation type
     */
    public static ActivationType activationTypeFor(final Entity entity) {
        if (entity instanceof WaterAnimal || entity instanceof AgeableWaterCreature) {
            return ActivationType.WATER;
        } else if (entity instanceof Villager) {
            return ActivationType.VILLAGER;
        } else if (entity instanceof Ghast || entity instanceof Phantom) { // TODO: some kind of better distinction here?
            return ActivationType.FLYING_MONSTER;
        } else if (entity instanceof Raider) {
            return ActivationType.RAIDER;
        } else if (entity instanceof Enemy) {
            return ActivationType.MONSTER;
        } else if (entity instanceof PathfinderMob || entity instanceof AmbientCreature) {
            return ActivationType.ANIMAL;
        } else {
            return ActivationType.MISC;
        }
    }
}
