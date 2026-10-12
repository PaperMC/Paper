package net.minecraft.world.entity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.mojang.authlib.GameProfile;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.sensing.TemptingSensor;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerEquipment;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.bukkit.support.RegistryHelper;
import org.bukkit.support.environment.VanillaFeature;
import org.junit.jupiter.api.Test;

@VanillaFeature
class LegacyOffHandMenuTest {
    private abstract static class NativePlayer extends Player {
        NativePlayer(Level level, GameProfile profile) { super(level, profile); }
        @Override
        public boolean tryReadyArrow(ItemStack bow, ItemStack arrow, MutableBoolean cancelled) {
            return super.tryReadyArrow(bow, arrow, cancelled);
        }
    }

    private static class NativeGameMode extends ServerPlayerGameMode {
        NativeGameMode() { super(null); }
        @Override
        public void setGameModeForPlayer(GameType current, GameType previous) {
            super.setGameModeForPlayer(current, previous);
        }
    }

    private record Fixture(Player player, Inventory inventory, InventoryMenu menu) {}

    private Fixture fixture(boolean enabled) {
        Player player = mock(NativePlayer.class);
        player.offHandEnabled = enabled;
        when(player.canUseSlot(any())).thenCallRealMethod();
        when(player.getEquipmentSlotForItem(any())).thenCallRealMethod();
        Inventory inventory = new Inventory(player, new PlayerEquipment(player));
        when(player.getInventory()).thenReturn(inventory);
        return new Fixture(player, inventory, new InventoryMenu(inventory, true, player));
    }

    private ItemStack blockingSword() {
        ItemStack sword = new ItemStack(Items.DIAMOND_SWORD);
        ItemStack shield = new ItemStack(Items.SHIELD);
        sword.set(DataComponents.BLOCKS_ATTACKS, shield.get(DataComponents.BLOCKS_ATTACKS));
        sword.set(DataComponents.EQUIPPABLE, shield.get(DataComponents.EQUIPPABLE));
        return sword;
    }

    @Test
    void closedOffHandShiftClickUsesNormalInventoryAndHotbarForShieldAndCustomEquipment() {
        for (ItemStack source : new ItemStack[]{new ItemStack(Items.SHIELD), blockingSword()}) {
            Fixture f = fixture(false);
            f.inventory.setItem(9, source.copy());
            assertEquals(source.getItem(), f.menu.quickMoveStack(f.player, 9).getItem());
            assertTrue(f.inventory.getItem(40).isEmpty());
            assertTrue(f.inventory.getItem(9).isEmpty());
            assertEquals(source.getItem(), f.inventory.getItem(0).getItem());
            f.menu.quickMoveStack(f.player, 36);
            assertTrue(f.inventory.getItem(0).isEmpty());
            assertEquals(source.getItem(), f.inventory.getItem(9).getItem());
            assertTrue(f.inventory.getItem(40).isEmpty());
        }
    }

    @Test
    void enabledNativeAutoEquipmentAndDisabledResidualPickupStayAvailable() {
        Fixture f = fixture(true);
        f.inventory.setItem(9, new ItemStack(Items.SHIELD));
        f.menu.quickMoveStack(f.player, 9);
        assertTrue(f.inventory.getItem(9).isEmpty());
        assertEquals(Items.SHIELD, f.inventory.getItem(40).getItem());
        assertTrue(f.menu.getSlot(45).mayPlace(new ItemStack(Items.SHIELD)));
        f.player.offHandEnabled = false;
        assertFalse(f.menu.getSlot(45).mayPlace(new ItemStack(Items.SHIELD)));
        assertFalse(f.menu.getSlot(45).mayPlace(new ItemStack(Items.STONE)));
        assertTrue(f.menu.getSlot(45).mayPickup(f.player));
        f.menu.quickMoveStack(f.player, 45);
        assertTrue(f.inventory.getItem(40).isEmpty());
        assertEquals(Items.SHIELD, f.inventory.getItem(9).getItem());
    }

    @Test
    void fullDestinationKeepsSourceAndNeverConsumesOrCreatesItems() {
        Fixture f = fixture(false);
        for (int i = 0; i < 36; i++) f.inventory.setItem(i, new ItemStack(Items.STONE, 64));
        f.inventory.setItem(9, new ItemStack(Items.SHIELD));
        assertTrue(f.menu.quickMoveStack(f.player, 9).isEmpty());
        assertEquals(Items.SHIELD, f.inventory.getItem(9).getItem());
        assertTrue(f.inventory.getItem(40).isEmpty());
        int stones = 0;
        for (int i = 0; i < 36; i++) if (f.inventory.getItem(i).is(Items.STONE)) stones += f.inventory.getItem(i).getCount();
        assertEquals(35 * 64, stones);
    }

