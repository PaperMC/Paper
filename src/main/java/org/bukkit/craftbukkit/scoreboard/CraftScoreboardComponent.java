package org.bukkit.craftbukkit.scoreboard;

abstract class CraftScoreboardComponent {
    private CraftScoreboard scoreboard;

    CraftScoreboardComponent(CraftScoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }

    CraftScoreboard checkState() throws IllegalStateException {
        CraftScoreboard scoreboard = this.scoreboard;
        if (scoreboard == null) {
            throw new IllegalStateException("Unregistered scoreboard component");
        }
        return scoreboard;
    }

    public CraftScoreboard getScoreboard() {
        return scoreboard;
    }

    abstract void unregister() throws IllegalStateException;

    final void setUnregistered() {
        scoreboard = null;
    }
}
