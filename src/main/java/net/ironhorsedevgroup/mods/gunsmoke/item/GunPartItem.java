package net.ironhorsedevgroup.mods.gunsmoke.item;

import net.ironhorsedevgroup.mods.toolshed.content_packs.resources.assets.ResourceLoader;
import net.ironhorsedevgroup.mods.toolshed.materials.Materials;
import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GunPartItem extends Item {
    public GunPartItem(Properties properties) {
        super(properties);
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> itemStack) {
        if (this.allowedIn(tab)) {
            for (ResourceLocation location : Materials.getMaterials()) {
                itemStack.add(NBT.putLocationTag(new ItemStack(this), "material", location));
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        if (level != null && level.isClientSide) {
            ResourceLocation material = NBT.getLocationTag(itemStack, "material");
            Style style = Style.EMPTY.withColor(TextColor.fromRgb(Materials.getMaterial(material).getProperties().getColor()));
            Component component = Component.translatable(ResourceLoader.getMaterials().getMaterialLang(material)).withStyle(style);
            components.add(component);
        }
        super.appendHoverText(itemStack, level, components, flag);
    }
}
