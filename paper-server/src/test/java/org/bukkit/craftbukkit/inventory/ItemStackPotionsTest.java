package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Joiner;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.craftbukkit.inventory.ItemStackTest.CompoundOperator;
import org.bukkit.craftbukkit.inventory.ItemStackTest.Operator;
import org.bukkit.craftbukkit.inventory.ItemStackTest.StackProvider;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ItemStackPotionsTest extends ItemStackTest {

    @Parameters(name = "[{index}]:{" + NAME_PARAMETER + "}")
    public static List<Object[]> data() {
        return StackProvider.compound(operators(), "%s %s", NAME_PARAMETER, Material.POTION);
    }

    @SuppressWarnings("unchecked")
    static List<Object[]> operators() {
        return CompoundOperator.compound(
            Joiner.on('+'),
            NAME_PARAMETER,
            Long.parseLong("10", 2),
            ItemStackLoreEnchantmentTest.operators(),
            Arrays.asList(
                new Object[] {
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            final PotionMeta meta = (PotionMeta) cleanStack.getItemMeta();
                            meta.addCustomEffect(PotionEffectType.CONFUSION.createEffect(1, 1), false);
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            return cleanStack;
                        }
                    },
                    "Potion vs Null"
                },
                new Object[] {
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            final PotionMeta meta = (PotionMeta) cleanStack.getItemMeta();
                            meta.addCustomEffect(PotionEffectType.HARM.createEffect(2, 1), false);
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            final PotionMeta meta = (PotionMeta) cleanStack.getItemMeta();
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    "Potion vs Blank"
                },
                new Object[] {
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            final PotionMeta meta = (PotionMeta) cleanStack.getItemMeta();
                            meta.addCustomEffect(PotionEffectType.SLOW_DIGGING.createEffect(1, 1), false);
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            final PotionMeta meta = (PotionMeta) cleanStack.getItemMeta();
                            meta.addCustomEffect(PotionEffectType.FAST_DIGGING.createEffect(1, 1), false);
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    "Potion vs Harder"
                },
                new Object[] {
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            final PotionMeta meta = (PotionMeta) cleanStack.getItemMeta();
                            meta.addCustomEffect(PotionEffectType.JUMP.createEffect(1, 1), false);
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            final PotionMeta meta = (PotionMeta) cleanStack.getItemMeta();
                            meta.addCustomEffect(PotionEffectType.JUMP.createEffect(1, 1), false);
                            meta.addCustomEffect(PotionEffectType.REGENERATION.createEffect(1, 1), false);
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    "Potion vs Better"
                },
                new Object[] {
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            final PotionMeta meta = (PotionMeta) cleanStack.getItemMeta();
                            meta.addCustomEffect(PotionEffectType.SPEED.createEffect(10, 1), false);
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            final PotionMeta meta = (PotionMeta) cleanStack.getItemMeta();
                            meta.addCustomEffect(PotionEffectType.SPEED.createEffect(5, 1), false);
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    "Potion vs Faster"
                },
                new Object[] {
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            final PotionMeta meta = (PotionMeta) cleanStack.getItemMeta();
                            meta.addCustomEffect(PotionEffectType.INCREASE_DAMAGE.createEffect(1, 1), false);
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            final PotionMeta meta = (PotionMeta) cleanStack.getItemMeta();
                            meta.addCustomEffect(PotionEffectType.INCREASE_DAMAGE.createEffect(1, 2), false);
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    "Potion vs Stronger"
                }
            )
        );
    }
}
