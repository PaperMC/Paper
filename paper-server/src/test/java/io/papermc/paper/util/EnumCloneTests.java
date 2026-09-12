package io.papermc.paper.util;

import java.util.Set;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import org.bukkit.Particle;
import org.bukkit.entity.Pose;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.params.provider.Arguments.arguments;

@Normal
public class EnumCloneTests {

    public enum ValidationType {
        SAME_NAME, // ensure twos enums have the same names useful compromise when an EnumCloneRewriter cannot be used
        SAME_ORDER // can't really enforce but still check the length
    }

    public static Set<Arguments> params() {
        return Set.of(
            arguments(Particle.RandomizationType.class, ClientboundLevelParticlesPacket.RandomizationType.class, ValidationType.SAME_NAME),
            arguments(Pose.class, net.minecraft.world.entity.Pose.class, ValidationType.SAME_ORDER)
        );
    }

    @ParameterizedTest
    @MethodSource("params")
    public <A extends Enum<A>, M extends Enum<M>> void testEnum(final Class<A> apiEnum, final Class<M> vanillaEnum, final ValidationType validationType) {
        final A[] apiConstants = apiEnum.getEnumConstants();
        final M[] vanillaConstants = vanillaEnum.getEnumConstants();

        Assertions.assertSame(vanillaConstants.length, apiConstants.length, "Size of both enum doesn't match");
        if (validationType == ValidationType.SAME_NAME) {
            for (final M vanilla : vanillaConstants) {
                Assertions.assertDoesNotThrow(() -> Enum.valueOf(apiEnum, vanilla.name()), "Enum constant not found for " + vanilla.name());
            }
        }
    }
}
