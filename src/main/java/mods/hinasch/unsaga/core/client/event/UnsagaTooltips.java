package mods.hinasch.unsaga.core.client.event;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;

import joptsimple.internal.Strings;
import mods.hinasch.lib.client.ClientHelper;
import mods.hinasch.lib.core.HSLib;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.unsaga.ability.AbilityAPI;
import mods.hinasch.unsaga.ability.AbilityAssociateRegistry;
import mods.hinasch.unsaga.ability.AbilityCapability;
import mods.hinasch.unsaga.ability.IAbilityAttachable;
import mods.hinasch.unsaga.common.tool.ItemWeaponUnsaga;
import mods.hinasch.unsaga.common.tool.UnsagaToolUtil;
import mods.hinasch.unsaga.material.IUnsagaMaterialSelector;
import mods.hinasch.unsaga.material.UnsagaMaterial;
import mods.hinasch.unsaga.material.UnsagaMaterialCapability;
import mods.hinasch.unsaga.material.UnsagaWeightType;
import mods.hinasch.unsaga.minsaga.MinsagaForgeCapability;
import mods.hinasch.unsaga.minsaga.MinsagaForgeCapability.IMinsagaForge;
import mods.hinasch.unsaga.minsaga.MinsagaForgeCapability.MaterialLayer;
import mods.hinasch.unsaga.minsaga.MinsagaForging;
import mods.hinasch.unsaga.minsaga.MinsagaMaterial;
import mods.hinasch.unsaga.minsaga.MinsagaUtil;
import mods.hinasch.unsaga.util.UnsagaTextFormatting;
import mods.hinasch.unsagamagic.enchant.EnchantmentProperty;
import mods.hinasch.unsagamagic.enchant.UnsagaEnchantmentCapability;
import mods.hinasch.unsagamagic.enchant.UnsagaEnchantmentCapability.EnchantmentState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

public class UnsagaTooltips {

	public static void addTooltips(ItemStack is, List<String> dispList, ITooltipFlag par4,Type... types){
		for(Type type:types){
			type.getTooltip(is, dispList, par4);
		}
	}
	public static void addTooltips(ItemStack is, List<String> dispList, ITooltipFlag par4,Collection<Type> types){
		for(Type type:types){
			type.getTooltip(is, dispList, par4);
		}
	}
	public static enum Type{
		DEBUG,ABILITY,MATERIAL_AND_WEIGHT,ABILITY_TO_LEARN,BOW,SHIELD,ACCESSORY_ARMOR,ENCHANTMENT_REMAIN
		,MINSAGA_MATERIALS,MINSAGA_MODIFIERS,DURABILITY;


