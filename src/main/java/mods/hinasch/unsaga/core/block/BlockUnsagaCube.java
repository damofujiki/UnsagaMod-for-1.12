package mods.hinasch.unsaga.core.block;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;

import mods.hinasch.lib.iface.IIntSerializable;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.unsaga.init.UnsagaBlockRegistry;
import mods.hinasch.unsaga.material.RawMaterialRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

public class BlockUnsagaCube extends Block{

	public static enum Type implements IIntSerializable,IStringSerializable{
			SERPENTINE(0,MapColor.GREEN,"serpentine"),SERPENTINE_SMOOTH(1,MapColor.GREEN,"serpentine_smooth","polished_serpentine")
			,LEAD(2,MapColor.IRON,"lead","cube_lead"),METEORIC_IRON(3,MapColor.IRON,"meteoric_iron","cube_meteoric_iron")
			,DAMASCUS(4,MapColor.IRON,"damascus","cube_damascus"),SILVER(5,MapColor.IRON,"silver","cube_silver")
			,FAERIE_SILVER(6,MapColor.IRON,"faerie_silver","cube_faerie_silver"),COPPER(7,MapColor.IRON,"copper","cube_copper")
			,STEEL(8,MapColor.IRON,"steel","cube_steel"),STEEL2(9,MapColor.IRON,"steel2","cube_steel2")
			,METEORITE(10,MapColor.STONE,"meteorite","cube_meteorite");

	//		UnsagaMaterialRegistry m = UnsagaRegistries.MATERIALS;
			int meta;
			String name;
			MapColor color;
			String unlocalized;
			public MapColor getMapColor() {
				return color;
			}
			private Type (int meta,MapColor color,String name,String unlName){
				this.meta = meta;
				this.name = name;
				this.color = color;
				this.unlocalized =unlName;
			}

			private Type (int meta,MapColor color,String name){
				this(meta, color, name, name);
			}
			@Override
			public String getName() {
				// TODO 自動生成されたメソッド・スタブ
				return this.name;
			}

			public static List<String> getJsonNames(){
				String[] strs = new String[Type.values().length];
				Arrays.stream(Type.values()).forEach(in ->{
					strs[in.getMeta()] = in.getUnlocalizedName();
				});
				return Lists.newArrayList(strs);
			}
			public String getUnlocalizedName(){
				return this.unlocalized;
			}
			@Override
			public int getMeta() {
				// TODO 自動生成されたメソッド・スタブ
				return this.meta;
			}
			public ItemStack getBaseItem(int amount){
				ItemStack is = this.getBaseItem();
				is.setCount(amount);
				return is;
			}

			public Material getMaterial(){
				switch(this){
				case METEORITE:
				case SERPENTINE:
				case SERPENTINE_SMOOTH:
					return Material.ROCK;
				default:
					return Material.IRON;
				}
			}

			public Block getBlock(){
				return UnsagaBlockRegistry.instance().stones.getObject(this);
			}
			public ItemStack getBaseItem(){
				ItemStack rt = ItemStack.EMPTY;
				switch(this){
				case COPPER:
					rt = RawMaterialRegistry.instance().copper.getItemStack(1);
					break;
				case DAMASCUS:
					rt = RawMaterialRegistry.instance().damascus.getItemStack(1);
					break;
				case FAERIE_SILVER:
					rt = RawMaterialRegistry.instance().faerieSilver.getItemStack(1);
					break;
				case LEAD:
					rt = RawMaterialRegistry.instance().lead.getItemStack(1);
					break;
				case METEORIC_IRON:
					rt = RawMaterialRegistry.instance().meteoricIron.getItemStack(1);
					break;
				case SERPENTINE:
					break;
				case SERPENTINE_SMOOTH:
					break;
				case SILVER:
					rt = RawMaterialRegistry.instance().silk.getItemStack(1);
					break;
				case STEEL:
					rt = RawMaterialRegistry.instance().steel1.getItemStack(1);
					break;
				case STEEL2:
					rt = RawMaterialRegistry.instance().steel2.getItemStack(1);
					break;
				case METEORITE:
					rt = RawMaterialRegistry.instance().meteorite.getItemStack(1);
					break;
				default:
					break;

				}
				return rt;
			}
			public ItemStack getStack(int amount){
				return new ItemStack(this.getBlock(),amount);
			}
			public static Type fromMeta(int meta){
				return HSLibs.fromMeta(Type.values(), meta);
			}
		}


	final BlockUnsagaCube.Type type;

	public BlockUnsagaCube(BlockUnsagaCube.Type type) {
		super(Material.ROCK);
		this.type = type;
		this.setHardness(1.5F);

	}


	@Override
    public Material getMaterial(IBlockState state)
    {
        return type.getMaterial();
    }
}
