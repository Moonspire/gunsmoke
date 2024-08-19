package net.ironhorsedevgroup.mods.gunsmoke.item;

import com.mrcrayfish.guns.common.Gun;
import net.minecraft.world.item.ItemStack;

public class GunItem extends com.mrcrayfish.guns.item.GunItem {
    public GunItem(Properties properties) {
        super(properties);
    }

    @Override
    public Gun getModifiedGun(ItemStack stack) {
        return super.getModifiedGun(stack);
    }
}
