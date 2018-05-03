package mods.hinasch.unsaga.init;

import mods.hinasch.unsaga.ability.AbilityCapability;
import mods.hinasch.unsaga.chest.ChestCapability;
import mods.hinasch.unsaga.core.advancement.UnsagaUnlockableContentCapability;
import mods.hinasch.unsaga.core.entity.projectile.custom_arrow.CustomArrowCapability;
import mods.hinasch.unsaga.core.inventory.AccessorySlotCapability;
import mods.hinasch.unsaga.core.item.misc.skillpanel.SkillPanelCapability;
import mods.hinasch.unsaga.core.item.weapon.ItemGunUnsaga;
import mods.hinasch.unsaga.core.world.UnsagaWorldCapability;
import mods.hinasch.unsaga.core.world.chunk.UnsagaChunkCapability;
import mods.hinasch.unsaga.lp.LifePoint;
import mods.hinasch.unsaga.material.UnsagaMaterialCapability;
import mods.hinasch.unsaga.minsaga.MinsagaForgeCapability;
import mods.hinasch.unsaga.status.TargetHolderCapability;
import mods.hinasch.unsaga.status.UnsagaXPCapability;
import mods.hinasch.unsaga.util.LivingHelper;
import mods.hinasch.unsaga.villager.UnsagaVillagers;
import mods.hinasch.unsaga.villager.village.VillageDistributionCapability;

public class UnsagaCapabilities {

	public static void registerCapabilities(){
		LifePoint.BUILDER.registerCapability();
		UnsagaMaterialCapability.base.registerCapability();
		AbilityCapability.adapterBase.registerCapability();
		AccessorySlotCapability.adapterBase.registerCapability();
//		EquipmentCacheCapability.adapterBase.registerCapability();
//		EntityStateCapability.adapterBase.registerCapability();
		SkillPanelCapability.adapterBase.registerCapability();
		UnsagaXPCapability.base.registerCapability();
		TargetHolderCapability.adapterBase.registerCapability();
		ChestCapability.adapterBase.registerCapability();
		UnsagaVillagers.instance().registerCapabilities();
		ItemGunUnsaga.adapterBase.registerCapability();
		MinsagaForgeCapability.BUILDER.registerCapability();
		UnsagaWorldCapability.BUILDER.registerCapability();
		UnsagaUnlockableContentCapability.builder.registerCapability();
//		ExtendedPotionCapability.adapterBase.registerCapability();
		LivingHelper.BUILDER.registerCapability();
//		AdditionalDamageAttributeCapability.builder.registerCapability();
		UnsagaChunkCapability.BUILDER.registerCapability();
		CustomArrowCapability.BUILDER.registerCapability();
		VillageDistributionCapability.BUILDER.registerCapability();
	}

	/** Capabilityのattachまたは初期化イベント*/
	public  static void registerCapabilityAttachEvents(){
		LifePoint.registerEvents();
		UnsagaMaterialCapability.register();
		AbilityCapability.registerEvents();
//		EquipmentCacheCapability.registerEvents();
		AccessorySlotCapability.registerEvents();
//		EntityStateCapability.register();
		SkillPanelCapability.registerEvents();
		UnsagaXPCapability.registerEvents();
		TargetHolderCapability.registerEvents();
		ChestCapability.register();
		UnsagaVillagers.instance().registerCapabilityAttachEvents();
		ItemGunUnsaga.adapter.registerAttachEvent();
		MinsagaForgeCapability.ADAPTER.registerAttachEvent();
		UnsagaWorldCapability.registerEvents();
		UnsagaUnlockableContentCapability.adapter.registerAttachEvent();
//		ExtendedPotionCapability.adapter.registerAttachEvent();
//		AdditionalDamageAttributeCapability.adapter.registerAttachEvent();
		LivingHelper.registerEvents();
		UnsagaChunkCapability.registerEvents();
		CustomArrowCapability.registerEvents();
		VillageDistributionCapability.ADAPTER.registerAttachEvent();
	}
}
