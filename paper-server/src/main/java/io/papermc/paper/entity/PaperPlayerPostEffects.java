package io.papermc.paper.entity;

import com.google.common.base.Preconditions;
import io.papermc.paper.adventure.PaperAdventure;
import java.util.LinkedHashSet;
import java.util.List;
import net.kyori.adventure.key.Key;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PaperPlayerPostEffects implements PlayerPostEffects {

    private final ServerPlayer handle;

    public PaperPlayerPostEffects(final ServerPlayer handle) {
        this.handle = handle;
    }

    private ServerPlayer getHandle() {
        return this.handle;
    }

    @Override
    public List<Key> values() {
        return this.getHandle().getPostEffects().stream().map(PaperAdventure::asAdventure).toList();
    }

    @Override
    public boolean set(final List<Key> postEffects) {
        Preconditions.checkArgument(postEffects != null, "postEffects cannot be null");
        final LinkedHashSet<Identifier> ids = new LinkedHashSet<>(postEffects.size());
        for (final Key effect : postEffects) {
            Preconditions.checkArgument(effect != null, "effects cannot be null");
            Preconditions.checkArgument(ids.add(PaperAdventure.asVanilla(effect)), "effects cannot be duplicate [%s]", effect);
        }
        return this.getHandle().setPostEffects(ids);
    }

    @Override
    public boolean add(final Key key) {
        Preconditions.checkArgument(key != null, "key cannot be null");
        return this.getHandle().addPostEffect(PaperAdventure.asVanilla(key));
    }

    @Override
    public boolean remove(final Key key) {
        Preconditions.checkArgument(key != null, "key cannot be null");
        return this.getHandle().removePostEffect(PaperAdventure.asVanilla(key));
    }

    @Override
    public boolean clear() {
        return this.getHandle().clearPostEffects();
    }

}
