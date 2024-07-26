package net.ironhorsedevgroup.mods.gunsmoke.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.ironhorsedevgroup.mods.gunsmoke.gui.inventory.GunBenchMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GunBenchGui extends AbstractContainerScreen<GunBenchMenu> {
    public GunBenchGui(GunBenchMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float v, int i, int i1) {

    }
}
