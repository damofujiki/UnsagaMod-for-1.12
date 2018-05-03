package mods.hinasch.unsaga.init;

import mods.hinasch.lib.block.BlockOreProperty;
import mods.hinasch.lib.registry.PropertyRegistryOre;
import mods.hinasch.unsaga.material.RawMaterialRegistry;
import net.minecraft.block.Block;

public class BlockOrePropertyRegistry extends PropertyRegistryOre<BlockOrePropertyRegistry.Property>{

	protected static BlockOrePropertyRegistry INSTANCE;

	public static BlockOrePropertyRegistry instance(){
		if(INSTANCE == null){
			INSTANCE = new BlockOrePropertyRegistry();
		}
		return INSTANCE;
	}

	public BlockOrePropertyRegistry(){

	}
	@Override
	protected void registerObjects() {
		this.put(UnsagaOres.LEAD);
		this.put(UnsagaOres.RUBY);
		this.put(UnsagaOres.SAPPHIRE);
		this.put(UnsagaOres.SILVER);
		this.put(UnsagaOres.COPPER);
		this.put(UnsagaOres.ANGELITE);
		this.put(UnsagaOres.DEMONITE);
	}

	public static class Property extends BlockOreProperty{

		String oreDictID;
		public Property(int id, String name,String oreDictID, float exp, int harvest) {
			super(id, name, exp, harvest);
			this.oreDictID = oreDictID;
			// TODO 自動生成されたコンストラクター・スタブ
		}

		@Override
		public Block getBlock() {
			// TODO 自動生成されたメソッド・スタブ
			return UnsagaBlockRegistry.instance().ores.getObject(this.getId());
		}

		@Override
		public String getOreDict() {
			// TODO 自動生成されたメソッド・スタブ
			return this.oreDictID;
		}

	}

	@Override
	public void initSmelted() {
		UnsagaOres.LEAD.setSmelt(RawMaterialRegistry.instance().lead);
		UnsagaOres.RUBY.setSmeltAndBreaked(RawMaterialRegistry.instance().ruby);
		UnsagaOres.SAPPHIRE.setSmeltAndBreaked(RawMaterialRegistry.instance().sapphire);
		UnsagaOres.SILVER.setSmelt(RawMaterialRegistry.instance().silver);
		UnsagaOres.COPPER.setSmelt(RawMaterialRegistry.instance().copper);
		UnsagaOres.ANGELITE.setSmeltAndBreaked(RawMaterialRegistry.instance().lightStone);
		UnsagaOres.DEMONITE.setSmeltAndBreaked(RawMaterialRegistry.instance().darkStone);
	}

	@Override
	public void initOreDictionary() {
		// TODO 自動生成されたメソッド・スタブ

	}
	@Override
	public void preInit() {
		this.registerObjects();

	}
	@Override
	public void init() {
		// TODO 自動生成されたメソッド・スタブ
		this.registerSmeltAndOreDictionary();

	}

	public static BlockOreProperty getOreData(int id) {
		// TODO 自動生成されたメソッド・スタブ
		return instance().getObjectById(id);
	}


}
