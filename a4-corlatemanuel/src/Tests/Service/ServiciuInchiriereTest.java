package Tests.Service;

import Entity.*;
import Repo.RepositoryImpl;
import Service.ServiciuInchiriere;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

class ServiciuInchiriereTest {

    @Test
    void testAdaugaInchiriere() throws Exception {
        RepositoryImpl<Inchiriere> repo = new RepositoryImpl<>();
        ServiciuInchiriere serviciu = new ServiciuInchiriere(repo);

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date dataInceput = format.parse("01/01/2023");
        Date dataSfarsit = format.parse("05/01/2023");
        Masina masina = new Masina(1, "Dacia", "Logan");

        Inchiriere inchiriere = new Inchiriere(1, masina, dataInceput, dataSfarsit);
        serviciu.adaugaInchiriere(inchiriere);

        List<Inchiriere> inchirieri = repo.findAll();
        assertEquals(1, inchirieri.size());
        assertEquals(1, inchirieri.get(0).getId());
    }

    @Test
    void testModificaInchiriere() throws Exception {
        RepositoryImpl<Inchiriere> repo = new RepositoryImpl<>();
        ServiciuInchiriere serviciu = new ServiciuInchiriere(repo);

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date dataInceput = format.parse("01/01/2023");
        Date dataSfarsit = format.parse("05/01/2023");
        Masina masina = new Masina(1, "Dacia", "Logan");

        Inchiriere inchiriere = new Inchiriere(1, masina, dataInceput, dataSfarsit);
        serviciu.adaugaInchiriere(inchiriere);

        Date dataInceputNoua = format.parse("02/01/2023");
        Date dataSfarsitNoua = format.parse("06/01/2023");
        Inchiriere inchiriereNoua = new Inchiriere(1, masina, dataInceputNoua, dataSfarsitNoua);
        serviciu.modificaInchiriere(inchiriereNoua);

        List<Inchiriere> inchirieri = repo.findAll();
        assertEquals(1, inchirieri.size());
        assertEquals(dataInceputNoua, inchirieri.get(0).getDataInceput());
        assertEquals(dataSfarsitNoua, inchirieri.get(0).getDataSfarsit());
    }

    @Test
    void testStergeInchiriere() throws Exception {
        RepositoryImpl<Inchiriere> repo = new RepositoryImpl<>();
        ServiciuInchiriere serviciu = new ServiciuInchiriere(repo);

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date dataInceput = format.parse("01/01/2023");
        Date dataSfarsit = format.parse("05/01/2023");
        Masina masina = new Masina(1, "Dacia", "Logan");

        Inchiriere inchiriere = new Inchiriere(1, masina, dataInceput, dataSfarsit);
        serviciu.adaugaInchiriere(inchiriere);
        serviciu.stergeInchiriere(1);

        List<Inchiriere> inchirieri = repo.findAll();
        assertTrue(inchirieri.isEmpty());
    }

    @Test
    void testToateInchirierile() throws Exception {
        RepositoryImpl<Inchiriere> repo = new RepositoryImpl<>();
        ServiciuInchiriere serviciu = new ServiciuInchiriere(repo);

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date dataInceput = format.parse("01/01/2023");
        Date dataSfarsit = format.parse("05/01/2023");
        Masina masina1 = new Masina(1, "Dacia", "Logan");
        Masina masina2 = new Masina(2, "BMW", "X5");

        Inchiriere inchiriere1 = new Inchiriere(1, masina1, dataInceput, dataSfarsit);
        Inchiriere inchiriere2 = new Inchiriere(2, masina2, dataInceput, dataSfarsit);
        serviciu.adaugaInchiriere(inchiriere1);
        serviciu.adaugaInchiriere(inchiriere2);

        List<Inchiriere> inchirieri = serviciu.toateInchirierile();
        assertEquals(2, inchirieri.size());
    }

    @Test
    void testAdaugaInchiriereInvalida() throws Exception {
        RepositoryImpl<Inchiriere> repo = new RepositoryImpl<>();
        ServiciuInchiriere serviciu = new ServiciuInchiriere(repo);

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date dataInceput = format.parse("05/01/2023");
        Date dataSfarsit = format.parse("01/01/2023");
        Masina masina = new Masina(1, "Dacia", "Logan");

        // Data de început este după data de sfârșit - ar trebui să genereze excepție
        assertThrows(IllegalArgumentException.class, () -> {
            Inchiriere inchiriere = new Inchiriere(1, masina, dataInceput, dataSfarsit);
            serviciu.adaugaInchiriere(inchiriere);
        });
    }
}
