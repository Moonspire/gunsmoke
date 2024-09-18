package net.ironhorsedevgroup.mods.gunsmoke.item.guns;

import net.ironhorsedevgroup.mods.gunsmoke.item.MagazineItem;
import net.ironhorsedevgroup.mods.gunsmoke.item.RoundItem;
import net.ironhorsedevgroup.mods.gunsmoke.item.magazines.Magazine;
import net.ironhorsedevgroup.mods.gunsmoke.item.rounds.Round;
import net.ironhorsedevgroup.mods.gunsmoke.item.rounds.Rounds;
import net.minecraft.world.item.ItemStack;

public interface Gun {
    com.mrcrayfish.guns.common.Gun asGun();
    DynamicGun.Properties getProperties();
    DynamicGun.Composition getComposition();
    Magazine getMagazine();
    DynamicGun.RoundStorage getRoundStorage();
    DynamicGun.Sounds getSounds();
    DynamicGun.Render getRender();

    default boolean load(ItemStack stack) {
        if (stack.getItem() instanceof RoundItem) {
            return loadRound(stack);
        }
        if (stack.getItem() instanceof MagazineItem) {
            return false;
        }
        return false;
    }

    default boolean loadRound(ItemStack stack) {
        if (Rounds.getRound(stack) != null) {
            return loadRound(Rounds.getRound(stack));
        }
        return false;
    }

    default boolean loadRound(Round round) {
        return false;
    }
}
