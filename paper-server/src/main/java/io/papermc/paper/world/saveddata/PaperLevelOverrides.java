package io.papermc.paper.world.saveddata;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.level.storage.PrimaryLevelData;
import net.minecraft.world.level.storage.ServerLevelData;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class PaperLevelOverrides extends SavedData implements ServerLevelData {
    private static final Codec<GameType> LEGACY_GAME_TYPE_CODEC = Codec.INT.xmap(GameType::byId, GameType::getId);
    public static final Codec<PaperLevelOverrides> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RespawnData.CODEC.fieldOf("spawn").forGetter(levelOverrides -> levelOverrides.respawnData),
        Codec.LONG.fieldOf("game_time").forGetter(levelOverrides -> levelOverrides.gameTime),
        Codec.BOOL.fieldOf("initialized").forGetter(levelOverrides -> levelOverrides.initialized),
        LEGACY_GAME_TYPE_CODEC.fieldOf("game_type").forGetter(levelOverrides -> levelOverrides.gameType),
        LevelSettings.DifficultySettings.CODEC.fieldOf("difficulty_settings").forGetter(levelOverrides -> levelOverrides.difficultySettings)
    ).apply(instance, PaperLevelOverrides::new));
    public static final SavedDataType<PaperLevelOverrides> TYPE = new SavedDataType<>(
        Identifier.fromNamespaceAndPath(Identifier.PAPER_NAMESPACE, "level_overrides"),
        PaperLevelOverrides::new,
        CODEC,
        DataFixTypes.NONE
    );

    private RespawnData respawnData;
    private long gameTime;
    private boolean initialized;
    private GameType gameType;
    private LevelSettings.DifficultySettings difficultySettings;
    private transient @Nullable PrimaryLevelData rootData;
    private transient ResourceKey<Level> dimension = Level.OVERWORLD;

    private PaperLevelOverrides() {
        this(RespawnData.DEFAULT, 0L, false, GameType.SURVIVAL, LevelSettings.DifficultySettings.DEFAULT);
    }

    private PaperLevelOverrides(
        final RespawnData respawnData,
        final long gameTime,
        final boolean initialized,
        final GameType gameType,
        final LevelSettings.DifficultySettings difficultySettings
    ) {
        this.respawnData = respawnData.normalized();
        this.gameTime = gameTime;
        this.initialized = initialized;
        this.gameType = gameType;
        this.difficultySettings = difficultySettings;
    }

    public static PaperLevelOverrides createFromLiveLevelData(final PrimaryLevelData rootData) {
         return new PaperLevelOverrides(
            RespawnData.fromVanilla(rootData.getRespawnData()),
            rootData.getGameTime(),
            false,
            rootData.getGameType(),
            new LevelSettings.DifficultySettings(rootData.getDifficulty(), rootData.isHardcore(), rootData.isDifficultyLocked())
        );
    }

    public static PaperLevelOverrides createFromRawLevelData(final @Nullable Dynamic<?> levelData) {
        if (levelData == null) {
            return new PaperLevelOverrides();
        }
        return new PaperLevelOverrides(
            RespawnData.fromVanilla(levelData.get("spawn").read(LevelData.RespawnData.CODEC).result().orElse(LevelData.RespawnData.DEFAULT)),
            levelData.get("Time").asLong(0L),
            levelData.get("initialized").asBoolean(true),
            GameType.byId(levelData.get("GameType").asInt(GameType.SURVIVAL.getId())),
            levelData.get("difficulty_settings").read(LevelSettings.DifficultySettings.CODEC).result().orElse(LevelSettings.DifficultySettings.DEFAULT)
        );
    }

    public PaperLevelOverrides attach(final PrimaryLevelData rootData, final ResourceKey<Level> dimension) {
        this.rootData = rootData;
        this.dimension = dimension;
        return this;
    }

    private void setRespawn(final RespawnData respawnData) {
        final RespawnData normalizedRespawnData = respawnData.normalized();
        if (!this.respawnData.equals(normalizedRespawnData)) {
            this.respawnData = normalizedRespawnData;
            // Do not syncRootData here, handled separately in MinecraftServer#updateEffectiveRespawnData
            this.setDirty();
        }
    }

    public void setDifficulty(final Difficulty difficulty) {
        if (this.difficultySettings.difficulty() != difficulty) {
            this.setDifficultySettings(new LevelSettings.DifficultySettings(difficulty, this.difficultySettings.hardcore(), this.difficultySettings.locked()));
        }
    }

    public void setHardcore(final boolean hardcore) {
        if (this.difficultySettings.hardcore() != hardcore) {
            this.setDifficultySettings(new LevelSettings.DifficultySettings(this.difficultySettings.difficulty(), hardcore, this.difficultySettings.locked()));
        }
    }

    public void setDifficultyLocked(final boolean difficultyLocked) {
        if (this.difficultySettings.locked() != difficultyLocked) {
            this.setDifficultySettings(new LevelSettings.DifficultySettings(this.difficultySettings.difficulty(), this.difficultySettings.hardcore(), difficultyLocked));
        }
    }

    @Override
    public LevelData.RespawnData getRespawnData() {
        return this.respawnData.toVanilla(this.dimension);
    }

    @Override
    public long getGameTime() {
        return this.gameTime;
    }

    @Override
    public GameType getGameType() {
        return this.gameType;
    }

    @Override
    public String getLevelName() {
        return this.rootDataOrThrow().getLevelName();
    }

    @Override
    public void setGameTime(final long time) {
        if (this.gameTime != time) {
            this.gameTime = time;
            this.syncRootData(rootData -> rootData.setGameTime(time));
            this.setDirty();
        }
    }

    @Override
    public void setSpawn(final LevelData.RespawnData respawnData) {
        this.setRespawn(RespawnData.fromVanilla(respawnData));
    }

    @Override
    public void setGameType(final GameType gameType) {
        if (this.gameType != gameType) {
            this.gameType = gameType;
            this.syncRootData(rootData -> rootData.setGameType(gameType));
            this.setDirty();
        }
    }

    @Override
    public boolean isHardcore() {
        return this.difficultySettings.hardcore();
    }

    @Override
    public boolean isAllowCommands() {
        return this.rootDataOrThrow().isAllowCommands();
    }

    @Override
    public boolean isInitialized() {
        return this.initialized;
    }

    @Override
    public void setInitialized(final boolean initialized) {
        if (this.initialized != initialized) {
            this.initialized = initialized;
            this.syncRootData(rootData -> rootData.setInitialized(initialized));
            this.setDirty();
        }
    }

    @Override
    public Difficulty getDifficulty() {
        return this.difficultySettings.difficulty();
    }

    @Override
    public boolean isDifficultyLocked() {
        return this.difficultySettings.locked();
    }

    private PrimaryLevelData rootDataOrThrow() {
        if (this.rootData == null) {
            throw new IllegalStateException(this.getClass().getName() + " not attached");
        }
        return this.rootData;
    }

    private void setDifficultySettings(final LevelSettings.DifficultySettings difficultySettings) {
        if (!this.difficultySettings.equals(difficultySettings)) {
            this.difficultySettings = difficultySettings;
            this.syncRootData(rootData -> rootData.settings = rootData.settings
                .withDifficulty(difficultySettings.difficulty())
                .withHardcore(difficultySettings.hardcore())
                .withDifficultyLock(difficultySettings.locked()));
            this.setDirty();
        }
    }

    private void syncRootData(final java.util.function.Consumer<PrimaryLevelData> action) {
        if (this.dimension == Level.OVERWORLD && this.rootData != null) {
            action.accept(this.rootData);
        }
    }

    // Like LevelData.RespawnData, but with a BlockPos instead of a GlobalPos
    public record RespawnData(BlockPos pos, float yaw, float pitch) {
        public static final RespawnData DEFAULT = new RespawnData(BlockPos.ZERO, 0.0F, 0.0F);
        public static final Codec<RespawnData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockPos.CODEC.fieldOf("pos").forGetter(RespawnData::pos),
            Codec.floatRange(-180.0F, 180.0F).fieldOf("yaw").forGetter(RespawnData::yaw),
            Codec.floatRange(-90.0F, 90.0F).fieldOf("pitch").forGetter(RespawnData::pitch)
        ).apply(instance, RespawnData::new));

        public RespawnData normalized() {
            return new RespawnData(this.pos.immutable(), Mth.wrapDegrees(this.yaw), Mth.clamp(this.pitch, -90.0F, 90.0F));
        }

        public LevelData.RespawnData toVanilla(final ResourceKey<Level> dimension) {
            final RespawnData normalized = this.normalized();
            return LevelData.RespawnData.of(dimension, normalized.pos, normalized.yaw, normalized.pitch);
        }

        public static RespawnData fromVanilla(final LevelData.RespawnData respawnData) {
            return new RespawnData(respawnData.pos(), respawnData.yaw(), respawnData.pitch()).normalized();
        }
    }
}
