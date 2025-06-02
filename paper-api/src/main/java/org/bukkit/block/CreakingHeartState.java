package org.bukkit.block;

/**
 * Represents the state of a Creaking Heart block.
 * <p>
 * The Creaking Heart can be in one of three states:
 * <ul>
 * <li>UPROOTED - The heart is not active.</li>
 * <li>DORMANT - The heart is dormant, waiting for conditions to change.</li>
 * <li>AWAKE - The heart is active and can spawn a Creaking.</li>
 * </ul>
 */
public enum CreakingHeartState {
    UPROOTED("uprooted"),
    DORMANT("dormant"),
    AWAKE("awake");

    private final String name;

    private CreakingHeartState(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

}
