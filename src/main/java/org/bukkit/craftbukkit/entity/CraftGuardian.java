package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityGuardian;
import net.minecraft.server.GenericAttributes;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Guardian;

public class CraftGuardian extends CraftMonster implements Guardian {

    public CraftGuardian(CraftServer server, EntityGuardian entity) {
        super(server, entity);
    }

    @Override
    public String toString() {
        return "CraftGuardian";
    }

    @Override
    public EntityType getType() {
        return EntityType.GUARDIAN;
    }

    @Override
    public boolean isElder() {
        return ((EntityGuardian)entity).isElder();
    }

    @Override
    public void setElder( boolean shouldBeElder ) {
        EntityGuardian entityGuardian = (EntityGuardian) entity;

        if (!isElder() && shouldBeElder) {
            entityGuardian.setElder(true);
        } else if (isElder() && !shouldBeElder) {
            entityGuardian.setElder(false);

            // Since minecraft does not reset the elder Guardian to a guardian we have to do that
            entity.setSize(0.85F, 0.85F);

            // Since aW() calls its supers it will try to re register attributes which is invalid
            // check these on update
            entityGuardian.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(6.0D);
            entityGuardian.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.5D);
            entityGuardian.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(16.0D);
            entityGuardian.getAttributeInstance(GenericAttributes.maxHealth).setValue(30.0D);

            // Update pathfinding (random stroll back to 80)
            entityGuardian.goalRandomStroll.setTimeBetweenMovement(80);

            // Tell minecraft that we need persistence since the guardian changed
            entityGuardian.initAttributes();
        }
    }
}
