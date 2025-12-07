package io.papermc.paper.datacomponent.item;

import com.google.common.base.Preconditions;
import net.minecraft.world.item.SwingAnimationType;
import org.bukkit.craftbukkit.util.Handleable;

public record PaperSwingAnimation(
    net.minecraft.world.item.component.SwingAnimation impl
) implements SwingAnimation, Handleable<net.minecraft.world.item.component.SwingAnimation> {

    @Override
    public net.minecraft.world.item.component.SwingAnimation getHandle() {
        return this.impl;
    }

    @Override
    public Animation type() {
        return Animation.valueOf(this.impl.type().name());
    }

    @Override
    public int duration() {
        return this.impl.duration();
    }

    static final class BuilderImpl implements SwingAnimation.Builder {

        private SwingAnimationType type = net.minecraft.world.item.component.SwingAnimation.DEFAULT.type();
        private int duration = net.minecraft.world.item.component.SwingAnimation.DEFAULT.duration();

        @Override
        public SwingAnimation.Builder type(final SwingAnimation.Animation type) {
            this.type = SwingAnimationType.valueOf(type.name());
            return this;
        }

        @Override
        public SwingAnimation.Builder duration(final int duration) {
            Preconditions.checkArgument(duration > 0, "duration must be positive");
            this.duration = duration;
            return this;
        }

        @Override
        public SwingAnimation build() {
            return new PaperSwingAnimation(
                new net.minecraft.world.item.component.SwingAnimation(
                    this.type,
                    this.duration
                )
            );
        }
    }
}
