package io.papermc.paper.entity;

import com.google.common.base.Preconditions;
import io.papermc.paper.inventory.OfflinePlayerEnderChest;
import io.papermc.paper.inventory.OfflinePlayerEnderChestImpl;
import io.papermc.paper.inventory.OfflinePlayerInventory;
import io.papermc.paper.inventory.OfflinePlayerInventoryImpl;
import io.papermc.paper.math.BlockPosition;
import io.papermc.paper.math.Position;
import net.kyori.adventure.util.TriState;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraft.world.level.storage.PlayerDataStorage;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.attribute.CraftAttributeMap;
import org.bukkit.craftbukkit.persistence.CraftPersistentDataContainer;
import org.bukkit.craftbukkit.persistence.CraftPersistentDataTypeRegistry;
import org.bukkit.craftbukkit.potion.CraftPotionUtil;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public final class PlayerDataFileImpl implements PlayerDataFile{
    public static final PlayerDataStorage playerStorage=MinecraftServer.getServer().playerDataStorage;
    private static final CraftPersistentDataTypeRegistry persistentRegistry=new CraftPersistentDataTypeRegistry();

    private final CompoundTag data;
    private final UUID uuid;

    private Location location;
    private final @Nullable BlockPosition sleepPos;
    private Vector velocity;

    private final OfflinePlayerInventoryImpl inventory;
    private final OfflinePlayerEnderChestImpl enderChest;

    private final Set<String> playerTags=new HashSet<>();
    private final List<PotionEffect> playerEffects=new ArrayList<>();
    private final Set<NamespacedKey> playerRecipes=new HashSet<>();

    private final AttributeMap playerAttributesHandle=new AttributeMap(DefaultAttributes.getSupplier(EntityType.PLAYER));
    private final CraftAttributeMap playerAttributes=new CraftAttributeMap(playerAttributesHandle);

    private final CompoundTag abilityCompound;
    private final CraftPersistentDataContainer customData;

    public static PlayerDataFileImpl load(UUID uuid) throws IOException, PlayerSerializationException {
        File playersDir=playerStorage.getPlayerDir();
        File playerFile=new File(playersDir,uuid.toString()+".dat");
        if(!playerFile.exists()||!playerFile.isFile())throw new PlayerSerializationException("Player file not founded");

        CompoundTag data=NbtIo.readCompressed(playerFile.toPath(), NbtAccounter.unlimitedHeap());
        return new PlayerDataFileImpl(uuid,data);
    }

    public PlayerDataFileImpl(UUID uuid, CompoundTag data) throws PlayerSerializationException {
        this.data=data;
        this.uuid=uuid;

        Optional<ListTag> posTag=data.getList("Pos");
        if(posTag.isEmpty())throw new PlayerSerializationException("Player has no position");
        ListTag pos=posTag.get();

        if(pos.identifyRawElementType()!=Tag.TAG_DOUBLE||pos.size()!=3)throw new PlayerSerializationException("Player position has incorrect format");

        double x=pos.getDoubleOr(0,0);
        double y=pos.getDoubleOr(1,0);
        double z=pos.getDoubleOr(2,0);

        Optional<ListTag> rotTag=data.getList("Rotation");
        if(rotTag.isEmpty())throw new PlayerSerializationException("Player has no rotation");
        ListTag rotation=rotTag.get();

        if(rotation.identifyRawElementType()!=Tag.TAG_FLOAT||rotation.size()!=2)throw new PlayerSerializationException("Player rotation has incorrect format");

        float yaw=rotation.getFloatOr(0,0);
        float pitch=rotation.getFloatOr(1,0);

        World world;

        Optional<Long> worldUUIDMost=data.getLong("WorldUUIDMost");
        Optional<Long> worldUUIDLeast=data.getLong("WorldUUIDLeast");
        Optional<String> worldName=data.getString("world");
        Optional<String> worldID=data.getString("Dimension");

        if(worldUUIDMost.isPresent()&&worldUUIDLeast.isPresent()){
            world=Bukkit.getWorld(new UUID(worldUUIDMost.get(),worldUUIDLeast.get()));
        }else world=worldName.map(Bukkit::getWorld).orElse(null);

        if(world==null&&worldID.isPresent()){
            NamespacedKey id=NamespacedKey.fromString(worldID.get());
            if(id!=null)world=Bukkit.getWorld(id);
        }

        location=new Location(world,x,y,z,yaw,pitch);

        Optional<ListTag> motionTag=data.getList("Motion");
        if(motionTag.isEmpty())throw new PlayerSerializationException("Player has no motion");
        ListTag motion=motionTag.get();

        if(motion.identifyRawElementType()!=Tag.TAG_DOUBLE||pos.size()!=3)throw new PlayerSerializationException("Player motion has incorrect format");

        velocity=new Vector(motion.getDoubleOr(0,0),motion.getDoubleOr(1,0),motion.getDoubleOr(2,0));

        final ListTag inventoryTag = data.getList("Inventory").orElseGet(()->{
            ListTag tg=new ListTag();
            data.put("Inventory",tg);
            return tg;
        });

        final CompoundTag equipmentTag = data.getCompound("equipment").orElseGet(()->{
            CompoundTag tg=new CompoundTag();
            data.put("equipment",tg);
            return tg;
        });

        inventory=new OfflinePlayerInventoryImpl(inventoryTag, equipmentTag,this);

        final ListTag enderChestTag = data.getList("EnderItems").orElseGet(()->{
            ListTag tg=new ListTag();
            data.put("EnderItems",tg);
            return tg;
        });

        enderChest=new OfflinePlayerEnderChestImpl(enderChestTag,this);

        ListTag tagsTag=data.getList("Tags").orElseGet(ListTag::new);

        byte tagsType=tagsTag.identifyRawElementType();
        if(tagsType!=Tag.TAG_END&&tagsType!=Tag.TAG_STRING)throw new PlayerSerializationException("Player tag list has incorrect format");

        tagsTag.stream().map(el->el.asString().get()).forEach(playerTags::add);

        ListTag effectsTag=data.getList("active_effects").orElseGet(ListTag::new);

        byte effectsType=effectsTag.identifyRawElementType();
        if(effectsType!=Tag.TAG_END&&effectsType!=Tag.TAG_COMPOUND)throw new PlayerSerializationException("Player effect list has incorrect format");

        effectsTag.stream()
            .map(tg->MobEffectInstance.CODEC.decode(NbtOps.INSTANCE,tg).getOrThrow(PlayerSerializationException::new).getFirst())
            .map(CraftPotionUtil::toBukkit)
            .forEach(playerEffects::add);

        CompoundTag recipeBookTag=data.getCompound("recipeBook").orElseGet(()->{
            CompoundTag tg=new CompoundTag();
            data.put("recipeBook",tg);
            return tg;
        });

        ListTag recipesTag=recipeBookTag.getList("recipes").orElseGet(ListTag::new);

        byte recipesType=effectsTag.identifyRawElementType();
        if(recipesType!=Tag.TAG_END&&recipesType!=Tag.TAG_STRING)throw new PlayerSerializationException("Player recipe list has incorrect format");

        recipesTag.stream()
            .map(tg->tg.asString().get())
            .map(NamespacedKey::fromString)
            .filter(Objects::nonNull)
            .forEach(playerRecipes::add);

        Optional<int[]> sleepPosTag=data.getIntArray("sleeping_pos");
        if(sleepPosTag.isEmpty())sleepPos=null;
        else{
            int[] sleepPosArr=sleepPosTag.get();

            if(sleepPosArr.length==0)sleepPos=null;
            else if(sleepPosArr.length!=3)throw new PlayerSerializationException("Player sleep pos has incorrect format");
            else sleepPos=Position.block(sleepPosArr[0],sleepPosArr[1],sleepPosArr[2]);
        }

        abilityCompound=data.getCompoundOrEmpty("abilities");

        ListTag attributesTag=data.getList("attributes").orElseGet(ListTag::new);

        byte attributesType=attributesTag.identifyRawElementType();
        if(attributesType!=Tag.TAG_END&&attributesType!=Tag.TAG_COMPOUND)throw new PlayerSerializationException("Player attribute list has incorrect format");

        if(!attributesTag.isEmpty()){
            List<AttributeInstance.Packed> customAttributes=AttributeInstance.Packed.LIST_CODEC
                .decode(NbtOps.INSTANCE,attributesTag)
                .getOrThrow(PlayerSerializationException::new).getFirst();

            playerAttributesHandle.apply(customAttributes);
        }

        CompoundTag customDataTag=data.getCompoundOrEmpty("BukkitValues");
        customData=new CraftPersistentDataContainer(persistentRegistry);
        customData.putAll(customDataTag);
    }

    @Override
    public @NotNull Location getLocation() {
        return location;
    }

    @Override
    public void setLocation(@NotNull final Location value) {
        Preconditions.checkArgument(value!=null,"value cannot be null");
        location=value;
    }

    @Override
    public @NotNull Vector getVelocity() {
        return velocity;
    }

    @Override
    public void setVelocity(@NotNull final Vector value) {
        Preconditions.checkArgument(value!=null,"value cannot be null");
        velocity=value;
    }

    @Override
    public int getFireTicks() {
        return data.getShortOr("Fire",(short)-20);
    }

    @Override
    public void setFireTicks(final int ticks) {
        data.putShort("Fire",(short)ticks);
    }

    @Override
    public @NotNull TriState getVisualFire() {
        Optional<Boolean> tag=data.getBoolean("HasVisualFire");
        return tag.map(val->(val?TriState.TRUE:TriState.FALSE)).orElse(TriState.NOT_SET);
    }

    @Override
    public void setVisualFire(@NotNull final TriState fire) {
        Preconditions.checkArgument(fire!=null,"fire cannot be null");
        if(fire==TriState.NOT_SET)data.remove("HasVisualFire");
        else data.putBoolean("HasVisualFire",fire.toBoolean());
    }

    @Override
    public int getFreezeTicks() {
        return data.getIntOr("TicksFrozen",0);
    }

    @Override
    public void setFreezeTicks(final int ticks) {
        data.putInt("TicksFrozen",ticks);
    }

    @Override
    public float getFallDistance() {
        return (float)data.getDoubleOr("fall_distance",0f);
    }

    @Override
    public void setFallDistance(final float distance) {
        data.putDouble("fall_distance",distance);
    }

    @Override
    public @NotNull UUID getUniqueId() {
        return uuid;
    }

    @Override
    public int getTicksLived() {
        return data.getIntOr("Spigot.ticksLived",0);
    }

    @Override
    public void setTicksLived(final int value) {
        data.putInt("Spigot.ticksLived",value);
    }

    @Override
    public void setInvulnerable(final boolean flag) {
        data.putBoolean("Invulnerable",flag);
    }

    @Override
    public boolean isInvulnerable() {
        return data.getBooleanOr("Invulnerable",false);
    }

    @Override
    public @NotNull Set<String> getScoreboardTags() {
        return Set.copyOf(playerTags);
    }

    @Override
    public boolean addScoreboardTag(@NotNull final String tag) {
        Preconditions.checkArgument(tag!=null,"tag cannot be null");
        return playerTags.add(tag);
    }

    @Override
    public boolean removeScoreboardTag(@NotNull final String tag) {
        Preconditions.checkArgument(tag!=null,"tag cannot be null");
        return playerTags.remove(tag);
    }

    @Override
    public float getHealth() {
        return data.getFloatOr("Health",20);
    }

    @Override
    public void setHealth(final float health) {
        Preconditions.checkArgument(health >= 0 && health <= getMaxHealth(),"Health should be higher or equals 0 and less or equals player max health");
        data.putFloat("Health",health);
    }

    @Override
    public float getAbsorptionAmount() {
        return data.getFloatOr("AbsorptionAmount",0);
    }

    @Override
    public void setAbsorptionAmount(final float amount) {
        Preconditions.checkArgument(amount >= 0 && Float.isFinite(amount), "amount < 0 or non-finite");
        data.putFloat("AbsorptionAmount",amount);
    }

    @Override
    public double getMaxHealth() {
        org.bukkit.attribute.AttributeInstance playerHealth=getAttribute(Attribute.MAX_HEALTH);
        return playerHealth==null?20:playerHealth.getValue();
    }

    @Override
    public int getRemainingAir() {
        return data.getShortOr("Air",(short)300);
    }

    @Override
    public void setRemainingAir(final int ticks) {
        data.putShort("Air",(short)ticks);
    }

    @Override
    public int getNoDamageTicks() {
        return data.getShortOr("HurtTime",(short)0);
    }

    @Override
    public void setNoDamageTicks(final int ticks) {
        data.putShort("HurtTime",(short)ticks);
    }

    @Override
    public boolean addPotionEffect(@NotNull final PotionEffect effect) {
        Preconditions.checkArgument(effect!=null,"effect cannot be null");

        AtomicBoolean stopFlag=new AtomicBoolean();
        List<PotionEffect> toRemove=new ArrayList<>();

        playerEffects.stream()
            .filter(eff->eff.getType().getKey().equals(effect.getType().getKey()))
            .takeWhile(eff->{
                if(eff.getAmplifier()<effect.getAmplifier()){
                    toRemove.add(eff);
                    return true;
                }else if(eff.getAmplifier()==effect.getAmplifier()){
                    if(!eff.isInfinite()&&eff.getDuration()<effect.getDuration()){
                        toRemove.add(eff);
                        return true;
                    }else{
                        stopFlag.set(true);
                        return false;
                    }
                }else{
                    stopFlag.set(true);
                    return false;
                }
            });

        if(stopFlag.get())return false;

        playerEffects.removeAll(toRemove);
        playerEffects.add(effect);

        return true;
    }

    @Override
    public boolean addPotionEffects(@NotNull final Collection<PotionEffect> effects){
        Preconditions.checkArgument(effects!=null,"effects cannot be null");

        boolean out=true;
        for(PotionEffect effect:effects){
            if(effect==null)continue;

            AtomicBoolean stopFlag=new AtomicBoolean();
            List<PotionEffect> toRemove=new ArrayList<>();

            playerEffects.stream()
                .filter(eff->eff.getType().equals(effect.getType()))
                .takeWhile(eff->{
                    if(eff.getAmplifier()<effect.getAmplifier()){
                        toRemove.add(eff);
                        return true;
                    }else if(eff.getAmplifier()==effect.getAmplifier()){
                        if(!eff.isInfinite()&&eff.getDuration()<effect.getDuration()){
                            toRemove.add(eff);
                            return true;
                        }else{
                            stopFlag.set(true);
                            return false;
                        }
                    }else{
                        stopFlag.set(true);
                        return false;
                    }
                });

            if(stopFlag.get())out=false;
            else{
                playerEffects.removeAll(toRemove);
                playerEffects.add(effect);
            }
        }

        return out;
    }

    @Override
    public boolean hasPotionEffect(@NotNull final PotionEffectType type) {
        Preconditions.checkArgument(type!=null,"type cannot be null");
        return playerEffects.stream()
            .anyMatch(eff->eff.getType().equals(type));
    }

    @Override
    public @Nullable PotionEffect getPotionEffect(@NotNull final PotionEffectType type) {
        Preconditions.checkArgument(type!=null,"type cannot be null");
        return playerEffects.stream()
            .filter(eff->eff.getType().equals(type))
            .findAny().orElse(null);
    }

    @Override
    public void removePotionEffect(@NotNull final PotionEffectType type) {
        Preconditions.checkArgument(type!=null,"type cannot be null");
        playerEffects.stream()
            .filter(eff->eff.getType().equals(type))
            .forEach(playerEffects::remove);
    }

    @Override
    public @NotNull Collection<PotionEffect> getActivePotionEffects() {
        return List.copyOf(playerEffects);
    }

    @Override
    public boolean clearActivePotionEffects() {
        if(playerEffects.isEmpty())return false;

        playerEffects.clear();
        return true;
    }

    @Override
    public boolean isSleeping() {
        return data.getShortOr("SleepTimer",(short)0)!=0;
    }

    @Override
    public @Nullable String getName() {
        return data.getCompoundOrEmpty("bukkit").getString("lastKnownName").orElse(null);
    }

    @Override
    public @NotNull OfflinePlayerInventory getInventory() {
        return inventory;
    }

    @Override
    public @NotNull OfflinePlayerEnderChest getEnderChest() {
        return enderChest;
    }

    @Override
    public int getEnchantmentSeed() {
        return data.getIntOr("XpSeed",0);
    }

    @Override
    public void setEnchantmentSeed(final int seed) {
        data.putInt("XpSeed",seed);
    }

    @Override
    public BlockPosition getBedLocation() {
        return sleepPos;
    }

    @Override
    public @NotNull GameMode getGameMode() {
        return GameMode.getByValue(data.getIntOr("playerGameType",0));
    }

    @Override
    public void setGameMode(final @NotNull GameMode mode) {
        data.putInt("playerGameType",mode.getValue());
    }

    @Override
    public @NotNull Set<NamespacedKey> getDiscoveredRecipeSet() {
        return Set.copyOf(playerRecipes);
    }

    @Override
    public boolean discoverRecipe(final @NotNull NamespacedKey recipe) {
        return playerRecipes.add(recipe);
    }

    @Override
    public int discoverRecipes(final Collection<NamespacedKey> recipes) {
        int out=0;

        for(NamespacedKey key:recipes){
            if(playerRecipes.add(key))out++;
        }

        return out;
    }

    @Override
    public boolean undiscoverRecipe(final @NotNull NamespacedKey recipe) {
        return playerRecipes.remove(recipe);
    }

    @Override
    public int undiscoverRecipes(final @NotNull Collection<NamespacedKey> recipes) {
        int out=0;

        for(NamespacedKey key:recipes){
            if(playerRecipes.remove(key))out++;
        }

        return out;
    }

    @Override
    public boolean hasDiscoveredRecipe(final @NotNull NamespacedKey recipe) {
        return playerRecipes.contains(recipe);
    }

    @Override
    public float getExhaustion() {
        return data.getFloatOr("foodExhaustionLevel",0);
    }

    @Override
    public void setExhaustion(final float value) {
        data.putFloat("foodExhaustionLevel",value);
    }

    @Override
    public float getSaturation() {
        return data.getFloatOr("foodSaturationLevel",0);
    }

    @Override
    public void setSaturation(final float value) {
        data.putFloat("foodSaturationLevel",value);
    }

    @Override
    public int getFoodLevel() {
        return data.getIntOr("foodLevel",0);
    }

    @Override
    public void setFoodLevel(final int value) {
        data.putInt("foodLevel",value);
    }

    @Override
    public @Nullable Location getLastDeathLocation() {
        Optional<CompoundTag> deathTag=data.getCompound("LastDeathLocation");
        if(deathTag.isEmpty())return null;

        CompoundTag deathData=deathTag.get();
        Optional<String> dimensionTag=deathData.getString("dimension");
        Optional<int[]> posTag=deathData.getIntArray("pos");

        if(posTag.isEmpty()||dimensionTag.isEmpty())return null;

        int[] pos=posTag.get();
        if(pos.length!=3)return null;

        NamespacedKey dimension=NamespacedKey.fromString(dimensionTag.get());
        if(dimension==null)return null;
        World world=Bukkit.getWorld(dimension);

        return new Location(world,pos[0],pos[1],pos[2]);
    }

    @Override
    public void setLastDeathLocation(@Nullable final Location location) {
        if(location==null){
            data.remove("LastDeathLocation");
            return;
        }

        CompoundTag deathTag=data.getCompound("LastDeathLocation").orElseGet(()->{
            CompoundTag tg=new CompoundTag();
            data.put("LastDeathLocation",tg);
            return tg;
        });

        String dimension=location.getWorld().getKey().toString();
        int[] pos={location.blockX(),location.blockY(),location.blockZ()};

        deathTag.putString("dimension",dimension);
        deathTag.putIntArray("pos",pos);
    }

    @Override
    public boolean isOnGround() {
        return data.getBooleanOr("OnGround",false);
    }

    @Override
    public @Nullable Location getRespawnLocation() {
        Optional<CompoundTag> respawnTag=data.getCompound("respawn");
        if(respawnTag.isEmpty())return null;
        CompoundTag respawn=respawnTag.get();

        Optional<int[]> posTag=respawn.getIntArray("pos");
        float angle=respawn.getFloatOr("angle",0);
        Optional<String> dimensionTag=respawn.getString("dimension");

        if(posTag.isEmpty()||dimensionTag.isEmpty())return null;

        int[] pos=posTag.get();
        if(pos.length!=3)return null;

        NamespacedKey dimension=NamespacedKey.fromString(dimensionTag.get());
        if(dimension==null)return null;
        World world=Bukkit.getWorld(dimension);

        return new Location(world,pos[0],pos[1],pos[2],angle,0);
    }

    @Override
    public void setRespawnLocation(@Nullable final Location location) {
        if(location==null){
            data.remove("respawn");
            return;
        }

        CompoundTag respawn=data.getCompound("respawn").orElseGet(()->{
            CompoundTag tg=new CompoundTag();
            data.put("respawn",tg);
            return tg;
        });

        String dimension=location.getWorld().getKey().toString();
        int[] pos={location.blockX(),location.blockY(),location.blockZ()};

        respawn.putString("dimension",dimension);
        respawn.putIntArray("pos",pos);
        respawn.putFloat("angle",location.getYaw());
    }

    @Override
    public boolean hasSeenWinScreen() {
        return data.getBooleanOr("seenCredits",false);
    }

    @Override
    public void setHasSeenWinScreen(final boolean hasSeenWinScreen) {
        data.putBoolean("seenCredits",hasSeenWinScreen);
    }

    @Override
    public @Nullable GameMode getPreviousGameMode() {
        return data.getInt("previousPlayerGameType").map(GameMode::getByValue).orElse(null);
    }

    @Override
    public float getExp() {
        return data.getFloatOr("XpP",0);
    }

    @Override
    public void setExp(final float exp) {
        Preconditions.checkArgument(exp >= 0.0 && exp <= 1.0, "Experience progress must be between 0.0 and 1.0 (%s)", exp);
        data.putFloat("XpP",exp);
    }

    @Override
    public int getLevel() {
        return data.getIntOr("XpLevel",0);
    }

    @Override
    public void setLevel(final int level) {
        Preconditions.checkArgument(level >= 0, "Experience level must not be negative (%s)", level);
        data.putInt("XpLevel",level);
    }

    @Override
    public int getTotalExperience() {
        return data.getIntOr("XpTotal",0);
    }

    @Override
    public void setTotalExperience(final int exp) {
        Preconditions.checkArgument(exp >= 0, "Total experience points must not be negative (%s)", exp);
        data.putInt("XpTotal",exp);
    }

    @Override
    public @Range(from = 0, to = Integer.MAX_VALUE) int calculateTotalExperiencePoints() {
        return calculateTotalExperiencePoints(getLevel()) + Math.round(getExperiencePointsNeededForNextLevel() * getExp());
    }

    private int calculateTotalExperiencePoints(int level) {
        if (level <= 16) {
            return (int) (Math.pow(level, 2) + 6 * level);
        } else if (level <= 31) {
            return (int) (2.5 * Math.pow(level, 2) - 40.5 * level + 360.0);
        } else {
            return (int) (4.5 * Math.pow(level, 2) - 162.5 * level + 2220.0);
        }
    }

    @Override
    public int getPortalCooldown() {
        return data.getIntOr("PortalCooldown",0);
    }

    @Override
    public void setPortalCooldown(final int cooldown) {
        data.putInt("PortalCooldown",cooldown);
    }

    @Override
    public void setExperienceLevelAndProgress(@Range(from = 0, to = Integer.MAX_VALUE) final int totalExperience) {
        Preconditions.checkArgument(totalExperience >= 0, "Total experience points must not be negative (%s)", totalExperience);
        int level = calculateLevelsForExperiencePoints(totalExperience);
        int remainingPoints = totalExperience - calculateTotalExperiencePoints(level);

        data.putInt("XpLevel",level);
        data.putFloat("XpP",(float) remainingPoints / getExperiencePointsNeededForNextLevel());
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

    @Override
    public int getExperiencePointsNeededForNextLevel() {
        int level=data.getIntOr("XpLevel",0);
        if (level >= 30) {
            return 112 + (level - 30) * 9;
        } else {
            return level >= 15 ? 37 + (level - 15) * 5 : 7 + level * 2;
        }
    }

    @Override
    public boolean getAllowFlight() {
        return abilityCompound.getBooleanOr("mayfly",false);
    }

    @Override
    public void setAllowFlight(final boolean flight) {
        abilityCompound.putBoolean("mayfly",flight);
    }

    @Override
    public boolean isFlying() {
        return abilityCompound.getBooleanOr("flying",false);
    }

    @Override
    public void setFlying(final boolean value) {
        abilityCompound.putBoolean("flying",value);
    }

    @Override
    public void setFlySpeed(final float value) throws IllegalArgumentException {
        abilityCompound.putFloat("flySpeed",value);
    }

    @Override
    public void setWalkSpeed(final float value) throws IllegalArgumentException {
        abilityCompound.putFloat("walkSpeed",value);
    }

    @Override
    public float getFlySpeed() {
        return abilityCompound.getFloatOr("flySpeed",0);
    }

    @Override
    public float getWalkSpeed() {
        return abilityCompound.getFloatOr("walkSpeed",0);
    }

    @Override
    public int getDeathScreenScore() {
        return data.getIntOr("Score",0);
    }

    @Override
    public void setDeathScreenScore(final int score) {
        data.putInt("Score",score);
    }

    @Override
    public int getSelectedHotbarSlot() {
        return data.getIntOr("SelectedItemSlot",0);
    }

    @Override
    public void setSelectedHotbarSlot(final int value) {
        data.putInt("SelectedItemSlot",value);
    }

    @Override
    public @Nullable org.bukkit.attribute.AttributeInstance getAttribute(@NotNull final Attribute attribute) {
        return playerAttributes.getAttribute(attribute);
    }

    @Override
    public void registerAttribute(@NotNull final Attribute attribute) {
        playerAttributes.registerAttribute(attribute);
    }

    @Override
    public @NotNull PersistentDataContainer getPersistentDataContainer() {
        return customData;
    }

    @Override
    public void save() throws IOException, PlayerSerializationException {
        World world= location.getWorld();
        if(world!=null){
            UUID worldId=world.getUID();

            data.putLong("WorldUUIDMost",worldId.getMostSignificantBits());
            data.putLong("WorldUUIDLeast",worldId.getLeastSignificantBits());
            data.putString("Dimension",world.getKey().asString());
        }

        ListTag posTag=new ListTag(List.of(DoubleTag.valueOf(location.x()),DoubleTag.valueOf(location.y()),DoubleTag.valueOf(location.z())));
        data.put("Pos",posTag);

        ListTag rotationTag=new ListTag(List.of(FloatTag.valueOf(location.getYaw()),FloatTag.valueOf(location.getPitch())));
        data.put("Rotation",rotationTag);

        ListTag motionTag=new ListTag(List.of(DoubleTag.valueOf(velocity.getX()),DoubleTag.valueOf(velocity.getY()),DoubleTag.valueOf(velocity.getZ())));
        data.put("Motion",motionTag);

        inventory.save();
        enderChest.save();

        ListTag tagsTag=new ListTag();
        playerTags.stream().map(StringTag::valueOf)
            .forEach(tagsTag::add);
        data.put("Tags",tagsTag);

        ListTag effectsTag=new ListTag();
        playerEffects.stream()
            .map(CraftPotionUtil::fromBukkit)
            .map(eff->MobEffectInstance.CODEC.encode(eff,NbtOps.INSTANCE,new CompoundTag()).getOrThrow(PlayerSerializationException::new))
            .forEach(effectsTag::add);
        data.put("active_effects",effectsTag);

        CompoundTag recipeBookTag=data.getCompoundOrEmpty("recipeBook");
        ListTag recipesTag=new ListTag();
        playerRecipes.stream()
            .map(NamespacedKey::asString).map(StringTag::valueOf)
            .forEach(recipesTag::add);
        recipeBookTag.put("recipes",recipesTag);

        ListTag attributesTag=new ListTag();

        List<AttributeInstance.Packed> customAttributes=playerAttributesHandle.pack();
        if(!customAttributes.isEmpty())AttributeInstance.Packed.LIST_CODEC
            .encode(customAttributes,NbtOps.INSTANCE,attributesTag)
            .getOrThrow(PlayerSerializationException::new);

        data.put("attributes",attributesTag);

        data.put("BukkitValues",customData.toTagCompound());

        Path playersDir=playerStorage.getPlayerDir().toPath();
        String fileName=uuid.toString();

        Path tempFile=Files.createTempFile(playersDir, fileName + "-",".dat");
        NbtIo.writeCompressed(data, tempFile);

        Path file = playersDir.resolve(fileName + ".dat");
        Path backupFile = playersDir.resolve(fileName + ".dat_old");

        Util.safeReplaceFile(file,tempFile,backupFile);
    }
}
