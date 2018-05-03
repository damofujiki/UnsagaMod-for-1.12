package mods.hinasch.unsaga.core.item.misc.icon;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import mods.hinasch.lib.iface.IIntSerializable;
import mods.hinasch.lib.item.ItemIcon;
import mods.hinasch.lib.util.HSLibs;
import mods.hinasch.lib.world.EnvironmentalManager.EnvironmentalCondition;

public class ItemIconCondition extends ItemIcon{

	public static final List<String> variants = Lists.newArrayList("hot","cool","humid");

	public static enum Type implements IIntSerializable{
		FINE(0,EnvironmentalCondition.Type.SAFE),HOT(1,EnvironmentalCondition.Type.COLD)
		,COOL(2,EnvironmentalCondition.Type.HOT),HUMID(3,EnvironmentalCondition.Type.HUMID);;

		final EnvironmentalCondition.Type condition;
		final int meta;
		private Type(int meta,EnvironmentalCondition.Type condition){
			this.meta = meta;
			this.condition = condition;
		}
		public static Type fromMeta(int meta){
			return HSLibs.fromMeta(Type.values(), meta);
		}
		public static Type fromConditionType(EnvironmentalCondition.Type type){
			Optional<Type> cond =  Lists.newArrayList(Type.values()).stream().filter(c -> c.getAssociatedCondition() == type)
					.findFirst();
			return cond.isPresent() ? cond.get() : Type.FINE;
		}

		public EnvironmentalCondition.Type getAssociatedCondition(){
			return this.condition;
		}

		public String getIconName(){
			return "icon/icon."+this.getAssociatedCondition().getName();
		}

		public static List<String> getIconNames(){
			return Lists.newArrayList(Type.values()).stream().map(in -> in.getIconName()).collect(Collectors.toList());
		}

		@Override
		public int getMeta() {
			// TODO 自動生成されたメソッド・スタブ
			return meta;
		}


	}

//	public static ItemStack getIcon(EnvironmentalCondition condition){
//		Type icontype = Type.fromConditionType(condition.getType());
//		ItemStack is = new ItemStack(UnsagaRegistries.ITEMS.iconCondition,1,icontype.getMeta());
//		return is;
//	}
}
