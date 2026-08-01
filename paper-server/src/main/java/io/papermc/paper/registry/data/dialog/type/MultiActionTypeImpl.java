package io.papermc.paper.registry.data.dialog.type;

import com.google.common.base.Preconditions;
import io.papermc.paper.registry.data.dialog.ActionButton;
import java.util.List;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.util.BoundChecker.requirePositive;

public record MultiActionTypeImpl(
    List<ActionButton> actions,
    @Nullable ActionButton exitAction,
    int columns
) implements MultiActionType {

    public MultiActionTypeImpl {
        actions = List.copyOf(actions);
    }

    public static final class BuilderImpl implements MultiActionType.Builder {
        private final List<ActionButton> actions;
        private @Nullable ActionButton exitAction = null;
        private int columns = 2;

        public BuilderImpl(final List<ActionButton> actions) {
            Preconditions.checkArgument(!actions.isEmpty(), "actions cannot be empty");
            this.actions = actions;
        }

        @Override
        public Builder exitAction(final @Nullable ActionButton exitAction) {
            this.exitAction = exitAction;
            return this;
        }

        @Override
        public Builder columns(final int columns) {
            this.columns = requirePositive(columns, "columns");
            return this;
        }

        @Override
        public MultiActionType build() {
            return new MultiActionTypeImpl(this.actions, this.exitAction, this.columns);
        }
    }
}
