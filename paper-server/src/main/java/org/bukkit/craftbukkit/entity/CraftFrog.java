package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import java.util.Locale;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.animal.FrogVariant;
import net.minecraft.world.entity.animal.frog.Frog;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.entity.Entity;

public class CraftFrog extends CraftAnimals implements org.bukkit.entity.Frog {

    public CraftFrog(CraftServer server, Frog entity) {
        super(server, entity);
    }

    @Override
    public Frog getHandle() {
        return (Frog) this.entity;
    }

    @Override
    public String toString() {
        return "CraftFrog";
    }

    @Override
    public Entity getTongueTarget() {
        return this.getHandle().getTongueTarget().map(net.minecraft.world.entity.Entity::getBukkitEntity).orElse(null);
    }

    @Override
    public void setTongueTarget(Entity target) {
        if (target == null) {
            this.getHandle().eraseTongueTarget();
        } else {
            this.getHandle().setTongueTarget(((CraftEntity) target).getHandle());
        }
    }

    @Override
    public Variant getVariant() {
        return CraftVariant.minecraftHolderToBukkit(this.getHandle().getVariant());
    }

    @Override
    public void setVariant(Variant variant) {
        Preconditions.checkArgument(variant != null, "variant");

        this.getHandle().setVariant(CraftVariant.bukkitToMinecraftHolder(variant));
    }

    public static class CraftVariant implements Variant, Handleable<FrogVariant> {
        private static int count = 0;

        public static Variant minecraftToBukkit(FrogVariant minecraft) {
            return CraftRegistry.minecraftToBukkit(minecraft, Registries.FROG_VARIANT, Registry.FROG_VARIANT);
        }

        public static Variant minecraftHolderToBukkit(Holder<FrogVariant> minecraft) {
            return CraftVariant.minecraftToBukkit(minecraft.value());
        }

        public static FrogVariant bukkitToMinecraft(Variant bukkit) {
            return CraftRegistry.bukkitToMinecraft(bukkit);
        }

        public static Holder<FrogVariant> bukkitToMinecraftHolder(Variant bukkit) {
            return CraftRegistry.bukkitToMinecraftHolder(bukkit, Registries.FROG_VARIANT);
        }

        private final NamespacedKey key;
        private final FrogVariant frogVariant;
        private final String name;
        private final int ordinal;

        public CraftVariant(NamespacedKey key, FrogVariant frogVariant) {
            this.key = key;
            this.frogVariant = frogVariant;
            // For backwards compatibility, minecraft values will still return the uppercase name without the namespace,
            // in case plugins use for example the name as key in a config file to receive variant specific values.
            // Custom variants will return the key with namespace. For a plugin this should look than like a new variant
            // (which can always be added in new minecraft versions and the plugin should therefore handle it accordingly).
            if (NamespacedKey.MINECRAFT.equals(key.getNamespace())) {
                this.name = key.getKey().toUpperCase(Locale.ROOT);
            } else {
                this.name = key.toString();
            }
            this.ordinal = CraftVariant.count++;
        }

        @Override
        public FrogVariant getHandle() {
            return this.frogVariant;
        }

        @Override
        public NamespacedKey getKey() {
            return this.key;
        }

        @Override
        public int compareTo(Variant variant) {
            return this.ordinal - variant.ordinal();
        }

        @Override
        public String name() {
            return this.name;
        }

        @Override
        public int ordinal() {
            return this.ordinal;
        }

        @Override
        public String toString() {
            // For backwards compatibility
            return this.name();
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }

            if (!(other instanceof CraftVariant)) {
                return false;
            }

            return this.getKey().equals(((Variant) other).getKey());
        }

        @Override
        public int hashCode() {
            return this.getKey().hashCode();
        }
    }
}
