import java.util.List;

public interface DataManager {
    List<Etudiant> lireEtudiants(String nomFichier) throws DataAccessException;

    void ecrireResultats(String nomFichier, List<Etudiant> etudiants) throws DataAccessException;
}