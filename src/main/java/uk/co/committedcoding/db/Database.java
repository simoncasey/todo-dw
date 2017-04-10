package uk.co.committedcoding.db;

import org.mapdb.DB;
import org.mapdb.DBMaker;

import javax.inject.Singleton;
import java.io.File;

/**
 * Created by Simon Casey on 10/04/2017.
 */
@Singleton
public class Database {

    private DB db;

    public Database(String dbFilePath) {
        File dbFile = new File(dbFilePath);
        db = DBMaker
                .fileDB(dbFile)
                .fileMmapEnable()            // Always enable mmap
                .fileMmapEnableIfSupported() // Only enable mmap on supported platforms
                .fileMmapPreclearDisable()   // Make mmap file faster
                .cleanerHackEnable()
                .make();
        db.getStore().fileLoad();
    }

    public DB getDb() {
        return db;
    }
}
