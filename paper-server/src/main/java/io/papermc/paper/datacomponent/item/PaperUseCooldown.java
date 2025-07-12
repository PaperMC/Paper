package io.papermc.paper.datacomponent.item;

import io.papermc.paper.adventure.PaperAdventure;
import java.util.Optional;
import net.kyori.adventure.key.Key;
import net.minecraft.resources.ResourceLocation;
import org.bukkit.craftbukkit.util.Handleable;
import org.jspecify.annotations.Nullable;

public record PaperUseCooldown(
    net.minecraft.world.item.component.UseCooldown impl
) implements UseCooldown, Handleable<net.minecraft.world.item.component.UseCooldown> {

    @Override
    public net.minecraft.world.item.component.UseCooldown getHandle() {
        return this.impl;
    }

    @Override
    public float seconds() {
        return this.impl.seconds();
    }

    @Override
    public @Nullable Key cooldownGroup() {
        return this.impl.cooldownGroup()
            .map(PaperAdventure::asAdventure)
            .orElse(null);
    }

    static final class BuilderImpl implements Builder {

        private final float seconds;
        private Optional<ResourceLocation> cooldownGroup = Optional.empty();

        BuilderImpl(final float seconds) {
            this.seconds = seconds;
        }

        @Override
        public Builder cooldownGroup(@Nullable final Key key) {
            this.cooldownGroup = Optional.ofNullable(key)
                .map(PaperAdventure::asVanilla);

            return this;
        }

        @Override
        public UseCooldown build() {
            return new PaperUseCooldown(
                new net.minecraft.world.item.component.UseCooldown(this.seconds, this.cooldownGroup)
            );
        }
    }
}
