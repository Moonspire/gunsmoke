package net.ironhorsedevgroup.mods.gunsmoke.registry;

import net.ironhorsedevgroup.mods.gunsmoke.item.guns.GunMaterial;
import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;

public enum GunsmokeMaterials implements StringRepresentable {
    NULL(new GunMaterial("null")),

    //Overworld Woods
    ACACIA(
            new GunMaterial("acacia")
                    .setColor(219, 117, 62)
                    .setFlamable(true)
                    .setDensity(10)
                    .setHardness(10)
                    .setPurity(10)
                    .setCraftingItem("minecraft:acacia_planks")
    ),
    BIRCH(
            new GunMaterial("birch")
                    .setColor(233, 218, 139)
                    .setFlamable(true)
                    .setDensity(8)
                    .setHardness(8)
                    .setPurity(5)
                    .setCraftingItem("minecraft:birch_planks")
    ),
    DARK_OAK(
            new GunMaterial("dark_oak")
                    .setColor(99, 68, 34)
                    .setFlamable(true)
                    .setDensity(9)
                    .setHardness(8)
                    .setPurity(5)
                    .setCraftingItem("minecraft:dark_oak_planks")
    ),
    JUNGLE(
            new GunMaterial("jungle")
                    .setColor(218, 155, 111)
                    .setFlamable(true)
                    .setDensity(3)
                    .setHardness(5)
                    .setPurity(5)
                    .setCraftingItem("minecraft:jungle_planks")
    ),
    MANGROVE(
            new GunMaterial("mangrove")
                    .setColor(173, 69, 76)
                    .setFlamable(true)
                    .setDensity(5)
                    .setHardness(7)
                    .setPurity(5)
                    .setCraftingItem("minecraft:mangrove_planks")
    ),
    OAK(
            new GunMaterial("oak")
                    .setColor(224, 175, 96)
                    .setFlamable(true)
                    .setDensity(9)
                    .setHardness(8)
                    .setPurity(5)
                    .setCraftingItem("minecraft:oak_planks")
    ),
    SPRUCE(
            new GunMaterial("spruce")
                    .setColor(142, 102, 51)
                    .setFlamable(true)
                    .setDensity(6)
                    .setHardness(7)
                    .setPurity(5)
                    .setCraftingItem("minecraft:spruce_planks")
    ),

    //Nether Woods
    CRIMSON(
            new GunMaterial("crimson")
                    .setColor(164, 69, 106)
                    .setDensity(13)
                    .setHardness(9)
                    .setPurity(5)
                    .setCraftingItem("minecraft:crimson_planks")
    ),
    WARPED(
            new GunMaterial("warped")
                    .setColor(65, 167, 165)
                    .setDensity(4)
                    .setHardness(6)
                    .setPurity(5)
                    .setCraftingItem("minecraft:warped_planks")
    ),

    //Quark Woods
    ANCIENT(
            new GunMaterial("ancient")
                    .setColor(255, 255, 255)
                    .setFlamable(true)
                    .setDensity(6)
                    .setHardness(7)
                    .setPurity(5)
                    .setCraftingItem("quark:ancient_planks")
    ),
    AZALEA(
            new GunMaterial("azalea")
                    .setColor(221, 227, 122)
                    .setFlamable(true)
                    .setDensity(9)
                    .setHardness(8)
                    .setPurity(5)
                    .setCraftingItem("quark:azalea_planks")
    ),
    BLOSSOM(
            new GunMaterial("blossom")
                    .setColor(131, 55, 35)
                    .setFlamable(true)
                    .setDensity(9)
                    .setHardness(8)
                    .setPurity(5)
                    .setCraftingItem("quark:blossom_planks")
    ),

    //IE Woods
    TREATED(
            new GunMaterial("treated")
                    .setColor(117, 63, 38)
                    .setDensity(10)
                    .setHardness(7)
                    .setPurity(6)
                    .setCraftingItem("immersiveengineering:treated_wood_horizontal")
    ),

    //Undergarden Woods
    GRONGLE(
            new GunMaterial("grongle")
                    .setColor(108, 120, 67)
                    .setFlamable(true)
                    .setDensity(10)
                    .setHardness(10)
                    .setPurity(5)
                    .setCraftingItem("undergarden:grongle_planks")
    ),
    SMOGSTEM(
            new GunMaterial("smogstem")
                    .setColor(93, 121, 114)
                    .setFlamable(true)
                    .setDensity(15)
                    .setHardness(15)
                    .setPurity(5)
                    .setCraftingItem("undergarden:smogstem_planks")
    ),
    WIGGLEWOOD(
            new GunMaterial("wigglewood")
                    .setColor(126, 95, 59)
                    .setFlamable(true)
                    .setDensity(9)
                    .setHardness(10)
                    .setPurity(5)
                    .setCraftingItem("undergarden:wigglewood_planks")
    ),

