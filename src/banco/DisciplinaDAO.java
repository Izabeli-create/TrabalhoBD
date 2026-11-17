package banco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DisciplinaDAO {

    public void cadastrarDisciplina(int codigo, String nome, int numero) {

        String sql = """
            INSERT INTO disciplina
            (cdDisciplina, nmDisciplina, nrDisciplina)
            VALUES (?, ?, ?)
        """;

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, codigo);
            stmt.setString(2, nome);
            stmt.setInt(3, numero);

            stmt.executeUpdate();

            System.out.println("Disciplina cadastrada com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listarDisciplinas() {

        String sql = """
            SELECT cdDisciplina, nmDisciplina, nrDisciplina
            FROM disciplina
            ORDER BY cdDisciplina
        """;

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet resultado = stmt.executeQuery()) {

            while (resultado.next()) {

                System.out.println(
                    resultado.getInt("cdDisciplina") + " - " +
                    resultado.getString("nmDisciplina") + " - " +
                    resultado.getInt("nrDisciplina")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void alterarDisciplina(int codigo, String novoNome, int novoNumero) {

        String sql = """
            UPDATE disciplina
            SET nmDisciplina = ?, nrDisciplina = ?
            WHERE cdDisciplina = ?
        """;

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, novoNome);
            stmt.setInt(2, novoNumero);
            stmt.setInt(3, codigo);

            stmt.executeUpdate();

            System.out.println("Disciplina alterada com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void excluirDisciplina(int codigo) {

        String sql = """
            DELETE FROM disciplina
            WHERE cdDisciplina = ?
        """;

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, codigo);

            stmt.executeUpdate();

            System.out.println("Disciplina excluída com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}