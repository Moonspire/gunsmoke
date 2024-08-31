package net.ironhorsedevgroup.mods.gunsmoke;

import com.mojang.logging.LogUtils;
import net.ironhorsedevgroup.mods.gunsmoke.data.GunsmokeDataHandler;
import net.ironhorsedevgroup.mods.gunsmoke.item.guns.GunModelOverride;
import net.ironhorsedevgroup.mods.gunsmoke.item.guns.GunUtils;
import net.ironhorsedevgroup.mods.gunsmoke.item.materials.MaterialUtils;
import net.ironhorsedevgroup.mods.gunsmoke.item.RoundItem;
import net.ironhorsedevgroup.mods.gunsmoke.data.recipes.RecipeGenerator;
import net.ironhorsedevgroup.mods.gunsmoke.item.parts.PartModelOverride;
import net.ironhorsedevgroup.mods.gunsmoke.item.parts.PartUtils;
import net.ironhorsedevgroup.mods.gunsmoke.item.rounds.RoundModelOverride;
import net.ironhorsedevgroup.mods.gunsmoke.item.rounds.RoundUtils;
import net.ironhorsedevgroup.mods.gunsmoke.network.GunsmokeMessages;
import net.ironhorsedevgroup.mods.gunsmoke.registry.*;
import net.ironhorsedevgroup.mods.toolshed.content_packs.data.DataLoader;
import net.ironhorsedevgroup.mods.toolshed.content_packs.resources.model.ItemModelOverrides;
import net.minecraft.data.DataGenerator;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Gunsmoke.MODID)
public class Gunsmoke {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "gunsmoke";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public Gunsmoke() {
        GunsmokeTabs.load();
        DataLoader.addPackDataFile("gunsmoke", new GunsmokeDataHandler());

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so items get registered
        GunsmokeBlocks.REGISTRY.register(modEventBus);
        GunsmokeBlockEntities.REGISTRY.register(modEventBus);
        GunsmokeItems.REGISTRY.register(modEventBus);

        if (ModList.get().isLoaded("tconstruct")) {
            //GunsmokeItems.TCONSTRUCT.register(modEventBus);
        }

        GunsmokeRecipes.SERIALIZERS.register(modEventBus);

        GunsmokeMenus.REGISTRY.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel PACKET_HANDLER = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, MODID), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
    private static int messageID = 0;

    public static <T> void addNetworkMessage(Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer) {
        PACKET_HANDLER.registerMessage(messageID, messageType, encoder, decoder, messageConsumer);
        messageID++;
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(GunsmokeMessages::register);

        ItemModelOverrides.registerItem(GunsmokeItems.PART_ITEM.get(), new PartModelOverride());
        ItemModelOverrides.registerItem(GunsmokeItems.ROUND_ITEM.get(), new RoundModelOverride());
        ItemModelOverrides.registerItem(GunsmokeItems.GUN_ITEM.get(), new GunModelOverride());
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayer player = event.getEntity().getServer().getPlayerList().getPlayer(event.getEntity().getUUID());
        MaterialUtils.sendMaterials(player);
        PartUtils.sendParts(player);
        RoundUtils.sendRounds(player);
        GunUtils.sendGuns(player);
    }

    @Mod.EventBusSubscriber(modid = Gunsmoke.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class DataGenerators {
        @SubscribeEvent
        public static void gatherData(GatherDataEvent event) {
            DataGenerator generator = event.getGenerator();
            ExistingFileHelper helper = event.getExistingFileHelper();

            generator.addProvider(true, new RecipeGenerator(generator));
        }
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {

        }

        @SubscribeEvent
        public static void registerItemColors(RegisterColorHandlersEvent.Item event)
        {
            event.getItemColors().register(
                    (
                            MaterialUtils::getPartColor
                    ),
                    GunsmokeItems.PART_ITEM.get()
            );
            event.getItemColors().register(
                    (
                            MaterialUtils::getRoundColor
                    ),
                    GunsmokeItems.ROUND_ITEM.get()
            );
            event.getItemColors().register(
                    (
                            MaterialUtils::getGunColor
                    ),
                    GunsmokeItems.GUN_ITEM.get()
            );
        }
    }

}
