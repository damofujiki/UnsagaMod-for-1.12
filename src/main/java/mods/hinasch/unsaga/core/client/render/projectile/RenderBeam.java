package mods.hinasch.unsaga.core.client.render.projectile;

import org.lwjgl.opengl.GL11;

import mods.hinasch.unsaga.core.client.model.ModelBeam;
import mods.hinasch.unsaga.core.entity.passive.EntityBeam;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RenderBeam extends Render<EntityBeam>{

	public final ModelBeam beamModel;
	public int count = 0;
	public RenderBeam(RenderManager renderManager) {
		super(renderManager);
		this.beamModel = new ModelBeam();
		// TODO 自動生成されたコンストラクター・スタブ
	}

	private static final ResourceLocation beaconBeam = new ResourceLocation("textures/entity/beacon_beam.png");
	private Vec3d func_177110_a(EntityLivingBase p_177110_1_, double p_177110_2_, float p_177110_4_)
	{
		double d1 = p_177110_1_.lastTickPosX + (p_177110_1_.posX - p_177110_1_.lastTickPosX) * (double)p_177110_4_;
		double d2 = p_177110_2_ + p_177110_1_.lastTickPosY + (p_177110_1_.posY - p_177110_1_.lastTickPosY) * (double)p_177110_4_;
		double d3 = p_177110_1_.lastTickPosZ + (p_177110_1_.posZ - p_177110_1_.lastTickPosZ) * (double)p_177110_4_;
		return new Vec3d(d1, d2, d3);
	}

	public void func_177109_a(EntityBeam parent, double x, double y, double z, float p_177109_8_, float p_177109_9_)
	{


		EntityLivingBase owner = parent.getOwner();


		if (owner != null)
		{
			// float f2 = parent.func_175477_p(p_177109_9_);
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder buffer = tessellator.getBuffer();
			this.bindEntityTexture(parent);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10497.0F);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10497.0F);
			GlStateManager.disableLighting();
			GlStateManager.disableCull();
			GlStateManager.disableBlend();
			GlStateManager.depthMask(true);
			float f3 = 240.0F;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, f3, f3);
			GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
			//            float f4 = (float)parent.worldObj.getTotalWorldTime() + p_177109_9_;
			//            float f5 = f4 * 0.5F % 1.0F;
			//            float f6 = parent.getEyeHeight();
			GlStateManager.pushMatrix();
			//            GlStateManager.translate((float)p_177109_2_, (float)p_177109_4_ + f6, (float)p_177109_6_);
			//            Vec3 vec3 = this.func_177110_a(target, (double)target.height * 0.5D, p_177109_9_);
			//   Vec3 vec31 = this.func_177110_a(parent, (double)f6, p_177109_9_);
			//      Vec3 vec32 = vec3.subtract(vec31);
			//            double d3 = vec32.lengthVector() + 1.0D;
			//            vec32 = vec32.normalize();
			//            float f7 = (float)Math.acos(vec32.yCoord);
			//            float f8 = (float)Math.atan2(vec32.zCoord, vec32.xCoord);
			//            GlStateManager.rotate((((float)Math.PI / 2F) + -f8) * (180F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
			//            GlStateManager.rotate(f7 * (180F / (float)Math.PI), 1.0F, 0.0F, 0.0F);

			//            double d4 = (double)f4 * 0.05D * (1.0D - (double)(b0 & 1) * 2.5D);
			//worldrenderer.startDrawingQuads();
			//   float f9 = f2 * f2;
			//            worldrenderer.setColorRGBA(64 + (int)(f9 * 240.0F), 32 + (int)(f9 * 192.0F), 128 - (int)(f9 * 64.0F), 255);

			//            double yaw = Math.toRadians(40.0D);
			//            double d5 = 1.0D;
			//            double d6 = Math.cos(Math.PI/2 - yaw);
			//            double d7 = Math.sin(Math.PI/2-yaw);
			double length = 4.0D;
			double height = 0.5D;
			double beamWidth = 1.0D;
			double fg = 0.5D; //From Ground
			double offset = -0.5D;
			//Vec3 vec3 = new Vec3(x,y,z);
			buffer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
			buffer.pos(offset+x,y+fg,z+length).tex(0.0D, 0.0D).endVertex();
			buffer.pos(offset+x,y+fg,z).tex(1.0D, 0.0D).endVertex();
			buffer.pos(offset+x+beamWidth,y+fg,z).tex(1.0D, 1.0D).endVertex();
			buffer.pos(offset+x+beamWidth,y+fg,z+length).tex(0.0D, 1.0D).endVertex();

			buffer.pos(offset+x,y+height+fg,z+length).tex(0.0D, 0.0D);
			buffer.pos(offset+x,y+height+fg,z).tex(1.0D, 0.0D);
			buffer.pos(offset+x+beamWidth,y+height+fg,z).tex(1.0D, 1.0D);
			buffer.pos(offset+x+beamWidth,y+height+fg,z+length).tex(0.0D, 1.0D);

			buffer.pos(offset+x,y+height+fg,z+length).tex(0.0D, 0.0D);
			buffer.pos(offset+x,y+height+fg,z).tex(1.0D, 0.0D);
			buffer.pos(offset+x,y+fg,z).tex(1.0D, 1.0D);
			buffer.pos(offset+x,y+fg,z+length).tex(0.0D, 1.0D);

			buffer.pos(offset+x+beamWidth,y+height+fg,z+length).tex(0.0D, 0.0D);
			buffer.pos(offset+x+beamWidth,y+height+fg,z).tex(1.0D, 0.0D);
			buffer.pos(offset+x+beamWidth,y+fg,z).tex(1.0D, 1.0D);
			buffer.pos(offset+x+beamWidth,y+fg,z+length).tex(0.0D, 1.0D);

			float beamYaw = MathHelper.wrapDegrees(owner.rotationYaw);
			//            Vec3 horizontal = owner.getLookVec().rotateYaw((float) Math.toRadians(90));
			//            GlStateManager.translate(horizontal.xCoord, 0, horizontal.zCoord);
			GlStateManager.rotate(-beamYaw, 0.0F,1.0F,0.0F);

			//            worldrenderer.addVertexWithUV(tpos.dx-d6, eppos.dy, tpos.dz+d7, 0.0D, 0.0D);
			//            worldrenderer.addVertexWithUV(eppos.dx-d6, eppos.dy, eppos.dz+d7, 1.0D, 0.0D);
			//            worldrenderer.addVertexWithUV(eppos.dx+d6, eppos.dy, tpos.dz-d7, 1.0D, 1.0D);
			//            worldrenderer.addVertexWithUV(tpos.dx+d6, eppos.dy, tpos.dz-d7, 0.0D, 1.0D);

			//            double d5 = (double)b0 * 0.2D;
			//            double d6 = d5 * 1.41D;
			//            double d7 = 0.0D + Math.cos(d4 + 2.356194490192345D) * d6;
			//            double d8 = 0.0D + Math.sin(d4 + 2.356194490192345D) * d6;
			//            double d9 = 0.0D + Math.cos(d4 + (Math.PI / 4D)) * d6;
			//            double d10 = 0.0D + Math.sin(d4 + (Math.PI / 4D)) * d6;
			//            double d11 = 0.0D + Math.cos(d4 + 3.9269908169872414D) * d6;
			//            double d12 = 0.0D + Math.sin(d4 + 3.9269908169872414D) * d6;
			//            double d13 = 0.0D + Math.cos(d4 + 5.497787143782138D) * d6;
			//            double d14 = 0.0D + Math.sin(d4 + 5.497787143782138D) * d6;
			//            double d15 = 0.0D + Math.cos(d4 + Math.PI) * d5;
			//            double d16 = 0.0D + Math.sin(d4 + Math.PI) * d5;
			//            double d17 = 0.0D + Math.cos(d4 + 0.0D) * d5;
			//            double d18 = 0.0D + Math.sin(d4 + 0.0D) * d5;
			//            double d19 = 0.0D + Math.cos(d4 + (Math.PI / 2D)) * d5;
			//            double d20 = 0.0D + Math.sin(d4 + (Math.PI / 2D)) * d5;
			//            double d21 = 0.0D + Math.cos(d4 + (Math.PI * 3D / 2D)) * d5;
			//            double d22 = 0.0D + Math.sin(d4 + (Math.PI * 3D / 2D)) * d5;
			//            double d23 = 0.0D;
			//            double d24 = 0.4999D;
			//            double d25 = (double)(-1.0F + f5);
			//            double d26 = d3 * (0.5D / d5) + d25;
			//            worldrenderer.addVertexWithUV(d15, d3, d16, d24, d26);
			//            worldrenderer.addVertexWithUV(d15, 0.0D, d16, d24, d25);
			//            worldrenderer.addVertexWithUV(d17, 0.0D, d18, d23, d25);
			//            worldrenderer.addVertexWithUV(d17, d3, d18, d23, d26);
			//            worldrenderer.addVertexWithUV(d19, d3, d20, d24, d26);
			//            worldrenderer.addVertexWithUV(d19, 0.0D, d20, d24, d25);
			//            worldrenderer.addVertexWithUV(d21, 0.0D, d22, d23, d25);
			//            worldrenderer.addVertexWithUV(d21, d3, d22, d23, d26);
			//            double d27 = 0.0D;
			//
			//            if (parent.ticksExisted % 2 == 0)
			//            {
			//                d27 = 0.5D;
			//            }
			//
			//            worldrenderer.addVertexWithUV(d7, d3, d8, 0.5D, d27 + 0.5D);
			//            worldrenderer.addVertexWithUV(d9, d3, d10, 1.0D, d27 + 0.5D);
			//            worldrenderer.addVertexWithUV(d13, d3, d14, 1.0D, d27);
			//            worldrenderer.addVertexWithUV(d11, d3, d12, 0.5D, d27);
			tessellator.draw();
			GlStateManager.popMatrix();
		}
	}


	public static double d = 0;
	@Override
	public void doRender(EntityBeam entity, double x, double y,
			double z, float yaw, float partialTicks) {
		//		UnsagaMod.logger.trace("aaaa", "called");
		//		if(entity.getOwner()!=null && entity.getTarget()!=null){
		//			this.func_177109_a(e, d1, d2, d3, d4, f1);

		//			UnsagaMod.logger.trace("aa", entity.getOwner().rotationYaw);
		//	        GlStateManager.popMatrix();
		//	        Entity target = entity.getTarget();
		//	        BlockPos blockpos = target.getPosition();

		//	        if (blockpos != null)
		//	        {
		float ownerYaw = entity.getOwner()!=null ? entity.getOwner().rotationYaw : 0;
		double scale = 0.25F;
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y+0.5D, z);
//		if(count>=0){
//			Vec3d vec = entity.getLookVec().scale(partialTicks*10+0.01D);
//			GlStateManager.translate(vec.x, 0, vec.z);
//			count++;
//			if(count>5){
//				count = 0;
//			}
//		}
//		GlStateManager.rotate(90.0F, 0, 1.0F, 0);
		GlStateManager.rotate(180F, 0, 1.0F, 0);
		GlStateManager.rotate(180F-yaw, 0, 1.0F, 0);
		GlStateManager.scale(scale, scale, 1.0F);
		GlStateManager.alphaFunc(516, 0.1F);
		this.bindTexture(beaconBeam);
		GlStateManager.disableFog();
