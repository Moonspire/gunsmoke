package net.ironhorsedevgroup.mods.gunsmoke.gui.inventory;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class GunBenchMenu extends AbstractContainerMenu {
    protected GunBenchMenu(@Nullable MenuType<?> type, int i) {
        super(type, i);
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
