package org.bukkit.craftbukkit.entity;

import java.util.stream.Collectors;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractBoat;
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
        return CraftBoat.getTreeSpecies(this.getHandle().getType());
    }

    @Override
    public void setWoodType(TreeSpecies species) {
        throw new UnsupportedOperationException("Not supported - you must spawn a new entity to change boat type.");
    }

    @Override
    public Type getBoatType() {
        return CraftBoat.boatTypeFromNms(this.getHandle().getType());
    }

    @Override
    public void setBoatType(Type type) {
        throw new UnsupportedOperationException("Not supported - you must spawn a new entity to change boat type.");
    }

    @Override
    public double getMaxSpeed() {
        return this.getHandle().maxSpeed;
    }

    @Override
    public void setMaxSpeed(double speed) {
        if (speed >= 0D) {
            this.getHandle().maxSpeed = speed;
        }
    }

    @Override
    public double getOccupiedDeceleration() {
        return this.getHandle().occupiedDeceleration;
    }

    @Override
    public void setOccupiedDeceleration(double speed) {
        if (speed >= 0D) {
            this.getHandle().occupiedDeceleration = speed;
        }
    }

    @Override
    public double getUnoccupiedDeceleration() {
        return this.getHandle().unoccupiedDeceleration;
    }

    @Override
    public void setUnoccupiedDeceleration(double speed) {
        this.getHandle().unoccupiedDeceleration = speed;
    }

    @Override
    public boolean getWorkOnLand() {
        return this.getHandle().landBoats;
    }

    @Override
    public void setWorkOnLand(boolean workOnLand) {
        this.getHandle().landBoats = workOnLand;
    }

    @Override
    public Status getStatus() {
        return CraftBoat.boatStatusFromNms(this.getHandle().status);
    }

    @Override
    public AbstractBoat getHandle() {
        return (AbstractBoat) this.entity;
    }

    @Override
    public String toString() {
        return "CraftBoat{boatType=" + this.getBoatType() + ",status=" + this.getStatus() + ",passengers=" + this.getPassengers().stream().map(Entity::toString).collect(Collectors.joining("-", "{", "}")) + "}";
    }

    public static Boat.Type boatTypeFromNms(EntityType<?> boatType) {
        if (boatType == EntityType.OAK_BOAT || boatType == EntityType.OAK_CHEST_BOAT) {
            return Type.OAK;
        }

        if (boatType == EntityType.BIRCH_BOAT || boatType == EntityType.BIRCH_CHEST_BOAT) {
            return Type.BIRCH;
        }

        if (boatType == EntityType.ACACIA_BOAT || boatType == EntityType.ACACIA_CHEST_BOAT) {
            return Type.ACACIA;
        }

        if (boatType == EntityType.CHERRY_BOAT || boatType == EntityType.CHERRY_CHEST_BOAT) {
            return Type.CHERRY;
        }

        if (boatType == EntityType.JUNGLE_BOAT || boatType == EntityType.JUNGLE_CHEST_BOAT) {
            return Type.JUNGLE;
        }

        if (boatType == EntityType.SPRUCE_BOAT || boatType == EntityType.SPRUCE_CHEST_BOAT) {
            return Type.SPRUCE;
        }

        if (boatType == EntityType.DARK_OAK_BOAT || boatType == EntityType.DARK_OAK_CHEST_BOAT) {
            return Type.DARK_OAK;
        }

        if (boatType == EntityType.MANGROVE_BOAT || boatType == EntityType.MANGROVE_CHEST_BOAT) {
            return Type.MANGROVE;
        }

        if (boatType == EntityType.BAMBOO_RAFT || boatType == EntityType.BAMBOO_CHEST_RAFT) {
            return Type.BAMBOO;
        }

        throw new EnumConstantNotPresentException(Type.class, boatType.toString());
    }

    public static Status boatStatusFromNms(net.minecraft.world.entity.vehicle.Boat.EnumStatus enumStatus) {
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
    public static TreeSpecies getTreeSpecies(EntityType<?> boatType) {
        if (boatType == EntityType.SPRUCE_BOAT || boatType == EntityType.SPRUCE_CHEST_BOAT) {
            return TreeSpecies.REDWOOD;
        }

        if (boatType == EntityType.BIRCH_BOAT || boatType == EntityType.BIRCH_CHEST_BOAT) {
            return TreeSpecies.BIRCH;
        }

        if (boatType == EntityType.JUNGLE_BOAT || boatType == EntityType.JUNGLE_CHEST_BOAT) {
            return TreeSpecies.JUNGLE;
        }

        if (boatType == EntityType.ACACIA_BOAT || boatType == EntityType.ACACIA_CHEST_BOAT) {
            return TreeSpecies.ACACIA;
        }

        if (boatType == EntityType.DARK_OAK_BOAT || boatType == EntityType.DARK_OAK_CHEST_BOAT) {
            return TreeSpecies.DARK_OAK;
        }

        return TreeSpecies.GENERIC;
    }
}
