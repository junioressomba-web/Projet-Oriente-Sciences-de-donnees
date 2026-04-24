// Exception personnalisée pour signaler les erreurs d'accès aux données
public class DataAccessException extends Exception {
    // Constructeur qui reçoit un message d'erreur
    public DataAccessException(String message) {
        super(message);
    }
}