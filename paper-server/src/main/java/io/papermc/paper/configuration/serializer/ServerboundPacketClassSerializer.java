package io.papermc.paper.configuration.serializer;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.mojang.logging.LogUtils;
import io.leangen.geantyref.GenericTypeReflector;
import io.leangen.geantyref.TypeToken;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.PacketType;
import net.minecraft.network.protocol.common.CommonPacketTypes;
import net.minecraft.network.protocol.configuration.ConfigurationPacketTypes;
import net.minecraft.network.protocol.cookie.CookiePacketTypes;
import net.minecraft.network.protocol.game.GamePacketTypes;
import net.minecraft.network.protocol.handshake.HandshakePacketTypes;
import net.minecraft.network.protocol.login.LoginPacketTypes;
import net.minecraft.network.protocol.ping.PingPacketTypes;
import net.minecraft.network.protocol.status.StatusPacketTypes;
import net.minecraft.resources.ResourceLocation;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.configurate.serialize.ScalarSerializer;
import org.spongepowered.configurate.serialize.SerializationException;

@SuppressWarnings("Convert2Diamond")
public final class ServerboundPacketClassSerializer extends ScalarSerializer<Class<? extends Packet<?>>> {

    private static final Logger LOGGER = LogUtils.getClassLogger();
    private static final TypeToken<Class<? extends Packet<?>>> TYPE = new TypeToken<Class<? extends Packet<?>>>() {};
    static final Set<Class<?>> PACKET_CLASS_HOLDERS = Set.of(
        PingPacketTypes.class,
        HandshakePacketTypes.class,
        StatusPacketTypes.class,
        LoginPacketTypes.class,
        ConfigurationPacketTypes.class,
        CommonPacketTypes.class,
        CookiePacketTypes.class,
        GamePacketTypes.class
    );
    private static final Map<ResourceLocation, PacketInfo> ID_TO_INFO;
    private static final Map<Class<?>, PacketInfo> CLASS_TO_INFO;

    static {
        try {
            final ImmutableMap.Builder<ResourceLocation, PacketInfo> idBuilder = ImmutableMap.builder();
            final ImmutableMap.Builder<Class<?>, PacketInfo> classBuilder = ImmutableMap.builder();
            for (final Class<?> holder : PACKET_CLASS_HOLDERS) {
                for (final Field field : holder.getDeclaredFields()) {
                    if (!Modifier.isStatic(field.getModifiers()) || !Modifier.isFinal(field.getModifiers()) || !field.getType().equals(PacketType.class)) {
                        continue;
                    }
                    final Type genericType = field.getGenericType();
                    if (!(genericType instanceof final ParameterizedType parameterizedType)) {
                        throw new RuntimeException("Unexpected generic type: " + genericType);
                    }
                    final PacketType<?> type = (PacketType<?>) field.get(null);
                    if (type.flow() != PacketFlow.SERVERBOUND) {
                        // we only care about serverbound for rate limiting
                        continue;
                    }
                    final Class<?> packetClass = GenericTypeReflector.erase(parameterizedType.getActualTypeArguments()[0]);
                    final PacketInfo info = new PacketInfo(packetClass.asSubclass(Packet.class), type);
                    idBuilder.put(type.id(), info);
                    classBuilder.put(packetClass, info);
                }
            }
            ID_TO_INFO = idBuilder.buildOrThrow();
            CLASS_TO_INFO = classBuilder.buildOrThrow();
            Preconditions.checkState(ID_TO_INFO.size() == 75, "Packet class map must have 75 entries");
        } catch (final ReflectiveOperationException e) {
            throw new RuntimeException("Could not create packet class map", e);
        }
    }

    public ServerboundPacketClassSerializer() {
        super(TYPE);
    }

    @Override
    public Class<? extends Packet<?>> deserialize(final Type type, final Object obj) throws SerializationException {
        final ResourceLocation location = ResourceLocation.tryParse(obj.toString());
        if (location == null) {
            throw new SerializationException(ResourceLocation.class, "Could not deserialize a packet from " + obj);
        }
        final PacketInfo info = ID_TO_INFO.get(location);
        if (info == null) {
            throw new SerializationException("Could not deserialize a packet from " + obj);
        }
        return info.packetClass();
    }

    @Override
    protected @Nullable Object serialize(final Class<? extends Packet<?>> packetClass, final Predicate<Class<?>> typeSupported) {
        final PacketInfo info = CLASS_TO_INFO.get(packetClass);
        if (info == null) {
            LOGGER.error("Could not serialize {} into a packet identifier", packetClass);
            return null;
        } else {
            return info.type().id().toString();
        }
    }

    @SuppressWarnings("rawtypes")
    record PacketInfo(Class<? extends Packet> clazz, PacketType<?> type) {

        @SuppressWarnings("unchecked")
        public Class<? extends Packet<?>> packetClass() {
            return (Class<? extends Packet<?>>) this.clazz;
        }
    }
}
