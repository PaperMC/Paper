package org.bukkit.craftbukkit;

import static org.bukkit.Sound.*;

import org.apache.commons.lang.Validate;
import org.bukkit.Sound;

public class CraftSound {
    private static String[] sounds = new String[Sound.values().length];

    static {
        sounds[AMBIENCE_CAVE.ordinal()] = "ambient.cave.cave";
        sounds[AMBIENCE_RAIN.ordinal()] = "ambient.weather.rain";
        sounds[AMBIENCE_THUNDER.ordinal()] = "ambient.weather.thunder";
        sounds[ARROW_HIT.ordinal()] = "random.bowhit";
        sounds[ARROW_SHAKE.ordinal()] = "random.drr";
        sounds[BREATH.ordinal()] = "random.breath";
        sounds[BURP.ordinal()] = "random.burp";
        sounds[CHEST_CLOSE.ordinal()] = "random.chestclosed";
        sounds[CHEST_OPEN.ordinal()] = "random.chestopen";
        sounds[CLICK.ordinal()] = "random.click";
        sounds[DOOR_CLOSE.ordinal()] = "random.door_close";
        sounds[DOOR_OPEN.ordinal()] = "random.door_open";
        sounds[DRINK.ordinal()] = "random.drink";
        sounds[EAT.ordinal()] = "random.eat";
        sounds[EXPLODE.ordinal()] = "random.explode";
        sounds[EXPLODE_OLD.ordinal()] = "random.old_explode";
        sounds[FALL_BIG.ordinal()] = "damage.fallbig";
        sounds[FALL_SMALL.ordinal()] = "damage.fallsmall";
        sounds[FIRE.ordinal()] = "fire.fire";
        sounds[FIRE_IGNITE.ordinal()] = "fire.ignite";
        sounds[FIZZ.ordinal()] = "random.fizz";
        sounds[FUSE.ordinal()] = "random.fuse";
        sounds[HURT.ordinal()] = "random.hurt";
        sounds[HURT_FLESH.ordinal()] = "damage.hurtflesh";
        sounds[ITEM_BREAK.ordinal()] = "random.break";
        sounds[ITEM_PICKUP.ordinal()] = "random.pop";
        sounds[LAVA.ordinal()] = "liquid.lava";
        sounds[LAVA_POP.ordinal()] = "liquid.lavapop";
        sounds[LEVEL_UP.ordinal()] = "random.levelup";
        sounds[NOTE_PIANO.ordinal()] = "note.harp";
        sounds[NOTE_BASS_DRUM.ordinal()] = "note.bd";
        sounds[NOTE_STICKS.ordinal()] = "note.hat";
        sounds[NOTE_BASS_GUITAR.ordinal()] = "note.bassattack";
        sounds[NOTE_SNARE_DRUM.ordinal()] = "note.snare";
        // NOTE_BASS("note.bass"),
        sounds[NOTE_PLING.ordinal()] = "note.pling";
        sounds[ORB_PICKUP.ordinal()] = "random.orb";
        sounds[PISTON_EXTEND.ordinal()] = "tile.piston.out";
        sounds[PISTON_RETRACT.ordinal()] = "tile.piston.in";
        sounds[PORTAL.ordinal()] = "portal.portal";
        sounds[PORTAL_TRAVEL.ordinal()] = "portal.travel";
        sounds[PORTAL_TRIGGER.ordinal()] = "portal.trigger";
        sounds[SHOOT_ARROW.ordinal()] = "random.bow";
        sounds[SPLASH.ordinal()] = "random.splash";
        sounds[SPLASH2.ordinal()] = "liquid.splash";
        sounds[STEP_GRAVEL.ordinal()] = "step.gravel";
        sounds[STEP_SAND.ordinal()] = "step.sand";
        sounds[STEP_SNOW.ordinal()] = "step.snow";
        sounds[STEP_STONE.ordinal()] = "step.stone";
        sounds[STEP_WOOD.ordinal()] = "step.wood";
        sounds[STEP_WOOL.ordinal()] = "step.wool";
        sounds[WATER.ordinal()] = "liquid.water";
        sounds[WOOD_CLICK.ordinal()] = "random.wood click";
        // Mob sounds
        sounds[BLAZE_BREATH.ordinal()] = "mob.blaze.breath";
        sounds[BLAZE_DEATH.ordinal()] = "mob.blaze.death";
        sounds[BLAZE_HIT.ordinal()] = "mob.blaze.hit";
        sounds[CAT_HISS.ordinal()] = "mob.cat.hiss";
        sounds[CAT_HIT.ordinal()] = "mob.cat.hitt";
        sounds[CAT_MEOW.ordinal()] = "mob.cat.meow";
        sounds[CAT_PURR.ordinal()] = "mob.cat.purr";
        sounds[CAT_PURREOW.ordinal()] = "mob.cat.purreow";
        sounds[CHICKEN_IDLE.ordinal()] = "mob.chicken";
        sounds[CHICKEN_HURT.ordinal()] = "mob.chickenhurt";
        sounds[CHICKEN_EGG_POP.ordinal()] = "mob.chickenplop";
        sounds[COW_IDLE.ordinal()] = "mob.cow";
        sounds[COW_HURT.ordinal()] = "mob.cowhurt";
        sounds[CREEPER_HISS.ordinal()] = "mob.creeper";
        sounds[CREEPER_DEATH.ordinal()] = "mob.creeperdeath";
        sounds[ENDERMAN_DEATH.ordinal()] = "mob.endermen.death";
        sounds[ENDERMAN_HIT.ordinal()] = "mob.endermen.hit";
        sounds[ENDERMAN_IDLE.ordinal()] = "mob.endermen.idle";
        sounds[ENDERMAN_TELEPORT.ordinal()] = "mob.endermen.portal";
        sounds[ENDERMAN_SCREAM.ordinal()] = "mob.endermen.scream";
        sounds[ENDERMAN_STARE.ordinal()] = "mob.endermen.stare";
        sounds[GHAST_SCREAM.ordinal()] = "mob.ghast.scream";
        sounds[GHAST_SCREAM2.ordinal()] = "mob.ghast.affectionate scream";
        sounds[GHAST_CHARGE.ordinal()] = "mob.ghast.charge";
        sounds[GHAST_DEATH.ordinal()] = "mob.ghast.death";
        sounds[GHAST_FIREBALL.ordinal()] = "mob.ghast.fireball";
        sounds[GHAST_MOAN.ordinal()] = "mob.ghast.moan";
        sounds[IRONGOLEM_DEATH.ordinal()] = "mob.irongolem.death";
        sounds[IRONGOLEM_HIT.ordinal()] = "mob.irongolem.hit";
        sounds[IRONGOLEM_THROW.ordinal()] = "mob.irongolem.throw";
        sounds[IRONGOLEM_WALK.ordinal()] = "mob.irongolem.walk";
        sounds[MAGMACUBE_WALK.ordinal()] = "mob.magmacube.small";
        sounds[MAGMACUBE_WALK2.ordinal()] = "mob.magmacube.big";
        sounds[MAGMACUBE_JUMP.ordinal()] = "mob.magmacube.jump";
        sounds[PIG_IDLE.ordinal()] = "mob.pig";
        sounds[PIG_DEATH.ordinal()] = "mob.pigdeath";
        sounds[SHEEP_IDLE.ordinal()] = "mob.sheep";
        sounds[SILVERFISH_HIT.ordinal()] = "mob.silverfish.hit";
        sounds[SILVERFISH_KILL.ordinal()] = "mob.silverfish.kill";
        sounds[SILVERFISH_IDLE.ordinal()] = "mob.silverfish.say";
        sounds[SILVERFISH_WALK.ordinal()] = "mob.silverfish.step";
        sounds[SKELETON_IDLE.ordinal()] = "mob.skeleton";
        sounds[SKELETON_DEATH.ordinal()] = "mob.skeletondeath";
        sounds[SKELETON_HURT.ordinal()] = "mob.skeletonhurt";
        sounds[SLIME_IDLE.ordinal()] = "mob.slime";
        sounds[SLIME_ATTACK.ordinal()] = "mob.slimeattack";
        sounds[SPIDER_IDLE.ordinal()] = "mob.spider";
        sounds[SPIDER_DEATH.ordinal()] = "mob.spiderdeath";
        sounds[WOLF_BARK.ordinal()] = "mob.wolf.bark";
        sounds[WOLF_DEATH.ordinal()] = "mob.wolf.death";
        sounds[WOLF_GROWL.ordinal()] = "mob.wolf.growl";
        sounds[WOLF_HOWL.ordinal()] = "mob.wolf.howl";
        sounds[WOLF_HURT.ordinal()] = "mob.wolf.hurt";
        sounds[WOLF_PANT.ordinal()] = "mob.wolf.panting";
        sounds[WOLF_SHAKE.ordinal()] = "mob.wolf.shake";
        sounds[WOLF_WHINE.ordinal()] = "mob.wolf.whine";
        sounds[ZOMBIE_METAL.ordinal()] = "mob.zombie.metal";
        sounds[ZOMBIE_WOOD.ordinal()] = "mob.zombie.wood";
        sounds[ZOMBIE_WOODBREAK.ordinal()] = "mob.zombie.woodbreak";
        sounds[ZOMBIE_IDLE.ordinal()] = "mob.zombie";
        sounds[ZOMBIE_DEATH.ordinal()] = "mob.zombiedeath";
        sounds[ZOMBIE_HURT.ordinal()] = "mob.zombiehurt";
        sounds[ZOMBIE_PIG_IDLE.ordinal()] = "mob.zombiepig.zpig";
        sounds[ZOMBIE_PIG_ANGRY.ordinal()] = "mob.zombiepig.zpigangry";
        sounds[ZOMBIE_PIG_DEATH.ordinal()] = "mob.zombiepig.zpigdeath";
        sounds[ZOMBIE_PIG_HURT.ordinal()] = "mob.zombiepig.zpighurt";
    }

    public static String getSound(final Sound sound) {
        Validate.notNull(sound, "Sound cannot be null");
        return sounds[sound.ordinal()];
    }

    private CraftSound() {}
}
