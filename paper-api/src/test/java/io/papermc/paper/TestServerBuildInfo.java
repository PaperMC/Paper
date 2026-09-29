package io.papermc.paper;

import java.time.Instant;
import java.util.Optional;
import java.util.OptionalInt;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class TestServerBuildInfo implements ServerBuildInfo {
    @Override
    public Key brandId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isBrandCompatible(final Key brandId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String brandName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String minecraftVersionId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String minecraftVersionName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public OptionalInt buildNumber() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Instant buildTime() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<String> gitBranch() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<String> gitCommit() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String asString(final StringRepresentation representation) {
        return "";
    }
}
