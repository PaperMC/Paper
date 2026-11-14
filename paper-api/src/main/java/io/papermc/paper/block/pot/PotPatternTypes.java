package io.papermc.paper.block.pot;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;

/**
 * All pot pattern types.
 */
public final class PotPatternTypes {

    // Start generate - PotPatternTypes
    public static final PotPatternType ANGLER = getType("angler");

    public static final PotPatternType ARCHER = getType("archer");

    public static final PotPatternType ARMS_UP = getType("arms_up");

    public static final PotPatternType BLADE = getType("blade");

    public static final PotPatternType BREWER = getType("brewer");

    public static final PotPatternType BURN = getType("burn");

    public static final PotPatternType DANGER = getType("danger");

    public static final PotPatternType EXPLORER = getType("explorer");

    public static final PotPatternType FLOW = getType("flow");

    public static final PotPatternType FRIEND = getType("friend");

    public static final PotPatternType GUSTER = getType("guster");

    public static final PotPatternType HEART = getType("heart");

    public static final PotPatternType HEARTBREAK = getType("heartbreak");

    public static final PotPatternType HOWL = getType("howl");

    public static final PotPatternType MINER = getType("miner");

    public static final PotPatternType MOURNER = getType("mourner");

    public static final PotPatternType PLENTY = getType("plenty");

    public static final PotPatternType PRIZE = getType("prize");

    public static final PotPatternType SCRAPE = getType("scrape");

    public static final PotPatternType SHEAF = getType("sheaf");

    public static final PotPatternType SHELTER = getType("shelter");

    public static final PotPatternType SKULL = getType("skull");

    public static final PotPatternType SNORT = getType("snort");
    // End generate - PotPatternTypes

    private static PotPatternType getType(@KeyPattern.Value final String key) {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.DECORATED_POT_PATTERN).getOrThrow(Key.key(Key.MINECRAFT_NAMESPACE, key));
    }

    private PotPatternTypes() {
    }
}
