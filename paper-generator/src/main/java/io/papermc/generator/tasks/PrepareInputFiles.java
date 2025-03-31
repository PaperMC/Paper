package io.papermc.generator.tasks;

import com.mojang.logging.LogUtils;
import io.papermc.generator.Main;
import io.papermc.generator.resources.DataFile;
import io.papermc.generator.resources.DataFileLoader;
import io.papermc.generator.resources.FlattenSliceResult;
import io.papermc.generator.resources.SliceResult;
import java.io.IOException;
import java.nio.file.Path;
import org.slf4j.Logger;

public class PrepareInputFiles {

    static {
        Main.bootStrap(true);
    }

    private static final Logger LOGGER = LogUtils.getLogger();

    public static void main(String[] args) throws IOException {
        Path resourceDir = Path.of(args[0]);
        for (DataFile<?, ?, ?> file : DataFileLoader.DATA_FILES_VIEW.values()) {
            upgrade(resourceDir, file);
        }
    }

    private static <V, A, R> void upgrade(Path resourceDir, DataFile<V, A, R> file) throws IOException {
        Path filePath = Path.of(file.path());
        SliceResult<A, R> result = file.upgrade(resourceDir.resolve(filePath));
        if (result.isEmpty()) {
            return;
        }

        FlattenSliceResult<String, String> printedResult = file.print(result);
        if (printedResult.added() != null) {
            LOGGER.info("Added the following elements in {}:", filePath);
            LOGGER.info(printedResult.added());
        }

        if (printedResult.removed() != null) {
            LOGGER.warn("Removed the following keys in {}:", filePath);
            LOGGER.warn(printedResult.removed());
        }
    }
}
