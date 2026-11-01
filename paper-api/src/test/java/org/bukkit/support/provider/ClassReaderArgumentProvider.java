package org.bukkit.support.provider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import org.bukkit.Bukkit;
import org.bukkit.support.test.ClassReaderTest;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;
import org.junit.jupiter.params.support.ParameterDeclarations;
import org.objectweb.asm.ClassReader;

@NullMarked
public class ClassReaderArgumentProvider implements ArgumentsProvider, AnnotationConsumer<ClassReaderTest> {

    // Needs to be a class, which is from the bukkit package and not a CraftBukkit class
    private static final URI BUKKIT_CLASSES;

    static {
        try {
            BUKKIT_CLASSES = Bukkit.class.getProtectionDomain().getCodeSource().getLocation().toURI();
        } catch (final URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private Class<?>[] excludedClasses = new Class<?>[0];
    private String[] excludedPackages = new String[0];

    @Override
    public void accept(final ClassReaderTest classReaderTest) {
        this.excludedClasses = classReaderTest.excludedClasses();
        this.excludedPackages = classReaderTest.excludedPackages();

        for (int i = 0; i < this.excludedPackages.length; i++) {
            this.excludedPackages[i] = this.excludedPackages[i].replace('.', '/');
        }
    }

    @Override
    public Stream<? extends Arguments> provideArguments(final ParameterDeclarations declarations, final ExtensionContext extensionContext) {
        return this.getClassReaders().map(Arguments::of);
    }

    public Stream<ClassReader> getClassReaders() {
        return this.readBukkitClasses().map(this::toClassReader);
    }

    private ClassReader toClassReader(final InputStream stream) {
        try (stream) {
            return new ClassReader(stream);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Stream<InputStream> readBukkitClasses() {
        try (Stream<Path> files = Files.walk(Path.of(BUKKIT_CLASSES))) {
            return files
                .map(Path::toFile)
                .filter(File::isFile)
                .filter(file -> file.getName().endsWith(".class"))
                .filter(file -> this.filterPackageNames(this.removeHomeDirectory(file)))
                .filter(file -> this.filterClass(this.removeHomeDirectory(file)))
                .map(file -> {
                    try {
                        return new FileInputStream(file);
                    } catch (final FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                });
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String removeHomeDirectory(final File file) {
        return file.getAbsolutePath().substring(BUKKIT_CLASSES.getPath().length());
    }

    private boolean filterPackageNames(final String name) {
        for (final String packageName : this.excludedPackages) {
            if (name.startsWith(packageName)) {
                return false;
            }
        }

        return true;
    }

    private boolean filterClass(final String name) {
        for (final Class<?> clazz : this.excludedClasses) {
            if (name.equals(clazz.getName().replace('.', '/') + ".class")) {
                return false;
            }
        }

        return true;
    }
}
