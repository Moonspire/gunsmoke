package net.ironhorsedevgroup.mods.gunsmoke.recipes;

import com.google.gson.JsonObject;
import net.ironhorsedevgroup.mods.gunsmoke.Gunsmoke;
import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
    private Ingredient stock = Ingredient.of(Items.AIR);
    private Ingredient breach = Ingredient.of(Items.AIR);
    private Ingredient core = Ingredient.of(Items.AIR);
    private Ingredient barrel = Ingredient.of(Items.AIR);

    public GunBenchRecipe(ResourceLocation id, Item result) {
        this.id = id;
        this.result = result;
    }

    public GunBenchRecipe(ResourceLocation id, Item result, List<Ingredient> ingredients) {
        this.id = id;
        this.result = result;
        this.stock = ingredients.get(0);
        this.breach = ingredients.get(1);
        this.core = ingredients.get(2);
        this.barrel = ingredients.get(3);
    }

    public GunBenchRecipe addStock(String item) {
        stock = getIngredient(item);
        return this;
    }

    public GunBenchRecipe addBarrel(String item) {
        barrel = getIngredient(item);
        return this;
    }

    public GunBenchRecipe addCore(String item) {
        core = getIngredient(item);
        return this;
    }

    public GunBenchRecipe addBreach(String item) {
        breach = getIngredient(item);
        return this;
    }

    @Override
    public boolean matches(SimpleContainer simpleContainer, Level level) {
        if (level.isClientSide()) {
            return false;
        }
        return
                stock.test(simpleContainer.getItem(0)) &&
                breach.test(simpleContainer.getItem(1)) &&
                core.test(simpleContainer.getItem(2)) &&
                barrel.test(simpleContainer.getItem(3));
    }

    @Override
    public ItemStack assemble(SimpleContainer simpleContainer) {
        ItemStack gunItem = new ItemStack(result);
        NBT.putStringTag(gunItem, "material_1", NBT.getStringTag(simpleContainer.getItem(0), "material"));
        NBT.putStringTag(gunItem, "material_2", NBT.getStringTag(simpleContainer.getItem(3), "material"));
        NBT.putStringTag(gunItem, "material_3", NBT.getStringTag(simpleContainer.getItem(2), "material"));
        NBT.putStringTag(gunItem, "material_4", NBT.getStringTag(simpleContainer.getItem(1), "material"));
        return gunItem;
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
