package banco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AlunoDAO {

    public void cadastrarAluno(
            int cdAluno,
            String nmAluno,
            String mtrAluno,
            double totalNotas) {

        String sql = """
            INSERT INTO aluno
            (cdAluno, nmAluno, mtrAluno, totalNotas)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt =
                     conexao.prepareStatement(sql)) {

            stmt.setInt(1, cdAluno);
            stmt.setString(2, nmAluno);
            stmt.setString(3, mtrAluno);
            stmt.setDouble(4, totalNotas);

            stmt.executeUpdate();

            System.out.println("Aluno cadastrado com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listarAlunos() {

        String sql = """
            SELECT cdAluno, nmAluno, mtrAluno, totalNotas
            FROM aluno
            ORDER BY cdAluno
        """;

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt =
                     conexao.prepareStatement(sql);
             ResultSet resultado =
                     stmt.executeQuery()) {

            while (resultado.next()) {

                System.out.println(
                        resultado.getInt("cdAluno") + " - " +
                        resultado.getString("nmAluno") + " - " +
                        resultado.getString("mtrAluno") + " - " +
                        resultado.getDouble("totalNotas")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void excluirAluno(int cdAluno) {

        String sql = """
            DELETE FROM aluno
            WHERE cdAluno = ?
        """;

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt =
                     conexao.prepareStatement(sql)) {

            stmt.setInt(1, cdAluno);

            stmt.executeUpdate();

            System.out.println("Aluno excluído com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}