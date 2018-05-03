package mods.hinasch.unsaga.core.client.event;

import mods.hinasch.lib.client.ClientHelper;
import mods.hinasch.lib.client.RenderHelperHS;
import mods.hinasch.lib.entity.RangedHelper;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.lib.world.XYZPos;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.core.potion.UnsagaPotions;
import mods.hinasch.unsaga.lp.LifePoint;
import mods.hinasch.unsaga.lp.LifePoint.ILifePoint;
import mods.hinasch.unsaga.skillpanel.SkillPanelAPI;
import mods.hinasch.unsaga.skillpanel.SkillPanels;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


/** 敵のLP表示関連*/
public class EventRenderGameOverlay {

	public static class RenderEnemyStatus extends Gui{


		public RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
		protected RenderEnemyStatus(){

		}

		public static RenderEnemyStatus getEvent(){
			return new RenderEnemyStatus();
		}

		public boolean canDetectEntityWithSkill(EntityLivingBase e){
//			if(SkillPanels.hasPanel(e.worldObj, ClientHelper.getClientPlayer(), SkillPanels.getInstance().watchingOut)){
//				if(e.getDistanceToEntity(ClientHelper.getClientPlayer())<50.0F){
//					return true;
//				}
//			}

			if(SkillPanelAPI.hasPanel(ClientHelper.getPlayer(), SkillPanels.WATCHING_OUT)){
				if(e.getDistance(ClientHelper.getPlayer())<10.0D){
					return true;
				}

			}
			return false;
		}


		protected void renderTarget(RenderLivingEvent.Post e,XYZPos pos){
			FontRenderer fontRenderer = e.getRenderer().getFontRendererFromRenderManager();
			RenderHelperHS renderHelper = RenderHelperHS.create(fontRenderer, renderManager);

			if(e.getEntity().getEntityId()==UnsagaMod.proxy.getKeyBindings().getClientTargetSelector().getCurrentIndex()){
				String str = "Target";
//		        int i = 61680;
//		        int j = i % 65536;
//		        int k = i / 65536;
//		        GlStateManager.disableAlpha();

		        if(ClientHelper.getWorld().getWorldTime()>12000){
//			        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j, (float)k);
//			        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
					renderHelper.renderStringAt(e.getEntity(), str, pos, 100, 0xffffff);
//			        GlStateManager.enableAlpha();
		        }else{
					renderHelper.renderStringAt(e.getEntity(), str, pos, 100, 0xffffff);
		        }


			}
		}

//		protected void renderShadowLife(RenderLivingEvent.Post e,XYZPos pos){
//			FontRenderer fontRenderer = e.getRenderer().getFontRendererFromRenderManager();
//			XYZPos offset = UnsagaMod.configs.getRenderLPOffset();
//			if(e.getEntity() instanceof EntityShadowServant){
//
//				EntityShadowServant servant = (EntityShadowServant) e.getEntity();
//				String str = String.valueOf(servant.getLife());
//				fontRenderer.drawString(str, (int)pos.dx, (int)pos.dy , 0xffffff);
//			}
//		}
		protected void renderLivingLPDamage(RenderLivingEvent.Post e,XYZPos pos){
			FontRenderer fontRenderer = e.getRenderer().getFontRendererFromRenderManager();
			RenderHelperHS renderHelper = RenderHelperHS.create(fontRenderer, renderManager);

//			//敵がdetectデバフがあるか、もしくはwatchingOutスキルがあるか（範囲制限あり）
			if(e.getEntity().isPotionActive(UnsagaPotions.instance().DETECTED)|| this.canDetectEntityWithSkill(e.getEntity())){
				String str = "%d / %d";
				int hp = (int)e.getEntity().getHealth();
				int maxhp = (int)e.getEntity().getMaxHealth();
				String str2 = String.format(str, hp,maxhp);
				//Unsaga.debug(this.getClass(),"kiteru");
				renderHelper.renderStringAt(e.getEntity(), str2, pos, 100, 0xffffff);


			}


			EntityLivingBase living = e.getEntity();



			if(LifePoint.adapter.hasCapability(living) && UnsagaMod.configs.isEnabledLifePointSystem()){

				ILifePoint lpCapability = LifePoint.adapter.getCapability(living);
				boolean isNearPlayer = ClientHelper.getPlayer().getDistance(e.getEntity())<8.0D;
				if(HSLibs.isSameTeam(ClientHelper.getPlayer(), living) ||(UnsagaMod.configs.isEnabledRenderNearMonsterLP() && isNearPlayer)){

					String str = String.format("%d / %d", lpCapability.getLifePoint(),lpCapability.getMaxLifePoint());


					renderHelper.renderStringAt(living, str, pos.addDouble(0, 0.2D, 0), 100, 0xffffff);

				}

				if(lpCapability.isDirty()){

					if(lpCapability.getRenderTextPos()!=null){
						lpCapability.setRenderTextPos(pos);
					}
					XYZPos p = lpCapability.getRenderTextPos();
					lpCapability.decrRenderTick(1);
					String str = "-"+String.valueOf(lpCapability.getRenderDamage())+" LP";
					renderHelper.renderStringAt(living, str, pos.addDouble(0,0.4D, 0), 100, 0xff0000);
					if(lpCapability.getRenderTick()<0){
						lpCapability.resetRenderTick();
					}
				}
			}

		}
		@SubscribeEvent
		public void onRenderLivingPost(RenderLivingEvent.Post e){
			XYZPos pos = new XYZPos(e.getX(),e.getY(),e.getZ());
			this.renderLivingLPDamage(e,pos);
			this.renderTarget(e,pos);
//			this.renderShadowLife(e, pos);

		}

	}


