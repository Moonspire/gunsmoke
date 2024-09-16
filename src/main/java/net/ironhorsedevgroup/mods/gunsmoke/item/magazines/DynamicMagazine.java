package net.ironhorsedevgroup.mods.gunsmoke.item.magazines;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.ironhorsedevgroup.mods.toolshed.materials.Materials;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class DynamicMagazine implements Magazine {
    private final int capacity;
    private final boolean gunReload;
    private final List<ResourceLocation> families;
    private final List<String> calibers;

    public DynamicMagazine() {
        this.capacity = 0;
        this.gunReload = false;
        this.families = new ArrayList<>();
        this.calibers = new ArrayList<>();
    }

    public DynamicMagazine(int capacity, boolean gunReload, List<ResourceLocation> families, List<String> calibers) {
        this.capacity = capacity;
        this.gunReload = gunReload;
        this.families = families;
        this.calibers = calibers;
    }

    public static Magazine fromJson(JsonObject json) {
        int capacity = json.get("capacity").getAsInt();
        boolean gunReload = false;
        List<ResourceLocation> families = new ArrayList<>();
        List<String> calibers = new ArrayList<>();

        if (json.has("internal_loading")) {
            gunReload = json.get("internal_loading").getAsBoolean();
        }
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

        return new DynamicMagazine(capacity, gunReload, families, calibers);
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public boolean canReloadInGun() {
        return gunReload;
    }

    @Override
    public ResourceLocation getMaterial() {
        return new ResourceLocation("null");
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

    @Override
    public void putTag(ItemStack stack) {}
}
