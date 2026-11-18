package com.destroystokyo.paper.profile;

import com.google.common.base.Preconditions;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

/**
 * Represents a property on a {@link PlayerProfile}.
 */
public final class ProfileProperty {

    private final String name;
    private final String value;
    private final @Nullable String signature;

    /**
     * Creates an unsigned property.
     *
     * @param name  the property name
     * @param value the property value
     */
    public ProfileProperty(final String name, final String value) {
        this(name, value, null);
    }

    /**
     * Creates a property.
     *
     * @param name      the property name, at most 64 characters
     * @param value     the property value, at most 32767 characters
     * @param signature the signature, at most 1024 characters, or {@code null} if unsigned
     */
    public ProfileProperty(final String name, final String value, final @Nullable String signature) {
        this.name = Preconditions.checkNotNull(name, "ProfileProperty name can not be null");
        this.value = Preconditions.checkNotNull(value, "ProfileProperty value can not be null");
        this.signature = signature;
        Preconditions.checkArgument(name.length() <= 64, "ProfileProperty name can not be longer than 64 characters");
        Preconditions.checkArgument(value.length() <= Short.MAX_VALUE, "ProfileProperty value can not be longer than 32767 characters");
        Preconditions.checkArgument(signature == null || signature.length() <= 1024, "ProfileProperty signature can not be longer than 1024 characters");
    }

    /**
     * Gets the name of this property.
     *
     * @return The property name, ie "textures"
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the value of this property.
     *
     * @return The property value, likely to be base64 encoded
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Gets the signature of this property.
     *
     * @return A signature for signed properties
     */
    public @Nullable String getSignature() {
        return this.signature;
    }

    /**
     * Checks whether this property is signed.
     *
     * @return If this property has a signature or not
     */
    public boolean isSigned() {
        return this.signature != null;
    }

    @Override
    public boolean equals(final @Nullable Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        final ProfileProperty that = (ProfileProperty) o;
        return Objects.equals(this.name, that.name) &&
            Objects.equals(this.value, that.value) &&
            Objects.equals(this.signature, that.signature);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name);
    }
}
