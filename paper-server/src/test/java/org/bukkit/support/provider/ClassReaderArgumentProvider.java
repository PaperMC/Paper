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
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;
import org.bukkit.Bukkit;
import org.bukkit.support.test.ClassReaderTest;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;
import org.objectweb.asm.ClassReader;

public class ClassReaderArgumentProvider implements ArgumentsProvider, AnnotationConsumer<ClassReaderTest> {

    // Needs to be a class, which is present in the source, and not a test class
    private static final URI CRAFT_BUKKIT_CLASSES;
    // Needs to be a class, which is from the bukkit package and not a CraftBukkit class
    private static final URI BUKKIT_CLASSES;

    static {
        try {
            CRAFT_BUKKIT_CLASSES = org.bukkit.craftbukkit.Main.class.getProtectionDomain().getCodeSource().getLocation().toURI();
            BUKKIT_CLASSES = Bukkit.class.getProtectionDomain().getCodeSource().getLocation().toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private ClassReaderTest.ClassType[] classTypes;
    private Class<?>[] excludedClasses;
    private String[] excludedPackages;

    @Override
    public void accept(ClassReaderTest classReaderTest) {
        this.classTypes = classReaderTest.value();
        this.excludedClasses = classReaderTest.excludedClasses();
        this.excludedPackages = classReaderTest.excludedPackages();

        for (int i = 0; i < this.excludedPackages.length; i++) {
            this.excludedPackages[i] = this.excludedPackages[i].replace('.', '/');
        }
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return this.getClassReaders().map(Arguments::of);
    }

    public Stream<ClassReader> getClassReaders() {

        Stream<InputStream> result = Stream.empty();

        if (this.contains(ClassReaderTest.ClassType.CRAFT_BUKKIT)) {
            result = Stream.concat(result, this.createCraftBukkitClasses());
        }

        if (this.contains(ClassReaderTest.ClassType.BUKKIT)) {
            result = Stream.concat(result, this.readBukkitClasses());
        }

        return result.map(this::toClassReader);
    }

    private ClassReader toClassReader(InputStream stream) {
        try (stream) {
            return new ClassReader(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean contains(ClassReaderTest.ClassType classType) {
        for (ClassReaderTest.ClassType c : this.classTypes) {
            if (c == classType) {
                return true;
            }
        }

        return false;
    }

    private Stream<InputStream> readBukkitClasses() {
        return this.readJarFile(ClassReaderArgumentProvider.BUKKIT_CLASSES, false);
    }

    private Stream<InputStream> readJarFile(URI uri, boolean filterModified) {
        try {
            JarFile jarFile = new JarFile(new File(uri));
            return jarFile.stream().onClose(() -> this.closeJarFile(jarFile))
                    .filter(entry -> entry.getName().endsWith(".class"))
                    .filter(entry -> this.filterModifiedIfNeeded(entry, filterModified))
                    .filter(entry -> this.filterPackageNames(entry.getName()))
                    .filter(entry -> this.filterClass(entry.getName()))
                    .map(entry -> {
                        try {
                            return jarFile.getInputStream(entry);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean filterModifiedIfNeeded(JarEntry entry, boolean needed) {
        if (!needed) {
            return true;
        }

        return !new File(ClassReaderArgumentProvider.CRAFT_BUKKIT_CLASSES.resolve(entry.getName())).exists();
    }

    private boolean filterPackageNames(String name) {
        for (String packageName : this.excludedPackages) {
            if (name.startsWith(packageName)) {
                return false;
            }
        }

        return true;
    }

    private boolean filterClass(String name) {
        for (Class<?> clazz : this.excludedClasses) {
            if (name.equals(clazz.getName().replace('.', '/') + ".class")) {
                return false;
            }
        }

        return true;
    }

    private Stream<InputStream> createCraftBukkitClasses() {
        try {
            return Files.walk(Path.of(ClassReaderArgumentProvider.CRAFT_BUKKIT_CLASSES))
                    .map(Path::toFile)
                    .filter(File::isFile)
                    .filter(file -> file.getName().endsWith(".class"))
                    .filter(file -> this.filterPackageNames(this.removeHomeDirectory(file)))
                    .filter(file -> this.filterClass(this.removeHomeDirectory(file)))
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
        return file.getAbsolutePath().substring(ClassReaderArgumentProvider.CRAFT_BUKKIT_CLASSES.getPath().length());
    }

    private void closeJarFile(JarFile jarFile) {
        try {
            jarFile.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
