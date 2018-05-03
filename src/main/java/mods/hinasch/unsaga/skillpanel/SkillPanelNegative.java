package mods.hinasch.unsaga.skillpanel;

import java.util.function.Predicate;

import net.minecraft.entity.Entity;

public class SkillPanelNegative extends SkillPanel{

	enum Type{WEAKNESS,DAMAGE};
	final Predicate<Entity> entityPredicate;
	final Type type;
	public SkillPanelNegative(String name,Predicate<Entity> pre,Type type) {
		super(name);
		this.entityPredicate = pre;
		this.type = type;
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public Type getNegativeType(){
		return this.type;
	}

	public Predicate<Entity> getTargetEntityPredicate(){
		return this.entityPredicate;
	}


}
