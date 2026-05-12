package com.ibarnstormer.ibarnorigins.mixin;

import com.ibarnstormer.ibarnorigins.entity.IbarnOriginsEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(FoodComponent.class)
public class FoodComponentMixin {

    @Inject(method = "onConsume", at = @At("RETURN"))
    public void foodComponent$onConsume(World world, LivingEntity user, ItemStack stack, ConsumableComponent consumable, CallbackInfo ci) {
        if(user instanceof IbarnOriginsEntity ioe && ioe.isSoulMage()) {
            try {
                FoodComponent food = stack.getItem().getComponents().get(DataComponentTypes.FOOD);

                if(food != null) {
                    List<Recipe<?>> recipes = (List<Recipe<?>>) world.getRecipeManager().getSynchronizedRecipes().recipes().stream().filter(r -> r.value() == stack.getItem()).map(r -> r.value()).toList();
                    TagKey<Item> meatTag = TagKey.of(RegistryKeys.ITEM, Identifier.of("origins", "meat"));
                    boolean hasMeatInRecipe = recipes.stream().anyMatch(r -> r.getIngredientPlacement().getIngredients().stream().anyMatch(i -> i.getMatchingItems().anyMatch(is -> is.isIn(meatTag))));

                    if (!stack.isIn(meatTag) && !hasMeatInRecipe && !world.isClient()) {
                        int amount = Math.min((food.nutrition() + Math.round(food.saturation())) * 2, Integer.MAX_VALUE);
                        ExperienceOrbEntity orb = new ExperienceOrbEntity(world, user.getX(), user.getY(), user.getZ(), amount);
                        world.spawnEntity(orb);
                    }
                }
            }
            catch(Exception ignored) {}
        }
    }

}
