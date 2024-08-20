package net.ironhorsedevgroup.mods.gunsmoke.registry;

import net.ironhorsedevgroup.mods.gunsmoke.item.RifleItem;
import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.resources.ResourceLocation;
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
                ItemStack rifleItem = new ItemStack(GunsmokeItems.DRAGOON.get());
                if (GunsmokeItems.DRAGOON.get() instanceof RifleItem rifle) {
                    rifleItem = rifle.setMaterials(new ResourceLocation("minecraft:spruce"), new ResourceLocation("forge:steel"), new ResourceLocation("forge:brass"), new ResourceLocation("forge:steel"));
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
