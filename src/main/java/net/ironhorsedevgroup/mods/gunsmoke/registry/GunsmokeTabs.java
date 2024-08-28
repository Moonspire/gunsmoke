package net.ironhorsedevgroup.mods.gunsmoke.registry;

import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.world.item.CreativeModeTab;
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
                ItemStack gunItem = new ItemStack(GunsmokeItems.GUN_ITEM.get());
                return NBT.putIntTag(gunItem, "AmmoCount", 1);
            }

            @OnlyIn(Dist.CLIENT)
            public boolean hasSearchBar() {
                return true;
            }
        };

        PARTS = new CreativeModeTab("gunsmoke.parts") {

            @Override
            public @NotNull ItemStack makeIcon() {
                return new ItemStack(GunsmokeItems.PART_ITEM.get());
            }

            @OnlyIn(Dist.CLIENT)
            public boolean hasSearchBar() {
                return true;
            }
        };
    }
}
