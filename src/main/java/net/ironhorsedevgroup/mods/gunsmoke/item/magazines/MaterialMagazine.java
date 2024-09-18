package net.ironhorsedevgroup.mods.gunsmoke.item.magazines;

import net.ironhorsedevgroup.mods.gunsmoke.item.rounds.MaterialRound;
import net.ironhorsedevgroup.mods.gunsmoke.item.rounds.Round;
import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MaterialMagazine implements Magazine {
    private final ResourceLocation magazine;
    private final ResourceLocation material;
    private final List<Round> rounds;

    private MaterialMagazine(ResourceLocation magazine, ResourceLocation material, List<Round> rounds) {
        this.magazine = magazine;
        this.material = material;
        this.rounds = rounds;
    }

    public static Magazine fromItemStack(ItemStack stack) {
        ResourceLocation magazine = NBT.getLocationTag(stack, "magazine");
        ResourceLocation material = NBT.getLocationTag(stack, "mag_material");
        List<Round> rounds = new ArrayList<>();
        for (int i = 0; i < Magazines.getMagazine(magazine).getCapacity(); i++) {
            Round round = MaterialRound.fromTag("round." + i, stack);
            if (round != null) {
                rounds.add(round);
            } else {
                break;
            }
        }

        return new MaterialMagazine(magazine, material, rounds);
    }

    @Override
    public int getCapacity() {
        return Magazines.getMagazine(magazine).getCapacity();
    }

    @Override
    public boolean canReloadInGun() {
        return Magazines.getMagazine(magazine).canReloadInGun();
    }

    @Override
    public ResourceLocation getMaterial() {
        return material;
    }

    @Override
    public List<ResourceLocation> getFamilies() {
        return Magazines.getMagazine(magazine).getFamilies();
    }

    @Override
    public boolean hasFamily(ResourceLocation location) {
        return Magazines.getMagazine(magazine).hasFamily(location);
    }

    @Override
    public List<String> getCalibers() {
        return Magazines.getMagazine(magazine).getCalibers();
    }

    @Override
    public boolean hasCaliber(String caliber) {
        return Magazines.getMagazine(magazine).hasCaliber(caliber);
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
    public boolean loadRound(Round round) {
        if (rounds.size() < Magazines.getMagazine(magazine).getCapacity()) {
            rounds.add(round);
            return true;
        }
        return false;
    }

    @Override
    public void putTag(ItemStack stack) {
        for (int i = 0; i < Magazines.getMagazine(magazine).getCapacity(); i++) {
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

    @Override
    public boolean isFull() {
        return rounds.size() == getCapacity();
    }

    @Override
    public List<Round> getRounds() {
        return rounds;
    }
}
