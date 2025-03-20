package org.bukkit.craftbukkit.scoreboard;

abstract class CraftScoreboardComponent {
    private final CraftScoreboard scoreboard;

    CraftScoreboardComponent(CraftScoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }

    public CraftScoreboard getScoreboard() {
        return this.scoreboard;
    }

    abstract void checkState();

    abstract void unregister();
}
