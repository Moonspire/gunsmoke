package net.ironhorsedevgroup.mods.gunsmoke.registry;

import net.ironhorsedevgroup.mods.gunsmoke.Gunsmoke;
import net.ironhorsedevgroup.mods.gunsmoke.item.GunPartItem;
import net.ironhorsedevgroup.mods.gunsmoke.item.RifleItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class GunsmokeItems {
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, Gunsmoke.MODID);

    public static final RegistryObject<Item> SHARPS_1874 = REGISTRY.register("sharps1874", () -> new RifleItem(new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_COMBAT), 10));
    public static final RegistryObject<Item> WINCHESTER_1895 = REGISTRY.register("winchester1895", () -> new RifleItem(new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_COMBAT), 2));
    public static final RegistryObject<Item> BARREL_SHORT = REGISTRY.register("barrelshort", () -> new GunPartItem(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
    public static final RegistryObject<Item> BARREL_MEDIUM = REGISTRY.register("barrelmedium", () -> new GunPartItem(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
    public static final RegistryObject<Item> BARREL_LONG = REGISTRY.register("barrellong", () -> new GunPartItem(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
    public static final RegistryObject<Item> STOCK = REGISTRY.register("stock", () -> new GunPartItem(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
    public static final RegistryObject<Item> STOCK_ADVANCED = REGISTRY.register("stockadvanced", () -> new GunPartItem(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));

    public static final DeferredRegister<Item> TCONSTRUCT = DeferredRegister.create(ForgeRegistries.ITEMS, Gunsmoke.MODID);

    public static final RegistryObject<Item> CAST_BARREL_SHORT = TCONSTRUCT.register("castbarrelshort", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
    public static final RegistryObject<Item> CAST_BARREL_MEDIUM = TCONSTRUCT.register("castbarrelmedium", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
    public static final RegistryObject<Item> CAST_BARREL_LONG = TCONSTRUCT.register("castbarrellong", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
    public static final RegistryObject<Item> CAST_STOCK = TCONSTRUCT.register("caststock", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
    // public static final RegistryObject<Item> CAST_STOCK_ADVANCED = TCONSTRUCT.register("caststockadvanced", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));

    private static RegistryObject<Item> block(DeferredRegister<Item> registry, RegistryObject<Block> block, CreativeModeTab tab) {
        return registry.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }
    private static RegistryObject<Item> block(DeferredRegister<Item> registry, RegistryObject<Block> block, CreativeModeTab tab, String id) {
        return registry.register(id, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }

}
