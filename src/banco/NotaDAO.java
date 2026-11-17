package banco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class NotaDAO {

    public void cadastrarNota(int cdAluno, int cdDisciplina, double nota) {

        String sql = """
            INSERT INTO nota (cdAluno, cdDisciplina, nota)
            VALUES (?, ?, ?)
        """;

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, cdAluno);
            stmt.setInt(2, cdDisciplina);
            stmt.setDouble(3, nota);

            stmt.executeUpdate();

            System.out.println("Nota cadastrada com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listarNotas() {

        String sql = """
            SELECT
                n.cdAluno,
                a.nmAluno,
                n.cdDisciplina,
                d.nmDisciplina,
                n.nota
            FROM nota n
            JOIN aluno a
                ON n.cdAluno = a.cdAluno
            JOIN disciplina d
                ON n.cdDisciplina = d.cdDisciplina
            ORDER BY a.nmAluno, d.nmDisciplina
        """;

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet resultado = stmt.executeQuery()) {

            while (resultado.next()) {

                System.out.println(
                    resultado.getInt("cdAluno") + " - " +
                    resultado.getString("nmAluno") + " - " +
                    resultado.getInt("cdDisciplina") + " - " +
                    resultado.getString("nmDisciplina") + " - " +
                    resultado.getDouble("nota")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void alterarNota(int cdAluno, int cdDisciplina, double novaNota) {

        String sql = """
            UPDATE nota
            SET nota = ?
            WHERE cdAluno = ?
            AND cdDisciplina = ?
        """;

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setDouble(1, novaNota);
            stmt.setInt(2, cdAluno);
            stmt.setInt(3, cdDisciplina);

            stmt.executeUpdate();

            System.out.println("Nota alterada com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void excluirNota(int cdAluno, int cdDisciplina) {

        String sql = """
            DELETE FROM nota
            WHERE cdAluno = ?
            AND cdDisciplina = ?
        """;

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, cdAluno);
            stmt.setInt(2, cdDisciplina);

            stmt.executeUpdate();

            System.out.println("Nota excluída com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}