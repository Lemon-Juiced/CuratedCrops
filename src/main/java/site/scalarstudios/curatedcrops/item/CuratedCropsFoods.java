package site.scalarstudios.curatedcrops.item;

import net.minecraft.world.food.FoodProperties;

public class CuratedCropsFoods {
    // Generic Foods
    public static final FoodProperties BERRY_FOOD_PROPS = new FoodProperties.Builder().nutrition(2).saturationModifier(0.1F).build();
    public static final FoodProperties BASIC_VEGETABLE_FOOD_PROPS = new FoodProperties.Builder().nutrition(3).saturationModifier(0.6F).build();
    public static final FoodProperties COOKED_VEGETABLE_FOOD_PROPS = new FoodProperties.Builder().nutrition(4).saturationModifier(0.7F).build();

    // Specific Foods
    public static final FoodProperties CHEESE_FOOD_PROPS = new FoodProperties.Builder().nutrition(5).saturationModifier(0.8F).build();
    public static final FoodProperties TACO_FOOD_PROPS = new FoodProperties.Builder().nutrition(6).saturationModifier(0.9F).build();
    public static final FoodProperties TORTILLA_FOOD_PROPS = new FoodProperties.Builder().nutrition(4).saturationModifier(0.6F).build();
}
