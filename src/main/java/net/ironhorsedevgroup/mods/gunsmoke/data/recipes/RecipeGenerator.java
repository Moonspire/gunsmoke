package net.ironhorsedevgroup.mods.gunsmoke.data.recipes;

import net.ironhorsedevgroup.mods.gunsmoke.item.guns.GunMaterial;
import net.ironhorsedevgroup.mods.gunsmoke.registry.GunsmokeMaterials;
import net.ironhorsedevgroup.mods.gunsmoke.registry.GunsmokeItems;
import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.registries.ForgeRegistries;
import slimeknights.mantle.recipe.helper.ItemOutput;
import slimeknights.tconstruct.library.recipe.casting.ItemCastingRecipeBuilder;

import java.io.*;
import java.util.Objects;
import java.util.function.Consumer;

public class RecipeGenerator extends RecipeProvider implements IConditionBuilder {
    public RecipeGenerator(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        /*
        castRecipe(GunsmokeItems.CAST_GUN_PARTS.get(), GunsmokeItems.GUN_PARTS.get(), consumer);
        castRecipe(GunsmokeItems.CAST_STOCK.get(), GunsmokeItems.STOCK.get(), consumer);
        castRecipe(GunsmokeItems.CAST_BARREL_SHORT.get(), GunsmokeItems.BARREL_SHORT.get(), consumer);
        castRecipe(GunsmokeItems.CAST_BARREL_MEDIUM.get(), GunsmokeItems.BARREL_MEDIUM.get(), consumer);
        castRecipe(GunsmokeItems.CAST_BARREL_LONG.get(), GunsmokeItems.BARREL_LONG.get(), consumer);
        for (GunsmokeMaterials object : GunsmokeMaterials.values()) {
            GunMaterial material = object.getMaterial();
            if (material != GunsmokeMaterials.NULL.getMaterial()) {
                // Casting recipes
                if (material.isCastable() && material.getCastingFluid() != null) {
                    castingRecipe(GunsmokeItems.CAST_BARREL_SHORT.get(), material, 90 * 2, 40 * 2, addMaterial(GunsmokeItems.BARREL_SHORT.get(), material), consumer);
                    castingRecipe(GunsmokeItems.CAST_BARREL_MEDIUM.get(), material, 90 * 3, 40 * 3, addMaterial(GunsmokeItems.BARREL_MEDIUM.get(), material), consumer);
                    castingRecipe(GunsmokeItems.CAST_BARREL_LONG.get(), material, 90 * 4, 40 * 4, addMaterial(GunsmokeItems.BARREL_LONG.get(), material), consumer);
                    castingRecipe(GunsmokeItems.CAST_STOCK.get(), material, 90 * 3, 40 * 3, addMaterial(GunsmokeItems.STOCK.get(), material), consumer);
                    castingRecipe(GunsmokeItems.CAST_GUN_PARTS.get(), material, 100 * 2, 40 * 2, addMaterial(GunsmokeItems.GUN_PARTS.get(), material), consumer);
                }
                // Upgrade recipes
                try {
                    upgradeRecipe(GunsmokeItems.STOCK_ADVANCED.get(), GunsmokeItems.STOCK.get(), material, consumer);
                    upgradeRecipe(GunsmokeItems.CHAMBER_PARTS.get(), GunsmokeItems.BARREL_SHORT.get(), material, consumer);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                // Vanilla recipes
                vanillaRecipes(material, consumer);
            }
        }
         */
    }

