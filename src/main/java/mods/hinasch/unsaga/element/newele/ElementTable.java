package mods.hinasch.unsaga.element.newele;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.realmsclient.util.Pair;

import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.unsaga.element.FiveElements;
import mods.hinasch.unsaga.util.UnsagaTextFormatting;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;

public class ElementTable {

	public static class Mutable {
		final Map<FiveElements.Type,Float> map;

		public Mutable(){
			this.map = this.getWhiteMap();
		}

		public Mutable(ElementTable table){
			this.map = Maps.newHashMap(table.map);
		}

		public void add(ElementTable other){
			for(FiveElements.Type type:other.map.keySet()){
				float amount = other.get(type) + this.map.get(type);
				this.map.put(type, amount);
			}
		}

		public void add(FiveElements.Type type,float f){
			float amount = this.map.get(type) + f;
			this.map.put(type, amount);
		}
		public float get(FiveElements.Type type){
			return this.map.get(type);
		}

		private Map<FiveElements.Type,Float> getWhiteMap(){
			Map<FiveElements.Type,Float> builder = Maps.newHashMap();
			for(FiveElements.Type type:FiveElements.Type.values()){
				builder.put(type, 0.0F);
			}
			return builder;
		}

		public void set(FiveElements.Type type,float in){
			this.map.put(type, in);
		}
		public ElementTable toImmutable(){
			return new ElementTable(this);
		}

		public void limit(float min,float max) {
			for(FiveElements.Type type:FiveElements.Type.values()){
				if(this.get(type)>max){
					this.set(type, max);
				}
				if(this.get(type)<min){
					this.set(type, min);
				}
			}
		}
	}

	public static final ElementTable ZERO = new ElementTable(0,0,0,0,0,0);


	final ImmutableMap<FiveElements.Type,Float> map;
	public ElementTable(){
		this.map = ImmutableMap.<FiveElements.Type,Float>builder().putAll(this.getWhiteMap()).build();
	}
	public ElementTable(ElementTable.Mutable mutable){
		this.map = ImmutableMap.copyOf(mutable.map);
	}
	public ElementTable(FiveElements.Type type,float p){
		Map<FiveElements.Type,Float> builder = this.getWhiteMap();
		builder.put(type,p);
		this.map = ImmutableMap.<FiveElements.Type,Float>builder().putAll(builder).build();
	}

	public ElementTable(float fire,float earth,float metal,float water,float wood,float forbidden){
		Builder<FiveElements.Type,Float> builder = ImmutableMap.builder();
		builder.put(FiveElements.Type.FIRE, fire);
		builder.put(FiveElements.Type.EARTH, earth);
		builder.put(FiveElements.Type.METAL, metal);
		builder.put(FiveElements.Type.WATER, water);
		builder.put(FiveElements.Type.WOOD, wood);
		builder.put(FiveElements.Type.FORBIDDEN, forbidden);
		this.map = builder.build();
	}
	protected ElementTable(Map<FiveElements.Type,Float> map){
		Builder<FiveElements.Type,Float> builder = ImmutableMap.builder();
		builder.putAll(map);
		this.map = builder.build();
	}

	public ElementTable(Pair<FiveElements.Type,Float>... entries){
		Mutable mutable = new Mutable();
		for(Pair<FiveElements.Type,Float> pair:entries){
			mutable.set(pair.first(), pair.second());
		}
		this.map = ImmutableMap.copyOf(mutable.map);
	}
	public ElementTable add(ElementTable other){
		Mutable mutable = new Mutable(this);
		mutable.add(other);
		return new ElementTable(mutable);
	}

