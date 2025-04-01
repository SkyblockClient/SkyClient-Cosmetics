package co.skyclient.scc.mixins;

import co.skyclient.scc.SkyclientCosmetics;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RendererLivingEntity.class)
public abstract class RendererLivingEntityMixin_AprilFools<T extends EntityLivingBase> extends Render<T> {

    protected RendererLivingEntityMixin_AprilFools(RenderManager renderManager) {
        super(renderManager);
    }

    @Inject(method = "rotateCorpse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;rotate(FFFF)V", shift = At.Shift.AFTER))
    public void scc$rotateCorpse(T entity, float p_77043_2_, float p_77043_3_, float partialTicks, CallbackInfo ci) {
        if (entity != null && SkyclientCosmetics.aprilTrollUntil != 0 && System.currentTimeMillis() < SkyclientCosmetics.aprilTrollUntil) {
            scc$dinnerboneRotation(entity);
        }
    }

    @Unique
    private static void scc$dinnerboneRotation(EntityLivingBase entity) {
        GlStateManager.translate(0.0f, entity.height + 0.1f, 0.0f);
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
    }

}