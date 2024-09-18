package net.ironhorsedevgroup.mods.gunsmoke.mixin;

import com.mrcrayfish.framework.api.network.LevelLocation;
import com.mrcrayfish.guns.Config;
import com.mrcrayfish.guns.common.ReloadTracker;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.network.message.S2CMessageGunSound;
import net.ironhorsedevgroup.mods.gunsmoke.item.GunItem;
import net.ironhorsedevgroup.mods.gunsmoke.item.guns.Gun;
import net.ironhorsedevgroup.mods.gunsmoke.item.guns.Guns;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ReloadTracker.class)
public class ReloadTrackerMixins {
    @Inject(at = @At("HEAD"), method = "increaseAmmo(Lnet/minecraft/world/entity/player/Player;)V", remap = false, cancellable = true)
    private void increaseAmmo(Player player, CallbackInfo ci) {
        ItemStack mainItem = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (mainItem.getItem() instanceof GunItem) {
            Gun gun = Guns.getMaterialGun(mainItem);
            if (gun.getMagazine().isFull()) {
                if (!gun.load(player.getItemInHand(InteractionHand.OFF_HAND))) {
                    for (int i = 0; i < 9; i++) {
                        ItemStack stack = player.getSlot(i).get();
                        if (gun.load(stack) && gun.getMagazine().isFull()) {
                            break;
                        }
                    }
                }
            }
            gunsmoke$playReloadSound(gun.asGun().getSounds().getReload(), player);
            ci.cancel();
        }
    }

    @Unique
    public void gunsmoke$playReloadSound(ResourceLocation sound, Player player) {
        if (sound != null) {
            double radius = (Double)Config.SERVER.reloadMaxDistance.get();
            double soundX = player.getX();
            double soundY = player.getY() + 1.0;
            double soundZ = player.getZ();
            S2CMessageGunSound message = new S2CMessageGunSound(sound, SoundSource.PLAYERS, (float)soundX, (float)soundY, (float)soundZ, 1.0F, 1.0F, player.getId(), false, true);
            PacketHandler.getPlayChannel().sendToNearbyPlayers(() -> {
                return LevelLocation.create(player.level, soundX, soundY, soundZ, radius);
            }, message);
        }
    }
}