		public void getTooltip(ItemStack is, List dispList, ITooltipFlag par4) {
			//Worldがnullの場合もある(==ClientPlayerがnull)ので注意
			UnsagaMaterial m = UnsagaMaterialCapability.adapter.hasCapability(is) ?UnsagaMaterialCapability.adapter.getCapability(is).getMaterial() : null;
			switch(this){
			case ABILITY_TO_LEARN:
				if(AbilityCapability.adapter.hasCapability(is) && ClientHelper.getPlayer()!=null &&ClientHelper.getPlayer().isCreative()){
					String abilityNamesToLearn  = AbilityAPI.getAbilityTableString(is);
					dispList.add(UnsagaTextFormatting.PROPERTY_LOCKED+HSLibs.translateKey("tooltip.unsaga.ability.learnable")+":"+abilityNamesToLearn);
				}
				break;
			case ABILITY:
				if(AbilityCapability.adapter.hasCapability(is)){
					IAbilityAttachable instance = AbilityCapability.adapter.getCapability(is);
					String abilityNames  = instance.getLearnedAbilities().stream().map(in -> in.getLocalized())
							.collect(Collectors.joining("/"));

					dispList.add(HSLibs.translateKey("tooltip.unsaga.ability")+":"+abilityNames);
				}
				break;
			case SHIELD:
				if(m==null)return;
				dispList.add(UnsagaTextFormatting.POSITIVE+HSLibs.translateKey("tooltip.unsaga.shield_avoid", m.getShieldPower()));
				dispList.add(UnsagaTextFormatting.POSITIVE+HSLibs.translateKey("tooltip.unsaga.shield_block", m.getShieldPower()+33));
				break;
			case DEBUG:

				if(HSLib.isDebug() && m!=null){
					dispList.add("Tool Material:"+m.getToolMaterial().name());
					dispList.add("Armor Material:"+m.getArmorMaterial());
					float multiply = 1.0F;
					if(is.getItem() instanceof ItemWeaponUnsaga){
						multiply = ((ItemWeaponUnsaga)is.getItem()).getMaxDamageMultiply();
					}
					if(is.getItem() instanceof IUnsagaMaterialSelector){
						IUnsagaMaterialSelector unsagaweapon = (IUnsagaMaterialSelector) is.getItem();

						dispList.add("Max Uses:"+unsagaweapon.getComponent().getMaxDamage(is) * multiply);
					}
				}

				break;
			case BOW:
				dispList.add(UnsagaTextFormatting.POSITIVE+"Bow Power +"+UnsagaToolUtil.getMaterial(is).getBowModifier());
				break;
			case MATERIAL_AND_WEIGHT:
				if(m==null)return;
				int weight = UnsagaMaterialCapability.adapter.getCapability(is).getWeight();
				UnsagaWeightType weightType = UnsagaWeightType.fromWeight(weight);
				String str = HSLibs.translateKey("tooltip.unsaga.material")+":"+HSLibs.translateKey("material."+m.getPropertyName());
				dispList.add(str);

				String weightString = HSLibs.translateKey("word.weight") + ":" + weight;
				weightString += "(" + weightType.getName() + ")";

				if(weightString!=null){
					dispList.add(weightString);
				}
				break;
			case ACCESSORY_ARMOR:
				if(m!=null){
					int modifier = AbilityAssociateRegistry.instance().getAbilityArmorModifier(m);
					if(modifier>0){
						dispList.add(UnsagaTextFormatting.POSITIVE+HSLibs.translateKey("tooltip.unsaga.accessory_modifier", modifier));
					}
				}
				break;
			case ENCHANTMENT_REMAIN:
				if(ClientHelper.getWorld()!=null){
					UnsagaEnchantmentCapability.adapter.getCapability(is).getEntries().stream()
					.sorted((o1,o2)->o1.getKey().compareTo(o2.getKey()))
					.forEach(in ->{
						EnchantmentProperty p = in.getKey();
						EnchantmentState state = in.getValue();
						String s = p.getLocalized() + " " + (state.getLevel() == 1 ? "" : HSLibs.translateKey("enchantment.level."+state.getLevel()));
						long remain = in.getValue().getExpireTime() - ClientHelper.getWorld().getTotalWorldTime();
						if(remain>0){
							s += "/"+String.format("Time Remaining:%d", (HSLibs.exceptZero((int) remain))/30);
						}

						dispList.add(UnsagaTextFormatting.POSITIVE+s);
					});
				}
				break;
			case MINSAGA_MATERIALS:
				IMinsagaForge instance = MinsagaForgeCapability.ADAPTER.getCapability(is);
				String forgedMaterials = instance.getMaterialLayers().stream().map((in)->{

					if(!in.isEmptyLayer()){
						String color = Strings.EMPTY;
						if(in.hasFinishedFitting()){
							color = UnsagaTextFormatting.POSITIVE.toString();
						}
						return color + in.getMaterial().getLocalized() + TextFormatting.GRAY;
					}
					return "--";
				}).collect(Collectors.joining("/"));
				dispList.add(forgedMaterials);
				MaterialLayer current = instance.getCurrentLayer();
				if(GuiScreen.isShiftKeyDown()){
					if(current!=null){
						dispList.add(current.getFittingProgress()+"/"+current.getMaxFittingProgress());
					}
					if(instance.hasForged()){
						dispList.addAll(MinsagaUtil.getModifierStrings(instance,OptionalInt.empty()));
						dispList.add(instance.getAbilities().stream().map(in -> in.getName()).collect(Collectors.joining("/")));
					}
				}else{
					dispList.add("[Shift Key]Customized Info");
				}
				break;
			case MINSAGA_MODIFIERS:
				Optional<MinsagaMaterial> material = MinsagaForging.instance().getMaterialFromItemStack(is);
				if(material.isPresent()){
					dispList.add(HSLibs.translateKey("word.minsaga.material")+":"+material.get().getLocalized());
					List<String> strings = MinsagaUtil.getModifierStrings(material.get(),OptionalInt.of(material.get().getRepairDamage()));
					dispList.addAll(strings);
					if(material.get().hasAbilities()){
						//						String ab = material.get().getAbilities().stream().map(in -> in.getName()).collect(Collectors.joining("/"));
						dispList.add(UnsagaTextFormatting.PROPERTY+material.get().getAbilities().stream().map(in -> in.getName()).collect(Collectors.joining("/")));
					}
				}
				break;
			case DURABILITY:
				int du = is.getMaxDamage() - is.getItemDamage();
				dispList.add("Durability:"+du+"/"+is.getMaxDamage());
				break;
			default:
				break;

			}
		}
	}

}
