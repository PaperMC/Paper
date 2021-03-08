package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Joiner;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;
import org.bukkit.craftbukkit.inventory.ItemStackTest.CompoundOperator;
import org.bukkit.craftbukkit.inventory.ItemStackTest.Operator;
import org.bukkit.craftbukkit.inventory.ItemStackTest.StackProvider;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ItemStackFireworkTest extends ItemStackTest {

    @Parameters(name = "[{index}]:{" + NAME_PARAMETER + "}")
    public static List<Object[]> data() {
        return StackProvider.compound(operators(), "%s %s", NAME_PARAMETER, Material.FIREWORK_ROCKET);
    }

    @SuppressWarnings("unchecked")
    static List<Object[]> operators() {
        return CompoundOperator.compound(
            Joiner.on('+'),
            NAME_PARAMETER,
            Long.parseLong("110", 2),
            ItemStackLoreEnchantmentTest.operators(),
            Arrays.asList(
                new Object[] {
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            FireworkMeta meta = (FireworkMeta) cleanStack.getItemMeta();
                            meta.addEffect(FireworkEffect.builder().withColor(Color.WHITE).build());
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            FireworkMeta meta = (FireworkMeta) cleanStack.getItemMeta();
                            meta.addEffect(FireworkEffect.builder().withColor(Color.BLACK).build());
                            meta.addEffect(FireworkEffect.builder().withColor(Color.GREEN).build());
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    "Effect Color 1 vs. Effect Color 2"
                },
                new Object[] {
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            FireworkMeta meta = (FireworkMeta) cleanStack.getItemMeta();
                            meta.addEffect(FireworkEffect.builder().withColor(Color.WHITE).with(Type.CREEPER).build());
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            FireworkMeta meta = (FireworkMeta) cleanStack.getItemMeta();
                            meta.addEffect(FireworkEffect.builder().withColor(Color.WHITE).with(Type.BURST).build());
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    "Effect type 1 vs. Effect type 2"
                },
                new Object[] {
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            FireworkMeta meta = (FireworkMeta) cleanStack.getItemMeta();
                            meta.addEffect(FireworkEffect.builder().withColor(Color.WHITE).withFade(Color.BLUE).build());
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            FireworkMeta meta = (FireworkMeta) cleanStack.getItemMeta();
                            meta.addEffect(FireworkEffect.builder().withColor(Color.WHITE).withFade(Color.RED).build());
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    "Effect fade 1 vs. Effect fade 2"
                },
                new Object[] {
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            FireworkMeta meta = (FireworkMeta) cleanStack.getItemMeta();
                            meta.addEffect(FireworkEffect.builder().withColor(Color.WHITE).withFlicker().build());
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            FireworkMeta meta = (FireworkMeta) cleanStack.getItemMeta();
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    "Effect vs. Null"
                },
                new Object[] {
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            FireworkMeta meta = (FireworkMeta) cleanStack.getItemMeta();
                            meta.addEffect(FireworkEffect.builder().withColor(Color.WHITE).withTrail().build());
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
                    "Effect vs. None"
                }
            ),
            Arrays.asList(
                new Object[] {
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            FireworkMeta meta = (FireworkMeta) cleanStack.getItemMeta();
                            meta.setPower(127);
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            FireworkMeta meta = (FireworkMeta) cleanStack.getItemMeta();
                            meta.setPower(100);
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    "Height vs. Other"
                },
                new Object[] {
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            FireworkMeta meta = (FireworkMeta) cleanStack.getItemMeta();
                            meta.setPower(42);
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            FireworkMeta meta = (FireworkMeta) cleanStack.getItemMeta();
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    "Height vs. Null"
                },
                new Object[] {
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            FireworkMeta meta = (FireworkMeta) cleanStack.getItemMeta();
                            meta.setPower(10);
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
                    "Height vs. None"
                }
            )
        );
    }
}
