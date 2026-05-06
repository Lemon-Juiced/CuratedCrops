package site.scalarstudios.curatedcrops.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import site.scalarstudios.curatedcrops.item.CuratedCropsItems;

/**
 * Corn supports wild generation by allowing fully-grown plants on grass-like soil.
 * Young stages still behave like normal crops and require crop-supporting soil.
 */
public class CornBlock extends FourStageCropBlock {
    public static final MapCodec<CornBlock> CODEC = simpleCodec(CornBlock::new);

    public CornBlock(BlockBehaviour.Properties properties) {
        super(properties, CuratedCropsItems.CORN);
    }

    @Override
    public MapCodec<? extends CropBlock> codec() {
        return CODEC;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        if (this.isMaxAge(state)) {
            BlockState below = level.getBlockState(pos.below());
            if (below.is(Blocks.GRASS_BLOCK) || below.is(Blocks.DIRT) || below.is(Blocks.COARSE_DIRT)) {
                return hasSufficientLight(level, pos);
            }
        }
        return super.canSurvive(state, level, pos);
    }
}

