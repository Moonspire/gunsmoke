package net.ironhorsedevgroup.mods.gunsmoke.item.guns;

import net.ironhorsedevgroup.mods.gunsmoke.item.RoundItem;
import net.ironhorsedevgroup.mods.gunsmoke.item.rounds.RoundProperties;
import net.ironhorsedevgroup.mods.gunsmoke.registry.GunsmokeMaterials;
import net.ironhorsedevgroup.mods.toolshed.tools.Color;
import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.world.item.ItemStack;

public class GunColor {
    public static int getColor(ItemStack itemStack, int tintIndex) {
        return GunsmokeMaterials.getMaterial(NBT.getStringTag(itemStack, "material_" + tintIndex)).getColor();
    }

    public static int getPartColor(ItemStack itemStack, int tintIndex) {
        return GunsmokeMaterials.getMaterial(NBT.getStringTag(itemStack, "material")).getColor();
    }

    public static int getRoundColor(ItemStack itemStack, int tintIndex) {
        if (tintIndex < 2) {
            return getColor(itemStack, tintIndex);
        }
        if (tintIndex == 2) {
            if (itemStack.getItem() instanceof RoundItem) {
                RoundProperties round = RoundItem.getRound(itemStack);
                if (round != null && round.getTexture() != null && round.getTexture().renderColor()) {
                    return round.getTexture().getRGB();
                }
            }
        }
        return Color.getIntFromRGB(255, 255, 255);
    }
}
