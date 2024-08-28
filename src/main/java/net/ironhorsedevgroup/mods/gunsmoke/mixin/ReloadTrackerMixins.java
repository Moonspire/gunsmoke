package net.ironhorsedevgroup.mods.gunsmoke.mixin;

import com.mrcrayfish.guns.common.AmmoContext;
import com.mrcrayfish.guns.common.Gun;
import com.mrcrayfish.guns.common.ReloadTracker;
import net.ironhorsedevgroup.mods.gunsmoke.item.GunItem;
import net.ironhorsedevgroup.mods.gunsmoke.item.RoundItem;
import net.ironhorsedevgroup.mods.gunsmoke.registry.GunsmokeItems;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ReloadTracker.class)
public class ReloadTrackerMixins {
    @Inject(at = @At("HEAD"), method = "increaseAmmo(Lnet/minecraft/world/entity/player/Player;)V", remap = false)
    private void increaseAmmo(Player player, CallbackInfo ci) {
        ItemStack mainItem = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (mainItem.getItem() instanceof GunItem) {
            AmmoContext context = Gun.findAmmo(player, GunsmokeItems.ROUND_ITEM.getId());
            ItemStack ammo = context.stack();
            if (ammo.getItem() instanceof RoundItem) {
                //rifleItem.loadRound(RoundItem.getRound(ammo));
            }
        }
    }
}
