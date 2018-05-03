package mods.hinasch.unsaga.core.client.event;

import com.google.common.base.Predicate;

import mods.hinasch.lib.client.ClientHelper;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.core.client.IRenderLivingEffect;
import mods.hinasch.unsaga.core.client.model.ModelMagicShield;
import mods.hinasch.unsaga.core.client.model.ModelThinBox;
import mods.hinasch.unsaga.core.potion.old.ShieldProperty;
import mods.hinasch.unsaga.skillpanel.SkillPanelAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelGhast;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


public class EventRenderLivingEffect {

	public ModelGhast model = new ModelGhast();
	public ModelMagicShield modelShield = new ModelMagicShield();
	public ModelThinBox modelThin = new ModelThinBox();
	public ModelBiped shadow = new ModelBiped();
	private static final ResourceLocation beaconBeam = new ResourceLocation("textures/entity/beacon_beam.png");

	public static final ResourceLocation SHOCKWAVE = new ResourceLocation(UnsagaMod.MODID,"textures/entity/shockwave.png");
	public static final ResourceLocation FIRE_SHIELD = new ResourceLocation(UnsagaMod.MODID,"textures/entity/magicshield/fire.png");
	public static final ResourceLocation WATER_SHIELD = new ResourceLocation(UnsagaMod.MODID,"textures/entity/magicshield/water.png");
	public static final ResourceLocation CIRCLE = new ResourceLocation(UnsagaMod.MODID,"textures/entity/circle.png");
	float rotate = 0.0F;
	int wait = 0;
	public EventRenderLivingEffect(){

	}
	@SubscribeEvent
	public void onRenderLiving(RenderLivingEvent.Post e){

		EntityLivingBase living = e.getEntity();
		World world = living.getEntityWorld();
		Predicate hasFamiliar = new Predicate(){

			@Override
			public boolean apply(Object input) {
				if(input instanceof EntityPlayer){
					if(SkillPanelAPI.hasFamiliar((EntityPlayer) input)){
						return true;
					}
				}
				return false;
			}
		};

		for(PotionEffect effect:e.getEntity().getActivePotionEffects()){
			if(effect.getPotion() instanceof IRenderLivingEffect){
				IRenderLivingEffect render = (IRenderLivingEffect)effect.getPotion();
				render.renderEffect(e);
			}
		}


		if(living instanceof EntityPlayer){
//			EntityTippedArrow
//			ParticleHelper.MovingType.FOUNTAIN.spawnParticle(world, XYZPos.createFrom(living),
//					EnumParticleTypes.CRIT, world.rand, 15, 0.1D+world.rand.nextFloat());
//	        GlStateManager.pushMatrix();
//			GlStateManager.enableRescaleNormal();
//
//            GlStateManager.rotate(-(float) MathHelper.wrapDegrees(living.rotationYaw), 0, 1.0F,0);
//
////            float f2 = (float)blockpos.getX() + 0.5F;
////            float f3 = (float)blockpos.getY() + 0.5F;
////            float f4 = (float)blockpos.getZ() + 0.5F;
//            double x= e.getX();
//            double y= e.getY();
//            double z = e.getZ();
//            GlStateManager.translate(x+0.5D,y+0.5D,z-0.5D);
//            double beamDistance = 10.0D;
//            double beamWidth = 0.5D;
//
//            e.getRenderer().bindTexture(beaconBeam);
//
//
//            Tessellator tessellator = Tessellator.getInstance();
//            BufferBuilder bufferbuilder = tessellator.getBuffer();
//            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
//            bufferbuilder.pos(x + beamWidth, y, z + beamDistance).tex(1.0D, 1.0D).endVertex();;
//            bufferbuilder.pos(x + beamWidth, y, z).tex(1.0D, 0).endVertex();;
//            bufferbuilder.pos(x, y, z).tex(0, 0).endVertex();
//            bufferbuilder.pos(x, y, z + beamDistance).tex(0, 1.0D).endVertex();;
//            tessellator.draw();
//	        GlStateManager.popMatrix();
		}

//		BlockPos fix = UnsagaMod.proxy.getDebugPos();


//		ShieldPropertyRegistry.instance().shields.stream().forEach(in ->{
//			if(e.getEntity().isPotionActive(in.getPotion())){
//				this.renderMagicShield(in, e, e.getEntity());
//			}
//		});


//		if(e.getEntity() instanceof EntitySuperPig){
//			RenderPlayer renderPlayer =  ClientHelper.getRenderManager().getSkinMap().get(((AbstractClientPlayer) ClientHelper.getPlayer()).getSkinType());
//
//			if(renderPlayer!=null){
//				GlStateManager.pushMatrix();
//
//
//
//
//				AbstractClientPlayer ep = (AbstractClientPlayer) ClientHelper.getPlayer();
//
////				renderPlayer.bindTexture(ep.getLocationSkin());
////				ModelBase model = renderPlayer.getMainModel();
//				shadow.swingProgress = ep.swingProgress;
//				Minecraft.getMinecraft().renderEngine.bindTexture(SHADOW);
//
//				GlStateManager.scale(0.9375F, 0.9375F, 0.9375F);
//				GlStateManager.translate(e.getX(), 15.0F, e.getZ());
//				GlStateManager.enableBlend();
//	            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
//				GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
//				GlStateManager.rotate(180F, 1.0F, 0F, 0.0F);
//				shadow.render(ep, ep.limbSwing, ep.limbSwingAmount, ep.getAge(), ep.rotationYawHead, ep.rotationPitch, 0.625F);
//
//				GlStateManager.disableBlend();
//				GlStateManager.popMatrix();
//			}
//		}

//		if(ShieldProperty.shields.stream().anyMatch(in -> e.getEntity().isPotionActive(in.getPotion()))){
//			this.renderMagicShield(in, e, e.getEntity());
//		}
//		GlStateManager.pushMatrix();
//		GlStateManager.enableRescaleNormal();
//		float scala = 0.025F;
//        Tessellator tessellator = Tessellator.getInstance();
//        VertexBuffer vertexbuffer = tessellator.getBuffer();
////        vertexbuffer.begin(7, VERTEX_FORMAT);
//        float f = 0.24975F;
//
//    	GlStateManager.translate(e.getX(),e.getY()+1.0F,e.getZ());
//		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
////		GlStateManager.scale(scala, scala, scala);
//		GlStateManager.rotate(180F, 1.0F, 0F, 0.0F);
//
//		Minecraft.getMinecraft().renderEngine.bindTexture(SHOCKWAVE);
//		vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
//        vertexbuffer.pos(-1, -1, 0).tex(0, f).endVertex();
//        vertexbuffer.pos(0, -1, 0).tex(f, f).endVertex();
//        vertexbuffer.pos(0, 0, 0).tex(f, 0).endVertex();
//        vertexbuffer.pos(-1, 0, 0).tex(0, 0).endVertex();
//        tessellator.draw();
////		model.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.625F);
//		GlStateManager.disableRescaleNormal();
//		GlStateManager.popMatrix();

//		if(e.getEntity().isPotionActive(UnsagaPotions.DARK_SEAL) || e.getEntity().isPotionActive(UnsagaPotions.HOLY_SEAL)){
//			GlStateManager.pushMatrix();
//			GlStateManager.enableRescaleNormal();
//			GlStateManager.translate(e.getX(),e.getY()+1.0D,e.getZ());
//			GlStateManager.scale(0.3F, 0.3F, 0.3F);
//			GlStateManager.rotate(180F, 1.0F, 0F, 0.0F);
//			GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F);
//			GlStateManager.disableCull();
//			GlStateManager.disableLighting();
//			GlStateManager.enableBlend();
//			GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
//
//			ClientHelper.bindTextureToTextureManager(CIRCLE);
//
//			//		wait ++;
//			//		if(wait>20 + UnsagaMod.proxy.getDebugPos().getX()){
//			//			wait = 0;
//			//
//			//		}
//
//			this.modelThin.rotate(rotate);
//			rotate += 0.05F;
//			rotate = MathHelper.wrapDegrees(rotate);
//
//			this.modelThin.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.625F);
//			GlStateManager.disableBlend();
//			GlStateManager.enableLighting();
//			GlStateManager.enableCull();
//			GlStateManager.popMatrix();
//		}



		if(hasFamiliar.apply(e.getEntity())){
			GlStateManager.pushMatrix();
			GlStateManager.enableRescaleNormal();
			GlStateManager.disableLighting();
			GlStateManager.disableCull();
			float scala = 0.025F;

			GlStateManager.translate(1.0F, 1.0F, 0.0F);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.scale(scala, scala, scala);
			GlStateManager.rotate(180F, 1.0F, 0F, 0.0F);

			Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("textures/entity/ghast/ghast.png"));
			model.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.625F);
			GlStateManager.disableRescaleNormal();
			GlStateManager.enableCull();
			GlStateManager.enableLighting();
			GlStateManager.popMatrix();
		}

	}


	public void renderMagicShield(ShieldProperty shield,RenderLivingEvent.Post e,EntityLivingBase renderEntity){

		if(renderEntity.getActivePotionEffect(shield.getPotion())!=null && renderEntity.getActivePotionEffect(shield.getPotion()).getDuration()<=0){
			renderEntity.removeActivePotionEffect(shield.getPotion());
		}

//		UnsagaMod.logger.trace(this.getClass().getName(), "kiteru");
		GlStateManager.pushMatrix();
		GlStateManager.enableRescaleNormal();
		GlStateManager.translate(e.getX(),e.getY()+1.0F,e.getZ());
		GlStateManager.scale(0.3F, 0.3F, 0.3F);
		GlStateManager.rotate(180F, 1.0F, 0F, 0.0F);
		GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F);
		GlStateManager.disableCull();
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);

		ClientHelper.bindTextureToTextureManager(shield.getTexture());

		//		wait ++;
		//		if(wait>20 + UnsagaMod.proxy.getDebugPos().getX()){
		//			wait = 0;
		//
		//		}

		this.modelShield.rotate(rotate);
		rotate += 0.05F;
		rotate = MathHelper.wrapDegrees(rotate);

		this.modelShield.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.625F);
		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GlStateManager.enableCull();
		GlStateManager.popMatrix();

	}
}
