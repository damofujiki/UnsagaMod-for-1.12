package mods.hinasch.unsaga.core.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;

import com.google.common.collect.Maps;

import mods.hinasch.lib.core.HSLib;
import mods.hinasch.lib.network.PacketSound;
import mods.hinasch.lib.util.ChatHandler;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.ability.AbilityAPI;
import mods.hinasch.unsaga.ability.AbilityShield;
import mods.hinasch.unsaga.ability.EventAbilityLearning;
import mods.hinasch.unsaga.chest.EventUnlockMagic;
import mods.hinasch.unsaga.core.entity.projectile.custom_arrow.SpecialArrowType;
import mods.hinasch.unsaga.core.entity.projectile.custom_arrow.CustomArrowCapability;
import mods.hinasch.unsaga.core.entity.projectile.custom_arrow.ICustomArrow;
import mods.hinasch.unsaga.core.inventory.AccessorySlotCapability;
import mods.hinasch.unsaga.core.item.weapon.ItemAxeUnsaga;
import mods.hinasch.unsaga.core.item.weapon.ItemGloveUnsaga;
import mods.hinasch.unsaga.core.item.weapon.ItemKnifeUnsaga;
import mods.hinasch.unsaga.core.item.weapon.ItemShieldUnsaga;
import mods.hinasch.unsaga.core.item.weapon.ItemSpearUnsaga;
import mods.hinasch.unsaga.core.item.weapon.ItemStaffUnsaga;
import mods.hinasch.unsaga.core.potion.PotionUnsaga;
import mods.hinasch.unsaga.core.potion.StateCombo;
import mods.hinasch.unsaga.core.potion.UnsagaPotions;
import mods.hinasch.unsaga.damage.AdditionalDamageData;
import mods.hinasch.unsaga.damage.DamageHelper;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.General;
import mods.hinasch.unsaga.damage.DamageTypeUnsaga.Sub;
import mods.hinasch.unsaga.lp.LPLogicProcessor;
import mods.hinasch.unsaga.lp.LifePoint;
import mods.hinasch.unsaga.lp.event.EventLPInvulnerability;
import mods.hinasch.unsaga.material.UnsagaMaterialCapability;
import mods.hinasch.unsaga.minsaga.EventFittingProgressMinsaga;
import mods.hinasch.unsaga.skillpanel.EventSaveDamage;
import mods.hinasch.unsaga.skillpanel.EventSkillPanel;
import mods.hinasch.unsaga.skillpanel.SkillPanelAPI;
import mods.hinasch.unsaga.skillpanel.SkillPanels;
import mods.hinasch.unsaga.status.UnsagaStatus;
import mods.hinasch.unsaga.util.AdditionalDamageCache;
import mods.hinasch.unsaga.util.LivingHelper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityEvokerFangs;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

/**
 *
 * Attack->Hurt->Damage->Deathの順番
 *
 */
public class DamageEventsUnsaga {


