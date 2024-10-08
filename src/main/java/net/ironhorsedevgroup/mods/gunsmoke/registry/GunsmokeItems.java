package net.ironhorsedevgroup.mods.gunsmoke.registry;

import com.mrcrayfish.guns.item.GunItem;
import net.ironhorsedevgroup.mods.gunsmoke.Gunsmoke;
import net.ironhorsedevgroup.mods.gunsmoke.item.GunPartItem;
import net.ironhorsedevgroup.mods.gunsmoke.item.RifleItem;
import net.ironhorsedevgroup.mods.gunsmoke.item.RoundItem;
import net.ironhorsedevgroup.mods.gunsmoke.item.rounds.CaliberProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;

public class GunsmokeItems {
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, Gunsmoke.MODID);

    public static final RegistryObject<Item> LIFTER_1873 = REGISTRY.register("lifter1873", () -> new RifleItem(new Item.Properties().stacksTo(1).tab(GunsmokeTabs.FIREARMS), 10));
    public static final RegistryObject<Item> SHARPS_1874 = REGISTRY.register("sharps1874", () -> new RifleItem(new Item.Properties().stacksTo(1).tab(GunsmokeTabs.FIREARMS), 10));
    public static final RegistryObject<Item> WINCHESTER_1895 = REGISTRY.register("winchester1895", () -> new RifleItem(new Item.Properties().stacksTo(1).tab(GunsmokeTabs.FIREARMS), 2));
    public static final RegistryObject<Item> DRAGOON = REGISTRY.register("dragoon", () -> new RifleItem(new Item.Properties().stacksTo(1).tab(GunsmokeTabs.FIREARMS), 10));

    public static final RegistryObject<Item> GUN_BENCH = block(GunsmokeBlocks.GUN_BENCH, GunsmokeTabs.FIREARMS);

    public static final RegistryObject<Item> BARREL_SHORT = REGISTRY.register("barrel_short", () -> new GunPartItem(new Item.Properties().tab(GunsmokeTabs.PARTS)));
    public static final RegistryObject<Item> BARREL_MEDIUM = REGISTRY.register("barrel_medium", () -> new GunPartItem(new Item.Properties().tab(GunsmokeTabs.PARTS)));
    public static final RegistryObject<Item> BARREL_LONG = REGISTRY.register("barrel_long", () -> new GunPartItem(new Item.Properties().tab(GunsmokeTabs.PARTS)));
    public static final RegistryObject<Item> BARREL_DOUBLE_SHORT = REGISTRY.register("barrel_double_short", () -> new GunPartItem(new Item.Properties().tab(GunsmokeTabs.PARTS)));
    public static final RegistryObject<Item> BARREL_DOUBLE_LONG = REGISTRY.register("barrel_double_long", () -> new GunPartItem(new Item.Properties().tab(GunsmokeTabs.PARTS)));
    public static final RegistryObject<Item> STOCK = REGISTRY.register("stock", () -> new GunPartItem(new Item.Properties().tab(GunsmokeTabs.PARTS)));
    public static final RegistryObject<Item> STOCK_ADVANCED = REGISTRY.register("stock_advanced", () -> new GunPartItem(new Item.Properties().tab(GunsmokeTabs.PARTS)));
    public static final RegistryObject<Item> GRIP = REGISTRY.register("grip", () -> new GunPartItem(new Item.Properties().tab(GunsmokeTabs.PARTS)));
    public static final RegistryObject<Item> GUN_PARTS = REGISTRY.register("gun_parts", () -> new GunPartItem(new Item.Properties().tab(GunsmokeTabs.PARTS)));
    public static final RegistryObject<Item> CHAMBER_PARTS = REGISTRY.register("chamber_parts", () -> new GunPartItem(new Item.Properties().tab(GunsmokeTabs.PARTS)));
    public static final RegistryObject<Item> CHAMBER_DOUBLE_PARTS = REGISTRY.register("chamber_double_parts", () -> new GunPartItem(new Item.Properties().tab(GunsmokeTabs.PARTS)));
    public static final RegistryObject<Item> CYLINDER_PARTS = REGISTRY.register("cylinder_parts", () -> new GunPartItem(new Item.Properties().tab(GunsmokeTabs.PARTS)));

    public static final Map<String, RegistryObject<Item>> CALIBERS = registerCalibers(REGISTRY);

    public static final DeferredRegister<Item> TCONSTRUCT = DeferredRegister.create(ForgeRegistries.ITEMS, Gunsmoke.MODID);

    public static final RegistryObject<Item> CAST_BARREL_SHORT = TCONSTRUCT.register("cast_barrel_short", () -> new Item(new Item.Properties().tab(GunsmokeTabs.PARTS)));
    public static final RegistryObject<Item> CAST_BARREL_MEDIUM = TCONSTRUCT.register("cast_barrel_medium", () -> new Item(new Item.Properties().tab(GunsmokeTabs.PARTS)));
    public static final RegistryObject<Item> CAST_BARREL_LONG = TCONSTRUCT.register("cast_barrel_long", () -> new Item(new Item.Properties().tab(GunsmokeTabs.PARTS)));
    public static final RegistryObject<Item> CAST_STOCK = TCONSTRUCT.register("cast_stock", () -> new Item(new Item.Properties().tab(GunsmokeTabs.PARTS)));
    public static final RegistryObject<Item> CAST_GUN_PARTS = TCONSTRUCT.register("cast_gun_parts", () -> new Item(new Item.Properties().tab(GunsmokeTabs.PARTS)));
    public static final RegistryObject<Item> CAST_CYLINDER_PARTS = TCONSTRUCT.register("cast_cylinder_parts", () -> new Item(new Item.Properties().tab(GunsmokeTabs.PARTS)));

    private static Map<String, RegistryObject<Item>> registerCalibers(DeferredRegister<Item> registry) {
        Map<String, RegistryObject<Item>> registryObjects = new HashMap<>();
        for (GunsmokeCalibers round : GunsmokeCalibers.values()) {
            CaliberProperties caliber = round.getCaliber();
            registryObjects.put(round.getSerializedName(), registry.register(round.getSerializedName(), () -> new RoundItem(new Item.Properties().tab(GunsmokeTabs.FIREARMS), caliber)));
        }
        return registryObjects;
    }

    private static RegistryObject<Item> block(RegistryObject<Block> block, CreativeModeTab tab) {
        return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }

}
