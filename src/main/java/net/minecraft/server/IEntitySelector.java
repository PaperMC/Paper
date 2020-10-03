package net.minecraft.server;

import com.google.common.base.Predicates;
import java.util.function.Predicate;
import javax.annotation.Nullable;

public final class IEntitySelector {

    public static final Predicate<Entity> a = Entity::isAlive;
    public static final Predicate<EntityLiving> b = EntityLiving::isAlive;
    public static final Predicate<Entity> c = (entity) -> {
        return entity.isAlive() && !entity.isVehicle() && !entity.isPassenger();
    };
    public static final Predicate<Entity> d = (entity) -> {
        return entity instanceof IInventory && entity.isAlive();
    };
    public static final Predicate<Entity> e = (entity) -> {
        return !(entity instanceof EntityHuman) || !entity.isSpectator() && !((EntityHuman) entity).isCreative();
    };
    public static final Predicate<Entity> f = (entity) -> {
        return !(entity instanceof EntityHuman) || !entity.isSpectator() && !((EntityHuman) entity).isCreative() && entity.world.getDifficulty() != EnumDifficulty.PEACEFUL;
    };
    public static final Predicate<Entity> g = (entity) -> {
        return !entity.isSpectator();
    };

    public static Predicate<Entity> a(double d0, double d1, double d2, double d3) {
        double d4 = d3 * d3;

        return (entity) -> {
            return entity != null && entity.h(d0, d1, d2) <= d4;
        };
    }

    public static Predicate<Entity> a(Entity entity) {
        ScoreboardTeamBase scoreboardteambase = entity.getScoreboardTeam();
        ScoreboardTeamBase.EnumTeamPush scoreboardteambase_enumteampush = scoreboardteambase == null ? ScoreboardTeamBase.EnumTeamPush.ALWAYS : scoreboardteambase.getCollisionRule();

        return (Predicate) (scoreboardteambase_enumteampush == ScoreboardTeamBase.EnumTeamPush.NEVER ? Predicates.alwaysFalse() : IEntitySelector.g.and((entity1) -> {
            if (!entity1.canCollideWith(entity) || !entity.canCollideWith(entity1)) { // CraftBukkit - collidable API
                return false;
            } else if (entity.world.isClientSide && (!(entity1 instanceof EntityHuman) || !((EntityHuman) entity1).ey())) {
                return false;
            } else {
                ScoreboardTeamBase scoreboardteambase1 = entity1.getScoreboardTeam();
                ScoreboardTeamBase.EnumTeamPush scoreboardteambase_enumteampush1 = scoreboardteambase1 == null ? ScoreboardTeamBase.EnumTeamPush.ALWAYS : scoreboardteambase1.getCollisionRule();

                if (scoreboardteambase_enumteampush1 == ScoreboardTeamBase.EnumTeamPush.NEVER) {
                    return false;
                } else {
                    boolean flag = scoreboardteambase != null && scoreboardteambase.isAlly(scoreboardteambase1);

                    return (scoreboardteambase_enumteampush == ScoreboardTeamBase.EnumTeamPush.PUSH_OWN_TEAM || scoreboardteambase_enumteampush1 == ScoreboardTeamBase.EnumTeamPush.PUSH_OWN_TEAM) && flag ? false : scoreboardteambase_enumteampush != ScoreboardTeamBase.EnumTeamPush.PUSH_OTHER_TEAMS && scoreboardteambase_enumteampush1 != ScoreboardTeamBase.EnumTeamPush.PUSH_OTHER_TEAMS || flag;
                }
            }
        }));
    }

    public static Predicate<Entity> b(Entity entity) {
        return (entity1) -> {
            while (true) {
                if (entity1.isPassenger()) {
                    entity1 = entity1.getVehicle();
                    if (entity1 != entity) {
                        continue;
                    }

                    return false;
                }

                return true;
            }
        };
    }

    public static class EntitySelectorEquipable implements Predicate<Entity> {

        private final ItemStack a;

        public EntitySelectorEquipable(ItemStack itemstack) {
            this.a = itemstack;
        }

        public boolean test(@Nullable Entity entity) {
            if (!entity.isAlive()) {
                return false;
            } else if (!(entity instanceof EntityLiving)) {
                return false;
            } else {
                EntityLiving entityliving = (EntityLiving) entity;

                return entityliving.e(this.a);
            }
        }
    }
}
