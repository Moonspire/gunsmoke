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
                    ItemStack rifleItem = rifle.setMaterials(GunsmokeMaterials.SPRUCE, GunsmokeMaterials.WROUGHT_IRON, GunsmokeMaterials.BRASS, GunsmokeMaterials.WROUGHT_IRON);
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
                return NBT.putStringTag(new ItemStack(GunsmokeItems.STOCK_ADVANCED.get()), "material", GunsmokeMaterials.OAK.getSerializedName());
            }

            @OnlyIn(Dist.CLIENT)
            public boolean hasSearchBar() {
                return true;
            }
        };
    }
}
