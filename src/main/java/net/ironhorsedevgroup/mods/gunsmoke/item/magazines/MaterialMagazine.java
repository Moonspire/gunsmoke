package net.ironhorsedevgroup.mods.gunsmoke.item.magazines;

import net.ironhorsedevgroup.mods.gunsmoke.item.rounds.MaterialRound;
import net.ironhorsedevgroup.mods.gunsmoke.item.rounds.Round;
import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MaterialMagazine implements Magazine {
    private final int capacity;
    private final List<ResourceLocation> families;
    private final List<String> calibers;
    private final List<Round> rounds;

    private MaterialMagazine(int capacity, List<ResourceLocation> families, List<String> calibers, List<Round> rounds) {
        this.capacity = capacity;
        this.families = families;
        this.calibers = calibers;
        this.rounds = rounds;
    }

    public static Magazine fromItemStack(ItemStack stack) {
        Magazine magazine = Magazines.getMagazine(stack);
        int capacity = magazine.getCapacity();
        List<ResourceLocation> families = magazine.getFamilies();
        List<String> calibers = magazine.getCalibers();
        List<Round> rounds = new ArrayList<>();
        for (int i = 0; i < capacity; i++) {
            Round round = MaterialRound.fromTag("round." + i, stack);
            if (round != null) {
                rounds.add(round);
            } else {
                break;
            }
        }

        return new MaterialMagazine(capacity, families, calibers, rounds);
    }

    @Override
    public int getCapacity() {
        return 0;
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
        return List.of();
    }

    @Override
    public boolean hasCaliber(String caliber) {
        return false;
    }
}
