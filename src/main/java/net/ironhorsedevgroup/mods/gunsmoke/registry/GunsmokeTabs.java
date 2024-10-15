package net.ironhorsedevgroup.mods.gunsmoke.registry;

import net.ironhorsedevgroup.mods.gunsmoke.item.GunItem;
import net.ironhorsedevgroup.mods.gunsmoke.item.PartItem;
import net.ironhorsedevgroup.mods.gunsmoke.item.RoundItem;
import net.ironhorsedevgroup.mods.gunsmoke.item.guns.Guns;
import net.ironhorsedevgroup.mods.gunsmoke.item.rounds.Rounds;
import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class GunsmokeTabs {
    public static CreativeModeTab FIREARMS;
    public static CreativeModeTab ROUNDS;
    public static CreativeModeTab PARTS;
    public static CreativeModeTab CUSTOM;

    public static void load() {

        FIREARMS = new CreativeModeTab("gunsmoke.firearms") {
            @Override
            public @NotNull ItemStack makeIcon() {
                ItemStack gunItem = GunItem.get().getDefaultInstance("gunsmoke:sharps");
                return NBT.putIntTag(gunItem, "AmmoCount", Guns.getGun("gunsmoke:sharps").getMagazine().getCapacity());
            }

            @OnlyIn(Dist.CLIENT)
            public boolean hasSearchBar() {
                return true;
            }
        };

        ROUNDS = new CreativeModeTab("gunsmoke.rounds") {

            @Override
            public @NotNull ItemStack makeIcon() {
                return RoundItem.getDefaultInstance(".50-70:pointed");
            }

            @OnlyIn(Dist.CLIENT)
            public boolean hasSearchBar() {
                return true;
            }
        };

        PARTS = new CreativeModeTab("gunsmoke.parts") {

            @Override
            public @NotNull ItemStack makeIcon() {
                return PartItem.getDefaultInstance("gunsmoke:gun_parts");
            }

            @OnlyIn(Dist.CLIENT)
            public boolean hasSearchBar() {
                return true;
            }
        };
    }
}
