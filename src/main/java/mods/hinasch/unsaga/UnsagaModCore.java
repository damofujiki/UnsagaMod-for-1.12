package mods.hinasch.unsaga;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Deprecated
public class UnsagaModCore {

	protected static UnsagaModCore instance;

	public static UnsagaModCore instance(){
		if(instance==null){
			instance = new UnsagaModCore();
		}
		return instance;
	}


//	public mods.hinasch.unsaga.ability.AbilityRegistry abilitiesNew;

//	public UnsagaBlockRegistry blocks;
//	public UnsagaEntityRegistry entities;
//	public mods.hinasch.unsaga.core.entity.UnsagaEntityRegistry entitiesNew;
//	public SimpleCreativeTab tabCombat;
//	public SimpleCreativeTab tabTools;
//	public SimpleCreativeTab tabMisc;
//	public SimpleCreativeTab tabSkillPanels;
//	public UnsagaMagic magic;
//	public UnsagaLibrary library = UnsagaLibrary.instance();
//	public BlockOrePropertyRegistry oreBlocks;
//	public MinsagaForging minsagaForging ;
//	public UnsagaEvents events = new UnsagaEvents();
//	public UnsagaAchievementRegistry achievements = UnsagaAchievementRegistry.instance();
//	public UnsagaModEvents eventsNew;

//	public LivingHelper.CacheType ACCESSORY;

//	public mods.hinasch.unsaga.material.UnsagaMaterials materialsNew;
//	public UnsagaItemRegistry itemsNew = UnsagaItemRegistry.instance();
//	public UnsagaPotions potions;
//	public StateRegistry states;
//	public RawMaterialItemRegistry rawMaterials;
//	public FuelHandlerCustom fuelHandler = new FuelHandlerCustom();


//	public SkillPanelRegistry newSkillPanels;
//	public MaterialItemAssociatedRegistry materialItemAssociation;
//	public UnsagaVillagers villager;
//	public SparklingPointRegistry sparklingPoint;

//	public UnsagaCapabilities capabilities = new UnsagaCapabilities();

//	public static Item itemElementChecker = new ItemElementChecker();
//	public static ItemSwordBase itemTest = new ItemSwordBase();
	private UnsagaModCore(){

	}
	public void init(FMLInitializationEvent e){


//		this.itemsNew.registerRecipes();
//
//		events.init();
//		if(e.getSide()==Side.CLIENT){
//			events.initClientEvents();
//		}
//		items.registerRecipes();
//		blocks.registerOthers();
//		debuffs.registerEventHooks();
//		abilities.registerEventHooks();
//		skillPanels.registerHooks();
//		UnsagaModIntegration.miscItems.init();
//		UnsagaModIntegration.oreBlocks.init();
//		materials.associateItemWithMaterial();
//		materials.initByproductAbility();
//
//
//
//		SimpleCreativeTab.setIconItemToTab(CreativeTabsUnsaga.tabUnsaga, items.getItem(ToolCategory.AXE, materials.damascus));
//		SimpleCreativeTab.setIconItemStackToTab(CreativeTabsUnsaga.tabPanels, skillPanels.unlock.getItemStack());
//
//


//
//		LivingHelper.registerEvents();
//		LivingHelper.registeredTypes.add(LivingHelper.CacheType.ACCESSORY);
//		LivingHelper.registerHook(new LivingHelper.Hook() {
//
//			@Override
//			public void onRefleshEquipments(ILivingHelper callback,EntityLivingBase living) {
//				if(living instanceof EntityPlayer && AccessorySlotCapability.adapter.hasCapability((EntityPlayer) living)){
//
//
//					for(ItemStack is:AccessorySlotCapability.adapter.getCapability((EntityPlayer)living).getEquippedAccessories()){
//						if(is!=null){
//							callback.getMap().get(LivingHelper.CacheType.ACCESSORY).add(new Equipment(is));
//						}else{
//							callback.getMap().get(LivingHelper.CacheType.ACCESSORY).add(LivingHelper.NONE);
//						}
//					}
//				}
//
//			}
//
//			@Override
//			public boolean onHasChangedEquipments(ILivingHelper callback,EntityLivingBase living) {
//				if(living instanceof EntityPlayer && AccessorySlotCapability.adapter.hasCapability((EntityPlayer) living)){
//
//					int index = 0;
//					for(ItemStack is:AccessorySlotCapability.adapter.getCapability((EntityPlayer) living).getEquippedAccessories()){
//						if(!callback.getMap().get(LivingHelper.CacheType.ACCESSORY).get(index).compare(is)){
//							return true;
//						}
//
//						index ++;
//					}
////					for(ItemStack is:AccessoryHelper.getCapability((EntityPlayer)living).getAccessories()){
////						if(is!=null){
////							if(!callback.getMap().get(ACCESSORY).contains(is)){
////								return true;
////							}
////						}
////					}
//				}
//				return false;
//			}
//		});
//
//		AccessoryHelper.adapter.registerEvent();
//		LifePoint.registerEvents();
//		UnsagaVillager.registerEvents();
//		ItemBowUnsaga.registerEvents();
//		UnsagaEntityAttributes.registerEvents();
//		MaterialAnalyzer.register();
//		BarteringPriceSupplier.register();
//		ItemSkillPanel.adapter.registerEvent();
//		ComponentSelectableIcon.adapter.registerEvent();
//		ItemSkillBook.adapter.registerEvent();
//		ItemGunUnsaga.adapter.registerEvent();
//		WatchingOutCounter.adapter.registerEvent();
//		ForgingCapability.adapter.registerEvent();
//		MinsagaForgingEvent.registerEvents();
//		XPHelper.adapter.registerEvent();
//		TaggedArrowHelper.registerEvents();
//
//		materials.initDisplayItemStack();
//		achievements.init();

	}

