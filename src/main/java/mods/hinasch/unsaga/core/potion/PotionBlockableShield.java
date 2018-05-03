package mods.hinasch.unsaga.core.potion;

import java.util.Random;

import mods.hinasch.lib.core.HSLib;
import mods.hinasch.lib.network.PacketSound;
import mods.hinasch.lib.network.PacketUtil;
import mods.hinasch.unsaga.core.client.model.ModelMagicShield;
import mods.hinasch.unsaga.damage.AdditionalDamageData;
import mods.hinasch.unsaga.damage.AdditionalDamageTypes;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

public class PotionBlockableShield extends PotionMagicShield{


	public static ModelMagicShield modelShield = new ModelMagicShield();

	float rotate = 0.0F;
	final float value;
	final AdditionalDamageTypes types;
	final boolean canBlockUnblockable;
	protected PotionBlockableShield(String name, int u, int v,AdditionalDamageTypes types,float value,boolean canBlockUnblockable) {
		super(name, u, v);
		this.types = types;
		this.value = value;
		this.canBlockUnblockable = canBlockUnblockable;
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public AdditionalDamageTypes getBlockableTypes(){
		return this.types;
	}

	public float getBlockableValue(){
		return this.value;
	}

	public boolean canBlockUnblockableAttack(){
		return this.canBlockUnblockable;
	}

	@Override
	public void affectOnDamage(LivingDamageEvent e,AdditionalDamageData other,int amplifier){
		super.affectOnDamage(e,other, amplifier);
		boolean isBlockableDamage = false;
		if(!e.getSource().isUnblockable()){
			isBlockableDamage = true;
		}
		//防御不能の中でガードできるのは魔法攻撃、ドラゴンブレスのみ
		if(e.getSource().isUnblockable() && this.canBlockUnblockableAttack()){
			if(e.getSource()==DamageSource.MAGIC || e.getSource()==DamageSource.DRAGON_BREATH){
				isBlockableDamage = true;
			}
		}

		Random rand = e.getEntityLiving().getRNG();
		if(isBlockableDamage){
			float additional = 0.5F * amplifier;
			if(other.hasIntersection(this.types) && rand.nextFloat()<this.getBlockableValue()+additional){
				HSLib.getPacketDispatcher().sendToAllAround(PacketSound.atEntity(SoundEvents.ITEM_SHIELD_BLOCK, e.getEntityLiving()), PacketUtil.getTargetPointNear(e.getEntityLiving()));
				e.setAmount(0);
			}
		}
	}


}
