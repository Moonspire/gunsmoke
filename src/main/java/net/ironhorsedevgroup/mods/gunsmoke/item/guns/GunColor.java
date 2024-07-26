package net.ironhorsedevgroup.mods.gunsmoke.item.guns;

import net.ironhorsedevgroup.mods.gunsmoke.registry.GunsmokeMaterials;
import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.world.item.ItemStack;

public class GunColor {
    public static int getColor(ItemStack itemStack, int tintIndex) {
        return GunsmokeMaterials.getMaterial(NBT.getStringTag(itemStack, "material_" + tintIndex)).getColor();
    }

    public static int getPartColor(ItemStack itemStack, int tintIndex) {
        return GunsmokeMaterials.getMaterial(NBT.getStringTag(itemStack, "material")).getColor();
    }
}
