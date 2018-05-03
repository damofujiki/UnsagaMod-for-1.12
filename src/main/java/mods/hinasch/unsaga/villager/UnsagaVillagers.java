package mods.hinasch.unsaga.villager;

import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.unsaga.villager.bartering.BarteringMaterialCategory;
import mods.hinasch.unsaga.villager.bartering.DisplayPriceEvent;
import mods.hinasch.unsaga.villager.bartering.MerchandiseCapability;
import mods.hinasch.unsaga.villager.smith.BlackSmithRegistries;
import mods.hinasch.unsaga.villager.smith.EventDisplaySmithTooltip;

public class UnsagaVillagers {

	public static final UnsagaVillagerProfession PROFESSION = UnsagaVillagerProfession.instance();
//	public static final BlackSmithRegistries BLACKSMITH_REGISTRIES = new BlackSmithRegistries();
	public static final BarteringMaterialCategory BARTERING_SHOP_TYPES = BarteringMaterialCategory.instance();

	private static UnsagaVillagers INSTANCE;

	public static UnsagaVillagers instance(){
		if(INSTANCE == null){
			INSTANCE = new UnsagaVillagers();
		}
		return INSTANCE;
	}
	public void registerCapabilities(){
		UnsagaVillagerCapability.BUILDER.registerCapability();
//		InteractionInfoCapability.adapterBase.registerCapability();
		MerchandiseCapability.adapterBase.registerCapability();
	}

	public void registerCapabilityAttachEvents(){
		UnsagaVillagerCapability.registerEvents();
//		InteractionInfoCapability.registerEvents();
		MerchandiseCapability.registerAttachEvents();
	}
	public void preInit(){
//		this.profession = UnsagaVillagerProfession.instance();
//		BLACKSMITH.VALID_PAYMENTS = ValidPaymentRegistry.instance();
//		BLACKSMITH.MATERIAL_TRANSFORMS = MaterialTransformRegistry.instance();
//		BLACKSMITH.ABILITY_TRANSFORMS = ByproductAbilityRegistry.instance();
	}

	public void init(){
		BARTERING_SHOP_TYPES.init();
		BlackSmithRegistries.VALID_PAYMENTS.register();
		BlackSmithRegistries.MATERIAL_TRANSFORMS.register();
		BlackSmithRegistries.ABILITY_TRANSFORMS.register();

		DisplayPriceEvent.register();
		HSLibs.registerEvent(new EventDisplaySmithTooltip());
//		HSLibs.registerEvent(new EventVillagerBirth());
	}


}
