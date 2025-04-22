package org.bukkit.craftbukkit.inventory;

import com.google.common.collect.ImmutableMap.Builder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.inventory.meta.KnowledgeBookMeta;

@DelegateDeserialization(SerializableMeta.class)
public class CraftMetaKnowledgeBook extends CraftMetaItem implements KnowledgeBookMeta {

    static final ItemMetaKeyType<List<ResourceKey<Recipe<?>>>> BOOK_RECIPES = new ItemMetaKeyType<>(DataComponents.RECIPES, "Recipes");
    static final int MAX_RECIPES = Short.MAX_VALUE;

    protected List<NamespacedKey> recipes = new ArrayList<>();

    CraftMetaKnowledgeBook(CraftMetaItem meta) {
        super(meta);

        if (meta instanceof final CraftMetaKnowledgeBook bookMeta) {
            this.recipes.addAll(bookMeta.recipes);
        }
    }

    CraftMetaKnowledgeBook(DataComponentPatch tag, java.util.Set<net.minecraft.core.component.DataComponentType<?>> extraHandledDcts) {
        super(tag, extraHandledDcts);

        getOrEmpty(tag, CraftMetaKnowledgeBook.BOOK_RECIPES).ifPresent((recipes) -> {
            for (ResourceKey<?> recipe : recipes) {
                this.addRecipe(CraftNamespacedKey.fromMinecraft(recipe.location()));
            }
        });
    }

    CraftMetaKnowledgeBook(Map<String, Object> map) {
        super(map);

        Iterable<?> pages = SerializableMeta.getObject(Iterable.class, map, CraftMetaKnowledgeBook.BOOK_RECIPES.BUKKIT, true);
        if (pages != null) {
            for (Object page : pages) {
                if (page instanceof String) {
                    this.addRecipe(CraftNamespacedKey.fromString((String) page));
                }
            }
        }
    }

    @Override
    void applyToItem(CraftMetaItem.Applicator tag) {
        super.applyToItem(tag);

        if (this.hasRecipes()) {
            List<ResourceKey<Recipe<?>>> list = new ArrayList<>();
            for (NamespacedKey recipe : this.recipes) {
                list.add(CraftRecipe.toMinecraft(recipe));
            }
            tag.put(CraftMetaKnowledgeBook.BOOK_RECIPES, list);
        }
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.isBookEmpty();
    }

    boolean isBookEmpty() {
        return !(this.hasRecipes());
    }

    @Override
    public boolean hasRecipes() {
        return !this.recipes.isEmpty();
    }

    @Override
    public void addRecipe(NamespacedKey... recipes) {
        for (NamespacedKey recipe : recipes) {
            if (recipe != null) {
                if (this.recipes.size() >= CraftMetaKnowledgeBook.MAX_RECIPES) {
                    return;
                }

                this.recipes.add(recipe);
            }
        }
    }

    @Override
    public List<NamespacedKey> getRecipes() {
        return Collections.unmodifiableList(this.recipes);
    }

    @Override
    public void setRecipes(List<NamespacedKey> recipes) {
        this.recipes.clear();
        for (NamespacedKey recipe : recipes) {
            this.addRecipe(recipe);
        }
    }

    @Override
    public CraftMetaKnowledgeBook clone() {
        CraftMetaKnowledgeBook meta = (CraftMetaKnowledgeBook) super.clone();
        meta.recipes = new ArrayList<>(this.recipes);
        return meta;
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();
        if (this.hasRecipes()) {
            hash = 61 * hash + 17 * this.recipes.hashCode();
        }
        return original != hash ? CraftMetaKnowledgeBook.class.hashCode() ^ hash : hash;
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof final CraftMetaKnowledgeBook other) {
            return (this.hasRecipes() ? other.hasRecipes() && this.recipes.equals(other.recipes) : !other.hasRecipes());
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaKnowledgeBook || this.isBookEmpty());
    }

    @Override
    Builder<String, Object> serialize(Builder<String, Object> builder) {
        super.serialize(builder);

        if (this.hasRecipes()) {
            List<String> recipesString = new ArrayList<>();
            for (NamespacedKey recipe : this.recipes) {
                recipesString.add(recipe.toString());
            }
            builder.put(CraftMetaKnowledgeBook.BOOK_RECIPES.BUKKIT, recipesString);
        }

        return builder;
    }
}
