package site.scalarstudios.curatedcrops.block.custom;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.CommonHooks;
import site.scalarstudios.curatedcrops.block.custom.properties.BerryBushBlockProperties;

import java.util.function.Supplier;

public class BerryBushBlock extends VegetationBlock implements BonemealableBlock {
    public static final MapCodec<BerryBushBlock> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    propertiesCodec(),
                    BuiltInRegistries.ITEM.byNameCodec()
                            .fieldOf("berry_item")
                            .forGetter(b -> b.berryItem.get()),
                    net.minecraft.util.ExtraCodecs.NON_EMPTY_STRING
                            .optionalFieldOf("placeable_on", BerryBushBlockProperties.PLACEABLE_ON_DIRT)
                            .forGetter(b -> b.placeableOn)
            ).apply(instance, (props, item, placeableOn) ->
                    new BerryBushBlock(props, () -> item, placeableOn)));

    private static final float HURT_SPEED_THRESHOLD = 0.003F;
    public static final int MAX_AGE = 3;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    private static final VoxelShape SAPLING_SHAPE    = Block.column(10.0, 0.0,  8.0);
    private static final VoxelShape MID_GROWTH_SHAPE = Block.column(14.0, 0.0, 16.0);

    private final Supplier<Item> berryItem;
    private final String placeableOn;

    public BerryBushBlock(Properties properties, Supplier<Item> berryItem) {
        this(properties, berryItem, BerryBushBlockProperties.PLACEABLE_ON_DIRT);
    }

    public BerryBushBlock(Properties properties, Supplier<Item> berryItem, String placeableOn) {
        super(properties);
        this.berryItem = berryItem;
        this.placeableOn = placeableOn;
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
    }

    @Override
    public MapCodec<? extends VegetationBlock> codec() {
        return CODEC;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return switch (this.placeableOn) {
            case BerryBushBlockProperties.PLACEABLE_ON_CRIMSON    -> state.is(Blocks.CRIMSON_NYLIUM);
            case BerryBushBlockProperties.PLACEABLE_ON_SOUL       -> state.is(Blocks.SOUL_SAND) || state.is(Blocks.SOUL_SOIL);
            case BerryBushBlockProperties.PLACEABLE_ON_WARPED     -> state.is(Blocks.WARPED_NYLIUM);
            case BerryBushBlockProperties.PLACEABLE_ON_NETHERRACK -> state.is(Blocks.NETHERRACK);
            // Default: treat grass-style soil the same as dirt plus farmland.
            default -> state.is(BlockTags.DIRT)
                    || state.is(Blocks.GRASS_BLOCK)
                    || state.is(Blocks.MYCELIUM)
                    || state.getBlock() instanceof FarmlandBlock;
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(AGE)) {
            case 0 -> SAPLING_SHAPE;
            case MAX_AGE -> super.getShape(state, level, pos, context);
            default -> MID_GROWTH_SHAPE;
        };
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return state.getValue(AGE) < MAX_AGE;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int age = state.getValue(AGE);
        if (age < MAX_AGE
                && level.getRawBrightness(pos.above(), 0) >= 9
                && CommonHooks.canCropGrow(level, pos, state, random.nextInt(5) == 0)) {
            BlockState grown = state.setValue(AGE, age + 1);
            level.setBlock(pos, grown, 2);
            CommonHooks.fireCropGrowPost(level, pos, state);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(grown));
        }
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
        if (entity instanceof LivingEntity && !entity.is(EntityType.FOX) && !entity.is(EntityType.BEE)) {
            entity.makeStuckInBlock(state, new Vec3(0.8F, 0.75, 0.8F));
            if (level instanceof ServerLevel serverLevel && state.getValue(AGE) != 0) {
                Vec3 movement = entity.isClientAuthoritative()
                        ? entity.getKnownMovement()
                        : entity.oldPosition().subtract(entity.position());
                if (movement.horizontalDistanceSqr() > 0.0) {
                    double xs = Math.abs(movement.x());
                    double zs = Math.abs(movement.z());
                    if (xs >= HURT_SPEED_THRESHOLD || zs >= HURT_SPEED_THRESHOLD) {
                        entity.hurtServer(serverLevel, level.damageSources().sweetBerryBush(), 1.0F);
                    }
                }
            }
        }
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        boolean ripe = state.getValue(AGE) == MAX_AGE;
        return !ripe && stack.is(Items.BONE_MEAL)
                ? InteractionResult.PASS
                : super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        int age = state.getValue(AGE);
        boolean ripe = age == MAX_AGE;
        if (age > 1) {
            if (level instanceof ServerLevel serverLevel) {
                int drops = 1 + serverLevel.getRandom().nextInt(2);
                popResource(serverLevel, pos, new ItemStack(berryItem.get(), drops + (ripe ? 1 : 0)));
                serverLevel.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + serverLevel.getRandom().nextFloat() * 0.4F);
                BlockState reset = state.setValue(AGE, 1);
                serverLevel.setBlock(pos, reset, 2);
                serverLevel.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, reset));
            }
            return InteractionResult.SUCCESS;
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
        return new ItemStack(berryItem.get());
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return state.getValue(AGE) < MAX_AGE;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        int newAge = Math.min(MAX_AGE, state.getValue(AGE) + 1);
        level.setBlock(pos, state.setValue(AGE, newAge), 2);
    }
}