    @Test
    void closedOffHandDoesNotChangeArmorAutoEquipment() {
        Fixture f = fixture(false);
        when(f.player.isEquippableInSlot(any(), eq(EquipmentSlot.HEAD))).thenReturn(true);
        f.inventory.setItem(9, new ItemStack(Items.DIAMOND_HELMET));
        f.menu.quickMoveStack(f.player, 9);
        assertTrue(f.inventory.getItem(9).isEmpty());
        assertEquals(Items.DIAMOND_HELMET, f.menu.getSlot(5).getItem().getItem());
        assertTrue(f.inventory.getItem(40).isEmpty());
    }

    @Test
    void automaticMergesSkipClosedResidualOffHandAndPreserveFullInventoryItems() {
        for (boolean enabled : new boolean[]{false, true}) {
            Fixture f = fixture(enabled);
            f.inventory.setItem(40, new ItemStack(Items.STONE, 32));
            f.inventory.setItem(9, new ItemStack(Items.STONE, 16));
            assertEquals(enabled ? 40 : 9, f.inventory.getSlotWithRemainingSpace(new ItemStack(Items.STONE, 4)));
            assertTrue(f.inventory.add(new ItemStack(Items.STONE, 4)));
            assertEquals(enabled ? 36 : 32, f.inventory.getItem(40).getCount());
            assertEquals(enabled ? 16 : 20, f.inventory.getItem(9).getCount());
        }
        Fixture full = fixture(false);
        for (int i = 0; i < 36; i++) full.inventory.setItem(i, new ItemStack(Items.STONE, 64));
        full.inventory.setItem(40, new ItemStack(Items.STONE, 32));
        ItemStack incoming = new ItemStack(Items.STONE, 4);
        assertFalse(full.inventory.add(incoming));
        assertEquals(4, incoming.getCount());
        assertEquals(32, full.inventory.getItem(40).getCount());
        for (int i = 0; i < 36; i++) assertEquals(64, full.inventory.getItem(i).getCount());
    }

    @Test
    void equipmentEligibilityClosesOnlyOffHandAndPreservesNativeMainAndArmorSlots() {
        for (boolean enabled : new boolean[]{false, true}) {
            Fixture f = fixture(enabled);
            assertEquals(enabled, f.player.canUseSlot(EquipmentSlot.OFFHAND));
            assertTrue(f.player.canUseSlot(EquipmentSlot.MAINHAND));
            assertTrue(f.player.canUseSlot(EquipmentSlot.HEAD));
            assertTrue(f.player.canUseSlot(EquipmentSlot.CHEST));
            when(f.player.isAlive()).thenReturn(true);
            when(f.player.getItemBySlot(any())).thenReturn(ItemStack.EMPTY);
            doCallRealMethod().when((LivingEntity)f.player).canDispenserEquipIntoSlot(any());
            when(f.player.canEquipWithDispenser(any())).thenCallRealMethod();
            ItemStack dispensable = new ItemStack(Items.STONE);
            dispensable.set(DataComponents.EQUIPPABLE,
                Equippable.builder(EquipmentSlot.OFFHAND).build());
            assertEquals(enabled, f.player.canEquipWithDispenser(dispensable));
            assertEquals(enabled ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND,
                f.player.getEquipmentSlotForItem(new ItemStack(Items.SHIELD)));
        }
    }

    @Test
    void nativeEquippableTargetAndMainHandSwapCannotInsertIntoClosedOffHand() {
        for (boolean enabled : new boolean[]{false, true}) {
            Fixture f = fixture(enabled);
            when(f.player.isEquippableInSlot(any(), any())).thenCallRealMethod();
            when(f.player.isAlive()).thenReturn(true);
            ItemStack source = new ItemStack(Items.SHIELD);
            var equippable = source.get(DataComponents.EQUIPPABLE);
            assertNotNull(equippable);
            Player actor = mock(Player.class);
            when(actor.level()).thenReturn(mock(Level.class));
            assertEquals(enabled ? InteractionResult.SUCCESS : InteractionResult.PASS,
                equippable.equipOnTarget(actor, f.player, source));
            assertEquals(enabled ? 0 : 1, source.getCount());
            if (enabled) verify(f.player).setItemSlot(eq(EquipmentSlot.OFFHAND), any());
            else {
                verify(f.player, never()).setItemSlot(eq(EquipmentSlot.OFFHAND), any());
                assertEquals(InteractionResult.PASS,
                    equippable.swapWithEquipmentSlot(new ItemStack(Items.SHIELD), f.player));
            }
        }
    }

