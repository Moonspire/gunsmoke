package net.ironhorsedevgroup.mods.gunsmoke;

import com.mojang.logging.LogUtils;
import net.ironhorsedevgroup.mods.gunsmoke.data.DataLoader;
import net.ironhorsedevgroup.mods.gunsmoke.data.models.ItemModelGenerator;
import net.ironhorsedevgroup.mods.gunsmoke.item.materials.MaterialUtils;
import net.ironhorsedevgroup.mods.gunsmoke.item.GunPartItem;
import net.ironhorsedevgroup.mods.gunsmoke.item.RifleItem;
import net.ironhorsedevgroup.mods.gunsmoke.item.RoundItem;
import net.ironhorsedevgroup.mods.gunsmoke.item.guns.GunColor;
import net.ironhorsedevgroup.mods.gunsmoke.data.recipes.RecipeGenerator;
import net.ironhorsedevgroup.mods.gunsmoke.registry.*;
import net.minecraft.client.Minecraft;
import net.minecraft.data.DataGenerator;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
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

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so items get registered
        GunsmokeBlocks.REGISTRY.register(modEventBus);
        GunsmokeBlockEntities.REGISTRY.register(modEventBus);
        GunsmokeItems.REGISTRY.register(modEventBus);

        if (ModList.get().isLoaded("tconstruct")) {
            GunsmokeItems.TCONSTRUCT.register(modEventBus);
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
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        DataLoader.loadData(event.getServer().getResourceManager());
    }

    @Mod.EventBusSubscriber(modid = Gunsmoke.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class DataGenerators {
        @SubscribeEvent
        public static void gatherData(GatherDataEvent event) {
            DataGenerator generator = event.getGenerator();
            ExistingFileHelper helper = event.getExistingFileHelper();

            generator.addProvider(true, new RecipeGenerator(generator));
            generator.addProvider(true, new ItemModelGenerator(generator, MODID, helper));
        }
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }

        @SubscribeEvent
        public static void registerItemColors(RegisterColorHandlersEvent.Item event)
        {
            for (RegistryObject<Item> regItem : GunsmokeItems.REGISTRY.getEntries()) {
                Item item = regItem.get();
                if (item instanceof GunPartItem) {
                    event.getItemColors().register(
                            (
                                    GunColor::getPartColor
                            ),
                            item
                    );
                } else if (item instanceof RifleItem) {
                    event.getItemColors().register(
                            (
                                    GunColor::getColor
                            ),
                            item
                    );
                } else if (item instanceof RoundItem) {
                    event.getItemColors().register(
                            (
                                    MaterialUtils::getRoundColor
                            ),
                            item
                    );
                }
            }
        }
    }

}
