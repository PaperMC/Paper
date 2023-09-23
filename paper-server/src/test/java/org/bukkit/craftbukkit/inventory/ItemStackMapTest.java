package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Joiner;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.junit.jupiter.params.provider.Arguments;

public class ItemStackMapTest extends ItemStackTest {

    public static Stream<Arguments> data() {
        return StackProvider.compound(operators(), "%s %s", NAME_PARAMETER, Material.FILLED_MAP);
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
                            MapMeta meta = (MapMeta) cleanStack.getItemMeta();
                            meta.setScaling(true);
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            MapMeta meta = (MapMeta) cleanStack.getItemMeta();
                            meta.setScaling(false);
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    "Scale vs. Unscale"
                },
                new Object[] {
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            MapMeta meta = (MapMeta) cleanStack.getItemMeta();
                            meta.setScaling(true);
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            MapMeta meta = (MapMeta) cleanStack.getItemMeta();
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    "Scale vs. Blank"
                },
                new Object[] {
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            MapMeta meta = (MapMeta) cleanStack.getItemMeta();
                            meta.setScaling(false);
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            MapMeta meta = (MapMeta) cleanStack.getItemMeta();
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    "Unscale vs. Blank"
                },
                new Object[] {
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            MapMeta meta = (MapMeta) cleanStack.getItemMeta();
                            meta.setScaling(true);
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
                    "Scale vs. None"
                },
                new Object[] {
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            MapMeta meta = (MapMeta) cleanStack.getItemMeta();
                            meta.setScaling(false);
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
                    "Unscale vs. None"
                }
            )
        );
    }
}
