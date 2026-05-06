package site.scalarstudios.curatedcrops.item;

import net.minecraft.world.food.FoodProperties;

public class CuratedCropsFoods {
    public static final FoodProperties BERRY_FOOD_PROPERTIES = new FoodProperties.Builder().nutrition(2).saturationModifier(0.1F).build();

    public static final FoodProperties BASIC_VEGETABLE_FOOD_PROPERTIES = new FoodProperties.Builder().nutrition(3).saturationModifier(0.6F).build();
    public static final FoodProperties COOKED_VEGETABLE_FOOD_PROPERTIES = new FoodProperties.Builder().nutrition(4).saturationModifier(0.7F).build();
}
