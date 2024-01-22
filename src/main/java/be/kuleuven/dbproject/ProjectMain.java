package be.kuleuven.dbproject;

import be.kuleuven.dbproject.model.*;
import be.kuleuven.dbproject.repository.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.persistence.Persistence;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;


public class ProjectMain extends Application {

    private static Stage rootStage;

    public static Stage getRootStage() {
        return rootStage;
    }

    @Override
    public void start(Stage stage) throws Exception {
        rootStage = stage;
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("logIn.fxml"));

        Scene scene = new Scene(root);

        stage.setTitle("Log in");
        stage.setScene(scene);
        stage.show();

        /*
        var sessionFactory = Persistence.createEntityManagerFactory("be.kuleuven.dbproject");
        var entityManager = sessionFactory.createEntityManager();
        BezoekerRepository bezoekerRepository = new BezoekerRepository(entityManager);
        BezoekRepository bezoekRepository = new BezoekRepository(entityManager);
        BijdrageRepository bijdrageRepository = new BijdrageRepository(entityManager);
        BoekRepository boekRepository = new BoekRepository(entityManager);
        GameRepository gameRepository = new GameRepository(entityManager);
        MedewerkerRepository medewerkerRepository = new MedewerkerRepository(entityManager);
        MuseumRepository museumRepository = new MuseumRepository(entityManager);

        Museum museum1 = new Museum("Kolenmuseum André Dumont", "Putsstraat 17, 3500 As");
        Museum museum2 = new Museum("Gallo-Romeins Museum", "Maastrichterstraat 15, 3700 Tongeren");
        Museum museum3 = new Museum("Jenevermuseum", "Vrouwenstraat 106, 3600 Hasselt");

        museumRepository.save(museum1);
        museumRepository.save(museum2);
        museumRepository.save(museum3);

        Medewerker medewerker1 = new Medewerker("Mohammed","mohammed@gmail.com","password",LocalDate.of(2002,11,27), true);
        Medewerker medewerker2 = new Medewerker("Ahmed","ahmed@gmail.com","wachtwoord",LocalDate.of(2000,10,5), false);
        Medewerker medewerker3 = new Medewerker("Jan","jan@gmail.com","jan123",LocalDate.of(2001,5,2), false);

        museum1.voegMedewerkerToe(medewerker1);
        museum2.voegMedewerkerToe(medewerker2);
        museum3.voegMedewerkerToe(medewerker3);

        medewerkerRepository.save(medewerker1);
        medewerkerRepository.save(medewerker2);
        medewerkerRepository.save(medewerker3);



        Boek boek1 = new Boek("Het leven", "Wouter",1999,"UHasselt",museum1,13);
        Boek boek2 = new Boek("De liefde", "Jan",2009,"Bibliotheek van Tongeren",museum2,45);
        Boek boek3 = new Boek("RED ZUUR DESEM", "Wouter Groeneveld",2020,"Brave New Books",museum1,28.50);

        boekRepository.save(boek1);
        boekRepository.save(boek2);
        boekRepository.save(boek3);

        Game game1 = new Game(museum1,"Ensemble Studios", 1999, 24.99, "Age Of Empires II", Console.PC);
        Game game2 = new Game(museum2,"Blizzard North", 2000, 20.00, "Diablo II", Console.PC);
        Game game3 = new Game(museum3,"Chris Sawyer", 1999, 14.99, "Roller Coaster Tycoon", Console.PC);
        Game game4 = new Game(museum3,"Quake", 1999, 10.00, "Quake", Console.PC);

        gameRepository.save(game1);
        gameRepository.save(game2);
        gameRepository.save(game3);
        gameRepository.save(game4);

        Bezoeker bezoeker1 = new Bezoeker("Kris Aerts", "Dorpstraat 6, Bree", "kris@kul.be", LocalDate.of(1999, Month.JULY, 7));
        Bezoeker bezoeker2 = new Bezoeker("Linus Torvalds", "Hoekstraat 6, Bree", "linus@linux.be", LocalDate.of(1966, Month.APRIL, 13));
        Bezoeker bezoeker3 = new Bezoeker("Jan De Mosselman", "Dijkstraat 6, Scheveningen", "Jan@mosselen.be", LocalDate.of(1944, Month.APRIL, 8));

        bezoekerRepository.save(bezoeker1);
        bezoekerRepository.save(bezoeker2);
        bezoekerRepository.save(bezoeker3);

        Bijdrage bijdrage1 = new Bijdrage(15.9,BetaalMethode.CASH);
        Bijdrage bijdrage2 = new Bijdrage(2.5, BetaalMethode.APPLE_PAY);
        Bijdrage bijdrage3 = new Bijdrage(7.99, BetaalMethode.DEBIT_CARD);

        bijdrageRepository.save(bijdrage1);
        bijdrageRepository.save(bijdrage2);
        bijdrageRepository.save(bijdrage3);

        Bezoek bezoek1 = new Bezoek(bezoeker1,museum1,LocalDate.now(),List.of(boek1),List.of(game1),List.of(bijdrage1));
        Bezoek bezoek2 = new Bezoek(bezoeker2,museum2,LocalDate.now(),List.of(boek2),List.of(game2),List.of(bijdrage2));
        Bezoek bezoek3 = new Bezoek(bezoeker3,museum3,LocalDate.now(),List.of(boek3),List.of(game3),List.of(bijdrage3));

        bezoekRepository.save(bezoek1);
        bezoekRepository.save(bezoek2);
        bezoekRepository.save(bezoek3);

        /*
        bezoekRepository.update(bezoek1);
        bezoekRepository.update(bezoek2);
        bezoekRepository.update(bezoek3);


        museumRepository.update(museum1);
        museumRepository.update(museum2);
        museumRepository.update(museum3);

        medewerkerRepository.update(medewerker1);
        medewerkerRepository.update(medewerker2);
        medewerkerRepository.update(medewerker3);

        boekRepository.update(boek1);
        boekRepository.update(boek2);
        boekRepository.update(boek3);

        gameRepository.update(game1);
        gameRepository.update(game2);
        gameRepository.update(game3);
        gameRepository.update(game4);

        bezoekerRepository.update(bezoeker1);
        bezoekerRepository.update(bezoeker2);
        bezoekerRepository.update(bezoeker3);

        bijdrageRepository.update(bijdrage1);
        bijdrageRepository.update(bijdrage2);
        bijdrageRepository.update(bijdrage3);

        bezoekRepository.update(bezoek1);
        bezoekRepository.update(bezoek2);
        bezoekRepository.update(bezoek3);
        */


    }

    public static void main(String[] args){
        launch();
    }
}
