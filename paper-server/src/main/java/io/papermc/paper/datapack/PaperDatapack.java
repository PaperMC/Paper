package io.papermc.paper.datapack;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.event.server.ServerResourcesReloadedEvent;
import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.repository.Pack;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class PaperDatapack extends PaperDiscoveredDatapack implements Datapack {

    private final Pack pack;
    private final boolean enabled;

    PaperDatapack(final Pack pack, final boolean enabled) {
        super(pack);
        this.pack = pack;
        this.enabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public void setEnabled(final boolean enabled) {
        final MinecraftServer server = MinecraftServer.getServer();
        final List<Pack> enabledPacks = new ArrayList<>(server.getPackRepository().getSelectedPacks());
        final Pack packToChange = server.getPackRepository().getPack(this.getName());
        if (packToChange == null) {
            throw new IllegalStateException("Cannot toggle state of pack that doesn't exist: " + this.getName());
        }
        if (enabled == enabledPacks.contains(packToChange)) {
            return;
        }
        if (enabled) {
            packToChange.getDefaultPosition().insert(enabledPacks, packToChange, Pack::selectionConfig, false); // modeled off the default /datapack enable logic
        } else {
            enabledPacks.remove(packToChange);
        }
        server.reloadResources(enabledPacks.stream().map(Pack::getId).toList(), ServerResourcesReloadedEvent.Cause.PLUGIN);
    }

    @Override
    public Component computeDisplayName() {
        return PaperAdventure.asAdventure(this.pack.getChatLink(this.enabled));
    }
}
