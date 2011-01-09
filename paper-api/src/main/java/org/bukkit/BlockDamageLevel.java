package org.bukkit;

public enum BlockDamageLevel {
    STARTED(0), DIGGING(1), BROKEN(3), STOPPED(2);

    private int level;

    private BlockDamageLevel(final int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
