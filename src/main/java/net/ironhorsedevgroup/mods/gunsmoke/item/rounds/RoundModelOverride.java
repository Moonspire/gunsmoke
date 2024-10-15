package net.ironhorsedevgroup.mods.gunsmoke.item.rounds;

import net.ironhorsedevgroup.mods.toolshed.content_packs.resources.assets.model.ItemModelOverride;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class RoundModelOverride implements ItemModelOverride {
    public RoundModelOverride() {}

    @Override
    public BakedModel getModel(ItemStack itemStack) {
        ModelManager manager = Minecraft.getInstance().getModelManager();
        if (Rounds.getRound(itemStack) instanceof DynamicRound round) {
            ModelResourceLocation location = new ModelResourceLocation(round.getRender().getModel(), "inventory");
            return manager.getModel(location);
        }
        return getModel();
    }

    @Override
    public BakedModel getModel(Item item) {
        return getModel();
    }

    @Override
    public BakedModel getModel() {
        ModelManager manager = Minecraft.getInstance().getModelManager();
        return manager.getModel(new ModelResourceLocation("gunsmoke:rounds/simple_pointed#inventory"));
    }
}
