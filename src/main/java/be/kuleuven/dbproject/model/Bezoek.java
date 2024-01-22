package be.kuleuven.dbproject.model;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Bezoek {

    @Id
    @GeneratedValue
    private int bezoekID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bezoekerID")
    private Bezoeker bezoeker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "museumID")
    private Museum museum;

    @Column(nullable = false)
    private LocalDate datum;

    @ManyToMany(fetch = FetchType.LAZY , cascade = CascadeType.ALL)
    @JoinTable(
            name = "geleendeBoeken",
            joinColumns = @JoinColumn(name = "bezoekID"),
            inverseJoinColumns = @JoinColumn(name = "boekID")
    )
    private List<Boek> geleendeBoeken;
    @ManyToMany(fetch = FetchType.LAZY , cascade = CascadeType.ALL)
    @JoinTable(
            name = "geleendeGames",
            joinColumns = @JoinColumn(name = "bezoekID"),
            inverseJoinColumns = @JoinColumn(name = "gameID")
    )
    private List<Game> geleendeGames;
    @ManyToMany(fetch = FetchType.LAZY , cascade = CascadeType.ALL)
    @JoinTable(
            name = "BijdragesTijdensBezoek",
            joinColumns = @JoinColumn(name = "bezoekID"),
            inverseJoinColumns = @JoinColumn(name = "bijdrageID")
    )
    private List<Bijdrage> bijdragesTijdensBezoek;

    public Bezoek(){

    }
    public Bezoek(Bezoeker bezoeker, Museum museum, LocalDate datum, List<Boek> geleendeBoeken, List<Game> geleendeGames, List<Bijdrage> bijdragesTijdensBezoek) {
        this.geleendeBoeken =new ArrayList<>();
        this.geleendeGames = new ArrayList<>();
        this.bijdragesTijdensBezoek = new ArrayList<>();
        kenBezoekerToe(bezoeker);
        kenMuseumToe(museum);
        this.datum = datum;
        kenBoekenToe(geleendeBoeken);
        kenGamesToe(geleendeGames);
        kenBijdragesToe(bijdragesTijdensBezoek);
    }


    public int getBezoekID() {
        return bezoekID;
    }

    public Bezoeker getBezoeker() {
        return bezoeker;
    }

    public Museum getMuseum() {
        return museum;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public List<Boek> getGeleendeBoeken() {
        return geleendeBoeken;
    }

    public List<Game> getGeleendeGames() {
        return geleendeGames;
    }

    public List<Bijdrage> getBijdragesTijdensBezoek() {
        return bijdragesTijdensBezoek;
    }

    public void setMuseum(Museum museum) {
        this.museum = museum;
    }

    public float getTotaleBedrag(){
        float totaalBedrag = 0.0f;

        for (Bijdrage bijdrage : bijdragesTijdensBezoek) {
            totaalBedrag += bijdrage.getBedrag();
        }

        return totaalBedrag;
    }

    public void kenBezoekerToe(Bezoeker bezoeker){
        bezoeker.getBezoeken().add(this);
        this.bezoeker = bezoeker;
    }
    public void kenMuseumToe(Museum museum){
        museum.getBezoeken().add(this);
        this.museum = museum;
    }

    public void kenBoekenToe(List<Boek> boeken){
        for(Boek boek: boeken) {
            boek.getBezoeken().add(this);
            boek.setUitgeleend(true);
            geleendeBoeken.add(boek);
        }
    }
    public void kenGamesToe(List<Game> games){
        for(Game game: games) {
            game.getBezoeken().add(this);
            game.setUitgeleend(true);
            geleendeGames.add(game);
        }
    }
    public void kenBijdragesToe(List<Bijdrage> bijdrages){
        for(Bijdrage bijdrage: bijdrages) {
            bijdrage.getBezoeken().add(this);
            bijdragesTijdensBezoek.add(bijdrage);
        }
    }



}
