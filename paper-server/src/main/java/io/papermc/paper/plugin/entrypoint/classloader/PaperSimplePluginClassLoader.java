package io.papermc.paper.plugin.entrypoint.classloader;

import io.papermc.paper.plugin.configuration.PluginMeta;
import io.papermc.paper.plugin.util.NamespaceChecker;
import org.jetbrains.annotations.ApiStatus;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * Represents a simple classloader used for paper plugin bootstrappers.
 */
@ApiStatus.Internal
public class PaperSimplePluginClassLoader extends URLClassLoader {

    static {
        ClassLoader.registerAsParallelCapable();
    }

    protected final PluginMeta configuration;
    protected final Path source;
    protected final Manifest jarManifest;
    protected final URL jarUrl;
    protected final JarFile jar;

    public PaperSimplePluginClassLoader(Path source, JarFile file, PluginMeta configuration, ClassLoader parentLoader) throws IOException {
        super(source.getFileName().toString(), new URL[]{source.toUri().toURL()}, parentLoader);

        this.source = source;
        this.jarManifest = file.getManifest();
        this.jarUrl = source.toUri().toURL();
        this.configuration = configuration;
        this.jar = file;
    }

    @Override
    public URL getResource(String name) {
        return this.findResource(name);
    }

    @Override
    public Enumeration<URL> getResources(String name) throws IOException {
        return this.findResources(name);
    }

    // Bytecode modification supported loader
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        NamespaceChecker.validateNameSpaceForClassloading(name);

        // See UrlClassLoader#findClass(String)
        String path = name.replace('.', '/').concat(".class");
        JarEntry entry = this.jar.getJarEntry(path);
        if (entry == null) {
            throw new ClassNotFoundException(name);
        }

        // See URLClassLoader#defineClass(String, Resource)
        byte[] classBytes;

        try (InputStream is = this.jar.getInputStream(entry)) {
            classBytes = is.readAllBytes();
        } catch (IOException ex) {
            throw new ClassNotFoundException(name, ex);
        }

        classBytes = ClassloaderBytecodeModifier.bytecodeModifier().modify(this.configuration, classBytes);

        int dot = name.lastIndexOf('.');
        if (dot != -1) {
            String pkgName = name.substring(0, dot);
            // Get defined package does not correctly handle sealed packages.
            if (this.getDefinedPackage(pkgName) == null) {
                try {
                    if (this.jarManifest != null) {
                        this.definePackage(pkgName, this.jarManifest, this.jarUrl);
                    } else {
                        this.definePackage(pkgName, null, null, null, null, null, null, null);
                    }
                } catch (IllegalArgumentException ex) {
                    // parallel-capable class loaders: re-verify in case of a
                    // race condition
                    if (this.getDefinedPackage(pkgName) == null) {
                        // Should never happen
                        throw new IllegalStateException("Cannot find package " + pkgName);
                    }
                }
            }
        }

        CodeSigner[] signers = entry.getCodeSigners();
        CodeSource source = new CodeSource(this.jarUrl, signers);

        return this.defineClass(name, classBytes, 0, classBytes.length, source);
    }

    @Override
    public String toString() {
        return "PaperSimplePluginClassLoader{" +
            "configuration=" + this.configuration +
            ", source=" + this.source +
            ", jarManifest=" + this.jarManifest +
            ", jarUrl=" + this.jarUrl +
            ", jar=" + this.jar +
            '}';
    }
}