	@SubscribeEvent
	public void onKnockback(LivingKnockBackEvent e){
		if(e.getAttacker() instanceof EntityLivingBase){ //短剣はノックバックを小さく
			ItemStack held = ((EntityLivingBase)e.getAttacker()).getHeldItemMainhand();
			if(!held.isEmpty() && held.getItem() instanceof ItemKnifeUnsaga){
				e.setStrength(e.getOriginalStrength()*0.5F);
				return;
			}
			if(e.getEntityLiving().isPotionActive(UnsagaPotions.COMBO)){
				e.setStrength(e.getOriginalStrength() * 0.5F);
				return;
			}
		}
	}
	@SubscribeEvent
	public void onLivingDeath(LivingDeathEvent e){
		if(EventLPInvulnerability.onLivingDeath(e)){
			AccessorySlotCapability.onLivingDeath(e); //アクセサリ死亡時ばらまき
			EventAbilityLearning.onLivingDeath(e); //アビリティの引き出し
			EventSkillPanel.onLivingDeath(e); //主にネガティブスキル
		}

	}
	@SubscribeEvent
	public void onLivingAttack(LivingAttackEvent e){
//		UnsagaMod.logger.trace("livingattack", "called");
		if(!e.getSource().isUnblockable() && e.getEntityLiving().isActiveItemStackBlocking() && e.getEntityLiving() instanceof EntityPlayer){
			this.shieldProcess(e);
		}
		EventAbilityLearning.onLivingAttack(e); //ひらめきイベント
		EventSaveDamage.onAttack(e); //節約スキルの実装 TODO:ItemDamageEventに移動
		EventUnlockMagic.INSTANCE.onAttack(e); //魔法鍵のアンロック
	}
	@SubscribeEvent
	public void onLivingHurt(LivingHurtEvent e){
//		UnsagaMod.logger.trace("livinghurt", "called");

		ItemGloveUnsaga.onLivingAttack(e);


		if(AdditionalDamageCache.getData(e.getSource())==null){
			AdditionalDamageData data = figureBaseAdditionalDamage(e);
			figureDamageSubType(e.getSource(), data);
			AdditionalDamageCache.addCache(e.getSource(), data);
		}
		//		if(AdditionalDamageAttributeCapability.adapter.getCapability(e.getEntityLiving()).getData()==null){
		//			AdditionalDamageData data = new AdditionalDamageData(e.getSource(),General.PUNCH,1.0F);
		//			AdditionalDamageAttributeCapability.adapter.getCapability(e.getEntityLiving()).setData(data);
		//		}

		if(EventFittingProgressMinsaga.apply(e)){
			EventFittingProgressMinsaga.process(e); //武器補強改造の定着プロセス
		}


		UnsagaPotions.onEntityHurt(e); // 被ダメ時ポーション反応
		LivingHelper.onEntityHurt(e); //ステイトの実装
		StateCombo.onDamage(e);
	}

	@SubscribeEvent
	public void onLivingDamage(LivingDamageEvent e){
//		UnsagaMod.logger.trace("livingdamage", "called");
		double rawDamage = e.getAmount();
		AdditionalDamageData data = AdditionalDamageCache.getData(e.getSource());
		if(data!=null){

			if(UnsagaMod.configs.getDifficulty().enableDamageMultiplyToWounded() && !this.isVictimPlayerTeam(e.getEntityLiving())){
				if(e.getEntityLiving().getHealth()<=0.1F || LifePoint.adapter.getCapability(e.getEntityLiving()).getLifePoint()<=0){
					e.setAmount(e.getAmount()*1.5F);
				}
			}

			EventSkillPanel.onLivingDamage(e);

			double armor = DamageHelper.getEntityMinArmorValue(data, e.getEntityLiving());
			if(armor<DamageHelper.getEntityMaxArmorValue(data, e.getEntityLiving())){
				armor = (armor + DamageHelper.getEntityMaxArmorValue(data, e.getEntityLiving()))/2;
			}
			float additional = getAdditionalDamageByStatus(1.0F,(float) armor,e.getAmount(),true);
			float appliedDamage = MathHelper.clamp(e.getAmount() + additional, 0, 65535F);


			//精神による魔法攻撃の特防
			float mentalGuard = e.getSource().isMagicDamage() ? getAdditionalDamageByStatus(1.0F,(float)e.getEntityLiving().getEntityAttribute(UnsagaStatus.MENTAL).getAttributeValue(),e.getAmount(),true) : 0;
			//攻撃者の知性による特攻
			float intelligencePower = 0;
			if(e.getSource().getTrueSource() instanceof EntityLivingBase && e.getSource().isMagicDamage()){
				EntityLivingBase attacker = (EntityLivingBase) e.getSource().getTrueSource();
				intelligencePower = getAdditionalDamageByStatus(1.0F,(float)attacker.getEntityAttribute(UnsagaStatus.INTELLIGENCE).getAttributeValue(),e.getAmount(),false);
			}



			e.setAmount(appliedDamage+mentalGuard+intelligencePower);



			if(e.getEntityLiving().isActiveItemStackBlocking() && e.getEntityLiving() instanceof EntityPlayer){
				this.shieldAbilityProcess(e, data);
			}


			for(PotionEffect effect:e.getEntityLiving().getActivePotionEffects()){
				if(effect.getPotion() instanceof PotionUnsaga){
					PotionUnsaga pu = (PotionUnsaga) effect.getPotion();
					pu.affectOnDamage(e,data,effect.getAmplifier());
				}
			}



			String mes1 = "Attacker:%s Source:%s Class:%s";
			String mes2 = "Raw:%.2f Amount:%.2f LP攻撃力:%.2f LP攻撃回数:%d,Type:%s";
			String mes3 = "残りHealth:%.2f 残りLP:%d 防御力:%.2f ";
			String mes4 = "能力:斬%.2f 殴%.2f 突%.2f 魔%.2f 精神特防:%.2f 攻撃者知性特攻:%.2f";
			EntityPlayer ep = e.getSource().getTrueSource() instanceof EntityPlayer ?(EntityPlayer) e.getSource().getTrueSource() : e.getEntityLiving() instanceof EntityPlayer ? (EntityPlayer)e.getEntityLiving() : null;
			//デバッグ用
			if(ep!=null && HSLib.isDebug()){
				String itemclass = ep.getHeldItemMainhand().isEmpty() ? "none" : ep.getHeldItemMainhand().getItem().getClass().getSimpleName();
				ChatHandler.sendChatToPlayer(ep, String.format(mes1, getEntityNameSafe(e.getSource().getTrueSource()),getEntityNameSafe(e.getSource().getImmediateSource()),itemclass));
				ChatHandler.sendChatToPlayer(ep, String.format(mes2,rawDamage, e.getAmount(),data.getLPAttribute().amount(),data.getLPAttribute().chances(),data.getDamageTypes()));
				ChatHandler.sendChatToPlayer(ep, String.format(mes3, e.getEntityLiving().getHealth(),LifePoint.adapter.getCapability(e.getEntityLiving()).getLifePoint(),armor));
				EntityLivingBase el = e.getEntityLiving();
				ChatHandler.sendChatToPlayer(ep, String.format(mes4, el.getEntityAttribute(UnsagaStatus.GENERALS.get(General.SWORD)).getAttributeValue(),el.getEntityAttribute(UnsagaStatus.GENERALS.get(General.PUNCH)).getAttributeValue()
						,el.getEntityAttribute(UnsagaStatus.GENERALS.get(General.SPEAR)).getAttributeValue(),el.getEntityAttribute(UnsagaStatus.GENERALS.get(General.MAGIC)).getAttributeValue()
						,mentalGuard,intelligencePower));
			}



			//LP操作
			LPLogicProcessor.tryDecrLP(e.getEntityLiving().getRNG(), e.getEntityLiving(), e.getSource(), e.getAmount(),data);
			//追加属性のキャッシュ消去
			AdditionalDamageCache.removeCache(e.getSource());
		}

		//		AdditionalDamageData data = AdditionalDamageAttributeCapability.adapter.getCapability(e.getEntityLiving()).getData();
		//		AdditionalDamageAttributeCapability.adapter.getCapability(e.getEntityLiving()).setData(null);
	}

