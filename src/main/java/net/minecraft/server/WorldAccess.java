package net.minecraft.server;

public interface WorldAccess extends GeneratorAccess {

    WorldServer getMinecraftWorld();

    default void addAllEntities(Entity entity) {
        entity.co().forEach(this::addEntity);
    }
}
