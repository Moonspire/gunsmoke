package net.ironhorsedevgroup.mods.gunsmoke.mixin;

import com.mrcrayfish.guns.client.ClientHandler;
import com.mrcrayfish.guns.client.handler.GunRenderingHandler;
import com.mrcrayfish.guns.client.util.PropertyHelper;
import com.mrcrayfish.guns.item.IColored;
import com.mrcrayfish.guns.item.attachment.IAttachment;
import net.ironhorsedevgroup.mods.gunsmoke.item.RifleItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ClientHandler.class)
public class GunClientHandler {

    //@Inject(at = @At("HEAD"), method = "registerColors()V", cancellable = true)
    /**
     * @author aunuli
     * @reason required to unmap RifleItem class from GunItem's ItemColors
     */
    @Overwrite(remap = false)
    private static void registerColors() {
        ItemColor color = (stack, index) -> {
            if (!IColored.isDyeable(stack)) {
                return -1;
            } else if (index == 0 && stack.hasTag() && stack.getTag().contains("Color", 3)) {
                return stack.getTag().getInt("Color");
            } else {
                if (index == 0 && stack.getItem() instanceof IAttachment) {
                    ItemStack renderingWeapon = GunRenderingHandler.get().getRenderingWeapon();
                    if (renderingWeapon != null) {
                        return Minecraft.getInstance().getItemColors().getColor(renderingWeapon, index);
                    }
                }

                return index == 2 ? PropertyHelper.getReticleColor(stack) : -1;
            }
        };
        ForgeRegistries.ITEMS.forEach((item) -> {
            if (item instanceof IColored && !(item instanceof RifleItem)) {
                Minecraft.getInstance().getItemColors().register(color, new ItemLike[]{item});
            }
        });
    }
}
