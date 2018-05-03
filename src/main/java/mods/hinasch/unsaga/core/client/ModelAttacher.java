package mods.hinasch.unsaga.core.client;

import com.google.common.collect.Lists;

import mods.hinasch.lib.client.ClientHelper;
import mods.hinasch.lib.client.ClientHelper.ModelHelper;
import mods.hinasch.lib.client.ClientHelper.PluralVariantsModelFactory;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.core.block.BlockUnsagaCube;
import mods.hinasch.unsaga.core.client.particle.ParticleItems;
import mods.hinasch.unsaga.init.BlockOrePropertyRegistry;
import mods.hinasch.unsaga.init.UnsagaBlockRegistry;
import mods.hinasch.unsaga.init.UnsagaItemRegistry;
import mods.hinasch.unsaga.init.UnsagaItems;
import net.minecraft.item.Item;

public class ModelAttacher {

	private final ModelHelper modelAgent = new ModelHelper(UnsagaMod.MODID);
	private final PluralVariantsModelFactory pluralVariantsModelFactory = PluralVariantsModelFactory.create(modelAgent, null);

	private UnsagaItemRegistry items = UnsagaItemRegistry.instance();
	private UnsagaBlockRegistry blocks = UnsagaBlockRegistry.instance();

	public void register(){
		//		List<String> list = Lists.newArrayList();
		//		list.add("sword");
		//		list.add("sword.failed");
		//		list.add("axe");
		//		pluralVariantsModelFactory.create(itemsNew.sword);
		//		.prepareVariants(list)
		//		.attach();

		this.registerModelAndColor(UnsagaItems.SWORD, "sword");
		this.registerModelAndColor(UnsagaItems.AXE, "axe");
		this.registerModelAndColor(UnsagaItems.KNIFE, "knife");
		this.registerModelAndColor(UnsagaItems.STAFF, "staff");
		this.registerModelAndColor(UnsagaItems.BOW, "bow");
		this.registerModelAndColor(UnsagaItems.SPEAR, "spear");
		this.registerModelAndColor(UnsagaItems.GLOVES, "gloves");
		this.registerModelAndColor(UnsagaItems.HELMET, "helmet");
		this.registerModelAndColor(UnsagaItems.ARMOR, "armor");
		this.registerModelAndColor(UnsagaItems.BOOTS, "boots");
		this.registerModelAndColor(UnsagaItems.LEGGINS, "leggins");
		this.registerModelAndColor(UnsagaItems.SHIELD, "shield");
		this.registerModelAndColor(UnsagaItems.ACCESSORY, "accessory");
		this.registerModelAndColor(UnsagaItems.ENTITY_EGGS, "entity_egg",3);
		this.registerModelAndColor(UnsagaItems.GROWTH_PANEL, "skill_panel");
		this.registerModelAndColor(UnsagaItems.AMMO, "ammo");
		this.registerModelAndColor(UnsagaItems.MUSKET, "musket");
		this.registerModelAndColor(UnsagaItems.WAZA_BOOK, "waza_book");
		modelAgent.registerModelMesher(UnsagaItems.ELEMENT_CHECKER, 0,"element_checker");
		//		modelAgent.registerModelMesher(this.itemsNew.iconCondition, 0,"icon.condition");
		//		modelAgent.registerModelMesher(this.itemsNew.iconCondition, 1,"icon.condition.hot");
		//		modelAgent.registerModelMesher(this.itemsNew.iconCondition, 2,"icon.condition.cold");
		//		modelAgent.registerModelMesher(this.itemsNew.iconCondition, 3,"icon.condition.humid");
		//		pluralVariantsModelFactory.create(this.itemsNew.iconCondition)
		//		.prepareVariants(ItemIconCondition.Type.getIconNames())
		//		.attach();

		//		pluralVariantsModelFactory.target(UnsagaItems.RAW_MATERIALS)
		//		.prepareVariants(RawMaterialItemRegistry.instance().getIconNames())
		//		.attachBy(RawMaterialItemRegistry.instance().getProperties(), in -> modelAgent.getNewModelResource(in.getIconName(),"inventory"));;
		//		ClientHelper.registerColorItem(UnsagaItems.RAW_MATERIALS);

		UnsagaItemRegistry.instance().rawMaterials.getKeys().forEach(in ->{
			modelAgent.registerModelMesher(in.getItem(), 0,modelAgent.getNewModelResource(in.getKey().getResourcePath(), "inventory"));
			ClientHelper.registerColorItem(in.getItem());
		}
				);

		//		pluralVariantsModelFactory.target(Item.getItemFromBlock(blocks.stonesAndMetals))
		//		.prepareVariants(BlockUnsagaStone.EnumType.getJsonNames())
		//		.attach();


		Lists.newArrayList(BlockUnsagaCube.Type.values()).forEach(in ->
		modelAgent.registerModelMesher(Item.getItemFromBlock(in.getBlock()), 0,modelAgent.getNewModelResource(in.getUnlocalizedName(), "inventory")));

		BlockOrePropertyRegistry.instance().getProperties()
		.forEach(ore -> modelAgent.registerModelMesher(ore.getBlockAsItem(), 0,modelAgent.getNewModelResource(ore.getPropertyName(), "inventory")));

		this.registerModelAndColor(ParticleItems.BOULDER, "boulder");
		this.registerModelAndColor(ParticleItems.BUBBLE_BALL, "bubble_ball");
		this.registerModelAndColor(ParticleItems.FIRE_BALL, "fire_ball");
		this.registerModelAndColor(ParticleItems.ICE_NEEDLE, "ice_needle");
		this.registerModelAndColor(ParticleItems.THUNDER_CRAP, "thunder_crap");

		//		modelAgent.registerModelMesher(this.itemsNew.iconCondition, 1,"icon.condition");
	}

	private void registerModelAndColor(Item item,String name,int... length){
		int len = 1;
		if(length.length>0){
			len = length[0];
		}
		for(int i=0;i<len;i++){
			modelAgent.registerModelMesher(item, i,name);
			ClientHelper.registerColorItem(item);
		}

	}

}