	/**
	 * 盾スキルがあるとスキルが発動
	 * @param e
	 * @param data
	 */
	private void shieldAbilityProcess(LivingDamageEvent e,AdditionalDamageData data){
//		UnsagaMod.logger.trace("cllaed", "call");
		EntityPlayer ep = (EntityPlayer) e.getEntityLiving();
		ItemStack shield = ep.getActiveItemStack();
		Random rand = ep.getRNG();
		if(SkillPanelAPI.hasPanel(ep, SkillPanels.SHIELD)){
//			UnsagaMod.logger.trace("cllaed", "call2");
			if(ep.getActiveItemStack().getItem() instanceof ItemShieldUnsaga){
				int prob = UnsagaMaterialCapability.adapter.getCapability(shield).getMaterial().getShieldPower() +(5 * SkillPanelAPI.getHighestPanelLevel(ep, SkillPanels.SHIELD).getAsInt());
				float reduce = (33 + UnsagaMaterialCapability.adapter.getCapability(shield).getMaterial().getShieldPower()) * 0.01F;
				List<AbilityShield> abilities = AbilityAPI.getMatchedShieldAbility(shield, data);
				if(!abilities.isEmpty()){ //マッチする盾アビがあった→軽減可能
					e.setAmount(e.getAmount()*reduce);
				}
				Optional<AbilityShield> ability = abilities.stream().reduce((a,b)->a.getBlockableValue()>b.getBlockableValue() ? a : b);
				if(ability.isPresent()){
					//マッチするアビリティの中から一番発動率の高いアビリティが発動したかどうか
					if(prob*ability.get().getBlockableValue()>rand.nextInt(100)){
						HSLib.getPacketDispatcher().sendTo(PacketSound.atEntity(SoundEvents.ITEM_SHIELD_BLOCK, ep), (EntityPlayerMP) ep);
						ItemShieldUnsaga.damageShield(ep, e.getAmount());
						e.setAmount(0);
					}

				}
//				if(AbilityAPI.hasAbility(shield,AbilityRegistry.GUARD_ALL_RANGE) && !e.getSource().isUnblockable()){
//					if(prob>ep.getRNG().nextInt(100)){
//						HSLib.getPacketDispatcher().sendTo(PacketSound.atEntity(SoundEvents.ITEM_SHIELD_BLOCK, ep), (EntityPlayerMP) ep);
//						ItemShieldUnsaga.damageShield(ep, e.getAmount());
//						e.setAmount(0);
//					}
//				}
//				if(AbilityAPI.hasAbility(shield,AbilityRegistry.BLOCK_FIRE_FREEZE_ELECTRIC) && e.getSource().isMagicDamage()){
//					if(prob>ep.getRNG().nextInt(100)){
//						HSLib.getPacketDispatcher().sendTo(PacketSound.atEntity(SoundEvents.ITEM_SHIELD_BLOCK, ep), (EntityPlayerMP) ep);
//						ItemShieldUnsaga.damageShield(ep, e.getAmount());
//						e.setAmount(0);
//					}
//				}
			}
		}
	}
	private void shieldProcess(LivingAttackEvent e){


		EntityPlayer ep = (EntityPlayer) e.getEntityLiving();
		ItemStack stack = e.getEntityLiving().getActiveItemStack();
		if(stack.getItem() instanceof ItemShieldUnsaga && ItemShieldUnsaga.canBlockDamageSource(ep, e.getSource())){
			if(UnsagaMaterialCapability.adapter.hasCapability(stack)){
				//					int shieldPower = UnsagaMaterialCapability.adapter.getCapability(stack).getMaterial().getShieldPower();
				//					float reduce = (1+shieldPower*shieldPower)/5000;
				//					reduce = (float) MathHelper.clamp(reduce, 0.1D, 0.9D);
				//					float shieldDamage = e.getAmount() - (e.getAmount()*reduce);
				if(e.getAmount()>0.0D){

					Method method = ReflectionHelper.findMethod(EntityPlayer.class, "damageShield", "func_184590_k", Float.class);
//					ItemShieldUnsaga.damageShield(ep, e.getAmount());
					try {
						method.invoke(e.getAmount());
					} catch (IllegalAccessException e1) {
						// TODO 自動生成された catch ブロック
						e1.printStackTrace();
					} catch (IllegalArgumentException e1) {
						// TODO 自動生成された catch ブロック
						e1.printStackTrace();
					} catch (InvocationTargetException e1) {
						// TODO 自動生成された catch ブロック
						e1.printStackTrace();
					}
				}

			}


		}
	}
	private float getAdditionalDamageByStatus(float threshold,float value,float damage,boolean isArmorStatus){
		float f = threshold - value;
		if(!isArmorStatus){
			return -(damage*f);
		}
		return damage * f;

	}
	private boolean isVictimPlayerTeam(EntityLivingBase victim){
		if(victim instanceof EntityPlayer){
			return true;
		}
		if(victim instanceof EntityTameable){
			if(((EntityTameable) victim).getOwner() instanceof EntityPlayer){
				return true;
			}
		}
		return false;
	}