	public void preInit(){


//		this.sparklingPoint = this.sparklingPoint.instance();
//		ChestCapability.registerChunkEvent();
//		itemElementChecker.setCreativeTab(CreativeTabs.TOOLS);
//		itemElementChecker.setRegistryName(UnsagaMod.MODID, "elementChecker");
//		GameRegistry.register(itemElementChecker);
//		itemTest.setCreativeTab(CreativeTabs.COMBAT);
//		itemTest.setRegistryName(UnsagaMod.MODID, "testSword");
//		itemTest.initPropertyGetter();
//		GameRegistry.register(itemTest);

//		UnsagaModIntegration.oreBlocks.preInit();
//
//		materials.init();
//		abilities.init();
//		debuffs.init();
//		skillPanels.init();
//		miscItems.preInit();
//
//		HSLibs.registerCapability(IUnsagaPropertyItem.class, new StorageIUnsagaItem(), DefaultIUnsagaPropertyItem.class);
////		HSLibs.registerCapability(IAbility.class, new StorageIAbility(), DefaultIAbility.class);
////		HSLibs.registerCapability(IAccessorySlot.class, new StorageIAccessorySlot(), DefaultIAccessorySlot.class);
////		HSLibs.registerCapability(ILifePoint.class, new StorageILifePoint(), DefaultILifePoint.class);
//
//		HSLibs.registerCapability(IUnsagaDamageSource.class, new StorageDummy<IUnsagaDamageSource>(){}, DefaultIUnsagaDamageSource.class);
////		HSLibs.registerCapability(IInfoOnEntity.class, new StorageIInfoOnEntity(), DefaultIInfoOnEntity.class);
////		HSLibs.registerCapability(IUnsagaVillager.class, new StorageIUnsagaVillager(), DefaultIUnsagaVillager.class);
//
//		AccessoryHelper.base.registerCapability();
//		LifePoint.base.registerCapability();
//		UnsagaVillager.base.registerCapability();
//		LivingHelper.adapterBase.registerCapability();
//		BarteringPriceSupplier.adapterBase.registerCapability();
//		ChestBehavior.adapterBase.registerCapability();;
//		ItemSkillPanel.adapterBase.registerCapability();
//		ComponentSelectableIcon.adapterBase.registerCapability();
//		ItemSkillBook.adapterBase.registerCapability();
//		ItemGunUnsaga.adapterBase.registerCapability();
//		WatchingOutCounter.adapterBase.registerCapability();
//		ForgingCapability.base.registerCapability();
//		XPHelper.base.registerCapability();
//		TaggedArrowHelper.base.registerCapability();
//		if(HSLib.configHandler.isDebug()){
//			testItem = new ItemTest(materials.angelite).setCreativeTab(CreativeTabs.COMBAT);
//		}
//		items.register();
//		blocks.register();
//
////		GameRegistry.register(testItem).setRegistryName("test");
//
//		this.entitiesNew = mods.hinasch.unsaga.core.entity.UnsagaEntityRegistry.instance();

//		GameRegistry.registerFuelHandler(fuelHandler);

//		this.materialItemAssociation = MaterialItemAssociatedRegistry.instance();
	}

	public void postInit(){

	}
//	private void registerCapabilities(){
//		LifePoint.base.registerCapability();
//		UnsagaMaterialCapability.base.registerCapability();
//		AbilityCapability.adapterBase.registerCapability();
//		AccessorySlotCapability.adapterBase.registerCapability();
////		EquipmentCacheCapability.adapterBase.registerCapability();
//		EntityStateCapability.adapterBase.registerCapability();
//		SkillPanelCapability.adapterBase.registerCapability();
//		UnsagaXPCapability.base.registerCapability();
//		TargetHolderCapability.adapterBase.registerCapability();
//		ChestCapability.adapterBase.registerCapability();
//		this.villager.registerCapabilities();
//		ItemGunUnsaga.adapterBase.registerCapability();
//		ForgingCapability.base.registerCapability();
//	}
//
//	/** Capabilityのattachまたは初期化イベント*/
//	private void registerCapabilityAttachEvents(){
//		LifePoint.registerEvents();
//		UnsagaMaterialCapability.register();
//		AbilityCapability.registerEvents();
////		EquipmentCacheCapability.registerEvents();
//		AccessorySlotCapability.registerEvents();
//		EntityStateCapability.register();
//		SkillPanelCapability.registerEvents();
//		UnsagaXPCapability.registerEvents();
//		TargetHolderCapability.registerEvents();
//		ChestCapability.register();
//		this.villager.registerCapabilityAttachEvents();
//		ItemGunUnsaga.adapter.registerAttachEvent();
//		ForgingCapability.adapter.registerAttachEvent();
//	}
}
