package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Joiner;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.junit.jupiter.params.provider.Arguments;

public class ItemStackBookTest extends ItemStackTest {

    public static Stream<Arguments> data() {
        return StackProvider.compound(operators(), "%s %s", NAME_PARAMETER, Material.WRITTEN_BOOK, Material.WRITABLE_BOOK);
    }

    @SuppressWarnings("unchecked")
    static List<Object[]> operators() {
        return CompoundOperator.compound(
            Joiner.on('+'),
            NAME_PARAMETER,
            Long.parseLong("1110", 2),
            ItemStackLoreEnchantmentTest.operators(),
            Arrays.asList(
                new Object[] {
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            BookMeta meta = (BookMeta) cleanStack.getItemMeta();
                            meta.addPage("Page 1", "Page 2");
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
                    "Pages vs. Null"
                },
                new Object[] {
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            BookMeta meta = (BookMeta) cleanStack.getItemMeta();
                            meta.addPage("Page 1", "Page 2");
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            cleanStack.setItemMeta(cleanStack.getItemMeta());
                            return cleanStack;
                        }
                    },
                    "Pages vs. blank"
                },
                new Object[] {
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            BookMeta meta = (BookMeta) cleanStack.getItemMeta();
                            meta.addPage("Page 1", "Page 2");
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            BookMeta meta = (BookMeta) cleanStack.getItemMeta();
                            meta.addPage("Page 2", "Page 1");
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    "Pages switched"
                },
                new Object[] {
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            BookMeta meta = (BookMeta) cleanStack.getItemMeta();
                            meta.addPage("Page 1", "Page 2");
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            BookMeta meta = (BookMeta) cleanStack.getItemMeta();
                            meta.addPage("Page 1");
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    "Pages short"
                }
            ),
            Arrays.asList(
                new Object[] {
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            BookMeta meta = (BookMeta) cleanStack.getItemMeta();
                            meta.setAuthor("AnAuthor");
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
                    "Author vs. Null"
                },
                new Object[] {
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            BookMeta meta = (BookMeta) cleanStack.getItemMeta();
                            meta.setAuthor("AnAuthor");
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            cleanStack.setItemMeta(cleanStack.getItemMeta());
                            return cleanStack;
                        }
                    },
                    "Author vs. blank"
                },
                new Object[] {
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            BookMeta meta = (BookMeta) cleanStack.getItemMeta();
                            meta.setAuthor("AnAuthor");
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            BookMeta meta = (BookMeta) cleanStack.getItemMeta();
                            meta.setAuthor("AnotherAuthor");
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    "Authors"
                }
            ),
            Arrays.asList(
                new Object[] {
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            BookMeta meta = (BookMeta) cleanStack.getItemMeta();
                            meta.setTitle("Some title");
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
                    "Title vs. Null"
                },
                new Object[] {
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            BookMeta meta = (BookMeta) cleanStack.getItemMeta();
                            meta.setTitle("Some title");
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            cleanStack.setItemMeta(cleanStack.getItemMeta());
                            return cleanStack;
                        }
                    },
                    "title vs. blank"
                },
                new Object[] {
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            BookMeta meta = (BookMeta) cleanStack.getItemMeta();
                            meta.setTitle("Some title");
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    new Operator() {
                        @Override
                        public ItemStack operate(ItemStack cleanStack) {
                            BookMeta meta = (BookMeta) cleanStack.getItemMeta();
                            meta.setTitle("Different title");
                            cleanStack.setItemMeta(meta);
                            return cleanStack;
                        }
                    },
                    "Titles"
                }
            )
        );
    }
}
