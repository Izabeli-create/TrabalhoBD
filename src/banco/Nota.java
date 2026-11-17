package banco;

public class Nota {

    private int cdAluno;
    private int cdDisciplina;
    private double nota;

    public Nota(int cdAluno, int cdDisciplina, double nota) {
        this.cdAluno = cdAluno;
        this.cdDisciplina = cdDisciplina;
        this.nota = nota;
    }

    public int getCdAluno() {
        return cdAluno;
    }

    public int getCdDisciplina() {
        return cdDisciplina;
    }

    public double getNota() {
        return nota;
    }
}