package io.papermc.paper.pluginremap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import net.neoforged.art.api.Transformer;

final class InsertManifestAttribute implements Transformer {
    static final String PAPERWEIGHT_NAMESPACE_MANIFEST_KEY = "paperweight-mappings-namespace";
    static final String MOJANG_NAMESPACE = "mojang";
    static final String MOJANG_PLUS_YARN_NAMESPACE = "mojang+yarn";
    static final String SPIGOT_NAMESPACE = "spigot";
    static final Set<String> KNOWN_NAMESPACES = Set.of(MOJANG_NAMESPACE, MOJANG_PLUS_YARN_NAMESPACE, SPIGOT_NAMESPACE);

    private final String mainAttributesKey;
    private final String namespace;
    private final boolean createIfMissing;
    private volatile boolean visitedManifest = false;

    static Transformer addNamespaceManifestAttribute(final String namespace) {
        return new InsertManifestAttribute(PAPERWEIGHT_NAMESPACE_MANIFEST_KEY, namespace, true);
    }

    InsertManifestAttribute(
        final String mainAttributesKey,
        final String namespace,
        final boolean createIfMissing
    ) {
        this.mainAttributesKey = mainAttributesKey;
        this.namespace = namespace;
        this.createIfMissing = createIfMissing;
    }

    @Override
    public ManifestEntry process(final ManifestEntry entry) {
        this.visitedManifest = true;
        try {
            final Manifest manifest = new Manifest(new ByteArrayInputStream(entry.getData()));
            manifest.getMainAttributes().putValue(this.mainAttributesKey, this.namespace);
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            manifest.write(out);
            return ManifestEntry.create(Entry.STABLE_TIMESTAMP, out.toByteArray());
        } catch (final IOException e) {
            throw new RuntimeException("Failed to modify manifest", e);
        }
    }

    @Override
    public Collection<? extends Entry> getExtras() {
        if (!this.visitedManifest && this.createIfMissing) {
            final Manifest manifest = new Manifest();
            manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
            manifest.getMainAttributes().putValue(this.mainAttributesKey, this.namespace);
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                manifest.write(out);
            } catch (final IOException e) {
                throw new RuntimeException("Failed to write manifest", e);
            }
            return List.of(ManifestEntry.create(Entry.STABLE_TIMESTAMP, out.toByteArray()));
        }
        return Transformer.super.getExtras();
    }
}
