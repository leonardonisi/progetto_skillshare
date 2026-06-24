package it.unibo;

import java.io.Serializable;

public class Annuncio implements Serializable {
    private static final long serialVersionUID = 1L;

    private String titolo;
    private String categoria;
    private String skillOfferta;
    private String controprestazioneCercata;
    private String disponibilita;
    private String utenteId;

    public Annuncio() {
    }

    public Annuncio(String titolo, String categoria, String skillOfferta, String controprestazioneCercata,
            String disponibilita, String utenteId) {
        this.titolo = titolo;
        this.categoria = categoria;
        this.skillOfferta = skillOfferta;
        this.controprestazioneCercata = controprestazioneCercata;
        this.disponibilita = disponibilita;
        this.utenteId = utenteId;
    }

    // Getter e Setter
    public String getTitolo() {
        return titolo;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getSkillOfferta() {
        return skillOfferta;
    }

    public String getControprestazioneCercata() {
        return controprestazioneCercata;
    }

    public String getDisponibilita() {
        return disponibilita;
    }

    public String getUtenteId() {
        return utenteId;
    }
}