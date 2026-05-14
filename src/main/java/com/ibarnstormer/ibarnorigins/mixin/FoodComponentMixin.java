package com.ibarnstormer.ibarnorigins.mixin;

import com.ibarnstormer.ibarnorigins.entity.IbarnOriginsEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

@Mixin(FoodProperties.class)
public class FoodComponentMixin {

    @Inject(method = "onConsume", at = @At("RETURN"))
    public void foodComponent$onConsume(Level world, LivingEntity user, ItemStack stack, Consumable consumable, CallbackInfo ci) {
        if(user instanceof IbarnOriginsEntity ioe && ioe.isSoulMage()) {
            try {
                FoodProperties food = stack.getItem().components().get(DataComponents.FOOD);

                if(food != null) {
                    List<Recipe<?>> recipes = (List<Recipe<?>>) world.recipeAccess().getSynchronizedRecipes().recipes().stream().filter(r -> r.value() == stack.getItem()).map(r -> r.value()).toList();
                    TagKey<Item> meatTag = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("origins", "meat"));
                    boolean hasMeatInRecipe = recipes.stream().anyMatch(r -> r.placementInfo().ingredients().stream().anyMatch(i -> i.items().anyMatch(is -> is.is(meatTag))));

                    if (!stack.is(meatTag) && !hasMeatInRecipe && !world.isClientSide()) {
                        int amount = Math.min((food.nutrition() + Math.round(food.saturation())) * 2, Integer.MAX_VALUE);
                        ExperienceOrb orb = new ExperienceOrb(world, user.getX(), user.getY(), user.getZ(), amount);
                        world.addFreshEntity(orb);
                    }
                }
            }
            catch(Exception ignored) {}
        }
    }

}
