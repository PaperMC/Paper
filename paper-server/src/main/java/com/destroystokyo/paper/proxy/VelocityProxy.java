package com.destroystokyo.paper.proxy;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.net.InetAddresses;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import io.papermc.paper.configuration.GlobalConfiguration;
import java.net.InetAddress;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.login.custom.CustomQueryPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.ProfilePublicKey;

/**
 * While Velocity supports BungeeCord-style IP forwarding, it is not secure. Users
 * have a lot of problems setting up firewalls or setting up plugins like IPWhitelist.
 * Further, the BungeeCord IP forwarding protocol still retains essentially its original
 * form, when there is brand-new support for custom login plugin messages in 1.13.
 * <p>
 * Velocity's modern IP forwarding uses an HMAC-SHA256 code to ensure authenticity
 * of messages, is packed into a binary format that is smaller than BungeeCord's
 * forwarding, and is integrated into the Minecraft login process by using the 1.13
 * login plugin message packet.
 */
public class VelocityProxy {
    private static final int SUPPORTED_FORWARDING_VERSION = 1;
    public static final int MODERN_FORWARDING_WITH_KEY = 2;
    public static final int MODERN_FORWARDING_WITH_KEY_V2 = 3;
    public static final int MODERN_LAZY_SESSION = 4;
    public static final byte MAX_SUPPORTED_FORWARDING_VERSION = MODERN_LAZY_SESSION;
    public static final ResourceLocation PLAYER_INFO_CHANNEL = ResourceLocation.fromNamespaceAndPath("velocity", "player_info");

    public static boolean checkIntegrity(final FriendlyByteBuf buf) {
        final byte[] signature = new byte[32];
        buf.readBytes(signature);

        final byte[] data = new byte[buf.readableBytes()];
        buf.getBytes(buf.readerIndex(), data);

        try {
            final Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(GlobalConfiguration.get().proxies.velocity.secret.getBytes(java.nio.charset.StandardCharsets.UTF_8), "HmacSHA256"));
            final byte[] mySignature = mac.doFinal(data);
            if (!MessageDigest.isEqual(signature, mySignature)) {
                return false;
            }
        } catch (final InvalidKeyException | NoSuchAlgorithmException e) {
            throw new AssertionError(e);
        }

        return true;
    }

    public static InetAddress readAddress(final FriendlyByteBuf buf) {
        return InetAddresses.forString(buf.readUtf(Short.MAX_VALUE));
    }

    public static GameProfile createProfile(final FriendlyByteBuf buf) {
        return new GameProfile(buf.readUUID(), buf.readUtf(16), readProperties(buf));
    }

    private static PropertyMap readProperties(final FriendlyByteBuf buf) {
        final ImmutableMultimap.Builder<String, Property> propertiesBuilder = ImmutableMultimap.builder();
        final int properties = buf.readVarInt();
        for (int i1 = 0; i1 < properties; i1++) {
            final String name = buf.readUtf(Short.MAX_VALUE);
            final String value = buf.readUtf(Short.MAX_VALUE);
            final String signature = buf.readBoolean() ? buf.readUtf(Short.MAX_VALUE) : null;
            propertiesBuilder.put(name, new Property(name, value, signature));
        }
        final ImmutableMultimap<String, Property> propertiesMap = propertiesBuilder.build();
        return new PropertyMap(propertiesMap);
    }

    public static ProfilePublicKey.Data readForwardedKey(FriendlyByteBuf buf) {
        return new ProfilePublicKey.Data(buf);
    }

    public static UUID readSignerUuidOrElse(FriendlyByteBuf buf, UUID orElse) {
        return buf.readBoolean() ? buf.readUUID() : orElse;
    }
}
