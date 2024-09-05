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

    @Override
    public Round useNextRound() {
        Round round = rounds.get(rounds.size() - 1);
        rounds.remove(rounds.size() - 1);
        return round;
    }

    @Override
    public Round useRound(int index) {
        if (rounds.size() > index) {
            Round round = rounds.get(index);
            rounds.remove(index);
            return round;
        }
        return null;
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
