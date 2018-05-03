package mods.hinasch.unsaga.init;

import mods.hinasch.lib.item.ItemCustomEntityEgg;
import mods.hinasch.lib.item.SimpleCreativeTab;
import mods.hinasch.lib.registry.BlockItemRegistry;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.ability.AbilityAssociateRegistry;
import mods.hinasch.unsaga.ability.AbilityCapability;
import mods.hinasch.unsaga.ability.IAbility;
import mods.hinasch.unsaga.core.item.UnsagaCreativeTabs;
import mods.hinasch.unsaga.core.item.misc.ItemElementChecker;
import mods.hinasch.unsaga.core.item.misc.ItemRawMaterialNew;
import mods.hinasch.unsaga.core.item.misc.ItemWazaBook;
import mods.hinasch.unsaga.core.item.misc.skillpanel.ItemSkillPanel;
import mods.hinasch.unsaga.core.item.weapon.ItemAxeUnsaga;
import mods.hinasch.unsaga.core.item.weapon.ItemBowUnsaga;
import mods.hinasch.unsaga.core.item.weapon.ItemGloveUnsaga;
import mods.hinasch.unsaga.core.item.weapon.ItemGunUnsaga;
import mods.hinasch.unsaga.core.item.weapon.ItemKnifeUnsaga;
import mods.hinasch.unsaga.core.item.weapon.ItemShieldUnsaga;
import mods.hinasch.unsaga.core.item.weapon.ItemSpearUnsaga;
import mods.hinasch.unsaga.core.item.weapon.ItemStaffUnsaga;
import mods.hinasch.unsaga.core.item.weapon.ItemSwordUnsaga;
import mods.hinasch.unsaga.core.item.wearable.ItemAccessoryUnsaga;
import mods.hinasch.unsaga.core.item.wearable.ItemArmorUnsaga;
import mods.hinasch.unsaga.material.RawMaterialRegistry;
import mods.hinasch.unsaga.material.RawMaterialRegistry.RawMaterial;
import mods.hinasch.unsaga.material.UnsagaMaterial;
import mods.hinasch.unsaga.material.UnsagaMaterialCapability;
import mods.hinasch.unsaga.material.UnsagaMaterials;
import mods.hinasch.unsaga.util.ToolCategory;
import mods.hinasch.unsaga.villager.UnsagaVillagerProfession;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;

public class UnsagaItemRegistry extends BlockItemRegistry<Item>{

	private static UnsagaItemRegistry INSTANCE;

	public static UnsagaItemRegistry instance(){
		if(INSTANCE == null){
			INSTANCE = new UnsagaItemRegistry();
		}
		return INSTANCE;
	}
//	public static final Item SWORD = (Item) instance().put(new ItemSwordUnsaga(), "sword2", UnsagaCreativeTabs.TOOLS);
//	public Item sword;
//	public Item staff;
//	public Item axe;
//	public Item knife;
//	public Item spear;
//	public Item bow;;
//	public Item armor;
//	public Item helmet;
//	public Item boots;
//	public Item leggins;
//	public Item shield;
//	public Item accessory;
//	public Item elementChecker;
//	public Item skillPanel;
//	public Item ammo;
//	public Item musket;
//	public Item wazaBook;
//	public Item rawMaterials;
//	public ItemCustomEntityEgg entityEggs;

//	public Item iconCondition;

	public RegistryNamespaced<RawMaterial,Item> rawMaterials = new RegistryNamespaced();

