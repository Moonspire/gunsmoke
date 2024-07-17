package net.ironhorsedevgroup.mods.gunsmoke.data;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.ironhorsedevgroup.mods.gunsmoke.item.guns.GunMaterial;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.Registry;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class VanillaShapedRecipeBuilder implements RecipeBuilder {
    private final Item result;
    private final GunMaterial material;
    private final List<String> modlist = Lists.newArrayList();
    private final List<String> rows = Lists.newArrayList();
    private final Map<Character, Ingredient> key = Maps.newLinkedHashMap();
    private final Advancement.Builder advancement = Advancement.Builder.advancement();
    private boolean requireMaterial = false;
    @javax.annotation.Nullable
    private String group;

    public VanillaShapedRecipeBuilder(Item result, GunMaterial material) {
        this.result = result;
        this.material = material;
    }

    public VanillaShapedRecipeBuilder define(Character character, TagKey<Item> item) {
        return this.define(character, Ingredient.of(item));
    }

    public VanillaShapedRecipeBuilder define(Character character, ItemLike item) {
        return this.define(character, Ingredient.of(item));
    }

    public VanillaShapedRecipeBuilder define(Character character, Ingredient ingredient) {
        if (this.key.containsKey(character)) {
            throw new IllegalArgumentException("Symbol '" + character + "' is already defined!");
        } else if (character == ' ') {
            throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
        } else {
            this.key.put(character, ingredient);
            return this;
        }
    }

    public VanillaShapedRecipeBuilder pattern(String pattern) {
        if (!this.rows.isEmpty() && pattern.length() != ((String)this.rows.get(0)).length()) {
            throw new IllegalArgumentException("Pattern must be the same width on every line!");
        } else {
            this.rows.add(pattern);
            return this;
        }
    }

    public VanillaShapedRecipeBuilder addMod(String modID) {
        this.modlist.add(modID);
        return this;
    }

    public VanillaShapedRecipeBuilder requireMaterial() {
        this.requireMaterial = true;
        return this;
    }

    public VanillaShapedRecipeBuilder addMod(ResourceLocation location) {
        this.modlist.add(location.getNamespace());
        return this;
    }

    public @NotNull VanillaShapedRecipeBuilder unlockedBy(@NotNull String s, @NotNull CriterionTriggerInstance criterion) {
        this.advancement.addCriterion(s, criterion);
        return this;
    }

    public @NotNull VanillaShapedRecipeBuilder group(@javax.annotation.Nullable String group) {
        this.group = group;
        return this;
    }

    public @NotNull Item getResult() {
        return this.result;
    }

    public void save(Consumer<FinishedRecipe> consumer, ResourceLocation location) {
        this.ensureValid(location);
        this.advancement.parent(ROOT_RECIPE_ADVANCEMENT).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(location)).rewards(net.minecraft.advancements.AdvancementRewards.Builder.recipe(location)).requirements(RequirementsStrategy.OR);
        String var10006 = this.group == null ? "" : this.group;
        String var10012 = location.getNamespace();
        String var10013 = this.result.getItemCategory().getRecipeFolderName();
        consumer.accept(new Result(location, this.result, this.material, this.modlist, var10006, (List) this.rows, (Map) this.key, this.advancement, new ResourceLocation(var10012, "recipes/" + var10013 + "/" + location.getPath()), this.requireMaterial));
    }

    private void ensureValid(ResourceLocation location) {
        if (this.rows.isEmpty()) {
            throw new IllegalStateException("No pattern is defined for shaped recipe " + location + "!");
        } else {
            Set<Character> $$1 = Sets.newHashSet(this.key.keySet());
            $$1.remove(' ');
            Iterator var3 = this.rows.iterator();

            while(var3.hasNext()) {
                String line = (String)var3.next();

                for(int ingredients = 0; ingredients < line.length(); ++ingredients) {
                    char ingredient = line.charAt(ingredients);
                    if (!this.key.containsKey(ingredient) && ingredient != ' ' && ingredient != 'X') {
                        throw new IllegalStateException("Pattern in recipe " + location + " uses undefined symbol '" + ingredient + "'");
                    }

                    $$1.remove(ingredient);
                }
            }

            if (!$$1.isEmpty()) {
                throw new IllegalStateException("Ingredients are defined but not used in pattern for recipe " + location);
            } else if (this.rows.size() == 1 && ((String)this.rows.get(0)).length() == 1) {
                throw new IllegalStateException("Shaped recipe " + location + " only takes in a single item - should it be a shapeless recipe instead?");
            }
        }
    }
    
    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final Item result;
        private final GunMaterial material;
        private final List<String> modlist;
        private final String group;
        private final List<String> pattern;
        private final Map<Character, Ingredient> key;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;
        private final boolean requireMaterial;

        public Result(ResourceLocation location, Item item, GunMaterial material, List<String> modlist, String group, List<String> pattern, Map<Character, Ingredient> keys, Advancement.Builder advancement, ResourceLocation advancementId, boolean requireMaterial) {
            this.id = location;
            this.result = item;
            this.material = material;
            this.modlist = modlist;
            this.group = group;
            this.pattern = pattern;
            this.key = keys;
            this.advancement = advancement;
            this.advancementId = advancementId;
            this.requireMaterial = requireMaterial;
        }

        public void serializeRecipeData(JsonObject json) {
            JsonObject nbt = new JsonObject();
            nbt.addProperty("material", material.getName());

            JsonArray pattern = new JsonArray();
            boolean usesDefault = false;

            for (String line : this.pattern) {
                if (line.contains("X")) {
                    usesDefault = true;
                }
                pattern.add(line);
            }
            JsonObject ingredients = new JsonObject();

            for (Map.Entry<Character, Ingredient> characterIngredientEntry : this.key.entrySet()) {
                JsonObject part = characterIngredientEntry.getValue().toJson().getAsJsonObject();
                if (requireMaterial) {
                    part.addProperty("type", "forge:partial_nbt");
                    part.add("nbt", nbt);
                }
                ingredients.add(String.valueOf(characterIngredientEntry.getKey()), part);
            }

            if (usesDefault) {
                JsonObject itemIngedient = new JsonObject();
                itemIngedient.addProperty("item", material.getCraftingItemID().toString());
                ingredients.add("X", itemIngedient);
            }

            JsonObject result = new JsonObject();

            result.addProperty("item", Registry.ITEM.getKey(this.result).toString());
            result.add("nbt", nbt);
            
            if (!modlist.isEmpty()) {
                JsonArray recipes = new JsonArray();
                JsonObject conditionalRecipe = new JsonObject();

                JsonArray conditions = new JsonArray();

                for (String modid : modlist) {
                    JsonObject condition = new JsonObject();

                    condition.addProperty("type", "forge:not");

                    JsonObject modCheck = new JsonObject();

                    modCheck.addProperty("type", "forge:mod_loaded");
                    modCheck.addProperty("modid", modid);

                    condition.add("value", modCheck);
                    conditions.add(condition);
                }

                conditionalRecipe.add("conditions", conditions);

                JsonObject recipe = new JsonObject();

                if (!this.group.isEmpty()) {
                    recipe.addProperty("group", this.group);
                }

                recipe.addProperty("type", "minecraft:crafting_shaped");
                recipe.add("pattern", pattern);

                recipe.add("key", ingredients);

                recipe.add("result", result);

                conditionalRecipe.add("recipe", recipe);

                recipes.add(conditionalRecipe);
                json.add("recipes", recipes);
            } else {
                if (!this.group.isEmpty()) {
                    json.addProperty("group", this.group);
                }

                json.add("pattern", pattern);

                json.add("key", ingredients);

                json.add("result", result);
            }
        }

        public RecipeSerializer<?> getType() {
            if (modlist.isEmpty()) {
                return RecipeSerializer.SHAPED_RECIPE;
            }
            return ConditionalRecipe.SERIALZIER;
        }

        public ResourceLocation getId() {
            return this.id;
        }

        @Override
        public JsonObject serializeAdvancement() {
            return this.advancement.serializeToJson();
        }

        @Nullable
        public ResourceLocation getAdvancementId() {
            return this.advancementId;
        }
    }
}
