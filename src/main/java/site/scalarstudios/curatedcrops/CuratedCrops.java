package site.scalarstudios.curatedcrops;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import site.scalarstudios.curatedcrops.block.CuratedCropsBlocks;
import site.scalarstudios.curatedcrops.item.CuratedCropsCreativeTab;
import site.scalarstudios.curatedcrops.item.CuratedCropsItems;

@Mod(CuratedCrops.MODID)
public class CuratedCrops {
    public static final String MODID = "curatedcrops";

    public CuratedCrops(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        // Register Items and Blocks
        CuratedCropsBlocks.register(modEventBus);
        CuratedCropsItems.register(modEventBus);

        // Register Creative Tab
        CuratedCropsCreativeTab.register(modEventBus);
        modEventBus.addListener(CuratedCropsCreativeTab::registerTabs);

        NeoForge.EVENT_BUS.register(this);
    }

    private void commonSetup(FMLCommonSetupEvent event) {}

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {}
}
