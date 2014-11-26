package org.aua.aoop.post;

import org.aua.aoop.post.conf.AppConfig;
import org.aua.aoop.post.product.ProductCatalog;
import org.jboss.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
public class Store {

    @EJB private Archive archive;
    @EJB private ProductCatalog productCatalog;
    private static final Logger logger =  Logger.getLogger(Store.class);
    private Manager manager;
    private String merchantID;

    public Store(){ }

    @PostConstruct
    private void SetupStore(){
        startUp();
    }

    public void startUp(){
        manager = new Manager();
        productCatalog.setManager(manager);
        merchantID = "MERCHANT_BOND_007";
        logger.info("Store Successfully Loaded!!!");
    }

    public void close() {
        saveState();
    }

    public void saveState() {
        archive.saveSalesLogToFile();
        productCatalog.saveToFile(AppConfig.getInstance().getProductCatalogFileName());
    }

    public ProductCatalog getProductCatalog() {
        return productCatalog;
    }

    public Archive getArchive() {
        return archive;
    }
}
