package net.ironhorsedevgroup.mods.gunsmoke.item.magazines;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class DynamicMagazine implements Magazine{
    private final int capacity;
    private final List<ResourceLocation> families;
    private final List<String> calibers;

    public DynamicMagazine() {
        this.capacity = 0;
        this.families = new ArrayList<>();
        this.calibers = new ArrayList<>();
    }

    private DynamicMagazine(int capacity, List<ResourceLocation> families, List<String> calibers) {
        this.capacity = capacity;
        this.families = families;
        this.calibers = calibers;
    }

    public static Magazine fromJson(JsonObject json) {
        int capacity = json.get("capacity").getAsInt();
        List<ResourceLocation> families = new ArrayList<>();
        List<String> calibers = new ArrayList<>();

        if (json.has("families")) {
            for (JsonElement entry : json.getAsJsonArray("families")) {
                families.add(new ResourceLocation(entry.getAsString()));
            }
        }
        if (json.has("calibers")) {
            for (JsonElement entry : json.getAsJsonArray("calibers")) {
                calibers.add(entry.getAsString());
            }
        }

        return new DynamicMagazine(capacity, families, calibers);
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public List<ResourceLocation> getFamilies() {
        return families;
    }

    @Override
    public boolean hasFamily(ResourceLocation location) {
        return families.contains(location);
    }

    @Override
    public List<String> getCalibers() {
        return calibers;
    }

    @Override
    public boolean hasCaliber(String caliber) {
        return calibers.contains(caliber);
    }


}
