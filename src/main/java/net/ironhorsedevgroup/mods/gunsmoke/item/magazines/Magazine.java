package net.ironhorsedevgroup.mods.gunsmoke.item.magazines;

import com.google.gson.JsonObject;
import net.ironhorsedevgroup.mods.gunsmoke.item.rounds.Round;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface Magazine {
    static Magazine fromJson(JsonObject json) {
        return DynamicMagazine.fromJson(json);
    }

    int getCapacity();
    boolean canReloadInGun();
    ResourceLocation getMaterial();
    List<ResourceLocation> getFamilies();
    boolean hasFamily(ResourceLocation location);
    List<String> getCalibers();
    boolean hasCaliber(String caliber);
    default Round useNextRound() {
        return null;
    }
    default Round useRound(int index) {
        return null;
    }
    default boolean loadRound(Round round) {
        return false;
    }
    void putTag(ItemStack stack);
}
