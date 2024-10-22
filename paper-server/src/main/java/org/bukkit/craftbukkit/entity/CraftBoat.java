package org.bukkit.craftbukkit.entity;

import java.util.stream.Collectors;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.vehicle.AbstractBoat;
import net.minecraft.world.entity.vehicle.EntityBoat;
import org.bukkit.TreeSpecies;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;

public abstract class CraftBoat extends CraftVehicle implements Boat {

    public CraftBoat(CraftServer server, AbstractBoat entity) {
        super(server, entity);
    }

    @Override
    public TreeSpecies getWoodType() {
        return getTreeSpecies(getHandle().getType());
    }

    @Override
    public void setWoodType(TreeSpecies species) {
        throw new UnsupportedOperationException("Not supported - you must spawn a new entity to change boat type.");
    }

    @Override
    public Type getBoatType() {
        return boatTypeFromNms(getHandle().getType());
    }

    @Override
    public void setBoatType(Type type) {
        throw new UnsupportedOperationException("Not supported - you must spawn a new entity to change boat type.");
    }

    @Override
    public double getMaxSpeed() {
        return getHandle().maxSpeed;
    }

    @Override
    public void setMaxSpeed(double speed) {
        if (speed >= 0D) {
            getHandle().maxSpeed = speed;
        }
    }

    @Override
    public double getOccupiedDeceleration() {
        return getHandle().occupiedDeceleration;
    }

    @Override
    public void setOccupiedDeceleration(double speed) {
        if (speed >= 0D) {
            getHandle().occupiedDeceleration = speed;
        }
    }

    @Override
    public double getUnoccupiedDeceleration() {
        return getHandle().unoccupiedDeceleration;
    }

    @Override
    public void setUnoccupiedDeceleration(double speed) {
        getHandle().unoccupiedDeceleration = speed;
    }

    @Override
    public boolean getWorkOnLand() {
        return getHandle().landBoats;
    }

    @Override
    public void setWorkOnLand(boolean workOnLand) {
        getHandle().landBoats = workOnLand;
    }

    @Override
    public Status getStatus() {
        return boatStatusFromNms(getHandle().status);
    }

    @Override
    public AbstractBoat getHandle() {
        return (AbstractBoat) entity;
    }

    @Override
    public String toString() {
        return "CraftBoat{boatType=" + getBoatType() + ",status=" + getStatus() + ",passengers=" + getPassengers().stream().map(Entity::toString).collect(Collectors.joining("-", "{", "}")) + "}";
    }

    public static Boat.Type boatTypeFromNms(EntityTypes<?> boatType) {
        if (boatType == EntityTypes.OAK_BOAT || boatType == EntityTypes.OAK_CHEST_BOAT) {
            return Type.OAK;
        }

        if (boatType == EntityTypes.BIRCH_BOAT || boatType == EntityTypes.BIRCH_CHEST_BOAT) {
            return Type.BIRCH;
        }

        if (boatType == EntityTypes.ACACIA_BOAT || boatType == EntityTypes.ACACIA_CHEST_BOAT) {
            return Type.ACACIA;
        }

        if (boatType == EntityTypes.CHERRY_BOAT || boatType == EntityTypes.CHERRY_CHEST_BOAT) {
            return Type.CHERRY;
        }

        if (boatType == EntityTypes.JUNGLE_BOAT || boatType == EntityTypes.JUNGLE_CHEST_BOAT) {
            return Type.JUNGLE;
        }

        if (boatType == EntityTypes.SPRUCE_BOAT || boatType == EntityTypes.SPRUCE_CHEST_BOAT) {
            return Type.SPRUCE;
        }

        if (boatType == EntityTypes.DARK_OAK_BOAT || boatType == EntityTypes.DARK_OAK_CHEST_BOAT) {
            return Type.DARK_OAK;
        }

        if (boatType == EntityTypes.MANGROVE_BOAT || boatType == EntityTypes.MANGROVE_CHEST_BOAT) {
            return Type.MANGROVE;
        }

        if (boatType == EntityTypes.BAMBOO_RAFT || boatType == EntityTypes.BAMBOO_CHEST_RAFT) {
            return Type.BAMBOO;
        }

        throw new EnumConstantNotPresentException(Type.class, boatType.toString());
    }

    public static Status boatStatusFromNms(EntityBoat.EnumStatus enumStatus) {
        return switch (enumStatus) {
            default -> throw new EnumConstantNotPresentException(Status.class, enumStatus.name());
            case IN_AIR -> Status.IN_AIR;
            case ON_LAND -> Status.ON_LAND;
            case UNDER_WATER -> Status.UNDER_WATER;
            case UNDER_FLOWING_WATER -> Status.UNDER_FLOWING_WATER;
            case IN_WATER -> Status.IN_WATER;
        };
    }

    @Deprecated
    public static TreeSpecies getTreeSpecies(EntityTypes<?> boatType) {
        if (boatType == EntityTypes.SPRUCE_BOAT || boatType == EntityTypes.SPRUCE_CHEST_BOAT) {
            return TreeSpecies.REDWOOD;
        }

        if (boatType == EntityTypes.BIRCH_BOAT || boatType == EntityTypes.BIRCH_CHEST_BOAT) {
            return TreeSpecies.BIRCH;
        }

        if (boatType == EntityTypes.JUNGLE_BOAT || boatType == EntityTypes.JUNGLE_CHEST_BOAT) {
            return TreeSpecies.JUNGLE;
        }

        if (boatType == EntityTypes.ACACIA_BOAT || boatType == EntityTypes.ACACIA_CHEST_BOAT) {
            return TreeSpecies.ACACIA;
        }

        if (boatType == EntityTypes.DARK_OAK_BOAT || boatType == EntityTypes.DARK_OAK_CHEST_BOAT) {
            return TreeSpecies.DARK_OAK;
        }

        return TreeSpecies.GENERIC;
    }
}
