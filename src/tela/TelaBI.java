package tela;
import banco.Conexao;
import banco.GerarARFF;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
public class TelaBI extends JFrame {
    private JTable tabela;
    private DefaultTableModel modelo;
    public TelaBI() {
        setTitle("BI - Dados Escolares");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        modelo = new DefaultTableModel(
                new Object[]{
                        "Aluno",
                        "Matrícula",
                        "Escola",
                        "Disciplina",
                        "Professor",
                        "Nota"
                }, 0
        );
        tabela = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabela);
        JButton btnAtualizar = new JButton("Atualizar Dados");
        JButton btnGerarARFF = new JButton("Gerar ARFF");
        JPanel painelBotoes = new JPanel();
        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnGerarARFF);
        setLayout(new BorderLayout(10, 10));
        add(scroll, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);
        btnAtualizar.addActionListener(e -> carregarDados());
        btnGerarARFF.addActionListener(e -> gerarARFF());
        carregarDados();
    }
    private void carregarDados() {
        modelo.setRowCount(0);
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
        try (
                Connection conexao = Conexao.conectar();
                PreparedStatement stmt =
                        conexao.prepareStatement(sql);
                ResultSet resultado =
                        stmt.executeQuery()
        ) {
            while (resultado.next()) {
                modelo.addRow(new Object[]{
                        resultado.getString("aluno"),
                        resultado.getString("matricula"),
                        resultado.getString("escola"),
                        resultado.getString("disciplina"),
                        resultado.getString("professor"),
                        resultado.getDouble("nota")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao carregar os dados do BI."
            );
            e.printStackTrace();
        }
    }
    private void gerarARFF() {
        try {
            GerarARFF.executar();
            JOptionPane.showMessageDialog(
                    this,
                    "Arquivo dados_escola.arff gerado com sucesso!"
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao gerar o arquivo ARFF."
            );
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaBI tela = new TelaBI();
            tela.setVisible(true);
        });
    }
}