package org.bukkit.craftbukkit.inventory;

import com.google.common.collect.ImmutableMap.Builder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.MinecraftKey;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.inventory.meta.KnowledgeBookMeta;

@DelegateDeserialization(SerializableMeta.class)
public class CraftMetaKnowledgeBook extends CraftMetaItem implements KnowledgeBookMeta {

    static final ItemMetaKeyType<List<MinecraftKey>> BOOK_RECIPES = new ItemMetaKeyType<>(DataComponents.RECIPES, "Recipes");
    static final int MAX_RECIPES = Short.MAX_VALUE;

    protected List<NamespacedKey> recipes = new ArrayList<NamespacedKey>();

    CraftMetaKnowledgeBook(CraftMetaItem meta) {
        super(meta);

        if (meta instanceof CraftMetaKnowledgeBook) {
            CraftMetaKnowledgeBook bookMeta = (CraftMetaKnowledgeBook) meta;
            this.recipes.addAll(bookMeta.recipes);
        }
    }

    CraftMetaKnowledgeBook(DataComponentPatch tag) {
        super(tag);

        getOrEmpty(tag, BOOK_RECIPES).ifPresent((pages) -> {
            for (int i = 0; i < pages.size(); i++) {
                MinecraftKey recipe = pages.get(i);

                addRecipe(CraftNamespacedKey.fromMinecraft(recipe));
            }
        });
    }

    CraftMetaKnowledgeBook(Map<String, Object> map) {
        super(map);

        Iterable<?> pages = SerializableMeta.getObject(Iterable.class, map, BOOK_RECIPES.BUKKIT, true);
        if (pages != null) {
            for (Object page : pages) {
                if (page instanceof String) {
                    addRecipe(CraftNamespacedKey.fromString((String) page));
                }
            }
        }
    }

    @Override
    void applyToItem(CraftMetaItem.Applicator itemData) {
        super.applyToItem(itemData);

        if (hasRecipes()) {
            List<MinecraftKey> list = new ArrayList<>();
            for (NamespacedKey recipe : this.recipes) {
                list.add(CraftNamespacedKey.toMinecraft(recipe));
            }
            itemData.put(BOOK_RECIPES, list);
        }
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && isBookEmpty();
    }

    boolean isBookEmpty() {
        return !(hasRecipes());
    }

    @Override
    boolean applicableTo(Material type) {
        return type == Material.KNOWLEDGE_BOOK;
    }

    @Override
    public boolean hasRecipes() {
        return !recipes.isEmpty();
    }

    @Override
    public void addRecipe(NamespacedKey... recipes) {
        for (NamespacedKey recipe : recipes) {
            if (recipe != null) {
                if (this.recipes.size() >= MAX_RECIPES) {
                    return;
                }

                this.recipes.add(recipe);
            }
        }
    }

    @Override
    public List<NamespacedKey> getRecipes() {
        return Collections.unmodifiableList(recipes);
    }

    @Override
    public void setRecipes(List<NamespacedKey> recipes) {
        this.recipes.clear();
        for (NamespacedKey recipe : recipes) {
            addRecipe(recipe);
        }
    }

    @Override
    public CraftMetaKnowledgeBook clone() {
        CraftMetaKnowledgeBook meta = (CraftMetaKnowledgeBook) super.clone();
        meta.recipes = new ArrayList<NamespacedKey>(recipes);
        return meta;
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();
        if (hasRecipes()) {
            hash = 61 * hash + 17 * this.recipes.hashCode();
        }
        return original != hash ? CraftMetaKnowledgeBook.class.hashCode() ^ hash : hash;
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaKnowledgeBook) {
            CraftMetaKnowledgeBook that = (CraftMetaKnowledgeBook) meta;

            return (hasRecipes() ? that.hasRecipes() && this.recipes.equals(that.recipes) : !that.hasRecipes());
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaKnowledgeBook || isBookEmpty());
    }

    @Override
    Builder<String, Object> serialize(Builder<String, Object> builder) {
        super.serialize(builder);

        if (hasRecipes()) {
            List<String> recipesString = new ArrayList<String>();
            for (NamespacedKey recipe : recipes) {
                recipesString.add(recipe.toString());
            }
            builder.put(BOOK_RECIPES.BUKKIT, recipesString);
        }

        return builder;
    }
}
