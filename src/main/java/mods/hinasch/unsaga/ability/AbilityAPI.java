package mods.hinasch.unsaga.ability;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.mojang.realmsclient.util.Pair;

import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.unsaga.ability.specialmove.Tech;
import mods.hinasch.unsaga.core.inventory.AccessorySlotCapability;
import mods.hinasch.unsaga.damage.AdditionalDamageTypes;
import mods.hinasch.unsaga.material.UnsagaMaterial;
import mods.hinasch.unsaga.material.UnsagaMaterialCapability;
import mods.hinasch.unsaga.util.ToolCategory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Tuple;

public class AbilityAPI {


	public static enum EquipmentSlot{
		MAIN_HAND(EntityEquipmentSlot.MAINHAND),
		OFF_HAND(EntityEquipmentSlot.OFFHAND),
		CHEST(EntityEquipmentSlot.CHEST),
		HELMET(EntityEquipmentSlot.HEAD),
		FEET(EntityEquipmentSlot.FEET),
		LEGS(EntityEquipmentSlot.LEGS),
		ACCESSORY_1(null),
		ACCESSORY_2(null);

		private @Nullable EntityEquipmentSlot vanillaslot;

		private EquipmentSlot(EntityEquipmentSlot slot){
			this.vanillaslot = slot;
		}

		public boolean isHand(){
			return this.vanillaslot!=null ? this.vanillaslot.getSlotType()==EntityEquipmentSlot.Type.HAND : false;
		}
		public boolean isAccessory(){
			return this==ACCESSORY_1 || this==ACCESSORY_2;
		}
		public String getName(){
			if(this.vanillaslot!=null){
				return this.vanillaslot.getName();
			}
			return this==ACCESSORY_1 ? "accessory_1" : "accessory_2";
		}

		public ItemStack getStackFrom(EntityLivingBase living){
			if(this.vanillaslot!=null){
				return living.getItemStackFromSlot(vanillaslot);
			}
			if(this.isAccessory() && AccessorySlotCapability.adapter.hasCapability(living)){
				return AccessorySlotCapability.adapter.getCapability(living).getEquippedList().get(this==ACCESSORY_1 ? 0 : 1);
			}
			return ItemStack.EMPTY;
		}
		public static EquipmentSlot fromName(String name){
			for(EquipmentSlot slot:EquipmentSlot.values()){
				if(slot.getName().equals(name)){
					return slot;
				}
			}
			return null;
		}
	}
	static final EntityEquipmentSlot ACCESSORY = null;
	public static Optional<Tech> getLearnedSpecialMove(ItemStack is){
		return getAttachedAbilities(is).stream().filter(in -> in instanceof Tech).map(in -> (Tech)in).findFirst();
	}


	/**
	 * 覚えているアビリティを取得
	 * @param is
	 * @return
	 */
	public static List<IAbility> getAttachedAbilities(ItemStack is){
		if(AbilityCapability.adapter.hasCapability(is)){
			return AbilityCapability.adapter.getCapability(is).getLearnedAbilities();
		}
		return Lists.newArrayList();
	}
	public static List<AbilityShield> getMatchedShieldAbility(ItemStack is,AdditionalDamageTypes other){
		return getAttachedPassiveAbilities(is).stream().filter(in -> in instanceof AbilityShield).map(in ->(AbilityShield)in).filter(in -> other.hasIntersection(in.getBlockableTypes())).collect(Collectors.toList());
	}

	/**
	 * 覚えているアビリティの内からパッシブアビリティを抜き出す。{@link #getAttachedAbilities(ItemStack)}を使用
	 * @param is
	 * @return
	 */
	public static List<Ability> getAttachedPassiveAbilities(ItemStack is){
		if(AbilityCapability.adapter.hasCapability(is)){
			return AbilityCapability.adapter.getCapability(is).getLearnedAbilities().stream().filter(in -> in instanceof Ability).map(in ->(Ability)in).collect(Collectors.toList());
		}
		return Lists.newArrayList();
	}
	/**
	 * そのアイテムで覚える事のできるアビリティを返す。
	 * @param is
	 * @return
	 */
	public static AbilityLearnableTable getAbilityTable(ItemStack is){
		if(AbilityCapability.adapter.hasCapability(is)){
			IAbilityAttachable instance = AbilityCapability.adapter.getCapability(is);
			if(instance.isUniqueItem()){
				return new AbilityLearnableTable(instance.getLearnableUniqueAbilities());
			}

			return AbilityAssociateRegistry.instance().getAbilityList(is);

		}
		return AbilityLearnableTable.EMPTY;
	}

