package mods.hinasch.unsaga.core.inventory.container;

import mods.hinasch.unsaga.element.newele.ElementTable;

/** 未完*/
public class MatrixAdapterElement extends HexMatrixAdapter<ElementTable>{

	@Override
	public ElementTable getMatrix(int x, int y) {
		if(x==0 && y==0){
			return new ElementTable(1.0F,1.0F,0F,0F,2.0F,0F);
		}
		if(x==1 && y==0){
			return new ElementTable(2.0F,1.0F,0F,0F,1.0F,0F);
		}
		if(x==1 && y==1){
			return new ElementTable(1.0F,1.0F,1.0F,1.0F,1.0F,1.0F);
		}
		if(x==0 && y==1){
			return new ElementTable(0.0F,0.0F,1.0F,2.0F,0.0F,1.0F);
		}
		if(x==2 && y==1){
			return new ElementTable(1.0F,2.0F,1.0F,0.0F,0.0F,0.0F);
		}
		if(x==0 && y==2){
			return new ElementTable(0.0F,0.0F,1.0F,1.0F,0.0F,0.0F);
		}
		if(x==1 && y==2){
			return new ElementTable(0.0F,1.0F,1.0F,0.0F,0.0F,0.0F);
		}
		return null;
	}

	@Override
	public boolean isSame(ElementTable o1, ElementTable o2) {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}


}