    //Vanilla Metals
    IRON(
            new GunMaterial("iron")
                    .setColor(195, 195, 195)
                    .setDensity(79)
                    .setHardness(45)
                    .setPurity(65)
                    .setCastingFluid("tconstruct:molten_iron")
    ),
    RAW_IRON(
            new GunMaterial("raw_iron")
                    .setColor(196, 159, 134)
                    .setDensity(78)
                    .setHardness(65)
                    .setPurity(40)
                    .setCastingFluid("tconstruct:molten_iron")
                    .setCraftingItem("minecraft:iron_ingot")
    ),
    WROUGHT_IRON(
            new GunMaterial("wrought_iron")
                    .setColor(80, 89, 115)
                    .setDensity(78)
                    .setHardness(70)
                    .setPurity(60)
                    .setCastingFluid("tconstruct:molten_iron")
                    .setCastable(true)
    ),
    COPPER(
            new GunMaterial("copper")
                    .setColor(255, 154, 118)
                    .setDensity(88)
                    .setHardness(30)
                    .setPurity(80)
                    .setCastingFluid("tconstruct:molten_copper")
                    .setCastable(true)
    ),
    OXIDIZED_COPPER(
            new GunMaterial("oxidized_copper")
                    .setColor(110, 197, 159)
                    .setDensity(87)
                    .setHardness(35)
                    .setPurity(65)
                    .setCastingFluid("tconstruct:molten_copper")
                    .setCraftingItem("minecraft:copper_ingot")
    ),
    GOLD(
            new GunMaterial("gold")
                    .setColor(253, 245, 95)
                    .setDensity(193)
                    .setHardness(25)
                    .setPurity(100)
                    .setCastingFluid("tconstruct:molten_gold")
                    .setCastable(true)
                    .setCraftingItem("minecraft:gold_ingot")
    ),

    //IE Metals
    LEAD(
            new GunMaterial("lead")
                    .setColor(68, 75, 91)
                    .setDensity(114)
                    .setHardness(25)
                    .setPurity(50)
                    .setCastingFluid("tconstruct:molten_lead")
                    .setCastable(true)
                    .setCraftingItem("immersiveengineeing:ingot_lead")
    ),
    SILVER(
            new GunMaterial("silver")
                    .setColor(200, 229, 255)
                    .setDensity(104)
                    .setHardness(35)
                    .setPurity(85)
                    .setCastingFluid("tconstruct:molten_silver")
                    .setCastable(true)
                    .setCraftingItem("immersiveengineeing:ingot_silver")
    ),
    STEEL(
            new GunMaterial("steel")
                    .setColor(88, 89, 93)
                    .setDensity(77)
                    .setHardness(80)
                    .setPurity(70)
                    .setCastingFluid("tconstruct:molten_steel")
                    .setCastable(true)
                    .setCraftingItem("immersiveengineeing:ingot_steel")
    ),

    //Create Metals
    BRASS(
            GunMaterial.TinkersCastable(
                            "brass",
                            255, 203, 92
                    )
                    .setDensity(82)
                    .setHardness(30)
                    .setPurity(90)
                    .setCraftingItem("create:brass_ingot")
    ),
    NETHERSTEEL (
        new GunMaterial("nethersteel")
                .setColor(117, 99, 91)
                .setDensity(80)
                .setHardness(95)
                .setPurity(100)
                .setCraftingItem("createbigcannons:nethersteel_ingot")
    ),

    //Tinkers Metals & Alloys
    COBALT(
            GunMaterial.TinkersCastable(
                    "cobalt",
                    66, 141, 215
            )
    ),
    ROSE_GOLD(
            GunMaterial.TinkersCastable(
                            "rose_gold",
                            241, 172, 147
                    )
                    .setDensity(123)
                    .setHardness(30)
                    .setPurity(90)
    ),
    CONSTANTAN(
            GunMaterial.TinkersCastable(
                    "constantan",
                    178, 99, 80
            )
    ),
    ELECTRUM(
            GunMaterial.TinkersCastable(
                            "electrum",
                            237, 223, 129
                    )
                    .setDensity(88)
                    .setHardness(35)
                    .setPurity(90)
    ),
    HEPATIZON( //BROKEN
            GunMaterial.TinkersCastable(
                    "hepatizon",
                    113, 89, 125
            )
    ),
    MANYULLYN( //BROKEN
            GunMaterial.TinkersCastable(
                    "manyullyn",
                    153, 102, 217
            )
    ),

    //Pigsteel & Hogsgold
    PIGSTEEL(
            new GunMaterial("pigsteel")
                    .setColor(224, 180, 221)
                    .setDensity(79)
                    .setHardness(70)
                    .setPurity(75)
                    .setCraftingItem("pigsteel:pigsteel_ingot")
    ),
    ZOMBIFIED_PIGSTEEL(
            new GunMaterial("zombified_pigsteel")
                    .setColor(109, 142, 106)
                    .setDensity(78)
                    .setHardness(60)
                    .setPurity(40)
    ),
    HOGSGOLD(
            new GunMaterial("hogsgold")
                    .setColor(230, 171, 83)
                    .setDensity(89)
                    .setHardness(25)
                    .setPurity(100)
    ),

    //Undergarden Metals
    CLOGGRUM(
            new GunMaterial("cloggrum")
                    .setColor(215, 188, 155)
                    .setDensity(79)
                    .setHardness(90)
                    .setPurity(30)
                    .setCraftingItem("undergarden:cloggrum_ingot")
    ),

    //Oddballs
    SHULKER(
            new GunMaterial("shulker")
                    .setColor(182, 128, 180)
                    .setDensity(-12)
                    .setHardness(15)
                    .setPurity(1)
                    .setCraftingItem("minecraft:shulker_shell")
    )
    ;

    private final GunMaterial material;

    private GunsmokeMaterials(GunMaterial Material) {
        material = Material;
    }

    @Override
    public String getSerializedName() {
        return material.getName();
    }

    public GunMaterial getMaterial() {
        return material;
    }

    public static GunMaterial getMaterial(String name) {
        for (GunsmokeMaterials material : GunsmokeMaterials.values()) {
            if (material.getMaterial().isMaterial(name)) {
                return material.getMaterial();
            }
        }
        return GunsmokeMaterials.NULL.getMaterial();
    }

    public static GunMaterial getMaterial(ItemStack stack) {
        return getMaterial(NBT.getStringTag(stack, "material"));
    }
}