package net.ironhorsedevgroup.mods.gunsmoke.item.magazines;

import net.ironhorsedevgroup.mods.gunsmoke.item.guns.DynamicGun;
import net.ironhorsedevgroup.mods.gunsmoke.item.guns.Gun;
import net.ironhorsedevgroup.mods.gunsmoke.item.guns.MaterialGun;
import net.ironhorsedevgroup.mods.gunsmoke.item.rounds.Round;
import net.ironhorsedevgroup.mods.toolshed.tools.NBT;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GunMagazine implements Magazine {
    private final int capacity;
    private final ResourceLocation material;
    private final List<String> calibers;
    private final List<Round> rounds = new ArrayList<>();

    public GunMagazine(int capacity, ResourceLocation material, List<String> calibers) {
        this.capacity = capacity;
        this.material = material;
        this.calibers = calibers;
    }

    public static GunMagazine fromGun(Gun gun) {
        int capacity = gun.getRoundStorage().getCapacity();
        ResourceLocation material = gun.getComposition().getCore().getMaterial();
        List<String> calibers = new ArrayList<>();

        for (DynamicGun.RoundStorage.Round round : gun.getRoundStorage().getRounds()) {
            if (round instanceof DynamicGun.RoundStorage.Caliber caliber) {
                calibers.add(caliber.getCaliber());
            }
        }

        return new GunMagazine(capacity, material, calibers);
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public boolean canReloadInGun() {
        return true;
    }

    @Override
    public ResourceLocation getMaterial() {
        return material;
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

    @Override
    public boolean isFull() {
        return rounds.size() == getCapacity();
    }

    @Override
    public List<Round> getRounds() {
        return rounds;
    }
}
