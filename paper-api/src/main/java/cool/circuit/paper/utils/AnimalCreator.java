package cool.circuit.paper.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public final class AnimalCreator {

    //Circuit board fork start

    public void summonAnimal(final EntityType type, final Location location, final String displayName) {
        final World world = location.getWorld();

        if (world == null) return;

        final Entity entity = world.spawnEntity(location, type);

        entity.setCustomName(displayName);

        entity.setCustomNameVisible(true);
    }

    public void summonAnimal(final EntityType type, final Location location, final Component displayName) {
        final World world = location.getWorld();

        if (world == null) return;

        final Entity entity = world.spawnEntity(location, type);

        entity.customName(displayName);

        entity.setCustomNameVisible(true);
    }

    //Circuit Board fork end
}
