package io.papermc.paper.world;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.registry.HolderableBase;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.OptionalInt;
import net.kyori.adventure.key.Key;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import org.bukkit.craftbukkit.CraftRegistry;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class PaperTimeline extends HolderableBase<net.minecraft.world.timeline.Timeline> implements Timeline {

    public static Timeline minecraftHolderToBukkit(final Holder<net.minecraft.world.timeline.Timeline> minecraft) {
        return CraftRegistry.minecraftHolderToBukkit(minecraft, Registries.TIMELINE);
    }

    public static Holder<net.minecraft.world.timeline.Timeline> bukkitToMinecraftHolder(final Timeline bukkit) {
        return CraftRegistry.bukkitToMinecraftHolder(bukkit);
    }

    public PaperTimeline(final Holder<net.minecraft.world.timeline.Timeline> holder) {
        super(holder);
    }

    @Override
    public WorldClock clock() {
        return PaperWorldClock.minecraftHolderToBukkit(this.getHandle().clock());
    }

    @Override
    public OptionalInt periodTicks() {
        return this.getHandle().periodTicks().map(OptionalInt::of).orElse(OptionalInt.empty());
    }

    @Override
    public @Unmodifiable Map<Key, TimeMarker> timeMarkers() {
        final Map<Key, TimeMarker> timeMarkers = new LinkedHashMap<>();
        this.getHandle().registerTimeMarkers((key, marker) ->
            timeMarkers.put(PaperAdventure.asAdventure(key.identifier()), TimeMarker.of(marker.ticks(), marker.showInCommands()))
        );
        return Map.copyOf(timeMarkers);
    }
}
