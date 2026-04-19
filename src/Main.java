import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        CsvManager csvManager = new CsvManager(',');
        GestionEtudiant gestionEtudiant = new GestionEtudiant();
        Scanner scanner = new Scanner(System.in);

        try {
            List<Etudiant> etudiants = csvManager.lireEtudiants("etudiants.csv");
            int choix;

            do {
                afficherMenu();
                choix = lireChoixMenu(scanner, 0, 5);

                switch (choix) {
                    case 1:
                        afficherEtudiants(etudiants);
                        break;

                    case 2:
                        gestionEtudiant.calculerMoyennes(etudiants);
                        afficherEtudiantsAvecMoyenne(etudiants);
                        break;

                    case 3:
                        gestionEtudiant.calculerMoyennes(etudiants);
                        gestionEtudiant.trierParMoyenne(etudiants);
                        afficherClassement(etudiants);
                        break;

                    case 4:
                        gestionEtudiant.calculerMoyennes(etudiants);
                        gestionEtudiant.trierParMoyenne(etudiants);

                        System.out.print("Entrez le nom du fichier de sauvegarde : ");
                        String nomFichier = scanner.nextLine().trim();

                        if (nomFichier.isEmpty()) {
                            System.out.println("Erreur : le nom du fichier ne peut pas etre vide.");
                            break;
                        }

                        if (!nomFichier.endsWith(".csv")) {
                            nomFichier = nomFichier + ".csv";
                        }

                        csvManager.ecrireResultats(nomFichier, etudiants);
                        System.out.println("Les resultats ont ete sauvegardes dans " + nomFichier);
                        break;

                    case 5:
                        gestionEtudiant.calculerMoyennes(etudiants);
                        rechercherEtudiant(etudiants, scanner);
                        break;

                    case 0:
                        System.out.println("Fin du programme.");
                        break;
                }

            } while (choix != 0);

        } catch (DataAccessException e) {
            System.out.println("Erreur : " + e.getMessage());
        }

        scanner.close();
    }

    public static void afficherMenu() {
        System.out.println("\n===== MENU =====");
        System.out.println("1 - Afficher les etudiants, les matieres et les notes");
        System.out.println("2 - Afficher la moyenne des etudiants");
        System.out.println("3 - Afficher le classement");
        System.out.println("4 - Sauvegarde du classement");
        System.out.println("5 - Rechercher un etudiant");
        System.out.println("0 - Quitter");
        System.out.print("Votre choix : ");
    }

    public static int lireChoixMenu(Scanner scanner, int min, int max) {
        while (true) {
            String saisie = scanner.nextLine().trim();

            try {
                int choix = Integer.parseInt(saisie);

                if (choix >= min && choix <= max) {
                    return choix;
                } else {
                    System.out.print("Erreur : commande invalide. Entrez un choix entre " + min + " et " + max + " : ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Erreur : veuillez entrer un nombre valide : ");
            }
        }
    }

    public static void afficherEtudiants(List<Etudiant> etudiants) {
        System.out.println("\n=== Liste des etudiants ===");

        for (Etudiant etudiant : etudiants) {
            System.out.println("ID : " + etudiant.getId());
            System.out.println("Nom : " + etudiant.getNom());
            System.out.println("Prenom : " + etudiant.getPrenom());

            List<String> cours = etudiant.getCours();
            List<Double> notes = etudiant.getNotes();

            for (int i = 0; i < cours.size() && i < notes.size(); i++) {
                System.out.println("  - Matiere : " + cours.get(i) + " | Note : " + notes.get(i));
            }

            System.out.println();
        }
    }

    public static void afficherEtudiantsAvecMoyenne(List<Etudiant> etudiants) {
        System.out.println("\n=== Etudiants avec moyenne ===");

        for (Etudiant etudiant : etudiants) {
            System.out.println("ID : " + etudiant.getId());
            System.out.println("Nom : " + etudiant.getNom());
            System.out.println("Prenom : " + etudiant.getPrenom());

            List<String> cours = etudiant.getCours();
            List<Double> notes = etudiant.getNotes();

            for (int i = 0; i < cours.size() && i < notes.size(); i++) {
                System.out.println("  - Matiere : " + cours.get(i) + " | Note : " + notes.get(i));
            }

            System.out.println("Moyenne : " + String.format("%.2f", etudiant.getMoyenne()));
            System.out.println();
        }
    }

    public static void afficherClassement(List<Etudiant> etudiants) {
        System.out.println("\n=== Classement des etudiants ===");

        int rang = 1;
        for (Etudiant etudiant : etudiants) {
            System.out.println(rang + " - " + etudiant.getPrenom() + " " + etudiant.getNom()
                    + " | Moyenne : " + String.format("%.2f", etudiant.getMoyenne()));
            rang++;
        }
    }

    public static void rechercherEtudiant(List<Etudiant> etudiants, Scanner scanner) {
        System.out.print("\nEntrez les coordonnees de l'etudiant : ");
        String saisie = scanner.nextLine().trim();

        if (saisie.isEmpty()) {
            System.out.println("Erreur : la recherche ne peut pas etre vide.");
            return;
        }

        List<Etudiant> resultats = new ArrayList<>();

        try {
            int idRecherche = Integer.parseInt(saisie);

            for (Etudiant etudiant : etudiants) {
                if (etudiant.getId() == idRecherche) {
                    resultats.add(etudiant);
                    break;
                }
            }
        } catch (NumberFormatException e) {
        }

        if (resultats.isEmpty()) {
            for (Etudiant etudiant : etudiants) {
                if (etudiant.getNomComplet().equalsIgnoreCase(saisie)) {
                    resultats.add(etudiant);
                }
            }
        }

        if (resultats.isEmpty()) {
            for (Etudiant etudiant : etudiants) {
                if (etudiant.getPrenom().equalsIgnoreCase(saisie)
                        || etudiant.getNom().equalsIgnoreCase(saisie)) {
                    resultats.add(etudiant);
                }
            }
        }

        if (resultats.isEmpty()) {
            for (Etudiant etudiant : etudiants) {
                if (etudiant.getPrenom().toLowerCase().contains(saisie.toLowerCase())
                        || etudiant.getNom().toLowerCase().contains(saisie.toLowerCase())
                        || etudiant.getNomComplet().toLowerCase().contains(saisie.toLowerCase())) {
                    resultats.add(etudiant);
                }
            }
        }

        if (resultats.isEmpty()) {
            System.out.println("Erreur : aucun etudiant trouve.");
            return;
        }

        Etudiant etudiantChoisi = resultats.get(0);

        System.out.println("\nEtudiant trouve : ID : " + etudiantChoisi.getId()
                + " | Nom : " + etudiantChoisi.getNom()
                + " | Prenom : " + etudiantChoisi.getPrenom());

        System.out.println("\nQue voulez-vous consulter ?");
        System.out.println("1 - Afficher toutes les notes");
        System.out.println("2 - Afficher la note d'une matiere");
        System.out.println("3 - Afficher la moyenne");
        System.out.print("Votre choix : ");

        int choixConsultation = lireChoixMenu(scanner, 1, 3);

        switch (choixConsultation) {
            case 1:
                System.out.println("\nNotes de " + etudiantChoisi.getPrenom() + " " + etudiantChoisi.getNom() + " :");
                for (int i = 0; i < etudiantChoisi.getCours().size() && i < etudiantChoisi.getNotes().size(); i++) {
                    System.out.println("  - Matiere : " + etudiantChoisi.getCours().get(i)
                            + " | Note : " + etudiantChoisi.getNotes().get(i));
                }
                break;

            case 2:
                afficherNoteParMatiere(etudiantChoisi, scanner);
                break;

            case 3:
                System.out.println("Moyenne de " + etudiantChoisi.getPrenom() + " " + etudiantChoisi.getNom() + " : "
                        + String.format("%.2f", etudiantChoisi.getMoyenne()));
                break;
        }
    }

    public static void afficherNoteParMatiere(Etudiant etudiant, Scanner scanner) {
        System.out.print("Entrez le nom complet de la matiere ou une partie du nom : ");
        String matiereRecherche = scanner.nextLine().trim();

        if (matiereRecherche.isEmpty()) {
            System.out.println("Erreur : la matiere ne peut pas etre vide.");
            return;
        }

        int indexTrouve = -1;

        for (int i = 0; i < etudiant.getCours().size(); i++) {
            String nomMatiere = etudiant.getCours().get(i);
            if (nomMatiere.equalsIgnoreCase(matiereRecherche)) {
                indexTrouve = i;
                break;
            }
        }

        if (indexTrouve == -1) {
            String rechercheMin = matiereRecherche.toLowerCase();

            for (int i = 0; i < etudiant.getCours().size(); i++) {
                String nomMatiere = etudiant.getCours().get(i).toLowerCase();
                if (nomMatiere.contains(rechercheMin)) {
                    indexTrouve = i;
                    break;
                }
            }
        }

        if (indexTrouve == -1) {
            System.out.println("Erreur : aucune matiere trouvee pour cet etudiant.");
            System.out.println("Matieres disponibles pour cet etudiant :");
            for (String c : etudiant.getCours()) {
                System.out.println("  - " + c);
            }
            return;
        }

        System.out.println("Note de " + etudiant.getPrenom() + " " + etudiant.getNom() + " en "
                + etudiant.getCours().get(indexTrouve) + " : " + etudiant.getNotes().get(indexTrouve));
    }
}