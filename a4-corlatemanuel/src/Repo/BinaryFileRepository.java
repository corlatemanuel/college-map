package Repo;

import Entity.*;
import java.io.*;
import java.util.List;

public class BinaryFileRepository<T extends Entitate & Serializable> extends RepositoryImpl<T> {
    private final String fileName;

    public BinaryFileRepository(String fileName) {
        this.fileName = fileName;
        loadFromFile();
    }

    private void loadFromFile() {
        File file = new File(fileName);

        if (!file.exists() || file.length() == 0) {
            System.out.println("Fișierul " + fileName + " este gol sau nu există. Se continuă cu un repository gol.");
            return;
        }

        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
            // Citește întreaga listă de obiecte din fișier
            List<T> loadedEntities = (List<T>) inputStream.readObject();
            for (T entity : loadedEntities) {
                super.add(entity);
            }
        } catch (EOFException e) {
            System.out.println("Toate datele au fost încărcate din fișierul " + fileName);
        } catch (Exception e) {
            System.err.println("Eroare la încărcarea datelor din fișierul " + fileName);
            e.printStackTrace();
        }
    }



    private void saveToFile() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName))) {
            // Scrie întreaga listă în fișier
            outputStream.writeObject(super.findAll());
            outputStream.flush();
        } catch (IOException e) {
            System.err.println("Eroare la salvarea datelor în fișierul " + fileName);
            e.printStackTrace();
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