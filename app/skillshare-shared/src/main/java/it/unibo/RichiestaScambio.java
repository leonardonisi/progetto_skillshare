package it.unibo;

import java.io.Serializable;

public class RichiestaScambio implements Serializable {
    private static final long serialVersionUID = 1L;

    // uso un enum per lo stato per evitare errori
    public enum StatoRichiesta {
        IN_ATTESA,
        ACCETTATO,
        RIFIUTATO,
        CONCLUSO
    }

    private Integer id;  
    private Integer idAnnuncio;   
    private String richiedenteUser;   
    private String proprietarioUser;  
    private StatoRichiesta stato;   

    public RichiestaScambio() {
    }

    public RichiestaScambio(Integer id, Integer idAnnuncio, String richiedenteUser, String proprietarioUser) {
        this.id = id;
        this.idAnnuncio = idAnnuncio;
        this.richiedenteUser = richiedenteUser;
        this.proprietarioUser = proprietarioUser;
        this.stato = StatoRichiesta.IN_ATTESA;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getIdAnnuncio() { return idAnnuncio; }
    public void setIdAnnuncio(Integer idAnnuncio) { this.id = idAnnuncio; }

    public String getRichiedenteUser() { return richiedenteUser; }
    public void setRichiedenteUser(String richiedenteUser) { this.richiedenteUser = richiedenteUser; }

    public String getProprietarioUser() { return proprietarioUser; }
    public void setProprietarioUser(String proprietarioUser) { this.proprietarioUser = proprietarioUser; }

    public StatoRichiesta getStato() { return stato; }
    public void setStato(StatoRichiesta stato) { this.stato = stato; }
}