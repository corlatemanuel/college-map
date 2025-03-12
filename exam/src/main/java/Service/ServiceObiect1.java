package Service;

import Entity.*;
import Repo.Repository;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ServiceObiect1 {
    private Repository<Obiect1> entRepository;

    public ServiceObiect1(Repository<Obiect1> entRepository) {
        this.entRepository = entRepository;
    }

    public void adaugaEntitate(Obiect1 ent) throws Exception {
        if (!isDurataValida(ent.getDurata())) {
            throw new Exception("Durata de format invalid");
        }
        entRepository.add(ent);
    }

    private boolean isDurataValida(String durata) {
        if (durata == null || !durata.contains(":")) {
            return false;
        }

        String[] parti = durata.split(":");
        if (parti.length != 2) {
            return false;
        }

        try {
            int minute = Integer.parseInt(parti[0]);
            int secunde = Integer.parseInt(parti[1]);

            return minute >= 0 && secunde >= 0 && secunde < 60;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    public List<Obiect1> toateEntitatile() {
        return entRepository.findAll();
    }

    public Optional<Obiect1> entDupaID(int id) {return entRepository.findById(id);}

    public void modificaEntitatea(Obiect1 ent) throws Exception {
        entRepository.update(ent);
    }

    public void stergeEntitatea(int id) throws Exception {
        entRepository.delete(id);
    }

    public @NotNull List<Obiect1> filtreazaDupa(String filtreaza){
        return entRepository.findAll().stream()
                .filter(entitate -> entitate.toString() != null &&
                        entitate.toString().toLowerCase().contains(filtreaza.toLowerCase()))
                .collect(Collectors.toList());
    }
}