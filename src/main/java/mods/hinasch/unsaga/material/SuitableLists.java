package mods.hinasch.unsaga.material;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonObject;

import jline.internal.Preconditions;
import mods.hinasch.lib.misc.JsonApplier;
import mods.hinasch.lib.misc.JsonApplier.IJsonApply;
import mods.hinasch.lib.misc.JsonApplier.IJsonParser;
import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.material.SuitableLists.ParserSuitable;
import mods.hinasch.unsaga.util.ToolCategory;
import net.minecraft.util.ResourceLocation;



public class SuitableLists implements IJsonApply<ParserSuitable>{

	protected static SuitableLists INSTANCE;

	private static ResourceLocation MATERIAL_SUITABLE = new ResourceLocation(UnsagaMod.MODID,"data/material_suitable.json");
//	Map<ToolCategory,SuitableList> suitableMap;
	Map<ToolCategory,Set<UnsagaMaterial>> suitables = Maps.newHashMap();

	public static class ParserSuitable implements IJsonParser{

		Set<UnsagaMaterial> m = Sets.newHashSet();
		Set<ToolCategory> categories = Sets.newHashSet();
		@Override
		public void parse(JsonObject jo) {
			String mn = jo.get("material").getAsString();
			this.m.add(UnsagaMaterialRegistry.instance().get(mn));
			if(UnsagaMaterialRegistry.instance().getMaterialsByCategoryID(mn)!=null){
				this.m = UnsagaMaterialRegistry.instance().getMaterialsByCategoryID(mn);
			}
			com.google.common.base.Preconditions.checkNotNull(this.m, mn);
			com.google.common.base.Preconditions.checkArgument(!this.m.isEmpty(),mn);
			String cate = jo.get("value").getAsString();
			for(String c:Splitter.on(',').split(cate)){
				ToolCategory category = ToolCategory.fromString(c);
				com.google.common.base.Preconditions.checkNotNull(category,cate);
				this.categories.add(category);
			}

		}

	}
//	public SuitableList swords;
//	public SuitableList knives;
//	public SuitableList axes;
//	public SuitableList staffs;
//	public SuitableList spears;
//	public SuitableList bows;
//	public SuitableList helmets;
//	public SuitableList armors;
//	public SuitableList boots;
//	public SuitableList leggins;

//	UnsagaMaterialRegistry materials;


	public static SuitableLists instance(){
		if(INSTANCE == null){
			INSTANCE = new SuitableLists();
		}
		return INSTANCE;
	}
	protected SuitableLists(){
//		this.suitableMap = Maps.newHashMap();
//		for(ToolCategory cate:ToolCategory.values()){
//			suitableMap.put(cate, new SuitableList());
//		}
//		swords = new SuitableList();
//		knives = new SuitableList();
//		axes = new SuitableList();
//		staffs = new SuitableList();
//		spears = new SuitableList();
//		bows = new SuitableList();
//		helmets = new SuitableList();
//		armors = new SuitableList();
//		boots = new SuitableList();
//		leggins = new SuitableList();
	}

	List<UnsagaMaterial> categoryWoods = Lists.newArrayList();
	List<UnsagaMaterial> categoryTusks = Lists.newArrayList();
	List<UnsagaMaterial> categoryBestials = Lists.newArrayList();
	List<UnsagaMaterial> categoryBones = Lists.newArrayList();
	List<UnsagaMaterial> categoryScales = Lists.newArrayList();
	List<UnsagaMaterial> categoryDebris = Lists.newArrayList();
	List<UnsagaMaterial> categoryRocks = Lists.newArrayList();
	List<UnsagaMaterial> categoryMetals = Lists.newArrayList();
	List<UnsagaMaterial> categorySteels = Lists.newArrayList();
	List<UnsagaMaterial> categoryClothes = Lists.newArrayList();
	List<UnsagaMaterial> categoryLeathers = Lists.newArrayList();
	List<UnsagaMaterial> categoryCorundums = Lists.newArrayList();

