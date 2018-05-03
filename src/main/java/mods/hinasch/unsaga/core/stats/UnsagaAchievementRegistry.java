//package mods.hinasch.unsaga.core.stats;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import mods.hinasch.unsaga.UnsagaMod;
//import mods.hinasch.unsaga.UnsagaModCore;
//import mods.hinasch.unsaga.material.RawMaterialItemRegistry;
//import mods.hinasch.unsagamagic.UnsagaMagic;
//import mods.hinasch.unsagamagic.item.UnsagaMagicItems;
//import net.minecraft.init.Blocks;
//import net.minecraft.init.Items;
//import net.minecraft.item.ItemStack;
//import net.minecraft.stats.Achievement;
//import net.minecraftforge.common.AchievementPage;
//
//public class UnsagaAchievementRegistry {
//
//	public AchievementPage page;
//	public List<Achievement> achieves;
//	public Achievement openInv;
//	public Achievement learnSkillFirst,gainAbilityFirst,masterSkill;
//	public Achievement firstChest,breakChest,unlockMagicLock;
//	public Achievement openVillager;
//	public Achievement restoreLP,restoreOtherLP;
//
//	public Achievement fullArmor;
//	public Achievement firstSmith,steel,steel2,damascus;
//	public Achievement getTablet,firstDecipher,startBlend,finishBlend;
//
//	public Achievement bartering1,bartering2,bartering3;
//
//	private static UnsagaAchievementRegistry INSTANCE;
//
//	public static UnsagaAchievementRegistry instance(){
//		if(INSTANCE == null){
//			INSTANCE = new UnsagaAchievementRegistry();
//		}
//		return INSTANCE;
//	}
//	private UnsagaAchievementRegistry(){
//		achieves = new ArrayList();
//
//	}
//
//	ItemStack iconSword;
//	ItemStack iconArmor;
//	public void init(){
////		this.iconSword = UnsagaMod.items.getItemStack(ToolCategory.SWORD, UnsagaMod.materials.stone, 1, 0);
////		this.iconArmor = UnsagaMod.items.getItemStack(ToolCategory.ARMOR, UnsagaMod.materials.stone, 1, 0);
////		//first
////
//		this.openInv = (Achievement) new UnsagaAchievement(0,"openInv",2,0,new ItemStack(Items.STICK),null).registerStat().initIndependentStat();
////
////		//2nd
////		//etc
////		this.openVillager = (Achievement) new UnsagaAchievement(1,"openVillager",4,0,
////				new ItemStack(Items.WHEAT),this.openInv).registerStat();
//		//Sparking
//		this.learnSkillFirst  = (Achievement) new UnsagaAchievement(10,"firstSkill",0,2,iconSword, this.openInv).registerStat();
//		this.gainAbilityFirst  = (Achievement) new UnsagaAchievement(11,"firstAbility",0,4,iconArmor, this.learnSkillFirst).registerStat();
//		this.masterSkill  = (Achievement) new UnsagaAchievement(12,"masterSkill",0,6,iconSword, this.gainAbilityFirst).registerStat();
////		//chest
//		ItemStack key = new ItemStack(Blocks.CHEST);
//		this.firstChest  = (Achievement) new UnsagaAchievement(13,"firstChest",2,2,key,this.openInv).registerStat();
//		this.breakChest  = (Achievement) new UnsagaAchievement(14,"breakChest",2,4,key, this.firstChest).registerStat();
//		this.unlockMagicLock  = (Achievement) new UnsagaAchievement(15,"unlockMagicLock",2,6,key, this.breakChest).registerStat();
////
////
//		//smith
//		this.firstSmith = (Achievement) new UnsagaAchievement(20,"firstSmith",4,2,
//				new ItemStack(Blocks.ANVIL,1),this.openInv).registerStat();
//		this.steel = (Achievement) new UnsagaAchievement(21,"steel",4,4,
//				RawMaterialItemRegistry.instance().steel1.getItemStack(1),this.firstSmith).registerStat();
//		this.steel2 = (Achievement) new UnsagaAchievement(22,"steel2",4,6,
//				RawMaterialItemRegistry.instance().steel2.getItemStack(1),this.steel).registerStat();
//		this.damascus = (Achievement) new UnsagaAchievement(23,"damascus",4,8,
//				RawMaterialItemRegistry.instance().damascus.getItemStack(1),this.steel2).registerStat();
//		//spell
//		ItemStack tablet = new ItemStack(UnsagaMagicItems.instance().tablet);
//		this.getTablet = (Achievement) new UnsagaAchievement(30,"getTablet",0,-2,
//				tablet,this.openInv).registerStat();
//		this.firstDecipher = (Achievement) new UnsagaAchievement(31,"firstDecipher",0,-4,
//				tablet,this.getTablet).registerStat();
//		this.startBlend = (Achievement) new UnsagaAchievement(32,"startBlend",0,-6,
//				tablet,this.firstDecipher).registerStat();
//		this.finishBlend = (Achievement) new UnsagaAchievement(33,"finishBlend",0,-8,
//				new ItemStack(UnsagaMagic.instance().items.tablet),this.startBlend).registerStat();
//
//		//LP
//		this.restoreLP = (Achievement) new UnsagaAchievement(40,"restoreLP",4,-2,
//				new ItemStack(Items.BED),this.openInv).registerStat();
//		this.restoreOtherLP = (Achievement) new UnsagaAchievement(41,"restoreOtherLP",4,-4,
//				new ItemStack(Items.BED),this.restoreLP).registerStat();
//
//		//Bartering
//		this.bartering1 = (Achievement) new UnsagaAchievement(50,"bartering1",6,0,
//				new ItemStack(Items.WHEAT),this.openInv).registerStat();
//		this.bartering2 = (Achievement) new UnsagaAchievement(51,"bartering2",6,-2,
//				new ItemStack(Items.WHEAT),this.bartering1).registerStat();
//		this.bartering3 = (Achievement) new UnsagaAchievement(52,"bartering3",6,-4,
//				new ItemStack(Items.WHEAT),this.bartering2).registerStat();
//
//		page = new AchievementPage("Unsaga Mod",this.getAchievesArray(achieves));
//		AchievementPage.registerAchievementPage(page);
//	}
//
//	protected Achievement[] getAchievesArray(List<Achievement> list){
//		return list.toArray(new Achievement[list.size()]);
//	}
//
//
//	public static class UnsagaAchievement extends Achievement{
//
//		public UnsagaAchievement(int id,String name,
//				int x, int y, ItemStack is,
//				Achievement parent) {
//			super(UnsagaMod.MODID+"."+String.valueOf(id), "unsaga."+name, x, y, is,
//					parent);
//
//			UnsagaModCore.instance().achievements.achieves.add(this);
//		}
//
//	}
//}
