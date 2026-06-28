package it.unibo;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

import java.io.File;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * Gestore centralizzato del database MapDB.
 * Implementa il pattern Singleton per garantire che un solo processo
 * alla volta acceda al file progetto_sweng.db.
 */
public class DatabaseCore {

    private static DB db;
    private static boolean testMode = false;

    
    // Attiva la modalità test (Database in memoria RAM).
    public static void enableTestMode() {
        testMode = true;
        close(); 
    }

    // Disattiva la modalità test e torna al database su file.
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

        seedCategorie(db);
    }

    private static void seedCategorie(DB db) {
        Set<String> dbCategorie = (Set<String>) db.hashSet("categorie", Serializer.STRING).createOrOpen();
        if (dbCategorie.isEmpty()) {
            try (InputStream is = DatabaseCore.class.getClassLoader().getResourceAsStream("cat.txt")) {
                if (is != null) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            String categoria = line.trim();
                            if (!categoria.isEmpty()) {
                                dbCategorie.add(categoria);
                            }
                        }
                    }
                    DatabaseCore.commit();
                    System.out.println("MAPDB -> Categorie caricate da file con successo.");
                } else {
                    System.err.println("MAPDB -> ATTENZIONE: File 'cat.txt' non trovato nelle resources!");
                }
            } catch (Exception e) {
                System.err.println("MAPDB -> Errore durante la lettura delle categorie: " + e.getMessage());
            }
        }
    }

    // Restituisce la lista completa delle categorie in formato immutabile.
    public static List<String> getCategorie() {
        DB db = getDB();
        Set<String> dbCategorie = (Set<String>) db.hashSet("categorie", Serializer.STRING).createOrOpen();
        List<String> categorieList = new ArrayList<>(dbCategorie);
        Collections.sort(categorieList);
        return Collections.unmodifiableList(categorieList);
    }

    // Restituisce l'istanza attiva del database, è synchronized per prevenire accessi contemporanei da thread diversi.
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

    // Salva permanentemente le modifiche su disco.
    public static void commit() {
        if (db != null && !db.isClosed()) {
            db.commit();
        }
    }

    // Chiude la connessione al database e libera il file.
    public static void close() {
        if (db != null && !db.isClosed()) {
            db.close();
        }
        db = null;
    }
}