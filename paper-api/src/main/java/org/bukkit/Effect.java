package org.bukkit;

import com.google.common.collect.Maps;
import java.util.Map;
import org.bukkit.block.BlockFace;
import org.bukkit.potion.Potion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A list of effects that the server is able to send to players.
 */
public enum Effect {
    /**
     * An alternate click sound.
     */
    CLICK2(1000, Type.SOUND),
    /**
     * A click sound.
     */
    CLICK1(1001, Type.SOUND),
    /**
     * Sound of a bow firing.
     */
    BOW_FIRE(1002, Type.SOUND),
    /**
     * Sound of a door opening.
     * @deprecated no longer exists
     * @see Sound#BLOCK_WOODEN_DOOR_OPEN
     */
    @Deprecated
    DOOR_TOGGLE(1006, Type.SOUND),
    /**
     * Sound of a door opening.
     * @deprecated no longer exists
     * @see Sound#BLOCK_IRON_DOOR_OPEN
     */
    @Deprecated
    IRON_DOOR_TOGGLE(1005, Type.SOUND),
    /**
     * Sound of a trapdoor opening.
     * @deprecated no longer exists
     * @see Sound#BLOCK_WOODEN_TRAPDOOR_OPEN
     */
    @Deprecated
    TRAPDOOR_TOGGLE(1007, Type.SOUND),
    /**
     * Sound of a door opening.
     * @deprecated no longer exists
     * @see Sound#BLOCK_IRON_TRAPDOOR_OPEN
     */
    @Deprecated
    IRON_TRAPDOOR_TOGGLE(1037, Type.SOUND),
    /**
     * Sound of a door opening.
     * @deprecated no longer exists
     * @see Sound#BLOCK_FENCE_GATE_OPEN
     */
    @Deprecated
    FENCE_GATE_TOGGLE(1008, Type.SOUND),
    /**
     * Sound of a door closing.
     * @deprecated no longer exists
     * @see Sound#BLOCK_WOODEN_DOOR_CLOSE
     */
    @Deprecated
    DOOR_CLOSE(1012, Type.SOUND),
    /**
     * Sound of a door closing.
     * @deprecated no longer exists
     * @see Sound#BLOCK_IRON_DOOR_CLOSE
     */
    @Deprecated
    IRON_DOOR_CLOSE(1011, Type.SOUND),
    /**
     * Sound of a trapdoor closing.
     * @deprecated no longer exists
     * @see Sound#BLOCK_WOODEN_TRAPDOOR_CLOSE
     */
    @Deprecated
    TRAPDOOR_CLOSE(1013, Type.SOUND),
    /**
     * Sound of a door closing.
     * @deprecated no longer exists
     * @see Sound#BLOCK_IRON_TRAPDOOR_CLOSE
     */
    @Deprecated
    IRON_TRAPDOOR_CLOSE(1036, Type.SOUND),
    /**
     * Sound of a door closing.
     * @deprecated no longer exists
     * @see Sound#BLOCK_FENCE_GATE_CLOSE
     */
    @Deprecated
    FENCE_GATE_CLOSE(1014, Type.SOUND),
    /**
     * Sound of fire being extinguished.
     */
    EXTINGUISH(1009, Type.SOUND),
    /**
     * A song from a record. Needs the record item ID as additional info
     */
    RECORD_PLAY(1010, Type.SOUND, Material.class),
    /**
     * Sound of ghast shrieking.
     */
    GHAST_SHRIEK(1015, Type.SOUND),
    /**
     * Sound of ghast firing.
     */
    GHAST_SHOOT(1016, Type.SOUND),
    /**
     * Sound of blaze firing.
     */
    BLAZE_SHOOT(1018, Type.SOUND),
    /**
     * Sound of zombies chewing on wooden doors.
     */
    ZOMBIE_CHEW_WOODEN_DOOR(1019, Type.SOUND),
    /**
     * Sound of zombies chewing on iron doors.
     */
    ZOMBIE_CHEW_IRON_DOOR(1020, Type.SOUND),
    /**
     * Sound of zombies destroying a door.
     */
    ZOMBIE_DESTROY_DOOR(1021, Type.SOUND),
    /**
     * A visual smoke effect. Needs direction as additional info.
     */
    SMOKE(2000, Type.VISUAL, BlockFace.class),
    /**
     * Sound of a block breaking. Needs block ID as additional info.
     */
    STEP_SOUND(2001, Type.SOUND, Material.class),
    /**
     * Visual effect of a splash potion breaking. Needs potion data value as
     * additional info.
     */
    POTION_BREAK(2002, Type.VISUAL, Potion.class),
    /**
     * Visual effect of an instant splash potion breaking. Needs color data
     * value as additional info.
     */
    INSTANT_POTION_BREAK(2007, Type.VISUAL, Color.class),
    /**
     * An ender eye signal; a visual effect.
     */
    ENDER_SIGNAL(2003, Type.VISUAL),
    /**
     * The flames seen on a mobspawner; a visual effect.
     */
    MOBSPAWNER_FLAMES(2004, Type.VISUAL),
    /**
     * The sound played by brewing stands when brewing
     */
    BREWING_STAND_BREW(1035, Type.SOUND),
    /**
     * The sound played when a chorus flower grows
     */
    CHORUS_FLOWER_GROW(1033, Type.SOUND),
    /**
     * The sound played when a chorus flower dies
     */
    CHORUS_FLOWER_DEATH(1034, Type.SOUND),
    /**
     * The sound played when traveling through a portal
     */
    PORTAL_TRAVEL(1032, Type.SOUND),
    /**
     * The sound played when launching an endereye
     */
    ENDEREYE_LAUNCH(1003, Type.SOUND),
    /**
     * The sound played when launching a firework
     */
    FIREWORK_SHOOT(1004, Type.SOUND),
    /**
     * Particles displayed when a villager grows a plant, data
     * is the number of particles
     */
    VILLAGER_PLANT_GROW(2005, Type.VISUAL, Integer.class),
    /**
     * The sound/particles used by the enderdragon's breath
     * attack.
     */
    DRAGON_BREATH(2006, Type.VISUAL),
    /**
     * The sound played when an anvil breaks
     */
    ANVIL_BREAK(1029, Type.SOUND),
    /**
     * The sound played when an anvil is used
     */
    ANVIL_USE(1030, Type.SOUND),
    /**
     * The sound played when an anvil lands after
     * falling
     */
    ANVIL_LAND(1031, Type.SOUND),
    /**
     * Sound of an enderdragon firing
     */
    ENDERDRAGON_SHOOT(1017, Type.SOUND),
    /**
     * The sound played when a wither breaks a block
     */
    WITHER_BREAK_BLOCK(1022, Type.SOUND),
    /**
     * Sound of a wither shooting
     */
    WITHER_SHOOT(1024, Type.SOUND),
    /**
     * The sound played when a zombie infects a target
     */
    ZOMBIE_INFECT(1026, Type.SOUND),
    /**
     * The sound played when a villager is converted by
     * a zombie
     */
    ZOMBIE_CONVERTED_VILLAGER(1027, Type.SOUND),
    /**
     * Sound played by a bat taking off
     */
    BAT_TAKEOFF(1025, Type.SOUND),
    /**
     * The sound/particles caused by a end gateway spawning
     */
    END_GATEWAY_SPAWN(3000, Type.VISUAL),
    /**
     * The sound of an enderdragon growling
     */
    ENDERDRAGON_GROWL(3001, Type.SOUND),
    /**
     * The sound played when phantom bites.
     */
    PHANTOM_BITE(1039, Type.SOUND),
    /**
     * The sound played when a zombie converts to a drowned.
     */
    ZOMBIE_CONVERTED_TO_DROWNED(1040, Type.SOUND),
    /**
     * The sound played when a husk converts to a zombie.
     */
    HUSK_CONVERTED_TO_ZOMBIE(1041, Type.SOUND),
    /**
     * The sound played when a grindstone is being used.
     */
    GRINDSTONE_USE(1042, Type.SOUND),
    /**
     * The sound played when a book page is being turned.
     */
    BOOK_PAGE_TURN(1043, Type.SOUND),
    /**
     * The sound played when a smithing table is being used.
     */
    SMITHING_TABLE_USE(1044, Type.SOUND),
    /**
     * The sound played when a pointed dripstone hits the surface.
     */
    POINTED_DRIPSTONE_LAND(1045, Type.SOUND),
    /**
     * The sound played when a pointed dripstone drips lava into a cauldron.
     */
    POINTED_DRIPSTONE_DRIP_LAVA_INTO_CAULDRON(1046, Type.SOUND),
    /**
     * The sound played when a pointed dripstone drips water into a cauldron.
     */
    POINTED_DRIPSTONE_DRIP_WATER_INTO_CAULDRON(1047, Type.SOUND),
    /**
     * The sound played when a skeleton converts to a stray.
     */
    SKELETON_CONVERTED_TO_STRAY(1048, Type.SOUND),
    /**
     * The sound played / particles shown when a composter is being attempted to
     * fill.
     *
     * True for a successful attempt false for an unsuccessful attempt.
     */
    COMPOSTER_FILL_ATTEMPT(1500, Type.VISUAL, Boolean.class),
    /**
     * The sound played / particles shown when lava interacts with the world.
     *
     * For example by forming stone, obsidian, basalt or destroying blocks such
     * as torches.
     */
    LAVA_INTERACT(1501, Type.VISUAL),
    /**
     * The sound played / particles shown when a redstone torch burns out.
     */
    REDSTONE_TORCH_BURNOUT(1502, Type.VISUAL),
    /**
     * The sound played / particles shown when an eye of ender is placed into an
     * ender portal frame.
     */
    END_PORTAL_FRAME_FILL(1503, Type.VISUAL),
    /**
     * The particles shown when a dripstone drips lava or water.
     *
     * This effect requires a dripstone at the location as well as lava or water
     * at the root of the dripstone.
     */
    DRIPPING_DRIPSTONE(1504, Type.VISUAL),
    /**
     * The sound played / particles shown when bone meal is used to grow a
     * plant.
     *
     * Data is the number of particles.
     */
    BONE_MEAL_USE(1505, Type.VISUAL, Integer.class),
    /**
     * The particles shown when an ender dragon destroys blocks.
     */
    ENDER_DRAGON_DESTROY_BLOCK(2008, Type.VISUAL),
    /**
     * The particles shown when a sponge dries in an ultra warm world (nether).
     */
    SPONGE_DRY(2009, Type.VISUAL),
    /**
     * The particles shown when a lightning hits a lightning rod or oxidized
     * copper.
     *
     * Data is the axis at which the particle should be shown. If no data is
     * provided it will show the particles at the block faces.
     */
    ELECTRIC_SPARK(3002, Type.VISUAL, Axis.class),
    /**
     * The sound played / particles shown when wax is applied to a copper block.
     */
    COPPER_WAX_ON(3003, Type.VISUAL),
    /**
     * The particles shown when wax is removed from a copper block.
     */
    COPPER_WAX_OFF(3004, Type.VISUAL),
    /**
     * The particles shown when oxidation is scraped of an oxidized copper
     * block.
     */
    OXIDISED_COPPER_SCRAPE(3005, Type.VISUAL),
    ;

