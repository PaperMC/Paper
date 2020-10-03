package net.minecraft.server;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CraftingManager extends ResourceDataJson {

    private static final Gson a = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private static final Logger LOGGER = LogManager.getLogger();
    public Map<Recipes<?>, Map<MinecraftKey, IRecipe<?>>> recipes = ImmutableMap.of();
    private boolean d;

    public CraftingManager() {
        super(CraftingManager.a, "recipes");
    }

    protected void a(Map<MinecraftKey, JsonElement> map, IResourceManager iresourcemanager, GameProfilerFiller gameprofilerfiller) {
        this.d = false;
        Map<Recipes<?>, Builder<MinecraftKey, IRecipe<?>>> map1 = Maps.newHashMap();
        Iterator iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry<MinecraftKey, JsonElement> entry = (Entry) iterator.next();
            MinecraftKey minecraftkey = (MinecraftKey) entry.getKey();

            try {
                IRecipe<?> irecipe = a(minecraftkey, ChatDeserializer.m((JsonElement) entry.getValue(), "top element"));

                ((Builder) map1.computeIfAbsent(irecipe.g(), (recipes) -> {
                    return ImmutableMap.builder();
                })).put(minecraftkey, irecipe);
            } catch (IllegalArgumentException | JsonParseException jsonparseexception) {
                CraftingManager.LOGGER.error("Parsing error loading recipe {}", minecraftkey, jsonparseexception);
            }
        }

        this.recipes = (Map) map1.entrySet().stream().collect(ImmutableMap.toImmutableMap(Entry::getKey, (entry1) -> {
            return ((Builder) entry1.getValue()).build();
        }));
        CraftingManager.LOGGER.info("Loaded {} recipes", map1.size());
    }

    public <C extends IInventory, T extends IRecipe<C>> Optional<T> craft(Recipes<T> recipes, C c0, World world) {
        return this.b(recipes).values().stream().flatMap((irecipe) -> {
            return SystemUtils.a(recipes.a(irecipe, world, c0));
        }).findFirst();
    }

    public <C extends IInventory, T extends IRecipe<C>> List<T> a(Recipes<T> recipes) {
        return (List) this.b(recipes).values().stream().map((irecipe) -> {
            return irecipe;
        }).collect(Collectors.toList());
    }

    public <C extends IInventory, T extends IRecipe<C>> List<T> b(Recipes<T> recipes, C c0, World world) {
        return (List) this.b(recipes).values().stream().flatMap((irecipe) -> {
            return SystemUtils.a(recipes.a(irecipe, world, c0));
        }).sorted(Comparator.comparing((irecipe) -> {
            return irecipe.getResult().j();
        })).collect(Collectors.toList());
    }

    private <C extends IInventory, T extends IRecipe<C>> Map<MinecraftKey, IRecipe<C>> b(Recipes<T> recipes) {
        return (Map) this.recipes.getOrDefault(recipes, Collections.emptyMap());
    }

    public <C extends IInventory, T extends IRecipe<C>> NonNullList<ItemStack> c(Recipes<T> recipes, C c0, World world) {
        Optional<T> optional = this.craft(recipes, c0, world);

        if (optional.isPresent()) {
            return ((IRecipe) optional.get()).b(c0);
        } else {
            NonNullList<ItemStack> nonnulllist = NonNullList.a(c0.getSize(), ItemStack.b);

            for (int i = 0; i < nonnulllist.size(); ++i) {
                nonnulllist.set(i, c0.getItem(i));
            }

            return nonnulllist;
        }
    }

    public Optional<? extends IRecipe<?>> getRecipe(MinecraftKey minecraftkey) {
        return this.recipes.values().stream().map((map) -> {
            return (IRecipe) map.get(minecraftkey);
        }).filter(Objects::nonNull).findFirst();
    }

    public Collection<IRecipe<?>> b() {
        return (Collection) this.recipes.values().stream().flatMap((map) -> {
            return map.values().stream();
        }).collect(Collectors.toSet());
    }

    public Stream<MinecraftKey> d() {
        return this.recipes.values().stream().flatMap((map) -> {
            return map.keySet().stream();
        });
    }

    public static IRecipe<?> a(MinecraftKey minecraftkey, JsonObject jsonobject) {
        String s = ChatDeserializer.h(jsonobject, "type");

        return ((RecipeSerializer) IRegistry.RECIPE_SERIALIZER.getOptional(new MinecraftKey(s)).orElseThrow(() -> {
            return new JsonSyntaxException("Invalid or unsupported recipe type '" + s + "'");
        })).a(minecraftkey, jsonobject);
    }
}
