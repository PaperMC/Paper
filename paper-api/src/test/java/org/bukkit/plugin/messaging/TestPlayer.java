package org.bukkit.plugin.messaging;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.InventoryView.Property;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.map.MapView;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class TestPlayer implements Player {
    public String getDisplayName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setDisplayName(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getPlayerListName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setPlayerListName(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setCompassTarget(Location loc) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Location getCompassTarget() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public InetSocketAddress getAddress() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void sendRawMessage(String message) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void kickPlayer(String message) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void chat(String msg) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean performCommand(String command) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isSneaking() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setSneaking(boolean sneak) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isSprinting() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setSprinting(boolean sprinting) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void saveData() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void loadData() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setSleepingIgnored(boolean isSleeping) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isSleepingIgnored() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void playNote(Location loc, byte instrument, byte note) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void playNote(Location loc, Instrument instrument, Note note) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void playEffect(Location loc, Effect effect, int data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void sendBlockChange(Location loc, Material material, byte data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean sendChunkChange(Location loc, int sx, int sy, int sz, byte[] data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void sendBlockChange(Location loc, int material, byte data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void sendMap(MapView map) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void updateInventory() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void awardAchievement(Achievement achievement) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void incrementStatistic(Statistic statistic) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void incrementStatistic(Statistic statistic, int amount) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void incrementStatistic(Statistic statistic, Material material) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void incrementStatistic(Statistic statistic, Material material, int amount) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setPlayerTime(long time, boolean relative) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public long getPlayerTime() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public long getPlayerTimeOffset() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isPlayerTimeRelative() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void resetPlayerTime() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void giveExp(int amount) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public float getExp() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setExp(float exp) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getExperience() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setExperience(int exp) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getLevel() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setLevel(int level) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getTotalExperience() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setTotalExperience(int exp) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public float getExhaustion() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setExhaustion(float value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public float getSaturation() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setSaturation(float value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getFoodLevel() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setFoodLevel(int value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Location getBedSpawnLocation() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public PlayerInventory getInventory() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ItemStack getItemInHand() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setItemInHand(ItemStack item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isSleeping() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getSleepTicks() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public GameMode getGameMode() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setGameMode(GameMode mode) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getHealth() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setHealth(int health) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getMaxHealth() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public double getEyeHeight() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public double getEyeHeight(boolean ignoreSneaking) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Location getEyeLocation() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Block> getLineOfSight(HashSet<Byte> transparent, int maxDistance) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Block getTargetBlock(HashSet<Byte> transparent, int maxDistance) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Block> getLastTwoTargetBlocks(HashSet<Byte> transparent, int maxDistance) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Egg throwEgg() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Snowball throwSnowball() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Arrow shootArrow() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isInsideVehicle() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean leaveVehicle() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Vehicle getVehicle() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getRemainingAir() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setRemainingAir(int ticks) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getMaximumAir() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setMaximumAir(int ticks) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void damage(int amount) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void damage(int amount, Entity source) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getMaximumNoDamageTicks() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setMaximumNoDamageTicks(int ticks) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getLastDamage() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setLastDamage(int damage) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getNoDamageTicks() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setNoDamageTicks(int ticks) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Player getKiller() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Location getLocation() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setVelocity(Vector velocity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Vector getVelocity() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public World getWorld() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean teleport(Location location) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean teleport(Location location, TeleportCause cause) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean teleport(Entity destination) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean teleport(Entity destination, TeleportCause cause) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Entity> getNearbyEntities(double x, double y, double z) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getEntityId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getFireTicks() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getMaxFireTicks() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setFireTicks(int ticks) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void remove() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isDead() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Server getServer() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Entity getPassenger() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean setPassenger(Entity passenger) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isEmpty() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean eject() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public float getFallDistance() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setFallDistance(float distance) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setLastDamageCause(EntityDamageEvent event) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public EntityDamageEvent getLastDamageCause() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public UUID getUniqueId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getTicksLived() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setTicksLived(int value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isPermissionSet(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isPermissionSet(Permission perm) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean hasPermission(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean hasPermission(Permission perm) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public PermissionAttachment addAttachment(Plugin plugin) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void removeAttachment(PermissionAttachment attachment) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void recalculatePermissions() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isOp() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setOp(boolean value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void sendMessage(String message) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void sendMessage(String[] messages) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isOnline() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isBanned() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setBanned(boolean banned) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isWhitelisted() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setWhitelisted(boolean value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Player getPlayer() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public long getFirstPlayed() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public long getLastPlayed() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean hasPlayedBefore() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Map<String, Object> serialize() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void sendPluginMessage(Plugin source, String channel, byte[] message) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Set<String> getListeningPluginChannels() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void playEffect(EntityEffect type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean getAllowFlight() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setAllowFlight(boolean flight) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setBedSpawnLocation(Location location) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void hidePlayer(Player player) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void showPlayer(Player player) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean canSee(Player player) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean addPotionEffect(PotionEffect effect) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean addPotionEffect(PotionEffect effect, boolean force) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean addPotionEffects(Collection<PotionEffect> effects) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean hasPotionEffect(PotionEffectType type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void removePotionEffect(PotionEffectType type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Collection<PotionEffect> getActivePotionEffects() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public EntityType getType() {
        return EntityType.PLAYER;
    }

    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<MetadataValue> getMetadata(String metadataKey) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean hasMetadata(String metadataKey) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public <T> void playEffect(Location loc, Effect effect, T data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public InventoryView getOpenInventory() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public InventoryView openInventory(Inventory inventory) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public InventoryView openWorkbench(Location location, boolean force) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void openInventory(InventoryView inventory) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void closeInventory() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public InventoryView openEnchanting(Location location, boolean force) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ItemStack getItemOnCursor() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setItemOnCursor(ItemStack item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean setWindowProperty(Property prop, int value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isConversing() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void acceptConversationInput(String input) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean beginConversation(Conversation conversation) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void abandonConversation(Conversation conversation) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void abandonConversation(Conversation conversation, ConversationAbandonedEvent details) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isBlocking() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
