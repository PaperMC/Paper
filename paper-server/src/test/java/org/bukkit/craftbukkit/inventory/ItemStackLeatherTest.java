package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Joiner;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.junit.jupiter.params.provider.Arguments;

public class ItemStackLeatherTest extends ItemStackTest {

    public static Stream<Arguments> data() {
        return StackProvider.compound(operators(), "%s %s", NAME_PARAMETER, Material.LEATHER_BOOTS, Material.LEATHER_CHESTPLATE, Material.LEATHER_HELMET, Material.LEATHER_LEGGINGS);
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
                            LeatherArmorMeta meta = (LeatherArmorMeta) cleanStack.getItemMeta();
                            meta.setColor(Color.FUCHSIA);
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
                    "Color vs Null"
                },
                new Object[] {
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            LeatherArmorMeta meta = (LeatherArmorMeta) cleanStack.getItemMeta();
                            meta.setColor(Color.GRAY);
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            LeatherArmorMeta meta = (LeatherArmorMeta) cleanStack.getItemMeta();
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    "Color vs Blank"
                },
                new Object[] {
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            LeatherArmorMeta meta = (LeatherArmorMeta) cleanStack.getItemMeta();
                            meta.setColor(Color.MAROON);
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            LeatherArmorMeta meta = (LeatherArmorMeta) cleanStack.getItemMeta();
                            meta.setColor(Color.ORANGE);
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    "Color vs Other"
                }
            )
        );
    }
}