	public static String getAbilityTableString(ItemStack is){
		return getAbilityTable(is).stream().map(in -> in.stream().map(i -> i.getLocalized()).collect(Collectors.joining(",")))
		.map(in ->"["+in+"]").collect(Collectors.joining());
	}
	public static boolean existLearnableAbility(ItemStack is){
		if(AbilityCapability.adapter.hasCapability(is)){
			AbilityLearnableTable table = getAbilityTable(is);
			NonNullList<IAbility> learned = AbilityCapability.adapter.getCapability(is).getLearnedAbilities();
//			UnsagaMod.logger.trace("size", learned.size(),AbilityCapability.adapter.getCapability(is).getMaxAbilitySize());
			for(int i=0;i<learned.size();i++){
//				UnsagaMod.logger.trace("learned", learned.get(i),learned.get(i).isAbilityEmpty());
//				UnsagaMod.logger.trace("table",table);
				if(learned.get(i).isAbilityEmpty()){

					if(table.hasLearnableAbilityAt(i)){
						return true;
					}

				}
			}
		}
		return false;
	}
	public static AbilityRegistry ability(){
		return AbilityRegistry.instance();
	}

	public static IAbility getAbilityByID(String id){
		return AbilityRegistry.instance().get(id);
	}


	/**
	 * 回復アビリティの効果量を全て足した数を返す。最小０。
	 * @param el
	 * @return
	 */
	public static int getHealAbilityAmount(EntityLivingBase el){
		return getEffectiveAllAbilities(el).stream().filter(in -> in instanceof AbilityNaturalHeal)
		.map(in -> (AbilityNaturalHeal)in).mapToInt(in ->in.getHealAmount()).sum();
	}

	public static int getAbilityAmount(EntityLivingBase el,IAbility ab){
		return (int)getEffectiveAllAbilities(el).stream().filter(in -> in==ab).count();
	}

	public static void forgetRandomAbility(Random rand,ItemStack is){
		if(AbilityCapability.adapter.hasCapability(is)){
			List<IAbility> list = AbilityCapability.adapter.getCapability(is).getLearnedAbilities();
			if(!list.isEmpty()){
				IAbility forgetAbility = HSLibs.randomPick(rand, list);
				AbilityCapability.adapter.getCapability(is).removeAbility(forgetAbility);
			}
		}
	}

	public static boolean hasBlockingAbility(ItemStack is){
		return AbilityAPI.getAttachedAbilities(is).stream().anyMatch(in -> in instanceof AbilityBlocking);
	}
	public static boolean hasAbility(ItemStack is,IAbility ab){
		if(AbilityCapability.adapter.hasCapability(is)){
			return AbilityCapability.adapter.getCapability(is).getLearnedAbilities().contains(ab);
		}
		return false;
	}
	public static boolean hasWeaponSpecialMove(ItemStack is){
		if(AbilityCapability.adapter.hasCapability(is) && UnsagaMaterialCapability.adapter.hasCapability(is)){
			if(AbilityCapability.adapter.getCapability(is).getLearnedAbilities().stream().anyMatch(in -> in instanceof Tech)){
				return true;
			}
		}
		return false;
	}

