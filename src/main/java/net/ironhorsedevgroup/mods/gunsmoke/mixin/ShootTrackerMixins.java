package net.ironhorsedevgroup.mods.gunsmoke.mixin;

import com.mrcrayfish.guns.common.Gun;
import com.mrcrayfish.guns.common.ShootTracker;
import com.mrcrayfish.guns.item.GunItem;
import net.ironhorsedevgroup.mods.gunsmoke.item.RifleItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShootTracker.class)
public class ShootTrackerMixins {
    @Inject(at = @At("HEAD"), method = "putCooldown(Lnet/minecraft/world/item/ItemStack;Lcom/mrcrayfish/guns/item/GunItem;Lcom/mrcrayfish/guns/common/Gun;)V", remap = false)
    public void putCooldown(ItemStack weapon, GunItem item, Gun modifiedGun, CallbackInfo ci) {
        if (item instanceof RifleItem rifleItem) {
            rifleItem.damageGun(weapon);
        }
    }
}
