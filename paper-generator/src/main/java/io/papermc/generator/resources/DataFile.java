package io.papermc.generator.resources;

import com.google.gson.FormattingStyle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import io.papermc.generator.Main;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.stream.Collectors;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.jetbrains.annotations.VisibleForTesting;

public abstract class DataFile<V, A, R> {

    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().setFormattingStyle(FormattingStyle.PRETTY).create();

    private final String path;
    protected final Codec<V> codec;
    protected final DynamicOps<JsonElement> requiredOps;
    private final Transmuter<V, A, R> transmuter;
    private final boolean requireRegistry;
    private @MonotonicNonNull V value;

    protected DataFile(String path, Codec<V> codec, Transmuter<V, A, R> transmuter, boolean requireRegistry) {
        this.path = path;
        this.codec = codec;
        this.transmuter = transmuter;
        this.requireRegistry = requireRegistry;
        this.requiredOps = this.getRequiredOps(JsonOps.INSTANCE);
    }

    protected DataFile(String path, Codec<V> codec, Transmuter<V, A, R> transmuter) {
        this(path, codec, transmuter, false);
    }

    public V get() {
        if (this.value == null) {
            this.value = this.readUnchecked();
        }
        return this.value;
    }

    public V readUnchecked() {
        try {
            return this.read();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public V read() throws IOException {
        try (Reader input = new BufferedReader(new InputStreamReader(DataFile.class.getClassLoader().getResourceAsStream(this.path)))) {
            JsonElement predicates = GSON.fromJson(input, JsonElement.class);
            return this.codec.parse(this.requiredOps, predicates).getOrThrow(JsonParseException::new);
        }
    }

    public SliceResult<A, R> slice(){
        return this.transmuter.examine(this.get());
    }

    public SliceResult<A, R> upgrade(Path destination) throws IOException {
        if (!(this.transmuter instanceof Transmuter.Mutable<V, A, R> mutableTransmuter)) {
            return SliceResult.empty();
        }

        SliceResult<A, R> result = this.slice();
        this.value = mutableTransmuter.apply(this.value, result);

        Files.writeString(destination, this.toJsonString(this.value) + "\n", StandardCharsets.UTF_8);
        return result;
    }

    public String toJsonString(V value) {
        JsonElement element = this.codec.encodeStart(this.requiredOps, value).getOrThrow();
        return GSON.toJson(element);
    }

    public abstract FlattenSliceResult<String, String> print(SliceResult<A, R> result);

    public String path() {
        return this.path;
    }

    @VisibleForTesting
    public Codec<V> codec() {
        return this.codec;
    }

    @VisibleForTesting
    public <T> DynamicOps<T> getRequiredOps(DynamicOps<T> ops) {
        if (this.requireRegistry) {
            return Main.REGISTRY_ACCESS.createSerializationContext(ops);
        }
        return ops;
    }

    @Override
    public String toString() {
        return "DataFile[path=" + this.path + ']';
    }

    public static class Map<K, V> extends DataFile<java.util.Map<K, V>, java.util.Map.Entry<K, V>, K> {
        public Map(String path, Codec<java.util.Map<K, V>> codec, Transmuter<java.util.Map<K, V>, java.util.Map.Entry<K, V>, K> transmuter, boolean requireRegistry) {
            super(path, codec, transmuter, requireRegistry);
        }

        public Map(String path, Codec<java.util.Map<K, V>> codec, Transmuter<java.util.Map<K, V>, java.util.Map.Entry<K, V>, K> transmuter) {
            super(path, codec, transmuter);
        }

        @Override
        public FlattenSliceResult<String, String> print(SliceResult<java.util.Map.Entry<K, V>, K> result) {
            String newEntries = null;
            String dropEntries = null;
            if (!result.added().isEmpty()) {
                java.util.Map<K, V> added = result.added().stream().collect(Collectors.toMap(java.util.Map.Entry::getKey, java.util.Map.Entry::getValue));
                newEntries = this.toJsonString(added);
            }

            if (!result.removed().isEmpty()) {
                java.util.Map<K, V> removed = new HashMap<>(result.removed().size());
                result.removed().forEach(key -> removed.put(key, null));

                JsonElement dropElement = this.codec.encodeStart(this.requiredOps, removed).getOrThrow();
                if (dropElement instanceof JsonObject object) {
                    JsonArray array = new JsonArray(object.size());
                    for (String key : object.keySet()) {
                        array.add(key);
                    }
                    dropEntries = GSON.toJson(array);
                }
            }

            return new FlattenSliceResult<>(
                newEntries,
                dropEntries
            );
        }
    }

    public static class List<E> extends DataFile<java.util.List<E>, E, E> {
        public List(String path, Codec<java.util.List<E>> codec, Transmuter<java.util.List<E>, E, E> transmuter, boolean requireRegistry) {
            super(path, codec, transmuter, requireRegistry);
        }

        public List(String path, Codec<java.util.List<E>> codec, Transmuter<java.util.List<E>, E, E> transmuter) {
            super(path, codec, transmuter);
        }

        @Override
        public FlattenSliceResult<String, String> print(SliceResult<E, E> result) {
            String newEntries = null;
            String dropEntries = null;
            if (!result.added().isEmpty()) {
                newEntries = this.toJsonString(java.util.List.copyOf(result.added()));
            }

            if (!result.removed().isEmpty()) {
                dropEntries = this.toJsonString(java.util.List.copyOf(result.removed()));
            }

            return new FlattenSliceResult<>(
                newEntries,
                dropEntries
            );
        }
    }
}
