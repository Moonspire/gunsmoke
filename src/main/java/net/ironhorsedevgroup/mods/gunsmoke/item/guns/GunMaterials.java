package net.ironhorsedevgroup.mods.gunsmoke.item.guns;

import net.ironhorsedevgroup.mods.toolshed.tools.Color;
import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public enum GunMaterials implements StringRepresentable {
    NULL("null", Color.getIntFromRGB(255, 0, 255), false, 0, 0, 0),

    //Overworld Woods
    ACACIA("acacia", Color.getIntFromRGB(219, 117, 62), true, 10, 10, 10),
    BIRCH("birch", Color.getIntFromRGB(233, 218, 139), true, 8, 8, 5),
    DARK_OAK("dark_oak", Color.getIntFromRGB(99, 68, 34), true, 9, 8, 5),
    JUNGLE("jungle", Color.getIntFromRGB(218, 155, 111), true, 3, 5, 5),
    MANGROVE("mangrove", Color.getIntFromRGB(173, 69, 76), true, 5, 7, 5),
    OAK("oak", Color.getIntFromRGB(224, 175, 96), true, 9, 8, 5),
    SPRUCE("spruce", Color.getIntFromRGB(142, 102, 51), true, 6, 7, 5),

    //Modded Woods
    ANCIENT("ancient", Color.getIntFromRGB(255, 255, 255), true, 6, 7, 5),
    AZALEA("azalea", Color.getIntFromRGB(221, 227, 122), true, 9, 8, 5),
    BLOSSOM("blossom", Color.getIntFromRGB(131, 55, 35), true, 9, 8, 5),
    TREATED("treated", Color.getIntFromRGB(117, 63, 38), 10, 7, 6),

    //Undergarden Woods
    GRONGLE("grongle", Color.getIntFromRGB(108, 120, 67), true, 10, 10, 5),
    SMOGSTEM("smogstem", Color.getIntFromRGB(93, 121, 114), true, 15, 15, 5),
    WIGGLEWOOD("wigglewood", Color.getIntFromRGB(126, 95, 59), true, 9, 10, 5),

    //Nether Woods
    CRIMSON("crimson", Color.getIntFromRGB(164, 69, 106), 13, 9, 5),
    WARPED("warped", Color.getIntFromRGB(65, 167, 165), 4, 6, 5),

    //Metals
    IRON("iron", Color.getIntFromRGB(195, 195, 195),79, 45, 65),
    RAW_IRON("raw_iron", Color.getIntFromRGB(196, 159, 134), 78, 65, 40),
    WROUGHT_IRON("wrought_iron", Color.getIntFromRGB(80, 89, 115), 78, 70, 60),
    COPPER("copper", Color.getIntFromRGB(255, 154, 118), 88, 30, 80),
    OXIDIZED_COPPER("oxidized_copper", Color.getIntFromRGB(110, 197, 159), 87, 35, 55),
    LEAD("lead", Color.getIntFromRGB(68, 75, 91), 114, 25, 50),
    SILVER("silver", Color.getIntFromRGB(200, 229, 255), 104, 35, 85),
    ROSE_GOLD("rose_gold", Color.getIntFromRGB(241, 172, 147), 123, 30, 80),
    STEEL("steel", Color.getIntFromRGB(88, 89, 93), 77, 80, 70),
    BRASS("brass", Color.getIntFromRGB(255, 203, 92), 82, 30, 90),
    HOGSGOLD("hogsgold", Color.getIntFromRGB(230, 171, 83), 89, 25, 100),
    PIGSTEEL("pigsteel", Color.getIntFromRGB(224, 180, 221), 79, 70, 75),
    ZOMBIFIED_PIGSTEEL("zombified_pigsteel", Color.getIntFromRGB(109, 142, 106), 78, 60, 40),
    CONSTANTAN("constantan", Color.getIntFromRGB(178, 99, 80)), //, 88, 30),
    ELECTRUM("electrum", Color.getIntFromRGB(237, 223, 129), 88, 35, 90),
    COBALT("cobalt", Color.getIntFromRGB(66, 141, 215)), // 89, 40),
    HEPTAZION("heptazion", Color.getIntFromRGB(113, 89, 125)),
    MANYULLYN("manyullyn", Color.getIntFromRGB(153, 102, 217)),
    CLOGGRUM("cloggrum", Color.getIntFromRGB(215, 188, 155), 79, 90, 30),
    NETHERSTEEL("nethersteel", Color.getIntFromRGB(117, 99, 91),80, 95, 100),

    //Oddballs
    SHULKER("shulker", Color.getIntFromRGB(182, 128, 180),-12, 15, 1);

    /*
    - Reliability
     + Barrel: Hardness
     + Breach: Density
     + Core: Purity
     + Furniture: Flammability

    - Recoil calculated on the density distribution (more balanced guns have less recoil)
    - Handling calculated off total weapon density
    - Fire delay calculated off the inverse of the core hardness
    - Reload delay calculated off breach purity
    - Bullet spread calculated off Barrel purity
    */

    private final String name;
    private final Boolean flamable;
    private final int color;
    private final Integer density; // kg/m3 / 1000
    private final Integer hardness; // Rough Mohs Hardness * 10
    private final Integer purity; // Just vibes man

    private GunMaterials(String Name, int Color) {
        this(Name, Color, false, 0, 0, 0);
    }

    private GunMaterials(String Name, int Color, Integer Density, Integer Hardness, Integer Purity) {
        this(Name, Color, false, Density, Hardness, Purity);
    }

    private GunMaterials(String Name, int Color, Boolean Flamable, Integer Density, Integer Hardness, Integer Purity) {
        this.name = Name;
        this.color = Color;
        this.flamable = Flamable;
        this.density = Density;
        this.hardness = Hardness;
        this.purity = Purity;
    }
    @Override
    public String getSerializedName() {
        return this.name;
    }

    public Integer getColor() {
        return this.color;
    }

    public Boolean isFlamable() {
        return this.flamable;
    }

    public Boolean isMaterial(String name) {
        return Objects.equals(this.name, name);
    }

    public Integer getPurity() {
        return this.purity;
    }

    public Integer getHardness() {
        return this.hardness;
    }

    public Integer getDensity(){
        return this.density;
    }

    public static GunMaterials getMaterial(String name) {
        for (GunMaterials material : GunMaterials.values()) {
            if (material.isMaterial(name)) {
                return material;
            }
        }
        return GunMaterials.NULL;
    }

    public static GunMaterials getMaterial(ItemStack part) {
        return getMaterial(NBT.getStringTag(part, "material"));
    }
}
