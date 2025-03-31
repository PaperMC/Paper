package io.papermc.generator;

import io.papermc.generator.resources.DataFile;
import io.papermc.generator.resources.DataFileLoader;
import io.papermc.generator.resources.FlattenSliceResult;
import io.papermc.generator.resources.SliceResult;
import java.util.Collection;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertNull;

public class DataFileTest extends BootstrapTest {

    public static Collection<DataFile<?, ?, ?>> files() {
        return DataFileLoader.DATA_FILES_VIEW.values();
    }

    @ParameterizedTest
    @MethodSource("files")
    public <V, A, R> void testFile(DataFile<V, A, R> file) {
        SliceResult<A, R> result = file.slice();
        if (result.isEmpty()) {
            return;
        }

        FlattenSliceResult<String, String> printedResult = file.print(result);
        assertNull(printedResult.added(), () -> "Missing some data in " + file.path());
        assertNull(printedResult.removed(), () -> "Extra data found in " + file.path());
    }
}
