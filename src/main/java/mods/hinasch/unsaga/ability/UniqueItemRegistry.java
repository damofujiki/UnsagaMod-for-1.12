package mods.hinasch.unsaga.ability;

import java.util.List;

import com.google.common.collect.Lists;

import mods.hinasch.lib.registry.PropertyElementBase;
import mods.hinasch.lib.registry.PropertyRegistry;
import mods.hinasch.unsaga.ability.UniqueItemRegistry.UniqueItem;
import mods.hinasch.unsaga.material.UnsagaMaterial;
import net.minecraft.util.ResourceLocation;

public class UniqueItemRegistry extends PropertyRegistry<UniqueItem>{

	public static class UniqueItem extends PropertyElementBase{

		List<IAbility> list = Lists.newArrayList();
		UnsagaMaterial material;
		public UniqueItem(String name) {
			super(new ResourceLocation(name), name);
			// TODO 自動生成されたコンストラクター・スタブ
		}

		public UnsagaMaterial getMaterial(){
			return material;
		}
		public List<IAbility> getLearnableAbilities(){
			return list;
		}
		@Override
		public Class getParentClass() {
			// TODO 自動生成されたメソッド・スタブ
			return UniqueItem.class;
		}

	}

	@Override
	public void init() {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void preInit() {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	protected void registerObjects() {
		// TODO 自動生成されたメソッド・スタブ

	}
}
