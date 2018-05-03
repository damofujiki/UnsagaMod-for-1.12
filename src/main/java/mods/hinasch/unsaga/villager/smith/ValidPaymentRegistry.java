package mods.hinasch.unsaga.villager.smith;

import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Maps;

import mods.hinasch.lib.item.ItemUtil;
import net.minecraft.item.ItemStack;

public class ValidPaymentRegistry {

	public static enum Value{
		LOW,MID,HIGH,RICH;
	}
	private static ValidPaymentRegistry INSTANCE;

	public static ValidPaymentRegistry instance(){
		if(INSTANCE==null){
			INSTANCE = new ValidPaymentRegistry();
		}
		return INSTANCE;
	}

	public static Optional<Value> getValue(ItemStack is){
		return ValidPaymentRegistry.instance().map.entrySet().stream().filter(in -> ItemUtil.isOreDict(is, in.getKey()))
				.map(in -> in.getValue()).findFirst();
	}
	Map<String,Value> map = Maps.newHashMap();
	protected ValidPaymentRegistry(){

	}

	public void register(){


		this.map.put("nuggetGold", Value.LOW);
		this.map.put("ingotGold", Value.MID);
		this.map.put("ingotSilver", Value.LOW);
		this.map.put("gemEmerald", Value.HIGH);
		this.map.put("gemDiamond", Value.RICH);
		this.map.put("ingotElectrum", Value.MID);
		this.map.put("ingotPlatinum ", Value.HIGH);
		this.map.put("blockGold ", Value.HIGH);
		this.map.put("blockSilver ", Value.MID);
	}
}
