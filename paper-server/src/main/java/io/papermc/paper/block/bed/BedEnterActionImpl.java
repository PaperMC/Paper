package io.papermc.paper.block.bed;

import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.minecraft.Optionull;
import net.minecraft.world.attribute.BedRule;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

public record BedEnterActionImpl(BedRuleResult canSleep, BedRuleResult canSetSpawn, @Nullable BedEnterProblem problem, @Nullable Component errorMessage)
    implements BedEnterAction {

    public BedEnterActionImpl(final BedRule bedRule, final Level level) {
        this(asBedRuleResult(bedRule.canSleep(), level), asBedRuleResult(bedRule.canSetSpawn(), level), null, null);
    }

    public BedEnterActionImpl(final BedRule bedRule, final Level level, final Player.BedSleepingProblem sleepingProblem) {
        BedEnterProblem enterProblem = null;
        if (sleepingProblem == Player.BedSleepingProblem.OTHER_PROBLEM) {
            enterProblem = BedEnterProblem.OTHER;
        } else if (sleepingProblem == Player.BedSleepingProblem.NOT_SAFE) {
            enterProblem = BedEnterProblem.NOT_SAFE;
        } else if (sleepingProblem == Player.BedSleepingProblem.OBSTRUCTED) {
            enterProblem = BedEnterProblem.OBSTRUCTED;
        } else if (sleepingProblem == Player.BedSleepingProblem.TOO_FAR_AWAY) {
            enterProblem = BedEnterProblem.TOO_FAR_AWAY;
        } else if (sleepingProblem == Player.BedSleepingProblem.EXPLOSION) {
            enterProblem = BedEnterProblem.EXPLOSION;
        }

        if (bedRule.canSleep() != BedRule.Rule.NEVER && bedRule.canSleep() != BedRule.Rule.WHEN_DARK) {
            enterProblem = BedEnterProblem.OTHER;
        }

        net.minecraft.network.chat.@Nullable Component errorMessage = sleepingProblem.message();
        if (sleepingProblem == Player.BedSleepingProblem.EXPLOSION && errorMessage == null) { // custom problem
            errorMessage = bedRule.errorMessage().orElse(null);
        }

        this(
            asBedRuleResult(bedRule.canSleep(), level),
            asBedRuleResult(bedRule.canSetSpawn(), level),
            enterProblem,
            Optionull.map(errorMessage, PaperAdventure::asAdventure)
        );
    }

    public static BedEnterAction from(final BedRule bedRule, final Level level, final Player.@Nullable BedSleepingProblem sleepingProblem) {
        if (sleepingProblem == null) {
            return new BedEnterActionImpl(bedRule, level);
        } else {
            return new BedEnterActionImpl(bedRule, level, sleepingProblem);
        }
    }

    private static BedRuleResult asBedRuleResult(final BedRule.Rule rule, final Level level) {
        if (rule == BedRule.Rule.ALWAYS) {
            return BedRuleResult.ALLOWED;
        }
        if (rule == BedRule.Rule.WHEN_DARK) {
            if (rule.test(level)) {
                return BedRuleResult.ALLOWED;
            } else {
                return BedRuleResult.TOO_MUCH_LIGHT;
            }
        }
        if (rule == BedRule.Rule.NEVER) {
            return BedRuleResult.NEVER;
        }
        throw new IllegalStateException(rule.toString());
    }
}