	//デバグ用
	private String getEntityNameSafe(Entity el){
		return el!=null ? el.getName() : "NULL";
	}

	//バニラデータ・他modからの推測
	public static AdditionalDamageData figureBaseAdditionalDamage(LivingHurtEvent e){
		final DamageSource ds = e.getSource();
		if(ds==DamageSource.CACTUS){
			return new AdditionalDamageData(ds,General.SPEAR, 0.1F,1);
		}
		if(ds==DamageSource.ON_FIRE){
			return new AdditionalDamageData(ds,General.MAGIC, 0F).setSubTypes(Sub.FIRE);
		}
		if(ds==DamageSource.IN_FIRE){
			return new AdditionalDamageData(ds,General.MAGIC, 0F).setSubTypes(Sub.FIRE);
		}
		if(ds==DamageSource.LAVA){
			return new AdditionalDamageData(ds,General.MAGIC, 0.1F).setSubTypes(Sub.FIRE);
		}
		if(ds==DamageSource.FIREWORKS){
			return new AdditionalDamageData(ds,General.MAGIC, 0.1F).setSubTypes(Sub.FIRE);
		}
		if(ds==DamageSource.HOT_FLOOR){
			return new AdditionalDamageData(ds,General.MAGIC, 0.1F).setSubTypes(Sub.FIRE);
		}
		if(ds==DamageSource.DRAGON_BREATH){
			return new AdditionalDamageData(ds,General.MAGIC, 0.1F).setSubTypes(Sub.FIRE,Sub.SHOCK);
		}
		if(ds==DamageSource.LIGHTNING_BOLT){
			return new AdditionalDamageData(ds,General.MAGIC, 0.1F).setSubTypes(Sub.ELECTRIC);
		}
		if(ds==DamageSource.WITHER){
			return new AdditionalDamageData(ds,General.MAGIC, 0F);
		}
		if(ds==DamageSource.STARVE){
			return new AdditionalDamageData(ds,General.MAGIC, 0F);
		}
		if(ds==DamageSource.FALL){
			return new AdditionalDamageData(ds,General.PUNCH, 3.0F,2);
		}
		if(ds==DamageSource.ANVIL){
			return new AdditionalDamageData(ds,General.PUNCH, 3.0F,2);
		}
		if(ds.getTrueSource()==null && ds.isMagicDamage()){
			return new AdditionalDamageData(ds,General.MAGIC, 0.0F,1);
		}
		if(ds.getImmediateSource() instanceof EntityArrow){
			if(CustomArrowCapability.ADAPTER.hasCapability(ds.getImmediateSource())){
				EntityArrow arrow = (EntityArrow) ds.getImmediateSource();
				ICustomArrow instance = CustomArrowCapability.ADAPTER.getCapability(arrow);
				if(instance.getArrowType()!=SpecialArrowType.NONE && ds.getTrueSource() instanceof EntityLivingBase){
					instance.getArrowType().onLivingHurt(e);
					return instance.getArrowType().getDamageSource((EntityLivingBase) ds.getTrueSource(), arrow);
				}
			}
			return new AdditionalDamageData(ds,General.SPEAR,0.65F);
		}
		if(ds.getImmediateSource() instanceof IProjectile){
			return new AdditionalDamageData(ds,0.5F,1,General.PUNCH,General.SPEAR);
		}
		if(ds.isExplosion()){
			return new AdditionalDamageData(ds,General.MAGIC,1.5F,2).setSubTypes(Sub.FIRE,Sub.SHOCK);
		}
		if(ds.getImmediateSource() instanceof EntityPotion){
			return new AdditionalDamageData(ds,General.MAGIC,0.5F,1);
		}
		if(ds.getDamageType().equals("thorns")){
			return new AdditionalDamageData(ds,0.4F,1,General.SPEAR);
		}
		if(ds.getImmediateSource() instanceof EntityEvokerFangs){
			return new AdditionalDamageData(ds,0.5F,1,General.SWORD,General.PUNCH);
		}
		if(ds.getTrueSource() instanceof EntitySpider){
			return new AdditionalDamageData(ds,0.5F,1,General.SWORD,General.PUNCH);
		}
		if(ds.getTrueSource() instanceof EntityGuardian){
			return new AdditionalDamageData(ds,General.MAGIC,0.5F).setSubTypes(Sub.SHOCK,Sub.ELECTRIC);
		}
		if(ds.getTrueSource() instanceof EntityLivingBase){
			EntityLivingBase living = (EntityLivingBase) ds.getTrueSource();
			ItemStack held = living.getHeldItemMainhand();
			if(held.isEmpty()){
				return new AdditionalDamageData(ds,General.PUNCH,0F);
			}

			ResourceLocation clazz = new ResourceLocation(held.getItem().getClass().getSimpleName());
			if(map.containsKey(clazz)){
				return map.get(clazz).apply(ds);
			}

			if(held.getItem() instanceof ItemGloveUnsaga){
				return new AdditionalDamageData(ds,0.3F,1,General.PUNCH);
			}
			if(held.getItem() instanceof ItemAxe){
				return new AdditionalDamageData(ds,0.6F,1,General.PUNCH,General.SWORD);
			}
			if(held.getItem() instanceof ItemStaffUnsaga){
				return new AdditionalDamageData(ds,0.4F,1,General.PUNCH);
			}
			if(held.getItem() instanceof ItemSpearUnsaga){
				return new AdditionalDamageData(ds,0.65F,1,General.SPEAR);
			}
			if(held.getItem() instanceof ItemKnifeUnsaga){
				return new AdditionalDamageData(ds,0.7F,1,General.SPEAR);
			}
			if(held.getItem() instanceof ItemAxeUnsaga){
				return new AdditionalDamageData(ds,0.6F,1,General.PUNCH,General.SWORD);
			}
			if(held.getItem() instanceof ItemSword){
				return new AdditionalDamageData(ds,0.5F,1,General.SWORD);
			}
			if(held.getItem() instanceof ItemPickaxe){
				return new AdditionalDamageData(ds,0.2F,1,General.SPEAR,General.PUNCH);
			}
			if(held.getItem() instanceof Item){
				if(ds.isMagicDamage()){
					return new AdditionalDamageData(ds,0.5F,1,General.MAGIC);
				}
				return new AdditionalDamageData(ds,0.5F,1,General.SWORD);
			}
		}

		if(ds.isMagicDamage()){
			return new AdditionalDamageData(ds,0.5F,1,General.MAGIC);
		}
		return new AdditionalDamageData(ds,0.5F,1,General.SWORD);
	}

