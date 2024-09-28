package net.ironhorsedevgroup.mods.gunsmoke.registry;

import net.ironhorsedevgroup.mods.gunsmoke.item.RifleItem;
import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class GunsmokeTabs {
    public static CreativeModeTab FIREARMS;
    public static CreativeModeTab PARTS;

    public static void load() {

        FIREARMS = new CreativeModeTab("gunsmoke.firearms") {
            @Override
            public ItemStack makeIcon() {
                if (GunsmokeItems.DRAGOON.get() instanceof RifleItem rifle) {
                    ItemStack rifleItem = rifle.setMaterials("minecraft:spruce", "minecraft:iron", "forge:brass", "minecraft:iron");
                    return NBT.putIntTag(rifleItem, "AmmoCount", rifle.getModifiedGun(rifleItem).getGeneral().getMaxAmmo());
                }
                return null;
            }

            @OnlyIn(Dist.CLIENT)
            public boolean hasSearchBar() {
                return true;
            }
        };

        PARTS = new CreativeModeTab("gunsmoke.parts") {
            @Override
            public ItemStack makeIcon() {
                return NBT.putStringTag(new ItemStack(GunsmokeItems.STOCK_ADVANCED.get()), "material", "minecraft:oak");
            }

            @OnlyIn(Dist.CLIENT)
            public boolean hasSearchBar() {
                return true;
            }
        };
    }
}
