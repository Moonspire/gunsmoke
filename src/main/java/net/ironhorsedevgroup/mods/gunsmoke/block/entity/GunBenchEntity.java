package net.ironhorsedevgroup.mods.gunsmoke.block.entity;

import io.netty.buffer.Unpooled;
import net.ironhorsedevgroup.mods.gunsmoke.gui.inventory.GunBenchMenu;
import net.ironhorsedevgroup.mods.gunsmoke.recipes.GunBenchRecipe;
import net.ironhorsedevgroup.mods.gunsmoke.registry.GunsmokeBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class GunBenchEntity extends RandomizableContainerBlockEntity implements WorldlyContainer {
    private final ItemStackHandler handler = new ItemStackHandler(6) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private LazyOptional<IItemHandler> lazyHandler = LazyOptional.empty();

    @Override
    public Component getDisplayName() {
        return null;
    }

    public GunBenchEntity(BlockPos pos, BlockState state) {
        super(GunsmokeBlockEntities.GUN_BENCH.get(), pos, state);
    }

    @Override
    protected Component getDefaultName() {
        return null;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        NonNullList<ItemStack> stacks = NonNullList.create();
        for (int i = 0; i < 6; i++) {
            stacks.add(handler.getStackInSlot(i));
        }
        return stacks;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> stacks) {
        for (int i = 0; i < 6; i++) {
            handler.setStackInSlot(i, stacks.get(i));
        }
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new GunBenchMenu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(this.worldPosition));
    }

    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return new GunBenchMenu(i, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(this.worldPosition));
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyHandler = LazyOptional.of(() -> handler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", handler.serializeNBT());

        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);

        handler.deserializeNBT(nbt.getCompound("inventory"));
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(handler.getSlots());
        for (int i = 0; i < handler.getSlots(); i++) {
            inventory.setItem(i, handler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[0];
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, @Nullable Direction direction) {
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
        return false;
    }

    @Override
    public int getContainerSize() {
        return handler.getSlots();
    }

    public SimpleContainer getContainer() {
        SimpleContainer container = new SimpleContainer(this.handler.getSlots());
        for (int i = 0; i < getContainerSize(); i++) {
            container.setItem(i, this.handler.getStackInSlot(i));
        }
        return container;
    }

    public static void serverTick(ServerLevel level, GunBenchEntity entity) {
        Optional<GunBenchRecipe> recipe = level.getRecipeManager().getRecipeFor(GunBenchRecipe.Type.INSTANCE, entity.getContainer(), level);

        if (recipe.isPresent()) {
            entity.handler.setStackInSlot(4, recipe.get().assemble(entity.getContainer()));
        } else {
            entity.handler.setStackInSlot(4, ItemStack.EMPTY);
        }
    }
}
