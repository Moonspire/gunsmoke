package net.ironhorsedevgroup.mods.gunsmoke.mixin;

import net.ironhorsedevgroup.mods.gunsmoke.item.rounds.Rounds;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(Item.class)
public class ItemMixins {
    @Inject(at = @At("HEAD"), method = "appendHoverText(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Ljava/util/List;Lnet/minecraft/world/item/TooltipFlag;)V")
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag flag, CallbackInfo info) {
        if (level != null && level.isClientSide()) {
            List<String> acceptedCalibers = Rounds.getCalibers(itemStack.getItem());
            if (!acceptedCalibers.isEmpty()) {
                components.add(Component.translatable("tooltip.gunsmoke.accepted_calibers"));
                for (String caliber : acceptedCalibers) {
                    components.add(Component.literal("- " + I18n.get("caliber." + caliber)));
                }
            }
        }
    }
}
