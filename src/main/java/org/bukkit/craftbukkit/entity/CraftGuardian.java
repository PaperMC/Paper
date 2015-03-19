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
        return ((EntityGuardian)entity).cn(); // PAIL: Rename isElder
    }

    @Override
    public void setElder( boolean shouldBeElder ) {
        EntityGuardian entityGuardian = (EntityGuardian) entity;

        if (!isElder() && shouldBeElder) {
            entityGuardian.a( true ); // PAIL: Rename setElder
        } else if (isElder() && !shouldBeElder) {
            entityGuardian.a( false ); // PAIL: Rename setElder

            // Since minecraft does not reset the elder Guardian to a guardian we have to do that
            entity.setSize(0.85F, 0.85F);

            // Since aW() calls its supers it will try to re register attributes which is invalid
            // check these on update
            entityGuardian.getAttributeInstance(GenericAttributes.e).setValue(6.0D); // PAIL: Rename ATTACK_DAMAGE
            entityGuardian.getAttributeInstance(GenericAttributes.d).setValue(0.5D); // PAIL: Rename MOVEMENT_SPEED
            entityGuardian.getAttributeInstance(GenericAttributes.b).setValue(16.0D); // PAIL: Rename FOLLOW_RANGE
            entityGuardian.getAttributeInstance(GenericAttributes.maxHealth).setValue(30.0D);

            // Update pathfinding (random stroll back to 80)
            entityGuardian.br.b(80); // PAIL: Rename goalRandomStroll.setTimeBetweenMovement

            // Tell minecraft that we need persistence since the guardian changed
            entityGuardian.initAttributes();
        }
    }
}
