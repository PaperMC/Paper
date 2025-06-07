package io.papermc.paper.registry.data.dialog.input.type;

import com.google.common.base.Preconditions;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

record SingleOptionDialogInputConfigImpl(
    int width, List<SingleOptionDialogInputConfig.OptionEntry> entries, Component label, boolean labelVisible
) implements SingleOptionDialogInputConfig {

    SingleOptionDialogInputConfigImpl {
        entries = List.copyOf(entries);
    }

    record SingleOptionEntryImpl(String id, @Nullable Component display, boolean initial) implements OptionEntry {
    }

    static final class BuilderImpl implements SingleOptionDialogInputConfig.Builder {

        private int width = 200;
        private final List<OptionEntry> entries;
        private final Component label;
        private boolean labelVisible = true;

        public BuilderImpl(final List<OptionEntry> entries, final Component label) {
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
        public SingleOptionDialogInputConfig build() {
            return new SingleOptionDialogInputConfigImpl(this.width, this.entries, this.label, this.labelVisible);
        }
    }
}
