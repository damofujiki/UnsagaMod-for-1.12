package mods.hinasch.unsaga.material;

import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import mods.hinasch.unsaga.core.block.BlockUnsagaCube;
import mods.hinasch.unsaga.init.UnsagaOres;
import mods.hinasch.unsaga.villager.smith.SmithMaterialRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class MaterialItemAssociatings {


	Map<UnsagaMaterial,ItemStack> materialAssociatedItems = Maps.newHashMap();
	Map<ItemStack,Float> materialRepairAmountMap = Maps.newHashMap();
	UnsagaMaterialRegistry materials = UnsagaMaterialRegistry.instance();

	private static MaterialItemAssociatings INSTANCE;

	public static MaterialItemAssociatings instance(){
		if(INSTANCE == null){
			INSTANCE = new MaterialItemAssociatings();
		}
		return INSTANCE;
	}

	protected MaterialItemAssociatings(){

	}

	public void init(){
		this.associateVanillaThings();
		this.registerOthers();
		this.validate();
	}
	protected void associateVanillaThings(){

		addAssociation(UnsagaMaterials.FEATHER, new ItemStack(Items.FEATHER,1),0.5F);
		addAssociation(UnsagaMaterials.WOOD, new ItemStack(Items.STICK,1),0.1F);
		addAssociation(UnsagaMaterials.BONE1, new ItemStack(Items.BONE,1),0.5F);
		addAssociation(UnsagaMaterials.QUARTZ, new ItemStack(Items.QUARTZ,1),0.5F);
		addAssociation(UnsagaMaterials.OBSIDIAN, new ItemStack(Blocks.OBSIDIAN,1),0.5F);
		addAssociation(UnsagaMaterials.IRON, new ItemStack(Items.IRON_INGOT,1),0.5F);
		addAssociation(UnsagaMaterials.IRON_ORE, new ItemStack(Blocks.IRON_ORE,1),0.5F);
		addAssociation(UnsagaMaterials.DIAMOND, new ItemStack(Items.DIAMOND,1),0.5F);
		addAssociation(UnsagaMaterials.PRISMARINE, new ItemStack(Items.PRISMARINE_SHARD,1),0.5F);
		addAssociation(UnsagaMaterials.SHULKER, new ItemStack(Items.SHULKER_SHELL,1),0.5F);

		OreDictionary.registerOre("ancientFishScale", new ItemStack(Items.PRISMARINE_SHARD));
	}

	protected void registerOthers(){
		addAssociation(UnsagaMaterials.SERPENTINE, BlockUnsagaCube.Type.SERPENTINE.getStack(1),0.5F);
		addAssociation(UnsagaMaterials.COPPER_ORE, new ItemStack(UnsagaOres.COPPER.getBlock(),1),0.5F);
	}


	protected void addAssociation(UnsagaMaterial m,ItemStack is,float amount){
		Preconditions.checkNotNull(is);
		materialAssociatedItems.put(m, is);
		this.materialRepairAmountMap.put(is, amount);
	}
	protected void registerOreDictionary(){

	}

	public OptionalDouble getAmoutInDurability(ItemStack other){
		if(SmithMaterialRegistry.instance().find(other).isPresent()){
			return OptionalDouble.of(SmithMaterialRegistry.instance().find(other).get().getAmount());
		}
		return this.materialRepairAmountMap.entrySet().stream().filter(in -> in.getKey().isItemEqual(other)).mapToDouble(in -> in.getValue()).findFirst();
	}
	public void registerAssociation(UnsagaMaterial m,ItemStack is){
		materialAssociatedItems.put(m, is);
	}

	public Optional<UnsagaMaterial> getMaterialFromStack(ItemStack is){
		return this.materialAssociatedItems.entrySet().stream().filter(in -> in.getValue().isItemEqual(is)).map(in -> in.getKey()).findFirst();
	}
	public ItemStack getAssociatedStack(UnsagaMaterial m){
		if(this.materialAssociatedItems.containsKey(m)){
			return this.materialAssociatedItems.get(m);
		}
		return ItemStack.EMPTY;
	}
	protected void validate(){
		UnsagaMaterialRegistry.instance().merchandiseMaterial.stream().forEach(in ->{
//			UnsagaMod.logger.trace("register", in);
			Preconditions.checkNotNull(materialAssociatedItems.get(in),in);
			ItemStack is = materialAssociatedItems.get(in);
			Preconditions.checkArgument(getAmoutInDurability(is).isPresent());
		});
	}
}