	public static class RenderPlayerStatus extends Gui{
		protected FontRenderer fontRenderer;



		protected RenderPlayerStatus(){

		}
		public static RenderPlayerStatus getEvent(){
			return new RenderPlayerStatus();
		}

		@SubscribeEvent
		public void onRenderStatus(RenderGameOverlayEvent e){


			Tessellator tesselater = Tessellator.getInstance();



			if(this.fontRenderer==null){
				this.fontRenderer = ClientHelper.fontRenderer();
			}


			//デバフの描画
			if(e.getType()==RenderGameOverlayEvent.ElementType.TEXT){
//				this.renderPlayerDebuffs();
				this.renderPlayerLP();
			}



		}

		public void renderPlayerDebuffs(){
//			EntityPlayer clientPlayer = ClientHelper.getPlayer();
//			ClientHelper.getRenderEngine().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
//
//			XYZPos offset = UnsagaMod.configHandler.getRenderDebuffOffset();
//			if(DebuffHelper.hasCapability(clientPlayer)){
//				ICustomDebuff debuffs = DebuffHelper.getCapability(clientPlayer);
//				int index = 0;
//
//				for(DebuffEffectBase debuff:debuffs.getDebuffs()){
//					if(debuff.getDebuff() instanceof DebuffBaseUnsaga){
//
//						if(((DebuffBaseUnsaga)debuff.getDebuff()).isDisplayDebuff()){
//
//							TextureAtlasSprite debuffIcon = debuff.getDebuff().getDebuffIconBase();
//							TextureAtlasSprite iconOverlay = debuff.getDebuff().getDebuffIconOverlay();
//				            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//				            GL11.glDisable(GL11.GL_LIGHTING);
//				            this.drawTexturedModalRect(offset.getX() + 6 + index * 18, offset.getY() + 32, debuffIcon, 16, 16);
//
//				            if(iconOverlay!=null){
//					            this.drawTexturedModalRect(offset.getX() + 6 + index * 18, offset.getY() + 32, iconOverlay, 16, 16);
//				            }
//				            index += 1;
//						}
//					}
//
//
//				}
//			}
		}
		public void renderPlayerLP(){
			RangedHelper r = RangedHelper.create(ClientHelper.getWorld(), ClientHelper.getPlayer(), 20);
			r.setSelector((self,in)->{
				return ((EntityLivingBase)in).isPotionActive(UnsagaPotions.instance().DETECTED);
			});

//			r.setConsumer(new BiConsumer<RangedHelper,EntityLivingBase>(){
//
//				@Override
//				public void accept(RangedHelper t, EntityLivingBase u) {
//					if(u.isPotionActive(UnsagaPotions.instance().detected)){
//						int dur = u.getActivePotionEffect(UnsagaPotions.instance().detected).getDuration();
//						if(dur<=0){
//							u.removePotionEffect(UnsagaPotions.instance().detected);
//						}
//					}
//
//					BlockPos pos = ClientHelper.getPlayer().getPosition().subtract(u.getPosition());
//					fontRenderer.drawString("e", 50+pos.getX(), 50+pos.getZ(), 0xffffff);
//					fontRenderer.drawString("P", 50, 50, 0xffffff);
//				}}
//			);
//			r.invoke();

//			if(ClientHelper.getPlayer().isPotionActive(UnsagaPotions.instance().detectPlant)){
//				PotionEffect p = ClientHelper.getPlayer().getActivePotionEffect(UnsagaPotions.instance().detectPlant);
//				if(p!=null){
//					if(p.getDuration()<=0){
//						ClientHelper.getPlayer().removeActivePotionEffect(UnsagaPotions.instance().detectPlant);
//					}
//				}
//				ScannerNew.create().base(ClientHelper.getPlayer()).range(10, 10, 10).ready().stream()
//				.forEach(in ->{
//					BlockPos pos = ClientHelper.getPlayer().getPosition().subtract(in);
//					Material material = ClientHelper.getWorld().getBlockState(in).getMaterial();
//					if(material==Material.WOOD || material==Material.PLANTS){
//						fontRenderer.drawString("W", 50+pos.getX(), 50+pos.getZ(), 0xffffff);
//						fontRenderer.drawString("P", 50, 50, 0xffffff);
//					}
//
//				});
//			}
//
			EntityPlayer clientPlayer = ClientHelper.getPlayer();
			XYZPos offset = UnsagaMod.configs.getRenderLPOffset();
			if(UnsagaMod.configs.isEnabledLifePointSystem() && LifePoint.adapter.hasCapability(clientPlayer)){
				String str = String.format("LP:%d / %d", LifePoint.adapter.getCapability(clientPlayer).getLifePoint(),LifePoint.adapter.getCapability(clientPlayer).getMaxLifePoint());
				fontRenderer.drawString(str, offset.getX()+10, offset.getY()+10, 0xffffff);
			}
//
//			if(clientPlayer.isPotionActive(UnsagaPotions.instance().detectGold)){
//				if(EntityStateCapability.adapter.hasCapability(clientPlayer)){
//					StateOreDetecter state = (StateOreDetecter) EntityStateCapability.adapter.getCapability(clientPlayer).getState(StateRegistry.instance().stateOreDetecter);
//					if(state.getBasePos().isPresent()){
//						for(BlockPos orePos:state.getOrePosList()){
//							BlockPos renderPos = state.getBasePos().get().subtract(orePos);
//							fontRenderer.drawString("O", 50+renderPos.getX(), 50+renderPos.getZ(), 0xffffff);
//
//						}
//						fontRenderer.drawString("P", 50, 50, 0xffffff);
//					}
//
//
//				}
//				if(clientPlayer.getActivePotionEffect(UnsagaPotions.instance().detectGold)!=null){
//					if(clientPlayer.getActivePotionEffect(UnsagaPotions.instance().detectGold).getDuration()<=0){
//						clientPlayer.removePotionEffect(UnsagaPotions.instance().detectGold);
//						if(EntityStateCapability.adapter.hasCapability(clientPlayer)){
//							StateOreDetecter state = (StateOreDetecter) EntityStateCapability.adapter.getCapability(clientPlayer).getState(StateRegistry.instance().stateOreDetecter);
//							state.clear();
//						}
//					}
//				}
//			}
		}

	}


}
