package io.papermc.paper.disguise;

import com.destroystokyo.paper.profile.CraftPlayerProfile;
import com.destroystokyo.paper.profile.PlayerProfile;
import io.papermc.paper.entity.meta.EntityTypeToEntityClass;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftEntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket.Action;
import static net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket.Entry;

public final class DisguiseUtil {
    private static final Logger LOG = LoggerFactory.getLogger(DisguiseUtil.class);

    private DisguiseUtil(){}

    public static boolean tryDisguise(ServerPlayer player, Entity entity, Packet<?> packet) {
            if(!(packet instanceof ClientboundAddEntityPacket clientboundAddEntityPacket)) {
                return false;
            }
            return switch (entity.getBukkitEntity().getDisguiseData()) {
                case DisguiseData.OriginalDisguise disguise -> false;
                case io.papermc.paper.disguise.EntityTypeDisguise(var type) -> {
                    player.connection.send(create(clientboundAddEntityPacket, CraftEntityType.bukkitToMinecraft(type)));
                    yield  true;
                }
                case PlayerDisguise(var playerProfile, var listed, var showHead, var skinParts) -> {
                    PlayerProfile adapted = Bukkit.createProfile(entity.getUUID(), playerProfile.getName());
                    adapted.setProperties(playerProfile.getProperties());
                    Entry playerUpdate = new Entry(
                            entity.getUUID(),
                            CraftPlayerProfile.asAuthlibCopy(adapted),
                            listed,
                            0,
                            net.minecraft.world.level.GameType.DEFAULT_MODE,
                            entity.getCustomName(),
                            showHead,
                            0,
                            null
                        );
                    player.connection.send(new ClientboundPlayerInfoUpdatePacket(EnumSet.of(Action.ADD_PLAYER, Action.UPDATE_LISTED), playerUpdate));
                    player.connection.send(create(clientboundAddEntityPacket, net.minecraft.world.entity.EntityType.PLAYER));
                    if(skinParts != null) {
                        player.connection.send(new net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket(
                            clientboundAddEntityPacket.getId(),
                            List.of(new SynchedEntityData.DataItem<>(Player.DATA_PLAYER_MODE_CUSTOMISATION, (byte) skinParts.getRaw()).value())
                        ));
                    }
                    yield  true;
                }
            };
    }

    /*
     * Only player disguise needs to be handled specially
     * because the client doesn't forget the player profile otherwise.
     * This would result in player being kicked cause the entities type mismatches the previously disguised one.
     */
    public static void tryDespawn(ServerPlayer player, Entity entity) {
        if(entity.getBukkitEntity().getDisguiseData() instanceof PlayerDisguise) {
            player.connection.send(new ClientboundPlayerInfoRemovePacket(List.of(entity.getUUID())));
        }
    }

    private static ClientboundAddEntityPacket create(ClientboundAddEntityPacket packet, EntityType<?> entityType) {
        return new net.minecraft.network.protocol.game.ClientboundAddEntityPacket(
            packet.getId(),
            packet.getUUID(),
            packet.getX(),
            packet.getY(),
            packet.getZ(),
            packet.getXRot(),
            packet.getYRot(),
            entityType,
            0,
            Vec3.ZERO.add(packet.getX(), packet.getY(), packet.getZ()).scale(1/8000.0D),
            packet.getYHeadRot()
        );
    }


    /*
     * Is used to skip entity meta that doesn't fit the disguised type.
     * e.g. Player having a float at index 15 (additional hearts) and the server side entity is an Armorstand
     * that has a byte at that index.
     */

    public static boolean shouldSkip(Entity entity, EntityDataAccessor<?> dataAccessor) {
        return shouldSkip(entity, dataAccessor.serializer(), dataAccessor.id());
    }

    public static boolean shouldSkip(Entity entity, EntityDataSerializer<?> entityDataSerializer, int id) {
        return switch (entity.getBukkitEntity().getDisguiseData()) {
            case DisguiseData.OriginalDisguise original -> false;
            case EntityTypeDisguise entityTypeDisguise -> !io.papermc.paper.entity.meta.EntityMetaWatcher.isValidForClass(
                EntityTypeToEntityClass.getClassByEntityType(entityTypeDisguise.entityType()),
                entityDataSerializer, id
            );
            case PlayerDisguise playerDisguise -> !io.papermc.paper.entity.meta.EntityMetaWatcher.isValidForClass(
                ServerPlayer.class,
                entityDataSerializer, id
            );
        };
    }

    public static List<SynchedEntityData.DataValue<?>> filter(Entity entity, List<SynchedEntityData.DataValue<?>> values) {
        List<SynchedEntityData.DataValue<?>> list = new ArrayList<>();
        for (SynchedEntityData.DataValue<?> value : values) {
            if (!shouldSkip(entity, value.serializer(), value.id())) {
                list.add(value);
            }
        }
        return list;
    }

    public static boolean shouldSkipAttributeSending(Entity entity) {
        return switch (entity.getBukkitEntity().getDisguiseData()) {
            case DisguiseData.OriginalDisguise original -> false;
            case EntityTypeDisguise entityTypeDisguise -> !entityTypeDisguise.entityType().hasDefaultAttributes();
            case PlayerDisguise playerDisguise -> false;
        };
    }

    public static boolean canSendAnimation(Entity entity) {
        return switch (entity.getBukkitEntity().getDisguiseData()) {
            case DisguiseData.OriginalDisguise original -> true;
            case EntityTypeDisguise entityTypeDisguise -> LivingEntity.class.isAssignableFrom(EntityTypeToEntityClass.getClassByEntityType(entityTypeDisguise.entityType()));
            case PlayerDisguise playerDisguise -> true;
        };
    }
}
