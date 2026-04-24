import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
// Classe responsable de la lecture et de l'écriture des fichiers CSV
public class CsvManager extends AbstractCsvHandler implements DataManager {
//on appelle le constructeur de la classe AbstractCsvHandler
    public CsvManager() {
        super();
    }

    public CsvManager(char separateur) {
        super(separateur);
    }

    @Override
    public List<Etudiant> lireEtudiants(String nomFichier) throws DataAccessException {
        // Auto-détection du séparateur à partir de la première ligne non vide
        char sep = detecterSeparateurDansFichier(nomFichier);
        this.separateur = sep;

        List<Etudiant> etudiants = new ArrayList<>();
                 // lecture du csv
        try (Reader reader = new FileReader(nomFichier);
             CSVReader csvReader = new CSVReaderBuilder(reader)
                     .withCSVParser(new CSVParserBuilder()
                             .withSeparator(sep)
                             .build())
                     .build()) {

            String[] enTetes = csvReader.readNext();

            if (enTetes == null || enTetes.length == 0) {
                throw new DataAccessException("Le fichier CSV est vide.");
            }
            if (enTetes.length < 4) {
                throw new DataAccessException("En-tete CSV invalide (au moins 4 colonnes attendues).");
            }

            String[] colonnes;
            while ((colonnes = csvReader.readNext()) != null) {
                if (estLigneVide(colonnes)) {
                    continue;
                }
                if (colonnes.length < 4) {
                    throw new DataAccessException(
                            "Ligne invalide dans le fichier CSV : " + String.join(String.valueOf(sep), colonnes));
                }
                // Conversion de la première colonne en entier pour l'identifiant
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
                    if (noteTexte.isEmpty()) {
                        continue;
                    }
                    try {
                        // Autorise aussi la virgule comme séparateur décimal ("12,5")
                        double note = Double.parseDouble(noteTexte.replace(',', '.'));
                        cours.add(enTetes[i].trim());
                        notes.add(note);
                    } catch (NumberFormatException e) {
                        throw new DataAccessException(
                                "Note invalide pour l'etudiant " + prenom + " " + nom + " : " + noteTexte);
                    }
                }

                etudiants.add(new Etudiant(id, nom, prenom, cours, notes));
            }

        } catch (IOException | CsvValidationException e) {
            throw new DataAccessException("Erreur lors de la lecture du fichier : " + nomFichier);
        }

        return etudiants;
    }

    @Override
    public void ecrireResultats(String nomFichier, List<Etudiant> etudiants) throws DataAccessException {
        try (Writer writer = new FileWriter(nomFichier);
             CSVWriter csvWriter = new CSVWriter(
                     writer,
                     separateur,
                     CSVWriter.NO_QUOTE_CHARACTER,
                     CSVWriter.NO_ESCAPE_CHARACTER,
                     CSVWriter.DEFAULT_LINE_END)) {

            csvWriter.writeNext(new String[] { "Rang", "ID", "Nom", "Prenom", "Moyenne" });
            // On parcours de tous les étudiants pour écrire une ligne par étudiant
            for (int i = 0; i < etudiants.size(); i++) {
                Etudiant e = etudiants.get(i);
                csvWriter.writeNext(new String[] {
                        String.valueOf(i + 1),
                        String.valueOf(e.getId()),
                        e.getNom(),
                        e.getPrenom(),
                        // Locale.ROOT -> force le point décimal, indépendamment du systeme local
                        String.format(Locale.ROOT, "%.2f", e.getMoyenne())
                });
            }

        } catch (IOException e) {
            throw new DataAccessException("Erreur lors de l'ecriture du fichier : " + nomFichier);
        }
    }

    // --- Utilitaires privés ---

    private char detecterSeparateurDansFichier(String nomFichier) throws DataAccessException {
        try (BufferedReader br = new BufferedReader(new FileReader(nomFichier))) {
            String premiereLigne;
            while ((premiereLigne = br.readLine()) != null) {
                if (!premiereLigne.trim().isEmpty()) {
                    return detecterSeparateur(premiereLigne);
                }
            }
        } catch (IOException e) {
            throw new DataAccessException("Erreur lors de la lecture du fichier : " + nomFichier);
        }
        return separateur; // valeur par défaut si fichier vide
    }

    private boolean estLigneVide(String[] colonnes) {
        if (colonnes.length == 0) return true;
        for (String c : colonnes) {
            if (c != null && !c.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }
}