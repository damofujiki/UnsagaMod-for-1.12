package mods.hinasch.unsaga.ability;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;

import net.minecraft.util.NonNullList;

/**
 *
 * {@code NonNullList<Set<IAbility>>}のラップクラス。
 *
 */
public class AbilityLearnableTable{


	NonNullList<Set<IAbility>> table;

	public static final AbilityLearnableTable EMPTY = new AbilityLearnableTable();

	public AbilityLearnableTable(){
		this.table = NonNullList.create();
	}
	public AbilityLearnableTable(int size){
		this.table = NonNullList.withSize(size, Sets.newHashSet());
	}
	public AbilityLearnableTable(NonNullList<IAbility> list){
		this.table = NonNullList.withSize(list.size(), Sets.newHashSet());
		for(int i=0;i<list.size();i++){
			if(!list.get(i).isAbilityEmpty()){
				this.table.set(i, Sets.newHashSet(list.get(i)));
			}

		}
	}


	public Set<IAbility> get(int index){
		return this.table.get(index);
	}

	public boolean isEmpty(){
		return this==EMPTY ? true : false;
	}

	public void set(int index,Collection<IAbility> abilities){
		this.table.set(index, Sets.newHashSet(abilities));
	}
	public void set(int index,IAbility ability){
		this.table.set(index, Sets.newHashSet(ability));
	}
	public int size(){
		return this.table.size();
	}
	public Stream<Set<IAbility>> stream(){
		return this.table.stream();
	}

	public boolean hasLearnableAbilityAt(int index){
		if(index<this.table.size()){
			return !this.get(index).isEmpty();
		}
		return false;
	}

	public String toString(){
		StringBuilder builder = new StringBuilder();
		for(int i=0;i<table.size();i++){
			builder.append("/");
			builder.append(Joiner.on(",").join(table.get(i)));
		}
		return builder.toString();
	}

}
