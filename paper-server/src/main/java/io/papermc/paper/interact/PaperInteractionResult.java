package io.papermc.paper.interact;

import net.minecraft.world.InteractionResult;

public final class PaperInteractionResult {

    public record Success(InteractionResult.Success handle) implements io.papermc.paper.interact.InteractionResult.Success {

        @Override
        public boolean consumesAction() {
            return handle.consumesAction();
        }

        @Override
        public SwingSource swingSource() {
            return SwingSource.values()[handle.swingSource().ordinal()];
        }

        @Override
        public ItemContext itemContext() {
            return new PaperItemContext(handle.itemContext());
        }

    }

    public record Fail(InteractionResult.Fail handle) implements io.papermc.paper.interact.InteractionResult.Fail {

        @Override
        public boolean consumesAction() {
            return handle.consumesAction();
        }

    }

    public record Pass(InteractionResult.Pass handle) implements io.papermc.paper.interact.InteractionResult.Pass {

        @Override
        public boolean consumesAction() {
            return handle.consumesAction();
        }

    }

    public record TryEmptyHandInteraction(InteractionResult.TryEmptyHandInteraction handle) implements io.papermc.paper.interact.InteractionResult.TryEmptyHandInteraction {

        @Override
        public boolean consumesAction() {
            return handle.consumesAction();
        }

    }

}
