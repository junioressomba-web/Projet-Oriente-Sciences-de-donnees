public abstract class AbstractCsvHandler {

    protected char separateur;

    public AbstractCsvHandler() {
        this.separateur = ',';
    }

    public AbstractCsvHandler(char separateur) {
        this.separateur = separateur;
    }

    public char getSeparateur() {
        return separateur;
    }

    public void setSeparateur(char separateur) {
        this.separateur = separateur;
    }

    protected char detecterSeparateur(String ligne) {
        if (ligne == null || ligne.trim().isEmpty()) {
            return separateur;
        }

        boolean contientVirgule = ligne.contains(",");
        boolean contientPointVirgule = ligne.contains(";");

        if (contientVirgule && !contientPointVirgule) {
            return ',';
        }

        if (contientPointVirgule && !contientVirgule) {
            return ';';
        }

        if (contientVirgule && contientPointVirgule) {
            int nbVirgules = compterOccurrences(ligne, ',');
            int nbPointsVirgules = compterOccurrences(ligne, ';');
            return nbPointsVirgules > nbVirgules ? ';' : ',';
        }

        return separateur;
    }

    private int compterOccurrences(String texte, char caractere) {
        int compteur = 0;

        for (int i = 0; i < texte.length(); i++) {
            if (texte.charAt(i) == caractere) {
                compteur++;
            }
        }

        return compteur;
    }
}