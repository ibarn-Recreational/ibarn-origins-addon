package com.ibarnstormer.ibarnorigins.mixin;

import com.ibarnstormer.ibarnorigins.client.ModModelLoader;
import com.ibarnstormer.ibarnorigins.entity.IbarnOriginsEntity;
import com.ibarnstormer.ibarnorigins.registry.IOEffects;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class EntityRendererDispatcherMixin {

    @Shadow
    private static void drawFireVertex(MatrixStack.Entry entry, VertexConsumer vertices, float x, float y, float z, float u, float v) {}
    @Shadow
    public Camera camera;

    @Unique
    private void renderSoulFire(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Entity entity, Sprite sprite, Sprite sprite2) {
        matrices.push();
        float f = entity.getWidth() * 1.4F;
        matrices.scale(f, f, f);
        float g = 0.5F;
        float h = 0.0F;
        float i = entity.getHeight() / f;
        float j = 0.0F;
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-this.camera.getYaw()));
        matrices.translate(0.0F, 0.0F, -0.3F + (float)((int)i) * 0.02F);
        float k = 0.0F;
        int l = 0;
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(TexturedRenderLayers.getEntityCutout());

        for(MatrixStack.Entry entry = matrices.peek(); i > 0.0F; ++l) {
            Sprite sprite3 = l % 2 == 0 ? sprite : sprite2;
            float m = sprite3.getMinU();
            float n = sprite3.getMinV();
            float o = sprite3.getMaxU();
            float p = sprite3.getMaxV();
            if (l / 2 % 2 == 0) {
                float q = o;
                o = m;
                m = q;
            }

            drawFireVertex(entry, vertexConsumer, g - 0.0F, 0.0F - j, k, o, p);
            drawFireVertex(entry, vertexConsumer, -g - 0.0F, 0.0F - j, k, m, p);
            drawFireVertex(entry, vertexConsumer, -g - 0.0F, 1.4F - j, k, m, n);
            drawFireVertex(entry, vertexConsumer, g - 0.0F, 1.4F - j, k, o, n);
            i -= 0.45F;
            j -= 0.45F;
            g *= 0.9F;
            k += 0.03F;
        }
        matrices.pop();
    }


    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderer;render(Lnet/minecraft/entity/Entity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"))
    public <E extends Entity> void entityRenderDispatcher$render(E entity, double x, double y, double z, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if(entity instanceof LivingEntity livingEntity && livingEntity.hasStatusEffect(IOEffects.SOUL_FIRE_STRENGTH.get()) && livingEntity.getStatusEffect(IOEffects.SOUL_FIRE_STRENGTH.get()).getAmplifier() >= 1) {
            renderSoulFire(matrices, vertexConsumers, entity, ModModelLoader.SOUL_FIRE_0.getSprite(), ModModelLoader.SOUL_FIRE_1.getSprite());
        }
        else if(entity instanceof IbarnOriginsEntity ioe && ioe.onSoulMageFire()) {
            renderSoulFire(matrices, vertexConsumers, entity, ModModelLoader.SOUL_MAGE_FIRE_0.getSprite(), ModModelLoader.SOUL_MAGE_FIRE_1.getSprite());
        }
    }

    @Inject(method = "renderFire", at = @At("HEAD"), cancellable = true)
    public void entityRenderDispatcher$renderFire(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Entity entity, CallbackInfo ci) {
        if(entity instanceof LivingEntity livingEntity && livingEntity.hasStatusEffect(IOEffects.SOUL_FIRE_STRENGTH.get()) && livingEntity.getStatusEffect(IOEffects.SOUL_FIRE_STRENGTH.get()).getAmplifier() >= 1) {
            ci.cancel();
        }
        else if(entity instanceof IbarnOriginsEntity ioe && ioe.onSoulMageFire()) {
            ci.cancel();
        }
    }


}