    private final int id;
    private final Type type;
    private final Class<?> data;
    private static final Map<Integer, Effect> BY_ID = Maps.newHashMap();

    Effect(int id, /*@NotNull*/ Type type) {
        this(id, type, null);
    }

    Effect(int id, /*@NotNull*/ Type type, /*@Nullable*/ Class<?> data) {
        this.id = id;
        this.type = type;
        this.data = data;
    }

    /**
     * Gets the ID for this effect.
     *
     * @return ID of this effect
     * @deprecated Magic value
     */
    @Deprecated
    public int getId() {
        return this.id;
    }

    /**
     * @return The type of the effect.
     */
    @NotNull
    public Type getType() {
        return this.type;
    }

    /**
     * @return The class which represents data for this effect, or null if
     *     none
     */
    @Nullable
    public Class<?> getData() {
        return this.data;
    }

    /**
     * Gets the Effect associated with the given ID.
     *
     * @param id ID of the Effect to return
     * @return Effect with the given ID
     * @deprecated Magic value
     */
    @Deprecated
    @Nullable
    public static Effect getById(int id) {
        return BY_ID.get(id);
    }

    static {
        for (Effect effect : values()) {
            BY_ID.put(effect.id, effect);
        }
    }

    /**
     * Represents the type of an effect.
     */
    public enum Type { SOUND, VISUAL }
}
