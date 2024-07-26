package net.ironhorsedevgroup.mods.gunsmoke.gui.inventory;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Supplier;

public class GunBenchMenu extends AbstractContainerMenu implements Supplier<Map<Integer, Slot>> {
    protected GunBenchMenu(@Nullable MenuType<?> type, int i) {
        super(type, i);
    }

    @Override
    public Map<Integer, Slot> get() {
        return Map.of();
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return false;
    }
}
