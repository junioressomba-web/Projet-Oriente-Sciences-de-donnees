import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GestionEtudiant {

    public void calculerMoyennes(List<Etudiant> etudiants) {
        for (Etudiant etudiant : etudiants) {
            List<Double> notes = etudiant.getNotes();

            if (notes == null || notes.isEmpty()) {
                etudiant.setMoyenne(0.0);
                continue;
            }

            double somme = 0.0;
            for (Double note : notes) {
                somme += note;
            }

            double moyenne = somme / notes.size();
            etudiant.setMoyenne(moyenne);
        }
    }

    public void trierParMoyenne(List<Etudiant> etudiants) {
        Collections.sort(etudiants, new Comparator<Etudiant>() {
            @Override
            public int compare(Etudiant e1, Etudiant e2) {
                return Double.compare(e2.getMoyenne(), e1.getMoyenne());
            }
        });
    }
}