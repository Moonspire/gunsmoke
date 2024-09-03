package net.ironhorsedevgroup.mods.gunsmoke.item.guns;

import net.ironhorsedevgroup.mods.toolshed.content_packs.resources.model.ItemModelOverride;
import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class GunModelOverride implements ItemModelOverride {
    public GunModelOverride() { }

    public BakedModel getModel(ResourceLocation location) {
        ModelManager manager = Minecraft.getInstance().getModelManager();
        return manager.getModel(new ModelResourceLocation(Guns.getModel(location), "inventory"));
    }

    @Override
    public BakedModel getModel(ItemStack itemStack) {
        return getModel(NBT.getLocationTag(itemStack, "gun"));
    }

    @Override
    public BakedModel getModel(Item item) {
        return getModel();
    }

    @Override
    public BakedModel getModel() {
        ModelManager manager = Minecraft.getInstance().getModelManager();
        return manager.getModel(new ModelResourceLocation("toolshed:error#inventory"));
    }
}
