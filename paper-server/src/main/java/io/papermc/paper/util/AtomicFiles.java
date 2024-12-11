package io.papermc.paper.util;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import org.spongepowered.configurate.util.CheckedConsumer;

// Stripped down version of https://github.com/jpenilla/squaremap/blob/7d7994b4096e5fc61364ea2d87e9aa4e14edf5c6/common/src/main/java/xyz/jpenilla/squaremap/common/util/FileUtil.java
public final class AtomicFiles {

    private AtomicFiles() {
    }

    public static void atomicWrite(final Path path, final CheckedConsumer<Path, IOException> op) throws IOException {
        final Path tmp = tempFile(path);

        try {
            op.accept(tmp);
            atomicMove(tmp, path, true);
        } catch (final IOException ex) {
            try {
                Files.deleteIfExists(tmp);
            } catch (final IOException ex1) {
                ex.addSuppressed(ex1);
            }
            throw ex;
        }
    }

    private static Path tempFile(final Path path) {
        return path.resolveSibling("." + System.nanoTime() + "-" + ThreadLocalRandom.current().nextInt() + "-" + path.getFileName().toString() + ".tmp");    }

    @SuppressWarnings("BusyWait") // not busy waiting
    public static void atomicMove(final Path from, final Path to, final boolean replaceExisting) throws IOException {
        final int maxRetries = 2;

        try {
            atomicMoveIfPossible(from, to, replaceExisting);
        } catch (final AccessDeniedException ex) {
            // Sometimes because of file locking this will fail... Let's just try again and hope for the best
            // Thanks Windows!
            int retries = 1;
            while (true) {
                try {
                    // Pause for a bit
                    Thread.sleep(10L * retries);
                    atomicMoveIfPossible(from, to, replaceExisting);
                    break; // success
                } catch (final AccessDeniedException ex1) {
                    ex.addSuppressed(ex1);
                    if (retries == maxRetries) {
                        throw ex;
                    }
                } catch (final InterruptedException interruptedException) {
                    ex.addSuppressed(interruptedException);
                    Thread.currentThread().interrupt();
                    throw ex;
                }
                ++retries;
            }
        }
    }

    private static void atomicMoveIfPossible(final Path from, final Path to, final boolean replaceExisting) throws IOException {
        final CopyOption[] options = replaceExisting
            ? new CopyOption[]{StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING}
            : new CopyOption[]{StandardCopyOption.ATOMIC_MOVE};

        try {
            Files.move(from, to, options);
        } catch (final AtomicMoveNotSupportedException ex) {
            Files.move(from, to, replaceExisting ? new CopyOption[]{StandardCopyOption.REPLACE_EXISTING} : new CopyOption[]{});
        }
    }

    private static <T, X extends Throwable> Consumer<T> sneaky(final CheckedConsumer<T, X> consumer) {
        return t -> {
            try {
                consumer.accept(t);
            } catch (final Throwable thr) {
                rethrow(thr);
            }
        };
    }

    @SuppressWarnings("unchecked")
    private static <X extends Throwable> RuntimeException rethrow(final Throwable t) throws X {
        throw (X) t;
    }
}
