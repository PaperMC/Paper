package io.papermc.paper.util;

import com.google.common.base.Preconditions;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.MethodInfo;
import io.github.classgraph.MethodInfoList;
import io.github.classgraph.ScanResult;
import io.papermc.paper.event.entity.EntityKnockbackEvent;
import java.lang.constant.ClassDesc;
import java.lang.constant.ConstantDescs;
import java.lang.constant.MethodTypeDesc;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.DispensibleContainerItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.entity.EntityAccess;
import org.bukkit.event.entity.EntityRemoveEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.support.environment.Normal;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.objectweb.asm.Type;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Normal
class SourceOverloadsTest {

    private static final String SUPPORTED_PACKAGE = "net.minecraft";

    record MethodOverload(Class<?> owner, String name, @Nullable MethodTypeDesc basedOn, int paramCount, ClassDesc... addedParams) {
        MethodOverload {
            Preconditions.checkState(owner.getPackageName().startsWith(SUPPORTED_PACKAGE));
        }

        // when there's no ambiguity and only one overload is expected you can use this
        MethodOverload(Class<?> owner, String name, int paramCount, ClassDesc... addedParams) {
            this(owner, name, null, paramCount, addedParams);
        }
    }

    private static final MethodOverload[] OVERLOADS = new MethodOverload[] {
        new MethodOverload(
            EntityAccess.class,
            "setRemoved",
            2,
            EntityRemoveEvent.Cause.class.describeConstable().orElseThrow()
        ),
        new MethodOverload(
            Entity.class,
            "remove",
            2,
            EntityRemoveEvent.Cause.class.describeConstable().orElseThrow()
        ),
        new MethodOverload(
            Entity.class,
            "discard",
            1,
            EntityRemoveEvent.Cause.class.describeConstable().orElseThrow()
        ),
        new MethodOverload(
            Entity.class,
            "removeVehicle",
            1,
            ConstantDescs.CD_boolean
        ),
        new MethodOverload(
            Entity.class,
            "removePassenger",
            2,
            ConstantDescs.CD_boolean
        ),
        new MethodOverload(
            Entity.class,
            "stopRiding",
            1,
            ConstantDescs.CD_boolean
        ),
        new MethodOverload(
            Entity.class,
            "teleportTo",
            MethodTypeDesc.ofDescriptor("(Lnet/minecraft/server/level/ServerLevel;DDDLjava/util/Set;FFZ)Z"),
            9,
            PlayerTeleportEvent.TeleportCause.class.describeConstable().orElseThrow()
        ),
        new MethodOverload(
            Entity.class,
            "push",
            MethodTypeDesc.ofDescriptor("(DDD)V"),
            4,
            Entity.class.describeConstable().orElseThrow(),
            EntityKnockbackEvent.Cause.class.describeConstable().orElseThrow()
        ),
        new MethodOverload(
            LivingEntity.class,
            "onEquipItem",
            4,
            ConstantDescs.CD_boolean
        ),
        new MethodOverload(
            LivingEntity.class,
            "setItemSlot",
            3,
            ConstantDescs.CD_boolean
        ),
        new MethodOverload(
            LivingEntity.class,
            "drop",
            MethodTypeDesc.ofDescriptor("(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;"),
            5,
            ConstantDescs.CD_boolean,
            Consumer.class.describeConstable().orElseThrow()
        ),
        new MethodOverload(
            LivingEntity.class,
            "knockback",
            5,
            EntityKnockbackEvent.Cause.class.describeConstable().orElseThrow()
        ),
        new MethodOverload(
            LivingEntity.class,
            "onAttack",
            1,
            Entity.class.describeConstable().orElseThrow()
        ),
        new MethodOverload(
            Mob.class,
            "setTarget",
            2,
            EntityTargetEvent.TargetReason.class.describeConstable().orElseThrow()
        ),
        new MethodOverload(
            Block.class,
            "playerDestroy",
            8,
            ConstantDescs.CD_boolean,
            ConstantDescs.CD_boolean
        ),
        new MethodOverload(
            DispensibleContainerItem.class,
            "checkExtraContent",
            5,
            Function.class.describeConstable().orElseThrow()
        ),
        // TODO check Entity#isCollidable, LivingEntity#addEffect
    };

    public static Stream<Arguments> methods() {
        final List<Arguments> args = new ArrayList<>();
        try (final ScanResult scanResult = new ClassGraph()
            .enableClassInfo()
            .enableMethodInfo()
            .acceptPackages(SUPPORTED_PACKAGE)
            .scan()
        ) {
            for (MethodOverload overload : OVERLOADS) {
                final ClassInfoList classes;
                if (overload.owner().isInterface()) {
                    classes = scanResult.getClassesImplementing(overload.owner().getName());
                } else {
                    classes = scanResult.getSubclasses(overload.owner().getName());
                }
                for (final ClassInfo klass : classes) {
                    if (klass.hasDeclaredMethod(overload.name())) {
                        MethodInfoList methods = klass.getDeclaredMethodInfo(overload.name());
                        if (overload.basedOn() != null) {
                            methods = matches(overload, methods);
                            if (methods.isEmpty()) {
                                continue;
                            }
                        }

                        args.add(Arguments.of(klass, methods, overload));
                    }
                }
            }
        }
        return args.stream();
    }

    private static MethodInfoList matches(final MethodOverload expected, final MethodInfoList methods) {
        final MethodTypeDesc fromDesc = expected.basedOn();
        assert fromDesc != null;
        final MethodTypeDesc toDesc = fromDesc.insertParameterTypes(expected.paramCount() - expected.addedParams().length, expected.addedParams());

        final MethodInfoList result = new MethodInfoList();
        for (final MethodInfo method : methods) {
            final String desc = method.getTypeDescriptorStr();
            if (desc.equals(fromDesc.descriptorString()) || desc.equals(toDesc.descriptorString())) {
                result.add(method);
            }
        }

        return result;
    }

    @ParameterizedTest
    @MethodSource("methods")
    public void checkOverloads(final ClassInfo owner, final MethodInfoList methodList, final MethodOverload expectedOverload) {
        assertEquals(1, methodList.size(), owner.getName() + " has multiple " + expectedOverload.name() + " methods");
        final MethodInfo method = methodList.getFirst();

        assertEquals(expectedOverload.paramCount(), method.getParameterInfo().length, owner.getName() + " doesn't have " + expectedOverload.paramCount() + " params for " + expectedOverload.name());

        final Type[] currentParams = Type.getArgumentTypes(method.getTypeDescriptorStr());
        final ClassDesc[] addedParams = expectedOverload.addedParams();
        for (int i = 0, len = addedParams.length; i < len; i++) {
            assertEquals(addedParams[i].descriptorString(), currentParams[expectedOverload.paramCount() - len + i].getDescriptor(), owner.getName() + " needs to change its override of " + expectedOverload.name());
        }
    }
}