    @Test
    void nativeHeldAmmoSkipsLockedResidualButRetainsMainHandAndEnabledSelection() {
        for (boolean enabled : new boolean[]{false, true}) {
            Fixture f = fixture(enabled);
            ItemStack offhand = new ItemStack(Items.ARROW, 5);
            ItemStack main = new ItemStack(Items.ARROW, 3);
            when(f.player.getItemInHand(InteractionHand.OFF_HAND)).thenReturn(offhand);
            when(f.player.getItemInHand(InteractionHand.MAIN_HAND)).thenReturn(main);
            var visits = new ArrayList<ItemStack>();
            Predicate<ItemStack> predicate = stack -> { visits.add(stack); return stack.is(Items.ARROW); };
            assertSame(enabled ? offhand : main,
                ProjectileWeaponItem.getHeldProjectile(f.player, predicate));
            assertEquals(List.of(enabled ? offhand : main), visits);
            when(f.player.getItemInHand(InteractionHand.MAIN_HAND)).thenReturn(new ItemStack(Items.BOW));
            assertSame(enabled ? offhand : ItemStack.EMPTY,
                ProjectileWeaponItem.getHeldProjectile(f.player, stack -> stack.is(Items.ARROW)));
            assertEquals(5, offhand.getCount());
            assertEquals(3, main.getCount());
        }
    }

    @Test
    void nativePlayerAmmoFallbackSkipsInventoryFortyButPreservesStorageAndMainSelection() throws Exception {
        for (boolean enabled : new boolean[]{false, true}) {
            Fixture f = fixture(enabled);
            Field inventory = Player.class.getDeclaredField("inventory");
            inventory.setAccessible(true);
            inventory.set(f.player, f.inventory);
            when(f.player.getItemInHand(any())).thenAnswer(call ->
                call.getArgument(0) == InteractionHand.OFF_HAND ? ItemStack.EMPTY : f.inventory.getItem(0));
            when(((NativePlayer)f.player).tryReadyArrow(any(), any(), any())).thenCallRealMethod();
            when(f.player.getProjectile(any())).thenCallRealMethod();
            ItemStack locked = new ItemStack(Items.ARROW, 5);
            f.inventory.setItem(40, locked);
            ItemStack bow = new ItemStack(Items.BOW);
            assertSame(enabled ? locked : ItemStack.EMPTY, f.player.getProjectile(bow));
            ItemStack storage = new ItemStack(Items.ARROW, 3);
            f.inventory.setItem(9, storage);
            assertSame(storage, f.player.getProjectile(bow));
            f.inventory.setItem(40, ItemStack.EMPTY);
            f.inventory.setItem(0, storage);
            assertSame(storage, f.player.getProjectile(bow));
            assertEquals(5, locked.getCount());
            assertEquals(3, storage.getCount());
        }
    }

    @Test
    void nativeHeldIntentAndBothTemptationSelectorsIgnoreClosedResidualItems() throws Exception {
        var axe = AxeItem.class.getDeclaredMethod("playerHasBlockingItemUseIntent", UseOnContext.class);
        axe.setAccessible(true);
        var follow = TemptGoal.class.getDeclaredMethod("shouldFollow", LivingEntity.class);
        follow.setAccessible(true);
        var tempting = TemptingSensor.class.getDeclaredMethod("playerHoldingTemptation", PathfinderMob.class, Player.class);
        tempting.setAccessible(true);
        for (boolean enabled : new boolean[]{false, true}) {
            Fixture f = fixture(enabled);
            when(f.player.getMainHandItem()).thenReturn(ItemStack.EMPTY);
            when(f.player.getOffhandItem()).thenReturn(new ItemStack(Items.SHIELD));
            var context = mock(UseOnContext.class);
            when(context.getPlayer()).thenReturn(f.player);
            when(context.getHand()).thenReturn(InteractionHand.MAIN_HAND);
            Object axeIntent = axe.invoke(null, context);
            ItemStack food = new ItemStack(Items.CARROT);
            when(f.player.getOffhandItem()).thenReturn(food);
            Predicate<ItemStack> predicate = stack -> stack.is(Items.CARROT);
            doCallRealMethod().when(f.player).isHolding(any(Predicate.class));
            var mob = mock(PathfinderMob.class);
            var goal = new TemptGoal(mob, 1.0, predicate, false);
            var sensor = new TemptingSensor(predicate);
            assertAll(
                () -> assertEquals(enabled, axeIntent, "axe intent"),
                () -> assertEquals(enabled, f.player.isHolding(predicate), "isHolding"),
                () -> assertEquals(enabled, follow.invoke(goal, f.player), "tempt goal"),
                () -> assertEquals(enabled, tempting.invoke(sensor, mob, f.player), "tempt sensor"));
            when(f.player.getMainHandItem()).thenReturn(food);
            assertTrue(f.player.isHolding(predicate));
            assertEquals(true, follow.invoke(goal, f.player));
            assertEquals(true, tempting.invoke(sensor, mob, f.player));
        }
    }

