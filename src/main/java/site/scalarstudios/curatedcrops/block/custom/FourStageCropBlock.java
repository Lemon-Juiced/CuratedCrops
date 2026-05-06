package site.scalarstudios.curatedcrops.block.custom;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

/**
 * A generic 4-stage crop block (AGE 0–3).
 * Pass a seed-item at construction time so you can reuse this class directly for simple crops without making a dedicated subclass per crop:
 * Example: TURNIP = registerBlock("turnip", p -> new FourStageCropBlock(p, CuratedCropsItems.TURNIP));
 *
 * @author Lemon_Juiced
 */
public class FourStageCropBlock extends CropBlock {

    public static final MapCodec<FourStageCropBlock> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    propertiesCodec(),
                    BuiltInRegistries.ITEM.byNameCodec()
                            .fieldOf("seed_item")
                            .forGetter(b -> b.seedItem.get())
            ).apply(instance, (props, item) -> new FourStageCropBlock(props, () -> item)));

    public static final int MAX_AGE = 3;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;

    private static final VoxelShape[] SHAPES = Block.boxes(MAX_AGE, age -> Block.column(16.0, 0.0, 4 + age * 4));

    private final Supplier<Item> seedItem;

    public FourStageCropBlock(BlockBehaviour.Properties properties, Supplier<Item> seedItem) {
        super(properties);
        this.seedItem = seedItem;
    }

    @Override
    public MapCodec<? extends CropBlock> codec() {
        return CODEC;
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return MAX_AGE;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES[this.getAge(state)];
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return seedItem.get();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }
}

