package be.kuleuven.dbproject.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Bijdrage {

    @Id
    @GeneratedValue
    private int bijdrageID;

    @Column(nullable = false)
    private double bedrag;

    @Column(nullable = false)
    private BetaalMethode betaalMethode;



    @ManyToMany(mappedBy = "bijdragesTijdensBezoek", fetch = FetchType.LAZY)
    private List<Bezoek> bezoeken;

    public Bijdrage(){

    }
    public Bijdrage(double bedrag, BetaalMethode betaalMethode) {
        this.bedrag = bedrag;
        this.betaalMethode = betaalMethode;
        this.bezoeken =new ArrayList<>();

    }

    public int getBijdrageID() {
        return bijdrageID;
    }

    public double getBedrag() {
        return bedrag;
    }

    public BetaalMethode getBetaalMethode() {
        return betaalMethode;
    }

    public List<Bezoek> getBezoeken() {
        return bezoeken;
    }
}
