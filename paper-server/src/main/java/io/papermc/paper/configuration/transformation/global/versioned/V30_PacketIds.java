package io.papermc.paper.configuration.transformation.global.versioned;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;
import java.util.Objects;
import net.minecraft.resources.ResourceLocation;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.NodePath;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;
import org.spongepowered.configurate.transformation.TransformAction;

import static java.util.Objects.requireNonNull;
import static org.spongepowered.configurate.NodePath.path;

public class V30_PacketIds implements TransformAction {

    public static final V30_PacketIds INSTANCE = new V30_PacketIds();
    private static final int VERSION = 30;

    private static final Gson GSON = new Gson();
    private static final Map<String, ResourceLocation> MOJANG_TO_ID;

    static {
        final ImmutableMap.Builder<String, ResourceLocation> builder2 = ImmutableMap.builder();
        final InputStream input = V30_PacketIds.class.getResourceAsStream("/config-data/packet-limiter-upgrade-data.json");
        if (input == null) {
            throw new RuntimeException("Failed to load packet limiter upgrade data");
        }
        try (final Reader reader = new InputStreamReader(new BufferedInputStream(input))) {
            final JsonArray array = GSON.fromJson(reader, JsonArray.class);
            for (final JsonElement element : array) {
                final JsonObject obj = element.getAsJsonObject();
                builder2.put(obj.get("simple_class_name").getAsString(), ResourceLocation.parse(obj.get("id").getAsString()));
            }
        } catch (final IOException e) {
            throw new RuntimeException("Failed to load packet limiter upgrade data", e);
        }
        MOJANG_TO_ID = builder2.build();
    }

    public static void apply(final ConfigurationTransformation.VersionedBuilder builder) {
        builder.addVersion(VERSION, ConfigurationTransformation.builder().addAction(path("packet-limiter", "overrides", ConfigurationTransformation.WILDCARD_OBJECT), INSTANCE).build());
    }

    private V30_PacketIds() {
    }


    @Override
    public Object @Nullable [] visitPath(final NodePath path, final ConfigurationNode value) {
        final String oldClassName = path.get(path.size() - 1).toString();
        if (MOJANG_TO_ID.containsKey(oldClassName)) {
            return path.with(path.size() - 1, MOJANG_TO_ID.get(oldClassName).toString()).array();
        }
        return null;
    }
}
