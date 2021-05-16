package io.papermc.paper.datapack;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@ApiStatus.Internal
@NullMarked
record DatapackSourceImpl(String name) implements DatapackSource {

    @Override
    public String toString() {
        return this.name;
    }
}
