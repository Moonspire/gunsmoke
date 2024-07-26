package net.ironhorsedevgroup.mods.gunsmoke.registry;

import net.ironhorsedevgroup.mods.gunsmoke.Gunsmoke;
import net.ironhorsedevgroup.mods.gunsmoke.block.entity.GunBenchEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class GunsmokeBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Gunsmoke.MODID);

    public static final RegistryObject<BlockEntityType<GunBenchEntity>> GUN_BENCH = REGISTRY.register("gun_bench", () -> BlockEntityType.Builder.of(GunBenchEntity::new, GunsmokeBlocks.GUN_BENCH.get()).build(null));
}
