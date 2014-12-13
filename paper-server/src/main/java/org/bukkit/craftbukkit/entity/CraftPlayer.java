package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.BaseEncoding;
import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Pair;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.shorts.ShortArraySet;
import it.unimi.dsi.fastutil.shorts.ShortSet;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ClientboundResourcePackPopPacket;
import net.minecraft.network.protocol.common.ClientboundResourcePackPushPacket;
import net.minecraft.network.protocol.common.ClientboundServerLinksPacket;
import net.minecraft.network.protocol.common.ClientboundStoreCookiePacket;
import net.minecraft.network.protocol.common.ClientboundTransferPacket;
import net.minecraft.network.protocol.common.custom.DiscardedPayload;
import net.minecraft.network.protocol.cookie.ClientboundCookieRequestPacket;
import net.minecraft.network.protocol.cookie.ServerboundCookieResponsePacket;
import net.minecraft.network.protocol.game.ClientboundBlockDestructionPacket;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundClearTitlesPacket;
import net.minecraft.network.protocol.game.ClientboundCustomChatCompletionsPacket;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.protocol.game.ClientboundHurtAnimationPacket;
import net.minecraft.network.protocol.game.ClientboundLevelEventPacket;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundRemoveMobEffectPacket;
import net.minecraft.network.protocol.game.ClientboundSectionBlocksUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundSetBorderCenterPacket;
import net.minecraft.network.protocol.game.ClientboundSetBorderLerpSizePacket;
import net.minecraft.network.protocol.game.ClientboundSetBorderSizePacket;
import net.minecraft.network.protocol.game.ClientboundSetBorderWarningDelayPacket;
import net.minecraft.network.protocol.game.ClientboundSetBorderWarningDistancePacket;
import net.minecraft.network.protocol.game.ClientboundSetDefaultSpawnPositionPacket;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.network.protocol.game.ClientboundSetExperiencePacket;
import net.minecraft.network.protocol.game.ClientboundSetHealthPacket;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.network.protocol.game.ClientboundSoundEntityPacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
import net.minecraft.network.protocol.game.ClientboundTabListPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateAttributesPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.players.UserWhiteListEntry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.world.level.border.BorderChangeListener;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Input;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Note;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.ServerLinks;
import org.bukkit.Sound;
import org.bukkit.Statistic;
import org.bukkit.WeatherType;
import org.bukkit.WorldBorder;
import org.bukkit.ban.IpBanList;
import org.bukkit.ban.ProfileBanList;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.block.TileState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.sign.Side;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ManuallyAbandonedConversationCanceller;
import org.bukkit.craftbukkit.CraftEffect;
import org.bukkit.craftbukkit.CraftEquipmentSlot;
import org.bukkit.craftbukkit.CraftInput;
import org.bukkit.craftbukkit.CraftOfflinePlayer;
import org.bukkit.craftbukkit.CraftParticle;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftServerLinks;
import org.bukkit.craftbukkit.CraftSound;
import org.bukkit.craftbukkit.CraftStatistic;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.CraftWorldBorder;
import org.bukkit.craftbukkit.advancement.CraftAdvancement;
import org.bukkit.craftbukkit.advancement.CraftAdvancementProgress;
import org.bukkit.craftbukkit.block.CraftBlockEntityState;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.craftbukkit.block.CraftSign;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.conversations.ConversationTracker;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.inventory.CraftRecipe;
import org.bukkit.craftbukkit.map.CraftMapCursor;
import org.bukkit.craftbukkit.map.CraftMapView;
import org.bukkit.craftbukkit.map.RenderData;
import org.bukkit.craftbukkit.potion.CraftPotionEffectType;
import org.bukkit.craftbukkit.potion.CraftPotionUtil;
import org.bukkit.craftbukkit.profile.CraftPlayerProfile;
import org.bukkit.craftbukkit.scoreboard.CraftScoreboard;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerExpCooldownChangeEvent;
import org.bukkit.event.player.PlayerHideEntityEvent;
import org.bukkit.event.player.PlayerRegisterChannelEvent;
import org.bukkit.event.player.PlayerShowEntityEvent;
import org.bukkit.event.player.PlayerSpawnChangeEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerUnregisterChannelEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.InventoryView.Property;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapCursor;
import org.bukkit.map.MapView;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.StandardMessenger;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.NotNull;

import net.md_5.bungee.api.chat.BaseComponent; // Spigot

@DelegateDeserialization(CraftOfflinePlayer.class)
public class CraftPlayer extends CraftHumanEntity implements Player {
    private long firstPlayed = 0;
    private long lastPlayed = 0;
    private boolean hasPlayedBefore = false;
    private final ConversationTracker conversationTracker = new ConversationTracker();
    private final Set<String> channels = new HashSet<String>();
    private final Map<UUID, Set<WeakReference<Plugin>>> invertedVisibilityEntities = new HashMap<>();
    private static final WeakHashMap<Plugin, WeakReference<Plugin>> pluginWeakReferences = new WeakHashMap<>();
    private int hash = 0;
    private double health = 20;
    private boolean scaledHealth = false;
    private double healthScale = 20;
    private CraftWorldBorder clientWorldBorder = null;
    private BorderChangeListener clientWorldBorderListener = this.createWorldBorderListener();

    public CraftPlayer(CraftServer server, ServerPlayer entity) {
        super(server, entity);

        this.firstPlayed = System.currentTimeMillis();
    }

    public GameProfile getProfile() {
        return this.getHandle().getGameProfile();
    }

    @Override
    public void remove() {
        // Will lead to an inconsistent player state if we remove the player as any other entity.
        throw new UnsupportedOperationException(String.format("Cannot remove player %s, use Player#kickPlayer(String) instead.", this.getName()));
    }

    @Override
    public boolean isOp() {
        return this.server.getHandle().isOp(this.getProfile());
    }

    @Override
    public void setOp(boolean value) {
        if (value == this.isOp()) return;

        if (value) {
            this.server.getHandle().op(this.getProfile());
        } else {
            this.server.getHandle().deop(this.getProfile());
        }

        this.perm.recalculatePermissions();
    }

    @Override
    public boolean isOnline() {
        return this.server.getPlayer(this.getUniqueId()) != null;
    }

    @Override
    public PlayerProfile getPlayerProfile() {
        return new CraftPlayerProfile(this.getProfile());
    }

    @Override
    public InetSocketAddress getAddress() {
        if (this.getHandle().connection.protocol() == null) return null;

        SocketAddress addr = this.getHandle().connection.getRemoteAddress();
        if (addr instanceof InetSocketAddress) {
            return (InetSocketAddress) addr;
        } else {
            return null;
        }
    }

    public interface TransferCookieConnection {

        boolean isTransferred();

        ConnectionProtocol getProtocol();

        void sendPacket(Packet<?> packet);

        void kickPlayer(Component reason);
    }

    public record CookieFuture(ResourceLocation key, CompletableFuture<byte[]> future) {

    }
    private final Queue<CookieFuture> requestedCookies = new LinkedList<>();

    public boolean isAwaitingCookies() {
        return !this.requestedCookies.isEmpty();
    }

    public boolean handleCookieResponse(ServerboundCookieResponsePacket response) {
        CookieFuture future = this.requestedCookies.peek();
        if (future != null) {
            if (future.key.equals(response.key())) {
                Preconditions.checkState(future == this.requestedCookies.poll(), "requestedCookies queue mismatch");

                future.future().complete(response.payload());
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isTransferred() {
        return this.getHandle().transferCookieConnection.isTransferred();
    }

    @Override
    public CompletableFuture<byte[]> retrieveCookie(NamespacedKey key) {
        Preconditions.checkArgument(key != null, "Cookie key cannot be null");

        CompletableFuture<byte[]> future = new CompletableFuture<>();
        ResourceLocation nms = CraftNamespacedKey.toMinecraft(key);
        this.requestedCookies.add(new CookieFuture(nms, future));

        this.getHandle().transferCookieConnection.sendPacket(new ClientboundCookieRequestPacket(nms));

        return future;
    }

    @Override
    public void storeCookie(NamespacedKey key, byte[] value) {
        Preconditions.checkArgument(key != null, "Cookie key cannot be null");
        Preconditions.checkArgument(value != null, "Cookie value cannot be null");
        Preconditions.checkArgument(value.length <= 5120, "Cookie value too large, must be smaller than 5120 bytes");
        Preconditions.checkState(this.getHandle().transferCookieConnection.getProtocol() == ConnectionProtocol.CONFIGURATION || this.getHandle().transferCookieConnection.getProtocol() == ConnectionProtocol.PLAY, "Can only store cookie in CONFIGURATION or PLAY protocol.");

        this.getHandle().transferCookieConnection.sendPacket(new ClientboundStoreCookiePacket(CraftNamespacedKey.toMinecraft(key), value));
    }

    @Override
    public void transfer(String host, int port) {
        Preconditions.checkArgument(host != null, "Host cannot be null");
        Preconditions.checkState(this.getHandle().transferCookieConnection.getProtocol() == ConnectionProtocol.CONFIGURATION || this.getHandle().transferCookieConnection.getProtocol() == ConnectionProtocol.PLAY, "Can only transfer in CONFIGURATION or PLAY protocol.");

        this.getHandle().transferCookieConnection.sendPacket(new ClientboundTransferPacket(host, port));
    }

    @Override
    public double getEyeHeight(boolean ignorePose) {
        if (ignorePose) {
            return 1.62D;
        } else {
            return this.getEyeHeight();
        }
    }

    @Override
    public void sendRawMessage(String message) {
        this.sendRawMessage(null, message);
    }

    @Override
    public void sendRawMessage(UUID sender, String message) {
        Preconditions.checkArgument(message != null, "message cannot be null");

        if (this.getHandle().connection == null) return;

        for (Component component : CraftChatMessage.fromString(message)) {
            this.getHandle().sendSystemMessage(component);
        }
    }

    @Override
    public void sendMessage(String message) {
        if (!this.conversationTracker.isConversingModaly()) {
            this.sendRawMessage(message);
        }
    }

    @Override
    public void sendMessage(String... messages) {
        for (String message : messages) {
            this.sendMessage(message);
        }
    }

    @Override
    public void sendMessage(UUID sender, String message) {
        if (!this.conversationTracker.isConversingModaly()) {
            this.sendRawMessage(sender, message);
        }
    }

    @Override
    public void sendMessage(UUID sender, String... messages) {
        for (String message : messages) {
            this.sendMessage(sender, message);
        }
    }

    @Override
    public String getDisplayName() {
        return this.getHandle().displayName;
    }

    @Override
    public void setDisplayName(final String name) {
        this.getHandle().displayName = name == null ? this.getName() : name;
    }

    @Override
    public String getPlayerListName() {
        return this.getHandle().listName == null ? this.getName() : CraftChatMessage.fromComponent(this.getHandle().listName);
    }

    @Override
    public void setPlayerListName(String name) {
        if (name == null) {
            name = this.getName();
        }
        this.getHandle().listName = name.equals(this.getName()) ? null : CraftChatMessage.fromStringOrNull(name);
        for (ServerPlayer player : (List<ServerPlayer>) this.server.getHandle().players) {
            if (player.getBukkitEntity().canSee(this)) {
                player.connection.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME, this.getHandle()));
            }
        }
    }

    @Override
    public int getPlayerListOrder() {
        return this.getHandle().listOrder;
    }

    @Override
    public void setPlayerListOrder(int order) {
        Preconditions.checkArgument(order >= 0, "order cannot be negative");

        this.getHandle().listOrder = order;
    }

    private Component playerListHeader;
    private Component playerListFooter;

    @Override
    public String getPlayerListHeader() {
        return (this.playerListHeader == null) ? null : CraftChatMessage.fromComponent(this.playerListHeader);
    }

    @Override
    public String getPlayerListFooter() {
        return (this.playerListFooter == null) ? null : CraftChatMessage.fromComponent(this.playerListFooter);
    }

    @Override
    public void setPlayerListHeader(String header) {
        this.playerListHeader = CraftChatMessage.fromStringOrNull(header, true);
        this.updatePlayerListHeaderFooter();
    }

    @Override
    public void setPlayerListFooter(String footer) {
        this.playerListFooter = CraftChatMessage.fromStringOrNull(footer, true);
        this.updatePlayerListHeaderFooter();
    }

    @Override
    public void setPlayerListHeaderFooter(String header, String footer) {
        this.playerListHeader = CraftChatMessage.fromStringOrNull(header, true);
        this.playerListFooter = CraftChatMessage.fromStringOrNull(footer, true);
        this.updatePlayerListHeaderFooter();
    }

    private void updatePlayerListHeaderFooter() {
        if (this.getHandle().connection == null) return;

        ClientboundTabListPacket packet = new ClientboundTabListPacket((this.playerListHeader == null) ? Component.empty() : this.playerListHeader, (this.playerListFooter == null) ? Component.empty() : this.playerListFooter);
        this.getHandle().connection.send(packet);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof OfflinePlayer)) {
            return false;
        }
        OfflinePlayer other = (OfflinePlayer) obj;
        if ((this.getUniqueId() == null) || (other.getUniqueId() == null)) {
            return false;
        }

