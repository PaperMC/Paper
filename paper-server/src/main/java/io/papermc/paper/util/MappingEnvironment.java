package io.papermc.paper.util;

import java.io.InputStream;
import java.util.Objects;
import java.util.jar.Manifest;
import net.minecraft.world.entity.MobCategory;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public final class MappingEnvironment {
    public static final boolean DISABLE_PLUGIN_REMAPPING = Boolean.getBoolean("paper.disablePluginRemapping");
    public static final String LEGACY_CB_VERSION = "v1_21_R6";
    private static final @Nullable String MAPPINGS_HASH = readMappingsHash();
    private static final boolean REOBF = checkReobf();

    private MappingEnvironment() {
    }

    public static boolean reobf() {
        return REOBF;
    }

    public static boolean hasMappings() {
        return MAPPINGS_HASH != null;
    }

    public static InputStream mappingsStream() {
        return Objects.requireNonNull(mappingsStreamIfPresent(), "Missing mappings!");
    }

    public static @Nullable InputStream mappingsStreamIfPresent() {
        return MappingEnvironment.class.getClassLoader().getResourceAsStream("META-INF/mappings/reobf.tiny");
    }

    public static String mappingsHash() {
        return Objects.requireNonNull(MAPPINGS_HASH, "MAPPINGS_HASH");
    }

    private static @Nullable String readMappingsHash() {
        final @Nullable Manifest manifest = JarManifests.manifest(MappingEnvironment.class);
        if (manifest != null) {
            final Object hash = manifest.getMainAttributes().getValue("Included-Mappings-Hash");
            if (hash != null) {
                return hash.toString();
            }
        }

        final @Nullable InputStream stream = mappingsStreamIfPresent();
        if (stream == null) {
            return null;
        }
        return Hashing.sha256(stream);
    }

    @SuppressWarnings("ConstantConditions")
    private static boolean checkReobf() {
        final Class<?> clazz = MobCategory.class;
        if (clazz.getSimpleName().equals("MobCategory")) {
            return false;
        } else if (clazz.getSimpleName().equals("EnumCreatureType")) {
            return true;
        }
        throw new IllegalStateException();
    }
}
