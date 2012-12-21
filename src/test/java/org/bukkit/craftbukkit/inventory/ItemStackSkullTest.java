package org.bukkit.craftbukkit.inventory;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.base.Joiner;

@RunWith(Parameterized.class)
public class ItemStackSkullTest extends ItemStackTest {

    @Parameters(name="[{index}]:{" + NAME_PARAMETER + "}")
    public static List<Object[]> data() {
        return StackProvider.compound(operators(), "%s %s", NAME_PARAMETER, Material.SKULL_ITEM);
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
                        public ItemStack operate(ItemStack cleanStack) {
                            SkullMeta meta = (SkullMeta) cleanStack.getItemMeta();
                            meta.setOwner("Notch");
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    new Operator() {
                        public ItemStack operate(ItemStack cleanStack) {
                            SkullMeta meta = (SkullMeta) cleanStack.getItemMeta();
                            meta.setOwner("Dinnerbone");
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    "Name 1 vs. Name 2"
                },
                new Object[] {
                    new Operator() {
                        public ItemStack operate(ItemStack cleanStack) {
                            SkullMeta meta = (SkullMeta) cleanStack.getItemMeta();
                            meta.setOwner("Notch");
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    new Operator() {
                        public ItemStack operate(ItemStack cleanStack) {
                            SkullMeta meta = (SkullMeta) cleanStack.getItemMeta();
                            meta.setOwner(null);
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    "Name vs. Null"
                },
                new Object[] {
                    new Operator() {
                        public ItemStack operate(ItemStack cleanStack) {
                            SkullMeta meta = (SkullMeta) cleanStack.getItemMeta();
                            meta.setOwner("Notch");
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    new Operator() {
                        public ItemStack operate(ItemStack cleanStack) {
                            return cleanStack;
                        }
                    },
                    "Name vs. None"
                }
            )
        );
    }
}
