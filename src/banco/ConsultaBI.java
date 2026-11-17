package banco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ConsultaBI {

    public void listarDados() {

        String sql = """
            SELECT
                a.nmAluno AS aluno,
                a.mtrAluno AS matricula,
                e.nmEscola AS escola,
                d.nmDisciplina AS disciplina,
                p.nmProfessor AS professor,
                n.nota AS nota
            FROM aluno AS a
        	JOIN matricula AS m
        		ON a.cdAluno = m.cdAluno

            JOIN escola AS e
                ON m.cdEscola = e.cdEscola

            JOIN cursa AS c
                ON a.cdAluno = c.cdAluno

            JOIN nota AS n
                ON n.cdAluno = a.cdAluno
            AND n.cdDisciplina = c.cdDisciplina

            JOIN disciplina AS d
                ON c.cdDisciplina = d.cdDisciplina

            JOIN leciona AS l
                ON d.cdDisciplina = l.cdDisciplina

            JOIN professor AS p
                ON l.cdProfessor = p.cdProfessor
            ORDER BY a.nmAluno, d.nmDisciplina
        """;

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet resultado = stmt.executeQuery()) {

            while (resultado.next()) {

                System.out.println(
                    resultado.getString("aluno") + " | " +
                    resultado.getString("matricula") + " | " +
                    resultado.getString("escola") + " | " +
                    resultado.getString("disciplina") + " | " +
                    resultado.getString("professor") + " | " +
                    resultado.getDouble("nota")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        ConsultaBI consulta = new ConsultaBI();

        consulta.listarDados();
    }
}