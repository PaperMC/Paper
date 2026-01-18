package io.papermc.paper.entity;

import io.papermc.paper.annotation.GeneratedClass;
import org.jspecify.annotations.NullMarked;

@SuppressWarnings({
        "unused",
        "SpellCheckingInspection"
})
@NullMarked
@GeneratedClass
public final class CraftCopperGolemStateBridge {
    public static CopperGolemState from(
            net.minecraft.world.entity.animal.golem.CopperGolemState from) {
        return switch (from) {
            case net.minecraft.world.entity.animal.golem.CopperGolemState.IDLE -> CopperGolemState.IDLE;
            case net.minecraft.world.entity.animal.golem.CopperGolemState.GETTING_ITEM -> CopperGolemState.GETTING_ITEM;
            case net.minecraft.world.entity.animal.golem.CopperGolemState.GETTING_NO_ITEM -> CopperGolemState.GETTING_NO_ITEM;
            case net.minecraft.world.entity.animal.golem.CopperGolemState.DROPPING_ITEM -> CopperGolemState.DROPPING_ITEM;
            case net.minecraft.world.entity.animal.golem.CopperGolemState.DROPPING_NO_ITEM -> CopperGolemState.DROPPING_NO_ITEM;
        };
    }

    public static net.minecraft.world.entity.animal.golem.CopperGolemState from(
            CopperGolemState from) {
        return switch (from) {
            case CopperGolemState.IDLE -> net.minecraft.world.entity.animal.golem.CopperGolemState.IDLE;
            case CopperGolemState.GETTING_ITEM -> net.minecraft.world.entity.animal.golem.CopperGolemState.GETTING_ITEM;
            case CopperGolemState.GETTING_NO_ITEM -> net.minecraft.world.entity.animal.golem.CopperGolemState.GETTING_NO_ITEM;
            case CopperGolemState.DROPPING_ITEM -> net.minecraft.world.entity.animal.golem.CopperGolemState.DROPPING_ITEM;
            case CopperGolemState.DROPPING_NO_ITEM -> net.minecraft.world.entity.animal.golem.CopperGolemState.DROPPING_NO_ITEM;
        };
    }
}