        boolean uuidEquals = this.getUniqueId().equals(other.getUniqueId());
        boolean idEquals = true;

        if (other instanceof CraftPlayer) {
            idEquals = this.getEntityId() == ((CraftPlayer) other).getEntityId();
        }

        return uuidEquals && idEquals;
    }

    @Override
    public void kickPlayer(String message) {
        org.spigotmc.AsyncCatcher.catchOp("player kick"); // Spigot
        this.getHandle().transferCookieConnection.kickPlayer(CraftChatMessage.fromStringOrEmpty(message, true));
    }

    @Override
    public void setCompassTarget(Location loc) {
        Preconditions.checkArgument(loc != null, "Location cannot be null");

        if (this.getHandle().connection == null) return;

        // Do not directly assign here, from the packethandler we'll assign it.
        this.getHandle().connection.send(new ClientboundSetDefaultSpawnPositionPacket(CraftLocation.toBlockPosition(loc), loc.getYaw()));
    }

    @Override
    public Location getCompassTarget() {
        return this.getHandle().compassTarget;
    }

    @Override
    public void chat(String msg) {
        Preconditions.checkArgument(msg != null, "msg cannot be null");

        if (this.getHandle().connection == null) return;

        this.getHandle().connection.chat(msg, PlayerChatMessage.system(msg), false);
    }

    @Override
    public boolean performCommand(String command) {
        Preconditions.checkArgument(command != null, "command cannot be null");
        return this.server.dispatchCommand(this, command);
    }

    @Override
    public void playNote(Location loc, byte instrument, byte note) {
        this.playNote(loc, Instrument.getByType(instrument), new Note(note));
    }

    @Override
    public void playNote(Location loc, Instrument instrument, Note note) {
        Preconditions.checkArgument(loc != null, "Location cannot be null");
        Preconditions.checkArgument(instrument != null, "Instrument cannot be null");
        Preconditions.checkArgument(note != null, "Note cannot be null");

        if (this.getHandle().connection == null) return;

        Sound instrumentSound = instrument.getSound();
        if (instrumentSound == null) return;

        float pitch = note.getPitch();
        this.getHandle().connection.send(new ClientboundSoundPacket(CraftSound.bukkitToMinecraftHolder(instrumentSound), net.minecraft.sounds.SoundSource.RECORDS, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), 3.0f, pitch, this.getHandle().getRandom().nextLong()));
    }

    @Override
    public void playSound(Location loc, Sound sound, float volume, float pitch) {
        this.playSound(loc, sound, org.bukkit.SoundCategory.MASTER, volume, pitch);
    }

    @Override
    public void playSound(Location loc, String sound, float volume, float pitch) {
        this.playSound(loc, sound, org.bukkit.SoundCategory.MASTER, volume, pitch);
    }

    @Override
    public void playSound(Location loc, Sound sound, org.bukkit.SoundCategory category, float volume, float pitch) {
        this.playSound(loc, sound, category, volume, pitch, this.getHandle().random.nextLong());
    }

    @Override
    public void playSound(Location loc, String sound, org.bukkit.SoundCategory category, float volume, float pitch) {
        this.playSound(loc, sound, category, volume, pitch, this.getHandle().random.nextLong());
    }

    @Override
    public void playSound(Location loc, Sound sound, org.bukkit.SoundCategory category, float volume, float pitch, long seed) {
        if (loc == null || sound == null || category == null || this.getHandle().connection == null) return;

        this.playSound0(loc, CraftSound.bukkitToMinecraftHolder(sound), net.minecraft.sounds.SoundSource.valueOf(category.name()), volume, pitch, seed);
    }

    @Override
    public void playSound(Location loc, String sound, org.bukkit.SoundCategory category, float volume, float pitch, long seed) {
        if (loc == null || sound == null || category == null || this.getHandle().connection == null) return;

        this.playSound0(loc, Holder.direct(SoundEvent.createVariableRangeEvent(ResourceLocation.parse(sound))), net.minecraft.sounds.SoundSource.valueOf(category.name()), volume, pitch, seed);
    }

    private void playSound0(Location loc, Holder<SoundEvent> soundEffectHolder, net.minecraft.sounds.SoundSource categoryNMS, float volume, float pitch, long seed) {
        Preconditions.checkArgument(loc != null, "Location cannot be null");

        if (this.getHandle().connection == null) return;

        ClientboundSoundPacket packet = new ClientboundSoundPacket(soundEffectHolder, categoryNMS, loc.getX(), loc.getY(), loc.getZ(), volume, pitch, seed);
        this.getHandle().connection.send(packet);
    }

    @Override
    public void playSound(org.bukkit.entity.Entity entity, Sound sound, float volume, float pitch) {
        this.playSound(entity, sound, org.bukkit.SoundCategory.MASTER, volume, pitch);
    }

    @Override
    public void playSound(org.bukkit.entity.Entity entity, String sound, float volume, float pitch) {
        this.playSound(entity, sound, org.bukkit.SoundCategory.MASTER, volume, pitch);
    }

    @Override
    public void playSound(org.bukkit.entity.Entity entity, Sound sound, org.bukkit.SoundCategory category, float volume, float pitch) {
        this.playSound(entity, sound, category, volume, pitch, this.getHandle().random.nextLong());
    }

    @Override
    public void playSound(org.bukkit.entity.Entity entity, String sound, org.bukkit.SoundCategory category, float volume, float pitch) {
        this.playSound(entity, sound, category, volume, pitch, this.getHandle().random.nextLong());
    }

    @Override
    public void playSound(org.bukkit.entity.Entity entity, Sound sound, org.bukkit.SoundCategory category, float volume, float pitch, long seed) {
        if (!(entity instanceof CraftEntity craftEntity) || sound == null || category == null || this.getHandle().connection == null) return;

        this.playSound0(entity, CraftSound.bukkitToMinecraftHolder(sound), net.minecraft.sounds.SoundSource.valueOf(category.name()), volume, pitch, seed);
    }

    @Override
    public void playSound(org.bukkit.entity.Entity entity, String sound, org.bukkit.SoundCategory category, float volume, float pitch, long seed) {
        if (!(entity instanceof CraftEntity craftEntity) || sound == null || category == null || this.getHandle().connection == null) return;

        this.playSound0(entity, Holder.direct(SoundEvent.createVariableRangeEvent(ResourceLocation.parse(sound))), net.minecraft.sounds.SoundSource.valueOf(category.name()), volume, pitch, seed);
    }

    private void playSound0(org.bukkit.entity.Entity entity, Holder<SoundEvent> soundEffectHolder, net.minecraft.sounds.SoundSource categoryNMS, float volume, float pitch, long seed) {
        Preconditions.checkArgument(entity != null, "Entity cannot be null");
        Preconditions.checkArgument(soundEffectHolder != null, "Holder of SoundEffect cannot be null");
        Preconditions.checkArgument(categoryNMS != null, "SoundCategory cannot be null");

        if (this.getHandle().connection == null) return;
        if (!(entity instanceof CraftEntity craftEntity)) return;

        ClientboundSoundEntityPacket packet = new ClientboundSoundEntityPacket(soundEffectHolder, categoryNMS, craftEntity.getHandle(), volume, pitch, seed);
        this.getHandle().connection.send(packet);
    }

    @Override
    public void stopSound(Sound sound) {
        this.stopSound(sound, null);
    }

    @Override
    public void stopSound(String sound) {
        this.stopSound(sound, null);
    }

    @Override
    public void stopSound(Sound sound, org.bukkit.SoundCategory category) {
        this.stopSound(sound.getKey().getKey(), category);
    }

    @Override
    public void stopSound(String sound, org.bukkit.SoundCategory category) {
        if (this.getHandle().connection == null) return;

        this.getHandle().connection.send(new ClientboundStopSoundPacket(ResourceLocation.parse(sound), category == null ? net.minecraft.sounds.SoundSource.MASTER : net.minecraft.sounds.SoundSource.valueOf(category.name())));
    }

    @Override
    public void stopSound(org.bukkit.SoundCategory category) {
        if (this.getHandle().connection == null) return;

        this.getHandle().connection.send(new ClientboundStopSoundPacket(null, net.minecraft.sounds.SoundSource.valueOf(category.name())));
    }

    @Override
    public void stopAllSounds() {
        if (this.getHandle().connection == null) return;

        this.getHandle().connection.send(new ClientboundStopSoundPacket(null, null));
    }

    @Override
    public void playEffect(Location loc, Effect effect, int data) {
        Preconditions.checkArgument(effect != null, "Effect cannot be null");
        Preconditions.checkArgument(loc != null, "Location cannot be null");

        if (this.getHandle().connection == null) return;

        int packetData = effect.getId();
        ClientboundLevelEventPacket packet = new ClientboundLevelEventPacket(packetData, CraftLocation.toBlockPosition(loc), data, false);
        this.getHandle().connection.send(packet);
    }

    @Override
    public <T> void playEffect(Location loc, Effect effect, T data) {
        Preconditions.checkArgument(effect != null, "Effect cannot be null");
        if (data != null) {
            Preconditions.checkArgument(effect.getData() != null, "Effect.%s does not have a valid Data", effect);
            Preconditions.checkArgument(effect.getData().isAssignableFrom(data.getClass()), "%s data cannot be used for the %s effect", data.getClass().getName(), effect);
        } else {
            // Special case: the axis is optional for ELECTRIC_SPARK
            Preconditions.checkArgument(effect.getData() == null || effect == Effect.ELECTRIC_SPARK, "Wrong kind of data for the %s effect", effect);
        }

        int datavalue = CraftEffect.getDataValue(effect, data);
        this.playEffect(loc, effect, datavalue);
    }

    @Override
    public boolean breakBlock(Block block) {
        Preconditions.checkArgument(block != null, "Block cannot be null");
        Preconditions.checkArgument(block.getWorld().equals(this.getWorld()), "Cannot break blocks across worlds");

        return this.getHandle().gameMode.destroyBlock(new BlockPos(block.getX(), block.getY(), block.getZ()));
    }

    @Override
    public void sendBlockChange(Location loc, Material material, byte data) {
        if (this.getHandle().connection == null) return;

        ClientboundBlockUpdatePacket packet = new ClientboundBlockUpdatePacket(CraftLocation.toBlockPosition(loc), CraftMagicNumbers.getBlock(material, data));
        this.getHandle().connection.send(packet);
    }

    @Override
    public void sendBlockChange(Location loc, BlockData block) {
        if (this.getHandle().connection == null) return;

        ClientboundBlockUpdatePacket packet = new ClientboundBlockUpdatePacket(CraftLocation.toBlockPosition(loc), ((CraftBlockData) block).getState());
        this.getHandle().connection.send(packet);
    }

    @Override
    public void sendBlockChanges(Collection<BlockState> blocks) {
        Preconditions.checkArgument(blocks != null, "blocks must not be null");

        if (this.getHandle().connection == null || blocks.isEmpty()) {
            return;
        }

        Map<SectionPos, ChunkSectionChanges> changes = new HashMap<>();
        for (BlockState state : blocks) {
            CraftBlockState cstate = (CraftBlockState) state;
            BlockPos blockPosition = cstate.getPosition();

            // The coordinates of the chunk section in which the block is located, aka chunk x, y, and z
            SectionPos sectionPosition = SectionPos.of(blockPosition);

            // Push the block change position and block data to the final change map
            ChunkSectionChanges sectionChanges = changes.computeIfAbsent(sectionPosition, (ignore) -> new ChunkSectionChanges());

            sectionChanges.positions().add(SectionPos.sectionRelativePos(blockPosition));
            sectionChanges.blockData().add(cstate.getHandle());
        }

        // Construct the packets using the data allocated above and send then to the players
        for (Map.Entry<SectionPos, ChunkSectionChanges> entry : changes.entrySet()) {
            ChunkSectionChanges chunkChanges = entry.getValue();
            ClientboundSectionBlocksUpdatePacket packet = new ClientboundSectionBlocksUpdatePacket(entry.getKey(), chunkChanges.positions(), chunkChanges.blockData().toArray(net.minecraft.world.level.block.state.BlockState[]::new));
            this.getHandle().connection.send(packet);
        }
    }

    @Override
    public void sendBlockChanges(Collection<BlockState> blocks, boolean suppressLightUpdates) {
        this.sendBlockChanges(blocks);
    }

    private record ChunkSectionChanges(ShortSet positions, List<net.minecraft.world.level.block.state.BlockState> blockData) {

        public ChunkSectionChanges() {
            this(new ShortArraySet(), new ArrayList<>());
        }
    }

    @Override
    public void sendBlockDamage(Location loc, float progress) {
        this.sendBlockDamage(loc, progress, this.getEntityId());
    }

    @Override
    public void sendBlockDamage(Location loc, float progress, org.bukkit.entity.Entity source) {
        Preconditions.checkArgument(source != null, "source must not be null");
        this.sendBlockDamage(loc, progress, source.getEntityId());
    }

    @Override
    public void sendBlockDamage(Location loc, float progress, int sourceId) {
        Preconditions.checkArgument(loc != null, "loc must not be null");
        Preconditions.checkArgument(progress >= 0.0 && progress <= 1.0, "progress must be between 0.0 and 1.0 (inclusive)");

        if (this.getHandle().connection == null) return;

        int stage = (int) (9 * progress); // There are 0 - 9 damage states
        if (progress == 0.0F) {
            stage = -1; // The protocol states that any other value will reset the damage, which this API promises
        }

        ClientboundBlockDestructionPacket packet = new ClientboundBlockDestructionPacket(sourceId, CraftLocation.toBlockPosition(loc), stage);
        this.getHandle().connection.send(packet);
    }

    @Override
    public void sendSignChange(Location loc, String[] lines) {
        this.sendSignChange(loc, lines, DyeColor.BLACK);
    }

    @Override
    public void sendSignChange(Location loc, String[] lines, DyeColor dyeColor) {
        this.sendSignChange(loc, lines, dyeColor, false);
    }

    @Override
    public void sendSignChange(Location loc, String[] lines, DyeColor dyeColor, boolean hasGlowingText) {
        Preconditions.checkArgument(loc != null, "Location cannot be null");
        Preconditions.checkArgument(dyeColor != null, "DyeColor cannot be null");

        if (lines == null) {
            lines = new String[4];
        }
        Preconditions.checkArgument(lines.length >= 4, "Must have at least 4 lines (%s)", lines.length);

        if (this.getHandle().connection == null) return;

        Component[] components = CraftSign.sanitizeLines(lines);
        SignBlockEntity sign = new SignBlockEntity(CraftLocation.toBlockPosition(loc), Blocks.OAK_SIGN.defaultBlockState());
        SignText text = sign.getFrontText();
        text = text.setColor(net.minecraft.world.item.DyeColor.byId(dyeColor.getWoolData()));
        text = text.setHasGlowingText(hasGlowingText);
        for (int i = 0; i < components.length; i++) {
            text = text.setMessage(i, components[i]);
        }
        sign.setText(text, true);

        this.getHandle().connection.send(new ClientboundBlockEntityDataPacket(sign.getBlockPos(), sign.getType(), sign.getUpdateTag(this.getHandle().registryAccess())));
    }

    @Override
    public void sendBlockUpdate(@NotNull Location location, @NotNull TileState tileState) throws IllegalArgumentException {
        Preconditions.checkArgument(location != null, "Location can not be null");
        Preconditions.checkArgument(tileState != null, "TileState can not be null");

        if (this.getHandle().connection == null) return;

        CraftBlockEntityState<?> craftState = ((CraftBlockEntityState<?>) tileState);
        this.getHandle().connection.send(craftState.getUpdatePacket(location));
    }

    @Override
    public void sendEquipmentChange(LivingEntity entity, EquipmentSlot slot, ItemStack item) {
        this.sendEquipmentChange(entity, Map.of(slot, item));
    }

    @Override
    public void sendEquipmentChange(LivingEntity entity, Map<EquipmentSlot, ItemStack> items) {
        Preconditions.checkArgument(entity != null, "Entity cannot be null");
        Preconditions.checkArgument(items != null, "items cannot be null");

        if (this.getHandle().connection == null) {
            return;
        }

        List<Pair<net.minecraft.world.entity.EquipmentSlot, net.minecraft.world.item.ItemStack>> equipment = new ArrayList<>(items.size());
        for (Map.Entry<EquipmentSlot, ItemStack> entry : items.entrySet()) {
            EquipmentSlot slot = entry.getKey();
            Preconditions.checkArgument(slot != null, "Cannot set null EquipmentSlot");

            equipment.add(new Pair<>(CraftEquipmentSlot.getNMS(slot), CraftItemStack.asNMSCopy(entry.getValue())));
        }

        this.getHandle().connection.send(new ClientboundSetEquipmentPacket(entity.getEntityId(), equipment));
    }

    @Override
    public void sendPotionEffectChange(LivingEntity entity, PotionEffect effect) {
        Preconditions.checkArgument(entity != null, "Entity cannot be null");
        Preconditions.checkArgument(effect != null, "Effect cannot be null");

        if (this.getHandle().connection == null) {
            return;
        }

        this.getHandle().connection.send(new ClientboundUpdateMobEffectPacket(entity.getEntityId(), CraftPotionUtil.fromBukkit(effect), true));
    }

    @Override
    public void sendPotionEffectChangeRemove(LivingEntity entity, PotionEffectType type) {
        Preconditions.checkArgument(entity != null, "Entity cannot be null");
        Preconditions.checkArgument(type != null, "Type cannot be null");

        if (this.getHandle().connection == null) {
            return;
        }

        this.getHandle().connection.send(new ClientboundRemoveMobEffectPacket(entity.getEntityId(), CraftPotionEffectType.bukkitToMinecraftHolder(type)));
    }

    @Override
    public WorldBorder getWorldBorder() {
        return this.clientWorldBorder;
    }

    @Override
    public void setWorldBorder(WorldBorder border) {
        CraftWorldBorder craftBorder = (CraftWorldBorder) border;

        if (border != null && !craftBorder.isVirtual() && !craftBorder.getWorld().equals(this.getWorld())) {
            throw new UnsupportedOperationException("Cannot set player world border to that of another world");
        }

        // Nullify the old client-sided world border listeners so that calls to it will not affect this player
        if (this.clientWorldBorder != null) {
            this.clientWorldBorder.getHandle().removeListener(this.clientWorldBorderListener);
        }

        net.minecraft.world.level.border.WorldBorder newWorldBorder;
        if (craftBorder == null || !craftBorder.isVirtual()) {
            this.clientWorldBorder = null;
            newWorldBorder = ((CraftWorldBorder) this.getWorld().getWorldBorder()).getHandle();
        } else {
            this.clientWorldBorder = craftBorder;
            this.clientWorldBorder.getHandle().addListener(this.clientWorldBorderListener);
            newWorldBorder = this.clientWorldBorder.getHandle();
        }

        // Send all world border update packets to the player
        ServerGamePacketListenerImpl connection = this.getHandle().connection;
        connection.send(new ClientboundSetBorderSizePacket(newWorldBorder));
        connection.send(new ClientboundSetBorderLerpSizePacket(newWorldBorder));
        connection.send(new ClientboundSetBorderCenterPacket(newWorldBorder));
        connection.send(new ClientboundSetBorderWarningDelayPacket(newWorldBorder));
        connection.send(new ClientboundSetBorderWarningDistancePacket(newWorldBorder));
    }

    private BorderChangeListener createWorldBorderListener() {
        return new BorderChangeListener() {
            @Override
            public void onBorderSizeSet(net.minecraft.world.level.border.WorldBorder border, double size) {
                CraftPlayer.this.getHandle().connection.send(new ClientboundSetBorderSizePacket(border));
            }

            @Override
            public void onBorderSizeLerping(net.minecraft.world.level.border.WorldBorder border, double fromSize, double toSize, long time) {
                CraftPlayer.this.getHandle().connection.send(new ClientboundSetBorderLerpSizePacket(border));
            }

            @Override
            public void onBorderCenterSet(net.minecraft.world.level.border.WorldBorder border, double centerX, double centerZ) {
                CraftPlayer.this.getHandle().connection.send(new ClientboundSetBorderCenterPacket(border));
            }

            @Override
            public void onBorderSetWarningTime(net.minecraft.world.level.border.WorldBorder border, int warningTime) {
                CraftPlayer.this.getHandle().connection.send(new ClientboundSetBorderWarningDelayPacket(border));
            }

            @Override
            public void onBorderSetWarningBlocks(net.minecraft.world.level.border.WorldBorder border, int warningBlockDistance) {
                CraftPlayer.this.getHandle().connection.send(new ClientboundSetBorderWarningDistancePacket(border));
            }

            @Override
            public void onBorderSetDamagePerBlock(net.minecraft.world.level.border.WorldBorder border, double damagePerBlock) {} // NO OP

            @Override
            public void onBorderSetDamageSafeZOne(net.minecraft.world.level.border.WorldBorder border, double safeZoneRadius) {} // NO OP
        };
    }

    public boolean hasClientWorldBorder() {
        return this.clientWorldBorder != null;
    }

    @Override
    public void sendMap(MapView map) {
        if (this.getHandle().connection == null) return;

        RenderData data = ((CraftMapView) map).render(this);
        Collection<MapDecoration> icons = new ArrayList<MapDecoration>();
        for (MapCursor cursor : data.cursors) {
            if (cursor.isVisible()) {
                icons.add(new MapDecoration(CraftMapCursor.CraftType.bukkitToMinecraftHolder(cursor.getType()), cursor.getX(), cursor.getY(), cursor.getDirection(), CraftChatMessage.fromStringOrOptional(cursor.getCaption())));
            }
        }

        ClientboundMapItemDataPacket packet = new ClientboundMapItemDataPacket(new MapId(map.getId()), map.getScale().getValue(), map.isLocked(), icons, new MapItemSavedData.MapPatch(0, 0, 128, 128, data.buffer));
        this.getHandle().connection.send(packet);
    }

    @Override
    public void sendHurtAnimation(float yaw) {
        if (this.getHandle().connection == null) {
            return;
        }

        /*
         * Vanilla degrees state that 0 = left, 90 = front, 180 = right, and 270 = behind.
         * This makes no sense. We'll add 90 to it so that 0 = front, clockwise from there.
         */
        float actualYaw = yaw + 90;
        this.getHandle().connection.send(new ClientboundHurtAnimationPacket(this.getEntityId(), actualYaw));
    }

    @Override
    public void sendLinks(ServerLinks links) {
        if (this.getHandle().connection == null) {
            return;
        }
        Preconditions.checkArgument(links != null, "links cannot be null");

        net.minecraft.server.ServerLinks nms = ((CraftServerLinks) links).getServerLinks();
        this.getHandle().connection.send(new ClientboundServerLinksPacket(nms.untrust()));
    }

    @Override
    public void addCustomChatCompletions(Collection<String> completions) {
        this.sendCustomChatCompletionPacket(completions, ClientboundCustomChatCompletionsPacket.Action.ADD);
    }

    @Override
    public void removeCustomChatCompletions(Collection<String> completions) {
        this.sendCustomChatCompletionPacket(completions, ClientboundCustomChatCompletionsPacket.Action.REMOVE);
    }

    @Override
    public void setCustomChatCompletions(Collection<String> completions) {
        this.sendCustomChatCompletionPacket(completions, ClientboundCustomChatCompletionsPacket.Action.SET);
    }

    private void sendCustomChatCompletionPacket(Collection<String> completions, ClientboundCustomChatCompletionsPacket.Action action) {
        if (this.getHandle().connection == null) return;

        ClientboundCustomChatCompletionsPacket packet = new ClientboundCustomChatCompletionsPacket(action, new ArrayList<>(completions));
        this.getHandle().connection.send(packet);
    }

    @Override
    public void setRotation(float yaw, float pitch) {
        throw new UnsupportedOperationException("Cannot set rotation of players. Consider teleporting instead.");
    }

    @Override
    public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause cause) {
        Preconditions.checkArgument(location != null, "location");
        Preconditions.checkArgument(location.getWorld() != null, "location.world");
        location.checkFinite();

        ServerPlayer entity = this.getHandle();

        if (this.getHealth() == 0 || entity.isRemoved()) {
            return false;
        }

        if (entity.connection == null) {
            return false;
        }

        if (entity.isVehicle()) {
            return false;
        }

        // From = Players current Location
        Location from = this.getLocation();
        // To = Players new Location if Teleport is Successful
        Location to = location;
        // Create & Call the Teleport Event.
        PlayerTeleportEvent event = new PlayerTeleportEvent(this, from, to, cause);
        this.server.getPluginManager().callEvent(event);

        // Return False to inform the Plugin that the Teleport was unsuccessful/cancelled.
        if (event.isCancelled()) {
            return false;
        }

        // If this player is riding another entity, we must dismount before teleporting.
        entity.stopRiding();

        // SPIGOT-5509: Wakeup, similar to riding
        if (this.isSleeping()) {
            this.wakeup(false);
        }

        // Update the From Location
        from = event.getFrom();
        // Grab the new To Location dependent on whether the event was cancelled.
        to = event.getTo();
        // Grab the To and From World Handles.
        ServerLevel fromWorld = ((CraftWorld) from.getWorld()).getHandle();
        ServerLevel toWorld = ((CraftWorld) to.getWorld()).getHandle();

        // Close any foreign inventory
        if (this.getHandle().containerMenu != this.getHandle().inventoryMenu) {
            this.getHandle().closeContainer();
        }

        // Check if the fromWorld and toWorld are the same.
        if (fromWorld == toWorld) {
            entity.connection.teleport(to);
        } else {
            entity.portalProcess = null; // SPIGOT-7785: there is no need to carry this over as it contains the old world/location and we might run into trouble if there is a portal in the same spot in both worlds
            // The respawn reason should never be used if the passed location is non null.
            this.server.getHandle().respawn(entity, true, Entity.RemovalReason.CHANGED_DIMENSION, null, to);
        }
        return true;
    }

    @Override
    public void setSneaking(boolean sneak) {
        this.getHandle().setShiftKeyDown(sneak);
    }

    @Override
    public boolean isSneaking() {
        return this.getHandle().isShiftKeyDown();
    }

    @Override
    public boolean isSprinting() {
        return this.getHandle().isSprinting();
    }

    @Override
    public void setSprinting(boolean sprinting) {
        this.getHandle().setSprinting(sprinting);
    }

    @Override
    public void loadData() {
        this.server.getHandle().playerIo.load(this.getHandle());
    }

    @Override
    public void saveData() {
        this.server.getHandle().playerIo.save(this.getHandle());
    }

    @Deprecated
    @Override
    public void updateInventory() {
        this.getHandle().containerMenu.sendAllDataToRemote();
    }

    @Override
    public void setSleepingIgnored(boolean isSleeping) {
        this.getHandle().fauxSleeping = isSleeping;
        ((CraftWorld) this.getWorld()).getHandle().updateSleepingPlayerList();
    }

    @Override
    public boolean isSleepingIgnored() {
        return this.getHandle().fauxSleeping;
    }

    @Override
    public Location getBedSpawnLocation() {
        return this.getRespawnLocation();
    }

    @Override
    public Location getRespawnLocation() {
        ServerLevel world = this.getHandle().server.getLevel(this.getHandle().getRespawnDimension());
        BlockPos bed = this.getHandle().getRespawnPosition();

        if (world != null && bed != null) {
            Optional<ServerPlayer.RespawnPosAngle> spawnLoc = ServerPlayer.findRespawnAndUseSpawnBlock(world, bed, this.getHandle().getRespawnAngle(), this.getHandle().isRespawnForced(), true);
            if (spawnLoc.isPresent()) {
                ServerPlayer.RespawnPosAngle vec = spawnLoc.get();
                return CraftLocation.toBukkit(vec.position(), world.getWorld(), vec.yaw(), 0);
            }
        }
        return null;
    }

    @Override
    public void setBedSpawnLocation(Location location) {
        this.setBedSpawnLocation(location, false);
    }

    @Override
    public void setRespawnLocation(Location location) {
        this.setRespawnLocation(location, false);
    }

    @Override
    public void setBedSpawnLocation(Location location, boolean override) {
        this.setRespawnLocation(location, override);
    }

    @Override
    public void setRespawnLocation(Location location, boolean override) {
        if (location == null) {
            this.getHandle().setRespawnPosition(null, null, 0.0F, override, false, PlayerSpawnChangeEvent.Cause.PLUGIN);
        } else {
            this.getHandle().setRespawnPosition(((CraftWorld) location.getWorld()).getHandle().dimension(), CraftLocation.toBlockPosition(location), location.getYaw(), override, false, PlayerSpawnChangeEvent.Cause.PLUGIN);
        }
    }

    @Override
    public Collection<EnderPearl> getEnderPearls() {
        return this.getHandle().getEnderPearls().stream().map((e) -> (EnderPearl) e.getBukkitEntity()).collect(Collectors.toList());
    }

    @Override
    public Input getCurrentInput() {
        return new CraftInput(this.getHandle().getLastClientInput());
    }

    @Override
    public Location getBedLocation() {
        Preconditions.checkState(this.isSleeping(), "Not sleeping");

        BlockPos bed = this.getHandle().getRespawnPosition();
        return CraftLocation.toBukkit(bed, this.getWorld());
    }

    @Override
    public boolean hasDiscoveredRecipe(NamespacedKey recipe) {
        Preconditions.checkArgument(recipe != null, "recipe cannot be null");
        return this.getHandle().getRecipeBook().contains(CraftRecipe.toMinecraft(recipe));
    }

    @Override
    public Set<NamespacedKey> getDiscoveredRecipes() {
        ImmutableSet.Builder<NamespacedKey> bukkitRecipeKeys = ImmutableSet.builder();
        this.getHandle().getRecipeBook().known.forEach(key -> bukkitRecipeKeys.add(CraftNamespacedKey.fromMinecraft(key.location())));
        return bukkitRecipeKeys.build();
    }

    @Override
    public void incrementStatistic(Statistic statistic) {
        CraftStatistic.incrementStatistic(this.getHandle().getStats(), statistic, this.getHandle());
    }

    @Override
    public void decrementStatistic(Statistic statistic) {
        CraftStatistic.decrementStatistic(this.getHandle().getStats(), statistic, this.getHandle());
    }

    @Override
    public int getStatistic(Statistic statistic) {
        return CraftStatistic.getStatistic(this.getHandle().getStats(), statistic);
    }

    @Override
    public void incrementStatistic(Statistic statistic, int amount) {
        CraftStatistic.incrementStatistic(this.getHandle().getStats(), statistic, amount, this.getHandle());
    }

    @Override
    public void decrementStatistic(Statistic statistic, int amount) {
        CraftStatistic.decrementStatistic(this.getHandle().getStats(), statistic, amount, this.getHandle());
    }

    @Override
    public void setStatistic(Statistic statistic, int newValue) {
        CraftStatistic.setStatistic(this.getHandle().getStats(), statistic, newValue, this.getHandle());
    }

    @Override
    public void incrementStatistic(Statistic statistic, Material material) {
        CraftStatistic.incrementStatistic(this.getHandle().getStats(), statistic, material, this.getHandle());
    }

    @Override
    public void decrementStatistic(Statistic statistic, Material material) {
        CraftStatistic.decrementStatistic(this.getHandle().getStats(), statistic, material, this.getHandle());
    }

    @Override
    public int getStatistic(Statistic statistic, Material material) {
        return CraftStatistic.getStatistic(this.getHandle().getStats(), statistic, material);
    }

    @Override
    public void incrementStatistic(Statistic statistic, Material material, int amount) {
        CraftStatistic.incrementStatistic(this.getHandle().getStats(), statistic, material, amount, this.getHandle());
    }

    @Override
    public void decrementStatistic(Statistic statistic, Material material, int amount) {
        CraftStatistic.decrementStatistic(this.getHandle().getStats(), statistic, material, amount, this.getHandle());
    }

    @Override
    public void setStatistic(Statistic statistic, Material material, int newValue) {
        CraftStatistic.setStatistic(this.getHandle().getStats(), statistic, material, newValue, this.getHandle());
    }

    @Override
    public void incrementStatistic(Statistic statistic, EntityType entityType) {
        CraftStatistic.incrementStatistic(this.getHandle().getStats(), statistic, entityType, this.getHandle());
    }

    @Override
    public void decrementStatistic(Statistic statistic, EntityType entityType) {
        CraftStatistic.decrementStatistic(this.getHandle().getStats(), statistic, entityType, this.getHandle());
    }

    @Override
    public int getStatistic(Statistic statistic, EntityType entityType) {
        return CraftStatistic.getStatistic(this.getHandle().getStats(), statistic, entityType);
    }

    @Override
    public void incrementStatistic(Statistic statistic, EntityType entityType, int amount) {
        CraftStatistic.incrementStatistic(this.getHandle().getStats(), statistic, entityType, amount, this.getHandle());
    }

    @Override
    public void decrementStatistic(Statistic statistic, EntityType entityType, int amount) {
        CraftStatistic.decrementStatistic(this.getHandle().getStats(), statistic, entityType, amount, this.getHandle());
    }

    @Override
    public void setStatistic(Statistic statistic, EntityType entityType, int newValue) {
        CraftStatistic.setStatistic(this.getHandle().getStats(), statistic, entityType, newValue, this.getHandle());
    }

    @Override
    public void setPlayerTime(long time, boolean relative) {
        this.getHandle().timeOffset = time;
        this.getHandle().relativeTime = relative;
    }

    @Override
    public long getPlayerTimeOffset() {
        return this.getHandle().timeOffset;
    }

    @Override
    public long getPlayerTime() {
        return this.getHandle().getPlayerTime();
    }

    @Override
    public boolean isPlayerTimeRelative() {
        return this.getHandle().relativeTime;
    }

    @Override
    public void resetPlayerTime() {
        this.setPlayerTime(0, true);
    }

    @Override
    public void setPlayerWeather(WeatherType type) {
        this.getHandle().setPlayerWeather(type, true);
    }

    @Override
    public WeatherType getPlayerWeather() {
        return this.getHandle().getPlayerWeather();
    }

    @Override
    public int getExpCooldown() {
        return this.getHandle().takeXpDelay;
    }

    @Override
    public void setExpCooldown(int ticks) {
        this.getHandle().takeXpDelay = CraftEventFactory.callPlayerXpCooldownEvent(this.getHandle(), ticks, PlayerExpCooldownChangeEvent.ChangeReason.PLUGIN).getNewCooldown();
    }

    @Override
    public void resetPlayerWeather() {
        this.getHandle().resetPlayerWeather();
    }

    @Override
    public boolean isBanned() {
        return ((ProfileBanList) this.server.getBanList(BanList.Type.PROFILE)).isBanned(this.getPlayerProfile());
    }

    @Override
    public BanEntry<PlayerProfile> ban(String reason, Date expires, String source) {
        return this.ban(reason, expires, source, true);
    }

    @Override
    public BanEntry<PlayerProfile> ban(String reason, Instant expires, String source) {
        return this.ban(reason, expires != null ? Date.from(expires) : null, source);
    }

    @Override
    public BanEntry<PlayerProfile> ban(String reason, Duration duration, String source) {
        return this.ban(reason, duration != null ? Instant.now().plus(duration) : null, source);
    }

    @Override
    public BanEntry<PlayerProfile> ban(String reason, Date expires, String source, boolean kickPlayer) {
        BanEntry<PlayerProfile> banEntry = ((ProfileBanList) this.server.getBanList(BanList.Type.PROFILE)).addBan(this.getPlayerProfile(), reason, expires, source);
        if (kickPlayer) {
            this.kickPlayer(reason);
        }
        return banEntry;
    }

    @Override
    public BanEntry<PlayerProfile> ban(String reason, Instant instant, String source, boolean kickPlayer) {
        return this.ban(reason, instant != null ? Date.from(instant) : null, source, kickPlayer);
    }

    @Override
    public BanEntry<PlayerProfile> ban(String reason, Duration duration, String source, boolean kickPlayer) {
        return this.ban(reason, duration != null ? Instant.now().plus(duration) : null, source, kickPlayer);
    }

    @Override
    public BanEntry<InetAddress> banIp(String reason, Date expires, String source, boolean kickPlayer) {
        Preconditions.checkArgument(this.getAddress() != null, "The Address of this Player is null");
        BanEntry<InetAddress> banEntry = ((IpBanList) this.server.getBanList(BanList.Type.IP)).addBan(this.getAddress().getAddress(), reason, expires, source);
        if (kickPlayer) {
            this.kickPlayer(reason);
        }
        return banEntry;
    }

    @Override
    public BanEntry<InetAddress> banIp(String reason, Instant instant, String source, boolean kickPlayer) {
        return this.banIp(reason, instant != null ? Date.from(instant) : null, source, kickPlayer);
    }

    @Override
    public BanEntry<InetAddress> banIp(String reason, Duration duration, String source, boolean kickPlayer) {
        return this.banIp(reason, duration != null ? Instant.now().plus(duration) : null, source, kickPlayer);
    }

    @Override
    public boolean isWhitelisted() {
        return this.server.getHandle().getWhiteList().isWhiteListed(this.getProfile());
    }

    @Override
    public void setWhitelisted(boolean value) {
        if (value) {
            this.server.getHandle().getWhiteList().add(new UserWhiteListEntry(this.getProfile()));
        } else {
            this.server.getHandle().getWhiteList().remove(this.getProfile());
        }
    }

    @Override
    public void setGameMode(GameMode mode) {
        Preconditions.checkArgument(mode != null, "GameMode cannot be null");
        if (this.getHandle().connection == null) return;

        this.getHandle().setGameMode(GameType.byId(mode.getValue()));
    }

    @Override
    public GameMode getGameMode() {
        return GameMode.getByValue(this.getHandle().gameMode.getGameModeForPlayer().getId());
    }

    @Override
    public GameMode getPreviousGameMode() {
        GameType previousGameMode = this.getHandle().gameMode.getPreviousGameModeForPlayer();

        return (previousGameMode == null) ? null : GameMode.getByValue(previousGameMode.getId());
    }

    @Override
    public void giveExp(int exp) {
        this.getHandle().giveExperiencePoints(exp);
    }

    @Override
    public void giveExpLevels(int levels) {
        this.getHandle().giveExperienceLevels(levels);
    }

    @Override
    public float getExp() {
        return this.getHandle().experienceProgress;
    }

    @Override
    public void setExp(float exp) {
        Preconditions.checkArgument(exp >= 0.0 && exp <= 1.0, "Experience progress must be between 0.0 and 1.0 (%s)", exp);
        this.getHandle().experienceProgress = exp;
        this.getHandle().lastSentExp = -1;
    }

    @Override
    public int getLevel() {
        return this.getHandle().experienceLevel;
    }

    @Override
    public void setLevel(int level) {
        Preconditions.checkArgument(level >= 0, "Experience level must not be negative (%s)", level);
        this.getHandle().experienceLevel = level;
        this.getHandle().lastSentExp = -1;
    }

    @Override
    public int getTotalExperience() {
        return this.getHandle().totalExperience;
    }

    @Override
    public void setTotalExperience(int exp) {
        Preconditions.checkArgument(exp >= 0, "Total experience points must not be negative (%s)", exp);
        this.getHandle().totalExperience = exp;
    }

    @Override
    public void sendExperienceChange(float progress) {
        this.sendExperienceChange(progress, this.getLevel());
    }

    @Override
    public void sendExperienceChange(float progress, int level) {
        Preconditions.checkArgument(progress >= 0.0 && progress <= 1.0, "Experience progress must be between 0.0 and 1.0 (%s)", progress);
        Preconditions.checkArgument(level >= 0, "Experience level must not be negative (%s)", level);

        if (this.getHandle().connection == null) {
            return;
        }

        ClientboundSetExperiencePacket packet = new ClientboundSetExperiencePacket(progress, this.getTotalExperience(), level);
        this.getHandle().connection.send(packet);
    }

    @Nullable
    private static WeakReference<Plugin> getPluginWeakReference(@Nullable Plugin plugin) {
        return (plugin == null) ? null : CraftPlayer.pluginWeakReferences.computeIfAbsent(plugin, WeakReference::new);
    }

    @Override
    @Deprecated
    public void hidePlayer(Player player) {
        this.hideEntity0(null, player);
    }

    @Override
    public void hidePlayer(Plugin plugin, Player player) {
        this.hideEntity(plugin, player);
    }

    @Override
    public void hideEntity(Plugin plugin, org.bukkit.entity.Entity entity) {
        Preconditions.checkArgument(plugin != null, "Plugin cannot be null");
        Preconditions.checkArgument(plugin.isEnabled(), "Plugin (%s) cannot be disabled", plugin.getName());

        this.hideEntity0(plugin, entity);
    }

    private void hideEntity0(@Nullable Plugin plugin, org.bukkit.entity.Entity entity) {
        Preconditions.checkArgument(entity != null, "Entity hidden cannot be null");
        if (this.getHandle().connection == null) return;
        if (this.equals(entity)) return;

        boolean shouldHide;
        if (entity.isVisibleByDefault()) {
            shouldHide = this.addInvertedVisibility(plugin, entity);
        } else {
            shouldHide = this.removeInvertedVisibility(plugin, entity);
        }

        if (shouldHide) {
            this.untrackAndHideEntity(entity);
        }
    }

    private boolean addInvertedVisibility(@Nullable Plugin plugin, org.bukkit.entity.Entity entity) {
        Set<WeakReference<Plugin>> invertedPlugins = this.invertedVisibilityEntities.get(entity.getUniqueId());
        if (invertedPlugins != null) {
            // Some plugins are already inverting the entity. Just mark that this
            // plugin wants the entity inverted too and end.
            invertedPlugins.add(CraftPlayer.getPluginWeakReference(plugin));
            return false;
        }
        invertedPlugins = new HashSet<>();
        invertedPlugins.add(CraftPlayer.getPluginWeakReference(plugin));
        this.invertedVisibilityEntities.put(entity.getUniqueId(), invertedPlugins);

        return true;
    }

    private void untrackAndHideEntity(org.bukkit.entity.Entity entity) {
        // Remove this entity from the hidden player's EntityTrackerEntry
        ChunkMap tracker = ((ServerLevel) this.getHandle().level()).getChunkSource().chunkMap;
        Entity other = ((CraftEntity) entity).getHandle();
        ChunkMap.TrackedEntity entry = tracker.entityMap.get(other.getId());
        if (entry != null) {
            entry.removePlayer(this.getHandle());
        }

        // Remove the hidden entity from this player user list, if they're on it
        if (other instanceof ServerPlayer) {
            ServerPlayer otherPlayer = (ServerPlayer) other;
            if (otherPlayer.sentListPacket) {
                this.getHandle().connection.send(new ClientboundPlayerInfoRemovePacket(List.of(otherPlayer.getUUID())));
            }
        }

        this.server.getPluginManager().callEvent(new PlayerHideEntityEvent(this, entity));
    }

    void resetAndHideEntity(org.bukkit.entity.Entity entity) {
        // SPIGOT-7312: Can't show/hide self
        if (this.equals(entity)) {
            return;
        }

        if (this.invertedVisibilityEntities.remove(entity.getUniqueId()) == null) {
            this.untrackAndHideEntity(entity);
        }
    }

    @Override
    @Deprecated
    public void showPlayer(Player player) {
        this.showEntity0(null, player);
    }

    @Override
    public void showPlayer(Plugin plugin, Player player) {
        this.showEntity(plugin, player);
    }

    @Override
    public void showEntity(Plugin plugin, org.bukkit.entity.Entity entity) {
        Preconditions.checkArgument(plugin != null, "Plugin cannot be null");
        // Don't require that plugin be enabled. A plugin must be allowed to call
        // showPlayer during its onDisable() method.
        this.showEntity0(plugin, entity);
    }

    private void showEntity0(@Nullable Plugin plugin, org.bukkit.entity.Entity entity) {
        Preconditions.checkArgument(entity != null, "Entity show cannot be null");
        if (this.getHandle().connection == null) return;
        if (this.equals(entity)) return;

        boolean shouldShow;
        if (entity.isVisibleByDefault()) {
            shouldShow = this.removeInvertedVisibility(plugin, entity);
        } else {
            shouldShow = this.addInvertedVisibility(plugin, entity);
        }

        if (shouldShow) {
            this.trackAndShowEntity(entity);
        }
    }

    private boolean removeInvertedVisibility(@Nullable Plugin plugin, org.bukkit.entity.Entity entity) {
        Set<WeakReference<Plugin>> invertedPlugins = this.invertedVisibilityEntities.get(entity.getUniqueId());
        if (invertedPlugins == null) {
            return false; // Entity isn't inverted
        }
        invertedPlugins.remove(CraftPlayer.getPluginWeakReference(plugin));
        if (!invertedPlugins.isEmpty()) {
            return false; // Some other plugins still want the entity inverted
        }
        this.invertedVisibilityEntities.remove(entity.getUniqueId());

        return true;
    }

    private void trackAndShowEntity(org.bukkit.entity.Entity entity) {
        ChunkMap tracker = ((ServerLevel) this.getHandle().level()).getChunkSource().chunkMap;
        Entity other = ((CraftEntity) entity).getHandle();

        if (other instanceof ServerPlayer) {
            ServerPlayer otherPlayer = (ServerPlayer) other;
            this.getHandle().connection.send(ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(List.of(otherPlayer)));
        }

        ChunkMap.TrackedEntity entry = tracker.entityMap.get(other.getId());
        if (entry != null && !entry.seenBy.contains(this.getHandle().connection)) {
            entry.updatePlayer(this.getHandle());
        }

        this.server.getPluginManager().callEvent(new PlayerShowEntityEvent(this, entity));
    }

    void resetAndShowEntity(org.bukkit.entity.Entity entity) {
        // SPIGOT-7312: Can't show/hide self
        if (this.equals(entity)) {
            return;
        }

        if (this.invertedVisibilityEntities.remove(entity.getUniqueId()) == null) {
            this.trackAndShowEntity(entity);
        }
    }

    public void onEntityRemove(Entity entity) {
        this.invertedVisibilityEntities.remove(entity.getUUID());
    }

    @Override
    public boolean canSee(Player player) {
        return this.canSee((org.bukkit.entity.Entity) player);
    }

    @Override
    public boolean canSee(org.bukkit.entity.Entity entity) {
        return this.equals(entity) || entity.isVisibleByDefault() ^ this.invertedVisibilityEntities.containsKey(entity.getUniqueId()); // SPIGOT-7312: Can always see self
    }

    public boolean canSeePlayer(UUID uuid) {
        org.bukkit.entity.Entity entity = this.getServer().getPlayer(uuid);

        return (entity != null) ? this.canSee(entity) : false; // If we can't find it, we can't see it
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<String, Object>();

        result.put("name", this.getName());

        return result;
    }

    @Override
    public Player getPlayer() {
        return this;
    }

    @Override
    public ServerPlayer getHandle() {
        return (ServerPlayer) this.entity;
    }

    public void setHandle(final ServerPlayer entity) {
        super.setHandle(entity);
    }

    @Override
    public String toString() {
        return "CraftPlayer{" + "name=" + this.getName() + '}';
    }

    @Override
    public int hashCode() {
        if (this.hash == 0 || this.hash == 485) {
            this.hash = 97 * 5 + (this.getUniqueId() != null ? this.getUniqueId().hashCode() : 0);
        }
        return this.hash;
    }

    @Override
    public long getFirstPlayed() {
        return this.firstPlayed;
    }

    @Override
    public long getLastPlayed() {
        return this.lastPlayed;
    }

    @Override
    public boolean hasPlayedBefore() {
        return this.hasPlayedBefore;
    }

    public void setFirstPlayed(long firstPlayed) {
        this.firstPlayed = firstPlayed;
    }

    public void readExtraData(CompoundTag nbttagcompound) {
        this.hasPlayedBefore = true;
        if (nbttagcompound.contains("bukkit")) {
            CompoundTag data = nbttagcompound.getCompound("bukkit");

            if (data.contains("firstPlayed")) {
                this.firstPlayed = data.getLong("firstPlayed");
                this.lastPlayed = data.getLong("lastPlayed");
            }

            if (data.contains("newExp")) {
                ServerPlayer handle = this.getHandle();
                handle.newExp = data.getInt("newExp");
                handle.newTotalExp = data.getInt("newTotalExp");
                handle.newLevel = data.getInt("newLevel");
                handle.expToDrop = data.getInt("expToDrop");
                handle.keepLevel = data.getBoolean("keepLevel");
            }
        }
    }

    public void setExtraData(CompoundTag nbttagcompound) {
        if (!nbttagcompound.contains("bukkit")) {
            nbttagcompound.put("bukkit", new CompoundTag());
        }

        CompoundTag data = nbttagcompound.getCompound("bukkit");
        ServerPlayer handle = this.getHandle();
        data.putInt("newExp", handle.newExp);
        data.putInt("newTotalExp", handle.newTotalExp);
        data.putInt("newLevel", handle.newLevel);
        data.putInt("expToDrop", handle.expToDrop);
        data.putBoolean("keepLevel", handle.keepLevel);
        data.putLong("firstPlayed", this.getFirstPlayed());
        data.putLong("lastPlayed", System.currentTimeMillis());
        data.putString("lastKnownName", handle.getScoreboardName());
    }

    @Override
    public boolean beginConversation(Conversation conversation) {
        return this.conversationTracker.beginConversation(conversation);
    }

    @Override
    public void abandonConversation(Conversation conversation) {
        this.conversationTracker.abandonConversation(conversation, new ConversationAbandonedEvent(conversation, new ManuallyAbandonedConversationCanceller()));
    }

    @Override
    public void abandonConversation(Conversation conversation, ConversationAbandonedEvent details) {
        this.conversationTracker.abandonConversation(conversation, details);
    }

    @Override
    public void acceptConversationInput(String input) {
        this.conversationTracker.acceptConversationInput(input);
    }

    @Override
    public boolean isConversing() {
        return this.conversationTracker.isConversing();
    }

    @Override
    public void sendPluginMessage(Plugin source, String channel, byte[] message) {
        StandardMessenger.validatePluginMessage(this.server.getMessenger(), source, channel, message);
        if (this.getHandle().connection == null) return;

        if (this.channels.contains(channel)) {
            ResourceLocation id = ResourceLocation.parse(StandardMessenger.validateAndCorrectChannel(channel));
            this.sendCustomPayload(id, message);
        }
    }

    private void sendCustomPayload(ResourceLocation id, byte[] message) {
        ClientboundCustomPayloadPacket packet = new ClientboundCustomPayloadPacket(new DiscardedPayload(id, Unpooled.wrappedBuffer(message)));
        this.getHandle().connection.send(packet);
    }

    @Override
    public void setTexturePack(String url) {
        this.setResourcePack(url);
    }

    @Override
    public void setResourcePack(String url) {
        this.setResourcePack(url, null);
    }

    @Override
    public void setResourcePack(String url, byte[] hash) {
        this.setResourcePack(url, hash, false);
    }

    @Override
    public void setResourcePack(String url, byte[] hash, String prompt) {
        this.setResourcePack(url, hash, prompt, false);
    }

    @Override
    public void setResourcePack(String url, byte[] hash, boolean force) {
        this.setResourcePack(url, hash, null, force);
    }

    @Override
    public void setResourcePack(String url, byte[] hash, String prompt, boolean force) {
        Preconditions.checkArgument(url != null, "Resource pack URL cannot be null");

        this.setResourcePack(UUID.nameUUIDFromBytes(url.getBytes(StandardCharsets.UTF_8)), url, hash, prompt, force);
    }

    @Override
    public void setResourcePack(UUID id, String url, byte[] hash, String prompt, boolean force) {
        Preconditions.checkArgument(id != null, "Resource pack ID cannot be null");
        Preconditions.checkArgument(url != null, "Resource pack URL cannot be null");

        String hashStr = "";
        if (hash != null) {
            Preconditions.checkArgument(hash.length == 20, "Resource pack hash should be 20 bytes long but was %s", hash.length);
            hashStr = BaseEncoding.base16().lowerCase().encode(hash);
        }

        this.handlePushResourcePack(new ClientboundResourcePackPushPacket(id, url, hashStr, force, CraftChatMessage.fromStringOrOptional(prompt, true)), true);
    }

    @Override
    public void addResourcePack(UUID id, String url, byte[] hash, String prompt, boolean force) {
        Preconditions.checkArgument(url != null, "Resource pack URL cannot be null");

        String hashStr = "";
        if (hash != null) {
            Preconditions.checkArgument(hash.length == 20, "Resource pack hash should be 20 bytes long but was %s", hash.length);
            hashStr = BaseEncoding.base16().lowerCase().encode(hash);
        }

        this.handlePushResourcePack(new ClientboundResourcePackPushPacket(id, url, hashStr, force, CraftChatMessage.fromStringOrOptional(prompt, true)), false);
    }

    @Override
    public void removeResourcePack(UUID id) {
        Preconditions.checkArgument(id != null, "Resource pack id cannot be null");
        if (this.getHandle().connection == null) return;
        this.getHandle().connection.send(new ClientboundResourcePackPopPacket(Optional.of(id)));
    }

    @Override
    public void removeResourcePacks() {
        if (this.getHandle().connection == null) return;
        this.getHandle().connection.send(new ClientboundResourcePackPopPacket(Optional.empty()));
    }

    private void handlePushResourcePack(ClientboundResourcePackPushPacket resourcePackPushPacket, boolean resetBeforePush) {
        if (this.getHandle().connection == null) return;

        if (resetBeforePush) {
            this.removeResourcePacks();
        }
        this.getHandle().connection.send(resourcePackPushPacket);
    }

    public void addChannel(String channel) {
        Preconditions.checkState(this.channels.size() < 128, "Cannot register channel '%s'. Too many channels registered!", channel);
        channel = StandardMessenger.validateAndCorrectChannel(channel);
        if (this.channels.add(channel)) {
            this.server.getPluginManager().callEvent(new PlayerRegisterChannelEvent(this, channel));
        }
    }

    public void removeChannel(String channel) {
        channel = StandardMessenger.validateAndCorrectChannel(channel);
        if (this.channels.remove(channel)) {
            this.server.getPluginManager().callEvent(new PlayerUnregisterChannelEvent(this, channel));
        }
    }

    @Override
    public Set<String> getListeningPluginChannels() {
        return ImmutableSet.copyOf(this.channels);
    }

    public void sendSupportedChannels() {
        if (this.getHandle().connection == null) return;
        Set<String> listening = this.server.getMessenger().getIncomingChannels();

        if (!listening.isEmpty()) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            for (String channel : listening) {
                try {
                    stream.write(channel.getBytes("UTF8"));
                    stream.write((byte) 0);
                } catch (IOException ex) {
                    Logger.getLogger(CraftPlayer.class.getName()).log(Level.SEVERE, "Could not send Plugin Channel REGISTER to " + this.getName(), ex);
                }
            }

            this.sendCustomPayload(ResourceLocation.withDefaultNamespace("register"), stream.toByteArray());
        }
    }

    @Override
    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        this.server.getPlayerMetadata().setMetadata(this, metadataKey, newMetadataValue);
    }

    @Override
    public List<MetadataValue> getMetadata(String metadataKey) {
        return this.server.getPlayerMetadata().getMetadata(this, metadataKey);
    }

    @Override
    public boolean hasMetadata(String metadataKey) {
        return this.server.getPlayerMetadata().hasMetadata(this, metadataKey);
    }

    @Override
    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
        this.server.getPlayerMetadata().removeMetadata(this, metadataKey, owningPlugin);
    }

    @Override
    public boolean setWindowProperty(Property prop, int value) {
        AbstractContainerMenu container = this.getHandle().containerMenu;
        if (container.getBukkitView().getType() != prop.getType()) {
            return false;
        }
        container.setData(prop.getId(), value);
        return true;
    }

    public void disconnect(String reason) {
        this.conversationTracker.abandonAllConversations();
        this.perm.clearPermissions();
    }

    @Override
    public boolean isFlying() {
        return this.getHandle().getAbilities().flying;
    }

    @Override
    public void setFlying(boolean value) {
        if (!this.getAllowFlight()) {
            Preconditions.checkArgument(!value, "Player is not allowed to fly (check #getAllowFlight())");
        }

        this.getHandle().getAbilities().flying = value;
        this.getHandle().onUpdateAbilities();
    }

    @Override
    public boolean getAllowFlight() {
        return this.getHandle().getAbilities().mayfly;
    }

    @Override
    public void setAllowFlight(boolean value) {
        if (this.isFlying() && !value) {
            this.getHandle().getAbilities().flying = false;
        }

        this.getHandle().getAbilities().mayfly = value;
        this.getHandle().onUpdateAbilities();
    }

    @Override
    public void setFlySpeed(float value) {
        this.validateSpeed(value);
        ServerPlayer player = this.getHandle();
        player.getAbilities().flyingSpeed = value / 2f;
        player.onUpdateAbilities();

    }

    @Override
    public void setWalkSpeed(float value) {
        this.validateSpeed(value);
        ServerPlayer player = this.getHandle();
        player.getAbilities().walkingSpeed = value / 2f;
        player.onUpdateAbilities();
        this.getHandle().getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(player.getAbilities().walkingSpeed); // SPIGOT-5833: combination of the two in 1.16+
    }

    @Override
    public float getFlySpeed() {
        return (float) this.getHandle().getAbilities().flyingSpeed * 2f;
    }

    @Override
    public float getWalkSpeed() {
        return this.getHandle().getAbilities().walkingSpeed * 2f;
    }

    private void validateSpeed(float value) {
        Preconditions.checkArgument(value <= 1f && value >= -1f, "Speed value (%s) need to be between -1f and 1f", value);
    }

    @Override
    public void setMaxHealth(double amount) {
        super.setMaxHealth(amount);
        this.health = Math.min(this.health, amount);
        this.getHandle().resetSentInfo();
    }

    @Override
    public void resetMaxHealth() {
        super.resetMaxHealth();
        this.getHandle().resetSentInfo();
    }

    @Override
    public CraftScoreboard getScoreboard() {
        return this.server.getScoreboardManager().getPlayerBoard(this);
    }

    @Override
    public void setScoreboard(Scoreboard scoreboard) {
        Preconditions.checkArgument(scoreboard != null, "Scoreboard cannot be null");
        Preconditions.checkState(this.getHandle().connection != null, "Cannot set scoreboard yet (invalid player connection)");

        this.server.getScoreboardManager().setPlayerBoard(this, scoreboard);
    }

    @Override
    public void setHealthScale(double value) {
        Preconditions.checkArgument(value > 0F, "Health value (%s) must be greater than 0", value);
        this.healthScale = value;
        this.scaledHealth = true;
        this.updateScaledHealth();
    }

    @Override
    public double getHealthScale() {
        return this.healthScale;
    }

    @Override
    public void setHealthScaled(boolean scale) {
        if (this.scaledHealth != (this.scaledHealth = scale)) {
            this.updateScaledHealth();
        }
    }

    @Override
    public boolean isHealthScaled() {
        return this.scaledHealth;
    }

    public float getScaledHealth() {
        return (float) (this.isHealthScaled() ? this.getHealth() * this.getHealthScale() / this.getMaxHealth() : this.getHealth());
    }

    @Override
    public double getHealth() {
        return this.health;
    }

    public void setRealHealth(double health) {
        this.health = health;
    }

    public void updateScaledHealth() {
        this.updateScaledHealth(true);
    }

    public void updateScaledHealth(boolean sendHealth) {
        AttributeMap attributemapserver = this.getHandle().getAttributes();
        Collection<AttributeInstance> set = attributemapserver.getSyncableAttributes();

        this.injectScaledMaxHealth(set, true);

        // SPIGOT-3813: Attributes before health
        if (this.getHandle().connection != null) {
            this.getHandle().connection.send(new ClientboundUpdateAttributesPacket(this.getHandle().getId(), set));
            if (sendHealth) {
                this.sendHealthUpdate();
            }
        }
        this.getHandle().getEntityData().set(net.minecraft.world.entity.LivingEntity.DATA_HEALTH_ID, (float) this.getScaledHealth());

        this.getHandle().maxHealthCache = this.getMaxHealth();
    }

    @Override
    public void sendHealthUpdate(double health, int foodLevel, float saturation) {
        this.getHandle().connection.send(new ClientboundSetHealthPacket((float) health, foodLevel, saturation));
    }

    @Override
    public void sendHealthUpdate() {
        FoodData foodData = this.getHandle().getFoodData();
        this.sendHealthUpdate(this.getScaledHealth(), foodData.getFoodLevel(), foodData.getSaturationLevel());
    }

    public void injectScaledMaxHealth(Collection<AttributeInstance> collection, boolean force) {
        if (!this.scaledHealth && !force) {
            return;
        }
        for (AttributeInstance genericInstance : collection) {
            if (genericInstance.getAttribute() == Attributes.MAX_HEALTH) {
                collection.remove(genericInstance);
                break;
            }
        }
        AttributeInstance dummy = new AttributeInstance(Attributes.MAX_HEALTH, (attribute) -> { });
        // Spigot start
        double healthMod = this.scaledHealth ? this.healthScale : this.getMaxHealth();
        if ( healthMod >= Float.MAX_VALUE || healthMod <= 0 )
        {
            healthMod = 20; // Reset health
            this.getServer().getLogger().warning( this.getName() + " tried to crash the server with a large health attribute" );
        }
        dummy.setBaseValue(healthMod);
        // Spigot end
        collection.add(dummy);
    }

    @Override
    public org.bukkit.entity.Entity getSpectatorTarget() {
        Entity followed = this.getHandle().getCamera();
        return followed == this.getHandle() ? null : followed.getBukkitEntity();
    }

    @Override
    public void setSpectatorTarget(org.bukkit.entity.Entity entity) {
        Preconditions.checkArgument(this.getGameMode() == GameMode.SPECTATOR, "Player must be in spectator mode");
        this.getHandle().setCamera((entity == null) ? null : ((CraftEntity) entity).getHandle());
    }

    @Override
    public void sendTitle(String title, String subtitle) {
        this.sendTitle(title, subtitle, 10, 70, 20);
    }

    @Override
    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        ClientboundSetTitlesAnimationPacket times = new ClientboundSetTitlesAnimationPacket(fadeIn, stay, fadeOut);
        this.getHandle().connection.send(times);

        if (title != null) {
            ClientboundSetTitleTextPacket packetTitle = new ClientboundSetTitleTextPacket(CraftChatMessage.fromString(title)[0]);
            this.getHandle().connection.send(packetTitle);
        }

        if (subtitle != null) {
            ClientboundSetSubtitleTextPacket packetSubtitle = new ClientboundSetSubtitleTextPacket(CraftChatMessage.fromString(subtitle)[0]);
            this.getHandle().connection.send(packetSubtitle);
        }
    }

    @Override
    public void resetTitle() {
        ClientboundClearTitlesPacket packetReset = new ClientboundClearTitlesPacket(true);
        this.getHandle().connection.send(packetReset);
    }

    @Override
    public void spawnParticle(Particle particle, Location location, int count) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count);
    }

    @Override
    public void spawnParticle(Particle particle, double x, double y, double z, int count) {
        this.spawnParticle(particle, x, y, z, count, null);
    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, T data) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, data);
    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, T data) {
        this.spawnParticle(particle, x, y, z, count, 0, 0, 0, data);
    }

    @Override
    public void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ);
    }

    @Override
    public void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ) {
        this.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, null);
    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, T data) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, data);
    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, T data) {
        this.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, 1, data);
    }

    @Override
    public void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, extra);
    }

    @Override
    public void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra) {
        this.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, extra, null);
    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, extra, data);
    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {
        this.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, extra, data, false);
    }

    @Override
    public <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, T data, boolean force) {
        this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, extra, data, force);
    }

    @Override
    public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, T data, boolean force) {
        ClientboundLevelParticlesPacket packetplayoutworldparticles = new ClientboundLevelParticlesPacket(CraftParticle.createParticleParam(particle, data), false, force, (float) x, (float) y, (float) z, (float) offsetX, (float) offsetY, (float) offsetZ, (float) extra, count);
        this.getHandle().connection.send(packetplayoutworldparticles);
    }

    @Override
    public org.bukkit.advancement.AdvancementProgress getAdvancementProgress(org.bukkit.advancement.Advancement advancement) {
        Preconditions.checkArgument(advancement != null, "advancement");

        CraftAdvancement craft = (CraftAdvancement) advancement;
        PlayerAdvancements data = this.getHandle().getAdvancements();
        AdvancementProgress progress = data.getOrStartProgress(craft.getHandle());

        return new CraftAdvancementProgress(craft, data, progress);
    }

    @Override
    public int getClientViewDistance() {
        return (this.getHandle().requestedViewDistance() == 0) ? Bukkit.getViewDistance() : this.getHandle().requestedViewDistance();
    }

    @Override
    public int getPing() {
        return this.getHandle().connection.latency();
    }

    @Override
    public String getLocale() {
        return this.getHandle().language;
    }

    @Override
    public void updateCommands() {
        if (this.getHandle().connection == null) return;

        this.getHandle().server.getCommands().sendCommands(this.getHandle());
    }

    @Override
    public void openBook(ItemStack book) {
        Preconditions.checkArgument(book != null, "ItemStack cannot be null");
        Preconditions.checkArgument(book.getType() == Material.WRITTEN_BOOK, "ItemStack Material (%s) must be Material.WRITTEN_BOOK", book.getType());

        ItemStack hand = this.getInventory().getItemInMainHand();
        this.getInventory().setItemInMainHand(book);
        this.getHandle().openItemGui(org.bukkit.craftbukkit.inventory.CraftItemStack.asNMSCopy(book), net.minecraft.world.InteractionHand.MAIN_HAND);
        this.getInventory().setItemInMainHand(hand);
    }

    @Override
    public void openSign(Sign sign) {
        this.openSign(sign, Side.FRONT);
    }

    @Override
    public void openSign(@NotNull Sign sign, @NotNull Side side) {
        CraftSign.openSign(sign, this, side);
    }

    @Override
    public void showDemoScreen() {
        if (this.getHandle().connection == null) return;

        this.getHandle().connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.DEMO_EVENT, ClientboundGameEventPacket.DEMO_PARAM_INTRO));
    }

    @Override
    public boolean isAllowingServerListings() {
        return this.getHandle().allowsListing();
    }

    // Spigot start
    private final Player.Spigot spigot = new Player.Spigot()
    {

        @Override
        public InetSocketAddress getRawAddress()
        {
            return (InetSocketAddress) CraftPlayer.this.getHandle().connection.getRawAddress();
        }

        @Override
        public void respawn()
        {
            if ( CraftPlayer.this.getHealth() <= 0 && CraftPlayer.this.isOnline() )
            {
                CraftPlayer.this.server.getServer().getPlayerList().respawn( CraftPlayer.this.getHandle(), false, Entity.RemovalReason.KILLED, org.bukkit.event.player.PlayerRespawnEvent.RespawnReason.PLUGIN );
            }
        }

        @Override
        public Set<Player> getHiddenPlayers()
        {
            Set<Player> ret = new HashSet<>();
            for ( Player p : CraftPlayer.this.getServer().getOnlinePlayers() )
            {
                if ( !CraftPlayer.this.canSee(p) )
                {
                    ret.add( p );
                }
            }

            return java.util.Collections.unmodifiableSet( ret );
        }

        @Override
        public void sendMessage(BaseComponent component) {
          this.sendMessage( new BaseComponent[] { component } );
        }

        @Override
        public void sendMessage(BaseComponent... components) {
           this.sendMessage(net.md_5.bungee.api.ChatMessageType.SYSTEM, components);
        }

        @Override
        public void sendMessage(UUID sender, BaseComponent component) {
            this.sendMessage(net.md_5.bungee.api.ChatMessageType.CHAT, sender, component);
        }

        @Override
        public void sendMessage(UUID sender, BaseComponent... components) {
            this.sendMessage(net.md_5.bungee.api.ChatMessageType.CHAT, sender, components);
        }

        @Override
        public void sendMessage(net.md_5.bungee.api.ChatMessageType position, BaseComponent component) {
            this.sendMessage( position, new BaseComponent[] { component } );
        }

        @Override
        public void sendMessage(net.md_5.bungee.api.ChatMessageType position, BaseComponent... components) {
            this.sendMessage(position, null, components);
        }

        @Override
        public void sendMessage(net.md_5.bungee.api.ChatMessageType position, UUID sender, BaseComponent component) {
            this.sendMessage( position, sender, new BaseComponent[] { component } );
        }

        @Override
        public void sendMessage(net.md_5.bungee.api.ChatMessageType position, UUID sender, BaseComponent... components) {
            if ( CraftPlayer.this.getHandle().connection == null ) return;

            CraftPlayer.this.getHandle().connection.send(new net.minecraft.network.protocol.game.ClientboundSystemChatPacket(components, position == net.md_5.bungee.api.ChatMessageType.ACTION_BAR));
        }
    };

    public Player.Spigot spigot()
    {
        return this.spigot;
    }
    // Spigot end
}
