package it.unibo;

import java.io.Serializable;
import java.util.ArrayList;
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

    public Utente(String username, String password) {
        this.username = username;
        this.password = password;
        
        this.competenzePreferite = new ArrayList<>();
        this.annunciPubblicati = new ArrayList<>();
        this.richiesteInviateId = new ArrayList<>();
        this.richiesteRicevuteId = new ArrayList<>();

        this.fotoProfiloBase64 = "images/utente.jpg";
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
}