	public void registerSuitableMaterials(){
		JsonApplier.create(MATERIAL_SUITABLE, in -> new ParserSuitable(), in -> this).parseAndApply();
		this.suitables.put(ToolCategory.GLOVES, Sets.newHashSet(UnsagaMaterials.COTTON));
//		this.materials = UnsagaMaterialRegistry.instance();
//		this.categoryWoods = UnsagaMaterialRegistry.instance().categorised.get("woods");
//		this.categoryTusks = UnsagaMaterialRegistry.instance().categorised.get("tusks");
//		this.categoryBestials = UnsagaMaterialRegistry.instance().categorised.get("bestials");
//		this.categoryBones = UnsagaMaterialRegistry.instance().categorised.get("bones");
//		this.categoryScales = UnsagaMaterialRegistry.instance().categorised.get("scales");
//		this.categoryDebris = UnsagaMaterialRegistry.instance().categorised.get("allDebris");
//		this.categoryMetals = UnsagaMaterialRegistry.instance().categorised.get("metals");
//		this.categorySteels = UnsagaMaterialRegistry.instance().categorised.get("steels");
//		this.categoryClothes = UnsagaMaterialRegistry.instance().categorised.get("clothes");
//		this.categoryLeathers = UnsagaMaterialRegistry.instance().categorised.get("leathers");
//		this.categoryCorundums = UnsagaMaterialRegistry.instance().categorised.get("corundums");
//		this.categoryRocks = UnsagaMaterialRegistry.instance().categorised.get("rocks");
////		UnsagaMod.logger.trace("null", swords,materials);
//		SuitableList swords = new SuitableList();
//		swords.add(categoryWoods);
//		swords.add(categoryTusks);
//		swords.add(categoryBestials);
//		swords.add(categoryBones);
//		swords.add(categoryScales);
//		swords.add(categoryDebris);
//		swords.add(categoryRocks);
//		swords.add(categoryMetals);
//		swords.add(categorySteels);
//		swords.add(materials.sivaQueen);
//		swords.add(materials.dragonHeart);
//		swords.exclude(materials.diamond);
//		this.suitableMap.put(ToolCategory.SWORD, swords);
//
//		SuitableList knives = new SuitableList();
//		knives.add(categoryWoods);
//		knives.add(categoryTusks);
//		knives.add(categoryBestials);
//		knives.add(categoryBones);
//		knives.add(categoryScales);
//		knives.add(categoryDebris);
//		knives.add(categoryRocks);
//		knives.add(categoryMetals);
//		knives.add(categorySteels);
//		knives.add(materials.diamond);
//		knives.add(materials.categoryCorundums);
//		knives.add(materials.sivaQueen);
//		knives.add(materials.dragonHeart);
//		this.suitableMap.put(ToolCategory.KNIFE, knives);
//
//		SuitableList axes = new SuitableList();
//		axes.add(categoryTusks);
//		axes.add(categoryBestials);
//		axes.add(categoryBones);
//		axes.add(materials.chitin);
//		axes.add(categoryDebris);
//		axes.add(categoryRocks);
//		axes.exclude(materials.diamond);
//		axes.add(categoryMetals);
//		axes.add(categorySteels);
//		axes.add(materials.sivaQueen);
//		axes.add(materials.dragonHeart);
//		this.suitableMap.put(ToolCategory.AXE, axes);
//
//		SuitableList staffs = new SuitableList();
//		staffs.add(categoryWoods);
//		staffs.add(categoryTusks);
//		staffs.add(categoryBestials);
//		staffs.add(categoryBones);
//		staffs.add(categoryDebris);
//		staffs.add(categoryRocks);
//		staffs.add(categoryMetals);
//		staffs.add(categorySteels);
//		staffs.add(categoryCorundums);
//		staffs.add(materials.sivaQueen);
//		staffs.add(materials.dragonHeart);
//		this.suitableMap.put(ToolCategory.STAFF, staffs);
//
//		SuitableList spears = new SuitableList();
//		spears.add(categoryWoods);
//		spears.add(categoryTusks);
//		spears.add(categoryBestials);
//		spears.add(categoryBones);
//		spears.add(categoryScales);
//		spears.add(categoryDebris);
//		spears.add(categoryRocks);
//		spears.add(categoryMetals);
//		spears.add(categorySteels);
//		spears.add(categoryCorundums);
//		spears.add(materials.sivaQueen);
//		spears.add(materials.dragonHeart);
//		this.suitableMap.put(ToolCategory.SPEAR, spears);
//
//		SuitableList bows = new SuitableList();
//		bows.add(categoryWoods);
//		bows.add(categoryTusks);
//		bows.add(categoryBones);
//		bows.add(categoryMetals);
//		bows.add(categorySteels);
//		bows.exclude(materials.silver);
//		bows.add(materials.sivaQueen);
//		bows.add(materials.dragonHeart);
//		this.suitableMap.put(ToolCategory.BOW, bows);
//
//		SuitableList helmets = new SuitableList();
//		helmets.add(materials.feather);
//		helmets.add(categoryClothes);
//		helmets.add(categoryLeathers);
//		helmets.add(categoryBones);
//		helmets.add(categoryScales);
//		helmets.add(categoryDebris);
//		helmets.add(categoryBestials);
//		helmets.add(categoryRocks);
//		helmets.add(categoryCorundums);
//		helmets.add(categoryMetals);
//		helmets.add(categorySteels);
//		this.suitableMap.put(ToolCategory.HELMET, helmets);
//
//		SuitableList armors = new SuitableList();
//		armors.add(categoryClothes);
//		armors.add(categoryLeathers);
//		armors.add(categoryWoods);
//		armors.add(categoryBones);
//		armors.add(categoryScales);
//		armors.add(categoryDebris);
//		armors.add(categoryBestials);
//		armors.add(categoryRocks);
//		armors.exclude(materials.diamond);
//		armors.add(categoryMetals);
//		armors.add(categorySteels);
//		armors.add(materials.faerieSilver);
//		armors.add(materials.damascus);
//		this.suitableMap.put(ToolCategory.ARMOR, armors);
//
//		SuitableList leggins = new SuitableList();
//		SuitableList boots = new SuitableList();
//		leggins.add(categoryClothes);
//		boots.add(categoryLeathers);
//		boots.add(categoryWoods);
//		leggins.add(categoryRocks);
//		leggins.add(categoryDebris);
//		leggins.exclude(materials.diamond);
//		leggins.add(categoryBestials);
//		leggins.add(materials.serpentine);
//		leggins.add(materials.copperOre);
//		leggins.add(materials.quartz);
//		boots.add(materials.meteorite);
//		leggins.add(materials.ironOre);
//		boots.add(materials.silver);
//		boots.add(materials.obsidian);
//		leggins.add(materials.copper);
//		leggins.add(materials.lead);
//		leggins.add(materials.iron);
//		leggins.add(materials.meteoricIron);
//		leggins.add(categorySteels);
//		boots.add(materials.faerieSilver);
//		leggins.add(materials.damascus);
//		leggins.exclude(materials.diamond);
//		this.suitableMap.put(ToolCategory.LEGGINS, leggins);
//		this.suitableMap.put(ToolCategory.BOOTS, boots);
//
//		SuitableList shield = new SuitableList();
//		shield.add(categoryLeathers);
//		shield.add(categoryWoods);
//		shield.add(categoryScales);
//		shield.add(categoryDebris);
//		shield.add(categoryBestials);
//		shield.add(categoryRocks);
//		shield.exclude(materials.diamond);
//		shield.add(categorySteels);
//		shield.add(materials.sivaQueen);
//		this.suitableMap.put(ToolCategory.SHIELD, shield);
//
//		SuitableList accessory = new SuitableList();
//		accessory.add(materials.feather);
//		accessory.add(categoryWoods);
//		accessory.add(categoryTusks);
//		accessory.add(categoryBones);
//		accessory.add(categoryScales);
//		accessory.add(materials.lightStone);
//		accessory.add(materials.darkStone);
//		accessory.add(categoryDebris);
//		accessory.add(categoryBestials);
//		accessory.add(categoryRocks);
//		accessory.add(categoryMetals);
//		accessory.add(categorySteels);
//		accessory.add(materials.sivaQueen);
//		accessory.add(categoryCorundums);
//		this.suitableMap.put(ToolCategory.ACCESSORY, accessory);
	}


//	public static SuitableList getSuitableList(ToolCategory cate){
//
//		return SuitableLists.instance().suitableMap.get(cate);
//	}

