package org.bukkit.craftbukkit.inventory;

import java.util.Arrays;
import java.util.List;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.base.Joiner;

@RunWith(Parameterized.class)
public class ItemStackLoreEnchantmentTest extends ItemStackTest {

    @Parameters(name="[{index}]:{" + NAME_PARAMETER + "}")
    public static List<Object[]> data() {
        return StackProvider.compound(operators(), "%s %s", NAME_PARAMETER, ItemStackTest.COMPOUND_MATERIALS);
    }

    @SuppressWarnings("unchecked")
    static List<Object[]> operators() {
        return CompoundOperator.compound(
            Joiner.on('+'),
            NAME_PARAMETER,
            ~0l,
            Arrays.asList(
                new Object[] {
                    new Operator() {
                        public ItemStack operate(ItemStack cleanStack) {
                            ItemMeta meta = cleanStack.getItemMeta();
                            meta.setLore(Arrays.asList("First Lore", "Second Lore"));
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    new Operator() {
                        public ItemStack operate(ItemStack cleanStack) {
                            return cleanStack;
                        }
                    },
                    "Lore vs Null"
                },
                new Object[] {
                    new Operator() {
                        public ItemStack operate(ItemStack cleanStack) {
                            ItemMeta meta = cleanStack.getItemMeta();
                            meta.setLore(Arrays.asList("Some lore"));
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    new Operator() {
                        public ItemStack operate(ItemStack cleanStack) {
                            ItemMeta meta = cleanStack.getItemMeta();
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    "Lore vs Blank"
                },
                new Object[] {
                    new Operator() {
                        public ItemStack operate(ItemStack cleanStack) {
                            ItemMeta meta = cleanStack.getItemMeta();
                            meta.setLore(Arrays.asList("Some more lore", "Another lore"));
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    new Operator() {
                        public ItemStack operate(ItemStack cleanStack) {
                            ItemMeta meta = cleanStack.getItemMeta();
                            meta.setLore(Arrays.asList("Some more lore"));
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    "Lore vs Other"
                }
            ),
            Arrays.asList(
                new Object[] {
                    new Operator() {
                        public ItemStack operate(ItemStack cleanStack) {
                            ItemMeta meta = cleanStack.getItemMeta();
                            meta.setDisplayName("TestItemName");
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    new Operator() {
                        public ItemStack operate(ItemStack cleanStack) {
                            return cleanStack;
                        }
                    },
                    "Name vs Null"
                },
                new Object[] {
                    new Operator() {
                        public ItemStack operate(ItemStack cleanStack) {
                            ItemMeta meta = cleanStack.getItemMeta();
                            meta.setDisplayName("AnotherItemName");
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    new Operator() {
                        public ItemStack operate(ItemStack cleanStack) {
                            ItemMeta meta = cleanStack.getItemMeta();
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    "Name vs Blank"
                },
                new Object[] {
                    new Operator() {
                        public ItemStack operate(ItemStack cleanStack) {
                            ItemMeta meta = cleanStack.getItemMeta();
                            meta.setDisplayName("The original ItemName");
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    new Operator() {
                        public ItemStack operate(ItemStack cleanStack) {
                            ItemMeta meta = cleanStack.getItemMeta();
                            meta.setDisplayName("The other name");
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    "Name vs Other"
                }
            ),
            Arrays.asList(
                new Object[] {
                    new Operator() {
                        public ItemStack operate(ItemStack cleanStack) {
                            cleanStack.addUnsafeEnchantment(Enchantment.DIG_SPEED, 2);
                            return cleanStack;
                        }
                    },
                    new Operator() {
                        public ItemStack operate(ItemStack cleanStack) {
                            return cleanStack;
                        }
                    },
                    "EnchantStack vs Null"
                },
                new Object[] {
                    new Operator() {
                        public ItemStack operate(ItemStack cleanStack) {
                            cleanStack.addUnsafeEnchantment(Enchantment.OXYGEN, 1);
                            return cleanStack;
                        }
                    },
                    new Operator() {
                        public ItemStack operate(ItemStack cleanStack) {
                            ItemMeta meta = cleanStack.getItemMeta();
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    "EnchantStack vs Blank"
                },
                new Object[] {
                    new Operator() {
                        public ItemStack operate(ItemStack cleanStack) {
                            cleanStack.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);
                            return cleanStack;
                        }
                    },
                    new Operator() {
                        public ItemStack operate(ItemStack cleanStack) {
                            cleanStack.addUnsafeEnchantment(Enchantment.ARROW_FIRE, 1);
                            return cleanStack;
                        }
                    },
                    "EnchantStack vs OtherEnchantStack"
                },
                new Object[] {
                    new Operator() {
                        public ItemStack operate(ItemStack cleanStack) {
                            ItemMeta meta = cleanStack.getItemMeta();
                            meta.addEnchant(Enchantment.DURABILITY, 1, true);
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    new Operator() {
                        public ItemStack operate(ItemStack cleanStack) {
                            ItemMeta meta = cleanStack.getItemMeta();
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    "Enchant vs Blank"
                },
                new Object[] {
                    new Operator() {
                        public ItemStack operate(ItemStack cleanStack) {
                            ItemMeta meta = cleanStack.getItemMeta();
                            meta.addEnchant(Enchantment.KNOCKBACK, 1, true);
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    new Operator() {
                        public ItemStack operate(ItemStack cleanStack) {
                            return cleanStack;
                        }
                    },
                    "Enchant vs Null"
                },
                new Object[] {
                    new Operator() {
                        public ItemStack operate(ItemStack cleanStack) {
                            ItemMeta meta = cleanStack.getItemMeta();
                            meta.addEnchant(Enchantment.PROTECTION_FIRE, 1, true);
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    new Operator() {
                        public ItemStack operate(ItemStack cleanStack) {
                            ItemMeta meta = cleanStack.getItemMeta();
                            meta.addEnchant(Enchantment.PROTECTION_FIRE, 2, true);
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    "Enchant vs Other"
                }
            )
        );
    }
}
