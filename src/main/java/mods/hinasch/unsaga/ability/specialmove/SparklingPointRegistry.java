package mods.hinasch.unsaga.ability.specialmove;

import java.util.Map;
import java.util.OptionalDouble;
import java.util.function.Predicate;

import com.google.common.collect.Maps;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntityStray;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityWitherSkeleton;

public class SparklingPointRegistry {


	Map<Predicate<EntityLivingBase>,Float> map = Maps.newHashMap();

	private static SparklingPointRegistry INSTANCE;
	public static SparklingPointRegistry instance(){
		if(INSTANCE==null){
			INSTANCE = new SparklingPointRegistry();
		}
		return INSTANCE;
	}
	protected SparklingPointRegistry(){

	}

	public double getSparklingPoint(EntityLivingBase entity){
		OptionalDouble rt = this.map.entrySet().stream().filter(in -> in.getKey().test(entity))
				.mapToDouble(in -> (double)in.getValue()).findFirst();
		return rt.isPresent() ? rt.getAsDouble() : 0.15D;
	}
	public void register(){
		this.map.put(in -> in instanceof EntityEnderman, 0.35F);
		this.map.put(in -> in instanceof EntityPigZombie, 0.35F);
		this.map.put(in -> in instanceof EntityWitch, 0.25F);
		this.map.put(in -> in instanceof EntityShulker, 0.25F);
		this.map.put(in -> in instanceof EntityGuardian, 0.25F);
		this.map.put(in -> in instanceof EntityBlaze, 0.25F);
		this.map.put(in -> in instanceof EntityGhast, 0.25F);
		this.map.put(in -> {
			if(in instanceof EntitySlime){
				EntitySlime slime = (EntitySlime) in;
				return slime.isSmallSlime();
			}
			return false;
		}, 0.01F);
		this.map.put(in -> in instanceof EntityWitherSkeleton, 0.15F);
		this.map.put(in -> in instanceof EntityStray, 0.15F);
	}
}