    @Test
    void nativeMendingCandidateFilterPreservesEligibleMainAndArmorRepairs() {
        var mending = RegistryHelper.registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
            .getOrThrow(Enchantments.MENDING);
        for (EquipmentSlot primarySlot : new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.HEAD}) {
            for (boolean enabled : new boolean[]{false, true}) {
                Fixture f = fixture(enabled);
                ItemStack primary = new ItemStack(primarySlot == EquipmentSlot.MAINHAND ? Items.DIAMOND_AXE : Items.DIAMOND_HELMET);
                ItemStack offhand = new ItemStack(Items.SHIELD);
                for (ItemStack stack : new ItemStack[]{primary, offhand}) { stack.enchant(mending, 1); stack.setDamageValue(50); }
                when(f.player.getItemBySlot(any())).thenAnswer(call -> call.getArgument(0) == EquipmentSlot.OFFHAND ? offhand
                    : call.getArgument(0) == primarySlot ? primary : ItemStack.EMPTY);
                var random = mock(RandomSource.class);
                when(f.player.getRandom()).thenReturn(random);
                when(random.nextInt(anyInt())).thenAnswer(call -> primarySlot == EquipmentSlot.MAINHAND ? (int)call.getArgument(0) - 1 : 0);
                var selected = EnchantmentHelper.getRandomItemWith(
                    EnchantmentEffectComponents.REPAIR_WITH_XP, f.player, ItemStack::isDamaged);
                assertSame(enabled ? offhand : primary, selected.orElseThrow().itemStack());
                when(f.player.getItemBySlot(primarySlot)).thenReturn(ItemStack.EMPTY);
                assertEquals(enabled, EnchantmentHelper.getRandomItemWith(
                    EnchantmentEffectComponents.REPAIR_WITH_XP, f.player, ItemStack::isDamaged).isPresent());
                assertEquals(50, offhand.getDamageValue());
            }
        }
    }

    @Test
    void nativeFireworkAttachmentAngleIgnoresClosedResidualHandWithoutChangingMainGeometry() {
        for (boolean enabled : new boolean[]{false, true}) {
            Fixture f = fixture(enabled);
            when(f.player.getMainArm()).thenReturn(HumanoidArm.RIGHT);
            when(f.player.getMainHandItem()).thenReturn(new ItemStack(Items.DIAMOND_SWORD));
            when(f.player.getOffhandItem()).thenReturn(new ItemStack(Items.FIREWORK_ROCKET));
            when(f.player.calculateViewVector(anyFloat(), anyFloat())).thenCallRealMethod();
            when(f.player.getHandHoldingItemAngle(any())).thenCallRealMethod();
            Vec3 angle = f.player.getHandHoldingItemAngle(Items.FIREWORK_ROCKET);
            assertTrue(enabled ? angle.x > 0 : angle.x < 0);
            assertEquals(0.0, angle.y, 0.0);
            assertTrue(angle.z > 0);
            assertEquals(0.5, angle.length(), 1.0E-4);
            when(f.player.getMainHandItem()).thenReturn(new ItemStack(Items.FIREWORK_ROCKET));
            assertTrue(f.player.getHandHoldingItemAngle(Items.FIREWORK_ROCKET).x < 0);
        }
        LivingEntity npc = mock(Mob.class);
        when(npc.getHandHoldingItemAngle(any())).thenCallRealMethod();
        assertEquals(Vec3.ZERO, npc.getHandHoldingItemAngle(Items.FIREWORK_ROCKET));
    }

    @Test
    void nativeRespawnCopiesBothEnabledAndDisabledCapabilitiesBeforeOtherRestoration() throws Exception {
        for (boolean enabled : new boolean[]{false, true}) {
            ServerPlayer old = mock(ServerPlayer.class);
            old.offHandEnabled = enabled;
            ServerPlayer next = mock(ServerPlayer.class);
            next.offHandEnabled = !enabled;
            var oldMode = mock(ServerPlayerGameMode.class);
            var nextMode = mock(NativeGameMode.class);
            Field mode = ServerPlayer.class.getDeclaredField("gameMode");
            mode.setAccessible(true);
            mode.set(old, oldMode);
            mode.set(next, nextMode);
            class StopAfterCapabilityCopy extends RuntimeException {}
            doThrow(new StopAfterCapabilityCopy()).when(nextMode).setGameModeForPlayer(any(), any());
            doCallRealMethod().when(next).restoreFrom(old, false);
            assertThrows(StopAfterCapabilityCopy.class, () -> next.restoreFrom(old, false));
            assertEquals(enabled, next.offHandEnabled);
        }
    }
}
