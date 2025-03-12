package Tests.Service;

import Entity.Masina;
import Repo.RepositoryImpl;
import Service.ServiciuMasina;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ServiciuMasinaTest {

    @Test
    void testAdaugaMasina() throws Exception {
        RepositoryImpl<Masina> repo = new RepositoryImpl<>();
        ServiciuMasina serviciu = new ServiciuMasina(repo);

        Masina masina = new Masina(1, "Dacia", "Logan");
        serviciu.adaugaMasina(masina);

        assertEquals(1, repo.findAll().size());
        assertEquals("Dacia", repo.findById(1).get().getMarca());
    }

    @Test
    void testModificaMasina() throws Exception {
        RepositoryImpl<Masina> repo = new RepositoryImpl<>();
        ServiciuMasina serviciu = new ServiciuMasina(repo);

        Masina masina = new Masina(1, "Dacia", "Logan");
        serviciu.adaugaMasina(masina);

        Masina masinaNoua = new Masina(1, "Renault", "Logan");
        serviciu.modificaMasina(masinaNoua);

        assertEquals("Renault", repo.findById(1).get().getMarca());
    }

    @Test
    void testStergeMasina() throws Exception {
        RepositoryImpl<Masina> repo = new RepositoryImpl<>();
        ServiciuMasina serviciu = new ServiciuMasina(repo);

        Masina masina = new Masina(1, "Dacia", "Logan");
        serviciu.adaugaMasina(masina);
        serviciu.stergeMasina(1);

        assertTrue(repo.findAll().isEmpty());
    }
}
