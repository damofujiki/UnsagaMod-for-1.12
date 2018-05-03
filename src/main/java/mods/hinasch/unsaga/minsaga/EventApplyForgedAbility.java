//package mods.hinasch.unsaga.minsaga;
//
//import java.util.List;
//import java.util.Random;
//
//import mods.hinasch.lib.item.ItemUtil;
//import mods.hinasch.unsaga.damage.DamageSourceUnsaga;
//import mods.hinasch.unsaga.util.LivingHurtEventUnsagaBase;
//import net.minecraft.entity.EntityLivingBase;
//import net.minecraft.init.MobEffects;
//import net.minecraft.item.ItemStack;
//import net.minecraft.potion.PotionEffect;
//import net.minecraft.util.DamageSource;
//import net.minecraft.util.math.MathHelper;
//import net.minecraft.world.DimensionType;
//import net.minecraftforge.event.entity.living.LivingHurtEvent;
//
//public class EventApplyForgedAbility extends LivingHurtEventUnsagaBase{
//
//	@Override
//	public boolean apply(LivingHurtEvent e, DamageSourceUnsaga dsu) {
//		// TODO 自動生成されたメソッド・スタブ
//		return true;
//	}
//
//
//	public DamageSourceUnsaga processWeapon(LivingHurtEvent e,DamageSourceUnsaga dsu){
//		EntityLivingBase el = (EntityLivingBase) e.getSource().getTrueSource();
//		ItemStack held = el.getHeldItemMainhand();
//		if(ItemUtil.isItemStackPresent(held) && MinsagaUtil.getAbilities(e.getEntityLiving()).contains(MinsagaForging.Ability.WEAKNESS)){
//			Random rand = el.getRNG();
//			if(rand.nextInt(3)==0){
//				e.getEntityLiving().addPotionEffect(new PotionEffect(MobEffects.POISON,ItemUtil.getPotionTime(15),0));
//			}
//		}
//		return dsu;
//	}
//
//	public DamageSourceUnsaga processArmor(LivingHurtEvent e,DamageSourceUnsaga dsu){
//		List<MinsagaForging.Ability> abilities = MinsagaUtil.getAbilities(e.getEntityLiving());
//		int blastAmount = (int) abilities.stream().filter(in -> in==MinsagaForging.Ability.METEOR).count();
//		if(blastAmount>0 && dsu.isExplosion()){
//			float base = e.getAmount();
//			float reduce = base * 0.15F * blastAmount;
//			reduce = MathHelper.clamp(blastAmount, 0, 0.7F);
//			e.setAmount(base - reduce);
//		}
//		int abyssAmount = (int) abilities.stream().filter(in -> in==MinsagaForging.Ability.ABYSS).count();
//		if(abyssAmount>0 && e.getEntityLiving().getEntityWorld().provider.getDimensionType()==DimensionType.THE_END){
//			float base = e.getAmount();
//			float reduce = base * 0.15F * abyssAmount;
//			reduce = MathHelper.clamp(abyssAmount, 0, 0.7F);
//			e.setAmount(base - reduce);
//		}
//		int darkAmount = (int) abilities.stream().filter(in -> in==MinsagaForging.Ability.DARK).count();
//		if(darkAmount>0 && e.getEntityLiving().getEntityWorld().provider.getDimensionType()==DimensionType.NETHER){
//			float base = e.getAmount();
//			float reduce = base * 0.15F * darkAmount;
//			reduce = MathHelper.clamp(darkAmount, 0, 0.7F);
//			e.setAmount(base - reduce);
//		}
//		return dsu;
//	}
//	@Override
//	public DamageSource process(LivingHurtEvent e, DamageSourceUnsaga dsu) {
//		DamageSourceUnsaga rt = this.processArmor(e, dsu);
//		if(e.getSource().getTrueSource() instanceof EntityLivingBase){
//			rt = this.processWeapon(e, dsu);
//		}
//		return rt;
//	}
//
//	@Override
//	public String getName() {
//		// TODO 自動生成されたメソッド・スタブ
//		return "minsaga forging  event";
//	}
//
//}
