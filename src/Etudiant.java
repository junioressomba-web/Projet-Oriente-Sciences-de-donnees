import java.util.List;

public class Etudiant {
    private int id;
    private String nom;
    private String prenom;
    private List<String> cours;
    private List<Double> notes;
    private double moyenne;

    public Etudiant(int id, String nom, String prenom, List<String> cours, List<Double> notes) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.cours = cours;
        this.notes = notes;
        this.moyenne = 0.0;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getNomComplet() {
        return prenom + " " + nom;
    }

    public List<String> getCours() {
        return cours;
    }

    public List<Double> getNotes() {
        return notes;
    }

    public double getMoyenne() {
        return moyenne;
    }

    public void setMoyenne(double moyenne) {
        this.moyenne = moyenne;
    }
}