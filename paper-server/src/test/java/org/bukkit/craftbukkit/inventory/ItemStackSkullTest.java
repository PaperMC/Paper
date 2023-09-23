package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Joiner;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.junit.jupiter.params.provider.Arguments;

public class ItemStackSkullTest extends ItemStackTest {

    public static Stream<Arguments> data() {
        return StackProvider.compound(operators(), "%s %s", NAME_PARAMETER, Material.PLAYER_HEAD);
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
                            SkullMeta meta = (SkullMeta) cleanStack.getItemMeta();
                            meta.setOwner("Notch");
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    new Operator() {
                        @Override
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
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            SkullMeta meta = (SkullMeta) cleanStack.getItemMeta();
                            meta.setOwner("Notch");
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    new Operator() {
                        @Override
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
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            SkullMeta meta = (SkullMeta) cleanStack.getItemMeta();
                            meta.setOwner("Notch");
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
                    "Name vs. None"
                }
            )
        );
    }
}
