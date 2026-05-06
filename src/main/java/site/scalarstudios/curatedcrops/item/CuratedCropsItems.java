package site.scalarstudios.curatedcrops.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import site.scalarstudios.curatedcrops.CuratedCrops;
import site.scalarstudios.curatedcrops.block.CuratedCropsBlocks;

import java.util.function.Function;
import java.util.function.Supplier;

public class CuratedCropsItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CuratedCrops.MODID);

    // Berries
    public static final DeferredItem<Item> BLUEBERRIES = ITEMS.registerItem("blueberries", createBlockItemWithCustomItemName(() -> CuratedCropsBlocks.BLUEBERRY_BUSH.get()), p -> p.food(CuratedCropsFoods.BERRY_FOOD_PROPERTIES));
    public static final DeferredItem<Item> RASPBERRIES = ITEMS.registerItem("raspberries", createBlockItemWithCustomItemName(() -> CuratedCropsBlocks.RASPBERRY_BUSH.get()), p -> p.food(CuratedCropsFoods.BERRY_FOOD_PROPERTIES));
    public static final DeferredItem<Item> STRAWBERRIES = ITEMS.registerItem("strawberries", createBlockItemWithCustomItemName(() -> CuratedCropsBlocks.STRAWBERRY_BUSH.get()), p -> p.food(CuratedCropsFoods.BERRY_FOOD_PROPERTIES));

    // Crops
    public static final DeferredItem<Item> CORN = ITEMS.registerItem("corn", createBlockItemWithCustomItemName(() -> CuratedCropsBlocks.CORN.get()), p -> p.food(CuratedCropsFoods.BASIC_VEGETABLE_FOOD_PROPERTIES));

    private static Function<Item.Properties, Item> createBlockItemWithCustomItemName(Supplier<? extends Block> block) {
        return p -> new BlockItem(block.get(), p.useItemDescriptionPrefix());
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
