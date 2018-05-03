package mods.hinasch.unsaga.ability;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import com.mojang.realmsclient.util.Pair;

import mods.hinasch.lib.core.HSLib;
import mods.hinasch.lib.item.ItemUtil;
import mods.hinasch.lib.network.PacketSound;
import mods.hinasch.lib.network.PacketSyncCapability;
import mods.hinasch.lib.util.ChatHandler;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.lib.util.UtilNBT;
import mods.hinasch.lib.world.WorldHelper;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.ability.specialmove.SparklingPointRegistry;
import mods.hinasch.unsaga.core.advancement.UnsagaTriggers;
import mods.hinasch.unsaga.skillpanel.SkillPanelAPI;
import mods.hinasch.unsaga.skillpanel.SkillPanels;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class EventAbilityLearning {

	public static void onLivingAttack(LivingAttackEvent e){
		if(e.getSource().getTrueSource() instanceof EntityPlayer){
			if(e.getEntityLiving() instanceof IMob){
				onPlayerAttackMob(e);
			}
		}
	}

	private static double calcSparklingModifierBySkill(EntityPlayer player,double d){
		double base = 0.0D;
		if(SkillPanelAPI.getHighestPanelLevel(player,SkillPanels.ARTISTE).isPresent()){
			base += 0.01D * (double)SkillPanelAPI.getHighestPanelLevel(player, SkillPanels.ARTISTE).getAsInt();
		}
		return base;
	}
	private static void onPlayerAttackMob(LivingAttackEvent e){
		if(WorldHelper.isClient(e.getEntityLiving().getEntityWorld())){
			return;
		}
		EntityPlayer player = (EntityPlayer) e.getSource().getTrueSource();
		EntityLivingBase mob = e.getEntityLiving();
		Random rand = player.getEntityWorld().rand;
		double sparkling = SparklingPointRegistry.instance().getSparklingPoint(mob);
		sparkling += calcSparklingModifierBySkill(player, 0.02D);
		float f = rand.nextFloat();
		if(f<sparkling ){

			ItemStack weapon = player.getHeldItem(EnumHand.MAIN_HAND);
			if(ItemUtil.isItemStackNull(weapon)){
				return;
			}
			//弓はちょっとひらめきやすく
			if(weapon.getItem() instanceof ItemBow){
				sparkling += 0.10F;
			}
			UnsagaMod.logger.trace(EventAbilityLearning.class.getClass().getName(), AbilityAPI.existLearnableAbility(weapon));
			if(AbilityAPI.existLearnableAbility(weapon)){
				Optional<IAbility> learned = AbilityAPI.learnRandomAbility(player.getRNG(), weapon);
				if(learned.isPresent()){
					HSLib.getPacketDispatcher().sendTo(PacketSyncCapability.create(AbilityCapability.CAPA, AbilityCapability.adapter.getCapability(weapon)),(EntityPlayerMP) player );
					HSLib.getPacketDispatcher().sendTo(PacketSound.atEntity(SoundEvents.BLOCK_ANVIL_PLACE, player), (EntityPlayerMP) player);
					String msg = HSLibs.translateKey("ability.unsaga.sparkling.specialMove", learned.get().getLocalized());
					ChatHandler.sendChatToPlayer(player, msg);
					UnsagaTriggers.LEARN_SPECIALMOVE.trigger((EntityPlayerMP) player);
//					player.addStat(UnsagaAchievementRegistry.instance().learnSkillFirst);
				}

			}
		}
	}

	public static void onLivingDeath(LivingDeathEvent e){
		if(e.getSource().getTrueSource() instanceof EntityPlayer){
			if(e.getEntityLiving() instanceof IMob && e.getEntityLiving().getMaxHealth()>=4.0F){
				onPlayerKilledMob(e);
			}
		}
	}

	private static void onPlayerKilledMob(LivingDeathEvent e){
		if(WorldHelper.isClient(e.getEntityLiving().getEntityWorld())){
			return;
		}
		EntityPlayer player = (EntityPlayer) e.getSource().getTrueSource();
		EntityLivingBase mob = e.getEntityLiving();
		Random rand = player.getEntityWorld().rand;
		double sparkling = SparklingPointRegistry.instance().getSparklingPoint(mob);
		sparkling += calcSparklingModifierBySkill(player, 0.05D);
		sparkling += 0.1D;
		float f = rand.nextFloat();
		UnsagaMod.logger.trace("sparkling", f,sparkling);
		if(f<sparkling){
//		if(true){
			List<Pair<AbilityAPI.EquipmentSlot,ItemStack>> list = AbilityAPI.getAllEquippedArmors(player).stream().filter(in -> AbilityAPI.existLearnableAbility(in.second())).collect(Collectors.toList());
//			List<ItemStack> list2 = list.stream().filter(in -> AbilityCapability.adapter.hasCapability(in)).collect(Collectors.toList());
			UnsagaMod.logger.trace("spkarling", list.stream().map(in -> in.second()).collect(Collectors.toList()));
			if(!list.isEmpty()){
				Pair<AbilityAPI.EquipmentSlot,ItemStack> sparkItem = HSLibs.randomPick(rand, list);
				Optional<IAbility> learned = AbilityAPI.learnRandomAbility(player.getRNG(), sparkItem.second());
				if(learned.isPresent()){
					NBTTagCompound nbt = UtilNBT.compound();
					nbt.setString("slot", sparkItem.first().getName());
					HSLib.getPacketDispatcher().sendTo(PacketSyncCapability.create(AbilityCapability.CAPA, AbilityCapability.adapter.getCapability(sparkItem.second()),nbt),(EntityPlayerMP) player );
					HSLib.getPacketDispatcher().sendTo(PacketSound.atEntity(SoundEvents.BLOCK_ANVIL_PLACE, player), (EntityPlayerMP) player);
					String msg = HSLibs.translateKey("ability.unsaga.sparkling.passive", sparkItem.second().getDisplayName(),learned.get().getLocalized());
					ChatHandler.sendChatToPlayer(player, msg);
					UnsagaTriggers.LEARN_ABILITY.trigger((EntityPlayerMP) player);
//					player.addStat(UnsagaAchievementRegistry.instance().gainAbilityFirst);
				}

			}


		}
	}
}
