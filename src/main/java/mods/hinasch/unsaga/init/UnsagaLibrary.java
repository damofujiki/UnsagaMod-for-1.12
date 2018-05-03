package mods.hinasch.unsaga.init;

import mods.hinasch.unsaga.UnsagaMod;
import mods.hinasch.unsaga.element.newele.ElementAssociationLibrary;
import mods.hinasch.unsaga.villager.bartering.BarteringItemRegistry;
import mods.hinasch.unsaga.villager.smith.SmithMaterialRegistry;

public class UnsagaLibrary {

	protected static UnsagaLibrary instance;

//	ElementPointLibrary elementPointLibrary;
	SmithMaterialRegistry smithMaterialLibray;
	BarteringItemRegistry itemPriceLibrary;
//	ValidPayments validPayments;
//	ForgingLibrary forgingLibrary;
//	MerchandisePriceLibrary merchandisePriseLibrary;

	ElementAssociationLibrary elementsNew;

	protected UnsagaLibrary(){

	}
	public static UnsagaLibrary instance(){
		if(instance==null){
			instance = new UnsagaLibrary();
		}

		return instance;
	}
//	public MerchandisePriceLibrary getMerchandisePriseLibrary() {
//		return merchandisePriseLibrary;
//	}
	public void init(){
		UnsagaMod.logger.get().info("Initialising Libraies...");
//		elementPointLibrary = new ElementPointLibrary();
		this.elementsNew = ElementAssociationLibrary.instance();
		this.elementsNew.register();
		this.smithMaterialLibray = SmithMaterialRegistry.instance();
		this.smithMaterialLibray.register();
		this.itemPriceLibrary = BarteringItemRegistry.instance();
		this.itemPriceLibrary.register();
//		validPayments = new ValidPayments();
//		forgingLibrary = new ForgingLibrary();
//		merchandisePriseLibrary = new MerchandisePriceLibrary();
	}
//	public ForgingLibrary getForgingLibrary() {
//		return forgingLibrary;
//	}

	public ElementAssociationLibrary elementLib(){
		return this.elementsNew;
	}

//	public ElementPointLibrary getElementPointLib() {
//		return elementPointLibrary;
//	}

//	public ValidPayments getValidPayments(){
//		return validPayments;
//	}
}
