package io.papermc.paper.plugin.loader.library;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PaperLibraryStore implements LibraryStore {

    private final List<Path> paths = new ArrayList<>();

    @Override
    public void addLibrary(@NotNull Path library) {
        this.paths.add(library);
    }

    public List<Path> getPaths() {
        return this.paths;
    }
}
