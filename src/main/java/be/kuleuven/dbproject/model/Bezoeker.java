package be.kuleuven.dbproject.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Bezoeker {

    @Id
    @GeneratedValue
    private int bezoekerID;
    @Column(nullable = false)
    private String naam;
    @Column(nullable = false)
    private String adres;
    @Column(nullable = false)
    private String emailAdres;
    @Column(nullable = false)
    private LocalDate geboortedatum;

    @OneToMany(mappedBy = "bezoeker", fetch = FetchType.LAZY ,cascade = CascadeType.ALL)
    private List<Bezoek> bezoeken;

    public Bezoeker(){

    }
    public Bezoeker(String naam, String adres, String emailAdres, LocalDate geboortedatum) {
        this.naam = naam;
        this.adres = adres;
        this.emailAdres = emailAdres;
        this.geboortedatum = geboortedatum;
        this.bezoeken = new ArrayList<>();
    }

    public int getBezoekerID() {
        return bezoekerID;
    }

    public String getNaam() {
        return naam;
    }

    public String getAdres() {
        return adres;
    }

    public String getEmailAdres() {
        return emailAdres;
    }

    public LocalDate getGeboortedatum() {
        return geboortedatum;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public void setEmailAdres(String emailAdres) {
        this.emailAdres = emailAdres;
    }

    public List<Bezoek> getBezoeken() {
        return bezoeken;
    }
}
