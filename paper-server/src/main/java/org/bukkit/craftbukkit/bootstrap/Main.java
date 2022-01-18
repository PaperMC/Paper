// Based on net.minecraft.bundler.Main
package org.bukkit.craftbukkit.bootstrap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] argv) {
        new Main().run(argv);
    }

    private void run(String[] argv) {
        try {
            String defaultMainClassName = readResource("main-class", BufferedReader::readLine);
            String mainClassName = System.getProperty("bundlerMainClass", defaultMainClassName);

            String repoDir = System.getProperty("bundlerRepoDir", "bundler");
            Path outputDir = Paths.get(repoDir).toAbsolutePath();
            if (!Files.isDirectory(outputDir)) {
                Files.createDirectories(outputDir);
            }

            System.out.println("Unbundling libraries to " + outputDir);

            boolean readOnly = Boolean.getBoolean("bundlerReadOnly");
            List<URL> extractedUrls = new ArrayList<>();
            readAndExtractDir("versions", outputDir, extractedUrls, readOnly);
            readAndExtractDir("libraries", outputDir, extractedUrls, readOnly);

            if (mainClassName == null || mainClassName.isEmpty()) {
                System.out.println("Empty main class specified, exiting");
                System.exit(0);
            }

            URLClassLoader classLoader = new URLClassLoader(extractedUrls.toArray(new URL[0]));

            System.out.println("Starting server");
            Thread runThread = new Thread(() -> {
                try {
                    Class<?> mainClass = Class.forName(mainClassName, true, classLoader);
                    MethodHandle mainHandle = MethodHandles.lookup().findStatic(mainClass, "main", MethodType.methodType(void.class, String[].class)).asFixedArity();
                    mainHandle.invoke(argv);
                } catch (Throwable t) {
                    Thrower.INSTANCE.sneakyThrow(t);
                }
            }, "ServerMain");

            runThread.setContextClassLoader(classLoader);
            runThread.start();
        } catch (Exception e) {
            e.printStackTrace(System.out);
            System.out.println("Failed to extract server libraries, exiting");
        }
    }

    private <T> T readResource(String resource, ResourceParser<T> parser) throws Exception {
        String fullPath = "/META-INF/" + resource;
        try (InputStream is = getClass().getResourceAsStream(fullPath)) {
            if (is == null) {
                throw new IllegalStateException("Resource " + fullPath + " not found");
            }
            return parser.parse(new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)));
        }
    }

    private void readAndExtractDir(String subdir, Path outputDir, List<URL> extractedUrls, boolean readOnly) throws Exception {
        List<FileEntry> entries = readResource(subdir + ".list", reader -> reader.lines().map(FileEntry::parseLine).toList());
        Path subdirPath = outputDir.resolve(subdir);
        for (FileEntry entry : entries) {
            if (entry.path.startsWith("minecraft-server")) {
                continue;
            }
            Path outputFile = subdirPath.resolve(entry.path);
            if (!readOnly) {
                checkAndExtractJar(subdir, entry, outputFile);
            }
            extractedUrls.add(outputFile.toUri().toURL());
        }
    }

    private void checkAndExtractJar(String subdir, FileEntry entry, Path outputFile) throws Exception {
        if (!Files.exists(outputFile) || !checkIntegrity(outputFile, entry.hash())) {
            System.out.printf("Unpacking %s (%s:%s) to %s%n", entry.path, subdir, entry.id, outputFile);
            extractJar(subdir, entry.path, outputFile);
        }
    }

    private void extractJar(String subdir, String jarPath, Path outputFile) throws IOException {
        Files.createDirectories(outputFile.getParent());

        try (InputStream input = getClass().getResourceAsStream("/META-INF/" + subdir + "/" + jarPath)) {
            if (input == null) {
                throw new IllegalStateException("Declared library " + jarPath + " not found");
            }

            Files.copy(input, outputFile, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private static boolean checkIntegrity(Path file, String expectedHash) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        try (InputStream output = Files.newInputStream(file)) {
            output.transferTo(new DigestOutputStream(OutputStream.nullOutputStream(), digest));

            String actualHash = byteToHex(digest.digest());
            if (actualHash.equalsIgnoreCase(expectedHash)) {
                return true;
            }

            System.out.printf("Expected file %s to have hash %s, but got %s%n", new Object[]{file, expectedHash, actualHash});
        }
        return false;
    }

    private static String byteToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            result.append(Character.forDigit(b >> 4 & 0xF, 16));
            result.append(Character.forDigit(b >> 0 & 0xF, 16));
        }
        return result.toString();
    }

    @FunctionalInterface
    private static interface ResourceParser<T> {

        T parse(BufferedReader param1BufferedReader) throws Exception;
    }

    private static final record FileEntry(String hash, String id, String path) {

        public static FileEntry parseLine(String line) {
            String[] fields = line.split(" ");
            if (fields.length != 2) {
                throw new IllegalStateException("Malformed library entry: " + line);
            }
            String path = fields[1].substring(1);
            return new FileEntry(fields[0], path, path);
        }
    }

    private static class Thrower<T extends Throwable> {

        private static final Thrower<RuntimeException> INSTANCE = new Thrower();

        public void sneakyThrow(Throwable exception) throws T {
            throw (T) exception;
        }
    }
}
