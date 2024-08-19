package net.ironhorsedevgroup.mods.gunsmoke.data;

import com.google.gson.JsonObject;
import net.ironhorsedevgroup.mods.gunsmoke.Gunsmoke;
import net.ironhorsedevgroup.mods.gunsmoke.item.materials.MaterialUtils;
import net.ironhorsedevgroup.mods.gunsmoke.item.rounds.RoundUtils;
import net.ironhorsedevgroup.mods.toolshed.tools.Data;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

public class DataLoader {
    public static void loadData(ResourceManager manager) {
        for (String namespace : manager.getNamespaces()) {
            JsonObject gunsmokeFile = Data.readJson(new ResourceLocation(namespace, "gunsmoke"), manager);
            if (gunsmokeFile.has("materials")) {
                Gunsmoke.LOGGER.info("[Gunsmoke]: Registering Materials from {}", namespace);
                MaterialUtils.loadMaterials(namespace, gunsmokeFile.getAsJsonArray("materials"), manager);
            }
            if (gunsmokeFile.has("rounds")) {
                Gunsmoke.LOGGER.info("[Gunsmoke]: Registering Rounds from {}", namespace);
                RoundUtils.loadRounds(namespace, gunsmokeFile.getAsJsonArray("rounds"), manager);
            }
        }
    }
}
