package Tests.Repo;

import Entity.Masina;
import Repo.TextFileRepository;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.List;

class TextFileRepositoryTest {

    @Test
    void testAddAndFindAll() throws Exception {
        String fileName = "test_masini.txt";
        File file = new File(fileName);
        file.delete(); // Asigură un fișier curat

        TextFileRepository<Masina> repo = new TextFileRepository<>(fileName);
        Masina masina = new Masina(1, "Dacia", "Logan");
        repo.add(masina);

        List<Masina> masini = repo.findAll();
        assertEquals(1, masini.size());
        assertEquals("Dacia", masini.get(0).getMarca());

        file.delete(); // Curăță fișierul după test
    }
}
