package Service;

import Entity.*;
import Repo.Repository;
import java.util.List;
import java.util.Optional;

public class ServiciuMasina {
    private Repository<Masina> masinaRepository;

    public ServiciuMasina(Repository<Masina> masinaRepository) {
        this.masinaRepository = masinaRepository;
    }

    public void adaugaMasina(Masina masina) throws Exception {
        masinaRepository.add(masina);
    }

    public List<Masina> toateMasinile() {
        return masinaRepository.findAll();
    }

    public Optional<Masina> masinaDupaID(int id) {return masinaRepository.findById(id);}

    public void modificaMasina(Masina masina) throws Exception {
        masinaRepository.update(masina);
    }

    public void stergeMasina(int id) throws Exception {
        masinaRepository.delete(id);
    }
}