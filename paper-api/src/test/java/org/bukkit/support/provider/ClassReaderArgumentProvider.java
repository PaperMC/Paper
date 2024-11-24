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
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;
import org.objectweb.asm.ClassReader;

public class ClassReaderArgumentProvider implements ArgumentsProvider, AnnotationConsumer<ClassReaderTest> {

    // Needs to be a class, which is from the bukkit package and not a CraftBukkit class
    private static final URI BUKKIT_CLASSES;

    static {
        try {
            BUKKIT_CLASSES = Bukkit.class.getProtectionDomain().getCodeSource().getLocation().toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private Class<?>[] excludedClasses;
    private String[] excludedPackages;

    @Override
    public void accept(ClassReaderTest classReaderTest) {
        this.excludedClasses = classReaderTest.excludedClasses();
        this.excludedPackages = classReaderTest.excludedPackages();

        for (int i = 0; i < excludedPackages.length; i++) {
            this.excludedPackages[i] = this.excludedPackages[i].replace('.', '/');
        }
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return getClassReaders().map(Arguments::of);
    }

    public Stream<ClassReader> getClassReaders() {
        return readBukkitClasses().map(this::toClassReader);
    }

    private ClassReader toClassReader(InputStream stream) {
        try (stream) {
            return new ClassReader(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Stream<InputStream> readBukkitClasses() {
        try {
            return Files.walk(Path.of(BUKKIT_CLASSES))
                    .map(Path::toFile)
                    .filter(File::isFile)
                    .filter(file -> file.getName().endsWith(".class"))
                    .filter(file -> filterPackageNames(removeHomeDirectory(file)))
                    .filter(file -> filterClass(removeHomeDirectory(file)))
                    .map(file -> {
                        try {
                            return new FileInputStream(file);
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String removeHomeDirectory(File file) {
        return file.getAbsolutePath().substring(BUKKIT_CLASSES.getPath().length());
    }

    private boolean filterPackageNames(String name) {
        for (String packageName : excludedPackages) {
            if (name.startsWith(packageName)) {
                return false;
            }
        }

        return true;
    }

    private boolean filterClass(String name) {
        for (Class<?> clazz : excludedClasses) {
            if (name.equals(clazz.getName().replace('.', '/') + ".class")) {
                return false;
            }
        }

        return true;
    }
}
