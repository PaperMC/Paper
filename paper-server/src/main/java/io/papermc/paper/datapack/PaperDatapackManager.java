package io.papermc.paper.datapack;

import com.google.common.collect.Collections2;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public class PaperDatapackManager implements DatapackManager {

    private final PackRepository repository;

    public PaperDatapackManager(final PackRepository repository) {
        this.repository = repository;
    }

    @Override
    public void refreshPacks() {
        this.repository.reload();
    }

    @Override
    public @Nullable Datapack getPack(final String name) {
        final @Nullable Pack pack = this.repository.getPack(name);
        if (pack == null) {
            return null;
        }
        return new PaperDatapack(pack, this.repository.getSelectedPacks().contains(pack));
    }

    @Override
    public Collection<Datapack> getPacks() {
        final Collection<Pack> enabledPacks = this.repository.getSelectedPacks();
        return this.transformPacks(this.repository.getAvailablePacks(), enabledPacks::contains);
    }

    @Override
    public Collection<Datapack> getEnabledPacks() {
        return this.transformPacks(this.repository.getSelectedPacks(), pack -> true);
    }

    private Collection<Datapack> transformPacks(final Collection<Pack> packs, final Predicate<Pack> enabled) {
        return Collections.unmodifiableCollection(
            Collections2.transform(
                packs,
                pack -> new PaperDatapack(pack, enabled.test(pack))
            )
        );
    }
}
