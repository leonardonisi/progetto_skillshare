package it.unibo;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import java.io.File;
import org.mapdb.Serializer;
import java.util.concurrent.ConcurrentMap;

/**
 * Gestore centralizzato del database MapDB.
 * Implementa il pattern Singleton per garantire che un solo processo
 * alla volta acceda al file progetto_sweng.db.
 */
public class DatabaseCore {

    private static DB db;
    private static boolean testMode = false;

    /**
     * Attiva la modalità test (Database in memoria RAM).
     */
    public static void enableTestMode() {
        testMode = true;
        close(); 
    }

    /**
     * Disattiva la modalità test e torna al database su file.
     */
    public static void disableTestMode() {
        testMode = false;
        close(); 
    }

    // inizializza il database con annunci preimpostati
    public static void seedDatabase() {
        DB db = DatabaseCore.getDB();
        ConcurrentMap<Integer, Annuncio> dbAnnunci = db.hashMap("annunci", Serializer.INTEGER, Serializer.JAVA).createOrOpen();
        
        if (dbAnnunci.isEmpty()) {
            for (int i = 1; i <= 10; i++) {
                Annuncio a = new Annuncio.Builder()
                    .titolo("Skill #" + i)
                    .categoria("Sviluppo Software")
                    .skillOfferta("Java GWT")
                    .controprestazioneCercata("Grafica")
                    .disponibilita("Weekend")
                    .utenteId("User" + i)
                    .build();
                dbAnnunci.put(i, a);
            }
            DatabaseCore.commit();
        }
    }

    /**
     * Restituisce l'istanza attiva del database.
     * synchronized per prevenire accessi contemporanei da thread diversi.
     */
    public static synchronized DB getDB() {
        if (db == null || db.isClosed()) {
            if (testMode) {
                // Database temporaneo in RAM per i test
                db = DBMaker.memoryDB()
                        .transactionEnable()
                        .make();
            } else {
                // --- MODIFICA PER IL CLOUD ---
                // Cerca una variabile d'ambiente chiamata "DATA_DIR"
                String dataDir = System.getenv("DATA_DIR");
                String dbPath = "progetto_sweng.db"; // Fallback: percorso locale sul tuo PC
                
                if (dataDir != null && !dataDir.trim().isEmpty()) {
                    // Crea la cartella sul server cloud se non esiste ancora
                    File dir = new File(dataDir);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    // Costruisce il percorso finale, es: "/percorso/cloud/progetto_sweng.db"
                    dbPath = dataDir + (dataDir.endsWith(File.separator) ? "" : File.separator) + "progetto_sweng.db";
                    System.out.println("MAPDB -> Avvio in modalità CLOUD. Percorso: " + dbPath);
                } else {
                    System.out.println("MAPDB -> Avvio in modalità LOCALE.");
                }

                // Database persistente su FILE
                db = DBMaker.fileDB(dbPath)
                        .transactionEnable()
                        .closeOnJvmShutdown() // rilascia il lock del file alla chiusura dell'app
                        .make();
            }
        }
        return db;
    }

    /**
     * Salva permanentemente le modifiche su disco.
     */
    public static void commit() {
        if (db != null && !db.isClosed()) {
            db.commit();
        }
    }

    /**
     * Chiude la connessione al database e libera il file.
     */
    public static void close() {
        if (db != null && !db.isClosed()) {
            db.close();
        }
        db = null;
    }
}