package net.ironhorsedevgroup.mods.gunsmoke.item.guns;

import net.ironhorsedevgroup.mods.gunsmoke.item.rounds.RoundProperties;
import net.ironhorsedevgroup.mods.gunsmoke.registry.GunsmokeCalibers;
import net.ironhorsedevgroup.mods.toolshed.materials.Materials;
import net.ironhorsedevgroup.mods.toolshed.tools.Color;
import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.world.item.ItemStack;

public class GunColor {
    public static int getColor(ItemStack itemStack, int tintIndex) {
        return Materials.getClientMaterial(NBT.getStringTag(itemStack, "material_" + tintIndex)).getProperties().getColor();
    }

    public static int getPartColor(ItemStack itemStack, int tintIndex) {
        return Materials.getClientMaterial(NBT.getStringTag(itemStack, "material")).getProperties().getColor();
    }

    public static int getRoundColor(ItemStack itemStack, int tintIndex) {
        if (tintIndex < 2) {
            return getColor(itemStack, tintIndex);
        }
        RoundProperties round = GunsmokeCalibers.getRound(itemStack);
        if (round.hasColor()) {
            return round.getColor();
        }
        return Color.getIntFromRGB(255, 255, 255);
    }
}
