package mods.hinasch.unsaga.core.potion;

import java.util.function.Predicate;

import mods.hinasch.lib.client.ClientHelper;
import mods.hinasch.lib.entity.RangedHelper;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.core.client.IRenderLivingEffect;
import mods.hinasch.unsaga.core.client.model.ModelThinBox;
import mods.hinasch.unsaga.damage.AdditionalDamageData;
import mods.hinasch.unsaga.damage.DamageHelper;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.General;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.Sub;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderLivingEvent.Post;

public class PotionRepel extends PotionBuff implements IRenderLivingEffect{

	public static final ResourceLocation CIRCLE = new ResourceLocation(UnsagaMod.MODID,"textures/entity/circle.png");
	public static ModelThinBox modelThin = new ModelThinBox();
	float rotate = 0;
	final Predicate<EntityLivingBase> predicateRepel;

	protected PotionRepel(String name, int u, int v,Predicate<EntityLivingBase> predicateRepel) {
		super(name, u, v);
		this.predicateRepel = predicateRepel;
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public boolean canRepelEnemy(EntityLivingBase t){
		return this.predicateRepel.test(t);
	}
	@Override
	public void performEffect(EntityLivingBase entityAffected, int amplifier)
	{
		super.performEffect(entityAffected, amplifier);
		PotionEffect effect = entityAffected.getActivePotionEffect(this);
//		int amplifier = effect.getAmplifier();
		World world = entityAffected.getEntityWorld();
		if(entityAffected instanceof EntityPlayer || entityAffected instanceof EntityTameable){
			RangedHelper.create(world, entityAffected, entityAffected.getEntityBoundingBox().grow(10.0D))
			.setSelector((self,target)->this.canRepelEnemy(target))
			.setConsumer((self,in)->{
				Vec3d v = in.getPositionVector().subtract(entityAffected.getPositionVector()).normalize().scale(0.1D);
				in.motionX += v.x;
				in.motionZ += v.z;
			}).invoke();

		}else{
			if(this.canRepelEnemy(entityAffected)){
	            if (entityAffected.getHealth() > 1.0F)
	            {
	                entityAffected.attackEntityFrom(DamageHelper.create(new AdditionalDamageData(DamageSource.MAGIC,General.MAGIC,0.1F).setSubTypes(Sub.SHOCK)), 1.0F);
	            }
			}
		}
//		if(this==UnsagaPotions.DARK_SEAL){
//			RangedHelper.create(world, entityLivingBaseIn, entityLivingBaseIn.getEntityBoundingBox().grow(10.0D))
//			.setSelector((self,target)->target.getCreatureAttribute()!=EnumCreatureAttribute.UNDEAD)
//			.setConsumer((self,in)->{
//				Vec3d v = in.getPositionVector().subtract(entityLivingBaseIn.getPositionVector()).normalize().scale(0.1D);
//				in.motionX += v.x;
//				in.motionZ += v.z;
//			}).invoke();
//
//		}
	}

	@Override
	public void renderEffect(Post e) {
		GlStateManager.pushMatrix();
		GlStateManager.enableRescaleNormal();
		GlStateManager.translate(e.getX(),e.getY()+0.15D,e.getZ());
		GlStateManager.scale(0.3F, 0.3F, 0.3F);
		GlStateManager.rotate(180F, 1.0F, 0F, 0.0F);
		GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F);
		GlStateManager.disableCull();
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);

		ClientHelper.bindTextureToTextureManager(CIRCLE);

		//		wait ++;
		//		if(wait>20 + UnsagaMod.proxy.getDebugPos().getX()){
		//			wait = 0;
		//
		//		}

		this.modelThin.rotate(rotate);
		rotate += 0.05F;
		rotate = MathHelper.wrapDegrees(rotate);

		this.modelThin.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.625F);
		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GlStateManager.enableCull();
		GlStateManager.popMatrix();
	}
}
