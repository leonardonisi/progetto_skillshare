package it.unibo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Utente implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    
    private String bio;
    private List<String> competenzePreferite;
    private String locazione;
    private String fotoProfiloBase64;

    private List<Integer> annunciPubblicati;

    private List<Integer> richiesteInviateId;  
    private List<Integer> richiesteRicevuteId;

    private int contatoreAnnunciInseriti;
    private int contatoreRichiesteInviate;
    private HashMap<String, Integer> scambiConclusiPerCategoria;
    private List<String> badgeOttenuti;

    public Utente(String username, String password) {
        this.username = username;
        this.password = password;
        
        this.competenzePreferite = new ArrayList<>();
        this.annunciPubblicati = new ArrayList<>();
        this.richiesteInviateId = new ArrayList<>();
        this.richiesteRicevuteId = new ArrayList<>();

        this.fotoProfiloBase64 = "images/utente.jpg";

        this.contatoreAnnunciInseriti = 0;
        this.contatoreRichiesteInviate = 0;
        this.scambiConclusiPerCategoria = new HashMap<>();
        this.badgeOttenuti = new ArrayList<>();
    }

    public Utente() {
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public List<String> getCompetenzePreferite() { return competenzePreferite; }
    public void setCompetenzePreferite(List<String> competenzePreferite) { this.competenzePreferite = competenzePreferite; }

    public String getLocazione() { return locazione; }
    public void setLocazione(String locazione) { this.locazione = locazione; }

    public String getFotoProfiloBase64() { return fotoProfiloBase64; }
    public void setFotoProfiloBase64(String fotoProfiloBase64) { this.fotoProfiloBase64 = fotoProfiloBase64; }

    public List<Integer> getAnnunciPubblicati() { return annunciPubblicati; }

    public List<Integer> getRichiesteInviateId() { return richiesteInviateId; }

    public List<Integer> getRichiesteRicevuteId() { return richiesteRicevuteId; }

    public int getContatoreAnnunciInseriti() { return contatoreAnnunciInseriti; }
    public void setContatoreAnnunciInseriti(int contatoreAnnunciInseriti) { this.contatoreAnnunciInseriti = contatoreAnnunciInseriti; }
    public void incrementaAnnunciInseriti() { this.contatoreAnnunciInseriti++; }

    public int getContatoreRichiesteInviate() { return contatoreRichiesteInviate; }
    public void setContatoreRichiesteInviate(int contatoreRichiesteInviate) { this.contatoreRichiesteInviate = contatoreRichiesteInviate; }
    public void incrementaRichiesteInviate() { this.contatoreRichiesteInviate++; }

    public HashMap<String, Integer> getScambiConclusiPerCategoria() { return scambiConclusiPerCategoria; }
    public void setScambiConclusiPerCategoria(HashMap<String, Integer> scambiConclusiPerCategoria) { this.scambiConclusiPerCategoria = scambiConclusiPerCategoria; }
    
    // Incrementa la specifica categoria e la inizializza a 1 se non esisteva ancora
    public void incrementaScambiCategoria(String categoria) {
        if (categoria == null) return;
        int attuale = this.scambiConclusiPerCategoria.getOrDefault(categoria, 0);
        this.scambiConclusiPerCategoria.put(categoria, attuale + 1);
    }

    public List<String> getBadgeOttenuti() { return badgeOttenuti; }
    public void setBadgeOttenuti(List<String> badgeOttenuti) { this.badgeOttenuti = badgeOttenuti; }
    
    // Aggiunge un badge solo se l'utente non lo possiede già
    public void aggiungiBadge(String nomeBadge) {
        if (!this.badgeOttenuti.contains(nomeBadge)) {
            this.badgeOttenuti.add(nomeBadge);
        }
    }
}