package net.minecraft.world.entity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.entity.CraftAbstractArrow;
import org.bukkit.support.environment.VanillaFeature;
import org.junit.jupiter.api.Test;

@VanillaFeature
class ProjectileInitialPositionTest {
    private final World world = mock(World.class);
    private final AbstractArrow arrow = mock(AbstractArrow.class);
    private final CraftAbstractArrow projectile = mock(CraftAbstractArrow.class, CALLS_REAL_METHODS);

    private void prepare() {
        doReturn(world).when(projectile).getWorld();
        doReturn(arrow).when(projectile).getHandle();
        when(arrow.isAlive()).thenReturn(true);
    }

    @Test
    void initialPositionChangesOnlyNativeCoordinatesBeforeInsertion() {
        prepare();
        projectile.setPositionBeforeSpawn(new Location(world, 2, 3, 4));
        verify(arrow).setPos(2, 3, 4);
    }

    @Test
    void spawnedOrTickedOrDeadProjectileCannotUseInitialPositionApi() {
        prepare();
        Location position = new Location(world, 2, 3, 4);
        arrow.valid = true;
        assertThrows(IllegalStateException.class, () -> projectile.setPositionBeforeSpawn(position));
        arrow.valid = false;
        arrow.tickCount = 1;
        assertThrows(IllegalStateException.class, () -> projectile.setPositionBeforeSpawn(position));
        arrow.tickCount = 0;
        when(arrow.isAlive()).thenReturn(false);
        assertThrows(IllegalStateException.class, () -> projectile.setPositionBeforeSpawn(position));
        verify(arrow, never()).setPos(anyDouble(), anyDouble(), anyDouble());
    }

    @Test
    void crossWorldNullAndNonFiniteCoordinatesAreRejected() {
        prepare();
        assertThrows(IllegalArgumentException.class, () -> projectile.setPositionBeforeSpawn(null));
        assertThrows(IllegalArgumentException.class, () -> projectile.setPositionBeforeSpawn(new Location(mock(World.class), 2, 3, 4)));
        assertThrows(IllegalArgumentException.class, () -> projectile.setPositionBeforeSpawn(new Location(world, Double.NaN, 3, 4)));
        verify(arrow, never()).setPos(anyDouble(), anyDouble(), anyDouble());
    }
}
