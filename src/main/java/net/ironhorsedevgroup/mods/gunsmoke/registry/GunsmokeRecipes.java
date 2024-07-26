package net.ironhorsedevgroup.mods.gunsmoke.registry;

import net.ironhorsedevgroup.mods.gunsmoke.Gunsmoke;
import net.ironhorsedevgroup.mods.gunsmoke.recipes.GunBenchRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class GunsmokeRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Gunsmoke.MODID);

    public static final RegistryObject<RecipeSerializer<GunBenchRecipe>> GUN_RECIPE_SERIALIZER = SERIALIZERS.register("simple_gun_smithing", () -> GunBenchRecipe.Serializer.INSTANCE);
}