    /*
    public ItemStack addMaterial(Item item, GunMaterial material) {
        ItemStack retStack = new ItemStack(item);
        return NBT.putStringTag(retStack, "material", material.getName());
    }

    public void castRecipe(ItemLike cast, ItemLike item, Consumer<FinishedRecipe> consumer) {
        ItemCastingRecipeBuilder
                .tableRecipe(cast)
                .setFluid(Objects.requireNonNull(ForgeRegistries.FLUIDS.getValue(new ResourceLocation("tconstruct:molten_gold"))), 90)
                .setCast(item, true)
                .setCoolingTime(40)
                .save(consumer);
    }

    public void castingRecipe(ItemLike cast, GunMaterial material, Integer amount, Integer coolTime, ItemStack result, Consumer<FinishedRecipe> consumer) {
        if (material.getCastingFluid() != null) {
            ItemCastingRecipeBuilder
                    .tableRecipe(ItemOutput.fromStack(result))
                    .setFluid(material.getCastingFluid(), amount)
                    .setCast(cast, false)
                    .setCoolingTime(coolTime)
                    .save(consumer, new ResourceLocation("gunsmoke", "casting/" + result.getDescriptionId()));
        }
    }

    public void upgradeRecipe(ItemLike result, ItemLike baseItem, GunMaterial material, Consumer<FinishedRecipe> consumer) throws IOException {
        ResourceLocation location = new ResourceLocation("gunsmoke", "crafting/" + addMaterial(result.asItem(), material).getDescriptionId());
        new GunPartUpgradeRecipeBuilder(result.asItem(), Ingredient.of(baseItem), material)
                .unlockedBy("haveiron", has(Items.IRON_INGOT))
                .save(consumer, location);
    }

    public void vanillaRecipes(GunMaterial material, Consumer<FinishedRecipe> consumer) {
        if (material.getCraftingItemID() != null) {
            VanillaShapedRecipeBuilder partsRecipe = new VanillaShapedRecipeBuilder(GunsmokeItems.GUN_PARTS.get(), material)
                    .pattern("XY")
                    .pattern("YX")
                    .define('Y', Items.IRON_NUGGET)
                    .unlockedBy("haveiron", has(Items.IRON_INGOT));

            VanillaShapedRecipeBuilder stockRecipe = new VanillaShapedRecipeBuilder(GunsmokeItems.STOCK.get(), material)
                    .pattern("XX")
                    .pattern(" X")
                    .unlockedBy("haveiron", has(Items.IRON_INGOT));

            VanillaShapedRecipeBuilder gripRecipe = new VanillaShapedRecipeBuilder(GunsmokeItems.GRIP.get(), material)
                    .pattern(" X")
                    .pattern("X ")
                    .unlockedBy("haveiron", has(Items.IRON_INGOT));

            VanillaShapedRecipeBuilder shortBarrelRecipe = new VanillaShapedRecipeBuilder(GunsmokeItems.BARREL_SHORT.get(), material)
                    .pattern("X ")
                    .pattern(" X")
                    .unlockedBy("haveiron", has(Items.IRON_INGOT));

            VanillaShapedRecipeBuilder mediumBarrelRecipe = new VanillaShapedRecipeBuilder(GunsmokeItems.BARREL_MEDIUM.get(), material)
                    .pattern("X  ")
                    .pattern(" X ")
                    .pattern("  X")
                    .unlockedBy("haveiron", has(Items.IRON_INGOT));

            VanillaShapedRecipeBuilder longBarrelRecipe = new VanillaShapedRecipeBuilder(GunsmokeItems.BARREL_LONG.get(), material)
                    .pattern("Y ")
                    .pattern(" Y")
                    .define('Y', GunsmokeItems.BARREL_SHORT.get())
                    .requireMaterial()
                    .unlockedBy("haveiron", has(Items.IRON_INGOT));

            if (material.isCastable()) {
                partsRecipe.addMod("tconstruct");
                stockRecipe.addMod("tconstruct");
                shortBarrelRecipe.addMod("tconstruct");
                mediumBarrelRecipe.addMod("tconstruct");
                longBarrelRecipe.addMod("tconstruct");
            }

            partsRecipe.save(consumer, new ResourceLocation("gunsmoke", "crafting/defaults/" + addMaterial(GunsmokeItems.GUN_PARTS.get(), material).getDescriptionId()));
            stockRecipe.save(consumer, new ResourceLocation("gunsmoke", "crafting/defaults/" + addMaterial(GunsmokeItems.STOCK.get(), material).getDescriptionId()));
            gripRecipe.save(consumer, new ResourceLocation("gunsmoke", "crafting/defaults/" + addMaterial(GunsmokeItems.GRIP.get(), material).getDescriptionId()));
            shortBarrelRecipe.save(consumer, new ResourceLocation("gunsmoke", "crafting/defaults/" + addMaterial(GunsmokeItems.BARREL_SHORT.get(), material).getDescriptionId()));
            mediumBarrelRecipe.save(consumer, new ResourceLocation("gunsmoke", "crafting/defaults/" + addMaterial(GunsmokeItems.BARREL_MEDIUM.get(), material).getDescriptionId()));
            longBarrelRecipe.save(consumer, new ResourceLocation("gunsmoke", "crafting/defaults/" + addMaterial(GunsmokeItems.BARREL_LONG.get(), material).getDescriptionId()));
        }
    }
     */
}