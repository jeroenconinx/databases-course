package be.kuleuven.dbproject.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Game {
    @Id
    @GeneratedValue
    private int gameID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "museumID")
    private Museum museum;
    @Column(nullable = false)
    private String developer;
    @Column(nullable = false)
    private int publicatiejaar;
    @Column(nullable = false)
    private double waarde;
    private String naam;
    @Column(nullable = false)
    private Console console;

    @Column
    private boolean uitgeleend;

    @ManyToMany(mappedBy = "geleendeGames", fetch = FetchType.LAZY)
    private List<Bezoek> bezoeken;

    public Game(){

    }
    public Game(Museum museum, String developer, int publicatiejaar, double waarde, String naam, Console console, List<Bezoek> bezoeken,boolean uitgeleend) {
        this.museum = museum;
        this.developer = developer;
        this.publicatiejaar = publicatiejaar;
        this.waarde = waarde;
        this.naam = naam;
        this.console = console;
        this.bezoeken = bezoeken;
        this.uitgeleend = uitgeleend;
    }
    public Game(Museum museum, String developer, int publicatiejaar, double waarde, String naam, Console console) {
        this.museum = museum;
        this.developer = developer;
        this.publicatiejaar = publicatiejaar;
        this.waarde = waarde;
        this.naam = naam;
        this.console = console;
        this.bezoeken = new ArrayList<>();
        this.uitgeleend = false;
    }

    public int getGameID() {
        return gameID;
    }

    public Museum getMuseum() {
        return museum;
    }

    public String getDeveloper() {
        return developer;
    }

    public int getPublicatiejaar() {
        return publicatiejaar;
    }

    public double getWaarde() {
        return waarde;
    }

    public String getNaam() {
        return naam;
    }

    public Console getConsole() {
        return console;
    }

    public List<Bezoek> getBezoeken() {
        return bezoeken;
    }

    public boolean isUitgeleend() {
        return uitgeleend;
    }

    public void setMuseum(Museum museum) {
        this.museum = museum;
    }

    public void setWaarde(float waarde) {
        this.waarde = waarde;
    }

    public void setBezoeken(List<Bezoek> bezoeken) {
        this.bezoeken = bezoeken;
    }

    public void setUitgeleend(boolean uitgeleend) {
        this.uitgeleend = uitgeleend;
    }

    private void voegGameToe(Museum museum){
        museum.getGames().add(this);
        this.museum = museum;
    }
}
