//package mods.hinasch.unsaga.core.event;
//
//import mods.hinasch.unsaga.UnsagaMod;
//import mods.hinasch.unsaga.ability.AbilityHelper;
//import mods.hinasch.unsaga.ability.waza.Waza;
//import mods.hinasch.unsaga.capability.IAbility;
//import mods.hinasch.unsaga.capability.IUnsagaPropertyItem;
//import mods.hinasch.unsaga.core.item.misc.ItemSkillBook;
//import mods.hinasch.unsaga.core.item.weapon.base.ComponentUnsagaTool;
//import net.minecraft.item.ItemStack;
//import net.minecraftforge.event.AnvilUpdateEvent;
//import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
//
//
//public class EventTranscriptWazaByAnvil {
//
//	int seed = 0;
//
//	@SubscribeEvent
//	public void onAnvilUpdate(AnvilUpdateEvent e){
////		Unsaga.debug(e.output);
//		//技書の処理
//		if(this.checkStacksOnAnvil(e)){
//
//				if(ItemSkillBook.adapter.getCapability(e.getRight()).getWaza()!=null){
//					e.setCost(5);
//					Waza skill = ItemSkillBook.adapter.getCapability(e.getRight()).getWaza();
//
//					IUnsagaPropertyItem capa = AbilityHelper.getCapability(e.getLeft());
//					if(UnsagaMod.abilities.isSkillSuitable(skill,capa.getToolCategory() ,ComponentUnsagaTool.isHeavy(e.getLeft()))){
//
//						this.transcriptSkillToWeapon(e, skill);
//
//					}
//
//				}
//
//
//
//		}
//
//
//		//技がついてるとコストがあがる
//		if(e.getOutput()!=null){
////			Unsaga.debug(e.output);
//			if(e.getOutput().getItem() instanceof IAbility){
////				AbilityAttacherBase attacher = AbilityAttacherBase.getAttacherFromCategory(null,e.output);
////				Unsaga.debug(attacher.getLearnedAbilitiesAmount());
//				if(!AbilityHelper.getCapability(e.getOutput()).getAbilityList().isEmpty()){
//
//					int cost = e.getCost() + AbilityHelper.getCapability(e.getOutput()).getAbilityList().size();
//					e.setCost(cost);
//				}
//			}
//		}
//
////		int cost = e.cost;
////		if(e.left!=null & e.right!=null){
////			if(!(e.left.getItem() instanceof IUnsagaMaterial))return;
////			if(e.left.getItem().isRepairable()){
////				info = new MaterialInfo(e.right);
////				UnsagaMaterial m1 = null;
////				UnsagaMaterial m2 =  null;
////				if(info.getMaterial().isPresent()){
////					m1 = HelperUnsagaItem.getMaterial(e.left);
////					m2 = info.getMaterial().get();
////
////					Unsaga.debug(m1,m2);
////
////				}
////				if(e.left.getItem()==e.right.getItem()){
////					m1 = HelperUnsagaItem.getMaterial(e.left);
////					m2 = HelperUnsagaItem.getMaterial(e.right);
////
////					int damage = (int) Math.min(Math.floor(getItemDurability(e.left)+getItemDurability(e.right)+(e.left.getMaxDamage()/20)),e.left.getMaxDamage());
////
////				}
////				if((m1!=null && m2!=null) && m1==m2){
////					e.output = e.left;
////					e.cost = cost;
////				}
////			}
////		}
//
//	}
//
//	public boolean checkStacksOnAnvil(AnvilUpdateEvent e){
//		if(e.getRight()!=null && e.getLeft()!=null){
//			if(ItemSkillBook.adapter.hasCapability(e.getRight()) && AbilityHelper.hasCapability(e.getLeft())){
//				return true;
//			}
//		}
//		return false;
//	}
//	protected void transcriptSkillToWeapon(AnvilUpdateEvent e,Waza skill){
////		WazaAttacher attacherLeft = new WazaAttacher(null,e.getLeft(),AbilityHelperNew.getCapabilityLearning(e.getLeft()));
//		if(AbilityHelper.getCapability(e.getLeft()).getAbilityList().isEmpty()){
//			e.setOutput(e.getLeft().copy());
//			AbilityHelper.getCapability(e.getOutput()).addAbility(skill);
//		}
//	}
//	public int getItemDurability(ItemStack is){
//		return is.getMaxDamage() - is.getItemDamage();
//	}
//}
