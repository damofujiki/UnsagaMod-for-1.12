package mods.hinasch.unsaga.core.client.render.projectile;

import mods.hinasch.lib.client.ClientHelper;
import mods.hinasch.unsaga.core.entity.projectile.EntityMagicBall;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class RenderMagicBall extends Render<EntityMagicBall>{

	public RenderMagicBall(RenderManager renderManager) {
		super(renderManager);
		// TODO 自動生成されたコンストラクター・スタブ
	}
	@Override
    public void doRender(EntityMagicBall entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        if(entity.getMagicBallType().getTextureItem()!=null){
        	TextureAtlasSprite texture = ClientHelper.getTextureAtlasSprite(entity.getMagicBallType().getTextureItem());
            this.renderSquare(texture, entity, x, y, z, 0, 0, 1.0F, 1.0F, 1.0F);
        }

    }
	public void renderSquare(TextureAtlasSprite icon,EntityMagicBall entity,double x,double y,double z,float u,float v,float mu,float mv,float f2){

		GlStateManager.pushMatrix();
		this.bindEntityTexture(entity);
		GlStateManager.translate((float)x, (float)y, (float)z);
        GlStateManager.disableLighting();
		GlStateManager.scale(f2 / 1.0F, f2 / 1.0F, f2 / 1.0F);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexBuffer = tessellator.getBuffer();
		float f3 = u;
		float f4 = mu;
		float f5 = v;
		float f6 = mv;
		float f7 = 1.0F;
		float f8 = 0.5F;
		float f9 = 0.25F;
		if(icon!=null){
			f3 = icon.getMinU();
			f4 = icon.getMaxU();
			f5 = icon.getMinV();
			f6 = icon.getMaxV();
		}



        GlStateManager.enableRescaleNormal();
		GlStateManager.rotate(180.0F - this.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-this.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
        float lastx = OpenGlHelper.lastBrightnessX;
        float lasty = OpenGlHelper.lastBrightnessY;
		if(entity.isGlowingInRender()){
			GlStateManager.disableBlend();
	        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);

		}

		vertexBuffer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
		vertexBuffer.pos(-0.5D, -0.25D, 0.0D).tex((double)f3, (double)f6).normal(0.0F, 1.0F, 0.0F).endVertex();
		vertexBuffer.pos(0.5D, -0.25D, 0.0D).tex((double)f4, (double)f6).normal(0.0F, 1.0F, 0.0F).endVertex();
		vertexBuffer.pos(0.5D, 0.75D, 0.0D).tex((double)f4, (double)f5).normal(0.0F, 1.0F, 0.0F).endVertex();
		vertexBuffer.pos(-0.5D, 0.75D, 0.0D).tex((double)f3, (double)f5).normal(0.0F, 1.0F, 0.0F).endVertex();
		tessellator.draw();

		if(entity.isGlowingInRender()){
	        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastx, lasty);
			GlStateManager.enableBlend();
		}
        GlStateManager.disableRescaleNormal();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
	}
	@Override
	protected ResourceLocation getEntityTexture(EntityMagicBall entity) {
		// TODO 自動生成されたメソッド・スタブ
		return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}

}
