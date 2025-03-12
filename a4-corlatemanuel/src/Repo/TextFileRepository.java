package Repo;

import Entity.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TextFileRepository<T extends Entitate> extends RepositoryImpl<T> {
    private final String fileName;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public TextFileRepository(String fileName) {
        this.fileName = fileName;
        loadFromFile();
    }

    private void loadFromFile() {
        File file = new File(fileName);

        if (!file.exists() || file.length() == 0) {
            System.out.println("Fișierul " + fileName + " este gol sau nu există. Se continuă cu un repository gol.");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                T entity = deserialize(line);
                if (entity != null) {
                    super.add(entity);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private T deserialize(String line) {
        String[] tokens = line.split(",");
        if (tokens[0].equals("Masina")) {
            int id = Integer.parseInt(tokens[1]);
            String marca = tokens[2];
            String model = tokens[3];
            return (T) new Masina(id, marca, model);
        } else if (tokens[0].equals("Inchiriere")) {
            int id = Integer.parseInt(tokens[1]);
            int masinaId = Integer.parseInt(tokens[2]);
            String marcaMasina = tokens[3];
            String modelMasina = tokens[4];
            Date dataInceput = parseDate(tokens[5]);
            Date dataSfarsit = parseDate(tokens[6]);

            // Reconstruirea obiectului Masina folosind datele din fișier
            Masina masina = new Masina(masinaId, marcaMasina, modelMasina);
            return (T) new Inchiriere(id, masina, dataInceput, dataSfarsit);
        }
        return null;
    }

    private String serialize(T entity) {
        if (entity instanceof Masina) {
            Masina masina = (Masina) entity;
            return "Masina," + masina.getId() + "," + masina.getMarca() + "," + masina.getModel();
        } else if (entity instanceof Inchiriere) {
            Inchiriere inchiriere = (Inchiriere) entity;
            // Salvăm toate detaliile complete ale mașinii
            Masina masina = inchiriere.getMasina();
            return "Inchiriere," + inchiriere.getId() + "," + masina.getId() + "," + masina.getMarca() + ","
                    + masina.getModel() + "," + formatDate(inchiriere.getDataInceput()) + ","
                    + formatDate(inchiriere.getDataSfarsit());
        }
        return "";
    }

    private String formatDate(Date date) {
        return dateFormat.format(date);
    }

    private Date parseDate(String dateString) {
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void saveToFile() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (T entity : findAll()) {
                writer.write(serialize(entity));
                writer.newLine();
            }
        }
    }

    @Override
    public void add(T entity) throws Exception {
        super.add(entity);
        saveToFile();
    }

    @Override
    public void update(T entity) throws Exception {
        super.update(entity);
        saveToFile();
    }

    @Override
    public void delete(int id) throws Exception {
        super.delete(id);
        saveToFile();
    }
}