//		int os = entity.ticksExisted % 10;

//		this.beamModel.setTextureOffset(os);
		this.beamModel.setCoreRotate(entity.ticksExisted % 180);
		this.beamModel.render(entity, 0, 0, partialTicks, (float) Math.toRadians(MathHelper.wrapDegrees(ownerYaw)), entity.rotationPitch, 0.625F);
//		this.beamModel.setRotationAngles(this, 0, entity.rotationYaw, 0);

//		UnsagaMod.logger.trace("", os);

		GlStateManager.enableFog();
		//	            GlStateManager.rotate(-(float) MathHelper.wrapDegrees(entity.getOwner().rotationYaw), 0, 1.0F,0);
		//	            GlStateManager.translate(0, 0.5D, 0);
		//	            float f2 = (float)blockpos.getX() + 0.5F;
		//	            float f3 = (float)blockpos.getY() + 0.5F;
		//	            float f4 = (float)blockpos.getZ() + 0.5F;
		//	            double beamDistance = 10.0D;
		//	            double beamWidth = 0.5D;
		//	            Tessellator tessellator = Tessellator.getInstance();
		//	            BufferBuilder bufferbuilder = tessellator.getBuffer();
		//	            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		//	            bufferbuilder.pos(x + beamWidth, y, z + beamDistance).tex(1.0D, 1.0D).endVertex();;
		//	            bufferbuilder.pos(x + beamWidth, y, z).tex(1.0D, 0).endVertex();;
		//	            bufferbuilder.pos(x, y, z).tex(0, 0).endVertex();
		//	            bufferbuilder.pos(x, y, z + beamDistance).tex(0, 1.0D).endVertex();;
		//	            tessellator.draw();
		//	            GlStateManager.enableBlend();
		//	            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		//	            TileEntityBeaconRenderer.renderBeamSegment(x, y, z, partialTicks, textureScale, totalWorldTime, yOffset, height, colors);
		//	            RenderDragon.renderCrystalBeams(x, y - 1.2999999523162842D + (double)(f1 * 0.4F), z, partialTicks, entity.posX, entity.posY, entity.posZ, entity.innerRotation, (double)f2, (double)f3, (double)f4);
		//	            GlStateManager.disableBlend();

		GlStateManager.popMatrix();
		//	        }
		//		}

	}

	@Override
	protected ResourceLocation getEntityTexture(EntityBeam entity) {
		// TODO 自動生成されたメソッド・スタブ
		return beaconBeam;
	}
}
