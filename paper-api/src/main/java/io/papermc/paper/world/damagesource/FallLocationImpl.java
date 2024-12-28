package io.papermc.paper.world.damagesource;

import org.jspecify.annotations.NullMarked;

@NullMarked
record FallLocationImpl(String id) implements FallLocation {

    @Override
    public String translationKey() {
        // Same as net.minecraft.world.damagesource.FallLocation#languageKey
        return "death.fell.accident." + this.id;
    }

}
