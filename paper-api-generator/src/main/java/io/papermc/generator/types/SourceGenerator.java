package io.papermc.generator.types;

import java.io.IOException;
import java.nio.file.Path;

public interface SourceGenerator {

    void writeToFile(Path parent) throws IOException;
}
