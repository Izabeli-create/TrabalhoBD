package banco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProfessorDAO {

    public void cadastrarProfessor(int codigo, String nome, double totalNotas,
                                   String telefone, String email) {

        String sql = """
            INSERT INTO professor
            (cdProfessor, nmProfessor, totalNotas, cdCtr, emailProfessor)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, codigo);
            stmt.setString(2, nome);
            stmt.setDouble(3, totalNotas);
            stmt.setString(4, telefone);
            stmt.setString(5, email);

            stmt.executeUpdate();

            System.out.println("Professor cadastrado com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listarProfessores() {

        String sql = """
            SELECT cdProfessor, nmProfessor, totalNotas,
                   cdCtr, emailProfessor
            FROM professor
            ORDER BY cdProfessor
        """;

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet resultado = stmt.executeQuery()) {

            while (resultado.next()) {

                System.out.println(
                    resultado.getInt("cdProfessor") + " - " +
                    resultado.getString("nmProfessor") + " - " +
                    resultado.getDouble("totalNotas") + " - " +
                    resultado.getString("cdCtr") + " - " +
                    resultado.getString("emailProfessor")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void alterarNota(int codigo, double novaNota) {

        String sql = """
            UPDATE professor
            SET totalNotas = ?
            WHERE cdProfessor = ?
        """;

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setDouble(1, novaNota);
            stmt.setInt(2, codigo);

            stmt.executeUpdate();

            System.out.println("Nota do professor alterada com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void excluirProfessor(int codigo) {

        String sql = """
            DELETE FROM professor
            WHERE cdProfessor = ?
        """;

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, codigo);

            stmt.executeUpdate();

            System.out.println("Professor excluído com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}