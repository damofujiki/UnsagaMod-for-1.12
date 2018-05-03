package mods.hinasch.unsaga.core.client.render.projectile;


import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.core.entity.projectile.EntityBullet;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderBullet extends Render<EntityBullet>
{

	//MMM氏のものをコピペ
    private float field_77002_a;

    private static final ResourceLocation arrowTextures = new ResourceLocation(UnsagaMod.MODID,"textures/items/barrett.png");


    public RenderBullet(RenderManager rm,float par1)
    {
    	super(rm);
        this.field_77002_a = par1;
        //this.loadicon = par2;
    }

    @Override
    public void doRender(EntityBullet par1EntityBarrett, double d, double d1, double d2, float f, float f1)
    {
    	GlStateManager.pushMatrix();
    	GlStateManager.translate((float)d, (float)d1, (float)d2);
    	GlStateManager.rotate((par1EntityBarrett.prevRotationYaw + (par1EntityBarrett.rotationYaw - par1EntityBarrett.prevRotationYaw) * f1) - 90F, 0.0F, 1.0F, 0.0F);
    	GlStateManager.rotate(par1EntityBarrett.prevRotationPitch + (par1EntityBarrett.rotationPitch - par1EntityBarrett.prevRotationPitch) * f1, 0.0F, 0.0F, 1.0F);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder worldRenderer = tessellator.getBuffer();
		int i = 0;
		float f10 = 0.05625F;
		GlStateManager.enableRescaleNormal();
		GlStateManager.disableTexture2D();
		GlStateManager.scale(f10, f10, f10);
		GlStateManager.pushMatrix();
		GlStateManager.rotate(45F, 1.0F, 0.0F, 0.0F);
		GlStateManager.translate(-4.7F, 0.0F, 0.0F);
//		worldRenderer.setNormal(f10, 0.0F, 0.0F);
		worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
//		worldRenderer.startDrawingQuads();
		worldRenderer.color(0.5F, 0.25F, 0.0F, 1.0F);
//		worldRenderer.setColorRGBA_F(0.5F, 0.25F, 0.0F, 1.0F);
		worldRenderer.pos(4.5D, -0.5D, 0.0D);
		worldRenderer.pos(4.5D, 0.0D, -0.5D);
		worldRenderer.pos(4.5D, 0.5D, 0.0D);
		worldRenderer.pos(4.5D, 0.0D, 0.5D);
		tessellator.draw();
		worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
//		worldRenderer.setNormal(-f10, 0.0F, 0.0F);
//		worldRenderer.startDrawingQuads();
		worldRenderer.color(0.4F, 0.25F, 0.0F, 1.0F);
		worldRenderer.pos(4.5D, 0.0D, 0.5D);
		worldRenderer.pos(4.5D, 0.5D, 0.0D);
		worldRenderer.pos(4.5D, 0.0D, -0.5D);
		worldRenderer.pos(4.5D, -0.5D, 0.0D);
		tessellator.draw();
		for (int j = 0; j < 4; j++) {
			GlStateManager.rotate(90F, 1.0F, 0.0F, 0.0F);
			worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
//			worldRenderer.setNormal(0.0F, 0.0F, f10);
//			worldRenderer.startDrawingQuads();
			worldRenderer.color(0.5F, 0.25F, 0.0F, 1.0F);
			worldRenderer.pos(4.5D, -0.5D, 0.0D);
			worldRenderer.pos(6.5D, -0.5D, 0.0D);
			worldRenderer.pos(6.5D, 0.5D, 0.0D);
			worldRenderer.pos(4.5D, 0.5D, 0.0D);
			tessellator.draw();
		}
		GlStateManager.popMatrix();

		GlStateManager.enableTexture2D();
		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
		super.doRender(par1EntityBarrett, d, d1, d2, f, f1);
    }

//    /**
//     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
//     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
//     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
//     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
//     */
//    @Override
//    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
//    {
//        this.doRenderBarrett((EntityBullet)par1Entity, par2, par4, par6, par8, par9);
//    }



	@Override
	protected ResourceLocation getEntityTexture(EntityBullet entity) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}
}
