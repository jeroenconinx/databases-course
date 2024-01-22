package be.kuleuven.dbproject.model;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity

public class Museum {

    @Id
    @GeneratedValue
    private int museumID;

    @Column(nullable = false)
    private String naam;
    @Column(nullable = false)
    private String adres;

    @ManyToMany(fetch = FetchType.LAZY , cascade = CascadeType.ALL)
    @JoinTable(
            name = "museum_medewerker",
            joinColumns = @JoinColumn(name = "museumID"),
            inverseJoinColumns = @JoinColumn(name = "medewerkerID")
    )
    private List<Medewerker> medewerkers;

    @OneToMany(mappedBy = "museum", cascade = CascadeType.ALL)
    private List<Boek> boeken;
    @OneToMany( mappedBy = "museum", cascade = CascadeType.ALL)
    private List<Game> games;
    @OneToMany( mappedBy = "museum", cascade = CascadeType.ALL)
    private List<Bezoek> bezoeken;

    public Museum(){

    }
    public Museum(String naam, String adres, List<Medewerker> medewerkers, List<Boek> boeken, List<Game> games,List<Bezoek> bezoeken) {
        this.naam = naam;
        this.adres = adres;
        this.medewerkers = medewerkers;
        this.boeken = boeken;
        this.games = games;
        this.bezoeken = bezoeken;
    }
    public Museum(String naam, String adres) {
        this.naam = naam;
        this.adres = adres;
        this.medewerkers = new ArrayList<>();
        this.boeken = new ArrayList<>();
        this.games = new ArrayList<>();
        this.bezoeken = new ArrayList<>();
    }

    public int getMuseumID() {
        return museumID;
    }

    public String getNaam() {
        return naam;
    }

    public String getAdres() {
        return adres;
    }

    public List<Medewerker> getMedewerkers() {
        return medewerkers;
    }

    public List<Boek> getBoeken() {
        return boeken;
    }

    public List<Game> getGames() {
        return games;
    }

    public List<Bezoek> getBezoeken() {
        return bezoeken;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public void setMedewerkers(List<Medewerker> medewerkers) {
        this.medewerkers = medewerkers;
    }

    public void setBoeken(List<Boek> boeken) {
        this.boeken = boeken;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

    public void setBezoeken(List<Bezoek> bezoeken) {
        this.bezoeken = bezoeken;
    }

    public void voegBoekToe(Boek boek){
        boeken.add(boek);
        boek.setMuseum(this);
    }
    public void voegGameToe(Game game){
        games.add(game);
        game.setMuseum(this);
    }
    public void voegMedewerkerToe(Medewerker medewerker){
        medewerkers.add(medewerker);
        medewerker.getMusea().add(this);
    }
    public void voegBezoekToe(Bezoek bezoek){
        bezoeken.add(bezoek);
        bezoek.setMuseum(this);
    }
    public float getTotaleOpbrengst() {
        float totaleOpbrengst = 0.0f;

        for (Bezoek bezoek : bezoeken) {
            totaleOpbrengst += bezoek.getTotaleBedrag();
        }

        return totaleOpbrengst;
    }


}
