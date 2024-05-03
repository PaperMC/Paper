package org.bukkit.craftbukkit.legacy.fieldrename;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.util.ApiVersion;

public record FieldRenameData(RenameData<String> renameData, RenameData<NamespacedKey> keyRenameData) {

    public String getReplacement(ApiVersion apiVersion, String from) {
        if (from == null) {
            return null;
        }

        from = from.toUpperCase(Locale.ROOT);
        return renameData.getReplacement(apiVersion, from);
    }

    public NamespacedKey getReplacement(NamespacedKey from, ApiVersion apiVersion) {
        if (from == null) {
            return null;
        }

        return keyRenameData.getReplacement(apiVersion, from);
    }

    public static class Builder {

        private final Map<String, String> data = new HashMap<>();
        private final NavigableMap<ApiVersion, Map<String, String>> versionData = new TreeMap<>();
        private final Map<NamespacedKey, NamespacedKey> keyData = new HashMap<>();
        private final NavigableMap<ApiVersion, Map<NamespacedKey, NamespacedKey>> versionKeyData = new TreeMap<>();
        private ApiVersion currentVersion;
        private boolean keyRename = false;

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder forVersionsBefore(ApiVersion apiVersion) {
            this.currentVersion = apiVersion;
            this.keyRename = false;
            return this;
        }

        public Builder forAllVersions() {
            this.currentVersion = null;
            this.keyRename = false;
            return this;
        }

        public Builder withKeyRename() {
            this.keyRename = true;
            return this;
        }

        public Builder change(String from, String to) {
            if (currentVersion != null) {
                versionData.computeIfAbsent(currentVersion, d -> new HashMap<>()).put(from.replace('.', '_'), to);
            } else {
                data.put(from.replace('.', '_'), to);
            }

            if (keyRename) {
                NamespacedKey fromKey = NamespacedKey.minecraft(from.toLowerCase(Locale.ROOT));
                NamespacedKey toKey = NamespacedKey.minecraft(to.toLowerCase(Locale.ROOT));
                if (currentVersion != null) {
                    versionKeyData.computeIfAbsent(currentVersion, d -> new HashMap<>()).put(fromKey, toKey);
                } else {
                    keyData.put(fromKey, toKey);
                }
            }
            return this;
        }

        public FieldRenameData build() {
            return new FieldRenameData(new RenameData<>(versionData, data), new RenameData<>(versionKeyData, keyData));
        }
    }

    private record RenameData<T>(NavigableMap<ApiVersion, Map<T, T>> versionData, Map<T, T> data) {
        public T getReplacement(ApiVersion apiVersion, T from) {
            from = data.getOrDefault(from, from);

            for (Map.Entry<ApiVersion, Map<T, T>> entry : versionData.entrySet()) {
                if (apiVersion.isNewerThanOrSameAs(entry.getKey())) {
                    continue;
                }

                from = entry.getValue().getOrDefault(from, from);
            }

            return from;
        }
    }
}
