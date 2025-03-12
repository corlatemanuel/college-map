package Service;

import Entity.*;
import Repo.Repository;
import Exceptions.*;

import java.text.DateFormatSymbols;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

public class ServiciuInchiriere {
    private Repository<Inchiriere> inchiriereRepository;

    public ServiciuInchiriere(Repository<Inchiriere> inchiriereRepository) {
        this.inchiriereRepository = inchiriereRepository;
    }

    public void adaugaInchiriere(Inchiriere inchiriere) throws Exception {
        // Validare suprapunere
        if (inchiriere.getDataInceput().after(inchiriere.getDataSfarsit())) {
            throw new IllegalArgumentException("Data de început trebuie să fie înainte de data de sfârșit.");
        }
        List<Inchiriere> inchirieri = inchiriereRepository.findAll();
        for (Inchiriere i : inchirieri) {
            if (i.getMasina().getId() == inchiriere.getMasina().getId()) {
                if (i.getDataInceput().before(inchiriere.getDataSfarsit()) &&
                        i.getDataSfarsit().after(inchiriere.getDataInceput())) {
                    throw new InchiriereDateException("Suprapunere de închiriere!");
                }
            }
        }

        inchiriereRepository.add(inchiriere);
    }

    public List<Inchiriere> toateInchirierile() {
        return inchiriereRepository.findAll();
    }

    public Optional<Inchiriere> inchiriereDupaID(int id) {return inchiriereRepository.findById(id);}

    public void modificaInchiriere(Inchiriere inchiriereActualizata) throws Exception {
        // Găsește închirierea veche
        Optional<Inchiriere> inchiriereVeche = inchiriereRepository.findById(inchiriereActualizata.getId());
        if (inchiriereVeche == null) {
            throw new NullObjectException("Închirierea specificată nu există!");
        }

        // Validare suprapunere
        List<Inchiriere> inchirieri = inchiriereRepository.findAll();
        for (Inchiriere i : inchirieri) {
            if (i.getMasina().getId() == inchiriereActualizata.getMasina().getId() &&
                    i.getId() != inchiriereActualizata.getId()) {
                if (i.getDataInceput().before(inchiriereActualizata.getDataSfarsit()) &&
                        i.getDataSfarsit().after(inchiriereActualizata.getDataInceput())) {
                    throw new InchiriereDateException("Suprapunere de închiriere!");
                }
            }
        }

        // Actualizează închirierea în repository
        inchiriereRepository.update(inchiriereActualizata);
    }

    public void stergeInchiriere(int id) throws Exception {
        inchiriereRepository.delete(id);
    }

    //Rapoarte Java 8

    public Map<Masina, Integer> celeMaiDesInchiriateMasini() {
        Map<Integer, Integer> inchirieriPeMasina = new HashMap<>();
        Map<Integer, Masina> masinaMap = new HashMap<>();

        // Iterăm prin toate închirierile pentru a număra închirierile și a colecta mașinile
        for (Inchiriere inchiriere : toateInchirierile()) {
            Masina masina = inchiriere.getMasina();
            int idMasina = masina.getId();

            // Salvăm referința mașinii pentru ID-ul respectiv
            masinaMap.put(idMasina, masina);

            // Numărăm închirierile pentru fiecare mașină
            inchirieriPeMasina.put(idMasina, inchirieriPeMasina.getOrDefault(idMasina, 0) + 1);
        }

        // Creăm un Map final, folosind obiectele `Masina` din `masinaMap`
        return inchirieriPeMasina.entrySet().stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                .collect(Collectors.toMap(
                        entry -> masinaMap.get(entry.getKey()), // Convertim ID-ul în obiect `Masina`
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new // Pentru a păstra ordinea sortării
                ));
    }

    public Map<String, Long> numarInchirieriPeLuna() {
        return inchiriereRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        inchiriere -> {
                            Date dataInceput = inchiriere.getDataInceput();
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(dataInceput);
                            int luna = calendar.get(Calendar.MONTH); // Returnează luna ca un index (0 = Ianuarie)
                            return new DateFormatSymbols().getMonths()[luna]; // Obține numele lunii
                        },
                        Collectors.counting()
                ))
                .entrySet()
                .stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue())) // Sortare descrescătoare
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new // Pentru a păstra ordinea
                ));
    }


    public Map<Masina, Long> masiniInchiriateCelMaiMultTimp() {
        Map<Integer, Long> durataInchirierePeMasina = new HashMap<>();
        Map<Integer, Masina> masinaMap = new HashMap<>();

        // Iterăm prin toate închirierile
        for (Inchiriere inchiriere : toateInchirierile()) {
            Masina masina = inchiriere.getMasina();
            int idMasina = masina.getId();

            // Salvăm referința mașinii pentru ID-ul respectiv
            masinaMap.put(idMasina, masina);

            // Calculăm durata închirierii
            long durataInchiriere = inchiriere.getDataSfarsit().getTime() - inchiriere.getDataInceput().getTime();
            durataInchiriere = durataInchiriere / (1000 * 60 * 60 * 24); // Convertim în zile

            // Adăugăm durata închirierii la totalul pentru această mașină
            durataInchirierePeMasina.put(idMasina, durataInchirierePeMasina.getOrDefault(idMasina, 0L) + durataInchiriere);
        }

        // Creăm un Map final, folosind obiectele `Masina` din `masinaMap`
        return durataInchirierePeMasina.entrySet().stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                .collect(Collectors.toMap(
                        entry -> masinaMap.get(entry.getKey()), // Convertim ID-ul în obiect `Masina`
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new // Pentru a păstra ordinea sortării
                ));
    }

}