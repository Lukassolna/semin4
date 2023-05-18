package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import integration.ExternalSystemHandler;
import integration.LogWriter;
import integration.NoDatabaseException;
import integration.NoItemFoundException;
import integration.Printer;
import model.CashRegister;
import model.Item;
import model.Sale;
import model.SaleObserver;
import model.CashPayment;

public class Controller {
	private ExternalSystemHandler extSysHan;
	private CashRegister cashRegister;
	private Printer printer;
	private Sale saleInformation;
	private LogWriter logWriter;
	private List <SaleObserver>  saleObservers = new ArrayList<>();	 

	/**
	 * Constructs a new Controller instance and initializes the cash
	 * register,printer and external system handler.
	 */
	public Controller(CashRegister cashRegister, Printer printer, ExternalSystemHandler extSysHan) {
		this.cashRegister = cashRegister;
		this.extSysHan = extSysHan;
		this.printer = printer;
		
		 try {
	            logWriter = new LogWriter();
	        } catch (IOException e) {
	            // Handle any potential exceptions
	            e.printStackTrace();
	           
	        }
	}

	/**
	 * Starts a new sale by creating a new Sale object.
	 */
	public void startSale() {
		saleInformation = new Sale(printer);
	}

	/**
	 * Looks up an item in the external inventory system based on its ID and adds it
	 * to the current sale if its exists
	 * 
	 * @param itemID the ID of the item to look up
	 * @return true if the item is found and added to the sale, false otherwise
	 */
	public boolean enterIdentifier(int itemID) {
	    try {
	        Item foundItem = extSysHan.getExternalInventorySystem().fetchItemInformation(itemID);
	        if (foundItem != null) {
	            saleInformation.addItem(foundItem);
	            return true;
	        } else {
	            return false;
	        }
	    } catch (NoItemFoundException e) {
	    	logWriter.logException(e);
	    	System.out.println(e.getIncorrectID());
	    	System.out.println(e.errorMessage());
	    	
	        // Handle NoItemFoundException
	        // Perform any necessary actions or logging
	        return false;
	    } catch (NoDatabaseException a) {
	    	logWriter.logException(a);
	    	System.out.println(a.getErrorMessage());
	    	
	        // Handle NoDatabaseException
	        // Perform any necessary actions or logging
	        return false; 
	    }
	}

	



	/**
	 * Fetches the discount for a customer based on their ID and the current sale.
	 * 
	 * @param customerID      the ID of the customer
	 * @param saleinformation the current sale
	 * @return the discount percentage for the customer
	 */
	public void applyDiscount(int customerID) {
		double discountToApply = extSysHan.fetchDiscount(customerID);
		saleInformation.applyDiscount(discountToApply);
	}

	/**
	 * Processes a cash payment for the current sale, updates external systems,
	 * creates a Cashpayment and adds it to cashregisters.
	 * 
	 * @param amount the amount of cash paid by the customer
	 */
	public void pay(double amount) {
		CashPayment cashPayment=new CashPayment(amount);
		cashRegister.addPayment(cashPayment);
		saleInformation.addSaleObservers(saleObservers);
		saleInformation.pay(cashPayment);
		extSysHan.updateExternalSystems(saleInformation);
		
	}

	public double change() {
		return saleInformation.change();
	}
	
	public List<Item> getItemsInSale() {
		return saleInformation.getItemList();
	}

	/**
	 * Ends the current sale and returns the total price of the items in the sale.
	 * 
	 * @return the total price of the items in the sale
	 */
	public double endSale() {
		return saleInformation.getTotalPrice();
	}

	/**
	 * Prints receipt for the current Sale
	 */
	public void printReceipt() {
		saleInformation.printReceipt();
	}

	public Sale getSaleInformation() {
		return saleInformation;
	}
	
	public void addSaleObserver(SaleObserver obs) {
		saleObservers.add(obs);
		
	}
}




