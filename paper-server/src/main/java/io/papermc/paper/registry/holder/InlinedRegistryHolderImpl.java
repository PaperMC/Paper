package io.papermc.paper.registry.holder;

import io.papermc.paper.registry.PaperRegistries;
import io.papermc.paper.registry.RegistryElement;
import io.papermc.paper.registry.RegistryKey;
import net.minecraft.core.Holder;
import org.bukkit.Keyed;
import org.bukkit.craftbukkit.CraftRegistry;

record InlinedRegistryHolderImpl<API extends Keyed & RegistryElement.Buildable<API, ENTRY, ?>, ENTRY, M>(
    RegistryKey<API> registryKey, ENTRY entry, Holder.Direct<M> holder
) implements RegistryHolder.Inlined<API, ENTRY> { // TODO remove Keyed

    @Override
    public API value() {
        //noinspection RedundantTypeArguments
        return CraftRegistry.<API, M>minecraftHolderToBukkit(this.holder, PaperRegistries.registryToNms(this.registryKey));
    }
}
