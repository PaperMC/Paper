package org.bukkit.block.pot;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import org.bukkit.Keyed;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface PotPatternType extends Keyed {

    // Start generate - PotPatternType
    PotPatternType ANGLER = getType("angler");

    PotPatternType ARCHER = getType("archer");

    PotPatternType ARMS_UP = getType("arms_up");

    PotPatternType BLADE = getType("blade");

    PotPatternType BREWER = getType("brewer");

    PotPatternType BURN = getType("burn");

    PotPatternType DANGER = getType("danger");

    PotPatternType EXPLORER = getType("explorer");

    PotPatternType FLOW = getType("flow");

    PotPatternType FRIEND = getType("friend");

    PotPatternType GUSTER = getType("guster");

    PotPatternType HEART = getType("heart");

    PotPatternType HEARTBREAK = getType("heartbreak");

    PotPatternType HOWL = getType("howl");

    PotPatternType MINER = getType("miner");

    PotPatternType MOURNER = getType("mourner");

    PotPatternType PLENTY = getType("plenty");

    PotPatternType PRIZE = getType("prize");

    PotPatternType SCRAPE = getType("scrape");

    PotPatternType SHEAF = getType("sheaf");

    PotPatternType SHELTER = getType("shelter");

    PotPatternType SKULL = getType("skull");

    PotPatternType SNORT = getType("snort");
    // End generate - PotPatternType

    private static PotPatternType getType(final @KeyPattern.Value String key) {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.DECORATED_POT_PATTERN).getOrThrow(Key.key(Key.MINECRAFT_NAMESPACE, key));
    }
}
