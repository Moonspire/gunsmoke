package net.ironhorsedevgroup.mods.gunsmoke.mixin;

import com.mrcrayfish.guns.init.ModEnchantments;
import com.mrcrayfish.guns.util.GunEnchantmentHelper;
import net.ironhorsedevgroup.mods.gunsmoke.item.GunItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GunEnchantmentHelper.class)
public class GunEnchantmentHelperMixin {

    @Inject(at = @At("HEAD"), method = "getReloadInterval(Lnet/minecraft/world/item/ItemStack;)I", cancellable = true, remap = false)
    private static void getReloadInterval(ItemStack weapon, CallbackInfoReturnable<Integer> callback) {
        if (weapon.getItem() instanceof GunItem rifleItem) {
            int interval = 10;
            int level = EnchantmentHelper.getTagEnchantmentLevel(ModEnchantments.QUICK_HANDS.get(), weapon);
            if (level > 0) {
                interval -= 3 * level;
            }
            if (interval < 1) {
                interval = 1;
            }
            callback.setReturnValue(interval);
        }
    }
}
