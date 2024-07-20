package net.ironhorsedevgroup.mods.gunsmoke.item;

import net.ironhorsedevgroup.mods.gunsmoke.item.guns.CaliberProperties;
import net.ironhorsedevgroup.mods.gunsmoke.item.guns.RoundProperties;
import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class RoundItem extends Item {
    private final CaliberProperties caliber;

    public RoundItem(Properties properties, CaliberProperties caliber) {
        super(properties);
        this.caliber = caliber;
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> itemStack) {
        if (this.allowedIn(tab)) {
            for (RoundProperties round : this.caliber.getRounds()) {
                ItemStack roundItem = new ItemStack(this);
                NBT.putIntTag(roundItem, "CustomModelData", round.getId());
                itemStack.add(roundItem);
            }
        }
    }
}
