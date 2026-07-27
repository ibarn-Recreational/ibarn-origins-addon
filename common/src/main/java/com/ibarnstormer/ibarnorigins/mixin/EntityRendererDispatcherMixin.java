package com.ibarnstormer.ibarnorigins.mixin;

import com.ibarnstormer.ibarnorigins.client.ModModelLoader;
import com.ibarnstormer.ibarnorigins.entity.IbarnOriginsEntity;
import com.ibarnstormer.ibarnorigins.entity.SoulFireBallEntity;
import com.ibarnstormer.ibarnorigins.registry.IOEffects;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class EntityRendererDispatcherMixin {

    @Shadow
    private static void fireVertex(PoseStack.Pose matrixEntry, VertexConsumer buffer, float x, float y, float z, float texU, float texV) {}
    @Shadow
    private Quaternionf cameraOrientation;

    @Unique
    private void renderSoulFire(PoseStack poseStack, MultiBufferSource buffer, Entity entity, Quaternionf quaternion, TextureAtlasSprite textureatlassprite, TextureAtlasSprite textureatlassprite1) {
        poseStack.pushPose();
        float f = entity.getBbWidth() * 1.4F;
        poseStack.scale(f, f, f);
        float f1 = 0.5F;
        float f2 = 0.0F;
        float f3 = entity.getBbHeight() / f;
        float f4 = 0.0F;
        poseStack.mulPose(quaternion);
        poseStack.translate(0.0F, 0.0F, 0.3F - (float)((int)f3) * 0.02F);
        float f5 = 0.0F;
        int i = 0;
        VertexConsumer vertexconsumer = buffer.getBuffer(Sheets.cutoutBlockSheet());

        for(PoseStack.Pose posestack$pose = poseStack.last(); f3 > 0.0F; ++i) {
            TextureAtlasSprite textureatlassprite2 = i % 2 == 0 ? textureatlassprite : textureatlassprite1;
            float f6 = textureatlassprite2.getU0();
            float f7 = textureatlassprite2.getV0();
            float f8 = textureatlassprite2.getU1();
            float f9 = textureatlassprite2.getV1();
            if (i / 2 % 2 == 0) {
                float f10 = f8;
                f8 = f6;
                f6 = f10;
            }

            fireVertex(posestack$pose, vertexconsumer, -f1 - 0.0F, 0.0F - f4, f5, f8, f9);
            fireVertex(posestack$pose, vertexconsumer, f1 - 0.0F, 0.0F - f4, f5, f6, f9);
            fireVertex(posestack$pose, vertexconsumer, f1 - 0.0F, 1.4F - f4, f5, f6, f7);
            fireVertex(posestack$pose, vertexconsumer, -f1 - 0.0F, 1.4F - f4, f5, f8, f7);
            f3 -= 0.45F;
            f4 -= 0.45F;
            f1 *= 0.9F;
            f5 -= 0.03F;
        }

        poseStack.popPose();
    }


    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;render(Lnet/minecraft/world/entity/Entity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"))
    public <E extends Entity> void entityRenderDispatcher$render(E entity, double x, double y, double z, float rotationYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        if(entity instanceof LivingEntity livingEntity && livingEntity.hasEffect(IOEffects.SOUL_FIRE_STRENGTH.getRef()) && livingEntity.getEffect(IOEffects.SOUL_FIRE_STRENGTH.getRef()).getAmplifier() >= 1) {
            renderSoulFire(poseStack, buffer, entity, Mth.rotationAroundAxis(Mth.Y_AXIS, this.cameraOrientation, new Quaternionf()), ModModelLoader.SOUL_FIRE_0.sprite(), ModModelLoader.SOUL_FIRE_1.sprite());
        }
        else if((entity instanceof IbarnOriginsEntity ioe && ioe.onSoulMageFire()) || entity instanceof SoulFireBallEntity) {
            renderSoulFire(poseStack, buffer, entity, Mth.rotationAroundAxis(Mth.Y_AXIS, this.cameraOrientation, new Quaternionf()), ModModelLoader.SOUL_MAGE_FIRE_0.sprite(), ModModelLoader.SOUL_MAGE_FIRE_1.sprite());
        }
    }

    @Inject(method = "renderFlame", at = @At("HEAD"), cancellable = true)
    public void entityRenderDispatcher$renderFire(PoseStack poseStack, MultiBufferSource buffer, Entity entity, Quaternionf quaternion, CallbackInfo ci) {
        if(entity instanceof LivingEntity livingEntity && livingEntity.hasEffect(IOEffects.SOUL_FIRE_STRENGTH.getRef()) && livingEntity.getEffect(IOEffects.SOUL_FIRE_STRENGTH.getRef()).getAmplifier() >= 1) {
            ci.cancel();
        }
        else if(entity instanceof IbarnOriginsEntity ioe && ioe.onSoulMageFire()) {
            ci.cancel();
        }
    }


}
