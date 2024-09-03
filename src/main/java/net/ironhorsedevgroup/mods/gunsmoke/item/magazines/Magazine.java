package net.ironhorsedevgroup.mods.gunsmoke.item.magazines;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public interface Magazine {
    static Magazine fromJson(JsonObject json) {
        return DynamicMagazine.fromJson(json);
    }

    int getCapacity();
    List<ResourceLocation> getFamilies();
    boolean hasFamily(ResourceLocation location);
    List<String> getCalibers();
    boolean hasCaliber(String caliber);
}
