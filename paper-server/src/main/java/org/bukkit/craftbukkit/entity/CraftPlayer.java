package org.bukkit.craftbukkit.entity;

import com.destroystokyo.paper.event.player.PlayerSetSpawnEvent;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.BaseEncoding;
import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Pair;
import io.papermc.paper.FeatureHooks;
import io.papermc.paper.configuration.GlobalConfiguration;
import io.papermc.paper.entity.LookAnchor;
import io.papermc.paper.entity.PaperPlayerGiveResult;
import io.papermc.paper.entity.PlayerGiveResult;
import io.papermc.paper.math.Position;
import io.papermc.paper.util.MCUtil;
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
import java.util.Iterator;
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
import net.kyori.adventure.util.TriState;
import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.commands.Commands;
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
import net.minecraft.network.protocol.game.ClientboundOpenSignEditorPacket;
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
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
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
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Effect;
import org.bukkit.EntityEffect;
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
import org.bukkit.craftbukkit.scoreboard.CraftScoreboard;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerExpCooldownChangeEvent;
import org.bukkit.event.player.PlayerHideEntityEvent;
import org.bukkit.event.player.PlayerRegisterChannelEvent;
import org.bukkit.event.player.PlayerShowEntityEvent;
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
import org.bukkit.scoreboard.Scoreboard;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

@DelegateDeserialization(CraftOfflinePlayer.class)
public class CraftPlayer extends CraftHumanEntity implements Player {

    private long firstPlayed = 0;
    private long lastPlayed = 0;
    private boolean hasPlayedBefore = false;
    private final ConversationTracker conversationTracker = new ConversationTracker();
    private final Set<String> channels = new HashSet<String>();
    private final Map<UUID, Set<WeakReference<Plugin>>> invertedVisibilityEntities = new HashMap<>();
    private final Set<UUID> unlistedEntities = new HashSet<>(); // Paper - Add Listing API for Player
    private static final WeakHashMap<Plugin, WeakReference<Plugin>> pluginWeakReferences = new WeakHashMap<>();
    private int hash = 0;
    private double health = 20;
    private boolean scaledHealth = false;
    private double healthScale = 20;
    private CraftWorldBorder clientWorldBorder = null;
    private BorderChangeListener clientWorldBorderListener = this.createWorldBorderListener();
    public org.bukkit.event.player.PlayerResourcePackStatusEvent.Status resourcePackStatus; // Paper - more resource pack API
    private static final boolean DISABLE_CHANNEL_LIMIT = System.getProperty("paper.disableChannelLimit") != null; // Paper - add a flag to disable the channel limit
    private long lastSaveTime; // Paper - getLastPlayed replacement API

    public CraftPlayer(CraftServer server, ServerPlayer entity) {
        super(server, entity);

        this.firstPlayed = System.currentTimeMillis();
    }

    @Override
    public ServerPlayer getHandle() {
        return (ServerPlayer) this.entity;
    }

    @Override
    public boolean equals(Object obj) {
        // Long-term, this should just use the super equals... for now, check the UUID
        if (obj == this) return true;
        if (!(obj instanceof OfflinePlayer other)) return false;
        return this.getUniqueId().equals(other.getUniqueId());
    }

    @Override
    public int hashCode() {
        if (this.hash == 0 || this.hash == 485) {
            this.hash = 97 * 5 + this.getUniqueId().hashCode();
        }
        return this.hash;
    }

    public GameProfile getProfile() {
        return this.getHandle().getGameProfile();
    }

