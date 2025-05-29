package io.papermc.paper;

import io.papermc.paper.adventure.ChatProcessor;
import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.chat.ChatTypeParameter;
import io.papermc.paper.chat.vanilla.ChatTypeRenderer;
import io.papermc.paper.util.MCUtil;
import io.papermc.paper.world.damagesource.CombatEntry;
import io.papermc.paper.world.damagesource.FallLocationType;
import io.papermc.paper.world.damagesource.PaperCombatEntryWrapper;
import io.papermc.paper.world.damagesource.PaperCombatTrackerWrapper;
import net.kyori.adventure.chat.ChatType;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.format.Style;
import net.minecraft.Optionull;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.ChatTypeDecoration;
import net.minecraft.world.damagesource.FallLocation;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.block.CraftBiome;
import org.bukkit.craftbukkit.damage.CraftDamageEffect;
import org.bukkit.craftbukkit.damage.CraftDamageSource;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.damage.DamageEffect;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import java.util.List;

@NullMarked
public class PaperServerInternalAPIBridge implements InternalAPIBridge {
    public static final PaperServerInternalAPIBridge INSTANCE = new PaperServerInternalAPIBridge();

    @Override
    public DamageEffect getDamageEffect(final String key) {
        return CraftDamageEffect.getById(key);
    }

    @Override
    public Biome constructLegacyCustomBiome() {
        class Holder {
            static final Biome LEGACY_CUSTOM = new CraftBiome.LegacyCustomBiomeImpl();
        }
        return Holder.LEGACY_CUSTOM;
    }

    @Override
    public CombatEntry createCombatEntry(final LivingEntity entity, final DamageSource damageSource, final float damage) {
        final net.minecraft.world.entity.LivingEntity mob = ((CraftLivingEntity) entity).getHandle();
        final FallLocation fallLocation = FallLocation.getCurrentFallLocation(mob);
        return createCombatEntry(
            ((CraftDamageSource) damageSource).getHandle(),
            damage,
            fallLocation,
            (float) mob.fallDistance
        );
    }

    @Override
    public CombatEntry createCombatEntry(
        final DamageSource damageSource,
        final float damage,
        @Nullable final FallLocationType fallLocationType,
        final float fallDistance
    ) {
        return createCombatEntry(
            ((CraftDamageSource) damageSource).getHandle(),
            damage,
            Optionull.map(fallLocationType, PaperCombatTrackerWrapper::paperToMinecraft),
            fallDistance
        );
    }

    private CombatEntry createCombatEntry(
        final net.minecraft.world.damagesource.DamageSource damageSource,
        final float damage,
        final net.minecraft.world.damagesource.@Nullable FallLocation fallLocation,
        final float fallDistance
    ) {
        return new PaperCombatEntryWrapper(new net.minecraft.world.damagesource.CombatEntry(
            damageSource, damage, fallLocation, fallDistance
        ));
    }

    @Override
    public ChatType createAdventureChatType(final String format, final String narration, final Style style, final List<ChatTypeParameter> parameters) {
        List<ChatTypeDecoration.Parameter> parameterList = MCUtil.transformUnmodifiable(parameters, chatTypeParameter -> ChatTypeDecoration.Parameter.values()[chatTypeParameter.ordinal()]);
        net.minecraft.network.chat.Style nmsType = PaperAdventure.asVanilla(style);

        net.minecraft.network.chat.ChatType chatType = new net.minecraft.network.chat.ChatType(
            new ChatTypeDecoration(format, parameterList, nmsType),
            new ChatTypeDecoration(narration, parameterList, nmsType)
        );
        return new PaperInternalChatType(Holder.direct(chatType));
    }

    public record PaperInternalChatType(Holder<net.minecraft.network.chat.ChatType> chatType) implements ChatType {

        @Override
        public @NotNull Key key() {
            throw new IllegalStateException("Cannot get key for this registry item, because it is not registered.");
        }
    }

    @Override
    public ChatRenderer createVanillaChatRenderer(final ChatTypeRenderer renderer) {
        return ChatProcessor.PaperVanillaChatRenderer.of(renderer);
    }
}
