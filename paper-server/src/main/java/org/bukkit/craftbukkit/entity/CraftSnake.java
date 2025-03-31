package org.bukkit.craftbukkit.entity;

import cool.circuit.paper.utils.GradientComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snake;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CraftSnake implements Snake {

    private final Location location;
    private final List<ArmorStand> body;
    private ArmorStand head;
    private ArmorStand tail;

    /**
     * Constructs a new CraftSnake at the given location.
     *
     * @param location The location of the snake.
     */
    public CraftSnake(final @NotNull Location location) {
        this.location = location;
        this.body = new ArrayList<>();
    }

    /**
     * Moves the snake towards the nearest player within range.
     */
    @Override
    public void move() {
        if (location == null || !location.isWorldLoaded() || Double.isNaN(location.getX()) || Double.isNaN(location.getY()) || Double.isNaN(location.getZ())) {
            return;
        }

        final Player player = location.getNearbyPlayers(3).stream().findFirst().orElse(null);
        if (player == null) {
            return;
        }

        final Vector direction = player.getLocation().toVector().subtract(location.toVector());
        if (Double.isNaN(direction.getX()) || Double.isNaN(direction.getY()) || Double.isNaN(direction.getZ())) {
            return;
        }

        direction.normalize();
        final Location newLocation = location.clone().add(direction.multiply(0.4));

        if (!newLocation.isFinite()) {
            return;
        }

        if (head != null) {
            Location previousLocation = head.getLocation();
            head.teleport(newLocation);

            for (final ArmorStand segment : body) {
                final Location temp = segment.getLocation();
                segment.teleport(previousLocation);
                previousLocation = temp;
            }

            if (tail != null) {
                tail.teleport(previousLocation);
            }
        }

        final Player playerToBite = newLocation.getNearbyPlayers(1.5).stream().findFirst().orElse(null);
        if (playerToBite != null) {
            bite(playerToBite);
        }

        this.location.setX(newLocation.getX());
        this.location.setY(newLocation.getY());
        this.location.setZ(newLocation.getZ());
    }

    /**
     * Damages the player if bitten by the snake.
     *
     * @param player The player being bitten by the snake.
     */
    @Override
    public void bite(final @NotNull Player player) {
        player.damage(5);
        player.sendMessage(new GradientComponent(TextColor.color(255, 0, 0), TextColor.color(255, 100, 0), "[!] You were bitten by a snake").getComponent());
    }

    /**
     * Spawns the snake by creating the head, body, and tail at the specified location.
     */
    @Override
    public void spawn() {
        this.head = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        head.setVisible(false);
        head.setInvulnerable(true);
        head.setGravity(false);
        head.setSmall(true);
        head.setCanPickupItems(false);
        head.setMarker(true);
        head.setHelmet(new ItemStack(Material.RED_WOOL));

        Location currentLocation = location.clone();

        for (int i = 1; i <= 5; i++) {
            currentLocation.subtract(0.4, 0, 0);
            final ArmorStand bodyPart = (ArmorStand) location.getWorld().spawnEntity(currentLocation, EntityType.ARMOR_STAND);
            bodyPart.setVisible(false);
            bodyPart.setInvulnerable(true);
            bodyPart.setGravity(false);
            bodyPart.setSmall(true);
            bodyPart.setHelmet(new ItemStack(Material.GREEN_WOOL));
            bodyPart.setCanPickupItems(false);
            bodyPart.setMarker(true);
            body.add(bodyPart);
        }

        Location tailLocation = body.getLast().getLocation();
        this.tail = (ArmorStand) location.getWorld().spawnEntity(tailLocation, EntityType.ARMOR_STAND);
        tail.setVisible(false);
        tail.setInvulnerable(true);
        tail.setGravity(false);
        tail.setSmall(true);
        tail.setCanPickupItems(false);
        tail.setMarker(true);
        tail.setHelmet(new ItemStack(Material.YELLOW_WOOL));
    }

    /**
     * Removes the snake from the world by deleting the head, body, and tail.
     */
    @Override
    public void die() {
        if (head != null) {
            head.remove();
            head = null;
        }

        if (tail != null) {
            tail.remove();
            tail = null;
        }

        for (final ArmorStand stand : body) {
            stand.remove();
        }
        body.clear();
    }

    /**
     * Plays a hissing sound at the snake's location.
     */
    @Override
    public void hiss() {
        location.getWorld().playSound(location, Sound.ENTITY_TNT_PRIMED, 1.0f, 1.0f);
    }
}
