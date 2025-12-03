package io.papermc.paper.datacomponent.item;

import io.papermc.paper.registry.data.util.Checks;
import org.bukkit.craftbukkit.util.Handleable;

import static io.papermc.paper.registry.data.util.Checks.requireArgumentNonNegative;

public record PaperWeapon(
    net.minecraft.world.item.component.Weapon impl
) implements Weapon, Handleable<net.minecraft.world.item.component.Weapon> {

    @Override
    public net.minecraft.world.item.component.Weapon getHandle() {
        return this.impl;
    }

    @Override
    public int itemDamagePerAttack() {
        return this.impl.itemDamagePerAttack();
    }

    @Override
    public float disableBlockingForSeconds() {
        return this.impl.disableBlockingForSeconds();
    }

    static final class BuilderImpl implements Builder {

        private int itemDamagePerAttack = 1;
        private float disableBlockingForSeconds;

        @Override
        public Builder itemDamagePerAttack(final int damage) {
            this.itemDamagePerAttack = Checks.requireArgumentNonNegative(damage, "damage");
            return this;
        }

        @Override
        public Builder disableBlockingForSeconds(final float seconds) {
            this.disableBlockingForSeconds = requireArgumentNonNegative(seconds, "seconds");
            return this;
        }

        @Override
        public Weapon build() {
            return new PaperWeapon(new net.minecraft.world.item.component.Weapon(
                this.itemDamagePerAttack,
                this.disableBlockingForSeconds
            ));
        }
    }
}
