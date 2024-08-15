package net.ironhorsedevgroup.mods.gunsmoke.registry;

import net.ironhorsedevgroup.mods.gunsmoke.olditem.RifleItem;
import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class GunsmokeTabs {
    public static CreativeModeTab FIREARMS;
    public static CreativeModeTab PARTS;

    public static void load() {

        FIREARMS = new CreativeModeTab("gunsmoke.firearms") {
            @Override
            public @NotNull ItemStack makeIcon() {
                ItemStack rifleItem = new ItemStack(GunsmokeItems.DRAGOON.get());
                if (GunsmokeItems.DRAGOON.get() instanceof RifleItem rifle) {
                    rifleItem = rifle.setMaterials(GunsmokeMaterials.SPRUCE, GunsmokeMaterials.WROUGHT_IRON, GunsmokeMaterials.BRASS, GunsmokeMaterials.WROUGHT_IRON);
                }
                return NBT.putIntTag(rifleItem, "AmmoCount", 1);
            }

            @OnlyIn(Dist.CLIENT)
            public boolean hasSearchBar() {
                return true;
            }
        };

        PARTS = new CreativeModeTab("gunsmoke.parts") {
            @Override
            public @NotNull ItemStack makeIcon() {
                return NBT.putStringTag(new ItemStack(GunsmokeItems.STOCK_ADVANCED.get()), "material", GunsmokeMaterials.OAK.getSerializedName());
            }

            @OnlyIn(Dist.CLIENT)
            public boolean hasSearchBar() {
                return true;
            }
        };
    }
}
