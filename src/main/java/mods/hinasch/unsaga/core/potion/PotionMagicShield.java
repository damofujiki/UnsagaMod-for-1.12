package mods.hinasch.unsaga.core.potion;

import mods.hinasch.lib.client.ClientHelper;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.core.client.IRenderLivingEffect;
import mods.hinasch.unsaga.core.client.model.ModelMagicShield;
import mods.hinasch.unsaga.damage.AdditionalDamageData;
import mods.hinasch.unsaga.damage.DamageHelper;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.General;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.Sub;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderLivingEvent.Post;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class PotionMagicShield extends PotionBuff implements IRenderLivingEffect{

	float rotate = 0.0F;
	public static ModelMagicShield modelShield = new ModelMagicShield();

	protected PotionMagicShield(String name, int u, int v) {
		super(name, u, v);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	protected PotionMagicShield(String name, int u, int v, IAttribute attribute, String uuid, double value) {
		super(name, u, v, attribute, uuid, value);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public String getTexture(){
		if(this==UnsagaPotions.WATER_SHIELD){
			return "water";
		}
		if(this==UnsagaPotions.SELF_BURNING){
			return "fire";
		}
		if(this==UnsagaPotions.LEAF_SHIELD){
			return "leaf";
		}
		if(this==UnsagaPotions.MISSILE_GUARD){
			return "missile";
		}
		if(this==UnsagaPotions.AEGIES_SHIELD){
			return "aegis";
		}
		return "";
	}
	@Override
	public void performEffect(EntityLivingBase entityLivingBaseIn, int p_76394_2_)
	{
		super.performEffect(entityLivingBaseIn, p_76394_2_);
		if(this==UnsagaPotions.WATER_SHIELD && entityLivingBaseIn.isBurning()){
			entityLivingBaseIn.extinguish();
		}

	}

	@Override
	public void affectOnHurt(LivingHurtEvent e,int amplifier){
		super.affectOnHurt(e, amplifier);
		if(this==UnsagaPotions.instance().SELF_BURNING){
			if(e.getSource().getImmediateSource()!=null){
				Entity entity = e.getSource().getImmediateSource();
				entity.attackEntityFrom(DamageHelper.create(new AdditionalDamageData(DamageSource.causeMobDamage(e.getEntityLiving()),General.MAGIC,0.5F).setSubTypes(Sub.FIRE))
						, 1.0F+(amplifier+1));
				entity.setFire(10 * (amplifier+1));
			}
		}
	}

	@Override
	public void renderEffect(Post e) {
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

		ResourceLocation res = new ResourceLocation(UnsagaMod.MODID,"textures/entity/magicshield/"+this.getTexture()+".png");
		ClientHelper.bindTextureToTextureManager(res);

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
