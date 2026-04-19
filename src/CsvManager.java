import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvManager {

    private char separateur;

    public CsvManager(char separateur) {
        this.separateur = separateur;
    }

    public List<Etudiant> lireEtudiants(String nomFichier) throws DataAccessException {
        List<Etudiant> etudiants = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(nomFichier))) {
            String ligneEnTete = reader.readLine();

            if (ligneEnTete == null || ligneEnTete.trim().isEmpty()) {
                throw new DataAccessException("Le fichier CSV est vide.");
            }

            String[] enTetes = ligneEnTete.split(String.valueOf(separateur));

            if (enTetes.length < 4) {
                throw new DataAccessException("En-tete CSV invalide.");
            }

            List<String> matieres = new ArrayList<>();
            for (int i = 3; i < enTetes.length; i++) {
                matieres.add(enTetes[i].trim());
            }

            String ligne;
            while ((ligne = reader.readLine()) != null) {
                if (ligne.trim().isEmpty()) {
                    continue;
                }

                String[] colonnes = ligne.split(String.valueOf(separateur));

                if (colonnes.length < 4) {
                    throw new DataAccessException("Ligne invalide dans le fichier CSV : " + ligne);
                }

                int id;
                try {
                    id = Integer.parseInt(colonnes[0].trim());
                } catch (NumberFormatException e) {
                    throw new DataAccessException("ID invalide dans le fichier CSV : " + colonnes[0]);
                }

                String nom = colonnes[1].trim();
                String prenom = colonnes[2].trim();

                List<String> cours = new ArrayList<>();
                List<Double> notes = new ArrayList<>();

                for (int i = 3; i < colonnes.length && i < enTetes.length; i++) {
                    String noteTexte = colonnes[i].trim();

                    if (!noteTexte.isEmpty()) {
                        try {
                            double note = Double.parseDouble(noteTexte);
                            cours.add(enTetes[i].trim());
                            notes.add(note);
                        } catch (NumberFormatException e) {
                            throw new DataAccessException("Note invalide pour l'etudiant " + prenom + " " + nom + " : " + noteTexte);
                        }
                    }
                }

                Etudiant etudiant = new Etudiant(id, nom, prenom, cours, notes);
                etudiants.add(etudiant);
            }

        } catch (IOException e) {
            throw new DataAccessException("Erreur lors de la lecture du fichier : " + nomFichier);
        }

        return etudiants;
    }

    public void ecrireResultats(String nomFichier, List<Etudiant> etudiants) throws DataAccessException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomFichier))) {
            writer.write("Rang,ID,Nom,Prenom,Moyenne");
            writer.newLine();

            for (int i = 0; i < etudiants.size(); i++) {
                Etudiant etudiant = etudiants.get(i);

                writer.write((i + 1) + ","
                        + etudiant.getId() + ","
                        + etudiant.getNom() + ","
                        + etudiant.getPrenom() + ","
                        + String.format("%.2f", etudiant.getMoyenne()));
                writer.newLine();
            }

        } catch (IOException e) {
            throw new DataAccessException("Erreur lors de l'ecriture du fichier : " + nomFichier);
        }
    }
}