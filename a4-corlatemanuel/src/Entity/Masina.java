package Entity;

public class Masina extends Entitate {
    private String marca;
    private String model;

    public Masina(int id, String marca, String model) {
        super(id);
        this.marca = marca;
        this.model = model;
    }

    public String getMarca() {
        return marca;
    }

    public String getModel() {
        return model;
    }

    @Override
    public String toString() {
        return "Masina{" + "id=" + id + ", marca='" + marca + '\'' + ", model='" + model + '\'' + '}';
    }
}