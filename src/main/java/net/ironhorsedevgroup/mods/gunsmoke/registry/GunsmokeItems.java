package net.ironhorsedevgroup.mods.gunsmoke.registry;

import net.ironhorsedevgroup.mods.gunsmoke.Gunsmoke;
import net.ironhorsedevgroup.mods.gunsmoke.item.GunItem;
import net.ironhorsedevgroup.mods.gunsmoke.item.PartItem;
import net.ironhorsedevgroup.mods.gunsmoke.item.RoundItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class GunsmokeItems {
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, Gunsmoke.MODID);

    public static final RegistryObject<Item> GUN_BENCH = block(GunsmokeBlocks.GUN_BENCH, CreativeModeTab.TAB_DECORATIONS);

    public static final RegistryObject<Item> GUN_ITEM = REGISTRY.register("gun_item", () -> new GunItem(new Item.Properties().tab(GunsmokeTabs.FIREARMS)));

    public static final RegistryObject<Item> ROUND_ITEM = REGISTRY.register("round_item", () -> new RoundItem(new Item.Properties().tab(GunsmokeTabs.ROUNDS)));

    public static final RegistryObject<Item> PART_ITEM = REGISTRY.register("part_item", () -> new PartItem(new Item.Properties().tab(GunsmokeTabs.PARTS)));

    public static final DeferredRegister<Item> TCONSTRUCT = DeferredRegister.create(ForgeRegistries.ITEMS, Gunsmoke.MODID);

    public static final RegistryObject<Item> CAST_BARREL_SHORT = TCONSTRUCT.register("cast_barrel_short", () -> new Item(new Item.Properties().tab(GunsmokeTabs.PARTS)));
    public static final RegistryObject<Item> CAST_BARREL_MEDIUM = TCONSTRUCT.register("cast_barrel_medium", () -> new Item(new Item.Properties().tab(GunsmokeTabs.PARTS)));
    public static final RegistryObject<Item> CAST_BARREL_LONG = TCONSTRUCT.register("cast_barrel_long", () -> new Item(new Item.Properties().tab(GunsmokeTabs.PARTS)));
    public static final RegistryObject<Item> CAST_STOCK = TCONSTRUCT.register("cast_stock", () -> new Item(new Item.Properties().tab(GunsmokeTabs.PARTS)));
    public static final RegistryObject<Item> CAST_GUN_PARTS = TCONSTRUCT.register("cast_gun_parts", () -> new Item(new Item.Properties().tab(GunsmokeTabs.PARTS)));

    private static RegistryObject<Item> block(RegistryObject<Block> block, CreativeModeTab tab) {
        return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }

}
