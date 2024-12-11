package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import java.util.Locale;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.entity.Villager;
import org.bukkit.entity.ZombieVillager;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityTransformEvent;

// Paper start
import com.destroystokyo.paper.entity.villager.Reputation;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.UUID;
// Paper end

public class CraftVillager extends CraftAbstractVillager implements Villager {

    public CraftVillager(CraftServer server, net.minecraft.world.entity.npc.Villager entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.npc.Villager getHandle() {
        return (net.minecraft.world.entity.npc.Villager) this.entity;
    }

    @Override
    public String toString() {
        return "CraftVillager";
    }

    @Override
    public void remove() {
        this.getHandle().releaseAllPois();

        super.remove();
    }

    @Override
    public Profession getProfession() {
        return CraftProfession.minecraftToBukkit(this.getHandle().getVillagerData().getProfession());
    }

    @Override
    public void setProfession(Profession profession) {
        Preconditions.checkArgument(profession != null, "Profession cannot be null");
        this.getHandle().setVillagerData(this.getHandle().getVillagerData().setProfession(CraftProfession.bukkitToMinecraft(profession)));
    }

    @Override
    public Type getVillagerType() {
        return CraftType.minecraftToBukkit(this.getHandle().getVillagerData().getType());
    }

    @Override
    public void setVillagerType(Type type) {
        Preconditions.checkArgument(type != null, "Type cannot be null");
        this.getHandle().setVillagerData(this.getHandle().getVillagerData().setType(CraftType.bukkitToMinecraft(type)));
    }

    @Override
    public int getVillagerLevel() {
        return this.getHandle().getVillagerData().getLevel();
    }

    @Override
    public void setVillagerLevel(int level) {
        Preconditions.checkArgument(1 <= level && level <= 5, "level (%s) must be between [1, 5]", level);

        this.getHandle().setVillagerData(this.getHandle().getVillagerData().setLevel(level));
    }

    @Override
    public int getVillagerExperience() {
        return this.getHandle().getVillagerXp();
    }

    @Override
    public void setVillagerExperience(int experience) {
        Preconditions.checkArgument(experience >= 0, "Experience (%s) must be positive", experience);

        this.getHandle().setVillagerXp(experience);
    }

    // Paper start
    @Override
    public boolean increaseLevel(int amount) {
        Preconditions.checkArgument(amount > 0, "Level earned must be positive");
        int supposedFinalLevel = this.getVillagerLevel() + amount;
        Preconditions.checkArgument(net.minecraft.world.entity.npc.VillagerData.MIN_VILLAGER_LEVEL <= supposedFinalLevel && supposedFinalLevel <= net.minecraft.world.entity.npc.VillagerData.MAX_VILLAGER_LEVEL,
            "Final level reached after the donation (%d) must be between [%d, %d]".formatted(supposedFinalLevel, net.minecraft.world.entity.npc.VillagerData.MIN_VILLAGER_LEVEL, net.minecraft.world.entity.npc.VillagerData.MAX_VILLAGER_LEVEL));

        it.unimi.dsi.fastutil.ints.Int2ObjectMap<net.minecraft.world.entity.npc.VillagerTrades.ItemListing[]> trades =
            net.minecraft.world.entity.npc.VillagerTrades.TRADES.get(this.getHandle().getVillagerData().getProfession());

        if (trades == null || trades.isEmpty()) {
            this.getHandle().setVillagerData(this.getHandle().getVillagerData().setLevel(supposedFinalLevel));
            return false;
        }

        while (amount > 0) {
            this.getHandle().increaseMerchantCareer();
            amount--;
        }
        return true;
    }

    @Override
    public boolean addTrades(int amount) {
        Preconditions.checkArgument(amount > 0, "Number of trades unlocked must be positive");
        return this.getHandle().updateTrades(amount);
    }

    @Override
    public int getRestocksToday() {
        return getHandle().numberOfRestocksToday;
    }

    @Override
    public void setRestocksToday(int restocksToday) {
        getHandle().numberOfRestocksToday = restocksToday;
    }
    // Paper end

    @Override
    public boolean sleep(Location location) {
        Preconditions.checkArgument(location != null, "Location cannot be null");
        Preconditions.checkArgument(location.getWorld() != null, "Location needs to be in a world");
        Preconditions.checkArgument(location.getWorld().equals(this.getWorld()), "Cannot sleep across worlds");
        Preconditions.checkState(!this.getHandle().generation, "Cannot sleep during world generation");

        BlockPos position = CraftLocation.toBlockPosition(location);
        BlockState iblockdata = this.getHandle().level().getBlockState(position);
        if (!(iblockdata.getBlock() instanceof BedBlock)) {
            return false;
        }

        this.getHandle().startSleeping(position);
        return true;
    }

    @Override
    public void wakeup() {
        Preconditions.checkState(this.isSleeping(), "Cannot wakeup if not sleeping");
        Preconditions.checkState(!this.getHandle().generation, "Cannot wakeup during world generation");

        this.getHandle().stopSleeping();
    }

    @Override
    public void shakeHead() {
        this.getHandle().setUnhappy();
    }

    @Override
    public ZombieVillager zombify() {
        net.minecraft.world.entity.monster.ZombieVillager entityzombievillager = Zombie.convertVillagerToZombieVillager(this.getHandle().level().getMinecraftWorld(), this.getHandle(), this.getHandle().blockPosition(), this.isSilent(), EntityTransformEvent.TransformReason.INFECTION, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return (entityzombievillager != null) ? (ZombieVillager) entityzombievillager.getBukkitEntity() : null;
    }

    public static class CraftType implements Type, Handleable<VillagerType> {
        private static int count = 0;

        public static Type minecraftToBukkit(VillagerType minecraft) {
            return CraftRegistry.minecraftToBukkit(minecraft, Registries.VILLAGER_TYPE, Registry.VILLAGER_TYPE);
        }

        public static VillagerType bukkitToMinecraft(Type bukkit) {
            return CraftRegistry.bukkitToMinecraft(bukkit);
        }

        private final NamespacedKey key;
        private final VillagerType villagerType;
        private final String name;
        private final int ordinal;

        public CraftType(NamespacedKey key, VillagerType villagerType) {
            this.key = key;
            this.villagerType = villagerType;
            // For backwards compatibility, minecraft values will still return the uppercase name without the namespace,
            // in case plugins use for example the name as key in a config file to receive type specific values.
            // Custom types will return the key with namespace. For a plugin this should look than like a new type
            // (which can always be added in new minecraft versions and the plugin should therefore handle it accordingly).
            if (NamespacedKey.MINECRAFT.equals(key.getNamespace())) {
                this.name = key.getKey().toUpperCase(Locale.ROOT);
            } else {
                this.name = key.toString();
            }
            this.ordinal = CraftType.count++;
        }

        @Override
        public VillagerType getHandle() {
            return this.villagerType;
        }

        @Override
        public NamespacedKey getKey() {
            return this.key;
        }

        @Override
        public int compareTo(Type type) {
            return this.ordinal - type.ordinal();
        }

        @Override
        public String name() {
            return this.name;
        }

        @Override
        public int ordinal() {
            return this.ordinal;
        }

        @Override
        public String toString() {
            // For backwards compatibility
            return this.name();
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }

            if (!(other instanceof CraftType)) {
                return false;
            }

            return this.getKey().equals(((Type) other).getKey());
        }

        @Override
        public int hashCode() {
            return this.getKey().hashCode();
        }
    }

    public static class CraftProfession implements Profession, Handleable<VillagerProfession> {
        private static int count = 0;

        public static Profession minecraftToBukkit(VillagerProfession minecraft) {
            return CraftRegistry.minecraftToBukkit(minecraft, Registries.VILLAGER_PROFESSION, Registry.VILLAGER_PROFESSION);
        }

        public static VillagerProfession bukkitToMinecraft(Profession bukkit) {
            return CraftRegistry.bukkitToMinecraft(bukkit);
        }

        private final NamespacedKey key;
        private final VillagerProfession villagerProfession;
        private final String name;
        private final int ordinal;

        public CraftProfession(NamespacedKey key, VillagerProfession villagerProfession) {
            this.key = key;
            this.villagerProfession = villagerProfession;
            // For backwards compatibility, minecraft values will still return the uppercase name without the namespace,
            // in case plugins use for example the name as key in a config file to receive profession specific values.
            // Custom professions will return the key with namespace. For a plugin this should look than like a new profession
            // (which can always be added in new minecraft versions and the plugin should therefore handle it accordingly).
            if (NamespacedKey.MINECRAFT.equals(key.getNamespace())) {
                this.name = key.getKey().toUpperCase(Locale.ROOT);
            } else {
                this.name = key.toString();
            }
            this.ordinal = CraftProfession.count++;
        }

        @Override
        public VillagerProfession getHandle() {
            return this.villagerProfession;
        }

        @Override
        public NamespacedKey getKey() {
            return this.key;
        }

        @Override
        public int compareTo(Profession profession) {
            return this.ordinal - profession.ordinal();
        }

        @Override
        public String name() {
            return this.name;
        }

        @Override
        public int ordinal() {
            return this.ordinal;
        }

        @Override
        public String toString() {
            // For backwards compatibility
            return this.name();
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }

            if (!(other instanceof CraftProfession)) {
                return false;
            }

            return this.getKey().equals(((Profession) other).getKey());
        }

        @Override
        public int hashCode() {
            return this.getKey().hashCode();
        }
    }

