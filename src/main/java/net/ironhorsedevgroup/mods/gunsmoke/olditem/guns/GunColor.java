package net.ironhorsedevgroup.mods.gunsmoke.olditem.guns;

import net.ironhorsedevgroup.mods.gunsmoke.materials.MaterialUtils;
import net.ironhorsedevgroup.mods.gunsmoke.olditem.rounds.RoundProperties;
import net.ironhorsedevgroup.mods.gunsmoke.registry.GunsmokeMaterials;
import net.ironhorsedevgroup.mods.toolshed.tools.Color;
import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class GunColor {
    public static int getColor(ItemStack itemStack, int tintIndex) {
        return GunsmokeMaterials.getMaterial(NBT.getStringTag(itemStack, "material_" + tintIndex)).getColor();
    }

    public static int getPartColor(ItemStack itemStack, int tintIndex) {
        return GunsmokeMaterials.getMaterial(NBT.getStringTag(itemStack, "material")).getColor();
    }
}