	public static void figureDamageSubType(DamageSource ds,AdditionalDamageData ad){
		if(ds.isFireDamage()){
			ad.setSubTypes(Sub.FIRE);
		}
		if(ds.getTrueSource() instanceof EntityLivingBase){
			EntityLivingBase el = (EntityLivingBase) ds.getTrueSource();
			ItemStack held = el.getHeldItemMainhand();
			if(!held.isEmpty()){
				if(EnchantmentHelper.getFireAspectModifier(el)>0){
					ad.setSubTypes(Sub.FIRE);
				}
			}
		}
	}

	public static Map<ResourceLocation,Function<DamageSource,AdditionalDamageData>> map = Maps.newHashMap();
	static{
		put("BattleAxe", ds -> new AdditionalDamageData(ds,0.5F,1,General.PUNCH,General.SWORD));
		put("BattleSign", ds -> new AdditionalDamageData(ds,0.3F,1,General.PUNCH));
		put("BroadSword", ds -> new AdditionalDamageData(ds,0.5F,1,General.SWORD));
		put("Cleaver", ds -> new AdditionalDamageData(ds,0.5F,1,General.PUNCH,General.SWORD));
		put("FryPan", ds -> new AdditionalDamageData(ds,0.3F,1,General.PUNCH));
		put("LongSword", ds -> new AdditionalDamageData(ds,0.5F,1,General.SWORD));
		put("Rapier", ds -> new AdditionalDamageData(ds,0.6F,1,General.SPEAR));
		put("Excavotor", ds -> new AdditionalDamageData(ds,0.5F,1,General.SWORD));
		put("Hammer", ds -> new AdditionalDamageData(ds,0.3F,1,General.PUNCH));
		put("Hatchet", ds -> new AdditionalDamageData(ds,0.6F,1,General.SPEAR));
		put("Kama", ds -> new AdditionalDamageData(ds,0.5F,1,General.SWORD));
		put("LumberAxe", ds -> new AdditionalDamageData(ds,0.5F,1,General.PUNCH,General.SWORD));
		put("Mattock", ds -> new AdditionalDamageData(ds,0.5F,1,General.PUNCH,General.SPEAR));
		put("Pickaxe", ds -> new AdditionalDamageData(ds,0.5F,1,General.PUNCH,General.SPEAR));
		put("Scythe", ds -> new AdditionalDamageData(ds,0.5F,1,General.SWORD));
		put("Shovel", ds -> new AdditionalDamageData(ds,0.3F,1,General.PUNCH));
		put("Arrow", ds -> new AdditionalDamageData(ds,0.6F,1,General.SPEAR));
		put("Bolt", ds -> new AdditionalDamageData(ds,0.6F,1,General.SPEAR));
		put("Shuriken", ds -> new AdditionalDamageData(ds,0.6F,1,General.SPEAR));
	}

	public static void put(String str,Function<DamageSource,AdditionalDamageData> func){
		map.put(new ResourceLocation(str), func);
	}
}
