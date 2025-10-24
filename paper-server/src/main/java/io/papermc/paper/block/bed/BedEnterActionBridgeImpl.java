package io.papermc.paper.block.bed;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.event.player.PlayerBedFailEnterEvent;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.world.attribute.BedRule;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.player.Player;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class BedEnterActionBridgeImpl implements BedEnterActionBridge {

    @Override
    public BedEnterProblem createTooFarAwayProblem() {
        return new BedEnterProblemImpl(Player.BedSleepingProblem.TOO_FAR_AWAY);
    }

    @Override
    public BedEnterProblem createObstructedProblem() {
        return new BedEnterProblemImpl(Player.BedSleepingProblem.OBSTRUCTED);
    }

    @Override
    public BedEnterProblem createNotSafeProblem() {
        return new BedEnterProblemImpl(Player.BedSleepingProblem.NOT_SAFE);
    }

    @Override
    public BedEnterProblem createExplosionProblem() {
        return new BedEnterProblemImpl(Player.BedSleepingProblem.EXPLOSION);
    }

    @Override
    public BedEnterProblem createOtherProblem() {
        return new BedEnterProblemImpl(Player.BedSleepingProblem.OTHER_PROBLEM);
    }

    @Override
    public BedEnterAction fromBedEnterResult(final org.bukkit.entity.Player player, final PlayerBedEnterEvent.BedEnterResult bedEnterResult) {
        Player vanillaPlayer = ((CraftPlayer) player).getHandle();
        BedRule bedRule = vanillaPlayer.level().environmentAttributes().getDimensionValue(EnvironmentAttributes.BED_RULE);
        BedRuleResult canSetSpawn = CraftEventFactory.asBedRuleResult(bedRule.canSetSpawn(), bedRule.canSetSpawn(vanillaPlayer.level()));

        if (bedEnterResult == PlayerBedEnterEvent.BedEnterResult.OK) {
            return new BedEnterActionImpl(
                BedRuleResult.ALLOWED,
                canSetSpawn,
                null,
                null
            );
        }

        if (bedEnterResult == PlayerBedEnterEvent.BedEnterResult.NOT_POSSIBLE_HERE || bedEnterResult == PlayerBedEnterEvent.BedEnterResult.NOT_POSSIBLE_NOW) {
            return new BedEnterActionImpl(
                bedEnterResult == PlayerBedEnterEvent.BedEnterResult.NOT_POSSIBLE_HERE ? BedRuleResult.NEVER : BedRuleResult.TOO_MUCH_LIGHT,
                canSetSpawn,
                null,
                bedRule.errorMessage().map(PaperAdventure::asAdventure).orElse(null)
            );
        }

        BedRuleResult canSleep = CraftEventFactory.asBedRuleResult(bedRule.canSleep(), bedRule.canSleep(vanillaPlayer.level()));
        BedEnterProblem enterProblem = switch (bedEnterResult) {
            case TOO_FAR_AWAY -> BedEnterProblem.TOO_FAR_AWAY;
            case OBSTRUCTED -> BedEnterProblem.OBSTRUCTED;
            case NOT_SAFE -> BedEnterProblem.NOT_SAFE;
            case OTHER_PROBLEM -> BedEnterProblem.OTHER;
            case EXPLOSION -> BedEnterProblem.EXPLOSION;
            default -> throw new IllegalStateException("Unexpected value: " + bedEnterResult);
        };
        Component errorMessage = ((BedEnterProblemImpl) enterProblem).vanillaProblem().message();
        if (bedEnterResult == PlayerBedEnterEvent.BedEnterResult.EXPLOSION) {
            errorMessage = bedRule.errorMessage().orElse(null);
        }

        return new BedEnterActionImpl(
            canSleep,
            canSetSpawn,
            enterProblem,
            errorMessage == null ? null : PaperAdventure.asAdventure(errorMessage)
        );
    }
}
