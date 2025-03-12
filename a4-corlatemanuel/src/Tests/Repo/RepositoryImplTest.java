package Tests.Repo;

import Entity.Masina;
import Repo.RepositoryImpl;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

class RepositoryImplTest {

    @Test
    void testAddAndFindById() throws Exception {
        RepositoryImpl<Masina> repo = new RepositoryImpl<>();
        Masina masina = new Masina(1, "Dacia", "Logan");
        repo.add(masina);

        Optional<Masina> found = repo.findById(1);
        assertTrue(found.isPresent());
        assertEquals("Dacia", found.get().getMarca());
    }

    @Test
    void testAddDuplicateThrowsException() {
        RepositoryImpl<Masina> repo = new RepositoryImpl<>();
        Masina masina = new Masina(1, "Dacia", "Logan");

        assertThrows(Exception.class, () -> {
            repo.add(masina);
            repo.add(masina);
        });
    }

    @Test
    void testUpdate() throws Exception {
        RepositoryImpl<Masina> repo = new RepositoryImpl<>();
        Masina masina = new Masina(1, "Dacia", "Logan");
        repo.add(masina);

        Masina updatedMasina = new Masina(1, "Renault", "Logan");
        repo.update(updatedMasina);

        Optional<Masina> found = repo.findById(1);
        assertTrue(found.isPresent());
        assertEquals("Renault", found.get().getMarca());
    }

    @Test
    void testDelete() throws Exception {
        RepositoryImpl<Masina> repo = new RepositoryImpl<>();
        Masina masina = new Masina(1, "Dacia", "Logan");
        repo.add(masina);

        repo.delete(1);
        assertFalse(repo.findById(1).isPresent());
    }
}
