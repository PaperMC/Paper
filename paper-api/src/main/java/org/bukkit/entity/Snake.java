package org.bukkit.entity;

import org.jetbrains.annotations.NotNull;

public interface Snake {
    void move();

    void bite(final @NotNull Player player);

    void spawn();

    void die();

    void hiss();
}
