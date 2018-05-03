package mods.hinasch.unsaga.core.client.render.entity;

import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.core.entity.mob.EntityShadowServant;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderShadowServant extends RenderBiped<EntityShadowServant>{

	public static final ResourceLocation SHADOW_TEXTURE = new ResourceLocation(UnsagaMod.MODID,"textures/entity/shadow.png");
	public static ModelBiped model = new ModelBiped();

	public RenderShadowServant(RenderManager renderManagerIn) {
		super(renderManagerIn, model, 1.0F);
		// TODO 自動生成されたコンストラクター・スタブ
	}


	@Override
    protected ResourceLocation getEntityTexture(EntityShadowServant entity){
    	return SHADOW_TEXTURE;
    }

	@Override
    public void doRender(EntityShadowServant entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
		GlStateManager.enableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.color(1, 1, 1,0.6F);
    	GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
    }

}
