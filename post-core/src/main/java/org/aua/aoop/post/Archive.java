package org.aua.aoop.post;

import org.aua.aoop.post.conf.AppConfig;
import org.jboss.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Singleton
@Startup
public class Archive implements Serializable {

    private static String SAVE_FILE_URL = AppConfig.getInstance().getSalesLogSaveFileName();
    private static final Logger logger =  Logger.getLogger(Archive.class);
    Map<UUID, ArchiveRecord> processedSaleList;

    public Archive() {}

    @PostConstruct
    private void SetupArchive(){
        this.processedSaleList = new HashMap<>();
        logger.info("Archive Successfully Loaded!!!");
    }

    public void archiveSale(ArchiveRecord archiveRecord) {
        System.out.println("Adddddddddddddddddddding **********************************");
        processedSaleList.put(archiveRecord.getSaleID(), archiveRecord);
    }

    public void printLog() {
        System.out.println("Printing Archive  *******************************");
        for (ArchiveRecord archiveRecord : processedSaleList.values()) {
            System.out.println("\r\n" + archiveRecord.toString());
        }
    }

    public void saveSalesLogToFile() {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(SAVE_FILE_URL));
            objectOutputStream.writeObject(this);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Archive loadSalesLogFromFile() {
        Archive archive = null;
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(new FileInputStream(SAVE_FILE_URL));
            Object loadedObj = objectInputStream.readObject();
            archive = (Archive) loadedObj;

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return archive == null ? new Archive() : archive;
    }
}
