package net.ironhorsedevgroup.mods.gunsmoke.item.magazines;

import net.ironhorsedevgroup.mods.gunsmoke.item.guns.DynamicGun;
import net.ironhorsedevgroup.mods.gunsmoke.item.rounds.Round;
import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GunMagazine implements Magazine {
    private final int capacity;
    private final List<String> calibers;
    private final List<Round> rounds = new ArrayList<>();

    public GunMagazine(int capacity, List<String> calibers) {
        this.capacity = capacity;
        this.calibers = calibers;
    }

    public static GunMagazine fromGun(DynamicGun gun) {
        int capacity = gun.getMagazine().getCapacity();
        List<String> calibers = new ArrayList<>();

        for (DynamicGun.RoundStorage.Round round : gun.getMagazine().getRounds()) {
            if (round instanceof DynamicGun.RoundStorage.Caliber caliber) {
                calibers.add(caliber.getCaliber());
            }
        }

        return new GunMagazine(capacity, calibers);
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public List<ResourceLocation> getFamilies() {
        return List.of();
    }

    @Override
    public boolean hasFamily(ResourceLocation location) {
        return false;
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
    public void putTag(ItemStack stack) {
        for (int i = 0; i < capacity; i++) {
            String tag = "round." + i;
            if (i < rounds.size()) {
                Round round = rounds.get(i);
                round.putTag(tag, stack);
            } else {
                NBT.removeTag(stack, tag + ".location");
                NBT.removeTag(stack, tag + ".round");
                NBT.removeTag(stack, tag + ".casing");
            }
        }
    }
}
