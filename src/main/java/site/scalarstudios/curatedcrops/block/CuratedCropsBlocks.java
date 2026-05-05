package site.scalarstudios.curatedcrops.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import site.scalarstudios.curatedcrops.CuratedCrops;
import site.scalarstudios.curatedcrops.block.custom.BerryBushBlock;
import site.scalarstudios.curatedcrops.block.custom.BerryBushBlockProperties;
import site.scalarstudios.curatedcrops.item.CuratedCropsItems;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public class CuratedCropsBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(CuratedCrops.MODID);

    // Berry Bushes
    public static final DeferredBlock<Block> BLUEBERRY_BUSH = registerBlockWithoutBlockItem("blueberry_bush", p -> new BerryBushBlock(p, () -> CuratedCropsItems.BLUEBERRIES.get()), BerryBushBlockProperties.BERRY_BUSH_PROPERTIES.get());
    public static final DeferredBlock<Block> STRAWBERRY_BUSH = registerBlockWithoutBlockItem("strawberry_bush", p -> new BerryBushBlock(p, () -> CuratedCropsItems.STRAWBERRIES.get()), BerryBushBlockProperties.BERRY_BUSH_PROPERTIES.get());
    public static final DeferredBlock<Block> RASPBERRY_BUSH = registerBlockWithoutBlockItem("raspberry_bush", p -> new BerryBushBlock(p, () -> CuratedCropsItems.RASPBERRIES.get()), BerryBushBlockProperties.BERRY_BUSH_PROPERTIES.get());

    private static DeferredBlock<Block> registerBlock(String name, UnaryOperator<BlockBehaviour.Properties> properties) {
        return registerBlock(name, Block::new, properties);
    }

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Function<BlockBehaviour.Properties, ? extends T> blockFactory, BlockBehaviour.Properties properties) {
        return registerBlock(name, blockFactory, ignored -> properties);
    }

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Function<BlockBehaviour.Properties, ? extends T> blockFactory, UnaryOperator<BlockBehaviour.Properties> properties) {
        DeferredBlock<T> toReturn = BLOCKS.registerBlock(name, blockFactory, properties);
        registerBlockItem(toReturn);
        return toReturn;
    }

    private static <T extends Block> DeferredBlock<T> registerBlockWithoutBlockItem(String name, Function<BlockBehaviour.Properties, ? extends T> blockFactory, BlockBehaviour.Properties properties) {
        return BLOCKS.registerBlock(name, blockFactory, ignored -> properties);
    }

    private static void registerBlockItem(DeferredBlock<? extends Block> block) {
        CuratedCropsItems.ITEMS.registerSimpleBlockItem(block);
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
