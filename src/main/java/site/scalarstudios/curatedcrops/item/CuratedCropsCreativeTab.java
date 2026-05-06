package site.scalarstudios.curatedcrops.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import site.scalarstudios.curatedcrops.CuratedCrops;

public class CuratedCropsCreativeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CuratedCrops.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CURATED_CROPS_TAB = CREATIVE_MODE_TABS.register("curated_crops", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.curatedcrops.title"))
            .icon(() -> new ItemStack(CuratedCropsItems.BLUEBERRIES.get()))
            .build());

    public static void registerTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == CURATED_CROPS_TAB.get()) {
            // Berries
            event.accept(CuratedCropsItems.BLUEBERRIES.get());
            event.accept(CuratedCropsItems.STRAWBERRIES.get());
            event.accept(CuratedCropsItems.RASPBERRIES.get());

            // Crops
            event.accept(CuratedCropsItems.CORN.get());
        }
    }

    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
