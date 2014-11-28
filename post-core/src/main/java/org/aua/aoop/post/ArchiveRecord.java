package org.aua.aoop.post;
import org.aua.aoop.post.payment.AbstractPayment;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by azakaryan on 28.11.2014.
 */

public class ArchiveRecord {

    private List<SaleItem> saleItems;
    private AbstractPayment payment;
    private String customerName;
    private UUID saleID;
    private Date dateTime;
    private double total;

    public ArchiveRecord(List<SaleItem> saleItems, AbstractPayment payment, String customerName, UUID saleID, double total) {
        this.payment = payment;
        this.customerName = customerName;
        this.saleItems = saleItems;
        this.saleID = saleID;
        this.total = total;
        this.dateTime = new Date();
    }

    /**
     * @return
     */
    public String customerName() {  return this.customerName;}

    /**
     * @return
     */
    public List<SaleItem> getSaleItems() { return saleItems; }

    /**
     * @return
     */
    public Date getDate() { return dateTime; }

    /**
     *
      * @return
     */
    public UUID getSaleID(){return saleID;}

    /**
     *
     * @return
     */
    public double getTotal(){return total;}

    @Override
    public String toString() {

        String saleProducts = "";

        for (SaleItem saleItem : saleItems) {
            saleProducts += saleItem.toString();
        }

        String printLog = "Sale{" +
                "saleID = " + saleID +
                "\n\t dateTime = " + dateTime +
                "\n\t customerName = '" + customerName + '\'' +
                "\n\t saleItems = " + saleProducts +
                "\n\t total = " + total +
                "\n\t payment = " + payment +
                '}';

        return printLog;
    }

}
