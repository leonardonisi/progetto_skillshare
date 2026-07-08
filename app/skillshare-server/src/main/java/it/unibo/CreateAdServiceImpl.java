package it.unibo;

import com.google.gwt.user.server.rpc.jakarta.RemoteServiceServlet;
import java.util.concurrent.ConcurrentMap;
import java.util.ArrayList;
import java.util.List;

public class CreateAdServiceImpl extends RemoteServiceServlet implements CreateAdService {

    // Rimosse le variabili static final globali per evitare bug nei test!

    @Override
    public boolean pubblicaAnnuncio(Annuncio annuncio) {
        if (annuncio == null || 
            annuncio.getTitolo() == null || annuncio.getTitolo().trim().isEmpty() || 
            annuncio.getSkillOfferta() == null || annuncio.getSkillOfferta().trim().isEmpty() ||
            annuncio.getControprestazione() == null || annuncio.getControprestazione().trim().isEmpty() || 
            annuncio.getDisponibilita() == null || annuncio.getDisponibilita().trim().isEmpty() || 
            annuncio.getAutore() == null || annuncio.getAutore().trim().isEmpty()) {
        return false;
        }
        
        try {
            // Recupero dinamico delle mappe (fondamentale per i Test in RAM)
            ConcurrentMap<Integer, Annuncio> dbAnnunci = DatabaseCore.getMappaAnnunci();
            ConcurrentMap<String, Utente> dbUtenti = DatabaseCore.getMappaUtenti();

            int id = DatabaseCore.generaNuovoIdAnnuncio();
            
            annuncio.setId(id);
            dbAnnunci.put(id, annuncio);
            
            Utente autore = dbUtenti.get(annuncio.getAutore());
            if (autore != null) {
                autore.getAnnunciPubblicati().add(id);
                dbUtenti.put(autore.getUsername(), autore);
            }

            DatabaseCore.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<String> getCategorie() {
        List<String> categorieImmutabili = DatabaseCore.getCategorie();
        return new ArrayList<>(categorieImmutabili);
    }

    @Override
    public boolean aggiornaAnnuncio(int id, Annuncio annuncioAggiornato) {
        if (annuncioAggiornato == null ||
                annuncioAggiornato.getTitolo() == null || annuncioAggiornato.getTitolo().trim().isEmpty() ||
                annuncioAggiornato.getCategoria() == null || annuncioAggiornato.getCategoria().trim().isEmpty() ||
                annuncioAggiornato.getSkillOfferta() == null || annuncioAggiornato.getSkillOfferta().trim().isEmpty() ||
                annuncioAggiornato.getDisponibilita() == null || annuncioAggiornato.getDisponibilita().trim().isEmpty()
                || annuncioAggiornato.getAutore() == null) {
            return false;
        }

        try {
            ConcurrentMap<Integer, Annuncio> dbAnnunci = DatabaseCore.getMappaAnnunci();
            
            if (!dbAnnunci.containsKey(id)) {
                return false;
            }
            
            annuncioAggiornato.setId(id);
            
            dbAnnunci.put(id, annuncioAggiornato);
            DatabaseCore.commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}