package org.aua.aoop.post;

import org.aua.aoop.post.payment.AbstractPayment;
import org.aua.aoop.post.payment.CashPayment;
import org.aua.aoop.post.payment.CheckPayment;
import org.aua.aoop.post.payment.CreditPayment;
import org.aua.aoop.post.ex.ItemNotFoundException;
import org.aua.aoop.post.ex.NotEnoughItemsException;
import org.aua.aoop.post.ex.ProductException;
import org.aua.aoop.post.product.ProductSpecification;
import org.jboss.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Stateless
@Startup
public class Terminal {

    @EJB transient Store store;
    @EJB ShoopingCart currentShoopingCart;

    private static final Logger logger =  Logger.getLogger(Terminal.class);

    private UUID terminalID;
    private SaleItem currentSaleItem;

    public Terminal(){
        terminalID = UUID.randomUUID();
        logger.info(" User Terminal "+terminalID+" Started");
    }

    public void startNewSale(String customerName){
        currentShoopingCart.init(customerName);
        System.out.println(new Date() + "\t" + "New sale started");
    }

    public void addItem(String UPC, int qty) throws ProductException {
        ProductSpecification specification = store.getProductCatalog().getProductSpecByID(UPC);
        if (specification != null) {
            if (specification.getQty() >= qty) {
                currentSaleItem = new SaleItem(specification, qty);
                currentShoopingCart.addSaleItem(currentSaleItem);
                System.out.println(new Date() + "\t" + "New item added\t" + UPC + "\t" + qty + "\t" + currentShoopingCart.getCustomerName());
            } else {
                throw new NotEnoughItemsException();
            }
        } else {
            throw new ItemNotFoundException();
        }
    }

    public SaleItem getCurrSaleItem(){
        return currentSaleItem;
    }

    public boolean processPayment(AbstractPayment.PaymentType paymentType, double amount, String info) {
        boolean result = false;
        switch (paymentType) {
            case CASH:
                currentShoopingCart.setPayment(new CashPayment(currentShoopingCart.getTotal()));
                result = currentShoopingCart.getPayment().process(amount);
                break;
            case CHEQUE:
                currentShoopingCart.setPayment(new CheckPayment(info));
                result = currentShoopingCart.getPayment().process(amount);
                break;
            case CREDIT_CARD:
                currentShoopingCart.setPayment(new CreditPayment(info));
                result = currentShoopingCart.getPayment().process(amount);
                break;
        }

        if (result) {
            endSale();
        }
        return result;
    }


    private void endSale(){
        List<SaleItem> saleItems = currentShoopingCart.getSaleItems();

        for (SaleItem item : saleItems) {
            item.getProductSpecification().decreaseQty(item.getQty());
        }
        //This is against OOP principles However since we could not keep steteful bean ShoopingCart in Archive Thus we have created a kind of copy of the shoping cart which is not a bean for keeping in Archive
        store.getArchive().archiveSale(new ArchiveRecord(currentShoopingCart.getSaleItems(),currentShoopingCart.getPayment(), currentShoopingCart.getCustomerName(), currentShoopingCart.getSaleID(), currentShoopingCart.getTotal()));
        System.out.println(new Date() + "\t" + "Sale ended");
    }



    public String getReceipt(){
        return currentShoopingCart.toString();
    }

    public double getCashBalance(){
        return ((CashPayment) currentShoopingCart.getPayment()).getBalance();
    }

    public void setPayment(double total){
        currentShoopingCart.setPayment(new CashPayment(total));
    }

    public ShoopingCart getCurrentShoopingCart(){
        return currentShoopingCart;
    }

    public boolean productExists(String UPC) {
        return store.getProductCatalog().productExists(UPC);
    }

    @Override
    public String toString() {
        return "Terminal{" +
                "terminalID=" + terminalID +
                '}';
    }
}
