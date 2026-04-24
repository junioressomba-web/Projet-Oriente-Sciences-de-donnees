# Projet Orienté Sciences des Données — Gestion d'étudiants

Application Java en ligne de commande permettant de gérer une liste d'étudiants,
de calculer leurs moyennes, de les classer et d'exporter les résultats dans un fichier CSV.

Le projet met l'accent sur :

- une **modélisation orientée objet** claire (interface, classe abstraite, classe concrète),
- la **manipulation de fichiers CSV** via la bibliothèque [OpenCSV](https://opencsv.sourceforge.net/),
- une **interface console** interactive et tolérante aux erreurs de saisie.

---

## 📑 Sommaire

- [Fonctionnalités](#-fonctionnalités)
- [Architecture](#-architecture)
- [Structure du projet](#-structure-du-projet)
- [Format des fichiers CSV](#-format-des-fichiers-csv)
- [Prérequis](#-prérequis)
- [Installation & exécution](#-installation--exécution)
- [Exemple d'utilisation](#-exemple-dutilisation)
- [Gestion des erreurs](#-gestion-des-erreurs)
- [Améliorations possibles](#-améliorations-possibles)
- [Auteur](#-Martin-Junior-Essomba)
- [Licence](#-licence)

---

## ✨ Fonctionnalités

Depuis un menu interactif, l'utilisateur peut :

1. **Afficher** la liste des étudiants avec leurs matières et leurs notes.
2. **Afficher** la moyenne générale de chaque étudiant.
3. **Afficher le classement** des étudiants (tri décroissant par moyenne).
4. **Sauvegarder le classement** dans un fichier CSV dont le nom est choisi par l'utilisateur
   (extension `.csv` ajoutée automatiquement si oubliée).
5. **Rechercher un étudiant** par :
    - ID numérique,
    - nom complet (`prénom nom`),
    - nom seul / prénom seul,
    - recherche partielle, insensible à la casse.

   Un sous-menu permet alors de consulter :
    - toutes les notes de l'étudiant,
    - la note d'une matière précise (recherche exacte ou partielle),
    - sa moyenne générale.
0. **Quitter** proprement le programme.

Autres points notables :

- **Auto-détection du séparateur CSV** (`,` ou `;`) à la lecture du fichier.
- **Validation des entrées utilisateur** (choix de menu, nom de fichier, matière, etc.).
- **Formatage des moyennes** avec 2 décimales et point décimal (indépendamment de la locale système).

---

## 🏗️ Architecture

Le projet suit une découpe simple en couches :

| Couche | Classe(s) | Rôle |
|---|---|---|
| **Point d'entrée / UI console** | `Main` | Affiche le menu, lit les saisies, orchestre les appels. |
| **Métier** | `GestionEtudiant` | Calcul des moyennes et tri par moyenne décroissante. |
| **Modèle** | `Etudiant` | Représente un étudiant (id, nom, prénom, cours, notes, moyenne). |
| **Accès aux données — contrat** | `DataManager` | Interface : lecture / écriture d'étudiants. |
| **Accès aux données — base commune** | `AbstractCsvHandler` | Classe abstraite gérant le séparateur + détection automatique. |
| **Accès aux données — implémentation** | `CsvManager` | Lecture/écriture CSV via OpenCSV. |
| **Erreurs** | `DataAccessException` | Exception dédiée aux problèmes d'accès aux données. |

Diagramme simplifié :



            DataManager (interface)
                  ▲
                  │ implements
                  │
  AbstractCsvHandler ──── extends ───► CsvManager
         │                                  │
         │ fournit : séparateur,            │ utilise : OpenCSV
         │ detecterSeparateur(...)          │


---

## 📁 Structure du projet

```
.
├── src/
│   ├── Main.java                 # Point d'entrée + menu interactif
│   ├── Etudiant.java             # Modèle d'un étudiant
│   ├── GestionEtudiant.java      # Calcul des moyennes + tri
│   ├── DataManager.java          # Interface d'accès aux données
│   ├── AbstractCsvHandler.java   # Base commune (séparateur + détection auto)
│   ├── CsvManager.java           # Implémentation OpenCSV
│   └── DataAccessException.java  # Exception métier
├── etudiants.csv                 # Fichier d'entrée (exemple fourni)
├── resultats.csv                 # Exemple de fichier de sortie
├── pom.xml                       # Configuration Maven (dépendances + fat jar)
└── README.md
```

---

## 📄 Format des fichiers CSV

### Fichier d'entrée — `etudiants.csv`

- **Séparateur** : `,` ou `;` (détecté automatiquement à partir de la première ligne).
- **Première ligne (en-tête)** : `id,nom,prenom,<matière_1>,<matière_2>,...`
- **Lignes suivantes** : une ligne par étudiant, dans le même ordre de colonnes.

Exemple :

```csv
id,nom,prenom,statistiques,structure_de_donnees,cloud_computing,intelligence_artificielle
1,Tremblay,Alexis,78,85,90,88
2,Gagnon,Marie,92,88,75,95
3,Leblanc,Thomas,65,72,80,70
```

Règles de validation :

- l'en-tête doit contenir **au moins 4 colonnes** (`id`, `nom`, `prenom` + ≥ 1 matière) ;
- l'`id` doit être un **entier** ;
- chaque note doit être un **nombre** (le point ou la virgule décimale sont acceptés) ;
- les cellules de notes vides sont **ignorées** pour l'étudiant concerné.

### Fichier de sortie — classement

Lors de la sauvegarde (option 4), le fichier produit contient :

```csv
Rang,ID,Nom,Prenom,Moyenne
1,14,Bernier,Jade,96.71
2,6,Bouchard,Emma,93.29
3,10,Lavoie,Léa,91.86
```

- Moyennes formatées sur **2 décimales** avec un **point** comme séparateur décimal.
- Pas de guillemets superflus autour des champs.

---

## 🛠️ Prérequis

- **JDK 17+** (testé jusqu'au JDK 25).
- **Maven 3.6+** (recommandé) — ou simplement IntelliJ IDEA qui gérera Maven à ta place.
- Connexion internet lors du **premier build** pour télécharger OpenCSV.

Dépendance principale (déclarée dans `pom.xml`) :

```xml
<dependency>
    <groupId>com.opencsv</groupId>
    <artifactId>opencsv</artifactId>
    <version>5.9</version>
</dependency>
```

---

## 🚀 Installation & exécution

### Option 1 — Depuis IntelliJ IDEA (le plus simple)

1. Ouvrir le projet.
2. Si nécessaire : clic droit sur `pom.xml` → **Add as Maven Project**.
3. Dans la fenêtre Maven, cliquer sur 🔄 *Reload All Maven Projects* (pour télécharger OpenCSV).
4. **Build → Rebuild Project**.
5. Lancer la classe `Main` (clic droit → *Run 'Main.main()'*).

### Option 2 — Depuis un terminal (fat jar)

Le `pom.xml` embarque le `maven-shade-plugin` qui produit un jar exécutable autonome
contenant OpenCSV.

```bash
mvn clean package
java -jar target/ProjetOriente-1.0-SNAPSHOT.jar
```

> ⚠️ Le fichier `etudiants.csv` doit se trouver dans le **répertoire de travail**
> d'où est lancée la commande `java` (la racine du projet, par défaut).

### Option 3 — Compilation manuelle (sans Maven)

Nécessite d'avoir le jar d'OpenCSV et ses dépendances sur le classpath :

```bash
javac -d out src/*.java
java -cp "out;chemin\vers\opencsv-5.9.jar;chemin\vers\deps\*" Main
```

(Sous macOS / Linux, remplacer `;` par `:` dans le classpath.)

---

## 🧭 Exemple d'utilisation

```
===== MENU =====
1 - Afficher les etudiants, les matieres et les notes
2 - Afficher la moyenne des etudiants
3 - Afficher le classement
4 - Sauvegarde du classement
5 - Rechercher un etudiant
0 - Quitter
Votre choix : 3

=== Classement des etudiants ===
1 - Jade Bernier | Moyenne : 96.71
2 - Emma Bouchard | Moyenne : 93.29
3 - Léa Lavoie | Moyenne : 91.86
...
```

Recherche d'un étudiant :

```
Votre choix : 5

Entrez les coordonnees de l'etudiant : sophie
Etudiant trouve : ID : 4 | Nom : Roy | Prenom : Sophie

Que voulez-vous consulter ?
1 - Afficher toutes les notes
2 - Afficher la note d'une matiere
3 - Afficher la moyenne
Votre choix : 3
Moyenne de Sophie Roy : 89.86
```

---

## 🛡️ Gestion des erreurs

Toutes les erreurs d'accès aux données (fichier vide, en-tête invalide, ID non numérique,
note non parseable, fichier illisible, etc.) sont remontées via
`DataAccessException` et affichées dans la console sous la forme :

```
Erreur : <message explicite>
```

Les saisies invalides au menu (lettres, choix hors intervalle, nom de fichier vide)
sont re-demandées à l'utilisateur sans interrompre le programme.

---

## 🧩 Améliorations possibles

- Ajouter des **tests unitaires** (JUnit 5) pour `GestionEtudiant` et `CsvManager`.
- Permettre **l'ajout / la modification / la suppression** d'étudiants depuis le menu.
- Exporter les résultats dans d'autres formats (**JSON**, **Excel**).
- **Internationalisation** des messages (français / anglais).
- Interface graphique (**JavaFX**) ou API **REST** (Spring Boot) pour aller plus loin.

---

## 👤 Martin Junior Essomba

Projet réalisé dans le cadre du cours **Projet Orienté Sciences des Données**.

## 📜 Licence

Projet académique — libre d'utilisation à des fins pédagogiques.
