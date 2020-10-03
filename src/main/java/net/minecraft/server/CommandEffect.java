package net.minecraft.server;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.Collection;
import java.util.Iterator;
import javax.annotation.Nullable;

public class CommandEffect {

    private static final SimpleCommandExceptionType a = new SimpleCommandExceptionType(new ChatMessage("commands.effect.give.failed"));
    private static final SimpleCommandExceptionType b = new SimpleCommandExceptionType(new ChatMessage("commands.effect.clear.everything.failed"));
    private static final SimpleCommandExceptionType c = new SimpleCommandExceptionType(new ChatMessage("commands.effect.clear.specific.failed"));

    public static void a(com.mojang.brigadier.CommandDispatcher<CommandListenerWrapper> com_mojang_brigadier_commanddispatcher) {
        com_mojang_brigadier_commanddispatcher.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) CommandDispatcher.a("effect").requires((commandlistenerwrapper) -> {
            return commandlistenerwrapper.hasPermission(2);
        })).then(((LiteralArgumentBuilder) CommandDispatcher.a("clear").executes((commandcontext) -> {
            return a((CommandListenerWrapper) commandcontext.getSource(), ImmutableList.of(((CommandListenerWrapper) commandcontext.getSource()).g()));
        })).then(((RequiredArgumentBuilder) CommandDispatcher.a("targets", (ArgumentType) ArgumentEntity.multipleEntities()).executes((commandcontext) -> {
            return a((CommandListenerWrapper) commandcontext.getSource(), ArgumentEntity.b(commandcontext, "targets"));
        })).then(CommandDispatcher.a("effect", (ArgumentType) ArgumentMobEffect.a()).executes((commandcontext) -> {
            return a((CommandListenerWrapper) commandcontext.getSource(), ArgumentEntity.b(commandcontext, "targets"), ArgumentMobEffect.a(commandcontext, "effect"));
        }))))).then(CommandDispatcher.a("give").then(CommandDispatcher.a("targets", (ArgumentType) ArgumentEntity.multipleEntities()).then(((RequiredArgumentBuilder) CommandDispatcher.a("effect", (ArgumentType) ArgumentMobEffect.a()).executes((commandcontext) -> {
            return a((CommandListenerWrapper) commandcontext.getSource(), ArgumentEntity.b(commandcontext, "targets"), ArgumentMobEffect.a(commandcontext, "effect"), (Integer) null, 0, true);
        })).then(((RequiredArgumentBuilder) CommandDispatcher.a("seconds", (ArgumentType) IntegerArgumentType.integer(1, 1000000)).executes((commandcontext) -> {
            return a((CommandListenerWrapper) commandcontext.getSource(), ArgumentEntity.b(commandcontext, "targets"), ArgumentMobEffect.a(commandcontext, "effect"), IntegerArgumentType.getInteger(commandcontext, "seconds"), 0, true);
        })).then(((RequiredArgumentBuilder) CommandDispatcher.a("amplifier", (ArgumentType) IntegerArgumentType.integer(0, 255)).executes((commandcontext) -> {
            return a((CommandListenerWrapper) commandcontext.getSource(), ArgumentEntity.b(commandcontext, "targets"), ArgumentMobEffect.a(commandcontext, "effect"), IntegerArgumentType.getInteger(commandcontext, "seconds"), IntegerArgumentType.getInteger(commandcontext, "amplifier"), true);
        })).then(CommandDispatcher.a("hideParticles", (ArgumentType) BoolArgumentType.bool()).executes((commandcontext) -> {
            return a((CommandListenerWrapper) commandcontext.getSource(), ArgumentEntity.b(commandcontext, "targets"), ArgumentMobEffect.a(commandcontext, "effect"), IntegerArgumentType.getInteger(commandcontext, "seconds"), IntegerArgumentType.getInteger(commandcontext, "amplifier"), !BoolArgumentType.getBool(commandcontext, "hideParticles"));
        }))))))));
    }

    private static int a(CommandListenerWrapper commandlistenerwrapper, Collection<? extends Entity> collection, MobEffectList mobeffectlist, @Nullable Integer integer, int i, boolean flag) throws CommandSyntaxException {
        int j = 0;
        int k;

        if (integer != null) {
            if (mobeffectlist.isInstant()) {
                k = integer;
            } else {
                k = integer * 20;
            }
        } else if (mobeffectlist.isInstant()) {
            k = 1;
        } else {
            k = 600;
        }

        Iterator iterator = collection.iterator();

        while (iterator.hasNext()) {
            Entity entity = (Entity) iterator.next();

            if (entity instanceof EntityLiving) {
                MobEffect mobeffect = new MobEffect(mobeffectlist, k, i, false, flag);

                if (((EntityLiving) entity).addEffect(mobeffect, org.bukkit.event.entity.EntityPotionEffectEvent.Cause.COMMAND)) { // CraftBukkit
                    ++j;
                }
            }
        }

        if (j == 0) {
            throw CommandEffect.a.create();
        } else {
            if (collection.size() == 1) {
                commandlistenerwrapper.sendMessage(new ChatMessage("commands.effect.give.success.single", new Object[]{mobeffectlist.d(), ((Entity) collection.iterator().next()).getScoreboardDisplayName(), k / 20}), true);
            } else {
                commandlistenerwrapper.sendMessage(new ChatMessage("commands.effect.give.success.multiple", new Object[]{mobeffectlist.d(), collection.size(), k / 20}), true);
            }

            return j;
        }
    }

    private static int a(CommandListenerWrapper commandlistenerwrapper, Collection<? extends Entity> collection) throws CommandSyntaxException {
        int i = 0;
        Iterator iterator = collection.iterator();

        while (iterator.hasNext()) {
            Entity entity = (Entity) iterator.next();

            if (entity instanceof EntityLiving && ((EntityLiving) entity).removeAllEffects(org.bukkit.event.entity.EntityPotionEffectEvent.Cause.COMMAND)) { // CraftBukkit
                ++i;
            }
        }

        if (i == 0) {
            throw CommandEffect.b.create();
        } else {
            if (collection.size() == 1) {
                commandlistenerwrapper.sendMessage(new ChatMessage("commands.effect.clear.everything.success.single", new Object[]{((Entity) collection.iterator().next()).getScoreboardDisplayName()}), true);
            } else {
                commandlistenerwrapper.sendMessage(new ChatMessage("commands.effect.clear.everything.success.multiple", new Object[]{collection.size()}), true);
            }

            return i;
        }
    }

    private static int a(CommandListenerWrapper commandlistenerwrapper, Collection<? extends Entity> collection, MobEffectList mobeffectlist) throws CommandSyntaxException {
        int i = 0;
        Iterator iterator = collection.iterator();

        while (iterator.hasNext()) {
            Entity entity = (Entity) iterator.next();

            if (entity instanceof EntityLiving && ((EntityLiving) entity).removeEffect(mobeffectlist, org.bukkit.event.entity.EntityPotionEffectEvent.Cause.COMMAND)) { // CraftBukkit
                ++i;
            }
        }

        if (i == 0) {
            throw CommandEffect.c.create();
        } else {
            if (collection.size() == 1) {
                commandlistenerwrapper.sendMessage(new ChatMessage("commands.effect.clear.specific.success.single", new Object[]{mobeffectlist.d(), ((Entity) collection.iterator().next()).getScoreboardDisplayName()}), true);
            } else {
                commandlistenerwrapper.sendMessage(new ChatMessage("commands.effect.clear.specific.success.multiple", new Object[]{mobeffectlist.d(), collection.size()}), true);
            }

            return i;
        }
    }
}
