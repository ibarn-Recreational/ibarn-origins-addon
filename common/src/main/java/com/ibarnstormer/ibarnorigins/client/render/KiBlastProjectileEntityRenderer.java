package com.ibarnstormer.ibarnorigins.client.render;

import com.ibarnstormer.ibarnorigins.entity.KiBlastProjectileEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class KiBlastProjectileEntityRenderer extends EntityRenderer<KiBlastProjectileEntity> {

    private static final Identifier TEXTURE = new Identifier("ibarnorigins:textures/entity/ki_blast_entity.png");
    public static final Identifier SOUL_BEAM_TEXTURE = new Identifier("ibarnorigins:textures/entity/soul_laser.png");
    private static final RenderLayer LAYER;
    private static final RenderLayer SOUL_BEAM_LAYER;

    public KiBlastProjectileEntityRenderer(EntityRendererFactory.Context dispatcher) {
        super(dispatcher);
    }

    protected int getBlockLight(KiBlastProjectileEntity KiBlastProjectileEntity, BlockPos blockPos) {
        return 15;
    }

    public void render(KiBlastProjectileEntity kiBlastProjectileEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        float o = MathHelper.sin(((float)kiBlastProjectileEntity.age + g) * 2) * 0.1F;
        matrixStack.scale(1.0F + o, 1.0F + o, 1.0F + o);
        matrixStack.translate(0.0F, -0.125F, 0.0F);
        matrixStack.multiply(this.dispatcher.getRotation());
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
        MatrixStack.Entry entry = matrixStack.peek();
        Matrix4f matrix4f = entry.getPositionMatrix();
        Matrix3f matrix3f = entry.getNormalMatrix();
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(LAYER);
        produceVertex(vertexConsumer, matrix4f, matrix3f, i, 0.0F, 0, 0, 1);
        produceVertex(vertexConsumer, matrix4f, matrix3f, i, 1.0F, 0, 1, 1);
        produceVertex(vertexConsumer, matrix4f, matrix3f, i, 1.0F, 1, 1, 0);
        produceVertex(vertexConsumer, matrix4f, matrix3f, i, 0.0F, 1, 0, 0);
        matrixStack.pop();

        LivingEntity owner = kiBlastProjectileEntity.getCachedOwner();

        if(owner != null) {
            matrixStack.push();
            float x = (float) (MathHelper.lerp(g, owner.prevX, owner.getX()) - MathHelper.lerp(g, kiBlastProjectileEntity.prevX, kiBlastProjectileEntity.getX()));
            float y = (float) (MathHelper.lerp(g, owner.prevY, owner.getY()) + owner.getBoundingBox().getYLength() / 2 - MathHelper.lerp(g, kiBlastProjectileEntity.prevY, kiBlastProjectileEntity.getY()));
            float z = (float) (MathHelper.lerp(g, owner.prevZ, owner.getZ()) - MathHelper.lerp(g, kiBlastProjectileEntity.prevZ, kiBlastProjectileEntity.getZ()));
            renderBeam(x, y, z, g, owner.age, matrixStack, vertexConsumerProvider, i);
            matrixStack.pop();
        }
        super.render(kiBlastProjectileEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    private static void produceVertex(VertexConsumer vertexConsumer, Matrix4f modelMatrix, Matrix3f normalMatrix, int light, float x, int y, int textureU, int textureV) {
        vertexConsumer.vertex(modelMatrix, x - 0.5F, (float)y - 0.25F, 0.0F).color(255, 255, 255, 255).texture((float)textureU, (float)textureV).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normalMatrix, 0.0F, 1.0F, 0.0F).next();
    }

    public static void renderBeam(float dx, float dy, float dz, float tickDelta, int age, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        float f = MathHelper.sqrt(dx * dx + dz * dz);
        float g = MathHelper.sqrt(dx * dx + dy * dy + dz * dz);
        matrices.push();
        matrices.translate(0.0D, 0.2D, 0.0D);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotation((float)(-Math.atan2((double)dz, (double)dx)) - 1.5707964F));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotation((float)(-Math.atan2((double)f, (double)dy)) - 1.5707964F));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotation(((float)age + tickDelta) * -0.08F));
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(SOUL_BEAM_LAYER);
        float h = 0.0F + ((float)age + tickDelta) * 0.15F;
        float i = MathHelper.sqrt(dx * dx + dy * dy + dz * dz) / 32.0F + ((float)age + tickDelta) * 0.15F;
        float k = 0.0F;
        float l = 0.75F;
        float m = 0.0F;
        MatrixStack.Entry entry = matrices.peek();
        Matrix4f matrix4f = entry.getPositionMatrix();
        Matrix3f matrix3f = entry.getNormalMatrix();

        for(int n = 1; n <= 4; ++n) {
            float o = MathHelper.sin((float)n * 6.2831855F / 4.0F) * 0.75F;
            float p = MathHelper.cos((float)n * 6.2831855F / 4.0F) * 0.75F;
            float q = (float)n / 4.0F;
            vertexConsumer.vertex(matrix4f, k * 0.25F, l * 0.25F, 0.0F).color(255, 255, 255, 255).texture(m, h).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix3f, 0.0F, -1.0F, 0.0F).next();
            vertexConsumer.vertex(matrix4f, k * 0.25F, l * 0.25F, g).color(255, 255, 255, 255).texture(m, i).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix3f, 0.0F, -1.0F, 0.0F).next();
            vertexConsumer.vertex(matrix4f, o * 0.25F, p * 0.25F, g).color(255, 255, 255, 255).texture(q, i).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix3f, 0.0F, -1.0F, 0.0F).next();
            vertexConsumer.vertex(matrix4f, o * 0.25F, p * 0.25F, 0.0F).color(255, 255, 255, 255).texture(q, h).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(matrix3f, 0.0F, -1.0F, 0.0F).next();
            k = o;
            l = p;
            m = q;
        }

        matrices.pop();
    }

    @Override
    public Identifier getTexture(KiBlastProjectileEntity entity) {
        return TEXTURE;
    }

    static {
        LAYER = RenderLayer.getBeaconBeam(TEXTURE, false);
        SOUL_BEAM_LAYER = RenderLayer.getBeaconBeam(SOUL_BEAM_TEXTURE, false);
    }

}
