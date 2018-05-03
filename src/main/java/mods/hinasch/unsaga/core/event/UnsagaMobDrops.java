package mods.hinasch.unsaga.core.event;

import mods.hinasch.lib.core.HSLib;
import mods.hinasch.lib.item.CustomDropNew;
import mods.hinasch.lib.item.CustomDropNew.EntityDropper;
import mods.hinasch.unsaga.core.entity.mob.EntityRuffleTree;
import mods.hinasch.unsaga.core.entity.mob.EntityTreasureSlimeNew;
import mods.hinasch.unsaga.core.entity.passive.EntityUnsagaChest;
import mods.hinasch.unsaga.core.potion.UnsagaPotions;
import mods.hinasch.unsaga.material.RawMaterialRegistry;
import mods.hinasch.unsagamagic.spell.tablet.TabletRegistry;
import net.minecraft.entity.monster.AbstractIllager;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class UnsagaMobDrops {

	public void init(){
		CustomDropNew dropTabletNew = new CustomDropNew();
		dropTabletNew.setDropValueGetter(in ->{
			if(in.getKilled() instanceof EntityWitch){
				return 0.15F;
			}
			if(in.getKilled() instanceof EntityEnderman){
				return 0.10F;
			}
			if(in.getKilled() instanceof EntityTreasureSlimeNew){
				return 0.35F;
			}
			if(in.getKilled() instanceof AbstractIllager){
				return 0.10F;
			}
			return 0F;
		});
		dropTabletNew.setItemDrop(in ->TabletRegistry.drawRandomTablet(in.rand()).getStack(1));
		CustomDropNew dropChestNew = new CustomDropNew();
		dropChestNew.setDropValueGetter(in ->{
			if(in.getKilled().getMaxHealth()>=20.0F){
				return 0.20F;
			}
			if(in.getKilled().getMaxHealth()>=8.0F){
				return 0.10F;
			}
			if(in.getKilled().getMaxHealth()>=2.0F){
				return 0.05F;
			}
			return 0F;
		});
		dropChestNew.setDropConsumer(new EntityDropper(in -> new EntityUnsagaChest(in.getKilled().getEntityWorld())));
		CustomDropNew dropChitinNew = new CustomDropNew();
		dropChitinNew.setDropValueGetter(in ->{
			if(in.getKilled() instanceof EntitySpider){
				return 0.2F;
			}
			return 0F;
		});
		dropChitinNew.setItemDrop(in ->RawMaterialRegistry.instance().chitin.getItemStack(1));
		CustomDropNew dropGoldNugget = new CustomDropNew();
		dropGoldNugget.setPredicate(in -> {
			EntityPlayer ep = (EntityPlayer) in.getDamageSource().getTrueSource();
			return ep.isPotionActive(UnsagaPotions.instance().GOLD_FINGER);
		});
		dropGoldNugget.setDropValueGetter(in ->{
			EntityPlayer ep = (EntityPlayer) in.getDamageSource().getTrueSource();
			return 0.3F*(ep.getActivePotionEffect(UnsagaPotions.instance().GOLD_FINGER).getAmplifier()+1);
		});
		dropGoldNugget.setItemDrop(in -> new ItemStack(Items.GOLD_NUGGET,1));
		CustomDropNew dropMush = new CustomDropNew();
		dropMush.setDropValueGetter(in ->{
			if(in.getKilled() instanceof EntityRuffleTree){
				return 1.0F;
			}
			return 0F;
		});
		dropMush.setItemDrop(in -> new ItemStack(Blocks.BROWN_MUSHROOM,1));

		HSLib.addMobDrop(dropGoldNugget);
		HSLib.addMobDrop(dropChitinNew);
		HSLib.addMobDrop(dropChestNew);
		HSLib.addMobDrop(dropTabletNew);
		HSLib.addMobDrop(dropMush);


//		HSLib.addMobDrop(dropTablet.setLogger(UnsagaMod.logger,"TabletDrop"));
//		HSLib.addMobDrop(dropChest.setLogger(UnsagaMod.logger,"Chest"));
//		HSLib.addMobDrop(dropChitin.setLogger(UnsagaMod.logger,"Chitin"));
//		HSLib.addMobDrop(dropMush.setLogger(UnsagaMod.logger,"Mush"));
//		HSLib.addMobDrop(dropNugget.setLogger(UnsagaMod.logger,"nugget"));
	}
}