	public static Set<UnsagaMaterial> getSuitables(ToolCategory cate){

		return SuitableLists.instance().suitables.get(cate);
	}

	public static Set<UnsagaMaterial> getSuitableMerchadises(ToolCategory cate){

		return SuitableLists.instance().suitables.get(cate).stream().filter(in -> UnsagaMaterialRegistry.instance().merchandiseMaterial.contains(in)).collect(Collectors.toSet());
	}
	//何か特別な動きがいると思ってラップしたけど必要なさそう…
	public static class SuitableList{

		List<UnsagaMaterial> list = Lists.newArrayList();

		public void add(UnsagaMaterialRegistry.Category cate){
			for(UnsagaMaterial m:cate.getChildMaterials()){
				list.add(m);
			}
		}

		public void add(List<UnsagaMaterial> materials){
			Preconditions.checkNotNull(materials);
			list.addAll(materials);
		}

//		public void add(String id){
//			List<UnsagaMaterial> category = UnsagaMaterials.instance().categorised.get("id");
//			Preconditions.checkNotNull(id);
//			list.addAll(category);
//		}
		public void add(UnsagaMaterial m){
			list.add(m);
		}

		public void add(UnsagaMaterial... materials){
			for(UnsagaMaterial m:materials){
				list.add(m);
			}
		}

		public UnsagaMaterial getRandom(Random rand,boolean isMerchandise){
			List<UnsagaMaterial> copylist;
			if(isMerchandise){
				copylist = list.stream().filter(in -> UnsagaMaterialRegistry.instance().merchandiseMaterial.contains(in)).collect(Collectors.toList());
			}else{
				copylist = Lists.newArrayList(list);
			}
			Collections.shuffle(copylist);
			return copylist.get(0);
		}
		public boolean contain(UnsagaMaterial m){
			return this.list.contains(m);
		}
		public List<UnsagaMaterial> values(){
			return this.list;
		}

		public void exclude(UnsagaMaterial m){
			list.remove(m);
		}
	}
	@Override
	public void applyJson(ParserSuitable parser) {
		// TODO 自動生成されたメソッド・スタブ
//		this.suitableMap.

		for(UnsagaMaterial m:parser.m){
			for(ToolCategory cate:parser.categories){
				Set<UnsagaMaterial> set = Sets.newHashSet();
				if(this.suitables.get(cate)!=null){
					if(!this.suitables.get(cate).isEmpty()){
						set.addAll(this.suitables.get(cate));
					}
				}
				set.add(m);
				UnsagaMod.logger.trace("registered suitables", cate,m,set);
				this.suitables.put(cate, ImmutableSet.copyOf(set));
			}
		}

	}
}
