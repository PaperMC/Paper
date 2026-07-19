package io.papermc.paper.world;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@Normal
class ContainerOpenersCounterTest {

    @Test
    void delayedCallbackContainersOpenSynchronously() {
        final RecordingOpenersCounter counter = new RecordingOpenersCounter();
        final LivingEntity entity = Mockito.mock(LivingEntity.class);
        final Level level = Mockito.mock(Level.class);
        final BlockState state = Mockito.mock(BlockState.class);

        counter.incrementOpeners(entity, level, BlockPos.ZERO, state, 0.0);

        assertTrue(counter.openedSynchronously);
        assertEquals(1, counter.getOpenerCount());
    }

    private static final class RecordingOpenersCounter extends ContainerOpenersCounter {
        private boolean openedSynchronously;

        @Override
        protected void onOpen(final Level level, final BlockPos pos, final BlockState blockState) {
            this.openedSynchronously = true;
        }

        @Override
        protected void onClose(final Level level, final BlockPos pos, final BlockState blockState) {
        }

        @Override
        protected void openerCountChanged(final Level level, final BlockPos pos, final BlockState blockState, final int previous, final int current) {
        }

        @Override
        public boolean delayCallbacks() {
            return true;
        }

        @Override
        public boolean isOwnContainer(final Player player) {
            return false;
        }
    }
}
