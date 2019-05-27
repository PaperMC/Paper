package io.papermc.paper;

import java.time.Instant;
import java.util.Optional;
import java.util.OptionalInt;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

public class TestServerBuildInfo implements ServerBuildInfo {
    @Override
    public @NotNull Key brandId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isBrandCompatible(final @NotNull Key brandId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull String brandName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull String minecraftVersionId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull String minecraftVersionName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull OptionalInt buildNumber() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Instant buildTime() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Optional<String> gitBranch() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Optional<String> gitCommit() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull String asString(final @NotNull StringRepresentation representation) {
        return "";
    }
}
