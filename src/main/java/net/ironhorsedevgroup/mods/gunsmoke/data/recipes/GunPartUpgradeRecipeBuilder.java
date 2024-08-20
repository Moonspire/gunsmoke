package net.ironhorsedevgroup.mods.gunsmoke.data.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.ironhorsedevgroup.mods.gunsmoke.item.guns.GunMaterial;
import net.ironhorsedevgroup.mods.gunsmoke.registry.GunsmokeItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.Registry;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class GunPartUpgradeRecipeBuilder implements RecipeBuilder {
    private final Item result;
    private final GunMaterial material;
    private final Ingredient ingredient;
    private final Advancement.Builder advancement = Advancement.Builder.advancement();
    @javax.annotation.Nullable
    private String group;

    public GunPartUpgradeRecipeBuilder(Item result, Ingredient ingredient, GunMaterial material) {
        this.result = result;
        this.material = material;
        this.ingredient = ingredient;
    }

    @Override
    public @NotNull RecipeBuilder unlockedBy(@NotNull String s, @NotNull CriterionTriggerInstance criterionTriggerInstance) {
        this.advancement.addCriterion(s, criterionTriggerInstance);
        return this;
    }

    @Override
    public @NotNull RecipeBuilder group(@Nullable String s) {
        this.group = s;
        return this;
    }

    @Override
    public @NotNull Item getResult() {
        return result;
    }

    @Override
    public void save(Consumer<FinishedRecipe> consumer, @NotNull ResourceLocation resourceLocation) {
        this.ensureValid(resourceLocation);
        this.advancement.parent(ROOT_RECIPE_ADVANCEMENT).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(resourceLocation)).rewards(net.minecraft.advancements.AdvancementRewards.Builder.recipe(resourceLocation)).requirements(RequirementsStrategy.OR);
        String var10006 = this.group == null ? "" : this.group;
        String var10011 = resourceLocation.getNamespace();
        String var10012 = this.result.getItemCategory().getRecipeFolderName();
        consumer.accept(new Result(resourceLocation, this.result, this.material, var10006, this.ingredient, this.advancement, new ResourceLocation(var10011, "recipes/" + var10012 + "/" + resourceLocation.getPath())));
    }

    private void ensureValid(ResourceLocation p_126208_) {
        if (this.advancement.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + p_126208_);
        }
    }

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final Item result;
        private final GunMaterial material;
        private final String group;
        private final Ingredient ingredient;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;
        
        public Result(ResourceLocation location, Item result, GunMaterial material, String group, Ingredient ingredient, Advancement.Builder advancement, ResourceLocation advancementId) {
            this.id = location;
            this.result = result;
            this.material = material;
            this.group = group;
            this.ingredient = ingredient;
            this.advancement = advancement;
            this.advancementId = advancementId;
        }

        @Override
        public void serializeRecipeData(@NotNull JsonObject json) {
            if (!this.group.isEmpty()) {
                json.addProperty("group", this.group);
            }

            JsonObject nbt = new JsonObject();
            nbt.addProperty("material", material.getName());

            JsonArray ingredients = new JsonArray();

            JsonObject part = ingredient.toJson().getAsJsonObject();
            part.addProperty("type", "forge:partial_nbt");
            part.add("nbt", nbt);
            ingredients.add(part);

            //part = Ingredient.of(GunsmokeItems.GUN_PARTS.get()).toJson().getAsJsonObject();
            part.addProperty("type", "forge:partial_nbt");
            part.add("nbt", nbt);
            ingredients.add(part);

            json.add("ingredients", ingredients);

            JsonObject resultJson = new JsonObject();
            resultJson.addProperty("item", Registry.ITEM.getKey(this.result).toString());
            resultJson.add("nbt", nbt);
            json.add("result", resultJson);
        }

        @Override
        public @NotNull ResourceLocation getId() {
            return this.id;
        }

        @Override
        public @NotNull RecipeSerializer<?> getType() {
            return RecipeSerializer.SHAPELESS_RECIPE;
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return this.advancement.serializeToJson();
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return this.advancementId;
        }
    }
}
