package mods.hinasch.unsaga.skillpanel;

import java.util.UUID;

import com.mojang.realmsclient.util.Pair;

import mods.hinasch.lib.item.ItemUtil;
import mods.hinasch.lib.util.Statics;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.core.inventory.container.HexMatrixAdapter;
import mods.hinasch.unsaga.core.item.misc.skillpanel.SkillPanelCapability;
import mods.hinasch.unsaga.status.UnsagaStatus;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.item.ItemStack;

public class SkillPanelBonus {


	public static final String NAME = UnsagaMod.MODID+".panelBonus";
	public static final UUID PANEL_UUID = UUID.fromString("7f4c3c3c-c447-4c55-b941-66ccee700ce3");

	public static enum Type{LUCK,ALL,MAGIC,LPSTR,HEALTH,STR,ELEMENTS;


		public Pair<IAttribute,AttributeModifier> getModifierPair(float f){
			int op = Statics.OPERATION_INCREMENT;
			switch(this){
			case ALL:
				break;
			case ELEMENTS:
				//TODO
				break;
			case HEALTH:
				return Pair.of(SharedMonsterAttributes.MAX_HEALTH, new AttributeModifier(PANEL_UUID,NAME,f,op));
			case LPSTR:
				return Pair.of(UnsagaStatus.DEXTALITY, new AttributeModifier(PANEL_UUID,NAME,f,op));
			case LUCK:
				return Pair.of(SharedMonsterAttributes.LUCK, new AttributeModifier(PANEL_UUID,NAME,f,op));
			case MAGIC:
				return Pair.of(UnsagaStatus.INTELLIGENCE, new AttributeModifier(PANEL_UUID,NAME,f,op));
			case STR:
				return Pair.of(SharedMonsterAttributes.ATTACK_DAMAGE, new AttributeModifier(PANEL_UUID,NAME,f,op));
			default:
				break;

			}
			return null;
		}
		public SkillPanel.IconType getIconType(){
			switch(this){
			case ELEMENTS:
				return SkillPanel.IconType.FAMILIAR;
			case ALL:
				return SkillPanel.IconType.NEGATIVE;
			case HEALTH:
				return SkillPanel.IconType.PROTECT;
			case LPSTR:
				return SkillPanel.IconType.KEY;
			case LUCK:
				return SkillPanel.IconType.COMMUNICATION;
			case MAGIC:
				return SkillPanel.IconType.ROLL;
			case STR:
				return SkillPanel.IconType.MELEE;
			default:
				break;

			}
			return null;
		}
	};

	public static void applyBonus(HexMatrixAdapter<ItemStack> matrix,EntityLivingBase living,boolean isLineBonus){

		for(Type type:Type.values()){
			float f = calcAppliedBonus(matrix,type,isLineBonus);
			if(type.getModifierPair(f)!=null){
				refleshModifier(living,type.getModifierPair(f).first(),type.getModifierPair(f).second());
			}
		}

		SkillPanelRegistry.instance().statusUpSkills.forEach(in ->{
			if(SkillPanelAPI.hasPanel(living, in)){
				int level = SkillPanelAPI.getHighestPanelLevel(living, in).getAsInt();
				AttributeModifier base = in.getModifierPair().second();
				AttributeModifier newmod = new AttributeModifier(base.getID(),base.getName(),base.getAmount()*level,base.getOperation());
				refleshModifier(living,in.getModifierPair().first(),newmod);
			}
		});

	}

	public static void refleshModifier(EntityLivingBase living,IAttribute attribute,AttributeModifier modifier){
		if(living.getEntityAttribute(attribute).getModifier(modifier.getID())!=null){
			living.getEntityAttribute(attribute).removeModifier(modifier.getID());
		}
		living.getEntityAttribute(attribute).applyModifier(modifier);
		UnsagaMod.logger.trace(attribute.getName(), living.getEntityAttribute(attribute).getAttributeValue());
	}
	public static float calcAppliedBonus(HexMatrixAdapter<ItemStack> matrix,Type type,boolean isLineBonus){
		float multiply = isLineBonus ? 2.0F : 1.0F;
		return (calcBonus(matrix,type.getIconType()) * multiply) + calcBonus(matrix,Type.ALL);
	}
	public static float calcBonus(HexMatrixAdapter<ItemStack> matrix,Type type){
		return calcBonus(matrix,type.getIconType());
	}

	public static float calcBonus(HexMatrixAdapter<ItemStack> matrix,SkillPanel.IconType type){
		double bonus = matrix.getJointed().stream().mapToDouble(xy ->{
			ItemStack is = matrix.getMatrix(xy);
			if(!ItemUtil.isItemStackNull(is)){
				if(SkillPanelCapability.adapter.hasCapability(is)){
					SkillPanel panel = SkillPanelCapability.adapter.getCapability(is).getPanel();
					if(panel.getIconType()==type){
						return 0.1F;
					}
				}

			}
			return 0.0F;
		}).sum();

		return (float) bonus;
	}
}
