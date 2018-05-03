package mods.hinasch.unsaga.core.client.render.entity;

import java.util.function.Function;

import com.google.common.base.Supplier;

import mods.hinasch.lib.client.ClientHelper;
import mods.hinasch.unsaga.chest.FieldChestType;
import mods.hinasch.unsaga.core.client.render.projectile.IChestModel;
import mods.hinasch.unsaga.core.entity.passive.EntityUnsagaChest;
import mods.hinasch.unsaga.core.potion.UnsagaPotions;
import mods.hinasch.unsaga.skillpanel.SkillPanel;
import mods.hinasch.unsaga.skillpanel.SkillPanelAPI;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderEntityChest extends RenderLiving<EntityUnsagaChest>{

	//private static final ResourceLocation chestTex = new ResourceLocation(Unsaga.DOMAIN+":textures/entity/entitychest.png");
	private static final ResourceLocation chestTex = new ResourceLocation("textures/entity/chest/normal.png");
//	private ModelChest modelChest = new ModelChest();
	private ModelChestCustom modelChestOpened = new Supplier<ModelChestCustom>(){

		@Override
		public ModelChestCustom get() {
			float ff1 = 1.0F;
			ModelChestCustom model = new ModelChestCustom();
			model.chestLid.rotateAngleX = -(ff1 * ((float)Math.PI / 2F));
			return model;
		}
	}.get();

	public RenderEntityChest(RenderManager rm){
		super(rm, new ModelChestCustom(), 0.5F);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityUnsagaChest p_110775_1_) {
		// TODO 自動生成されたメソッド・スタブ
		return chestTex;
	}


	@Override
	public void doRender(EntityUnsagaChest e, double d1,
			double d2, double d3, float d4,
			float f1) {

//		this.doRenderEntityChest((EntityUnsagaChest) e, d1, d2, d3, d4, f1);
		GlStateManager.pushMatrix();
		this.bindTexture(chestTex);
		GlStateManager.translate(d1-0.5D, d2 + 1.0D, d3+0.5D);
		GlStateManager.rotate(180F, 1.0F, 0, 0);
		float ff1 = 1.0F;


//		ModelChest modelInstance =  new ModelChest();
//		UnsagaMod.logger.trace("chest", chest.isOpened());


		ModelChestCustom model = (ModelChestCustom) this.mainModel;
		if(e instanceof IChestModel){

			Function<EntityUnsagaChest,Boolean> func = in ->{
				FieldChestType type = in.getVisibilityType();
				SkillPanel skill = type.getSuitableSkill();
				if(type==FieldChestType.NORMAL){
					return true;
				}
				if(type!=FieldChestType.NORMAL){
					if(SkillPanelAPI.hasPanel(ClientHelper.getPlayer(), skill)){
						return true;
					}
					if(in.isPotionActive(UnsagaPotions.instance().DETECTED)){
						return true;
					}
				}

				return false;
			};

			boolean visible = func.apply(e);
			if(visible){
				if(((IChestModel)e).isOpened()){
					modelChestOpened.renderAll(e.rotationYaw);
				}else{
					model.renderAll();
				}
			}

		}
		GlStateManager.popMatrix();

		super.doRender(e, d1, d2, d3, d4, f1);
	}


	@Deprecated
	public static <T> void renderChest(double d1,double d2,double d3,float f1,T callBack,IChestModel chest,ModelChest model){

		FieldChestType type = chest.getVisibilityType();
		SkillPanel skill = type.getSuitableSkill();
		if(type!=FieldChestType.NORMAL){
			if(!SkillPanelAPI.hasPanel(ClientHelper.getPlayer(), skill)){
				return;
			}
		}


		GlStateManager.pushMatrix();
		if(callBack instanceof Render){
			((Render)callBack).bindTexture(chestTex);
			GlStateManager.translate(d1-0.5D, d2 + 1.0D, d3+0.5D);
		}
//		if(callBack instanceof RenderBlockChest){
////			((RenderBlockChest)callBack).bindTextureFrom(chestTex);
//			GlStateManager.translate(d1, d2 + 1.0D, d3+1.0D);
//		}


		GlStateManager.rotate(180F, 1.0F, 0, 0);


		//float ff1 = e.lidAngle;

//		float ff1 = chest.getPrevLidAngle() + (chest.getLidAngle() - chest.getPrevLidAngle()) * f1;
//
//		ff1 = 1.0F - f1;
//		ff1 = 1.0F - f1 * f1 * f1;
		float ff1 = 1.0F;

		ModelChest modelInstance =  new ModelChest();
//		UnsagaMod.logger.trace("chest", chest.isOpened());
		if(chest.isOpened()){
			modelInstance.chestLid.rotateAngleX = -(ff1 * ((float)Math.PI / 2F));
		}

		modelInstance.renderAll();
		GlStateManager.popMatrix();
	}


	public static class ModelChestCustom extends ModelChest{

		static final float F= 0.017453292F;

	    public void renderAll(float yaw)
	    {
	    	this.chestBelow.rotateAngleY = yaw * F;
	    	this.chestKnob.rotateAngleY = yaw * F;
	    	this.chestLid.rotateAngleY = yaw*F;
	        this.chestKnob.rotateAngleX = this.chestLid.rotateAngleX;
	        this.chestLid.render(0.0625F);
	        this.chestKnob.render(0.0625F);
	        this.chestBelow.render(0.0625F);
	    }
	}
}
