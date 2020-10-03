package net.minecraft.server;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.serialization.DynamicLike;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameRules {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Map<GameRules.GameRuleKey<?>, GameRules.GameRuleDefinition<?>> I = Maps.newTreeMap(Comparator.comparing((gamerules_gamerulekey) -> {
        return gamerules_gamerulekey.a;
    }));
    public static final GameRules.GameRuleKey<GameRules.GameRuleBoolean> DO_FIRE_TICK = a("doFireTick", GameRules.GameRuleCategory.UPDATES, GameRules.GameRuleBoolean.b(true));
    public static final GameRules.GameRuleKey<GameRules.GameRuleBoolean> MOB_GRIEFING = a("mobGriefing", GameRules.GameRuleCategory.MOBS, GameRules.GameRuleBoolean.b(true));
    public static final GameRules.GameRuleKey<GameRules.GameRuleBoolean> KEEP_INVENTORY = a("keepInventory", GameRules.GameRuleCategory.PLAYER, GameRules.GameRuleBoolean.b(false));
    public static final GameRules.GameRuleKey<GameRules.GameRuleBoolean> DO_MOB_SPAWNING = a("doMobSpawning", GameRules.GameRuleCategory.SPAWNING, GameRules.GameRuleBoolean.b(true));
    public static final GameRules.GameRuleKey<GameRules.GameRuleBoolean> DO_MOB_LOOT = a("doMobLoot", GameRules.GameRuleCategory.DROPS, GameRules.GameRuleBoolean.b(true));
    public static final GameRules.GameRuleKey<GameRules.GameRuleBoolean> DO_TILE_DROPS = a("doTileDrops", GameRules.GameRuleCategory.DROPS, GameRules.GameRuleBoolean.b(true));
    public static final GameRules.GameRuleKey<GameRules.GameRuleBoolean> DO_ENTITY_DROPS = a("doEntityDrops", GameRules.GameRuleCategory.DROPS, GameRules.GameRuleBoolean.b(true));
    public static final GameRules.GameRuleKey<GameRules.GameRuleBoolean> COMMAND_BLOCK_OUTPUT = a("commandBlockOutput", GameRules.GameRuleCategory.CHAT, GameRules.GameRuleBoolean.b(true));
    public static final GameRules.GameRuleKey<GameRules.GameRuleBoolean> NATURAL_REGENERATION = a("naturalRegeneration", GameRules.GameRuleCategory.PLAYER, GameRules.GameRuleBoolean.b(true));
    public static final GameRules.GameRuleKey<GameRules.GameRuleBoolean> DO_DAYLIGHT_CYCLE = a("doDaylightCycle", GameRules.GameRuleCategory.UPDATES, GameRules.GameRuleBoolean.b(true));
    public static final GameRules.GameRuleKey<GameRules.GameRuleBoolean> LOG_ADMIN_COMMANDS = a("logAdminCommands", GameRules.GameRuleCategory.CHAT, GameRules.GameRuleBoolean.b(true));
    public static final GameRules.GameRuleKey<GameRules.GameRuleBoolean> SHOW_DEATH_MESSAGES = a("showDeathMessages", GameRules.GameRuleCategory.CHAT, GameRules.GameRuleBoolean.b(true));
    public static final GameRules.GameRuleKey<GameRules.GameRuleInt> RANDOM_TICK_SPEED = a("randomTickSpeed", GameRules.GameRuleCategory.UPDATES, GameRules.GameRuleInt.b(3));
    public static final GameRules.GameRuleKey<GameRules.GameRuleBoolean> SEND_COMMAND_FEEDBACK = a("sendCommandFeedback", GameRules.GameRuleCategory.CHAT, GameRules.GameRuleBoolean.b(true));
    public static final GameRules.GameRuleKey<GameRules.GameRuleBoolean> REDUCED_DEBUG_INFO = a("reducedDebugInfo", GameRules.GameRuleCategory.MISC, GameRules.GameRuleBoolean.b(false, (minecraftserver, gamerules_gameruleboolean) -> {
        int i = gamerules_gameruleboolean.a() ? 22 : 23;
        Iterator iterator = minecraftserver.getPlayerList().getPlayers().iterator();

        while (iterator.hasNext()) {
            EntityPlayer entityplayer = (EntityPlayer) iterator.next();

            entityplayer.playerConnection.sendPacket(new PacketPlayOutEntityStatus(entityplayer, (byte) i));
        }

    }));
    public static final GameRules.GameRuleKey<GameRules.GameRuleBoolean> SPECTATORS_GENERATE_CHUNKS = a("spectatorsGenerateChunks", GameRules.GameRuleCategory.PLAYER, GameRules.GameRuleBoolean.b(true));
    public static final GameRules.GameRuleKey<GameRules.GameRuleInt> SPAWN_RADIUS = a("spawnRadius", GameRules.GameRuleCategory.PLAYER, GameRules.GameRuleInt.b(10));
    public static final GameRules.GameRuleKey<GameRules.GameRuleBoolean> DISABLE_ELYTRA_MOVEMENT_CHECK = a("disableElytraMovementCheck", GameRules.GameRuleCategory.PLAYER, GameRules.GameRuleBoolean.b(false));
    public static final GameRules.GameRuleKey<GameRules.GameRuleInt> MAX_ENTITY_CRAMMING = a("maxEntityCramming", GameRules.GameRuleCategory.MOBS, GameRules.GameRuleInt.b(24));
    public static final GameRules.GameRuleKey<GameRules.GameRuleBoolean> DO_WEATHER_CYCLE = a("doWeatherCycle", GameRules.GameRuleCategory.UPDATES, GameRules.GameRuleBoolean.b(true));
    public static final GameRules.GameRuleKey<GameRules.GameRuleBoolean> DO_LIMITED_CRAFTING = a("doLimitedCrafting", GameRules.GameRuleCategory.PLAYER, GameRules.GameRuleBoolean.b(false));
    public static final GameRules.GameRuleKey<GameRules.GameRuleInt> MAX_COMMAND_CHAIN_LENGTH = a("maxCommandChainLength", GameRules.GameRuleCategory.MISC, GameRules.GameRuleInt.b(65536));
    public static final GameRules.GameRuleKey<GameRules.GameRuleBoolean> ANNOUNCE_ADVANCEMENTS = a("announceAdvancements", GameRules.GameRuleCategory.CHAT, GameRules.GameRuleBoolean.b(true));
    public static final GameRules.GameRuleKey<GameRules.GameRuleBoolean> DISABLE_RAIDS = a("disableRaids", GameRules.GameRuleCategory.MOBS, GameRules.GameRuleBoolean.b(false));
    public static final GameRules.GameRuleKey<GameRules.GameRuleBoolean> DO_INSOMNIA = a("doInsomnia", GameRules.GameRuleCategory.SPAWNING, GameRules.GameRuleBoolean.b(true));
    public static final GameRules.GameRuleKey<GameRules.GameRuleBoolean> DO_IMMEDIATE_RESPAWN = a("doImmediateRespawn", GameRules.GameRuleCategory.PLAYER, GameRules.GameRuleBoolean.b(false, (minecraftserver, gamerules_gameruleboolean) -> {
        Iterator iterator = minecraftserver.getPlayerList().getPlayers().iterator();

        while (iterator.hasNext()) {
            EntityPlayer entityplayer = (EntityPlayer) iterator.next();

            entityplayer.playerConnection.sendPacket(new PacketPlayOutGameStateChange(PacketPlayOutGameStateChange.l, gamerules_gameruleboolean.a() ? 1.0F : 0.0F));
        }

    }));
    public static final GameRules.GameRuleKey<GameRules.GameRuleBoolean> DROWNING_DAMAGE = a("drowningDamage", GameRules.GameRuleCategory.PLAYER, GameRules.GameRuleBoolean.b(true));
    public static final GameRules.GameRuleKey<GameRules.GameRuleBoolean> FALL_DAMAGE = a("fallDamage", GameRules.GameRuleCategory.PLAYER, GameRules.GameRuleBoolean.b(true));
    public static final GameRules.GameRuleKey<GameRules.GameRuleBoolean> FIRE_DAMAGE = a("fireDamage", GameRules.GameRuleCategory.PLAYER, GameRules.GameRuleBoolean.b(true));
    public static final GameRules.GameRuleKey<GameRules.GameRuleBoolean> DO_PATROL_SPAWNING = a("doPatrolSpawning", GameRules.GameRuleCategory.SPAWNING, GameRules.GameRuleBoolean.b(true));
    public static final GameRules.GameRuleKey<GameRules.GameRuleBoolean> DO_TRADER_SPAWNING = a("doTraderSpawning", GameRules.GameRuleCategory.SPAWNING, GameRules.GameRuleBoolean.b(true));
    public static final GameRules.GameRuleKey<GameRules.GameRuleBoolean> FORGIVE_DEAD_PLAYERS = a("forgiveDeadPlayers", GameRules.GameRuleCategory.MOBS, GameRules.GameRuleBoolean.b(true));
    public static final GameRules.GameRuleKey<GameRules.GameRuleBoolean> UNIVERSAL_ANGER = a("universalAnger", GameRules.GameRuleCategory.MOBS, GameRules.GameRuleBoolean.b(false));
    private final Map<GameRules.GameRuleKey<?>, GameRules.GameRuleValue<?>> J;

    private static <T extends GameRules.GameRuleValue<T>> GameRules.GameRuleKey<T> a(String s, GameRules.GameRuleCategory gamerules_gamerulecategory, GameRules.GameRuleDefinition<T> gamerules_gameruledefinition) {
        GameRules.GameRuleKey<T> gamerules_gamerulekey = new GameRules.GameRuleKey<>(s, gamerules_gamerulecategory);
        GameRules.GameRuleDefinition<?> gamerules_gameruledefinition1 = (GameRules.GameRuleDefinition) GameRules.I.put(gamerules_gamerulekey, gamerules_gameruledefinition);

        if (gamerules_gameruledefinition1 != null) {
            throw new IllegalStateException("Duplicate game rule registration for " + s);
        } else {
            return gamerules_gamerulekey;
        }
    }

    public GameRules(DynamicLike<?> dynamiclike) {
        this();
        this.a(dynamiclike);
    }

    public GameRules() {
        this.J = (Map) GameRules.I.entrySet().stream().collect(ImmutableMap.toImmutableMap(Entry::getKey, (entry) -> {
            return ((GameRules.GameRuleDefinition) entry.getValue()).getValue();
        }));
    }

    private GameRules(Map<GameRules.GameRuleKey<?>, GameRules.GameRuleValue<?>> map) {
        this.J = map;
    }

    public <T extends GameRules.GameRuleValue<T>> T get(GameRules.GameRuleKey<T> gamerules_gamerulekey) {
        return (T) this.J.get(gamerules_gamerulekey); // CraftBukkit - decompile error
    }

    public NBTTagCompound a() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        this.J.forEach((gamerules_gamerulekey, gamerules_gamerulevalue) -> {
            nbttagcompound.setString(gamerules_gamerulekey.a, gamerules_gamerulevalue.getValue());
        });
        return nbttagcompound;
    }

    private void a(DynamicLike<?> dynamiclike) {
        this.J.forEach((gamerules_gamerulekey, gamerules_gamerulevalue) -> {
            dynamiclike.get(gamerules_gamerulekey.a).asString().result().ifPresent(gamerules_gamerulevalue::setValue);
        });
    }

    public GameRules b() {
        return new GameRules((Map) this.J.entrySet().stream().collect(ImmutableMap.toImmutableMap(Entry::getKey, (entry) -> {
            return ((GameRules.GameRuleValue) entry.getValue()).f();
        })));
    }

    public static void a(GameRules.GameRuleVisitor gamerules_gamerulevisitor) {
        GameRules.I.forEach((gamerules_gamerulekey, gamerules_gameruledefinition) -> {
            a(gamerules_gamerulevisitor, gamerules_gamerulekey, gamerules_gameruledefinition);
        });
    }

    private static <T extends GameRules.GameRuleValue<T>> void a(GameRules.GameRuleVisitor gamerules_gamerulevisitor, GameRules.GameRuleKey<?> gamerules_gamerulekey, GameRules.GameRuleDefinition<?> gamerules_gameruledefinition) {
        gamerules_gamerulevisitor.a((GameRules.GameRuleKey<T>) gamerules_gamerulekey, (GameRules.GameRuleDefinition<T>) gamerules_gameruledefinition); // CraftBukkit - decompile error
        ((GameRules.GameRuleDefinition<T>) gamerules_gameruledefinition).a(gamerules_gamerulevisitor, (GameRules.GameRuleKey<T>) gamerules_gamerulekey); // CraftBukkit - decompile error
    }

    public boolean getBoolean(GameRules.GameRuleKey<GameRules.GameRuleBoolean> gamerules_gamerulekey) {
        return ((GameRules.GameRuleBoolean) this.get(gamerules_gamerulekey)).a();
    }

    public int getInt(GameRules.GameRuleKey<GameRules.GameRuleInt> gamerules_gamerulekey) {
        return ((GameRules.GameRuleInt) this.get(gamerules_gamerulekey)).a();
    }

    public static class GameRuleBoolean extends GameRules.GameRuleValue<GameRules.GameRuleBoolean> {

        private boolean b;

        private static GameRules.GameRuleDefinition<GameRules.GameRuleBoolean> b(boolean flag, BiConsumer<MinecraftServer, GameRules.GameRuleBoolean> biconsumer) {
            return new GameRules.GameRuleDefinition<>(BoolArgumentType::bool, (gamerules_gameruledefinition) -> {
                return new GameRules.GameRuleBoolean(gamerules_gameruledefinition, flag);
            }, biconsumer, GameRules.GameRuleVisitor::b);
        }

        private static GameRules.GameRuleDefinition<GameRules.GameRuleBoolean> b(boolean flag) {
            return b(flag, (minecraftserver, gamerules_gameruleboolean) -> {
            });
        }

        public GameRuleBoolean(GameRules.GameRuleDefinition<GameRules.GameRuleBoolean> gamerules_gameruledefinition, boolean flag) {
            super(gamerules_gameruledefinition);
            this.b = flag;
        }

        @Override
        protected void a(CommandContext<CommandListenerWrapper> commandcontext, String s) {
            this.b = BoolArgumentType.getBool(commandcontext, s);
        }

        public boolean a() {
            return this.b;
        }

        public void a(boolean flag, @Nullable MinecraftServer minecraftserver) {
            this.b = flag;
            this.onChange(minecraftserver);
        }

        @Override
        public String getValue() {
            return Boolean.toString(this.b);
        }

        @Override
        public void setValue(String s) { // PAIL - protected->public
            this.b = Boolean.parseBoolean(s);
        }

        @Override
        public int getIntValue() {
            return this.b ? 1 : 0;
        }

        @Override
        protected GameRules.GameRuleBoolean g() {
            return this;
        }

        @Override
        protected GameRules.GameRuleBoolean f() {
            return new GameRules.GameRuleBoolean(this.a, this.b);
        }
    }

    public static class GameRuleInt extends GameRules.GameRuleValue<GameRules.GameRuleInt> {

        private int b;

        private static GameRules.GameRuleDefinition<GameRules.GameRuleInt> a(int i, BiConsumer<MinecraftServer, GameRules.GameRuleInt> biconsumer) {
            return new GameRules.GameRuleDefinition<>(IntegerArgumentType::integer, (gamerules_gameruledefinition) -> {
                return new GameRules.GameRuleInt(gamerules_gameruledefinition, i);
            }, biconsumer, GameRules.GameRuleVisitor::c);
        }

        private static GameRules.GameRuleDefinition<GameRules.GameRuleInt> b(int i) {
            return a(i, (minecraftserver, gamerules_gameruleint) -> {
            });
        }

        public GameRuleInt(GameRules.GameRuleDefinition<GameRules.GameRuleInt> gamerules_gameruledefinition, int i) {
            super(gamerules_gameruledefinition);
            this.b = i;
        }

        @Override
        protected void a(CommandContext<CommandListenerWrapper> commandcontext, String s) {
            this.b = IntegerArgumentType.getInteger(commandcontext, s);
        }

        public int a() {
            return this.b;
        }

        @Override
        public String getValue() {
            return Integer.toString(this.b);
        }

        @Override
        public void setValue(String s) { // PAIL - protected->public
            this.b = c(s);
        }

        private static int c(String s) {
            if (!s.isEmpty()) {
                try {
                    return Integer.parseInt(s);
                } catch (NumberFormatException numberformatexception) {
                    GameRules.LOGGER.warn("Failed to parse integer {}", s);
                }
            }

            return 0;
        }

        @Override
        public int getIntValue() {
            return this.b;
        }

        @Override
        protected GameRules.GameRuleInt g() {
            return this;
        }

        @Override
        protected GameRules.GameRuleInt f() {
            return new GameRules.GameRuleInt(this.a, this.b);
        }
    }

    public abstract static class GameRuleValue<T extends GameRules.GameRuleValue<T>> {

        protected final GameRules.GameRuleDefinition<T> a;

        public GameRuleValue(GameRules.GameRuleDefinition<T> gamerules_gameruledefinition) {
            this.a = gamerules_gameruledefinition;
        }

        protected abstract void a(CommandContext<CommandListenerWrapper> commandcontext, String s);

        public void b(CommandContext<CommandListenerWrapper> commandcontext, String s) {
            this.a(commandcontext, s);
            this.onChange(((CommandListenerWrapper) commandcontext.getSource()).getServer());
        }

        public void onChange(@Nullable MinecraftServer minecraftserver) {
            if (minecraftserver != null) {
                this.a.c.accept(minecraftserver, this.g());
            }

        }

        public abstract void setValue(String s); // PAIL - private->public

        public abstract String getValue();

        public String toString() {
            return this.getValue();
        }

        public abstract int getIntValue();

        protected abstract T g();

        protected abstract T f();
    }

    public static class GameRuleDefinition<T extends GameRules.GameRuleValue<T>> {

        private final Supplier<ArgumentType<?>> a;
        private final Function<GameRules.GameRuleDefinition<T>, T> b;
        private final BiConsumer<MinecraftServer, T> c;
        private final GameRules.h<T> d;

        private GameRuleDefinition(Supplier<ArgumentType<?>> supplier, Function<GameRules.GameRuleDefinition<T>, T> function, BiConsumer<MinecraftServer, T> biconsumer, GameRules.h<T> gamerules_h) {
            this.a = supplier;
            this.b = function;
            this.c = biconsumer;
            this.d = gamerules_h;
        }

        public RequiredArgumentBuilder<CommandListenerWrapper, ?> a(String s) {
            return CommandDispatcher.a(s, (ArgumentType) this.a.get());
        }

        public T getValue() {
            return this.b.apply(this); // CraftBukkit - decompile error
        }

        public void a(GameRules.GameRuleVisitor gamerules_gamerulevisitor, GameRules.GameRuleKey<T> gamerules_gamerulekey) {
            this.d.call(gamerules_gamerulevisitor, gamerules_gamerulekey, this);
        }
    }

    public static final class GameRuleKey<T extends GameRules.GameRuleValue<T>> {

        private final String a;
        private final GameRules.GameRuleCategory b;

        public GameRuleKey(String s, GameRules.GameRuleCategory gamerules_gamerulecategory) {
            this.a = s;
            this.b = gamerules_gamerulecategory;
        }

        public String toString() {
            return this.a;
        }

        public boolean equals(Object object) {
            return this == object ? true : object instanceof GameRules.GameRuleKey && ((GameRules.GameRuleKey) object).a.equals(this.a);
        }

        public int hashCode() {
            return this.a.hashCode();
        }

        public String a() {
            return this.a;
        }

        public String b() {
            return "gamerule." + this.a;
        }
    }

    public interface GameRuleVisitor {

        default <T extends GameRules.GameRuleValue<T>> void a(GameRules.GameRuleKey<T> gamerules_gamerulekey, GameRules.GameRuleDefinition<T> gamerules_gameruledefinition) {}

        default void b(GameRules.GameRuleKey<GameRules.GameRuleBoolean> gamerules_gamerulekey, GameRules.GameRuleDefinition<GameRules.GameRuleBoolean> gamerules_gameruledefinition) {}

        default void c(GameRules.GameRuleKey<GameRules.GameRuleInt> gamerules_gamerulekey, GameRules.GameRuleDefinition<GameRules.GameRuleInt> gamerules_gameruledefinition) {}
    }

    interface h<T extends GameRules.GameRuleValue<T>> {

        void call(GameRules.GameRuleVisitor gamerules_gamerulevisitor, GameRules.GameRuleKey<T> gamerules_gamerulekey, GameRules.GameRuleDefinition<T> gamerules_gameruledefinition);
    }

    public static enum GameRuleCategory {

        PLAYER("gamerule.category.player"), MOBS("gamerule.category.mobs"), SPAWNING("gamerule.category.spawning"), DROPS("gamerule.category.drops"), UPDATES("gamerule.category.updates"), CHAT("gamerule.category.chat"), MISC("gamerule.category.misc");

        private final String h;

        private GameRuleCategory(String s) {
            this.h = s;
        }
    }
}
