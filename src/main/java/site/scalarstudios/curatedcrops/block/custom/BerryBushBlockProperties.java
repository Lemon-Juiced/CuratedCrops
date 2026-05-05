package site.scalarstudios.curatedcrops.block.custom;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.function.Supplier;

/**
 * Common properties for berry bushes in Curated Crops.
 * This is a descendant from older code, but extensible (will keep unless determined to be obsolete).
 *
 * @author Lemon_Juiced
 */
public class BerryBushBlockProperties {
    public static final String PLACEABLE_ON_DIRT = "dirt";
    public static final String PLACEABLE_ON_CRIMSON = "crimson";
    public static final String PLACEABLE_ON_SOUL = "soul";
    public static final String PLACEABLE_ON_WARPED = "warped";
    public static final String PLACEABLE_ON_NETHERRACK = "netherrack";

    public static final Supplier<BlockBehaviour.Properties> BERRY_BUSH_PROPERTIES = () ->
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.PLANT)
                    .randomTicks()
                    .noCollision()
                    .sound(SoundType.SWEET_BERRY_BUSH)
                    .pushReaction(PushReaction.DESTROY);
}
