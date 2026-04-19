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
}