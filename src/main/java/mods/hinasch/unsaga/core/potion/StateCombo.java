package mods.hinasch.unsaga.core.potion;

import mods.hinasch.lib.core.HSLib;
import mods.hinasch.lib.item.ItemUtil;
import mods.hinasch.lib.network.PacketSound;
import mods.hinasch.lib.network.PacketUtil;
import mods.hinasch.lib.particle.PacketParticle;
import mods.hinasch.lib.util.ChatHandler;
import mods.hinasch.lib.util.VecUtil;
import mods.hinasch.lib.world.XYZPos;
import mods.hinasch.unsaga.damage.AdditionalDamageData;
import mods.hinasch.unsaga.damage.DamageComponent;
import mods.hinasch.unsaga.lp.LifePoint;
import mods.hinasch.unsaga.util.AdditionalDamageCache;
import mods.hinasch.unsaga.util.LivingHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class StateCombo extends PotionUnsaga{

	//TODO:ターゲットの同一性も観るようにする
	protected StateCombo(String name) {
		super(name, true, 0, 0,0);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public static void onDamage(LivingHurtEvent e){
		if(!(e.getSource().getTrueSource() instanceof EntityLivingBase)){
			return;
		}
		EntityLivingBase attacker = (EntityLivingBase) e.getSource().getTrueSource();
		if(LivingHelper.getState(attacker, UnsagaPotions.COMBO) instanceof Effect){
			World world = e.getEntity().getEntityWorld();
			EntityLivingBase victim = e.getEntityLiving();
			Effect effect = (Effect) LivingHelper.getState(attacker, UnsagaPotions.COMBO);
			int attackCount = effect.getAmplifier();
			if(effect.getWeapon()!=attacker.getHeldItemMainhand()){
				return;
			} //コンボの武器が同じか
			if(effect.getHitTick()==attacker.ticksExisted){
				return;
			} //スイープの同時ヒットによるカウントを防ぐ
			if(attackCount>=effect.getMaxCount()){
				LifePoint.adapter.getCapability(victim).setHurtInterval(0);
				HSLib.getPacketDispatcher().sendToAllAround(PacketParticle.create(new XYZPos(e.getEntityLiving().getPosition().up()),EnumParticleTypes.CRIT,35), PacketUtil.getTargetPointNear(victim));
				LivingHelper.getCapability(attacker).removeState(UnsagaPotions.COMBO);
				HSLib.getPacketDispatcher().sendToAllAround(PacketSound.atEntity(SoundEvents.ENTITY_IRONGOLEM_HURT, e.getEntityLiving()).setPitch(0.5F), PacketUtil.getTargetPointNear(victim));
				e.setAmount(e.getAmount()+effect.getFinishDamage().hp());
				if(AdditionalDamageCache.getData(e.getSource())!=null){
					AdditionalDamageData data = AdditionalDamageCache.getData(e.getSource());
					data.setLP(effect.getFinishDamage().lp());
				}
				VecUtil.knockback(attacker, victim, 1.0D, 0.4D);

				if(attacker instanceof EntityPlayer){
					ChatHandler.sendChatToPlayer((EntityPlayer) attacker, (attackCount+2)+" Hit Combo Finish!");
				}
			}else{
				HSLib.getPacketDispatcher().sendToAllAround(PacketParticle.create(new XYZPos(victim.getPosition().up()),effect.getParticle(),35), PacketUtil.getTargetPointNear(victim));

				HSLib.getPacketDispatcher().sendToAllAround(PacketSound.atEntity(SoundEvents.ENTITY_IRONGOLEM_HURT, e.getEntityLiving()), PacketUtil.getTargetPointNear(victim));
//				UnsagaPotions.addRemoveQueue(e.getEntityLiving(), this);
				LivingHelper.getCapability(attacker).addState(effect.copy(attackCount+1).setHitTick(attacker.ticksExisted));
				if(attacker instanceof EntityPlayer){
					ChatHandler.sendChatToPlayer((EntityPlayer) attacker, (attackCount+2)+" Hit Combo!");
				}
//				UnsagaMod.logger.trace(this.getName(), "combo:",e.getEntityLiving().getActivePotionEffect(this).getAmplifier());
			}
		}
	}

	public static class Effect extends PotionEffect{

		final DamageComponent finishDamage;
		final EnumParticleTypes particle;
		final ItemStack weapon;
		final int maxCount;
		int hitTick = 0;
		public Effect(int current,int max,DamageComponent lastDamage,EnumParticleTypes particle,ItemStack weapon) {
			super(UnsagaPotions.COMBO, ItemUtil.getPotionTime(2),current,false,false);
			this.finishDamage = lastDamage;
			this.particle = particle;
			this.weapon = weapon;
			this.maxCount = max;
			// TODO 自動生成されたコンストラクター・スタブ
		}

		public DamageComponent getFinishDamage(){
			return this.finishDamage;
		}

		public EnumParticleTypes getParticle(){
			return this.particle;
		}

		public ItemStack getWeapon(){
			return this.weapon;
		}

		public int getHitTick(){
			return this.hitTick;
		}

		public Effect setHitTick(int tick){
			this.hitTick = tick;
			return this;
		}
		public int getMaxCount(){
			return this.maxCount;
		}
		public Effect copy(int hit){
			return new Effect(hit,this.maxCount,this.finishDamage,this.particle,this.weapon);
		}


	}
}
