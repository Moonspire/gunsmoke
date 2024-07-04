package net.ironhorsedevgroup.mods.gunsmoke.recipes;

import net.ironhorsedevgroup.mods.gunsmoke.item.guns.GunMaterials;
import net.ironhorsedevgroup.mods.gunsmoke.registry.GunsmokeItems;
import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import slimeknights.mantle.recipe.helper.ItemOutput;
import slimeknights.tconstruct.library.recipe.casting.ItemCastingRecipeBuilder;
import slimeknights.tconstruct.library.recipe.partbuilder.ItemPartRecipeBuilder;
import slimeknights.tconstruct.library.recipe.partbuilder.Pattern;

import java.util.function.Consumer;

public class RecipeGenerator extends RecipeProvider implements IConditionBuilder {
    public RecipeGenerator(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        for (GunMaterials material : GunMaterials.values()) {
            if (material.getFluidID() != null) {
                castRecipe(GunsmokeItems.CAST_BARREL_SHORT.get(), material, 90 * 2, 20, addMaterial(GunsmokeItems.BARREL_SHORT.get(), material), consumer);
                castRecipe(GunsmokeItems.CAST_BARREL_MEDIUM.get(), material, 90 * 3, 20, addMaterial(GunsmokeItems.BARREL_MEDIUM.get(), material), consumer);
                castRecipe(GunsmokeItems.CAST_BARREL_LONG.get(), material, 90 * 4, 20, addMaterial(GunsmokeItems.BARREL_LONG.get(), material), consumer);
                castRecipe(GunsmokeItems.CAST_STOCK.get(), material, 90 * 3, 20, addMaterial(GunsmokeItems.STOCK.get(), material), consumer);
            }
        }
    }

    public ItemStack addMaterial(Item item, GunMaterials material) {
        ItemStack retStack = new ItemStack(item);
        return NBT.putStringTag(retStack, "material", material.getSerializedName());
    }

    public void castRecipe(ItemLike cast, GunMaterials material, Integer amount, Integer coolTime, ItemStack result, Consumer<FinishedRecipe> consumer) {
        if (material.getFluid() != null) {
            ItemCastingRecipeBuilder
                    .tableRecipe(ItemOutput.fromStack(result))
                    .setFluid(material.getFluid(), amount)
                    .setCast(cast, false)
                    .setCoolingTime(coolTime)
                    .save(consumer, new ResourceLocation("gunsmoke", "casting/" + result.getDescriptionId()));
        }
    }

    public void partsRecipe(ItemStack result, Consumer<FinishedRecipe> consumer) {
        ItemPartRecipeBuilder
                .item(new ResourceLocation("gunsmoke", "builder/" + result.getDescriptionId()), ItemOutput.fromStack(result))
                .save(consumer);
    }
}