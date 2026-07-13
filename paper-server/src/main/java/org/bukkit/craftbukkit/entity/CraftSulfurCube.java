package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import io.papermc.paper.entity.PaperBucketable;
import io.papermc.paper.entity.PaperShearable;
import io.papermc.paper.registry.HolderableBase;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.SulfurCubeArchetype;
import net.minecraft.world.entity.item.PrimedTnt;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.SulfurCube;
import org.bukkit.inventory.ItemStack;

public class CraftSulfurCube extends CraftAbstractCubeMob implements SulfurCube, PaperShearable, PaperBucketable {
    public CraftSulfurCube(final CraftServer server, final net.minecraft.world.entity.monster.cubemob.SulfurCube entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.monster.cubemob.SulfurCube getHandle() {
        return (net.minecraft.world.entity.monster.cubemob.SulfurCube) this.entity;
    }

    @Override
    public int getFuseTicks() {
        return this.getHandle().getFuse();
    }

    @Override
    public void setFuseTicks(final int ticks) {
        Preconditions.checkArgument(ticks == PrimedTnt.NO_FUSE || ticks > 0, "ticks must be positive or %s", PrimedTnt.NO_FUSE);
        this.getHandle().setFuse(ticks);
        this.getHandle().getEntityData().set(net.minecraft.world.entity.monster.cubemob.SulfurCube.MAX_FUSE, ticks);
    }

    @Override
    public boolean canExplode() {
        return this.getHandle().canExplode();
    }

    @Override
    public boolean ignite(final boolean imminent) {
        return this.getHandle().primeTime(imminent);
    }

    @Override
    public boolean equipItem(final ItemStack itemStack) {
        return this.getHandle().equipItem(CraftItemStack.asNMSCopy(itemStack));
    }

    @Override
    public int getAge() {
        return this.getHandle().getAge();
    }

    @Override
    public void setAge(final int age) {
        this.getHandle().setAge(age);
    }

    @Override
    public void setAgeLock(final boolean lock) {
        this.getHandle().setAgeLocked(lock);
    }

    @Override
    public boolean getAgeLock() {
        return this.getHandle().isAgeLocked();
    }

    @Override
    public void setBaby() {
        CraftAgeable.setBaby(this.getHandle(), true);
    }

    @Override
    public void setAdult() {
        CraftAgeable.setBaby(this.getHandle(), false);
    }

    @Override
    public boolean isAdult() {
        return !this.getHandle().isBaby();
    }

    @Override
    public boolean canBreed() {
        return false;
    }

    @Override
    public void setBreed(final boolean breed) {
    }

    public static class CraftArchetype extends HolderableBase<SulfurCubeArchetype> implements Archetype {

        public static Archetype minecraftHolderToBukkit(Holder<SulfurCubeArchetype> minecraft) {
            return CraftRegistry.minecraftHolderToBukkit(minecraft, Registries.SULFUR_CUBE_ARCHETYPE);
        }

        public static Holder<SulfurCubeArchetype> bukkitToMinecraftHolder(Archetype bukkit) {
            return CraftRegistry.bukkitToMinecraftHolder(bukkit);
        }

        public CraftArchetype(final Holder<SulfurCubeArchetype> holder) {
            super(holder);
        }
    }
}
