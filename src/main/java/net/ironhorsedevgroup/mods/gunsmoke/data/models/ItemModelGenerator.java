package net.ironhorsedevgroup.mods.gunsmoke.data.models;

import net.ironhorsedevgroup.mods.gunsmoke.item.RoundItem;
import net.ironhorsedevgroup.mods.gunsmoke.item.rounds.RoundProperties;
import net.ironhorsedevgroup.mods.gunsmoke.item.rounds.RoundTextureSources;
import net.ironhorsedevgroup.mods.gunsmoke.registry.GunsmokeItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.List;
import java.util.Objects;

public class ItemModelGenerator extends ItemModelProvider {
    private static final ResourceLocation ITEM_GENERATED = new ResourceLocation("item/generated");

    public ItemModelGenerator(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
        super(generator, modid, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (String caliber : GunsmokeItems.CALIBERS.keySet()) {
            roundItem(caliber);
        }
        /*
        for (GunsmokeCalibers calibers : GunsmokeCalibers.values()) {
            roundItem(calibers.getCaliber());
        }
        */
    }

    private ItemModelBuilder roundItem(String name) {
        ItemModelBuilder builder = null;
        RoundTextureSources lastTexture = null;
        List<RoundProperties> caliber = RoundItem.getCaliber(name);
        if (caliber != null) {
            for (RoundProperties round : caliber) {
                if (!(Objects.equals(round.getTexture(), lastTexture))) {
                    int id = round.getId();
                    lastTexture = round.getTexture();
                    String modelName = name + "." + id;
                    if (id == 0) {
                        modelName = name;
                    }
                    ItemModelBuilder model = withExistingParent(modelName, ITEM_GENERATED);

                    if (lastTexture.renderAccessory()) {
                        model.texture("layer3", lastTexture.getAccessory());
                        setInvisibleLayers(model, 3);
                    }
                    if (lastTexture.renderColor()) {
                        model.texture("layer2", lastTexture.getColor());
                        setInvisibleLayers(model, 2);
                    }
                    if (lastTexture.renderCasing()) {
                        model.texture("layer1", lastTexture.getCasing());
                        setInvisibleLayers(model, 1);
                    }
                    if (lastTexture.renderRound()) {
                        model.texture("layer0", lastTexture.getRound());
                    }

                    if (id == 0) {
                        builder = model;
                    } else {
                        builder.override()
                                .predicate(new ResourceLocation(modelName), id)
                                .model(model);
                    }
                }
            }
        }
        return builder;
    }

    private ItemModelBuilder setInvisibleLayers(ItemModelBuilder builder, int index) {
        for (int i = 0; i < index; i++) {
            builder.texture("layer" + i, new ResourceLocation("gunsmoke", "items/invis"));
        }
        return builder;
    }

    /*
    private ItemModelBuilder roundItem(CaliberProperties caliber) {
        ItemModelBuilder builder = withExistingParent(caliber.getName(), ITEM_GENERATED);
        String lastPath = "";
        for (RoundProperties round : caliber.getRounds()) {
            String path = round.getTexture().toString();
            if (!path.equals(lastPath)) {
                lastPath = path;
                if (round.getId() == 0) {
                    if (round.isRoundRendered()) {
                        builder.texture("layer0", path + "/round");
                    } else {
                        builder.texture("layer0", new ResourceLocation("gunsmoke", "items/invis"));
                    }

                    if (!round.isCaseless()) {
                        builder.texture("layer1", path + "/casing");
                    } else {
                        builder.texture("layer1", new ResourceLocation("gunsmoke", "items/invis"));
                    }

                    if (round.hasColor()) {
                        builder.texture("layer2", path + "/color");
                    } else if (round.hasAccessories()) {
                        builder.texture("layer2", path + "/accessories");
                    }
                } else {
                    ItemModelBuilder predicate = withExistingParent(caliber.getName() + "." + round.getId(), ITEM_GENERATED);
                    if (round.isRoundRendered()) {
                        predicate.texture("layer0", path + "/round");
                    } else {
                        predicate.texture("layer0", new ResourceLocation("gunsmoke", "items/invis"));
                    }

                    if (!round.isCaseless()) {
                        predicate.texture("layer1", path + "/casing");
                    } else {
                        predicate.texture("layer1", new ResourceLocation("gunsmoke", "items/invis"));
                    }

                    if (round.hasColor()) {
                        predicate.texture("layer2", path + "/color");
                    } else if (round.hasAccessories()) {
                        predicate.texture("layer2", path + "/accessories");
                    }

                    builder.override()
                            .predicate(new ResourceLocation("custom_model_data"), round.getId())
                            .model(predicate);
                }
            }
        }
        return builder;
    }
    */
}