    @Override
    public void remove() {
        if (this.getHandle().getClass().equals(ServerPlayer.class)) { // special case for NMS plugins inheriting
        // Will lead to an inconsistent player state if we remove the player as any other entity.
        throw new UnsupportedOperationException(String.format("Cannot remove player %s, use Player#kickPlayer(String) instead.", this.getName()));
        } else {
            super.remove();
        }
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

    // Paper start
    @Override
    public boolean isConnected() {
        return !this.getHandle().hasDisconnected();
    }
    // Paper end

    @Override
    public InetSocketAddress getAddress() {
        if (this.getHandle().connection == null) return null;

        SocketAddress addr = this.getHandle().connection.getRemoteAddress();
        if (addr instanceof InetSocketAddress) {
            return (InetSocketAddress) addr;
        } else {
            return null;
        }
    }

    // Paper start - Add API to get player's proxy address
    @Override
    public @Nullable InetSocketAddress getHAProxyAddress() {
        if (this.getHandle().connection == null) return null;

        return this.getHandle().connection.connection.haProxyAddress instanceof final InetSocketAddress inetSocketAddress ? inetSocketAddress : null;
    }
    // Paper end - Add API to get player's proxy address

    public interface TransferCookieConnection {

        boolean isTransferred();

        ConnectionProtocol getProtocol();

        void sendPacket(Packet<?> packet);

        void kickPlayer(Component reason, org.bukkit.event.player.PlayerKickEvent.Cause cause); // Paper - kick event causes
    }

    public record CookieFuture(ResourceLocation key, CompletableFuture<byte @Nullable []> future) {

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
    public CompletableFuture<byte @Nullable []> retrieveCookie(final NamespacedKey key) {
        Preconditions.checkArgument(key != null, "Cookie key cannot be null");

        CompletableFuture<byte @Nullable []> future = new CompletableFuture<>();
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

    // Paper start - Implement NetworkClient
    @Override
    public int getProtocolVersion() {
        if (getHandle().connection == null) return -1;
        return getHandle().connection.connection.protocolVersion;
    }

    @Override
    public InetSocketAddress getVirtualHost() {
        if (getHandle().connection == null) return null;
        return getHandle().connection.connection.virtualHost;
    }
    // Paper end

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

    // Paper start
    @Override
    @Deprecated
    public void sendActionBar(BaseComponent[] message) {
        if (getHandle().connection == null || message == null) return;
        net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket packet = new net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket(org.bukkit.craftbukkit.util.CraftChatMessage.bungeeToVanilla(message));
        getHandle().connection.send(packet);
    }

    @Override
    @Deprecated
    public void sendActionBar(String message) {
        if (getHandle().connection == null || message == null || message.isEmpty()) return;
        getHandle().connection.send(new net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket(CraftChatMessage.fromStringOrNull(message)));
    }

    @Override
    @Deprecated
    public void sendActionBar(char alternateChar, String message) {
        if (message == null || message.isEmpty()) return;
        sendActionBar(org.bukkit.ChatColor.translateAlternateColorCodes(alternateChar, message));
    }

    @Override
    public void setPlayerListHeaderFooter(BaseComponent @Nullable [] header, BaseComponent @Nullable [] footer) {
         if (header != null) {
             String headerJson = CraftChatMessage.bungeeToJson(header);
             playerListHeader = net.kyori.adventure.text.serializer.gson.GsonComponentSerializer.gson().deserialize(headerJson);
         } else {
             playerListHeader = null;
         }

        if (footer != null) {
             String footerJson =  CraftChatMessage.bungeeToJson(footer);
             playerListFooter = net.kyori.adventure.text.serializer.gson.GsonComponentSerializer.gson().deserialize(footerJson);
        } else {
             playerListFooter = null;
         }

         updatePlayerListHeaderFooter();
    }

    @Override
    public void setPlayerListHeaderFooter(@Nullable BaseComponent header, @Nullable BaseComponent footer) {
        this.setPlayerListHeaderFooter(header == null ? null : new BaseComponent[]{header},
                footer == null ? null : new BaseComponent[]{footer});
    }

    @Override
    public void setTitleTimes(int fadeInTicks, int stayTicks, int fadeOutTicks) {
        getHandle().connection.send(new ClientboundSetTitlesAnimationPacket(fadeInTicks, stayTicks, fadeOutTicks));
    }

    @Override
    public void setSubtitle(BaseComponent[] subtitle) {
        final ClientboundSetSubtitleTextPacket packet = new ClientboundSetSubtitleTextPacket(org.bukkit.craftbukkit.util.CraftChatMessage.bungeeToVanilla(subtitle));
        getHandle().connection.send(packet);
    }

    @Override
    public void setSubtitle(BaseComponent subtitle) {
        setSubtitle(new BaseComponent[]{subtitle});
    }

    @Override
    public void showTitle(BaseComponent[] title) {
        final ClientboundSetTitleTextPacket packet = new ClientboundSetTitleTextPacket(org.bukkit.craftbukkit.util.CraftChatMessage.bungeeToVanilla(title));
        getHandle().connection.send(packet);
    }

    @Override
    public void showTitle(BaseComponent title) {
        showTitle(new BaseComponent[]{title});
    }

    @Override
    public void showTitle(BaseComponent[] title, BaseComponent[] subtitle, int fadeInTicks, int stayTicks, int fadeOutTicks) {
        setTitleTimes(fadeInTicks, stayTicks, fadeOutTicks);
        setSubtitle(subtitle);
        showTitle(title);
    }

    @Override
    public void showTitle(BaseComponent title, BaseComponent subtitle, int fadeInTicks, int stayTicks, int fadeOutTicks) {
        setTitleTimes(fadeInTicks, stayTicks, fadeOutTicks);
        setSubtitle(subtitle);
        showTitle(title);
    }

    @Override
    public void sendTitle(com.destroystokyo.paper.Title title) {
        Preconditions.checkNotNull(title, "Title is null");
        setTitleTimes(title.getFadeIn(), title.getStay(), title.getFadeOut());
        setSubtitle(title.getSubtitle() == null ? new BaseComponent[0] : title.getSubtitle());
        showTitle(title.getTitle());
    }

    @Override
    public void updateTitle(com.destroystokyo.paper.Title title) {
        Preconditions.checkNotNull(title, "Title is null");
        setTitleTimes(title.getFadeIn(), title.getStay(), title.getFadeOut());
        if (title.getSubtitle() != null) {
            setSubtitle(title.getSubtitle());
        }
        showTitle(title.getTitle());
    }

    @Override
    public void hideTitle() {
        getHandle().connection.send(new ClientboundClearTitlesPacket(false));
    }
    // Paper end

    @Override
    public String getDisplayName() {
        if (true) return io.papermc.paper.adventure.DisplayNames.getLegacy(this); // Paper
        return this.getHandle().displayName;
    }

    @Override
    public void setDisplayName(final String name) {
        this.getHandle().adventure$displayName = name != null ? net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().deserialize(name) : net.kyori.adventure.text.Component.text(this.getName()); // Paper
        this.getHandle().displayName = name == null ? this.getName() : name;
    }

    // Paper start
    @Override
    public void playerListName(net.kyori.adventure.text.Component name) {
        getHandle().listName = name == null ? null : io.papermc.paper.adventure.PaperAdventure.asVanilla(name);
        if (getHandle().connection == null) return; // Updates are possible before the player has fully joined
        for (ServerPlayer player : server.getHandle().players) {
            if (player.getBukkitEntity().canSee(this)) {
                player.connection.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME, getHandle()));
            }
        }
    }
    @Override
    public net.kyori.adventure.text.Component playerListName() {
        return getHandle().listName == null ? net.kyori.adventure.text.Component.text(getName()) : io.papermc.paper.adventure.PaperAdventure.asAdventure(getHandle().listName);
    }
    @Override
    public net.kyori.adventure.text.Component playerListHeader() {
        return playerListHeader;
    }
    @Override
    public net.kyori.adventure.text.Component playerListFooter() {
        return playerListFooter;
    }
    // Paper end
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
        if (this.getHandle().connection == null) return; // Paper - Updates are possible before the player has fully joined
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
        // Paper start - Send update packet
        if (getHandle().connection == null) return; // Updates are possible before the player has fully joined
        for (ServerPlayer player : server.getHandle().players) {
            if (player.getBukkitEntity().canSee(this)) {
                player.connection.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LIST_ORDER, getHandle()));
            }
        }
        // Paper end - Send update packet
    }

    private net.kyori.adventure.text.Component playerListHeader; // Paper - Adventure
    private net.kyori.adventure.text.Component playerListFooter; // Paper - Adventure

    @Override
    public String getPlayerListHeader() {
        return (this.playerListHeader == null) ? null : net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().serialize(this.playerListHeader);
    }

    @Override
    public String getPlayerListFooter() {
        return (this.playerListFooter == null) ? null : net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().serialize(this.playerListFooter); // Paper - Adventure
    }

    @Override
    public void setPlayerListHeader(String header) {
        this.playerListHeader = header == null ? null : net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().deserialize(header); // Paper - Adventure
        this.updatePlayerListHeaderFooter();
    }

    @Override
    public void setPlayerListFooter(String footer) {
        this.playerListFooter = footer == null ? null : net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().deserialize(footer); // Paper - Adventure
        this.updatePlayerListHeaderFooter();
    }

    @Override
    public void setPlayerListHeaderFooter(String header, String footer) {
        this.playerListHeader = header == null ? null : net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().deserialize(header); // Paper - Adventure
        this.playerListFooter = footer == null ? null : net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().deserialize(footer); // Paper - Adventure
        this.updatePlayerListHeaderFooter();
    }

    private void updatePlayerListHeaderFooter() {
        if (this.getHandle().connection == null) return;

        ClientboundTabListPacket packet = new ClientboundTabListPacket(io.papermc.paper.adventure.PaperAdventure.asVanillaNullToEmpty(this.playerListHeader), io.papermc.paper.adventure.PaperAdventure.asVanillaNullToEmpty(this.playerListFooter)); // Paper - adventure
        this.getHandle().connection.send(packet);
    }

    @Override
    public void kickPlayer(String message) {
        org.spigotmc.AsyncCatcher.catchOp("player kick"); // Spigot
        this.getHandle().transferCookieConnection.kickPlayer(CraftChatMessage.fromStringOrEmpty(message, true), org.bukkit.event.player.PlayerKickEvent.Cause.PLUGIN); // Paper - kick event cause
    }

    private static final net.kyori.adventure.text.Component DEFAULT_KICK_COMPONENT = net.kyori.adventure.text.Component.translatable("multiplayer.disconnect.kicked");

    @Override
    public void kick() {
        this.kick(DEFAULT_KICK_COMPONENT);
    }

    @Override
    public void kick(net.kyori.adventure.text.Component message, org.bukkit.event.player.PlayerKickEvent.Cause cause) {
        org.spigotmc.AsyncCatcher.catchOp("player kick");
        final ServerGamePacketListenerImpl connection = this.getHandle().connection;
        if (connection != null) {
            connection.disconnect(message == null ? net.kyori.adventure.text.Component.empty() : message, cause);
        }
    }

    @Override
    public <T> T getClientOption(com.destroystokyo.paper.ClientOption<T> type) {
        if (com.destroystokyo.paper.ClientOption.SKIN_PARTS == type) {
            return type.getType().cast(new com.destroystokyo.paper.PaperSkinParts(this.getHandle().getEntityData().get(net.minecraft.world.entity.player.Player.DATA_PLAYER_MODE_CUSTOMISATION)));
        } else if (com.destroystokyo.paper.ClientOption.CHAT_COLORS_ENABLED == type) {
            return type.getType().cast(this.getHandle().canChatInColor());
        } else if (com.destroystokyo.paper.ClientOption.CHAT_VISIBILITY == type) {
            return type.getType().cast(com.destroystokyo.paper.ClientOption.ChatVisibility.valueOf(this.getHandle().getChatVisibility().name()));
        } else if (com.destroystokyo.paper.ClientOption.LOCALE == type) {
            return type.getType().cast(this.getLocale());
        } else if (com.destroystokyo.paper.ClientOption.MAIN_HAND == type) {
            return type.getType().cast(this.getMainHand());
        } else if (com.destroystokyo.paper.ClientOption.VIEW_DISTANCE == type) {
            return type.getType().cast(this.getClientViewDistance());
        } else if (com.destroystokyo.paper.ClientOption.TEXT_FILTERING_ENABLED == type) {
            return type.getType().cast(this.getHandle().isTextFilteringEnabled());
        } else if (com.destroystokyo.paper.ClientOption.ALLOW_SERVER_LISTINGS == type) {
            return type.getType().cast(this.getHandle().allowsListing());
        } else if (com.destroystokyo.paper.ClientOption.PARTICLE_VISIBILITY == type) {
            return type.getType().cast(com.destroystokyo.paper.ClientOption.ParticleVisibility.valueOf(this.getHandle().particleStatus.name()));
        }
        throw new RuntimeException("Unknown settings type");
    }

    @Override
    public void sendOpLevel(byte level) {
        Preconditions.checkArgument(level >= Commands.LEVEL_ALL && level <= Commands.LEVEL_OWNERS, "Level must be within [%s, %s]", Commands.LEVEL_ALL, Commands.LEVEL_OWNERS);

        this.getHandle().getServer().getPlayerList().sendPlayerPermissionLevel(this.getHandle(), level, false);
    }

    @Override
    public void addAdditionalChatCompletions(@NonNull Collection<String> completions) {
        this.getHandle().connection.send(new net.minecraft.network.protocol.game.ClientboundCustomChatCompletionsPacket(
            net.minecraft.network.protocol.game.ClientboundCustomChatCompletionsPacket.Action.ADD,
            new ArrayList<>(completions)
        ));
    }

    @Override
    public void removeAdditionalChatCompletions(@NonNull Collection<String> completions) {
        this.getHandle().connection.send(new net.minecraft.network.protocol.game.ClientboundCustomChatCompletionsPacket(
            net.minecraft.network.protocol.game.ClientboundCustomChatCompletionsPacket.Action.REMOVE,
            new ArrayList<>(completions)
        ));
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

        // Paper start - Improve chat handling
        if (ServerGamePacketListenerImpl.isChatMessageIllegal(msg)) {
            this.getHandle().connection.disconnect(Component.translatable("multiplayer.disconnect.illegal_characters"), org.bukkit.event.player.PlayerKickEvent.Cause.ILLEGAL_CHARACTERS); // Paper - kick event causes
        } else {
            if (msg.startsWith("/")) {
                this.getHandle().connection.handleCommand(msg);
            } else {
                final PlayerChatMessage playerChatMessage = PlayerChatMessage.system(msg).withUnsignedContent(Component.literal(msg));
                // TODO chat decorating
                // TODO text filtering
                this.getHandle().connection.chat(msg, playerChatMessage, false);
            }
        }
        // Paper end - Improve chat handling
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

        // Paper start - use correct pitch (modeled off of NoteBlock)
        final net.minecraft.world.level.block.state.properties.NoteBlockInstrument noteBlockInstrument = CraftBlockData.toNMS(instrument, net.minecraft.world.level.block.state.properties.NoteBlockInstrument.class);
        final float pitch = noteBlockInstrument.isTunable() ? note.getPitch() : 1.0f;
        // Paper end
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
        if (!(entity instanceof CraftEntity) || sound == null || category == null || this.getHandle().connection == null) return;

        this.playSound0(entity, CraftSound.bukkitToMinecraftHolder(sound), net.minecraft.sounds.SoundSource.valueOf(category.name()), volume, pitch, seed);
    }

    @Override
    public void playSound(org.bukkit.entity.Entity entity, String sound, org.bukkit.SoundCategory category, float volume, float pitch, long seed) {
        if (!(entity instanceof CraftEntity) || sound == null || category == null || this.getHandle().connection == null) return;

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
            Preconditions.checkArgument(effect.isApplicable(data), "%s data cannot be used for the %s effect", data.getClass().getName(), effect); // Paper
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
    public void sendMultiBlockChange(final Map<? extends io.papermc.paper.math.Position, BlockData> blockChanges) {
        if (this.getHandle().connection == null) return;

        Map<SectionPos, it.unimi.dsi.fastutil.shorts.Short2ObjectMap<net.minecraft.world.level.block.state.BlockState>> sectionMap = new HashMap<>();

        for (Map.Entry<? extends io.papermc.paper.math.Position, BlockData> entry : blockChanges.entrySet()) {
            BlockData blockData = entry.getValue();
            BlockPos blockPos = io.papermc.paper.util.MCUtil.toBlockPos(entry.getKey());
            SectionPos sectionPos = SectionPos.of(blockPos);

            it.unimi.dsi.fastutil.shorts.Short2ObjectMap<net.minecraft.world.level.block.state.BlockState> sectionData = sectionMap.computeIfAbsent(sectionPos, key -> new it.unimi.dsi.fastutil.shorts.Short2ObjectArrayMap<>());
            sectionData.put(SectionPos.sectionRelativePos(blockPos), ((CraftBlockData) blockData).getState());
        }

        for (Map.Entry<SectionPos, it.unimi.dsi.fastutil.shorts.Short2ObjectMap<net.minecraft.world.level.block.state.BlockState>> entry : sectionMap.entrySet()) {
            SectionPos sectionPos = entry.getKey();
            it.unimi.dsi.fastutil.shorts.Short2ObjectMap<net.minecraft.world.level.block.state.BlockState> blockData = entry.getValue();

            net.minecraft.network.protocol.game.ClientboundSectionBlocksUpdatePacket packet = new net.minecraft.network.protocol.game.ClientboundSectionBlocksUpdatePacket(sectionPos, blockData);
            this.getHandle().connection.send(packet);
        }
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
            BlockPos pos = cstate.getPosition();

            // The coordinates of the chunk section in which the block is located, aka chunk x, y, and z
            SectionPos sectionPosition = SectionPos.of(pos);

            // Push the block change position and block data to the final change map
            ChunkSectionChanges sectionChanges = changes.computeIfAbsent(sectionPosition, (ignore) -> new ChunkSectionChanges());

            sectionChanges.positions().add(SectionPos.sectionRelativePos(pos));
            sectionChanges.blockData().add(cstate.getHandle());
        }

        // Construct the packets using the data allocated above and send then to the players
        for (Map.Entry<SectionPos, ChunkSectionChanges> entry : changes.entrySet()) {
            ChunkSectionChanges chunkChanges = entry.getValue();
            ClientboundSectionBlocksUpdatePacket packet = new ClientboundSectionBlocksUpdatePacket(entry.getKey(), chunkChanges.positions(), chunkChanges.blockData().toArray(net.minecraft.world.level.block.state.BlockState[]::new));
            this.getHandle().connection.send(packet);
        }
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

    // Paper start
    @Override
    public void sendSignChange(Location loc, @Nullable List<? extends net.kyori.adventure.text.Component> lines, DyeColor dyeColor, boolean hasGlowingText) {
        if (getHandle().connection == null) {
            return;
        }
        if (lines == null) {
            lines = new java.util.ArrayList<>(4);
        }
        Preconditions.checkArgument(loc != null, "Location cannot be null");
        Preconditions.checkArgument(dyeColor != null, "DyeColor cannot be null");
        if (lines.size() < 4) {
            throw new IllegalArgumentException("Must have at least 4 lines");
        }
        Component[] components = CraftSign.sanitizeLines(lines);
        this.sendSignChange0(components, loc, dyeColor, hasGlowingText);
    }
    // Paper end
    @Override
    public void sendSignChange(Location loc, @Nullable String @Nullable [] lines) {
        this.sendSignChange(loc, lines, DyeColor.BLACK);
    }

    @Override
    public void sendSignChange(Location loc, @Nullable String @Nullable [] lines, DyeColor dyeColor) {
        this.sendSignChange(loc, lines, dyeColor, false);
    }

    @Override
    public void sendSignChange(Location loc, @Nullable String @Nullable [] lines, DyeColor dyeColor, boolean hasGlowingText) {
        Preconditions.checkArgument(loc != null, "Location cannot be null");
        Preconditions.checkArgument(dyeColor != null, "DyeColor cannot be null");

        if (lines == null) {
            lines = new String[4];
        }
        Preconditions.checkArgument(lines.length >= 4, "Must have at least 4 lines (%s)", lines.length);

        if (this.getHandle().connection == null) return;

        Component[] components = CraftSign.sanitizeLines(lines);
        // Paper start - adventure
        this.sendSignChange0(components, loc, dyeColor, hasGlowingText);
    }

    private void sendSignChange0(Component[] components, Location loc, DyeColor dyeColor, boolean hasGlowingText) {
        // Paper end
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
    public void sendBlockUpdate(@NonNull Location location, @NonNull TileState tileState) throws IllegalArgumentException {
        Preconditions.checkArgument(location != null, "Location can not be null");
        Preconditions.checkArgument(tileState != null, "TileState can not be null");

        if (this.getHandle().connection == null) return;

        CraftBlockEntityState<?> craftState = ((CraftBlockEntityState<?>) tileState);
        this.getHandle().connection.send(craftState.getUpdatePacket(location));
    }

    @Override
    public void sendEquipmentChange(LivingEntity entity, EquipmentSlot slot, @Nullable ItemStack item) {
        this.sendEquipmentChange(entity, java.util.Collections.singletonMap(slot, item));
    }

    @Override
    public void sendEquipmentChange(LivingEntity entity, Map<EquipmentSlot, @Nullable ItemStack> items) {
        Preconditions.checkArgument(entity != null, "Entity cannot be null");
        Preconditions.checkArgument(items != null, "items cannot be null");
        Preconditions.checkArgument(!items.isEmpty(), "items cannot be empty");

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
        // Paper start - Add target entity to sendHurtAnimation
        this.sendHurtAnimation(yaw, this);
    }
    public void sendHurtAnimation(float yaw, org.bukkit.entity.Entity target) {
        // Paper end - Add target entity to sendHurtAnimation
        if (this.getHandle().connection == null) {
            return;
        }

        /*
         * Vanilla degrees state that 0 = left, 90 = front, 180 = right, and 270 = behind.
         * This makes no sense. We'll add 90 to it so that 0 = front, clockwise from there.
         */
        float actualYaw = yaw + 90;
        this.getHandle().connection.send(new ClientboundHurtAnimationPacket(target.getEntityId(), actualYaw)); // Paper - Add target entity to sendHurtAnimation
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

    // Paper start
    @Override
    public void showWinScreen() {
        if (getHandle().connection == null) return;
        var packet = new ClientboundGameEventPacket(ClientboundGameEventPacket.WIN_GAME, 1);
        getHandle().connection.send(packet);
    }

    @Override
    public boolean hasSeenWinScreen() {
        return getHandle().seenCredits;
    }

    @Override
    public void setHasSeenWinScreen(boolean hasSeenWinScreen) {
        getHandle().seenCredits = hasSeenWinScreen;
    }
    // Paper end

    @Override
    public void setRotation(float yaw, float pitch) {
        // Paper start - Teleport API
        if (this.getHandle().connection == null) return;
        this.getHandle().forceSetRotation(yaw, pitch);
        // Paper end - Teleportation API
    }

    @Override
    public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause cause) {
        // Paper start - Teleport API
        return this.teleport(location, cause, new io.papermc.paper.entity.TeleportFlag[0]);
    }

    @Override
    public void lookAt(org.bukkit.entity.@NonNull Entity entity, @NonNull LookAnchor playerAnchor, @NonNull LookAnchor entityAnchor) {
        this.getHandle().lookAt(toNmsAnchor(playerAnchor), ((CraftEntity) entity).getHandle(), toNmsAnchor(entityAnchor));
    }

    public static net.minecraft.world.entity.Relative deltaRelativeToNMS(io.papermc.paper.entity.TeleportFlag.Relative apiFlag) {
        return switch (apiFlag) {
            case VELOCITY_X -> net.minecraft.world.entity.Relative.DELTA_X;
            case VELOCITY_Y -> net.minecraft.world.entity.Relative.DELTA_Y;
            case VELOCITY_Z -> net.minecraft.world.entity.Relative.DELTA_Z;
            case VELOCITY_ROTATION -> net.minecraft.world.entity.Relative.ROTATE_DELTA;
        };
    }

    public static @org.jetbrains.annotations.Nullable io.papermc.paper.entity.TeleportFlag.Relative deltaRelativeToAPI(net.minecraft.world.entity.Relative nmsFlag) {
        return switch (nmsFlag) {
            case DELTA_X -> io.papermc.paper.entity.TeleportFlag.Relative.VELOCITY_X;
            case DELTA_Y -> io.papermc.paper.entity.TeleportFlag.Relative.VELOCITY_Y;
            case DELTA_Z -> io.papermc.paper.entity.TeleportFlag.Relative.VELOCITY_Z;
            case ROTATE_DELTA -> io.papermc.paper.entity.TeleportFlag.Relative.VELOCITY_ROTATION;
            default -> null;
        };
    }

    @Override
    public boolean teleport(Location location, org.bukkit.event.player.PlayerTeleportEvent.TeleportCause cause, io.papermc.paper.entity.TeleportFlag... flags) {
        Set<io.papermc.paper.entity.TeleportFlag.Relative> relativeArguments;
        Set<io.papermc.paper.entity.TeleportFlag> allFlags;
        if (flags.length == 0) {
            relativeArguments = Set.of();
            allFlags = Set.of();
        } else {
            relativeArguments = java.util.EnumSet.noneOf(io.papermc.paper.entity.TeleportFlag.Relative.class);
            allFlags = new HashSet<>();
            for (io.papermc.paper.entity.TeleportFlag flag : flags) {
                if (flag instanceof final io.papermc.paper.entity.TeleportFlag.Relative relativeTeleportFlag) {
                    relativeArguments.add(relativeTeleportFlag);
                }
                allFlags.add(flag);
            }
        }
        boolean dismount = !allFlags.contains(io.papermc.paper.entity.TeleportFlag.EntityState.RETAIN_VEHICLE);
        boolean ignorePassengers = allFlags.contains(io.papermc.paper.entity.TeleportFlag.EntityState.RETAIN_PASSENGERS);
        // Paper end - Teleport API
        Preconditions.checkArgument(location != null, "location");
        Preconditions.checkArgument(location.getWorld() != null, "location.world");
        // Paper start - Teleport passenger API
        // Don't allow teleporting between worlds while keeping passengers
        if (ignorePassengers && entity.isVehicle() && location.getWorld() != this.getWorld()) {
            return false;
        }

        // Don't allow to teleport between worlds if remaining on vehicle
        if (!dismount && entity.isPassenger() && location.getWorld() != this.getWorld()) {
            return false;
        }
        // Paper end
        location.checkFinite();

        ServerPlayer entity = this.getHandle();

        if (this.getHealth() == 0 || entity.isRemoved()) {
            return false;
        }

        if (entity.connection == null) {
            return false;
        }

        if (entity.isVehicle() && !ignorePassengers) { // Paper - Teleport API
            return false;
        }

        // From = Players current Location
        Location from = this.getLocation();
        // To = Players new Location if Teleport is Successful
        Location to = location;
        // Create & Call the Teleport Event.
        PlayerTeleportEvent event = new PlayerTeleportEvent(this, from, to, cause, Set.copyOf(relativeArguments)); // Paper - Teleport API
        this.server.getPluginManager().callEvent(event);

        // Return False to inform the Plugin that the Teleport was unsuccessful/cancelled.
        if (event.isCancelled()) {
            return false;
        }

        // If this player is riding another entity, we must dismount before teleporting.
        if (dismount) entity.stopRiding(); // Paper - Teleport API

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
        if (this.getHandle().containerMenu != this.getHandle().inventoryMenu && !allFlags.contains(io.papermc.paper.entity.TeleportFlag.EntityState.RETAIN_OPEN_INVENTORY)) { // Paper
            this.getHandle().closeContainer(org.bukkit.event.inventory.InventoryCloseEvent.Reason.TELEPORT); // Paper - Inventory close reason
        }

        // Check if the fromWorld and toWorld are the same.
        if (fromWorld == toWorld) {
            // Paper start - Teleport API
            final Set<net.minecraft.world.entity.Relative> nms = java.util.EnumSet.noneOf(net.minecraft.world.entity.Relative.class);
            for (final io.papermc.paper.entity.TeleportFlag.Relative bukkit : relativeArguments) {
                nms.add(deltaRelativeToNMS(bukkit));
            }
            entity.connection.internalTeleport(new net.minecraft.world.entity.PositionMoveRotation(
                io.papermc.paper.util.MCUtil.toVec3(to), net.minecraft.world.phys.Vec3.ZERO, to.getYaw(), to.getPitch()
            ), nms);
            // Paper end - Teleport API
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
        this.server.getHandle().playerIo.load(this.getHandle(), ProblemReporter.DISCARDING);
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
    public Location getRespawnLocation(final boolean loadLocationAndValidate) {
        final ServerPlayer.RespawnConfig respawnConfig = this.getHandle().getRespawnConfig();
        if (respawnConfig == null) return null;

        final ServerLevel world = this.getHandle().getServer().getLevel(respawnConfig.dimension());
        if (world == null) return null;

        if (!loadLocationAndValidate) {
            return CraftLocation.toBukkit(respawnConfig.pos(), world.getWorld(), respawnConfig.angle(), 0);
        }

        return ServerPlayer.findRespawnAndUseSpawnBlock(world, respawnConfig, false)
            .map(pos -> CraftLocation.toBukkit(pos.position(), world.getWorld(), pos.yaw(), 0))
            .orElse(null);
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
            this.getHandle().setRespawnPosition(null, false, PlayerSetSpawnEvent.Cause.PLUGIN);
        } else {
            this.getHandle().setRespawnPosition(
                new ServerPlayer.RespawnConfig(
                    ((CraftWorld) location.getWorld()).getHandle().dimension(),
                    CraftLocation.toBlockPosition(location),
                    location.getYaw(),
                    override
                ),
                false,
                PlayerSetSpawnEvent.Cause.PLUGIN
            );
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

        BlockPos bed = this.getHandle().getRespawnConfig().pos();
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
        return this.getHandle().weatherType;
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
    public BanEntry<com.destroystokyo.paper.profile.PlayerProfile> ban(String reason, Date expires, String source) { // Paper - fix ban list API
        return this.ban(reason, expires, source, true);
    }

    @Override
    public BanEntry<com.destroystokyo.paper.profile.PlayerProfile> ban(String reason, Instant expires, String source) { // Paper - fix ban list API
        return this.ban(reason, expires != null ? Date.from(expires) : null, source);
    }

    @Override
    public BanEntry<com.destroystokyo.paper.profile.PlayerProfile> ban(String reason, Duration duration, String source) { // Paper - fix ban list API
        return this.ban(reason, duration != null ? Instant.now().plus(duration) : null, source);
    }

    @Override
    public BanEntry<com.destroystokyo.paper.profile.PlayerProfile> ban(String reason, Date expires, String source, boolean kickPlayer) { // Paper - fix ban list API
        BanEntry<com.destroystokyo.paper.profile.PlayerProfile> banEntry = ((ProfileBanList) this.server.getBanList(BanList.Type.PROFILE)).addBan(this.getPlayerProfile(), reason, expires, source); // Paper - fix ban list API
        if (kickPlayer) {
            this.kickPlayer(reason);
        }
        return banEntry;
    }

    @Override
    public BanEntry<com.destroystokyo.paper.profile.PlayerProfile> ban(String reason, Instant instant, String source, boolean kickPlayer) { // Paper - fix ban list API
        return this.ban(reason, instant != null ? Date.from(instant) : null, source, kickPlayer);
    }

    @Override
    public BanEntry<com.destroystokyo.paper.profile.PlayerProfile> ban(String reason, Duration duration, String source, boolean kickPlayer) { // Paper - fix ban list API
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

        this.getHandle().setGameMode(GameType.byId(mode.getValue()), org.bukkit.event.player.PlayerGameModeChangeEvent.Cause.PLUGIN, null); // Paper - Expand PlayerGameModeChangeEvent
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
    public int applyMending(int amount) {
        ServerPlayer handle = this.getHandle();
        // Logic copied from ExperienceOrb#repairPlayerItems

        final Optional<net.minecraft.world.item.enchantment.EnchantedItemInUse> stackEntry = net.minecraft.world.item.enchantment.EnchantmentHelper
            .getRandomItemWith(net.minecraft.world.item.enchantment.EnchantmentEffectComponents.REPAIR_WITH_XP, handle, net.minecraft.world.item.ItemStack::isDamaged);
        final net.minecraft.world.item.ItemStack itemstack = stackEntry.map(net.minecraft.world.item.enchantment.EnchantedItemInUse::itemStack).orElse(net.minecraft.world.item.ItemStack.EMPTY);
        if (!itemstack.isEmpty() && itemstack.getItem().components().has(net.minecraft.core.component.DataComponents.MAX_DAMAGE)) {
            net.minecraft.world.entity.ExperienceOrb orb = net.minecraft.world.entity.EntityType.EXPERIENCE_ORB.create(handle.level(), net.minecraft.world.entity.EntitySpawnReason.COMMAND);
            orb.setValue(amount);;
            orb.spawnReason = org.bukkit.entity.ExperienceOrb.SpawnReason.CUSTOM;
            orb.setPosRaw(handle.getX(), handle.getY(), handle.getZ());

            final int possibleDurabilityFromXp = net.minecraft.world.item.enchantment.EnchantmentHelper.modifyDurabilityToRepairFromXp(
                handle.level(), itemstack, amount
            );
            int i = Math.min(possibleDurabilityFromXp, itemstack.getDamageValue());
            final int consumedExperience = i > 0 ? i * amount / possibleDurabilityFromXp : possibleDurabilityFromXp; // Paper - taken from ExperienceOrb#repairPlayerItems + prevent division by 0
            org.bukkit.event.player.PlayerItemMendEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callPlayerItemMendEvent(handle, orb, itemstack, stackEntry.get().inSlot(), i, consumedExperience);
            i = event.getRepairAmount();
            orb.discard(org.bukkit.event.entity.EntityRemoveEvent.Cause.DESPAWN);
            if (!event.isCancelled()) {
                amount -= consumedExperience; // Use previously computed variable to reduce diff on change.
                itemstack.setDamageValue(itemstack.getDamageValue() - i);
            }
        }
        return amount;
    }

    @Override
    public void giveExp(int exp, boolean applyMending) {
        if (applyMending) {
            exp = this.applyMending(exp);
        }
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
    // Paper start
    @Override
    public int calculateTotalExperiencePoints() {
        return calculateTotalExperiencePoints(this.getLevel()) + Math.round(this.getExperiencePointsNeededForNextLevel() * getExp());
    }

    @Override
    public void setExperienceLevelAndProgress(final int totalExperience) {
        Preconditions.checkArgument(totalExperience >= 0, "Total experience points must not be negative (%s)", totalExperience);
        int level = calculateLevelsForExperiencePoints(totalExperience);
        int remainingPoints = totalExperience - calculateTotalExperiencePoints(level);

        this.getHandle().experienceLevel = level;
        this.getHandle().experienceProgress = (float) remainingPoints / this.getExperiencePointsNeededForNextLevel();
        this.getHandle().lastSentExp = -1;
    }

    @Override
    public int getExperiencePointsNeededForNextLevel() {
        return this.getHandle().getXpNeededForNextLevel();
    }

    // See https://minecraft.wiki/w/Experience#Leveling_up for reference
    private int calculateTotalExperiencePoints(int level) {
        if (level <= 16) {
            return (int) (Math.pow(level, 2) + 6 * level);
        } else if (level <= 31) {
            return (int) (2.5 * Math.pow(level, 2) - 40.5 * level + 360.0);
        } else {
            return (int) (4.5 * Math.pow(level, 2) - 162.5 * level + 2220.0);
        }
    }

    private int calculateLevelsForExperiencePoints(int points) {
        if (points <= 352) { // Level 0-16
            return (int) Math.floor(Math.sqrt(points + 9) - 3);
        } else if (points <= 1507) { // Level 17-31
            return (int) Math.floor(8.1 + Math.sqrt(0.4 * (points - (7839.0 / 40.0))));
        } else { // 32+
            return (int) Math.floor((325.0 / 18.0) + Math.sqrt((2.0 / 9.0) * (points - (54215.0 / 72.0))));
        }
    }
    // Paper end

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

    private static @Nullable WeakReference<Plugin> getPluginWeakReference(@Nullable Plugin plugin) {
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
        // Paper start
        Entity other = ((CraftEntity) entity).getHandle();
        unregisterEntity(other);

        server.getPluginManager().callEvent(new PlayerHideEntityEvent(this, entity));
    }
    private void unregisterEntity(Entity other) {
        // Paper end
        ChunkMap tracker = ((ServerLevel) this.getHandle().level()).getChunkSource().chunkMap;
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
        // Paper start - uuid override
        this.trackAndShowEntity(entity, null);
    }
    private void trackAndShowEntity(org.bukkit.entity.Entity entity, final @Nullable UUID uuidOverride) {
        // Paper end
        ChunkMap tracker = ((ServerLevel) this.getHandle().level()).getChunkSource().chunkMap;
        Entity other = ((CraftEntity) entity).getHandle();

        if (other instanceof ServerPlayer) {
            ServerPlayer otherPlayer = (ServerPlayer) other;
            // Paper start - uuid override
            UUID original = null;
            if (uuidOverride != null) {
                original = otherPlayer.getUUID();
                otherPlayer.setUUID(uuidOverride);
            }
            // Paper end
            this.getHandle().connection.send(ClientboundPlayerInfoUpdatePacket.createPlayerInitializing(List.of(otherPlayer), this.getHandle())); // Paper - Add Listing API for Player
            if (original != null) otherPlayer.setUUID(original); // Paper - uuid override
        }

        ChunkMap.TrackedEntity entry = tracker.entityMap.get(other.getId());
        if (entry != null && !entry.seenBy.contains(this.getHandle().connection)) {
            entry.updatePlayer(this.getHandle());
        }

        this.server.getPluginManager().callEvent(new PlayerShowEntityEvent(this, entity));
    }
    // Paper start
    @Override
    public void setPlayerProfile(com.destroystokyo.paper.profile.PlayerProfile profile) {
        ServerPlayer self = this.getHandle();
        GameProfile gameProfile = com.destroystokyo.paper.profile.CraftPlayerProfile.asAuthlibCopy(profile);
        if (!self.sentListPacket) {
            self.gameProfile = gameProfile;
            return;
        }
        List<ServerPlayer> players = this.server.getServer().getPlayerList().players;
        // First unregister the player for all players with the OLD game profile
        for (ServerPlayer player : players) {
            CraftPlayer bukkitPlayer = player.getBukkitEntity();
            if (bukkitPlayer.canSee(this)) {
                bukkitPlayer.unregisterEntity(self);
            }
        }

        // Set the game profile here, we should have unregistered the entity via iterating all player entities above.
        self.gameProfile = gameProfile;

        // Re-register the game profile for all players
        for (ServerPlayer player : players) {
            CraftPlayer bukkitPlayer = player.getBukkitEntity();
            if (bukkitPlayer.canSee(this)) {
                bukkitPlayer.trackAndShowEntity(self.getBukkitEntity(), gameProfile.getId());
            }
        }

        // Refresh misc player things AFTER sending game profile
        this.refreshPlayer();
    }
    // Paper end

    void resetAndShowEntity(org.bukkit.entity.Entity entity) {
        // SPIGOT-7312: Can't show/hide self
        if (this.equals(entity)) {
            return;
        }

        if (this.invertedVisibilityEntities.remove(entity.getUniqueId()) == null) {
            this.trackAndShowEntity(entity);
        }
    }
    // Paper start
    public com.destroystokyo.paper.profile.PlayerProfile getPlayerProfile() {
        return new com.destroystokyo.paper.profile.CraftPlayerProfile(this).clone();
    }

    private void refreshPlayer() {
        ServerPlayer handle = this.getHandle();

        ServerGamePacketListenerImpl connection = handle.connection;

        // Respawn the player then update their position and selected slot
        ServerLevel level = handle.level();
        connection.send(new net.minecraft.network.protocol.game.ClientboundRespawnPacket(handle.createCommonSpawnInfo(level), net.minecraft.network.protocol.game.ClientboundRespawnPacket.KEEP_ALL_DATA));
        handle.onUpdateAbilities();
        connection.internalTeleport(net.minecraft.world.entity.PositionMoveRotation.of(this.getHandle()), java.util.Collections.emptySet());
        net.minecraft.server.players.PlayerList playerList = handle.getServer().getPlayerList();
        playerList.sendPlayerPermissionLevel(handle, false);
        playerList.sendLevelInfo(handle, level);
        playerList.sendAllPlayerInfo(handle);

        // Resend their XP and effects because the respawn packet resets it
        connection.send(new net.minecraft.network.protocol.game.ClientboundSetExperiencePacket(handle.experienceProgress, handle.totalExperience, handle.experienceLevel));
        for (net.minecraft.world.effect.MobEffectInstance mobEffect : handle.getActiveEffects()) {
            connection.send(new net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket(handle.getId(), mobEffect, false));
        }
    }
    // Paper end

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

    // Paper start - Add Listing API for Player
    @Override
    public boolean isListed(Player other) {
        return !this.unlistedEntities.contains(other.getUniqueId());
    }

    @Override
    public boolean unlistPlayer(@NonNull Player other) {
        Preconditions.checkNotNull(other, "hidden entity cannot be null");
        if (this.getHandle().connection == null) return false;
        if (!this.canSee(other)) return false;

        if (unlistedEntities.add(other.getUniqueId())) {
            this.getHandle().connection.send(ClientboundPlayerInfoUpdatePacket.updateListed(other.getUniqueId(), false));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean listPlayer(@NonNull Player other) {
        Preconditions.checkNotNull(other, "hidden entity cannot be null");
        if (this.getHandle().connection == null) return false;
        if (!this.canSee(other)) throw new IllegalStateException("Player cannot see other player");

        if (this.unlistedEntities.remove(other.getUniqueId())) {
            this.getHandle().connection.send(ClientboundPlayerInfoUpdatePacket.updateListed(other.getUniqueId(), true));
            return true;
        } else {
            return false;
        }
    }
    // Paper end - Add Listing API for Player

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

    // Paper start - getLastPlayed replacement API
    @Override
    public long getLastLogin() {
        return this.getHandle().loginTime;
    }

    @Override
    public long getLastSeen() {
        return this.isOnline() ? System.currentTimeMillis() : this.lastSaveTime;
    }
    // Paper end - getLastPlayed replacement API

    public void readExtraData(ValueInput tag) {
        this.hasPlayedBefore = true;
        tag.child("bukkit").ifPresent(data -> {
            this.firstPlayed = data.getLongOr("firstPlayed", 0);
            this.lastPlayed = data.getLongOr("lastPlayed", 0);

            final ServerPlayer handle = getHandle();
            handle.newExp = data.getIntOr("newExp", 0);
            handle.newTotalExp = data.getIntOr("newTotalExp", 0);
            handle.newLevel = data.getIntOr("newLevel", 0);
            handle.expToDrop = data.getIntOr("expToDrop", 0);
            handle.keepLevel = data.getBooleanOr("keepLevel", false);
        });
    }

    public void setExtraData(ValueOutput tag) {
        this.lastSaveTime = System.currentTimeMillis(); // Paper

        ValueOutput data = tag.child("bukkit");
        ServerPlayer handle = this.getHandle();
        data.putInt("newExp", handle.newExp);
        data.putInt("newTotalExp", handle.newTotalExp);
        data.putInt("newLevel", handle.newLevel);
        data.putInt("expToDrop", handle.expToDrop);
        data.putBoolean("keepLevel", handle.keepLevel);
        data.putLong("firstPlayed", this.getFirstPlayed());
        data.putLong("lastPlayed", System.currentTimeMillis());
        data.putString("lastKnownName", handle.getScoreboardName());

        // Paper start - persist for use in offline save data
        ValueOutput paper = tag.child("Paper");
        paper.putLong("LastLogin", handle.loginTime);
        paper.putLong("LastSeen", System.currentTimeMillis());
        // Paper end
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
        ClientboundCustomPayloadPacket packet = new ClientboundCustomPayloadPacket(new DiscardedPayload(id, message));
        this.getHandle().connection.send(packet);
    }

    @Override
    public void setTexturePack(String url) {
        this.setResourcePack(url);
    }

    @Override
    public void setResourcePack(String url) {
        this.setResourcePack(url, (byte[]) null);
    }

    @Override
    public void setResourcePack(String url, byte @Nullable [] hash) {
        this.setResourcePack(url, hash, false);
    }

    @Override
    public void setResourcePack(String url, byte @Nullable [] hash, String prompt) {
        this.setResourcePack(url, hash, prompt, false);
    }

    @Override
    public void setResourcePack(String url, byte @Nullable [] hash, boolean force) {
        this.setResourcePack(url, hash, (String) null, force);
    }

    @Override
    public void setResourcePack(String url, byte @Nullable [] hash, String prompt, boolean force) {
        Preconditions.checkArgument(url != null, "Resource pack URL cannot be null");

        this.setResourcePack(UUID.nameUUIDFromBytes(url.getBytes(StandardCharsets.UTF_8)), url, hash, prompt, force);
    }

    @Override
    public void setResourcePack(UUID id, String url, byte @Nullable [] hash, String prompt, boolean force) {
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
    public void addResourcePack(UUID id, String url, byte @Nullable [] hash, String prompt, boolean force) {
        Preconditions.checkArgument(url != null, "Resource pack URL cannot be null");

        String hashStr = "";
        if (hash != null) {
            Preconditions.checkArgument(hash.length == 20, "Resource pack hash should be 20 bytes long but was %s", hash.length);
            hashStr = BaseEncoding.base16().lowerCase().encode(hash);
        }

        this.handlePushResourcePack(new ClientboundResourcePackPushPacket(id, url, hashStr, force, CraftChatMessage.fromStringOrOptional(prompt, true)), false);
    }

    // Paper start - adventure
    @Override
    public void setResourcePack(final UUID uuid, final String url, final byte @Nullable [] hashBytes, final net.kyori.adventure.text.Component prompt, final boolean force) {
        Preconditions.checkArgument(uuid != null, "Resource pack UUID cannot be null");
        Preconditions.checkArgument(url != null, "Resource pack URL cannot be null");
        final String hash;
        if (hashBytes != null) {
            Preconditions.checkArgument(hashBytes.length == 20, "Resource pack hash should be 20 bytes long but was " + hashBytes.length);
            hash = BaseEncoding.base16().lowerCase().encode(hashBytes);
        } else {
            hash = "";
        }
        this.getHandle().connection.send(new ClientboundResourcePackPopPacket(Optional.empty()));
        this.getHandle().connection.send(new ClientboundResourcePackPushPacket(uuid, url, hash, force, Optional.ofNullable(prompt).map(io.papermc.paper.adventure.PaperAdventure::asVanilla)));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    void sendBundle(final List<? extends net.minecraft.network.protocol.Packet<? extends net.minecraft.network.protocol.common.ClientCommonPacketListener>> packet) {
        this.getHandle().connection.send(new net.minecraft.network.protocol.game.ClientboundBundlePacket((List) packet));
    }

    @Override
    public void sendResourcePacks(final net.kyori.adventure.resource.ResourcePackRequest request) {
        if (this.getHandle().connection == null) return;
        final List<ClientboundResourcePackPushPacket> packs = new java.util.ArrayList<>(request.packs().size());
        if (request.replace()) {
            this.clearResourcePacks();
        }
        final Component prompt = io.papermc.paper.adventure.PaperAdventure.asVanilla(request.prompt());
        for (final java.util.Iterator<net.kyori.adventure.resource.ResourcePackInfo> iter = request.packs().iterator(); iter.hasNext();) {
            final net.kyori.adventure.resource.ResourcePackInfo pack = iter.next();
            packs.add(new ClientboundResourcePackPushPacket(pack.id(), pack.uri().toASCIIString(), pack.hash(), request.required(), iter.hasNext() ? Optional.empty() : Optional.ofNullable(prompt)));
            if (request.callback() != net.kyori.adventure.resource.ResourcePackCallback.noOp()) {
                this.getHandle().connection.packCallbacks.put(pack.id(), request.callback()); // just override if there is a previously existing callback
            }
        }
        this.sendBundle(packs);
        super.sendResourcePacks(request);
    }

    @Override
    public void removeResourcePacks(final UUID id, final UUID... others) {
        if (this.getHandle().connection == null) return;
        this.sendBundle(net.kyori.adventure.util.MonkeyBars.nonEmptyArrayToList(pack -> new ClientboundResourcePackPopPacket(Optional.of(pack)), id, others));
    }

    @Override
    public void clearResourcePacks() {
        if (this.getHandle().connection == null) return;
        this.getHandle().connection.send(new ClientboundResourcePackPopPacket(Optional.empty()));
    }
    // Paper end - adventure

    // Paper start - more resource pack API
    @Override
    public org.bukkit.event.player.PlayerResourcePackStatusEvent.Status getResourcePackStatus() {
        return this.resourcePackStatus;
    }
    // Paper end - more resource pack API

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
        Preconditions.checkState(DISABLE_CHANNEL_LIMIT || this.channels.size() < 128, "Cannot register channel. Too many channels registered!"); // Paper - flag to disable channel limit
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
                    stream.write(channel.getBytes(StandardCharsets.UTF_8));
                    stream.write((byte) 0);
                } catch (IOException ex) {
                    Logger.getLogger(CraftPlayer.class.getName()).log(Level.SEVERE, "Could not send Plugin Channel REGISTER to " + this.getName(), ex);
                }
            }

            this.sendCustomPayload(ServerGamePacketListenerImpl.CUSTOM_REGISTER, stream.toByteArray());
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
        boolean needsUpdate = getHandle().getAbilities().flying != value; // Paper - Only refresh abilities if needed
        if (!this.getAllowFlight()) {
            Preconditions.checkArgument(!value, "Player is not allowed to fly (check #getAllowFlight())");
        }

        this.getHandle().getAbilities().flying = value;
        if (needsUpdate) this.getHandle().onUpdateAbilities(); // Paper - Only refresh abilities if needed
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

    // Paper start - flying fall damage
    @Override
    public void setFlyingFallDamage(@NonNull TriState flyingFallDamage) {
        getHandle().flyingFallDamage = flyingFallDamage;
    }

    @Override
    public @NonNull TriState hasFlyingFallDamage() {
        return getHandle().flyingFallDamage;
    }
    // Paper end - flying fall damage

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
        if (!(scoreboard instanceof CraftScoreboard craftScoreboard)) {
            throw new IllegalArgumentException("Cannot set player scoreboard to an unregistered Scoreboard");
        }

        this.server.getScoreboardManager().setPlayerBoard(this, craftScoreboard);
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
        if (Double.isNaN(health)) return; // Paper - Check for NaN
        this.health = health;
    }

    public void updateScaledHealth() {
        this.updateScaledHealth(true);
    }

    public void updateScaledHealth(boolean sendHealth) {
        // SPIGOT-3813: Attributes before health
        if (this.getHandle().connection != null) {
            this.getHandle().connection.send(new ClientboundUpdateAttributesPacket(this.getHandle().getId(), Set.of(this.getScaledMaxHealth())));
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
        // Paper start - cancellable death event
        ClientboundSetHealthPacket packet = new ClientboundSetHealthPacket(this.getScaledHealth(), foodData.getFoodLevel(), foodData.getSaturationLevel());
        if (this.getHandle().queueHealthUpdatePacket) {
            this.getHandle().queuedHealthUpdatePacket = packet;
        } else {
            this.getHandle().connection.send(packet);
        }
        // Paper end
    }

    public void injectScaledMaxHealth(Collection<AttributeInstance> collection, boolean force) {
        if (!this.scaledHealth && !force) {
            return;
        }
        Iterator<AttributeInstance> iterator = collection.iterator();
        while (iterator.hasNext()) {
            AttributeInstance genericInstance = iterator.next();
            if (genericInstance.getAttribute() == Attributes.MAX_HEALTH) {
                iterator.remove();
                break;
            }
        }
        collection.add(getScaledMaxHealth());
    }

    public AttributeInstance getScaledMaxHealth() {
        AttributeInstance dummy = new AttributeInstance(Attributes.MAX_HEALTH, (attribute) -> { });
        double healthMod = this.scaledHealth ? this.healthScale : this.getMaxHealth();
        if (healthMod >= Float.MAX_VALUE || healthMod <= 0) {
            healthMod = 20; // Reset health
            this.getServer().getLogger().warning(this.getName() + " tried to crash the server with a large health attribute");
        }
        dummy.setBaseValue(healthMod);
        return dummy;
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
        ClientboundLevelParticlesPacket packetplayoutworldparticles = new ClientboundLevelParticlesPacket(CraftParticle.createParticleParam(particle, data), force, false, x, y, z, (float) offsetX, (float) offsetY, (float) offsetZ, (float) extra, count); // Paper - fix x/y/z precision loss
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

    // Paper start
    @Override
    public java.util.Locale locale() {
        return getHandle().adventure$locale;
    }
    // Paper end
    @Override
    public int getPing() {
        return this.getHandle().connection.latency();
    }

    @Override
    public String getLocale() {
        // Paper start - Locale change event
        final String locale = this.getHandle().language;
        return locale != null ? locale : "en_us";
        // Paper end
    }

    // Paper start
    public void setAffectsSpawning(boolean affects) {
        this.getHandle().affectsSpawning = affects;
    }

    @Override
    public boolean getAffectsSpawning() {
        return this.getHandle().affectsSpawning;
    }
    // Paper end

    @Override
    public void updateCommands() {
        if (this.getHandle().connection == null) return;

        this.getHandle().getServer().getCommands().sendCommands(this.getHandle());
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
    public void openSign(@NonNull Sign sign, @NonNull Side side) {
        CraftSign.openSign(sign, this, side);
    }

    @Override
    public void openVirtualSign(Position block, Side side) {
        if (this.getHandle().connection == null) return;

        this.getHandle().connection.send(new ClientboundOpenSignEditorPacket(MCUtil.toBlockPos(block), side == Side.FRONT));
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

    // Paper start
    @Override
    public net.kyori.adventure.text.Component displayName() {
        return this.getHandle().adventure$displayName;
    }

    @Override
    public void displayName(final net.kyori.adventure.text.Component displayName) {
        this.getHandle().adventure$displayName = displayName != null ? displayName : net.kyori.adventure.text.Component.text(this.getName());
        this.getHandle().displayName = null;
    }

    @Override
    public void deleteMessage(net.kyori.adventure.chat.SignedMessage.Signature signature) {
        if (getHandle().connection == null) return;
        net.minecraft.network.chat.MessageSignature sig = new net.minecraft.network.chat.MessageSignature(signature.bytes());

        this.getHandle().connection.send(new net.minecraft.network.protocol.game.ClientboundDeleteChatPacket(new net.minecraft.network.chat.MessageSignature.Packed(sig)));
    }

    private net.minecraft.network.chat.ChatType.Bound toHandle(net.kyori.adventure.chat.ChatType.Bound boundChatType) {
        net.minecraft.core.Registry<net.minecraft.network.chat.ChatType> chatTypeRegistry = this.getHandle().level().registryAccess().lookupOrThrow(net.minecraft.core.registries.Registries.CHAT_TYPE);

        return new net.minecraft.network.chat.ChatType.Bound(
            chatTypeRegistry.getOrThrow(net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.CHAT_TYPE, io.papermc.paper.adventure.PaperAdventure.asVanilla(boundChatType.type().key()))),
            io.papermc.paper.adventure.PaperAdventure.asVanilla(boundChatType.name()),
            Optional.ofNullable(io.papermc.paper.adventure.PaperAdventure.asVanilla(boundChatType.target()))
        );
    }

    @Override
    public void sendMessage(net.kyori.adventure.text.Component message, net.kyori.adventure.chat.ChatType.Bound boundChatType) {
        if (getHandle().connection == null) return;

        net.minecraft.network.chat.Component component = io.papermc.paper.adventure.PaperAdventure.asVanilla(message);
        this.getHandle().sendChatMessage(new net.minecraft.network.chat.OutgoingChatMessage.Disguised(component), this.getHandle().isTextFilteringEnabled(), this.toHandle(boundChatType));
    }

    @Override
    public void sendMessage(net.kyori.adventure.chat.SignedMessage signedMessage, net.kyori.adventure.chat.ChatType.Bound boundChatType) {
        if (getHandle().connection == null) return;

        if (signedMessage instanceof PlayerChatMessage.AdventureView view) {
            this.getHandle().sendChatMessage(net.minecraft.network.chat.OutgoingChatMessage.create(view.playerChatMessage()), this.getHandle().isTextFilteringEnabled(), this.toHandle(boundChatType));
            return;
        }
        net.kyori.adventure.text.Component message = signedMessage.unsignedContent() == null ? net.kyori.adventure.text.Component.text(signedMessage.message()) : signedMessage.unsignedContent();
        if (signedMessage.isSystem()) {
            this.sendMessage(message, boundChatType);
        } else {
            super.sendMessage(signedMessage, boundChatType);
        }
//        net.minecraft.network.chat.PlayerChatMessage playerChatMessage = new net.minecraft.network.chat.PlayerChatMessage(
//            null, // TODO:
//            new net.minecraft.network.chat.MessageSignature(signedMessage.signature().bytes()),
//            null, // TODO
//            io.papermc.paper.adventure.PaperAdventure.asVanilla(signedMessage.unsignedContent()),
//            net.minecraft.network.chat.FilterMask.PASS_THROUGH
//        );
//
//        this.getHandle().sendChatMessage(net.minecraft.network.chat.OutgoingChatMessage.create(playerChatMessage), this.getHandle().isTextFilteringEnabled(), this.toHandle(boundChatType));
    }

    @Deprecated(forRemoval = true)
    @Override
    public void sendMessage(final net.kyori.adventure.identity.Identity identity, final net.kyori.adventure.text.Component message, final net.kyori.adventure.audience.MessageType type) {
        if (getHandle().connection == null) return;
        final net.minecraft.core.Registry<net.minecraft.network.chat.ChatType> chatTypeRegistry = this.getHandle().level().registryAccess().lookupOrThrow(net.minecraft.core.registries.Registries.CHAT_TYPE);
        this.getHandle().connection.send(new net.minecraft.network.protocol.game.ClientboundSystemChatPacket(message, false));
    }

    @Override
    public void sendActionBar(final net.kyori.adventure.text.Component message) {
        final net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket packet = new net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket(io.papermc.paper.adventure.PaperAdventure.asVanillaNullToEmpty(message));
        this.getHandle().connection.send(packet);
    }

    @Override
    public void sendPlayerListHeader(final net.kyori.adventure.text.Component header) {
        this.playerListHeader = header;
        this.adventure$sendPlayerListHeaderAndFooter();
    }

    @Override
    public void sendPlayerListFooter(final net.kyori.adventure.text.Component footer) {
        this.playerListFooter = footer;
        this.adventure$sendPlayerListHeaderAndFooter();
    }

    @Override
    public void sendPlayerListHeaderAndFooter(final net.kyori.adventure.text.Component header, final net.kyori.adventure.text.Component footer) {
        this.playerListHeader = header;
        this.playerListFooter = footer;
        this.adventure$sendPlayerListHeaderAndFooter();
    }

    private void adventure$sendPlayerListHeaderAndFooter() {
        final ServerGamePacketListenerImpl connection = this.getHandle().connection;
        if (connection == null) return;
        final ClientboundTabListPacket packet = new ClientboundTabListPacket(
            io.papermc.paper.adventure.PaperAdventure.asVanillaNullToEmpty(this.playerListHeader),
            io.papermc.paper.adventure.PaperAdventure.asVanillaNullToEmpty(this.playerListFooter)
        );
        connection.send(packet);
    }

    @Override
    public void showTitle(final net.kyori.adventure.title.Title title) {
        final ServerGamePacketListenerImpl connection = this.getHandle().connection;
        final net.kyori.adventure.title.Title.Times times = title.times();
        if (times != null) {
            connection.send(new ClientboundSetTitlesAnimationPacket(ticks(times.fadeIn()), ticks(times.stay()), ticks(times.fadeOut())));
        }
        final ClientboundSetSubtitleTextPacket sp = new ClientboundSetSubtitleTextPacket(io.papermc.paper.adventure.PaperAdventure.asVanilla(title.subtitle()));
        connection.send(sp);
        final ClientboundSetTitleTextPacket tp = new ClientboundSetTitleTextPacket(io.papermc.paper.adventure.PaperAdventure.asVanilla(title.title()));
        connection.send(tp);
    }

    @Override
    public <T> void sendTitlePart(final net.kyori.adventure.title.TitlePart<T> part, T value) {
        java.util.Objects.requireNonNull(part, "part");
        java.util.Objects.requireNonNull(value, "value");
        if (part == net.kyori.adventure.title.TitlePart.TITLE) {
            final ClientboundSetTitleTextPacket tp = new ClientboundSetTitleTextPacket(io.papermc.paper.adventure.PaperAdventure.asVanilla((net.kyori.adventure.text.Component)value));
            this.getHandle().connection.send(tp);
        } else if (part == net.kyori.adventure.title.TitlePart.SUBTITLE) {
            final ClientboundSetSubtitleTextPacket sp = new ClientboundSetSubtitleTextPacket(io.papermc.paper.adventure.PaperAdventure.asVanilla((net.kyori.adventure.text.Component)value));
            this.getHandle().connection.send(sp);
        } else if (part == net.kyori.adventure.title.TitlePart.TIMES) {
            final net.kyori.adventure.title.Title.Times times = (net.kyori.adventure.title.Title.Times) value;
            this.getHandle().connection.send(new ClientboundSetTitlesAnimationPacket(ticks(times.fadeIn()), ticks(times.stay()), ticks(times.fadeOut())));
        } else {
            throw new IllegalArgumentException("Unknown TitlePart");
        }
    }

    private static int ticks(final java.time.Duration duration) {
        if (duration == null) {
            return -1;
        }
        return (int) (duration.toMillis() / 50L);
    }

    @Override
    public void clearTitle() {
        this.getHandle().connection.send(new net.minecraft.network.protocol.game.ClientboundClearTitlesPacket(false));
    }

    // resetTitle implemented above

    private @Nullable Set<net.kyori.adventure.bossbar.BossBar> activeBossBars;

    @Override
    public @NonNull Iterable<? extends net.kyori.adventure.bossbar.BossBar> activeBossBars() {
        if (this.activeBossBars != null) {
            return java.util.Collections.unmodifiableSet(this.activeBossBars);
        }
        return Set.of();
    }

    @Override
    public void showBossBar(final net.kyori.adventure.bossbar.BossBar bar) {
        net.kyori.adventure.bossbar.BossBarImplementation.get(bar, io.papermc.paper.adventure.BossBarImplementationImpl.class).playerShow(this);
        if (this.activeBossBars == null) {
            this.activeBossBars = new HashSet<>();
        }
        this.activeBossBars.add(bar);
    }

    @Override
    public void hideBossBar(final net.kyori.adventure.bossbar.BossBar bar) {
        net.kyori.adventure.bossbar.BossBarImplementation.get(bar, io.papermc.paper.adventure.BossBarImplementationImpl.class).playerHide(this);
        if (this.activeBossBars != null) {
            this.activeBossBars.remove(bar);
            if (this.activeBossBars.isEmpty()) {
                this.activeBossBars = null;
            }
        }
    }

    @Override
    public void playSound(final net.kyori.adventure.sound.Sound sound) {
        final net.minecraft.world.phys.Vec3 pos = this.getHandle().position();
        this.playSound(sound, pos.x, pos.y, pos.z);
    }

    @Override
    public void playSound(final net.kyori.adventure.sound.Sound sound, final double x, final double y, final double z) {
        this.getHandle().connection.send(io.papermc.paper.adventure.PaperAdventure.asSoundPacket(sound, x, y, z, sound.seed().orElseGet(this.getHandle().getRandom()::nextLong), null));
    }

    @Override
    public void playSound(final net.kyori.adventure.sound.Sound sound, final net.kyori.adventure.sound.Sound.Emitter emitter) {
        final Entity entity;
        if (emitter == net.kyori.adventure.sound.Sound.Emitter.self()) {
            entity = this.getHandle();
        } else if (emitter instanceof CraftEntity craftEntity) {
            entity = craftEntity.getHandle();
        } else {
            throw new IllegalArgumentException("Sound emitter must be an Entity or self(), but was: " + emitter);
        }
        this.getHandle().connection.send(io.papermc.paper.adventure.PaperAdventure.asSoundPacket(sound, entity, sound.seed().orElseGet(this.getHandle().getRandom()::nextLong), null));
    }

    @Override
    public void stopSound(final net.kyori.adventure.sound.SoundStop stop) {
        this.getHandle().connection.send(new ClientboundStopSoundPacket(
            io.papermc.paper.adventure.PaperAdventure.asVanillaNullable(stop.sound()),
            io.papermc.paper.adventure.PaperAdventure.asVanillaNullable(stop.source())
        ));
    }

    @Override
    public void openBook(final net.kyori.adventure.inventory.Book book) {
        final java.util.Locale locale = this.getHandle().adventure$locale;
        final net.minecraft.world.item.ItemStack item = io.papermc.paper.adventure.PaperAdventure.asItemStack(book, locale);
        final ServerPlayer player = this.getHandle();
        final ServerGamePacketListenerImpl connection = player.connection;
        final net.minecraft.world.entity.player.Inventory inventory = player.getInventory();
        final int slot = inventory.getNonEquipmentItems().size() + inventory.getSelectedSlot();
        final int stateId = getHandle().containerMenu.getStateId();
        connection.send(new net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket(0, stateId, slot, item));
        connection.send(new net.minecraft.network.protocol.game.ClientboundOpenBookPacket(net.minecraft.world.InteractionHand.MAIN_HAND));
        connection.send(new net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket(0, stateId, slot, inventory.getSelectedItem()));
    }

    @Override
    public net.kyori.adventure.pointer.Pointers pointers() {
        if (this.adventure$pointers == null) {
            this.adventure$pointers = net.kyori.adventure.pointer.Pointers.builder()
                .withDynamic(net.kyori.adventure.identity.Identity.DISPLAY_NAME, this::displayName)
                .withDynamic(net.kyori.adventure.identity.Identity.NAME, this::getName)
                .withDynamic(net.kyori.adventure.identity.Identity.UUID, this::getUniqueId)
                .withStatic(net.kyori.adventure.permission.PermissionChecker.POINTER, this::permissionValue)
                .withDynamic(net.kyori.adventure.identity.Identity.LOCALE, this::locale)
                .build();
        }

        return this.adventure$pointers;
    }

    @Override
    public float getCooldownPeriod() {
        return getHandle().getCurrentItemAttackStrengthDelay();
    }

    @Override
    public float getCooledAttackStrength(float adjustTicks) {
        return getHandle().getAttackStrengthScale(adjustTicks);
    }

    @Override
    public void resetCooldown() {
        getHandle().resetAttackStrengthTicker();
    }
    // Paper end
    // Spigot start
    private final Player.Spigot spigot = new Player.Spigot() {

        @Override
        public InetSocketAddress getRawAddress() {
            return (InetSocketAddress) CraftPlayer.this.getHandle().connection.getRawAddress();
        }

        @Override
        public void respawn() {
            if (CraftPlayer.this.getHealth() <= 0 && CraftPlayer.this.isOnline()) {
                CraftPlayer.this.server.getServer().getPlayerList().respawn(CraftPlayer.this.getHandle(), false, Entity.RemovalReason.KILLED, org.bukkit.event.player.PlayerRespawnEvent.RespawnReason.PLUGIN);
            }
        }

        @Override
        public Set<Player> getHiddenPlayers() {
            Set<Player> ret = new HashSet<>();
            for (Player player : CraftPlayer.this.getServer().getOnlinePlayers()) {
                if (!CraftPlayer.this.canSee(player)) {
                    ret.add(player);
                }
            }

            return java.util.Collections.unmodifiableSet(ret);
        }

        @Override
        public void sendMessage(BaseComponent component) {
            this.sendMessage(new BaseComponent[]{component});
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
            this.sendMessage(position, new BaseComponent[]{component});
        }

        @Override
        public void sendMessage(net.md_5.bungee.api.ChatMessageType position, BaseComponent... components) {
            this.sendMessage(position, null, components);
        }

        @Override
        public void sendMessage(net.md_5.bungee.api.ChatMessageType position, UUID sender, BaseComponent component) {
            this.sendMessage(position, sender, new BaseComponent[]{component});
        }

        @Override
        public void sendMessage(net.md_5.bungee.api.ChatMessageType position, UUID sender, BaseComponent... components) {
            if (CraftPlayer.this.getHandle().connection == null) return;

            CraftPlayer.this.getHandle().connection.send(new net.minecraft.network.protocol.game.ClientboundSystemChatPacket(components, position == net.md_5.bungee.api.ChatMessageType.ACTION_BAR));
        }

        // Paper start
        @Override
        public int getPing() {
            return CraftPlayer.this.getPing();
        }
        // Paper end
    };

    // Paper start - brand support
    @Override
    public String getClientBrandName() {
        return getHandle().clientBrandName;
    }
    // Paper end

    // Paper start
    @Override
    public void showElderGuardian(boolean silent) {
        if (getHandle().connection != null) getHandle().connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.GUARDIAN_ELDER_EFFECT, silent ? 0F : 1F));
    }

    @Override
    public int getWardenWarningCooldown() {
        return this.getHandle().wardenSpawnTracker.cooldownTicks;
    }

    @Override
    public void setWardenWarningCooldown(int cooldown) {
        this.getHandle().wardenSpawnTracker.cooldownTicks = Math.max(cooldown, 0);
    }

    @Override
    public int getWardenTimeSinceLastWarning() {
        return this.getHandle().wardenSpawnTracker.ticksSinceLastWarning;
    }

    @Override
    public void setWardenTimeSinceLastWarning(int time) {
        this.getHandle().wardenSpawnTracker.ticksSinceLastWarning = time;
    }

    @Override
    public int getWardenWarningLevel() {
        return this.getHandle().wardenSpawnTracker.getWarningLevel();
    }

    @Override
    public void setWardenWarningLevel(int warningLevel) {
        this.getHandle().wardenSpawnTracker.setWarningLevel(warningLevel);
    }

    @Override
    public void increaseWardenWarningLevel() {
        this.getHandle().wardenSpawnTracker.increaseWarningLevel();
    }
    // Paper end

    // Paper start
    @Override
    public Duration getIdleDuration() {
        return Duration.ofMillis(net.minecraft.Util.getMillis() - this.getHandle().getLastActionTime());
    }

    @Override
    public void resetIdleDuration() {
        this.getHandle().resetLastActionTime();
    }
    // Paper end

    // Paper start - Add chunk view API
    @Override
    public Set<java.lang.Long> getSentChunkKeys() {
        org.spigotmc.AsyncCatcher.catchOp("accessing sent chunks");
        return FeatureHooks.getSentChunkKeys(this.getHandle());
    }

    @Override
    public Set<org.bukkit.Chunk> getSentChunks() {
        org.spigotmc.AsyncCatcher.catchOp("accessing sent chunks");
        return FeatureHooks.getSentChunks(this.getHandle());
    }

    @Override
    public boolean isChunkSent(final long chunkKey) {
        org.spigotmc.AsyncCatcher.catchOp("accessing sent chunks");
        return FeatureHooks.isChunkSent(this.getHandle(), chunkKey);
    }
    // Paper end

    public Player.Spigot spigot() {
        return this.spigot;
    }
    // Spigot end

    @Override
    public int getViewDistance() {
        return ca.spottedleaf.moonrise.common.PlatformHooks.get().getViewDistance(this.getHandle());
    }

    @Override
    public void setViewDistance(final int viewDistance) {
        FeatureHooks.setViewDistance(this.getHandle(), viewDistance); // Paper - chunk system
    }

    @Override
    public int getSimulationDistance() {
        return ca.spottedleaf.moonrise.common.PlatformHooks.get().getTickViewDistance(this.getHandle());
    }

    @Override
    public void setSimulationDistance(final int simulationDistance) {
        FeatureHooks.setSimulationDistance(this.getHandle(), simulationDistance); // Paper - chunk system
    }

    @Override
    public int getSendViewDistance() {
        return ca.spottedleaf.moonrise.common.PlatformHooks.get().getSendViewDistance(this.getHandle());
    }

    @Override
    public void setSendViewDistance(final int viewDistance) {
        FeatureHooks.setSendViewDistance(this.getHandle(), viewDistance); // Paper - chunk system
    }

    // Paper start - entity effect API
    @Override
    public void sendEntityEffect(final EntityEffect effect, final org.bukkit.entity.Entity target) {
        if (this.getHandle().connection == null) {
            return;
        }
        Preconditions.checkArgument(effect.isApplicableTo(target), "Entity effect cannot apply to the target");
        this.getHandle().connection.send(new net.minecraft.network.protocol.game.ClientboundEntityEventPacket(((CraftEntity) target).getHandle(), effect.getData()));
    }
    // Paper end - entity effect API

    @Override
    public @NonNull PlayerGiveResult give(final @NonNull Collection<@NonNull ItemStack> items, final boolean dropIfFull) {
        Preconditions.checkArgument(items != null, "items cannot be null");
        if (items.isEmpty()) return PaperPlayerGiveResult.EMPTY; // Early opt out for empty input.

        // Validate all items before attempting to spawn any.
        for (final ItemStack item : items) {
            Preconditions.checkArgument(item != null, "ItemStack cannot be null");
            Preconditions.checkArgument(!item.isEmpty(), "ItemStack cannot be empty");
            Preconditions.checkArgument(item.getAmount() <= item.getMaxStackSize(), "ItemStack amount cannot be greater than its max stack size");
        }

        final ServerPlayer handle = this.getHandle();
        final ImmutableList.Builder<Item> drops = ImmutableList.builder();
        final ImmutableList.Builder<ItemStack> leftovers = ImmutableList.builder();
        for (final ItemStack item : items) {
            final net.minecraft.world.item.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
            final boolean added = handle.getInventory().add(nmsStack);
            if (added && nmsStack.isEmpty()) continue; // Item was fully added, neither a drop nor a leftover is needed.

            leftovers.add(CraftItemStack.asBukkitCopy(nmsStack)); // Insert copy to avoid mutation to the dropped item from affecting leftovers
            if (!dropIfFull) continue;

            final ItemEntity entity = handle.drop(nmsStack, false, true);
            if (entity != null) drops.add((Item) entity.getBukkitEntity());
        }

        handle.containerMenu.broadcastChanges();
        return new PaperPlayerGiveResult(leftovers.build(), drops.build());
    }

    @Override
    public float getSidewaysMovement() {
        final boolean leftMovement = this.getHandle().getLastClientInput().left();
        final boolean rightMovement = this.getHandle().getLastClientInput().right();

        return leftMovement == rightMovement ? 0 : leftMovement ? 1 : -1;
    }

    @Override
    public float getForwardsMovement() {
        final boolean forwardMovement = this.getHandle().getLastClientInput().forward();
        final boolean backwardMovement = this.getHandle().getLastClientInput().backward();

        return forwardMovement == backwardMovement ? 0 : forwardMovement ? 1 : -1;
    }

    @Override
    public int getDeathScreenScore() {
        return getHandle().getScore();
    }

    @Override
    public void setDeathScreenScore(final int score) {
        getHandle().setScore(score);
    }
}
