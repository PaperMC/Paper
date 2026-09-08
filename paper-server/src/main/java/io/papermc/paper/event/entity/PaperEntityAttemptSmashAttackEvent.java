package io.papermc.paper.event.entity;

import org.bukkit.craftbukkit.event.entity.CraftEntityEvent;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class PaperEntityAttemptSmashAttackEvent extends CraftEntityEvent implements EntityAttemptSmashAttackEvent {

    private final LivingEntity target;
    private final ItemStack weapon;
    private final boolean originalResult;
    private Result result = Result.DEFAULT;

    public PaperEntityAttemptSmashAttackEvent(final LivingEntity attacker, final LivingEntity target, final ItemStack weapon, final boolean originalResult) {
        super(attacker);
        this.target = target;
        this.weapon = weapon;
        this.originalResult = originalResult;
    }

    public PaperEntityAttemptSmashAttackEvent(
        final net.minecraft.world.entity.LivingEntity attacker,
        final net.minecraft.world.entity.LivingEntity target,
        final net.minecraft.world.item.ItemStack weapon,
        final boolean originalResult
    ) {
        this(attacker.getBukkitEntity(), target.getBukkitEntity(), CraftItemStack.asCraftMirror(weapon), originalResult);
    }

    @Override
    public LivingEntity getTarget() {
        return this.target;
    }

    @Override
    public ItemStack getWeapon() {
        return this.weapon.clone();
    }

    @Override
    public boolean getOriginalResult() {
        return this.originalResult;
    }

    @Override
    public Result getResult() {
        return this.result;
    }

    @Override
    public void setResult(final Result result) {
        this.result = result;
    }

    @Override
    public HandlerList getHandlers() {
        return EntityAttemptSmashAttackEvent.getHandlerList();
    }
}
