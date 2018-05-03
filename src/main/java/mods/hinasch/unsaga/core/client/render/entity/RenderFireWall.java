package mods.hinasch.unsaga.core.client.render.entity;

import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.core.client.model.ModelFireWall;
import mods.hinasch.unsaga.core.entity.passive.EntityFireWall;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class RenderFireWall extends Render<EntityFireWall>{

	public static final ResourceLocation RES = new ResourceLocation(UnsagaMod.MODID,"textures/entity/firewall.png");
	public static final ResourceLocation RES2 = new ResourceLocation(UnsagaMod.MODID,"textures/entity/firewall_2.png");
	ModelFireWall modelFireWall = new ModelFireWall();
//	ModelCreeper creeper = new ModelCreeper();

	public RenderFireWall(RenderManager renderManager) {
		super(renderManager);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@Override
    public void doRender(EntityFireWall entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
//        super.doRender(entity, x, y, z, entityYaw, partialTicks);

//		UnsagaMod.logger.trace("called", "called");
		float scale = 2.0F;
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
		this.bindEntityTexture(entity);

        BlockPos pos = UnsagaMod.proxy.getDebugPos(0);
        GlStateManager.translate((float)x,(float) y +1.0F,(float) z);

//        GlStateManager.translate(0,-2.5F,0);
        GlStateManager.scale(1.0F,scale,1.0F);
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
//        GlStateManager.enableAlpha();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
//        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//        GlStateManager.color(1.0F, 1.0F, 1.0F,1.0F);
//        GlStateManager.rotate(180F,1.0F,0,0);

        int i = 61680;
        int j = i % 65536;
        int k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j, (float)k);
        this.modelFireWall.render(entity, 0, 0, 0, 0,0,0.0625F);
//        this.creeper.render(entity, 0, 0, 0, entityYaw, 0, 1.0F);
        GlStateManager.disableBlend();
//        GlStateManager.disableAlpha();
        GlStateManager.enableLighting();
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
    }


	@Override
	protected ResourceLocation getEntityTexture(EntityFireWall entity) {
		if(entity.ticksExisted % 10 <5){
			return RES2;
		}
		return RES;
	}

}
