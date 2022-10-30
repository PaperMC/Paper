package io.papermc.paper.plugin.entrypoint.classloader;

import io.papermc.paper.pluginremap.reflect.ReflectionRemapper;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import static java.util.Objects.requireNonNullElse;

public final class BytecodeModifyingURLClassLoader extends URLClassLoader {
    static {
        ClassLoader.registerAsParallelCapable();
    }

    private static final Object MISSING_MANIFEST = new Object();

    private final Function<byte[], byte[]> modifier;
    private final Map<String, Object> manifests = new ConcurrentHashMap<>();

    public BytecodeModifyingURLClassLoader(
        final URL[] urls,
        final ClassLoader parent,
        final Function<byte[], byte[]> modifier
    ) {
        super(urls, parent);
        this.modifier = modifier;
    }

    public BytecodeModifyingURLClassLoader(
        final URL[] urls,
        final ClassLoader parent
    ) {
        this(urls, parent, bytes -> {
            final ClassReader classReader = new ClassReader(bytes);
            final ClassWriter classWriter = new ClassWriter(classReader, 0);
            final ClassVisitor visitor = ReflectionRemapper.visitor(classWriter);
            if (visitor == classWriter) {
                return bytes;
            }
            classReader.accept(visitor, 0);
            return classWriter.toByteArray();
        });
    }

    @Override
    protected Class<?> findClass(final String name) throws ClassNotFoundException {
        final Class<?> result;
        final String path = name.replace('.', '/').concat(".class");
        final URL url = this.findResource(path);
        if (url != null) {
            try {
                result = this.defineClass(name, url);
            } catch (final IOException e) {
                throw new ClassNotFoundException(name, e);
            }
        } else {
            result = null;
        }
        if (result == null) {
            throw new ClassNotFoundException(name);
        }
        return result;
    }

    private Class<?> defineClass(String name, URL url) throws IOException {
        int i = name.lastIndexOf('.');
        if (i != -1) {
            String pkgname = name.substring(0, i);
            // Check if package already loaded.
            final @Nullable Manifest man = this.manifestFor(url);
            final URL jarUrl = URI.create(jarName(url)).toURL();
            if (this.getAndVerifyPackage(pkgname, man, jarUrl) == null) {
                try {
                    if (man != null) {
                        this.definePackage(pkgname, man, jarUrl);
                    } else {
                        this.definePackage(pkgname, null, null, null, null, null, null, null);
                    }
                } catch (IllegalArgumentException iae) {
                    // parallel-capable class loaders: re-verify in case of a
                    // race condition
                    if (this.getAndVerifyPackage(pkgname, man, jarUrl) == null) {
                        // Should never happen
                        throw new AssertionError("Cannot find package " +
                            pkgname);
                    }
                }
            }
        }
        final byte[] bytes;
        try (final InputStream is = url.openStream()) {
            bytes = is.readAllBytes();
        }

        final byte[] modified = this.modifier.apply(bytes);

        final CodeSource cs = new CodeSource(url, (CodeSigner[]) null);
        return this.defineClass(name, modified, 0, modified.length, cs);
    }

    private Package getAndVerifyPackage(
        String pkgname,
        Manifest man, URL url
    ) {
        Package pkg = getDefinedPackage(pkgname);
        if (pkg != null) {
            // Package found, so check package sealing.
            if (pkg.isSealed()) {
                // Verify that code source URL is the same.
                if (!pkg.isSealed(url)) {
                    throw new SecurityException(
                        "sealing violation: package " + pkgname + " is sealed");
                }
            } else {
                // Make sure we are not attempting to seal the package
                // at this code source URL.
                if ((man != null) && this.isSealed(pkgname, man)) {
                    throw new SecurityException(
                        "sealing violation: can't seal package " + pkgname +
                            ": already loaded");
                }
            }
        }
        return pkg;
    }

    private boolean isSealed(String name, Manifest man) {
        Attributes attr = man.getAttributes(name.replace('.', '/').concat("/"));
        String sealed = null;
        if (attr != null) {
            sealed = attr.getValue(Attributes.Name.SEALED);
        }
        if (sealed == null) {
            if ((attr = man.getMainAttributes()) != null) {
                sealed = attr.getValue(Attributes.Name.SEALED);
            }
        }
        return "true".equalsIgnoreCase(sealed);
    }

    private @Nullable Manifest manifestFor(final URL url) throws IOException {
        Manifest man = null;
        if (url.getProtocol().equals("jar")) {
            try {
                final Object computedManifest = this.manifests.computeIfAbsent(jarName(url), $ -> {
                    try {
                        final Manifest m = ((JarURLConnection) url.openConnection()).getManifest();
                        return requireNonNullElse(m, MISSING_MANIFEST);
                    } catch (final IOException e) {
                        throw new UncheckedIOException(e);
                    }
                });
                if (computedManifest instanceof Manifest found) {
                    man = found;
                }
            } catch (final UncheckedIOException e) {
                throw e.getCause();
            } catch (final IllegalArgumentException e) {
                throw new IOException(e);
            }
        }
        return man;
    }

    private static String jarName(final URL sourceUrl) {
        final int exclamationIdx = sourceUrl.getPath().lastIndexOf('!');
        if (exclamationIdx != -1) {
            return sourceUrl.getPath().substring(0, exclamationIdx);
        }
        throw new IllegalArgumentException("Could not find jar for URL " + sourceUrl);
    }
}
