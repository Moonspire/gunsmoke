package net.ironhorsedevgroup.mods.gunsmoke.registry;

import net.ironhorsedevgroup.mods.gunsmoke.Gunsmoke;
import net.ironhorsedevgroup.mods.gunsmoke.item.GunPartItem;
import net.ironhorsedevgroup.mods.gunsmoke.item.RifleItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class GunsmokeItems {
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, Gunsmoke.MODID);
    private static RegistryObject<Item> block(RegistryObject<Block> block, CreativeModeTab tab) {
        return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }
    private static RegistryObject<Item> block(RegistryObject<Block> block, CreativeModeTab tab, String id) {
        return REGISTRY.register(id, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }

    public static final RegistryObject<Item> SHARPS_1874 = REGISTRY.register("sharps1874", () -> new RifleItem(new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_COMBAT), 10));
    public static final RegistryObject<Item> BARREL_SHORT = REGISTRY.register("barrelshort", () -> new GunPartItem(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
    public static final RegistryObject<Item> BARREL_MEDIUM = REGISTRY.register("barrelmedium", () -> new GunPartItem(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
    public static final RegistryObject<Item> BARREL_LONG = REGISTRY.register("barrellong", () -> new GunPartItem(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
}
