package io.papermc.paper.contract;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.InsideBlockEffectApplier.StepBasedCollector;
import net.minecraft.world.entity.InsideBlockEffectType;
import net.minecraft.world.entity.animal.Pig;
import org.bukkit.support.environment.AllFeatures;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * Simple test ensuring that the {@link StepBasedCollector} executes calls to {@link StepBasedCollector#runBefore(InsideBlockEffectType, Consumer)}
 * even if the effect is never registered.
 * <p>
 * Paper relies on this implementation detail to perform some events, specifically
 * - net.minecraft.world.level.block.LayeredCauldronBlock#entityInside
 */
@AllFeatures
public class StepBasedCollectorRunBeforeTest {

    @Test
    public void testExecuteRunBeforeWithoutEffect() {
        final StepBasedCollector stepBasedCollector = new StepBasedCollector();
        final AtomicBoolean triggered = new AtomicBoolean(false);
        final Entity entity = Mockito.mock(Entity.class);
        Mockito.when(entity.isAlive()).thenReturn(true);

        stepBasedCollector.runBefore(InsideBlockEffectType.EXTINGUISH, e -> triggered.set(true));
        stepBasedCollector.applyAndClear(entity);

        Assertions.assertTrue(triggered.get());
    }

}
