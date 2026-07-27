package com.ibarnstormer.ibarnorigins.mixin;

import com.ibarnstormer.ibarnorigins.entity.IbarnOriginsEntity;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.List;

@Mixin(Player.class)
public class PlayerEntityMixin {


    @Inject(method = "eat", at = @At("RETURN"))
    public void playerEntity$eatFood(Level level, ItemStack stack, FoodProperties foodProperties, CallbackInfoReturnable<net.minecraft.world.item.ItemStack> cir) {
        Player player = (Player) (Object) this;
        if(player instanceof IbarnOriginsEntity ioe && ioe.isSoulMage()) {
            try {
                FoodProperties food = stack.getItem().components().get(DataComponents.FOOD);

                if(food != null) {
                    List<Recipe<?>> recipes = (List<Recipe<?>>) level.getRecipeManager().getRecipes().stream().filter(r -> r.value() == stack.getItem()).map(RecipeHolder::value).toList();
                    TagKey<Item> meatTag = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("origins", "meat"));
                    boolean hasMeatInRecipe = recipes.stream().anyMatch(r -> r.getIngredients().stream().anyMatch(i -> Arrays.stream(i.getItems()).anyMatch(is -> is.is(meatTag))));

                    if (!stack.is(meatTag) && !hasMeatInRecipe && !level.isClientSide) {
                        int amount = Math.min((food.nutrition() + Math.round(food.saturation())) * 2, Integer.MAX_VALUE);
                        ExperienceOrb orb = new ExperienceOrb(level, player.getX(), player.getY(), player.getZ(), amount);
                        level.addFreshEntity(orb);
                    }
                }
            }
            catch(Exception ignored) {}
        }
    }



}
