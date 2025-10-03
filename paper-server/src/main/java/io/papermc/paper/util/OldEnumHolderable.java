package io.papermc.paper.util;

import com.google.common.base.Preconditions;
import io.papermc.paper.registry.HolderableBase;
import java.util.Locale;
import net.minecraft.core.Holder;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.util.OldEnum;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@SuppressWarnings({"removal", "DeprecatedIsStillUsed"})
@Deprecated
@NullMarked
public abstract class OldEnumHolderable<A extends OldEnum<A>, M> extends HolderableBase<M> implements Holderable<M>, OldEnum<A>, Keyed {

    private final int ordinal;
    private final @Nullable String name;

    protected OldEnumHolderable(final Holder<M> holder, final int ordinal) {
        super(holder);
        this.ordinal = ordinal;
        if (holder instanceof final Holder.Reference<M> reference) {
            // For backwards compatibility, minecraft values will stile return the uppercase name without the namespace,
            // in case plugins use for example the name as key in a config file to receive registry item specific values.
            // Custom registry items will return the key with namespace. For a plugin this should look than like a new registry item
            // (which can always be added in new minecraft versions and the plugin should therefore handle it accordingly).
            if (NamespacedKey.MINECRAFT.equals(reference.key().location().getNamespace())) {
                this.name = reference.key().location().getPath().toUpperCase(Locale.ROOT);
            } else {
                this.name = reference.key().location().toString();
            }
        } else {
            this.name = null;
        }
    }

    @Override
    @Deprecated
    public int compareTo(final A other) {
        this.checkIsReference();
        return this.ordinal - other.ordinal();
    }

    @Override
    @Deprecated
    public String name() {
        this.checkIsReference();
        return this.name;
    }

    @Override
    @Deprecated
    public int ordinal() {
        this.checkIsReference();
        return this.ordinal;
    }

    private void checkIsReference() {
        Preconditions.checkState(this.holder.kind() == Holder.Kind.REFERENCE, "Cannot call method for this registry item, because it is not registered.");
    }

    @Override
    public String toString() {
        return this.implToString();
    }
}
