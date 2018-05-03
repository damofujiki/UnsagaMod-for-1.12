package mods.hinasch.unsaga.lp;

import mods.hinasch.lib.iface.IIntSerializable;
import mods.hinasch.lib.util.HSLibs;

public class LPDifficulty {

	public static enum Type implements IIntSerializable{
		/**
		 * LPが０にならなくても敵が死ぬ。
		 * LP０/HP0.1以下で敵に与えるダメージ倍増。
		 */
		SUPER_EASY(0),
		/**
		 * LPが０にならないと敵が死なない。
		 * 敵に与えるLPダメージが多い。
		 */
		EASY(1),
		/**
		 * 通常。LPが０にならないと敵が死なない。
		 */
		NORMAL(2);

		int meta;
		private Type(int meta){
			this.meta = meta;
		}
		/**
		 * LPが残っている場合の不死身かどうか
		 * @return
		 */
		public boolean enableInvulnerablity(){
			switch(this){
			case EASY:
				return true;
			case NORMAL:
				return true;
			case SUPER_EASY:
				break;
			default:
				break;

			}
			return false;
		}

		/**
		 * 敵LP０やHP0.1の時にダメージを倍増させるか
		 *
		 * @return
		 */
		public boolean enableDamageMultiplyToWounded(){
			switch(this){
			case EASY:
				return true;
			case NORMAL:
				return false;
			case SUPER_EASY:
				return true;
			default:
				break;

			}
			return false;
		}

		/**
		 * 敵に与えるLPダメージを易しめにする
		 * @return
		 */
		public boolean isLPDamageEasy(){
			switch(this){
			case EASY:
				return true;
			case NORMAL:
				return false;
			case SUPER_EASY:
				return true;
			default:
				break;

			}
			return false;
		}
		@Override
		public int getMeta() {
			// TODO 自動生成されたメソッド・スタブ
			return meta;
		}

		public static Type fromMeta(int meta){
			return HSLibs.fromMeta(Type.values(), meta);
		}


	}

}