    // Paper start - Add villager reputation API
    @Override
    public Reputation getReputation(UUID uniqueId) {
        net.minecraft.world.entity.ai.gossip.GossipContainer.EntityGossips rep = getHandle().getGossips().gossips.get(uniqueId);
        if (rep == null) {
            return new Reputation(new java.util.EnumMap<>(com.destroystokyo.paper.entity.villager.ReputationType.class));
        }

        return rep.getPaperReputation();
    }

    @Override
    public Map<UUID, Reputation> getReputations() {
        return getHandle().getGossips().gossips.entrySet()
            .stream()
            .collect(java.util.stream.Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getPaperReputation()));
    }

    @Override
    public void setReputation(UUID uniqueId, Reputation reputation) {
        net.minecraft.world.entity.ai.gossip.GossipContainer.EntityGossips nmsReputation =
            getHandle().getGossips().gossips.computeIfAbsent(
                uniqueId,
                key -> new net.minecraft.world.entity.ai.gossip.GossipContainer.EntityGossips()
            );
        nmsReputation.assignFromPaperReputation(reputation);
    }

    @Override
    public void setReputations(Map<UUID, Reputation> reputations) {
        for (Map.Entry<UUID, Reputation> entry : reputations.entrySet()) {
            setReputation(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clearReputations() {
        getHandle().getGossips().gossips.clear();
    }
    // Paper end
}