	public ElementTable add(FiveElements.Type type,float in){
		Mutable mutable = new Mutable(this);
		mutable.add(type,in);
		return new ElementTable(mutable);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null)return false;
		if(this.getClass()!=obj.getClass())return false;
		ElementTable other = (ElementTable) obj;
		return Lists.newArrayList(FiveElements.Type.values())
		.stream().allMatch(type ->this.get(type)==other.get(type));
	}

	public ElementTable scale(float scale){
		ElementTable.Mutable mutable = new Mutable(this);
		for(FiveElements.Type type:FiveElements.Type.values()){
			float f = mutable.get(type);
			BigDecimal bd = new BigDecimal(f*scale);
			mutable.set(type, bd.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue());
		}
		return mutable.toImmutable();
	}
	public float get(FiveElements.Type elm){
		BigDecimal bd = new BigDecimal(this.map.get(elm));
		return bd.setScale(3, RoundingMode.HALF_DOWN).floatValue();
	}

	public String getAmountAsFloatLocalized(){
		return HSLibs.translateKey("elements.unsaga.float", this.get(FiveElements.Type.FIRE)
				,this.get(FiveElements.Type.METAL),this.get(FiveElements.Type.WOOD)
				,this.get(FiveElements.Type.WATER),this.get(FiveElements.Type.EARTH)
				,this.get(FiveElements.Type.FORBIDDEN));
	}
	public String getAmountAsIntLocalized(){
		return HSLibs.translateKey("elements.unsaga.int", this.getInt(FiveElements.Type.FIRE)
				,this.getInt(FiveElements.Type.METAL),this.getInt(FiveElements.Type.WOOD)
				,this.getInt(FiveElements.Type.WATER),this.getInt(FiveElements.Type.EARTH)
				,this.getInt(FiveElements.Type.FORBIDDEN));
	}

	public String getAmountAsPercentageLocalized(boolean reverse){
		List<String> list = Lists.newArrayList();
		for(FiveElements.Type type:FiveElements.Type.values()){
			TextFormatting tf;
			if(reverse){
				tf = this.get(type)<0 ? UnsagaTextFormatting.POSITIVE : this.get(type)>0 ? UnsagaTextFormatting.NEGATIVE : TextFormatting.WHITE;
			}else{
				tf = this.get(type)<0 ? UnsagaTextFormatting.NEGATIVE : this.get(type)>0 ? UnsagaTextFormatting.POSITIVE : TextFormatting.WHITE;
			}

			String valu = String.valueOf((int)(this.get(type)*100F));
			list.add(tf+valu+"%"+TextFormatting.WHITE);
		}
		return HSLibs.translateKey("elements.unsaga.percentage",list.get(0),list.get(1),list.get(2),list.get(3),list.get(4),list.get(5));
	}
	public int getInt(FiveElements.Type elm){
		return this.map.get(elm).intValue();
	}

	private Map<FiveElements.Type,Float> getWhiteMap(){
		Map<FiveElements.Type,Float> builder = Maps.newHashMap();
		for(FiveElements.Type type:FiveElements.Type.values()){
			builder.put(type, 0.0F);
		}
		return builder;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(this.get(FiveElements.Type.EARTH));
		result = prime * result + Float.floatToIntBits(this.get(FiveElements.Type.FIRE));
		result = prime * result + Float.floatToIntBits(this.get(FiveElements.Type.METAL));
		result = prime * result + Float.floatToIntBits(this.get(FiveElements.Type.WATER));
		result = prime * result + Float.floatToIntBits(this.get(FiveElements.Type.METAL));
		result = prime * result + Float.floatToIntBits(this.get(FiveElements.Type.FORBIDDEN));
		return result;
	}

	@Override
	public String toString(){
		return this.getAmountAsFloatLocalized();
	}
	public boolean isBiggerOrEqual(ElementTable other){
		return Lists.newArrayList(FiveElements.Type.values()).stream().allMatch(in -> this.get(in)>=other.get(in));
	}

	public static ElementTable of(FiveElements.Type type,float value){
		return new ElementTable(type,value);
	}

	public static ElementTable of(NonNullList<FiveElements.Type> types){
		Mutable mutable = new Mutable();
		for(FiveElements.Type type:types){
			mutable.add(type, 1);
		}
		return mutable.toImmutable();
	}
}