	protected UnsagaItemRegistry() {
		super(UnsagaMod.MODID);
		this.setUnlocalizedNamePrefix("unsaga");
	}
	@Override
	public void register() {
		SimpleCreativeTab tabTools = UnsagaCreativeTabs.TOOLS;
		SimpleCreativeTab tabMisc = UnsagaCreativeTabs.MISC;
		SimpleCreativeTab tabSkillPanels = UnsagaCreativeTabs.PANEL_GROWTH;

		this.put(new ItemSwordUnsaga(), "sword",tabTools);
		this.put(new ItemAxeUnsaga(), "axe", tabTools);
		this.put(new ItemKnifeUnsaga(), "knife", tabTools);
		this.put(new ItemStaffUnsaga(), "staff", tabTools);
		this.put(new ItemBowUnsaga(), "bow", tabTools);
		this.put(new ItemSpearUnsaga(), "spear", tabTools);
		this.put(new ItemGloveUnsaga(), "gloves", tabTools);
		this.put(new ItemArmorUnsaga(ToolCategory.HELMET), "helmet", tabTools);
		this.put(new ItemArmorUnsaga(ToolCategory.BOOTS), "boots", tabTools);
		this.put(new ItemArmorUnsaga(ToolCategory.LEGGINS), "leggins", tabTools);
		this.put(new ItemArmorUnsaga(ToolCategory.ARMOR), "armor", tabTools);
		this.put(new ItemShieldUnsaga(), "shield", tabTools);
		this.put(new ItemAccessoryUnsaga(), "accessory", tabTools);
		this.put(new ItemElementChecker(), "element_checker", tabMisc);
		this.put(new ItemSkillPanel(), "skill_panel", tabSkillPanels);


//		this.iconCondition = (Item) this.put(new ItemIcon(), "icon.condition", null);
//		this.put(new ItemRawMaterial("unsaga.raw_material"), "raw_material", tabMisc);

		RawMaterialRegistry.instance().getProperties().stream().sorted().forEach(in->{
			Item item = this.put(new ItemRawMaterialNew(in), in.getKey().getResourcePath(), tabMisc);
			rawMaterials.putObject(in, item);
		});

		this.put(new ItemCustomEntityEgg(),"entity_egg",tabMisc);




		this.put(new ItemGunUnsaga(), "musket", tabMisc);
		this.put(new Item(), "ammo", tabMisc);
		this.put(new ItemWazaBook(), "waza_book", tabMisc);

		this.put(new Item(), "fire_ball", null);
		this.put(new Item(), "bubble_ball", null);
		this.put(new Item(), "boulder", null);
		this.put(new Item(), "thunder_crap", null);
		this.put(new Item(), "ice_needle", null);
//		UnsagaItems.test();
	}

	public void init(){
		this.registerEntityEggs();
	}
	private void registerEntityEggs(){
//		ItemCustomEntityEgg entityEggs = (ItemCustomEntityEgg) UnsagaItems.ENTITY_EGGS;
		UnsagaItems.ENTITY_EGGS.addMaping(0, EntityVillager.class, new ResourceLocation("unsaga.villager.merchant"), 0xff0000, 0x000000,(w,e,p)->{
			if(e instanceof EntityVillager){
				((EntityVillager)e).setProfession(UnsagaVillagerProfession.MERCHANT);
			}
			return e;
		});
		UnsagaItems.ENTITY_EGGS.addMaping(1, EntityVillager.class, new ResourceLocation("unsaga.villager.magic_merchant"), 0xff0000, 0x000000,(w,e,p)->{
			if(e instanceof EntityVillager){
				((EntityVillager)e).setProfession(UnsagaVillagerProfession.MAGIC_MERCHANT);
			}
			return e;
		});
		UnsagaItems.ENTITY_EGGS.addMaping(2, EntityVillager.class, new ResourceLocation("unsaga.villager.blacksmith"), 0xff0000, 0x000000,(w,e,p)->{
			if(e instanceof EntityVillager){
				((EntityVillager)e).setProfession(UnsagaVillagerProfession.BLACKSMITH);
			}
			return e;
		});
	}
	public void registerRecipes(){
//		RecipeUtilNew.RecipeShaped.create().setBase("G I"," P ").addAssociation('G', Items.GUNPOWDER)
//		.addAssociation('I', Items.IRON_INGOT).addAssociation('P', Items.PAPER).setOutput(new ItemStack(ammo,4)).register();
	}

	public static ItemStack createStack(Item item,UnsagaMaterial mate,int meta){
		ItemStack newStack = new ItemStack(item,1,meta);
		UnsagaMaterial material = mate;
		if(item==UnsagaItems.MUSKET){
			material = UnsagaMaterials.IRON;
		}

		//最初から覚えているアビリティを付与

//		if(material==UnsagaMaterials.instance().dragonHeart){
//			if(AbilityCapability.adapter.hasCapability(newStack)){
//				AbilityCapability.adapter.getCapability(newStack).setAbility(3, AbilityRegistry.instance().superHealing);
//			}
//		}
		if(UnsagaMaterialCapability.adapter.hasCapability(newStack)){

			if(AbilityCapability.adapter.hasCapability(newStack)){
				if(AbilityAssociateRegistry.instance().getInherentAbility(mate).isPresent()){
					IAbility ab = AbilityAssociateRegistry.instance().getInherentAbility(mate).get();
					AbilityCapability.adapter.getCapability(newStack).setAbility(3, ab);
				}
			}


			UnsagaMaterialCapability.adapter.getCapability(newStack).setMaterial(material);
			UnsagaMaterialCapability.adapter.getCapability(newStack).setWeight(material.getWeight());
		}
		return newStack;
	}

}
