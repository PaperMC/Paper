/*
 * Forge Auto Renaming Tool
 * Copyright (c) 2021
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 2.1
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package net.neoforged.art.internal;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import net.neoforged.cliutils.JarUtils;
import net.neoforged.cliutils.progress.ProgressReporter;
import org.objectweb.asm.Opcodes;

import net.neoforged.art.api.ClassProvider;
import net.neoforged.art.api.Renamer;
import net.neoforged.art.api.Transformer;
import net.neoforged.art.api.Transformer.ClassEntry;
import net.neoforged.art.api.Transformer.Entry;
import net.neoforged.art.api.Transformer.ManifestEntry;
import net.neoforged.art.api.Transformer.ResourceEntry;

public class RenamerImpl implements Renamer { // Paper - public
    private static final ProgressReporter PROGRESS = ProgressReporter.getDefault();
    static final int MAX_ASM_VERSION = Opcodes.ASM9;
    private static final String MANIFEST_NAME = "META-INF/MANIFEST.MF";
    private final List<File> libraries;
    private final List<Transformer> transformers;
    private final SortedClassProvider sortedClassProvider;
    private final List<ClassProvider> classProviders;
    private final int threads;
    private final Consumer<String> logger;
    private final Consumer<String> debug;
    private boolean setup = false;
    private ClassProvider libraryClasses;

    RenamerImpl(List<File> libraries, List<Transformer> transformers, SortedClassProvider sortedClassProvider, List<ClassProvider> classProviders,
                int threads, Consumer<String> logger, Consumer<String> debug) {
        this.libraries = libraries;
        this.transformers = transformers;
        this.sortedClassProvider = sortedClassProvider;
        this.classProviders = Collections.unmodifiableList(classProviders);
        this.threads = threads;
        this.logger = logger;
        this.debug = debug;
    }

    private void setup() {
        if (this.setup)
            return;

        this.setup = true;

        ClassProvider.Builder libraryClassesBuilder = ClassProvider.builder().shouldCacheAll(true);
        this.logger.accept("Adding Libraries to Inheritance");
        this.libraries.forEach(f -> libraryClassesBuilder.addLibrary(f.toPath()));

        this.libraryClasses = libraryClassesBuilder.build();
    }

    @Override
    public void run(File input, File output) {
        // Paper start - Add remappingSelf
        this.run(input, output, false);
    }
    public void run(File input, File output, boolean remappingSelf) {
        // Paper end
        if (!this.setup)
            this.setup();

        if (Boolean.getBoolean(ProgressReporter.ENABLED_PROPERTY)) {
            try {
                PROGRESS.setMaxProgress(JarUtils.getFileCountInZip(input));
            } catch (IOException e) {
                logger.accept("Failed to read zip file count: " + e);
            }
        }

        input = Objects.requireNonNull(input).getAbsoluteFile();
        output = Objects.requireNonNull(output).getAbsoluteFile();

        if (!input.exists())
            throw new IllegalArgumentException("Input file not found: " + input.getAbsolutePath());

        logger.accept("Reading Input: " + input.getAbsolutePath());
        PROGRESS.setStep("Reading input jar");
        // Read everything from the input jar!
        List<Entry> oldEntries = new ArrayList<>();
        try (ZipFile in = new ZipFile(input)) {
            int amount = 0;
            for (Enumeration<? extends ZipEntry> entries = in.entries(); entries.hasMoreElements();) {
                final ZipEntry e = entries.nextElement();
                if (e.isDirectory())
                    continue;
                String name = e.getName();
                byte[] data;
                try (InputStream entryInput = in.getInputStream(e)) {
                    data = entryInput.readAllBytes(); // Paper - Use readAllBytes
                }

                if (name.endsWith(".class") && !name.contains("META-INF/")) // Paper - Skip META-INF entries
                    oldEntries.add(ClassEntry.create(name, e.getTime(), data));
                else if (name.equals(MANIFEST_NAME))
                    oldEntries.add(ManifestEntry.create(e.getTime(), data));
                else if (name.equals("javadoctor.json"))
                    oldEntries.add(Transformer.JavadoctorEntry.create(e.getTime(), data));
                else
                    oldEntries.add(ResourceEntry.create(name, e.getTime(), data));

                if ((++amount) % 10 == 0) {
                    PROGRESS.setProgress(amount);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not parse input: " + input.getAbsolutePath(), e);
        }

        this.sortedClassProvider.clearCache();
        ArrayList<ClassProvider> classProviders = new ArrayList<>(this.classProviders);
        classProviders.add(0, this.libraryClasses);
        this.sortedClassProvider.classProviders = classProviders;

        AsyncHelper async = new AsyncHelper(threads);
        try {

            /* Disabled until we do something with it
            // Gather original file Hashes, so that we can detect changes and update the manifest if necessary
            log("Gathering original hashes");
            Map<String, String> oldHashes = async.invokeAll(oldEntries,
                e -> new Pair<>(e.getName(), HashFunction.SHA256.hash(e.getData()))
            ).stream().collect(Collectors.toMap(Pair::getLeft, Pair::getRight));
            */

            PROGRESS.setProgress(0);
            PROGRESS.setIndeterminate(true);
            PROGRESS.setStep("Processing entries");

            List<ClassEntry> ourClasses = oldEntries.stream()
                .filter(e -> e instanceof ClassEntry && !e.getName().startsWith("META-INF/"))
                .map(ClassEntry.class::cast)
                .collect(Collectors.toList());

            // Add the original classes to the inheritance map, TODO: Multi-Release somehow?
            logger.accept("Adding input to inheritance map");
            ClassProvider.Builder inputClassesBuilder = ClassProvider.builder();
            async.consumeAll(ourClasses, ClassEntry::getClassName, c ->
                inputClassesBuilder.addClass(c.getName().substring(0, c.getName().length() - 6), c.getData())
            );
            classProviders.add(0, inputClassesBuilder.build());

            // Process everything
            logger.accept("Processing entries");
            List<Entry> newEntries = async.invokeAll(oldEntries, Entry::getName, this::processEntry);

            logger.accept("Adding extras");
            // Paper start - I'm pretty sure the duplicates are because the input is already on the classpath
            List<Entry> finalNewEntries = newEntries;
            transformers.forEach(t -> finalNewEntries.addAll(t.getExtras()));

            Set<String> seen = new HashSet<>();
            if (remappingSelf) {
                // deduplicate
                List<Entry> n = new ArrayList<>();
                for (final Entry e : newEntries) {
                    if (seen.add(e.getName())) {
                        n.add(e);
                    }
                }
                newEntries = n;
            } else {
            String dupes = newEntries.stream().map(Entry::getName)
                .filter(n -> !seen.add(n))
                .sorted()
                .collect(Collectors.joining(", "));
            if (!dupes.isEmpty())
                throw new IllegalStateException("Duplicate entries detected: " + dupes);
            }
            // Paper end

            // We care about stable output, so sort, and single thread write.
            logger.accept("Sorting");
            Collections.sort(newEntries, this::compare);

            if (!output.getParentFile().exists())
                output.getParentFile().mkdirs();

            seen.clear();

            PROGRESS.setMaxProgress(newEntries.size());
            PROGRESS.setStep("Writing output");

            logger.accept("Writing Output: " + output.getAbsolutePath());
            try (OutputStream fos = new BufferedOutputStream(Files.newOutputStream(output.toPath()));
                 ZipOutputStream zos = new ZipOutputStream(fos)) {

                int amount = 0;
                for (Entry e : newEntries) {
                    String name = e.getName();
                    int idx = name.lastIndexOf('/');
                    if (idx != -1)
                        addDirectory(zos, seen, name.substring(0, idx));

                    logger.accept("  " + name);
                    ZipEntry entry = new ZipEntry(name);
                    entry.setTime(e.getTime());
                    zos.putNextEntry(entry);
                    zos.write(e.getData());
                    zos.closeEntry();

                    if ((++amount) % 10 == 0) {
                        PROGRESS.setProgress(amount);
                    }
                }

                PROGRESS.setProgress(amount);
            }
        } catch (final IOException e) {
            throw new RuntimeException("Could not write to file " + output.getAbsolutePath(), e);
        } finally {
            async.shutdown();
        }
    }

    private byte[] readAllBytes(InputStream in, long size) throws IOException {
        // This program will crash if size exceeds MAX_INT anyway since arrays are limited to 32-bit indices
        ByteArrayOutputStream tmp = new ByteArrayOutputStream(size >= 0 ? (int) size : 0);

        byte[] buffer = new byte[8192];
        int read;
        while ((read = in.read(buffer)) != -1) {
            tmp.write(buffer, 0, read);
        }

        return tmp.toByteArray();
    }

    // Tho Directory entries are not strictly necessary, we add them because some bad implementations of Zip extractors
    // attempt to extract files without making sure the parents exist.
    private void addDirectory(ZipOutputStream zos, Set<String> seen, String path) throws IOException {
        if (!seen.add(path))
            return;

        int idx = path.lastIndexOf('/');
        if (idx != -1)
            addDirectory(zos, seen, path.substring(0, idx));

        logger.accept("  " + path + '/');
        ZipEntry dir = new ZipEntry(path + '/');
        dir.setTime(Entry.STABLE_TIMESTAMP);
        zos.putNextEntry(dir);
        zos.closeEntry();
    }

    private Entry processEntry(final Entry start) {
        Entry entry = start;
        for (Transformer transformer : RenamerImpl.this.transformers) {
            entry = entry.process(transformer);
            if (entry == null)
                return null;
        }
        return entry;
    }

    private int compare(Entry o1, Entry o2) {
        // In order for JarInputStream to work, MANIFEST has to be the first entry, so make it first!
        if (MANIFEST_NAME.equals(o1.getName()))
            return MANIFEST_NAME.equals(o2.getName()) ? 0 : -1;
        if (MANIFEST_NAME.equals(o2.getName()))
            return MANIFEST_NAME.equals(o1.getName()) ? 0 :  1;
        return o1.getName().compareTo(o2.getName());
    }

    @Override
    public void close() throws IOException {
        this.sortedClassProvider.close();
    }
}
