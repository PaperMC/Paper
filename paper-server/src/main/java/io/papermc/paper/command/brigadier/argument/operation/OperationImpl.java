package io.papermc.paper.command.brigadier.argument.operation;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.arguments.OperationArgument;
import net.minecraft.world.scores.ScoreAccess;
import net.minecraft.world.scores.ScoreHolder;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.scoreboard.CraftObjective;
import org.bukkit.craftbukkit.scoreboard.CraftScoreboard;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class OperationImpl implements Operation {

    private final OperationArgument.Operation operation;
    private final OperationArgument.@Nullable SimpleOperation simpleOperation;

    public OperationImpl(OperationArgument.SimpleOperation operation) {
        this.operation = operation;
        this.simpleOperation = operation;
    }

    public OperationImpl(OperationArgument.Operation operation) {
        this.operation = operation;
        this.simpleOperation = null;
    }

    @Override
    public int apply(int left, int right) throws CommandSyntaxException {
        if (this.simpleOperation == null) {
            throw OperationArgument.ERROR_INVALID_OPERATION.create();
        }

        return this.simpleOperation.apply(left, right);
    }

    @Override
    public void apply(Scoreboard scoreboard, Objective objective, String target, OfflinePlayer sourcePlayer) throws CommandSyntaxException {
        apply(scoreboard, objective, CraftScoreboard.getScoreHolder(target), CraftScoreboard.getScoreHolder(sourcePlayer));
    }

    @Override
    public void apply(Scoreboard scoreboard, Objective objective, OfflinePlayer targetPlayer, String source) throws CommandSyntaxException {
        apply(scoreboard, objective, CraftScoreboard.getScoreHolder(targetPlayer), CraftScoreboard.getScoreHolder(source));
    }

    @Override
    public void apply(Scoreboard scoreboard, Objective objective, String target, String source) throws CommandSyntaxException {
        apply(scoreboard, objective, CraftScoreboard.getScoreHolder(target), CraftScoreboard.getScoreHolder(source));
    }

    @Override
    public void apply(Scoreboard scoreboard, Objective objective, OfflinePlayer targetPlayer, OfflinePlayer sourcePlayer) throws CommandSyntaxException {
        apply(scoreboard, objective, CraftScoreboard.getScoreHolder(targetPlayer), CraftScoreboard.getScoreHolder(sourcePlayer));
    }

    private void apply(Scoreboard scoreboard, Objective objective, ScoreHolder targetScoreHolder, ScoreHolder sourceScoreHolder) throws CommandSyntaxException {
        net.minecraft.world.scores.Scoreboard nmsScoreboard = ((CraftScoreboard) scoreboard).getHandle();
        net.minecraft.world.scores.Objective nmsObjective = ((CraftObjective) objective).getHandle();

        ScoreAccess targetScoreAccess = nmsScoreboard.getOrCreatePlayerScore(targetScoreHolder, nmsObjective);
        ScoreAccess sourceScoreAccess = nmsScoreboard.getOrCreatePlayerScore(sourceScoreHolder, nmsObjective);

        this.operation.apply(targetScoreAccess, sourceScoreAccess);
    }

    public static Operation fromVanilla(OperationArgument.Operation vanillaOperation) {
        if (vanillaOperation instanceof OperationArgument.SimpleOperation simple) {
            return new OperationImpl(simple);
        }
        return new OperationImpl(vanillaOperation);
    }
}
