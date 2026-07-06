package it.unibo;

import java.io.Serializable;

public class Messaggio implements Serializable {
    private static final long serialVersionUID = 1L;

    private String mittente;
    private String destinatario;
    private String testo;

    public Messaggio() {
    }

    public Messaggio(String mittente, String destinatario, String testo) {
        this.mittente = mittente;
        this.destinatario = destinatario;
        this.testo = testo;
    }

    public String getMittente() {
        return mittente;
    }

    public void setMittente(String mittente) {
        this.mittente = mittente;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }
}