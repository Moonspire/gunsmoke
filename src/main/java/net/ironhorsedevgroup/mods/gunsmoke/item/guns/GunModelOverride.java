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

import java.util.HashMap;
import java.util.Map;

public class GunModelOverride implements ItemModelOverride {
    private final Map<ResourceLocation, BakedModel> models;

    public GunModelOverride() {
        this.models = new HashMap<>();
    }

    public void addModel(ResourceLocation gun) {
        ModelManager manager = Minecraft.getInstance().getModelManager();
        ResourceLocation location = GunUtils.getGun(gun).getRender().getModel();
        location = new ModelResourceLocation(location, "inventory");
        models.put(gun, manager.getModel(location));
    }

    public BakedModel getModel(ResourceLocation location) {
        if (models.containsKey(location)) {
            return models.get(location);
        }
        return getModel();
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
