package net.ironhorsedevgroup.mods.gunsmoke.data;

import com.google.gson.*;
import com.mojang.authlib.minecraft.client.ObjectMapper;
import net.ironhorsedevgroup.mods.gunsmoke.item.guns.GunMaterial;
import net.ironhorsedevgroup.mods.gunsmoke.item.guns.GunMaterials;
import net.ironhorsedevgroup.mods.gunsmoke.registry.GunsmokeItems;
import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.common.data.ForgeRecipeProvider;
import slimeknights.mantle.recipe.helper.ItemOutput;
import slimeknights.tconstruct.library.recipe.casting.ItemCastingRecipeBuilder;
import slimeknights.tconstruct.library.recipe.partbuilder.ItemPartRecipeBuilder;

import javax.json.Json;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

public class RecipeGenerator extends RecipeProvider implements IConditionBuilder {
    public RecipeGenerator(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        for (GunMaterials object : GunMaterials.values()) {
            GunMaterial material = object.getMaterial();
            if (material.isCastable() && material.getCastingFluid() != null) {
                castRecipe(GunsmokeItems.CAST_BARREL_SHORT.get(), material, 90 * 2, 20, addMaterial(GunsmokeItems.BARREL_SHORT.get(), material), consumer);
                castRecipe(GunsmokeItems.CAST_BARREL_MEDIUM.get(), material, 90 * 3, 20, addMaterial(GunsmokeItems.BARREL_MEDIUM.get(), material), consumer);
                castRecipe(GunsmokeItems.CAST_BARREL_LONG.get(), material, 90 * 4, 20, addMaterial(GunsmokeItems.BARREL_LONG.get(), material), consumer);
                castRecipe(GunsmokeItems.CAST_STOCK.get(), material, 90 * 3, 20, addMaterial(GunsmokeItems.STOCK.get(), material), consumer);
                castRecipe(GunsmokeItems.CAST_GUN_PARTS.get(), material, 90 * 2, 20, addMaterial(GunsmokeItems.GUN_PARTS.get(), material), consumer);
            }
            try {
                upgradeRecipe(GunsmokeItems.STOCK_ADVANCED.get(), GunsmokeItems.STOCK.get(), material, consumer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public ItemStack addMaterial(Item item, GunMaterial material) {
        ItemStack retStack = new ItemStack(item);
        return NBT.putStringTag(retStack, "material", material.getName());
    }

    public void castRecipe(ItemLike cast, GunMaterial material, Integer amount, Integer coolTime, ItemStack result, Consumer<FinishedRecipe> consumer) {
        if (material.getCastingFluid() != null) {
            ItemCastingRecipeBuilder
                    .tableRecipe(ItemOutput.fromStack(result))
                    .setFluid(material.getCastingFluid(), amount)
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

    public void upgradeRecipe(ItemLike result, ItemLike baseItem, GunMaterial material, Consumer<FinishedRecipe> consumer) throws IOException {
        if (material != GunMaterials.NULL.getMaterial()) {
            ResourceLocation location = new ResourceLocation("gunsmoke", "crafting/" + addMaterial(result.asItem(), material).getDescriptionId());
            new GunPartUpgradeRecipeBuilder(result.asItem(), Ingredient.of(baseItem), material)
                    .unlockedBy("haveiron", has(Items.IRON_INGOT))
                    .save(consumer, location);
        }
    }
}