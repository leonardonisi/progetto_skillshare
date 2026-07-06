package it.unibo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentMap;

public class DatabaseSeeder{
    public static void eseguiSeeding(){

        ConcurrentMap<String, Utente> dbUtenti = DatabaseCore.getMappaUtenti();
        ConcurrentMap<Integer, Annuncio> dbAnnunci = DatabaseCore.getMappaAnnunci();
        ConcurrentMap<Integer, RichiestaScambio> dbRichieste = DatabaseCore.getMappaRichieste();

        //creazione Utenti
        Utente filker67 = new Utente("filker67", "password", "studente Unibo", 
                                    new ArrayList<>(Arrays.asList("Cura degli Animali", "Economia e Finanza", "Data Science", "Social Media Marketing")),
                                    "Cesena, FC", "images/filker67.jpg");

        Utente brung05 = new Utente("brung05", "password", "studente Unibo", 
                                    new ArrayList<>(Arrays.asList("Sviluppo Software", "Scrittura Creativa", "Data Science", "Fotografia")),
                                    "Riva del Garda, TN", "images/brung05.jpg");

        Utente leonisi007 = new Utente("leonisi007", "password", "studente Unibo", 
                                    new ArrayList<>(Arrays.asList("Traduzioni", "Ripetizioni di Matematica", "Fotografia", "Social Media Marketing")),
                                    "Ancona, AN", "images/leonisi007.jpg");

        Utente mastroky = new Utente("mastroky", "password", "studente Unibo", 
                                    new ArrayList<>(Arrays.asList("Sviluppo Software", "Economia e Finanza", "Canto e Strumenti", "Giardinaggio")),
                                    "Ancona, AN", "images/mastroky.jpg");

        Utente admin = new Utente("admin", "password");

        //creazione Annunci
        Annuncio annuncioFilker1 = new Annuncio.Builder()
            .id(1)
            .autore("filker67")
            .titolo("Lezioni di Java e Programmazione a Oggetti")
            .categoria("Sviluppo Software")
            .skillOfferta("Java, OOP, Design Pattern")
            .controprestazioneCercata("Economia e Finanza")
            .disponibilita("Lunedì e Mercoledì sera")
            .build();

        Annuncio annuncioFilker2 = new Annuncio.Builder()
            .id(2)
            .autore("filker67")
            .titolo("Corso base di Fotografia Digitale")
            .categoria("Fotografia")
            .skillOfferta("Composizione, esposizione e uso della reflex")
            .controprestazioneCercata("Social Media Marketing")
            .disponibilita("Sabato mattina")
            .build();

        Annuncio annuncioFilker3 = new Annuncio.Builder()
            .id(3)
            .autore("filker67")
            .titolo("Traduzioni Italiano ↔ Inglese")
            .categoria("Traduzioni")
            .skillOfferta("Traduzioni di testi tecnici e universitari")
            .controprestazioneCercata("Data Science")
            .disponibilita("Tutti i pomeriggi")
            .build();

        Annuncio annuncioFilker4 = new Annuncio.Builder()
            .id(4)
            .autore("filker67")
            .titolo("Laboratorio di Scrittura Creativa")
            .categoria("Scrittura Creativa")
            .skillOfferta("Storytelling, dialoghi e sviluppo personaggi")
            .controprestazioneCercata("Cura degli Animali")
            .disponibilita("Giovedì sera")
            .build();

        Annuncio annuncioFilker5 = new Annuncio.Builder()
            .id(5)
            .autore("filker67")
            .titolo("Ripetizioni di Matematica per il Biennio")
            .categoria("Ripetizioni di Matematica")
            .skillOfferta("Algebra, geometria e funzioni")
            .controprestazioneCercata("Economia e Finanza")
            .disponibilita("Domenica pomeriggio")
            .build();

        filker67.getAnnunciPubblicati().add(annuncioFilker1.getId());
        filker67.getAnnunciPubblicati().add(annuncioFilker2.getId());
        filker67.getAnnunciPubblicati().add(annuncioFilker3.getId());
        filker67.getAnnunciPubblicati().add(annuncioFilker4.getId());
        filker67.getAnnunciPubblicati().add(annuncioFilker5.getId());

        Annuncio annuncioBrung1 = new Annuncio.Builder()
            .id(6)
            .autore("brung05")
            .titolo("Introduzione agli Investimenti e alla Finanza Personale")
            .categoria("Economia e Finanza")
            .skillOfferta("Budget, ETF, gestione del risparmio")
            .controprestazioneCercata("Sviluppo Software")
            .disponibilita("Martedì sera")
            .build();

        Annuncio annuncioBrung2 = new Annuncio.Builder()
            .id(7)
            .autore("brung05")
            .titolo("Analisi dei dati con Python")
            .categoria("Data Science")
            .skillOfferta("Python, Pandas, visualizzazione dati")
            .controprestazioneCercata("Fotografia")
            .disponibilita("Mercoledì pomeriggio")
            .build();

        Annuncio annuncioBrung3 = new Annuncio.Builder()
            .id(8)
            .autore("brung05")
            .titolo("Gestione di un profilo Instagram per attività locali")
            .categoria("Social Media Marketing")
            .skillOfferta("Content planning, Reel e metriche")
            .controprestazioneCercata("Traduzioni")
            .disponibilita("Weekend")
            .build();

        Annuncio annuncioBrung4 = new Annuncio.Builder()
            .id(9)
            .autore("brung05")
            .titolo("Lezioni di Chitarra per Principianti")
            .categoria("Canto e Strumenti")
            .skillOfferta("Accordi, ritmo e accompagnamento")
            .controprestazioneCercata("Data Science")
            .disponibilita("Lunedì sera")
            .build();

        Annuncio annuncioBrung5 = new Annuncio.Builder()
            .id(10)
            .autore("brung05")
            .titolo("Consigli pratici per la cura del giardino")
            .categoria("Giardinaggio")
            .skillOfferta("Potatura, irrigazione e manutenzione stagionale")
            .controprestazioneCercata("Scrittura Creativa")
            .disponibilita("Sabato mattina")
            .build();

        brung05.getAnnunciPubblicati().add(annuncioBrung1.getId());
        brung05.getAnnunciPubblicati().add(annuncioBrung2.getId());
        brung05.getAnnunciPubblicati().add(annuncioBrung3.getId());
        brung05.getAnnunciPubblicati().add(annuncioBrung4.getId());
        brung05.getAnnunciPubblicati().add(annuncioBrung5.getId());

        Annuncio annuncioLeo1 = new Annuncio.Builder()
            .id(11)
            .autore("leonisi007")
            .titolo("Sviluppo di applicazioni Java desktop")
            .categoria("Sviluppo Software")
            .skillOfferta("Java, Swing, programmazione orientata agli oggetti")
            .controprestazioneCercata("Traduzioni")
            .disponibilita("Lunedì e Giovedì sera")
            .build();

        Annuncio annuncioLeo2 = new Annuncio.Builder()
            .id(12)
            .autore("leonisi007")
            .titolo("Corso base di Fotografia e Post-produzione")
            .categoria("Fotografia")
            .skillOfferta("Scatto in manuale, Lightroom e composizione")
            .controprestazioneCercata("Social Media Marketing")
            .disponibilita("Sabato pomeriggio")
            .build();

        Annuncio annuncioLeo3 = new Annuncio.Builder()
            .id(13)
            .autore("leonisi007")
            .titolo("Analisi dati con Excel e Python")
            .categoria("Data Science")
            .skillOfferta("Excel avanzato, Pandas e grafici")
            .controprestazioneCercata("Ripetizioni di Matematica")
            .disponibilita("Martedì sera")
            .build();

        Annuncio annuncioLeo4 = new Annuncio.Builder()
            .id(14)
            .autore("leonisi007")
            .titolo("Gestione del bilancio familiare")
            .categoria("Economia e Finanza")
            .skillOfferta("Pianificazione spese e risparmio")
            .controprestazioneCercata("Fotografia")
            .disponibilita("Venerdì pomeriggio")
            .build();

        Annuncio annuncioLeo5 = new Annuncio.Builder()
            .id(15)
            .autore("leonisi007")
            .titolo("Consigli per la cura di cani e gatti")
            .categoria("Cura degli Animali")
            .skillOfferta("Alimentazione, educazione e benessere")
            .controprestazioneCercata("Sviluppo Software")
            .disponibilita("Domenica mattina")
            .build();

        leonisi007.getAnnunciPubblicati().add(annuncioLeo1.getId());
        leonisi007.getAnnunciPubblicati().add(annuncioLeo2.getId());
        leonisi007.getAnnunciPubblicati().add(annuncioLeo3.getId());
        leonisi007.getAnnunciPubblicati().add(annuncioLeo4.getId());
        leonisi007.getAnnunciPubblicati().add(annuncioLeo5.getId());

        Annuncio annuncioMatty1 = new Annuncio.Builder()
            .id(16)
            .autore("mastroky")
            .titolo("Traduzioni Italiano ↔ Inglese per studenti")
            .categoria("Traduzioni")
            .skillOfferta("Traduzione di testi accademici, CV e documentazione tecnica")
            .controprestazioneCercata("Sviluppo Software")
            .disponibilita("Lunedì pomeriggio")
            .build();

        Annuncio annuncioMatty2 = new Annuncio.Builder()
            .id(17)
            .autore("mastroky")
            .titolo("Ripetizioni di Matematica per scuole superiori")
            .categoria("Ripetizioni di Matematica")
            .skillOfferta("Algebra, geometria analitica e funzioni")
            .controprestazioneCercata("Economia e Finanza")
            .disponibilita("Martedì e Giovedì sera")
            .build();

        Annuncio annuncioMatty3 = new Annuncio.Builder()
            .id(18)
            .autore("mastroky")
            .titolo("Strategie di Social Media Marketing")
            .categoria("Social Media Marketing")
            .skillOfferta("Creazione contenuti, piano editoriale e analisi delle performance")
            .controprestazioneCercata("Canto e Strumenti")
            .disponibilita("Mercoledì pomeriggio")
            .build();

        Annuncio annuncioMatty4 = new Annuncio.Builder()
            .id(19)
            .autore("mastroky")
            .titolo("Workshop di Scrittura Creativa")
            .categoria("Scrittura Creativa")
            .skillOfferta("Storytelling, costruzione dei personaggi e tecniche narrative")
            .controprestazioneCercata("Giardinaggio")
            .disponibilita("Venerdì sera")
            .build();

        Annuncio annuncioMatty5 = new Annuncio.Builder()
            .id(20)
            .autore("mastroky")
            .titolo("Fotografia da viaggio e paesaggio")
            .categoria("Fotografia")
            .skillOfferta("Composizione, luce naturale e post-produzione con Lightroom")
            .controprestazioneCercata("Data Science")
            .disponibilita("Sabato mattina")
            .build();
        
        mastroky.getAnnunciPubblicati().add(annuncioMatty1.getId());
        mastroky.getAnnunciPubblicati().add(annuncioMatty2.getId());
        mastroky.getAnnunciPubblicati().add(annuncioMatty3.getId());
        mastroky.getAnnunciPubblicati().add(annuncioMatty4.getId());
        mastroky.getAnnunciPubblicati().add(annuncioMatty5.getId());

        dbAnnunci.put(annuncioFilker1.getId(), annuncioFilker1);
        dbAnnunci.put(annuncioFilker2.getId(), annuncioFilker2);
        dbAnnunci.put(annuncioFilker3.getId(), annuncioFilker3);
        dbAnnunci.put(annuncioFilker4.getId(), annuncioFilker4);
        dbAnnunci.put(annuncioFilker5.getId(), annuncioFilker5);

        dbAnnunci.put(annuncioBrung1.getId(), annuncioBrung1);
        dbAnnunci.put(annuncioBrung2.getId(), annuncioBrung2);
        dbAnnunci.put(annuncioBrung3.getId(), annuncioBrung3);
        dbAnnunci.put(annuncioBrung4.getId(), annuncioBrung4);
        dbAnnunci.put(annuncioBrung5.getId(), annuncioBrung5);

        dbAnnunci.put(annuncioLeo1.getId(), annuncioLeo1);
        dbAnnunci.put(annuncioLeo2.getId(), annuncioLeo2);
        dbAnnunci.put(annuncioLeo3.getId(), annuncioLeo3);
        dbAnnunci.put(annuncioLeo4.getId(), annuncioLeo4);
        dbAnnunci.put(annuncioLeo5.getId(), annuncioLeo5);

        dbAnnunci.put(annuncioMatty1.getId(), annuncioMatty1);
        dbAnnunci.put(annuncioMatty2.getId(), annuncioMatty2);
        dbAnnunci.put(annuncioMatty3.getId(), annuncioMatty3);
        dbAnnunci.put(annuncioMatty4.getId(), annuncioMatty4);
        dbAnnunci.put(annuncioMatty5.getId(), annuncioMatty5);

        // creazione Richieste
        RichiestaScambio richiesta1 = new RichiestaScambio(1, 6, "filker67", "brung05");
        richiesta1.setStato(RichiestaScambio.StatoRichiesta.IN_ATTESA);
        filker67.getRichiesteInviateId().add(richiesta1.getId());
        brung05.getRichiesteRicevuteId().add(richiesta1.getId());

        RichiestaScambio richiesta2 = new RichiestaScambio(2, 13, "filker67", "leonisi007");
        richiesta2.setStato(RichiestaScambio.StatoRichiesta.ACCETTATO);
        filker67.getRichiesteInviateId().add(richiesta2.getId());
        leonisi007.getRichiesteRicevuteId().add(richiesta2.getId());

        RichiestaScambio richiesta3 = new RichiestaScambio(3, 18, "filker67", "mastroky");
        richiesta3.setStato(RichiestaScambio.StatoRichiesta.CONCLUSO);
        filker67.getRichiesteInviateId().add(richiesta3.getId());
        mastroky.getRichiesteRicevuteId().add(richiesta3.getId());

        RichiestaScambio richiesta4 = new RichiestaScambio(4, 1, "brung05", "filker67");
        richiesta4.setStato(RichiestaScambio.StatoRichiesta.ACCETTATO);
        brung05.getRichiesteInviateId().add(richiesta4.getId());
        filker67.getRichiesteRicevuteId().add(richiesta4.getId());

        RichiestaScambio richiesta5 = new RichiestaScambio(5, 15, "brung05", "leonisi007");
        richiesta5.setStato(RichiestaScambio.StatoRichiesta.IN_ATTESA);
        brung05.getRichiesteInviateId().add(richiesta5.getId());
        leonisi007.getRichiesteRicevuteId().add(richiesta5.getId());

        RichiestaScambio richiesta6 = new RichiestaScambio(6, 20, "brung05", "mastroky");
        richiesta6.setStato(RichiestaScambio.StatoRichiesta.RIFIUTATO);
        brung05.getRichiesteInviateId().add(richiesta6.getId());
        mastroky.getRichiesteRicevuteId().add(richiesta6.getId());

        RichiestaScambio richiesta7 = new RichiestaScambio(7, 4, "leonisi007", "filker67");
        richiesta7.setStato(RichiestaScambio.StatoRichiesta.CONCLUSO);
        leonisi007.getRichiesteInviateId().add(richiesta7.getId());
        filker67.getRichiesteRicevuteId().add(richiesta7.getId());

        RichiestaScambio richiesta8 = new RichiestaScambio(8, 7, "leonisi007", "brung05");
        richiesta8.setStato(RichiestaScambio.StatoRichiesta.ACCETTATO);
        leonisi007.getRichiesteInviateId().add(richiesta8.getId());
        brung05.getRichiesteRicevuteId().add(richiesta8.getId());

        RichiestaScambio richiesta9 = new RichiestaScambio(9, 17, "leonisi007", "mastroky");
        richiesta9.setStato(RichiestaScambio.StatoRichiesta.IN_ATTESA);
        leonisi007.getRichiesteInviateId().add(richiesta9.getId());
        mastroky.getRichiesteRicevuteId().add(richiesta9.getId());

        RichiestaScambio richiesta10 = new RichiestaScambio(10, 3, "mastroky", "filker67");
        richiesta10.setStato(RichiestaScambio.StatoRichiesta.RIFIUTATO);
        mastroky.getRichiesteInviateId().add(richiesta10.getId());
        filker67.getRichiesteRicevuteId().add(richiesta10.getId());

        RichiestaScambio richiesta11 = new RichiestaScambio(11, 8, "mastroky", "brung05");
        richiesta11.setStato(RichiestaScambio.StatoRichiesta.CONCLUSO);
        mastroky.getRichiesteInviateId().add(richiesta11.getId());
        brung05.getRichiesteRicevuteId().add(richiesta11.getId());

        RichiestaScambio richiesta12 = new RichiestaScambio(12, 12, "mastroky", "leonisi007");
        richiesta12.setStato(RichiestaScambio.StatoRichiesta.ACCETTATO);
        mastroky.getRichiesteInviateId().add(richiesta12.getId());
        leonisi007.getRichiesteRicevuteId().add(richiesta12.getId());

        dbRichieste.put(richiesta1.getId(), richiesta1);
        dbRichieste.put(richiesta2.getId(), richiesta2);
        dbRichieste.put(richiesta3.getId(), richiesta3);

        dbRichieste.put(richiesta4.getId(), richiesta4);
        dbRichieste.put(richiesta5.getId(), richiesta5);
        dbRichieste.put(richiesta6.getId(), richiesta6);

        dbRichieste.put(richiesta7.getId(), richiesta7);
        dbRichieste.put(richiesta8.getId(), richiesta8);
        dbRichieste.put(richiesta9.getId(), richiesta9);

        dbRichieste.put(richiesta10.getId(), richiesta10);
        dbRichieste.put(richiesta11.getId(), richiesta11);
        dbRichieste.put(richiesta12.getId(), richiesta12);

        dbUtenti.put(filker67.getUsername(), filker67);
        dbUtenti.put(brung05.getUsername(), brung05);
        dbUtenti.put(leonisi007.getUsername(), leonisi007);
        dbUtenti.put(mastroky.getUsername(), mastroky);
        dbUtenti.put(admin.getUsername(), admin);
    }
}