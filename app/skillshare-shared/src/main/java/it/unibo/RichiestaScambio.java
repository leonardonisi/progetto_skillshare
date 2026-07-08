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

    private Integer id;             // ID univoco della richiesta
    private Integer idAnnuncio;   
    private String richiedenteId;   // Username di chi ha cliccato "Richiedi"
    private String proprietarioId;  // Username di chi ha pubblicato l'annuncio
    private StatoRichiesta stato;   // stato della richiesta

    private boolean confermatoDaProprietario;
    private boolean confermatoDaRichiedente;

    private String messaggioProposta;

    public RichiestaScambio() {
    
    }
    
    public RichiestaScambio(Integer id, Integer idAnnuncio, String richiedenteId, String proprietarioId) {
        this.id = id;
        this.idAnnuncio = idAnnuncio;
        this.richiedenteId = richiedenteId;
        this.proprietarioId = proprietarioId;
        this.stato = StatoRichiesta.IN_ATTESA;
        this.confermatoDaProprietario = false;
        this.confermatoDaRichiedente = false;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getIdAnnuncio() { return idAnnuncio; }
    public void setIdAnnuncio(Integer idAnnuncio) { this.id = idAnnuncio; }

    public String getRichiedenteId() { return richiedenteId; }
    public void setRichiedenteId(String richiedenteId) { this.richiedenteId = richiedenteId; }

    public String getProprietarioId() { return proprietarioId; }
    public void setProprietarioId(String proprietarioId) { this.proprietarioId = proprietarioId; }

    public StatoRichiesta getStato() { return stato; }
    public void setStato(StatoRichiesta stato) { this.stato = stato; }

    public boolean isConfermatoDaProprietario() { return confermatoDaProprietario; }
    public void setConfermatoDaProprietario(boolean confermatoDaProprietario) { this.confermatoDaProprietario = confermatoDaProprietario; }

    public boolean isConfermatoDaRichiedente() { return confermatoDaRichiedente; }
    public void setConfermatoDaRichiedente(boolean confermatoDaRichiedente) { this.confermatoDaRichiedente = confermatoDaRichiedente; }

    public String getMessaggioProposta() { return messaggioProposta; }
    public void setMessaggioProposta(String messaggioProposta) { this.messaggioProposta = messaggioProposta; }
}