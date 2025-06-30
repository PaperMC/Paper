package io.papermc.paper.registry.data.dialog.input;

import com.google.common.base.Preconditions;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

record SingleOptionDialogInputImpl(
    String key,
    int width,
    List<SingleOptionDialogInput.OptionEntry> entries,
    Component label,
    boolean labelVisible
) implements SingleOptionDialogInput {

    SingleOptionDialogInputImpl {
        entries = List.copyOf(entries);
    }

    record SingleOptionEntryImpl(String id, @Nullable Component display, boolean initial) implements OptionEntry {
    }

    static final class BuilderImpl implements SingleOptionDialogInput.Builder {

        private final String key;
        private int width = 200;
        private final List<OptionEntry> entries;
        private final Component label;
        private boolean labelVisible = true;

        public BuilderImpl(final String key, final List<OptionEntry> entries, final Component label) {
            this.key = key;
            Preconditions.checkArgument(!entries.isEmpty(), "entries must not be empty");
            this.entries = entries;
            this.label = label;
        }

        @Override
        public BuilderImpl width(final int width) {
            this.width = width;
            return this;
        }

        @Override
        public BuilderImpl labelVisible(final boolean labelVisible) {
            this.labelVisible = labelVisible;
            return this;
        }

        @Override
        public SingleOptionDialogInput build() {
            return new SingleOptionDialogInputImpl(this.key, this.width, this.entries, this.label, this.labelVisible);
        }
    }
}
