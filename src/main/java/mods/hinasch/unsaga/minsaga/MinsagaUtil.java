package mods.hinasch.unsaga.minsaga;

import java.util.List;
import java.util.OptionalInt;
import java.util.Random;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.unsaga.minsaga.MinsagaForgeCapability.IStatusModifier;
import mods.hinasch.unsaga.util.UnsagaTextFormatting;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;

public class MinsagaUtil {

	public static List<ItemStack> getForgedArmors(EntityLivingBase el){
		return Lists.newArrayList(el.getArmorInventoryList()).stream().filter(in -> !in.isEmpty()&&MinsagaForgeCapability.ADAPTER.hasCapability(in))
				.collect(Collectors.toList());
	}
	public static List<MinsagaForging.Ability> getAbilities(EntityLivingBase el){
		return getForgedArmors(el).stream().flatMap(in ->{
			List<MinsagaForging.Ability> list = MinsagaForgeCapability.ADAPTER.getCapability(in).getAbilities();
			if(list!=null && !list.isEmpty()){
				return list.stream();
			}
			return Lists.<MinsagaForging.Ability>newArrayList().stream();
		}).collect(Collectors.toList());
	}

	public static void damageToolProcess(EntityLivingBase entityIn,ItemStack tool,int modifier,Random rand){
		int prob = MathHelper.clamp(10 * Math.abs(modifier), 0, 50);
		if(rand.nextInt(100)<prob){
			if(modifier<0){
				tool.damageItem(1, entityIn);
			}else{
				if(tool.getItemDamage()>0){
					tool.damageItem(-1, entityIn);
				}
			}
		}

	}

	public static String getColored(int f){
		String str = "";
		if(f>0){
			str = UnsagaTextFormatting.POSITIVE.toString();
		}
		if(f<0){
			str = UnsagaTextFormatting.NEGATIVE.toString();
		}
		str += String.format("%d", f);
		return str += TextFormatting.GRAY;
	}

	public static String getColored(float f){
		String str = "";
		if(f>0){
			str = UnsagaTextFormatting.POSITIVE.toString();
		}
		if(f<0){
			str = UnsagaTextFormatting.NEGATIVE.toString();
		}
		str += String.format("%.1f", f);
		return str+= TextFormatting.GRAY;
	}

	public static List<String> getModifierStrings(IStatusModifier status,OptionalInt repairDamage){
		StringBuilder text = new StringBuilder("");
		text.append(HSLibs.translateKey("word.minsaga.modifier.attack")).append(":");
		text.append(getColored(status.getAttackModifier())).append("/");
		text.append(HSLibs.translateKey("word.minsaga.modifier.efficiency")).append(":");
		text.append(getColored(status.getEfficiencyModifier()));
		StringBuilder text1 = new StringBuilder("");
		text1.append(HSLibs.translateKey("word.minsaga.modifier.defence.melee")).append(":");
		text1.append(getColored(status.getArmorModifier().melee())).append("/");
		text1.append(HSLibs.translateKey("word.minsaga.modifier.defence.magic")).append(":");
		text1.append(getColored(status.getArmorModifier().magic()));
		StringBuilder text2 = new StringBuilder("");
		text2.append(HSLibs.translateKey("word.minsaga.modifier.repair.cost")).append(":");
		text2.append(getColored(status.getCostModifier())).append("/");
		StringBuilder text3 = new StringBuilder("");
		text2.append(HSLibs.translateKey("word.minsaga.modifier.durability")).append(":");
		text2.append(getColored(status.getDurabilityModifier())).append("/");
		if(repairDamage.isPresent()){
			text2.append(HSLibs.translateKey("word.minsaga.modifier.repair.damage")).append(":");
			text2.append(getColored(repairDamage.getAsInt()));
		}

		return Lists.newArrayList(text.toString(),text1.toString(),text2.toString());
	}

}
