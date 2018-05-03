//package mods.hinasch.unsaga.util;
//
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.item.ItemStack;
//
//public class ArmorWeight {
//
//	public static int getPlayerArmorWeight(EntityPlayer ep){
//		int weight = 0;
//		for(ItemStack is:ep.getArmorInventoryList()){
//			if(is!=null){
//				if(MaterialAnalyzer.hasCapability(is) && MaterialAnalyzer.getCapability(is).getWeight().isPresent()){
//					weight += MaterialAnalyzer.getCapability(is).getWeight().get();
//				}else{
//					weight += 1;
//				}
//
//			}
//		}
//		return weight;
//	}
//
//	public static boolean isLightWeight(EntityPlayer ep){
//		return getPlayerArmorWeight(ep)<3;
//	}
//}
