package net.ironhorsedevgroup.mods.gunsmoke.recipes;

import com.google.gson.JsonObject;
import net.ironhorsedevgroup.mods.gunsmoke.Gunsmoke;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public class GunBenchRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    private final Item result;
    private List<Ingredient> ingredients = new ArrayList<>();

    public GunBenchRecipe(ResourceLocation id, Item result) {
        this.id = id;
        this.result = result;
    }

    public GunBenchRecipe(ResourceLocation id, Item result, List<Ingredient> ingredients) {
        this.id = id;
        this.result = result;
        this.ingredients = ingredients;
    }

    public GunBenchRecipe addStock(String item) {
        ingredients.add(0, getIngredient(item));
        return this;
    }

    public GunBenchRecipe addBarrel(String item) {
        ingredients.add(1, getIngredient(item));
        return this;
    }

    public GunBenchRecipe addCore(String item) {
        ingredients.add(2, getIngredient(item));
        return this;
    }

    public GunBenchRecipe addBreach(String item) {
        ingredients.add(3, getIngredient(item));
        return this;
    }

    @Override
    public boolean matches(SimpleContainer simpleContainer, Level level) {
        if (level.isClientSide()) {
            return false;
        }
        return
                        ingredients.get(0).test(simpleContainer.getItem(1)) &&
                        ingredients.get(1).test(simpleContainer.getItem(2)) &&
                        ingredients.get(2).test(simpleContainer.getItem(3)) &&
                        ingredients.get(3).test(simpleContainer.getItem(4));
    }

    @Override
    public ItemStack assemble(SimpleContainer simpleContainer) {
        return new ItemStack(result);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return new ItemStack(result);
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    private Ingredient getIngredient(String id) {
        return Ingredient.of(ForgeRegistries.ITEMS.getValue(new ResourceLocation(id)));
    }

    public static class Type implements RecipeType<GunBenchRecipe> {
        private Type() {}
        public static final Type INSTANCE = new Type();
        public static final String ID = "simple_gun_smithing";
    }

    public static class Serializer implements RecipeSerializer<GunBenchRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(Gunsmoke.MODID, "simple_gun_smithing");

        @Override
        public GunBenchRecipe fromJson(ResourceLocation recipeId, JsonObject jsonObject) {
            Item result = ForgeRegistries.ITEMS.getValue(new ResourceLocation(jsonObject.get("result").getAsString()));

            GunBenchRecipe recipe = new GunBenchRecipe(recipeId, result);

            recipe.addBarrel(jsonObject.get("barrel").getAsString());
            recipe.addBreach(jsonObject.get("breach").getAsString());
            recipe.addCore(jsonObject.get("core").getAsString());
            recipe.addStock(jsonObject.get("stock").getAsString());

            return recipe;
        }

        @Override
        public @Nullable GunBenchRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf byteBuf) {
            int len = byteBuf.readInt();
            List<Ingredient> ingredients = new ArrayList<>();

            for (int i = 0; i < len; i++) {
                ingredients.add(i, Ingredient.fromNetwork(byteBuf));
            }

            Item result = byteBuf.readItem().getItem();
            return new GunBenchRecipe(recipeId, result, ingredients);
        }

        @Override
        public void toNetwork(FriendlyByteBuf byteBuf, GunBenchRecipe gunBenchRecipe) {
            byteBuf.writeInt(gunBenchRecipe.getIngredients().size());

            for (Ingredient ingredient : gunBenchRecipe.getIngredients()) {
                ingredient.toNetwork(byteBuf);
            }
            byteBuf.writeItem(gunBenchRecipe.getResultItem());
        }
    }
}
