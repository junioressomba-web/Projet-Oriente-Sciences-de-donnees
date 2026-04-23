import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        CsvManager csvManager = new CsvManager();

       //creation d'objet gestion etudiant pour calcul moyenne et classement
        GestionEtudiant gestionEtudiant = new GestionEtudiant();

        Scanner scanner = new Scanner(System.in);

        try {
            // On lit la liste des étudiants à partir du fichier "etudiants.csv" et On transforme Chaque ligne du fichier en objet Etudiant.
            List<Etudiant> etudiants = csvManager.lireEtudiants("etudiants.csv");
            int choix;

            do {
                afficherMenu();

                choix = lireChoixMenu(scanner, 0, 5);

                switch (choix) {
                    case 1:
                        //On affiche tous les étudiants, leurs matières et notes.
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

                        // Si l'utilisateur n'a pas mis .csv, on l'ajoute automatiquement.
                        if (!nomFichier.endsWith(".csv")) {
                            nomFichier = nomFichier + ".csv";
                        }

                        // Écriture des résultats (rang, id, nom, moyenne) dans le fichier.
                        csvManager.ecrireResultats(nomFichier, etudiants);
                        System.out.println("Les resultats ont ete sauvegardes dans " + nomFichier);
                        break;

                    case 5:
                        // Calculer les moyennes pour être sûr qu'elles sont à jour,
                        // puis lancer la recherche d'un étudiant (par id, nom, prénom, etc.).
                        gestionEtudiant.calculerMoyennes(etudiants);
                        rechercherEtudiant(etudiants, scanner);
                        break;

                    case 0:
                        // Sortie propre du programme.
                        System.out.println("Fin du programme.");
                        break;
                }

            } while (choix != 0); // Tant que l'utilisateur n'a pas choisi 0.

        } catch (DataAccessException e) {
            // En cas de problème lors de la lecture/écriture des données (CSV),
            // on affiche un message d'erreur clair.
            System.out.println("Erreur : " + e.getMessage());
        }

        // On ferme le scanner pour libérer la ressource.
        scanner.close();
    }

    // Affiche le menu principal du programme avec les différentes options disponibles.
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

    // Lit un choix entier au clavier et vérifie qu'il se situe entre min et max.
    // Tant que l'utilisateur se trompe (lettre, nombre hors intervalle),
    // on redemande une saisie.
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

    // Affiche tous les étudiants avec leurs identifiants, noms, prénoms
    // et la liste de leurs matières et notes.
    public static void afficherEtudiants(List<Etudiant> etudiants) {
        System.out.println("\n=== Liste des etudiants ===");

        for (Etudiant etudiant : etudiants) {
            System.out.println("ID : " + etudiant.getId());
            System.out.println("Nom : " + etudiant.getNom());
            System.out.println("Prenom : " + etudiant.getPrenom());

            List<String> cours = etudiant.getCours();
            List<Double> notes = etudiant.getNotes();


            for (int i = 0; i < cours.size() && i < notes.size(); i++) {
                System.out.println(cours.get(i) + " | Note : " + notes.get(i));
            }

            System.out.println();
        }
    }

    // Affiche tous les étudiants, leurs matières, notes,
    // et leur moyenne générale (déjà calculée avant l'appel).
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

            // Affichage de la moyenne avec 2 chiffres après la virgule.
            System.out.println("Moyenne : " + String.format("%.2f", etudiant.getMoyenne()));
            System.out.println();
        }
    }

    // Affiche le classement des étudiants par moyenne.
    // On suppose que la liste "etudiants" a déjà été triée par moyenne décroissante.
    public static void afficherClassement(List<Etudiant> etudiants) {
        System.out.println("\n=== Classement des etudiants ===");

        int rang = 1;
        for (Etudiant etudiant : etudiants) {
            System.out.println(rang + " - " + etudiant.getPrenom() + " " + etudiant.getNom()
                    + " | Moyenne : " + String.format("%.2f", etudiant.getMoyenne()));
            rang++;
        }
    }

    // Permet de rechercher un étudiant à partir de différentes informations :
    // id, nom complet, prénom, nom, ou partie du nom/prénom.
    // Une fois l'étudiant trouvé, on propose un mini-menu pour afficher
    // ses notes, la note d'une matière, ou sa moyenne.
    public static void rechercherEtudiant(List<Etudiant> etudiants, Scanner scanner) {
        System.out.print("\nEntrez les coordonnees de l'etudiant : ");
        String saisie = scanner.nextLine().trim();

        if (saisie.isEmpty()) {
            System.out.println("Erreur : la recherche ne peut pas etre vide.");
            return;
        }

        List<Etudiant> resultats = new ArrayList<>();

        // 1) Tentative de recherche par ID si la saisie est un nombre.
        try {
            int idRecherche = Integer.parseInt(saisie);

            for (Etudiant etudiant : etudiants) {
                if (etudiant.getId() == idRecherche) {
                    resultats.add(etudiant);
                    break;
                }
            }
        } catch (NumberFormatException e) {
            // Si la saisie n'est pas un nombre, on ignore cette partie.
        }

        // 2) Si rien trouvé par ID, on teste l'égalité exacte sur le nom complet.
        if (resultats.isEmpty()) {
            for (Etudiant etudiant : etudiants) {
                if (etudiant.getNomComplet().equalsIgnoreCase(saisie)) {
                    resultats.add(etudiant);
                }
            }
        }

        // 3) Si toujours vide, on cherche par prénom OU par nom (égalité exacte).
        if (resultats.isEmpty()) {
            for (Etudiant etudiant : etudiants) {
                if (etudiant.getPrenom().equalsIgnoreCase(saisie)
                        || etudiant.getNom().equalsIgnoreCase(saisie)) {
                    resultats.add(etudiant);
                }
            }
        }

        // 4) Si toujours rien, on fait une recherche partielle (contains)
        // sur le prénom, le nom ou le nom complet (insensible à la casse).
        if (resultats.isEmpty()) {
            for (Etudiant etudiant : etudiants) {
                if (etudiant.getPrenom().toLowerCase().contains(saisie.toLowerCase())
                        || etudiant.getNom().toLowerCase().contains(saisie.toLowerCase())
                        || etudiant.getNomComplet().toLowerCase().contains(saisie.toLowerCase())) {
                    resultats.add(etudiant);
                }
            }
        }

        // Si aucun étudiant ne correspond, on affiche un message d'erreur.
        if (resultats.isEmpty()) {
            System.out.println("Erreur : aucun etudiant trouve.");
            return;
        }

        Etudiant etudiantChoisi = resultats.get(0);

        System.out.println("\nEtudiant trouve : ID : " + etudiantChoisi.getId()
                + " | Nom : " + etudiantChoisi.getNom()
                + " | Prenom : " + etudiantChoisi.getPrenom());

        // Mini-menu pour consulter les informations de l'étudiant choisi.
        System.out.println("\nQue voulez-vous consulter ?");
        System.out.println("1 - Afficher toutes les notes");
        System.out.println("2 - Afficher la note d'une matiere");
        System.out.println("3 - Afficher la moyenne");
        System.out.print("Votre choix : ");

        int choixConsultation = lireChoixMenu(scanner, 1, 3);

        switch (choixConsultation) {
            case 1:
                // Affiche toutes les matières et les notes de l'étudiant.
                System.out.println("\nNotes de " + etudiantChoisi.getPrenom() + " " + etudiantChoisi.getNom() + " :");
                for (int i = 0; i < etudiantChoisi.getCours().size()
                        && i < etudiantChoisi.getNotes().size(); i++) {
                    System.out.println("  - Matiere : " + etudiantChoisi.getCours().get(i)
                            + " | Note : " + etudiantChoisi.getNotes().get(i));
                }
                break;

            case 2:
                // Permet de rechercher une matière précise (nom complet ou partiel)
                // et d'afficher uniquement la note associée.
                afficherNoteParMatiere(etudiantChoisi, scanner);
                break;

            case 3:
                // Affiche la moyenne générale de l'étudiant.
                System.out.println("Moyenne de " + etudiantChoisi.getPrenom() + " "
                        + etudiantChoisi.getNom() + " : "
                        + String.format("%.2f", etudiantChoisi.getMoyenne()));
                break;
        }
    }

    // Permet d'afficher la note de l'étudiant pour une matière donnée.
    // La recherche se fait d'abord par nom exact, puis par nom partiel.
    public static void afficherNoteParMatiere(Etudiant etudiant, Scanner scanner) {
        System.out.print("Entrez le nom complet de la matiere ou une partie du nom : ");
        String matiereRecherche = scanner.nextLine().trim();

        if (matiereRecherche.isEmpty()) {
            System.out.println("Erreur : la matiere ne peut pas etre vide.");
            return;
        }

        int indexTrouve = -1;

        // 1) Recherche par nom EXACT (insensible à la casse).
        for (int i = 0; i < etudiant.getCours().size(); i++) {
            String nomMatiere = etudiant.getCours().get(i);
            if (nomMatiere.equalsIgnoreCase(matiereRecherche)) {
                indexTrouve = i;
                break;
            }
        }

        // 2) Si aucune matière trouvée, on fait une recherche PARTIELLE (contains).
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

        // 3) Si toujours rien, on informe l'utilisateur et on affiche la liste des matières.
        if (indexTrouve == -1) {
            System.out.println("Erreur : aucune matiere trouvee pour cet etudiant.");
            System.out.println("Matieres disponibles pour cet etudiant :");
            for (String c : etudiant.getCours()) {
                System.out.println("  - " + c);
            }
            return;
        }

        // 4) Si une matière a été trouvée, on affiche la note correspondante.
        System.out.println("Note de " + etudiant.getPrenom() + " " + etudiant.getNom() + " en "
                + etudiant.getCours().get(indexTrouve) + " : " + etudiant.getNotes().get(indexTrouve));
    }
}