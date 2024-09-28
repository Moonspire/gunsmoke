package net.ironhorsedevgroup.mods.gunsmoke.item.guns;

import net.ironhorsedevgroup.mods.toolshed.materials.Materials;
import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.world.item.ItemStack;

public class GunColor {
    public static int getColor(ItemStack itemStack, int tintIndex) {
        return Materials.getMaterial(NBT.getStringTag(itemStack, "material_" + tintIndex)).getProperties().getColor();
    }

    public static int getPartColor(ItemStack itemStack, int tintIndex) {
        return Materials.getMaterial(NBT.getStringTag(itemStack, "material")).getProperties().getColor();
    }
}
