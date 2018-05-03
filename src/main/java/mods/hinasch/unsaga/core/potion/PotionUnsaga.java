package mods.hinasch.unsaga.core.potion;

import mods.hinasch.lib.client.ClientHelper;
import mods.hinasch.lib.item.ItemUtil;
import mods.hinasch.lib.registry.IPropertyElement;
import mods.hinasch.lib.util.Statics;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.damage.AdditionalDamageData;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class PotionUnsaga extends Potion implements IPropertyElement{


	public static final ResourceLocation TEXTURE = new ResourceLocation(UnsagaMod.MODID,"textures/gui/container/status_effects.png");
	public static PotionUnsaga of(String name,boolean isBadEffectIn, int liquidColorIn,int u,int v){
		return new PotionUnsaga(name, isBadEffectIn, liquidColorIn,u,v);
	}

	public static PotionUnsaga badPotion(String name,int u,int v){
		return new PotionUnsaga(name, true, 0xff0000,u,v);
	}
	public static PotionUnsaga badPotion(String name,int u,int v,IAttribute attribute,String uuid,double value){
		return (PotionUnsaga) new PotionUnsaga(name, true, 0xff0000,u,v).registerPotionAttributeModifier(attribute, uuid, value, Statics.OPERATION_INCREMENT);
	}
	public static PotionUnsaga buff(String name,int u,int v){
		return new PotionUnsaga(name, false, 0xff0000,u,v);
	}
	String name;
	ResourceLocation key;
	PotionType type;
	boolean hasStatusIcon = false;
	protected PotionUnsaga(String name,boolean isBadEffectIn, int liquidColorIn,int u,int v) {
		super(isBadEffectIn, 250);
		this.setIconIndex(u, v);
		//		this.setRegistryName(new ResourceLocation(UnsagaMod.MODID,"potion."+name));
		this.setPotionName("unsaga.potion."+name);
		this.key = new ResourceLocation(UnsagaMod.MODID,name);
		this.setRegistryName(this.key);
	}
	public PotionEffect createEffect(int time,int amp){
		return new PotionEffect(this,time,amp);
	}
	@Override
	public void performEffect(EntityLivingBase entityLivingBaseIn, int p_76394_2_)
	{
		super.performEffect(entityLivingBaseIn, p_76394_2_);
//		PotionEffect effect = entityLivingBaseIn.getActivePotionEffect(this);
//		if(effect!=null){
////			if(WorldHelper.isClient(entityLivingBaseIn.getEntityWorld())){
//			UnsagaMod.logger.trace("potion", this);
//				if(effect.getDuration()<=0){
//					UnsagaPotions.addRemoveQueue(entityLivingBaseIn, this);
//				}
////			}
//		}


	}


	public void affectOnHurt(LivingHurtEvent e,int amplifier){
	}
	@Override
	public boolean isInstant() {
		return false;
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}

	@Override
	public int getStatusIconIndex() {
		ClientHelper.bindTextureToTextureManager(TEXTURE);
		return super.getStatusIconIndex();
	}

	@Override
	public boolean hasStatusIcon() {
		return this.hasStatusIcon;
	}

	@Override
	public ResourceLocation getKey() {
		// TODO 自動生成されたメソッド・スタブ
		return this.key;
	}

	public void initPotionType(){
		if(this.type==null){
			this.type = new PotionType("unsaga."+this.getPropertyName(),new PotionEffect[]{new PotionEffect(this,ItemUtil.getPotionTime(10),0)});
		}

	}

	public Potion setHasStatusIcon(boolean par1){
		this.hasStatusIcon = par1;
		return this;
	}
	public void setPotionType(PotionType type){
		this.type = type;
	}

	public PotionType getPotionType(){
		return this.type;
	}

	@Override
	public String getPropertyName() {
		// TODO 自動生成されたメソッド・スタブ
		return this.getName();
	}

	public void affectOnDamage(LivingDamageEvent e, AdditionalDamageData data, int amplifier) {
		// TODO 自動生成されたメソッド・スタブ

	}


}
