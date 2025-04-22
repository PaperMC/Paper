package org.bukkit.craftbukkit.block;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import net.minecraft.advancements.critereon.DataComponentMatchers;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.component.DataComponentExactPredicate;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.LockCode;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Beacon;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.potion.CraftPotionEffectType;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CraftBeacon extends CraftBlockEntityState<BeaconBlockEntity> implements Beacon {

    public CraftBeacon(World world, BeaconBlockEntity blockEntity) {
        super(world, blockEntity);
    }

    protected CraftBeacon(CraftBeacon state, Location location) {
        super(state, location);
    }

    @Override
    public Collection<LivingEntity> getEntitiesInRange() {
        this.ensureNoWorldGeneration();

        BlockEntity blockEntity = this.getBlockEntityFromWorld();
        if (blockEntity instanceof BeaconBlockEntity) {
            BeaconBlockEntity beacon = (BeaconBlockEntity) blockEntity;

            Collection<Player> nms = BeaconBlockEntity.getHumansInRange(beacon.getLevel(), beacon.getBlockPos(), beacon.levels, beacon); // Paper - Custom beacon ranges
            Collection<LivingEntity> bukkit = new ArrayList<>(nms.size());

            for (Player human : nms) {
                bukkit.add(human.getBukkitEntity());
            }

            return bukkit;
        }

        // block is no longer a beacon
        return new ArrayList<>();
    }

    @Override
    public int getTier() {
        return this.getSnapshot().levels;
    }

    @Override
    public PotionEffect getPrimaryEffect() {
        return this.getSnapshot().getPrimaryEffect();
    }

    @Override
    public void setPrimaryEffect(PotionEffectType effect) {
        this.getSnapshot().primaryPower = (effect != null) ? CraftPotionEffectType.bukkitToMinecraftHolder(effect) : null;
    }

    @Override
    public PotionEffect getSecondaryEffect() {
        return this.getSnapshot().getSecondaryEffect();
    }

    @Override
    public void setSecondaryEffect(PotionEffectType effect) {
        this.getSnapshot().secondaryPower = (effect != null) ? CraftPotionEffectType.bukkitToMinecraftHolder(effect) : null;
    }

    @Override
    public net.kyori.adventure.text.Component customName() {
        final BeaconBlockEntity beacon = this.getSnapshot();
        return beacon.name != null ? io.papermc.paper.adventure.PaperAdventure.asAdventure(beacon.name) : null;
    }

    @Override
    public void customName(final net.kyori.adventure.text.Component customName) {
        this.getSnapshot().setCustomName(customName != null ? io.papermc.paper.adventure.PaperAdventure.asVanilla(customName) : null);
    }

    @Override
    public String getCustomName() {
        BeaconBlockEntity beacon = this.getSnapshot();
        return beacon.name != null ? CraftChatMessage.fromComponent(beacon.name) : null;
    }

    @Override
    public void setCustomName(String name) {
        this.getSnapshot().setCustomName(CraftChatMessage.fromStringOrNull(name));
    }

    @Override
    public boolean isLocked() {
        return this.getSnapshot().lockKey != LockCode.NO_LOCK;
    }

    @Override
    public String getLock() {
        Optional<? extends Component> customName = this.getSnapshot().lockKey.predicate().components().exact().asPatch().get(DataComponents.CUSTOM_NAME);

        return (customName != null) ? customName.map(CraftChatMessage::fromComponent).orElse("") : "";
    }

    @Override
    public void setLock(String key) {
        if (key == null) {
            this.getSnapshot().lockKey = LockCode.NO_LOCK;
        } else {
            DataComponentExactPredicate predicate = DataComponentExactPredicate.builder().expect(DataComponents.CUSTOM_NAME, CraftChatMessage.fromStringOrNull(key)).build();
            this.getSnapshot().lockKey = new LockCode(new ItemPredicate(Optional.empty(), MinMaxBounds.Ints.ANY, new DataComponentMatchers(predicate, Collections.emptyMap())));
        }
    }

    @Override
    public void setLockItem(ItemStack key) {
        if (key == null) {
            this.getSnapshot().lockKey = LockCode.NO_LOCK;
        } else {
            this.getSnapshot().lockKey = new LockCode(CraftItemStack.asCriterionConditionItem(key));
        }
    }

    @Override
    public CraftBeacon copy() {
        return new CraftBeacon(this, null);
    }

    @Override
    public CraftBeacon copy(Location location) {
        return new CraftBeacon(this, location);
    }

    // Paper start
    @Override
    public double getEffectRange() {
        return this.getSnapshot().getEffectRange();
    }

    @Override
    public void setEffectRange(double range) {
        this.getSnapshot().setEffectRange(range);
    }

    @Override
    public void resetEffectRange() {
        this.getSnapshot().resetEffectRange();
    }
    // Paper end
}
