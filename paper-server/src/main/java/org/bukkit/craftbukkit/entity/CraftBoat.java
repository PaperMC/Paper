package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import java.util.stream.Collectors;
import net.minecraft.world.entity.vehicle.EntityBoat;
import org.bukkit.TreeSpecies;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class CraftBoat extends CraftVehicle implements Boat {

    public CraftBoat(CraftServer server, EntityBoat entity) {
        super(server, entity);
    }

    @Override
    public TreeSpecies getWoodType() {
        return getTreeSpecies(getHandle().getVariant());
    }

    @Override
    public void setWoodType(TreeSpecies species) {
        getHandle().setVariant(getBoatType(species));
    }

    @Override
    public Type getBoatType() {
        return boatTypeFromNms(getHandle().getVariant());
    }

    @Override
    public void setBoatType(Type type) {
        Preconditions.checkArgument(type != null, "Boat.Type cannot be null");

        getHandle().setVariant(boatTypeToNms(type));
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
    public EntityBoat getHandle() {
        return (EntityBoat) entity;
    }

    @Override
    public String toString() {
        return "CraftBoat{boatType=" + getBoatType() + ",status=" + getStatus() + ",passengers=" + getPassengers().stream().map(Entity::toString).collect(Collectors.joining("-", "{", "}")) + "}";
    }

    @Override
    public EntityType getType() {
        return EntityType.BOAT;
    }

    public static Boat.Type boatTypeFromNms(EntityBoat.EnumBoatType boatType) {
        return switch (boatType) {
            default -> throw new EnumConstantNotPresentException(Type.class, boatType.name());
            case OAK -> Type.OAK;
            case BIRCH -> Type.BIRCH;
            case ACACIA -> Type.ACACIA;
            case CHERRY -> Type.CHERRY;
            case JUNGLE -> Type.JUNGLE;
            case SPRUCE -> Type.SPRUCE;
            case DARK_OAK -> Type.DARK_OAK;
            case MANGROVE -> Type.MANGROVE;
            case BAMBOO -> Type.BAMBOO;
        };
    }

    public static EntityBoat.EnumBoatType boatTypeToNms(Boat.Type type) {
        return switch (type) {
            default -> throw new EnumConstantNotPresentException(EntityBoat.EnumBoatType.class, type.name());
            case BAMBOO -> EntityBoat.EnumBoatType.BAMBOO;
            case MANGROVE -> EntityBoat.EnumBoatType.MANGROVE;
            case SPRUCE -> EntityBoat.EnumBoatType.SPRUCE;
            case DARK_OAK -> EntityBoat.EnumBoatType.DARK_OAK;
            case JUNGLE -> EntityBoat.EnumBoatType.JUNGLE;
            case CHERRY -> EntityBoat.EnumBoatType.CHERRY;
            case ACACIA -> EntityBoat.EnumBoatType.ACACIA;
            case BIRCH -> EntityBoat.EnumBoatType.BIRCH;
            case OAK -> EntityBoat.EnumBoatType.OAK;
        };
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
    public static TreeSpecies getTreeSpecies(EntityBoat.EnumBoatType boatType) {
        switch (boatType) {
            case SPRUCE:
                return TreeSpecies.REDWOOD;
            case BIRCH:
                return TreeSpecies.BIRCH;
            case JUNGLE:
                return TreeSpecies.JUNGLE;
            case ACACIA:
                return TreeSpecies.ACACIA;
            case DARK_OAK:
                return TreeSpecies.DARK_OAK;
            case OAK:
            default:
                return TreeSpecies.GENERIC;
        }
    }

    @Deprecated
    public static EntityBoat.EnumBoatType getBoatType(TreeSpecies species) {
        switch (species) {
            case REDWOOD:
                return EntityBoat.EnumBoatType.SPRUCE;
            case BIRCH:
                return EntityBoat.EnumBoatType.BIRCH;
            case JUNGLE:
                return EntityBoat.EnumBoatType.JUNGLE;
            case ACACIA:
                return EntityBoat.EnumBoatType.ACACIA;
            case DARK_OAK:
                return EntityBoat.EnumBoatType.DARK_OAK;
            case GENERIC:
            default:
                return EntityBoat.EnumBoatType.OAK;
        }
    }
}
