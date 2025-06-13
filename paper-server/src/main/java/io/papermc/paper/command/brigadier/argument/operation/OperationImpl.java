package io.papermc.paper.command.brigadier.argument.operation;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.arguments.OperationArgument;
import net.minecraft.world.scores.ScoreAccess;
import net.minecraft.world.scores.ScoreHolder;
import org.bukkit.craftbukkit.scoreboard.CraftObjective;
import org.bukkit.craftbukkit.scoreboard.CraftScoreHolder;
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
    public void apply(Scoreboard scoreboard, Objective targetObjective, Objective sourceObjective, org.bukkit.scoreboard.ScoreHolder targetHolder, org.bukkit.scoreboard.ScoreHolder sourceHolder) throws CommandSyntaxException {
        apply(scoreboard, targetObjective, sourceObjective, ((CraftScoreHolder) targetHolder).asNmsScoreHolder(), ((CraftScoreHolder) sourceHolder).asNmsScoreHolder());
    }

    private void apply(Scoreboard scoreboard, Objective targetObjective, Objective sourceObjective, ScoreHolder targetScoreHolder, ScoreHolder sourceScoreHolder) throws CommandSyntaxException {
        net.minecraft.world.scores.Scoreboard nmsScoreboard = ((CraftScoreboard) scoreboard).getHandle();

        net.minecraft.world.scores.Objective targetNmsObjective = ((CraftObjective) targetObjective).getHandle();
        net.minecraft.world.scores.Objective sourceNmsObjective = ((CraftObjective) sourceObjective).getHandle();

        ScoreAccess targetScoreAccess = nmsScoreboard.getOrCreatePlayerScore(targetScoreHolder, targetNmsObjective);
        ScoreAccess sourceScoreAccess = nmsScoreboard.getOrCreatePlayerScore(sourceScoreHolder, sourceNmsObjective);

        this.operation.apply(targetScoreAccess, sourceScoreAccess);
    }

    public static Operation fromVanilla(OperationArgument.Operation vanillaOperation) {
        if (vanillaOperation instanceof OperationArgument.SimpleOperation simple) {
            return new OperationImpl(simple);
        }
        return new OperationImpl(vanillaOperation);
    }
}
