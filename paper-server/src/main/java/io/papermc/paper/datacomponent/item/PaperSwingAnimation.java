package io.papermc.paper.datacomponent.item;

import net.minecraft.world.item.SwingAnimationType;
import org.bukkit.craftbukkit.util.Handleable;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record PaperSwingAnimation(
        net.minecraft.world.item.component.SwingAnimation impl
) implements SwingAnimation, Handleable<net.minecraft.world.item.component.SwingAnimation> {

    private static SwingAnimation.Animation fromNms(final SwingAnimationType nms) {
        return switch (nms) {
            case NONE -> SwingAnimation.Animation.NONE;
            case WHACK -> SwingAnimation.Animation.WHACK;
            case STAB -> SwingAnimation.Animation.STAB;
        };
    }

    private static SwingAnimationType toNms(final SwingAnimation.Animation api) {
        return switch (api) {
            case NONE -> SwingAnimationType.NONE;
            case WHACK -> SwingAnimationType.WHACK;
            case STAB -> SwingAnimationType.STAB;
        };
    }

    @Override
    public net.minecraft.world.item.component.SwingAnimation getHandle() {
        return this.impl;
    }

    @Override
    public SwingAnimation.Animation type() {
        return fromNms(this.impl.type());
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
            this.type = toNms(type);
            return this;
        }

        @Override
        public SwingAnimation.Builder duration(final int duration) {
            com.google.common.base.Preconditions.checkArgument(duration >= 0, "duration must be >= 0");
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