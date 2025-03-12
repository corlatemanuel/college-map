package UI;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import Service.*;
import Entity.*;

public class UI {
    private final ServiciuMasina serviciuMasina;
    private final ServiciuInchiriere serviciuInchiriere;

    public UI(ServiciuMasina serviciuMasina, ServiciuInchiriere serviciuInchiriere) {
        this.serviciuMasina = serviciuMasina;
        this.serviciuInchiriere = serviciuInchiriere;
    }

    public void start() {

//        try {
//            serviciuMasina.adaugaMasina(new Masina(1, "Dacia", "Logan"));
//            serviciuMasina.adaugaMasina(new Masina(2, "BMW", "X5"));
//            serviciuMasina.adaugaMasina(new Masina(3, "Audi", "A3"));
//            serviciuMasina.adaugaMasina(new Masina(4, "Mercedes", "C-Class"));
//            serviciuMasina.adaugaMasina(new Masina(5, "Ford", "Focus"));
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }

        Scanner scanner = new Scanner(System.in);
        int optiune;

        do {
            System.out.println(" 1. Adauga masina");
            System.out.println(" 2. Modifica masina");
            System.out.println(" 3. Sterge masina");
            System.out.println(" 4. Vezi masini");
            System.out.println(" 5. Adauga inchiriere");
            System.out.println(" 6. Modifica inchiriere");
            System.out.println(" 7. Sterge inchiriere");
            System.out.println(" 8. Vezi inchirieri");
            System.out.println(" 9. Cele mai des inchiriate masini");
            System.out.println("10. Numarul de inchirieri pe luna");
            System.out.println("11. Masinile inchiriate cel mai mult timp");
            System.out.println(" 0. Iesi");
            System.out.print("Alege o optiune: ");
            optiune = scanner.nextInt();

            switch (optiune) {
                case 1:
                    adaugaMasina(scanner);
                    break;
                case 2:
                    modificaMasina(scanner);
                    break;
                case 3:
                    stergeMasina(scanner);
                    break;
                case 4:
                    afiseazaMasini();
                    break;
                case 5:
                    adaugaInchiriere(scanner);
                    break;
                case 6:
                    modificaInchiriere(scanner);
                    break;
                case 7:
                    stergeInchiriere(scanner);
                    break;
                case 8:
                    afiseazaInchirieri();
                    break;
                case 9:
                    celeMaiDesInchiriateMasini();
                    break;
                case 10:
                    numarInchirieriPeLuna();
                    break;
                case 11:
                    masiniInchiriateCelMaiMultTimp();
                    break;
                case 0:
                    System.out.println("La revedere!");
                    break;
                default:
                    System.out.println("Optiune invalida!");
                    break;
            }
        } while (optiune != 0);
        scanner.close();
    }

    private void adaugaMasina(Scanner scanner) {
        System.out.print("ID: ");
        int id = scanner.nextInt();
        System.out.print("Marca: ");
        String marca = scanner.next();
        System.out.print("Model: ");
        String model = scanner.next();
        try {
            serviciuMasina.adaugaMasina(new Masina(id, marca, model));
            System.out.println("Masina adaugata!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void modificaMasina(Scanner scanner) {
        System.out.print("ID: ");
        int id = scanner.nextInt();
        System.out.print("Marca noua: ");
        String marca = scanner.next();
        System.out.print("Model nou: ");
        String model = scanner.next();
        try {
            serviciuMasina.modificaMasina(new Masina(id, marca, model));
            System.out.println("Masina modificata!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void stergeMasina(Scanner scanner) {
        System.out.print("ID: ");
        int id = scanner.nextInt();
        try {
            serviciuMasina.stergeMasina(id);
            System.out.println("Masina stearsa!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void afiseazaMasini() {
        List<Masina> masini = serviciuMasina.toateMasinile();
        if (masini.isEmpty()) {
            System.out.println("Nu sunt mașini disponibile.");
        } else {
            System.out.println("Mașini disponibile:");
            for (Masina masina : masini) {
                System.out.println(masina);
            }
        }
    }

    private void adaugaInchiriere(Scanner scanner) {
        System.out.print("ID Inchiriere: ");
        int idInchiriere = scanner.nextInt();
        System.out.print("ID Masina: ");
        int idMasina = scanner.nextInt();
        System.out.print("Data Inceput (dd/MM/yyyy): ");
        String dataInceputString = scanner.next();
        System.out.print("Data Sfarsit (dd/MM/yyyy): ");
        String dataSfarsitString = scanner.next();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date dataInceput = format.parse(dataInceputString);
            Date dataSfarsit = format.parse(dataSfarsitString);

            Masina masinaGasita = serviciuMasina.toateMasinile().stream()
                    .filter(m -> m.getId() == idMasina).findFirst().orElse(null);
            if (masinaGasita != null) {
                Inchiriere inchiriere = new Inchiriere(idInchiriere, masinaGasita, dataInceput, dataSfarsit);
                serviciuInchiriere.adaugaInchiriere(inchiriere);
                System.out.println("Inchiriere adaugata!");
            } else {
                System.out.println("Masina nu a fost găsită!");
            }
        } catch (Exception e) {
            System.out.println("Eroare la procesarea datelor: " + e.getMessage());
        }
    }

    private void modificaInchiriere(Scanner scanner) {
        System.out.print("ID Inchiriere: ");
        int idInchiriere = scanner.nextInt();
        System.out.print("ID Nou Masina: ");
        int idMasina = scanner.nextInt();
        System.out.print("Data Inceput (dd/MM/yyyy): ");
        String dataInceputString = scanner.next();
        System.out.print("Data Sfarsit (dd/MM/yyyy): ");
        String dataSfarsitString = scanner.next();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date dataInceput = format.parse(dataInceputString);
            Date dataSfarsit = format.parse(dataSfarsitString);

            Inchiriere inchiriereExistenta = serviciuInchiriere.toateInchirierile().stream()
                    .filter(i -> i.getId() == idInchiriere).findFirst().orElse(null);

            Masina masinaNoua = serviciuMasina.toateMasinile().stream()
                    .filter(m -> m.getId() == idMasina).findFirst().orElse(null);

            if (inchiriereExistenta != null && masinaNoua != null) {
                Inchiriere inchiriereActualizata = new Inchiriere(idInchiriere, masinaNoua, dataInceput, dataSfarsit);
                serviciuInchiriere.modificaInchiriere(inchiriereActualizata);
                System.out.println("Închiriere modificată!");
            } else {
                System.out.println("Inchirierea sau mașina nu a fost găsită!");
            }
        } catch (Exception e) {
            System.out.println("Eroare la procesarea datelor: " + e.getMessage());
        }
    }

    private void stergeInchiriere(Scanner scanner) {
        System.out.print("ID: ");
        int id = scanner.nextInt();
        try {
            serviciuInchiriere.stergeInchiriere(id);
            System.out.println("Închiriere ștearsă!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void afiseazaInchirieri() {
        List<Inchiriere> inchirieri = serviciuInchiriere.toateInchirierile();
        if (inchirieri.isEmpty()) {
            System.out.println("Nu sunt închirieri active.");
        } else {
            System.out.println("Închirieri active:");
            for (Inchiriere inchiriere : inchirieri) {
                System.out.println(inchiriere);
            }
        }
    }

    private void celeMaiDesInchiriateMasini() {
        serviciuInchiriere.celeMaiDesInchiriateMasini()
                .forEach((masina, numarInchirieri) ->
                        System.out.println("Mașina: " + masina + ", Număr de închirieri: " + numarInchirieri));
    }

    private void numarInchirieriPeLuna() {
        serviciuInchiriere.numarInchirieriPeLuna()
                .forEach((luna, numarInchirieri) ->
                        System.out.println("Luna: " + luna + ", Număr de închirieri: " + numarInchirieri));

    }

    private void masiniInchiriateCelMaiMultTimp() {
        serviciuInchiriere.masiniInchiriateCelMaiMultTimp()
                .forEach((masina, zile) ->
                        System.out.println("Mașina: " + masina + ", Număr total de zile: " + zile));
    }
}
