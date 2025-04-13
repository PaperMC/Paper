package io.papermc.paper;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.block.CraftBiome;
import org.bukkit.craftbukkit.damage.CraftDamageEffect;
import org.bukkit.damage.DamageEffect;
import org.jspecify.annotations.NullMarked;
import java.util.ArrayList;
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

    private final List<String> enabledGameRules = new ArrayList<>();

    @Override
    public boolean isGameRuleEnabled(final String gameRule) {
        if (enabledGameRules.isEmpty()) {
            MinecraftServer.getServer().getGameRules().visitGameRuleTypes(new GameRules.GameRuleTypeVisitor() {
                @Override
                public <T extends GameRules.Value<T>> void visit(final GameRules.Key<T> key, final GameRules.Type<T> type) {
                    enabledGameRules.add(key.toString());
                }
            });
        }
        return enabledGameRules.contains(gameRule);
    }
}
