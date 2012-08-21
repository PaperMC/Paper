package org.bukkit;

import java.util.HashMap;
import java.util.Map;

/**
 * An Enum of Sounds the server is able to send to players.
 * <p />
 * WARNING: At any time, sounds may be added/removed from this Enum or even MineCraft itself! There is no guarantee the sounds will play.
 * There is no guarantee values will not be removed from this Enum. As such, you should not depend on the ordinal values of this class.
 */
public enum Sound {
    AMBIENCE_CAVE("ambient.cave.cave"),
    AMBIENCE_RAIN("ambient.weather.rain"),
    AMBIENCE_THUNDER("ambient.weather.thunder"),
    ARROW_HIT("random.bowhit"),
    ARROW_SHAKE("random.drr"),
    BREATH("random.breath"),
    BURP("random.burp"),
    CHEST_CLOSE("random.chestclosed"),
    CHEST_OPEN("random.chestopen"),
    CLICK("random.click"),
    DOOR_CLOSE("random.door_close"),
    DOOR_OPEN("random.door_open"),
    DRINK("random.drink"),
    EAT("random.eat"),
    EXPLODE("random.explode"),
    EXPLODE_OLD("random.old_explode"),
    FALL_BIG("damage.fallbig"),
    FALL_SMALL("damage.fallsmall"),
    FIRE("fire.fire"),
    FIRE_IGNITE("fire.ignite"),
    FIZZ("random.fizz"),
    FUSE("random.fuse"),
    HURT("random.hurt"),
    HURT_FLESH("damage.hurtflesh"),
    ITEM_BREAK("random.break"),
    ITEM_PICKUP("random.pop"),
    LAVA("liquid.lava"),
    LAVA_POP("liquid.lavapop"),
    LEVEL_UP("random.levelup"),
    NOTE_PIANO("note.harp"),
    NOTE_BASS_DRUM("note.bd"),
    NOTE_STICKS("note.hat"),
    NOTE_BASS_GUITAR("note.bassattack"),
    NOTE_SNARE_DRUM("note.snare"),
    // NOTE_BASS("note.bass"),
    NOTE_PLING("note.pling"),
    ORB_PICKUP("random.orb"),
    PISTON_EXTEND("tile.piston.out"),
    PISTON_RETRACT("tile.piston.in"),
    PORTAL("portal.portal"),
    PORTAL_TRAVEL("portal.travel"),
    PORTAL_TRIGGER("portal.trigger"),
    SHOOT_ARROW("random.bow"),
    SPLASH("random.splash"),
    SPLASH2("liquid.splash"),
    STEP_GRAVEL("step.gravel"),
    STEP_SAND("step.sand"),
    STEP_SNOW("step.snow"),
    STEP_STONE("step.stone"),
    STEP_WOOD("step.wood"),
    STEP_WOOL("step.wool"),
    WATER("liquid.water"),
    WOOD_CLICK("random.wood click"),
    // Mob sounds
    BLAZE_BREATH("mob.blaze.breath"),
    BLAZE_DEATH("mob.blaze.death"),
    BLAZE_HIT("mob.blaze.hit"),
    CAT_HISS("mob.cat.hiss"),
    CAT_HIT("mob.cat.hitt"),
    CAT_MEOW("mob.cat.meow"),
    CAT_PURR("mob.cat.purr"),
    CAT_PURREOW("mob.cat.purreow"),
    CHICKEN_IDLE("mob.chicken"),
    CHICKEN_HURT("mob.chickenhurt"),
    CHICKEN_EGG_POP("mob.chickenplop"),
    COW_IDLE("mob.cow"),
    COW_HURT("mob.cowhurt"),
    CREEPER_HISS("mob.creeper"),
    CREEPER_DEATH("mob.creeperdeath"),
    ENDERMAN_DEATH("mob.endermen.death"),
    ENDERMAN_HIT("mob.endermen.hit"),
    ENDERMAN_IDLE("mob.endermen.idle"),
    ENDERMAN_TELEPORT("mob.endermen.portal"),
    ENDERMAN_SCREAM("mob.endermen.scream"),
    ENDERMAN_STARE("mob.endermen.stare"),
    GHAST_SCREAM("mob.ghast.scream"),
    GHAST_SCREAM2("mob.ghast.affectionate scream"),
    GHAST_CHARGE("mob.ghast.charge"),
    GHAST_DEATH("mob.ghast.death"),
    GHAST_FIREBALL("mob.ghast.fireball"),
    GHAST_MOAN("mob.ghast.moan"),
    IRONGOLEM_DEATH("mob.irongolem.death"),
    IRONGOLEM_HIT("mob.irongolem.hit"),
    IRONGOLEM_THROW("mob.irongolem.throw"),
    IRONGOLEM_WALK("mob.irongolem.walk"),
    MAGMACUBE_WALK("mob.magmacube.small"),
    MAGMACUBE_WALK2("mob.magmacube.big"),
    MAGMACUBE_JUMP("mob.magmacube.jump"),
    PIG_IDLE("mob.pig"),
    PIG_DEATH("mob.pigdeath"),
    SHEEP_IDLE("mob.sheep"),
    SILVERFISH_HIT("mob.silverfish.hit"),
    SILVERFISH_KILL("mob.silverfish.kill"),
    SILVERFISH_IDLE("mob.silverfish.say"),
    SILVERFISH_WALK("mob.silverfish.step"),
    SKELETON_IDLE("mob.skeleton"),
    SKELETON_DEATH("mob.skeletondeath"),
    SKELETON_HURT("mob.skeletonhurt"),
    SLIME_IDLE("mob.slime"),
    SLIME_ATTACK("mob.slimeattack"),
    SPIDER_IDLE("mob.spider"),
    SPIDER_DEATH("mob.spiderdeath"),
    WOLF_BARK("mob.wolf.bark"),
    WOLF_DEATH("mob.wolf.death"),
    WOLF_GROWL("mob.wolf.growl"),
    WOLF_HOWL("mob.wolf.howl"),
    WOLF_HURT("mob.wolf.hurt"),
    WOLF_PANT("mob.wolf.panting"),
    WOLF_SHAKE("mob.wolf.shake"),
    WOLF_WHINE("mob.wolf.whine"),
    ZOMBIE_METAL("mob.zombie.metal"),
    ZOMBIE_WOOD("mob.zombie.wood"),
    ZOMBIE_WOODBREAK("mob.zombie.woodbreak"),
    ZOMBIE_IDLE("mob.zombie"),
    ZOMBIE_DEATH("mob.zombiedeath"),
    ZOMBIE_HURT("mob.zombiehurt"),
    ZOMBIE_PIG_IDLE("mob.zombiepig.zpig"),
    ZOMBIE_PIG_ANGRY("mob.zombiepig.zpigangry"),
    ZOMBIE_PIG_DEATH("mob.zombiepig.zpigdeath"),
    ZOMBIE_PIG_HURT("mob.zombiepig.zpighurt");

    private final String sound;

    private static final Map<String, Sound> byName = new HashMap<String, Sound>();

    Sound(String name) {
        sound = name;
    }

    public String getSound() {
        return sound;
    }

    /**
     * Look up a sound by it's raw sound name
     *
     * @param name The Sounds raw name to look up
     * @return Sound if it exists, null if not
     */
    public static Sound getSound(String name) {
        return byName.get(name);
    }

    static {
        for (Sound sound : values()) {
            byName.put(sound.getSound(), sound);
        }
    }
}
