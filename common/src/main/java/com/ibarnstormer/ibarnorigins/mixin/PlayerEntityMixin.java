package com.ibarnstormer.ibarnorigins.mixin;

import com.ibarnstormer.ibarnorigins.entity.IbarnOriginsEntity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.List;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {


    @Inject(method = "eatFood", at = @At("RETURN"))
    public void playerEntity$eatFood(World world, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if(player instanceof IbarnOriginsEntity ioe && ioe.isSoulMage()) {
            try {
                List<Recipe<?>> recipes = world.getRecipeManager().values().stream().filter(r -> r.createIcon().getItem() == stack.getItem()).toList();
                TagKey<Item> meatTag = TagKey.of(RegistryKeys.ITEM, Identifier.of("origins", "meat"));
                boolean hasMeatInRecipe = recipes.stream().anyMatch(r -> r.getIngredients().stream().anyMatch(i -> Arrays.stream(i.getMatchingStacks()).anyMatch(is -> is.isIn(meatTag))));
                FoodComponent food = stack.getItem().getFoodComponent();

                if (!stack.isIn(meatTag) && !hasMeatInRecipe && !(food != null && food.isMeat()) && !world.isClient() && stack.getItem().getFoodComponent() != null) {
                    int amount = Math.min((stack.getItem().getFoodComponent().getHunger() + Math.round(stack.getItem().getFoodComponent().getSaturationModifier())) * 2, Integer.MAX_VALUE);
                    ExperienceOrbEntity orb = new ExperienceOrbEntity(world, player.getX(), player.getY(), player.getZ(), amount);
                    world.spawnEntity(orb);
                }
            }
            catch(Exception ignored) {}
        }
    }



}