	public static void addAbility(ItemStack is,ToolCategory category,IAbility ab){
		if(category.isWeapon()){
			AbilityCapability.adapter.getCapability(is).setAbility(0, ab);
		}else{

		}
	}
	public static Optional<IAbility> learnRandomAbility(Random rand,ItemStack is){
		if(AbilityCapability.adapter.hasCapability(is) && UnsagaMaterialCapability.adapter.hasCapability(is)){
			if(!existLearnableAbility(is)){
				return Optional.empty();
			}
			ToolCategory cate = ToolCategory.getCategoryFromItem(is.getItem());
			UnsagaMaterial mate = UnsagaMaterialCapability.adapter.getCapability(is).getMaterial();
			AbilityLearnableTable table = getAbilityTable(is);
//			if(cate.isWeapon()){
//				list = getAbilityTable(is);
//				if(hasWeaponSpecialMove(is)){
//					list.removeIf(in -> in instanceof SpecialMove);
//				}
//			}
//			list.removeIf(in -> AbilityCapability.adapter.getCapability(is).getLearnedAbilities().contains(in));

			if(!table.isEmpty()){
				int slot = rand.nextInt(table.size());
				Set<IAbility> set = table.get(slot);
				IAbility learnAbility = HSLibs.randomPick(rand, Lists.newArrayList(set));

				AbilityCapability.adapter.getCapability(is).setAbility(slot,learnAbility);

				return Optional.of(learnAbility);
			}
		}
		return Optional.empty();
	}
	public static List<Pair<AbilityAPI.EquipmentSlot,ItemStack>> getAllEquippedArmors(EntityLivingBase el){
		return getAllEquippedItems(el,true);
	}

	public static List<Pair<AbilityAPI.EquipmentSlot,ItemStack>> getAllEquippedItems(EntityLivingBase el,boolean ignoreHelds){
		List<Pair<AbilityAPI.EquipmentSlot,ItemStack>> stacks = Lists.newArrayList();
		for(AbilityAPI.EquipmentSlot slot:AbilityAPI.EquipmentSlot.values()){
			ItemStack is = slot.getStackFrom(el);
			if(!is.isEmpty()){
				if(ignoreHelds){
					if(!slot.isHand()){
						stacks.add(Pair.of(slot,is));
					}
				}else{
					stacks.add(Pair.of(slot,is));
				}

			}
		}

//		if(!ignoreHelds){
//			el.getHeldEquipment().iterator().forEachRemaining(is -> {
//				if(ItemUtil.isItemStackPresent(is)){
//					stacks.add(is);
//				}
//			});
//		}
//
//		el.getArmorInventoryList().forEach(is ->{
//			if(ItemUtil.isItemStackPresent(is)){
//				stacks.add(is);
//			}
//		});
//		if(AccessorySlotCapability.adapter.hasCapability(el)){
//			AccessorySlotCapability.adapter.getCapability(el).getEquippedList().forEach(in ->{
//				if(!in.isEmpty()){
//					stacks.add(Pair.of(ACCESSORY,in));
//				}
//
//			});
//		}
		return stacks;
	}

	public static List<Tuple<IAttribute,Double>> getAllAbilityModifiers(EntityLivingBase el){
		return getEffectiveAllAbilities(el).stream().filter(in -> in instanceof Ability).map(in -> (Ability)in)
		.filter(in -> in.getAttributeModifier()!=null).map(in -> in.getAttributeModifier())
		.collect(Collectors.toList());
	}

	/** パッシブアビリティ(Ability not SpecialMove)を取得する*/
	public static List<Ability> getEffectiveAllPassiveAbilities(EntityLivingBase el){
		return getEffectiveAllAbilities(el).stream().filter(in -> in instanceof Ability).map(in ->(Ability)in).collect(Collectors.toList());
	}
	/**
	 * 身体、手、アクセサリの身につけているもの全部のアビリティを取得
	 * @param el
	 * @return
	 */
	public static List<IAbility> getEffectiveAllAbilities(EntityLivingBase el){
		List<ItemStack> stacks = getAllEquippedItems(el,false).stream().map(in -> in.second()).collect(Collectors.toList());
		List<IAbility> list = Lists.newArrayList();
		if(!stacks.isEmpty()){
			list = stacks.stream().flatMap(in ->{
				List<IAbility> rt = Lists.newArrayList();
				if(AbilityCapability.adapter.hasCapability(in)){
					rt.addAll(AbilityCapability.adapter.getCapability(in).getLearnedAbilities());
				}
				return rt.stream();
			}).filter(in -> in!=AbilityRegistry.empty()).collect(Collectors.toList());

		}

		return list;
	}
}
