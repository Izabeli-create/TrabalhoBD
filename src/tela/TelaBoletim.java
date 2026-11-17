package tela;

import banco.Conexao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TelaBoletim extends JFrame {

    private JLabel labelNome;
    private JLabel labelMatricula;
    private JTable tabela;
    private DefaultTableModel modelo;

    public TelaBoletim(int cdAluno) {

        setTitle("Boletim Escolar");
        setSize(750, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Título
        JLabel titulo = new JLabel(
                "BOLETIM ESCOLAR",
                SwingConstants.CENTER
        );

        titulo.setFont(new Font("Arial", Font.BOLD, 26));

        // Informações do aluno
        labelNome = new JLabel("Aluno: ");
        labelMatricula = new JLabel("Matrícula: ");

        labelNome.setFont(new Font("Arial", Font.BOLD, 16));
        labelMatricula.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel painelAluno = new JPanel(
                new GridLayout(2, 1, 5, 5)
        );

        painelAluno.add(labelNome);
        painelAluno.add(labelMatricula);

        // Tabela
        modelo = new DefaultTableModel(
                new Object[]{
                        "Disciplina",
                        "Professor",
                        "Nota"
                }, 0
        );

        tabela = new JTable(modelo);

        JScrollPane scroll = new JScrollPane(tabela);

        // Botão
        JButton btnAtualizar = new JButton("Atualizar");

        btnAtualizar.addActionListener(
                e -> carregarBoletim(cdAluno)
        );

        // Painel superior
        JPanel painelSuperior = new JPanel(
                new BorderLayout(10, 10)
        );

        painelSuperior.add(titulo, BorderLayout.NORTH);
        painelSuperior.add(painelAluno, BorderLayout.CENTER);

        // Painel inferior
        JPanel painelInferior = new JPanel();
        painelInferior.add(btnAtualizar);

        // Tela
        setLayout(new BorderLayout(10, 10));

        add(painelSuperior, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(painelInferior, BorderLayout.SOUTH);

        carregarBoletim(cdAluno);
    }

    private void carregarBoletim(int cdAluno) {

        modelo.setRowCount(0);

        try (Connection conexao = Conexao.conectar()) {

            // Buscar aluno
            String sqlAluno = """
                SELECT nmAluno, mtrAluno
                FROM aluno
                WHERE cdAluno = ?
            """;

            try (PreparedStatement stmt =
                         conexao.prepareStatement(sqlAluno)) {

                stmt.setInt(1, cdAluno);

                try (ResultSet resultado =
                             stmt.executeQuery()) {

                    if (resultado.next()) {

                        labelNome.setText(
                                "Aluno: "
                                        + resultado.getString("nmAluno")
                        );

                        labelMatricula.setText(
                                "Matrícula: "
                                        + resultado.getString("mtrAluno")
                        );
                    }
                }
            }

            // Buscar notas
            String sqlNotas = """
                SELECT
                    d.nmDisciplina AS disciplina,
                    p.nmProfessor AS professor,
                    n.nota AS nota
                FROM nota n
                JOIN disciplina d
                    ON n.cdDisciplina = d.cdDisciplina
                LEFT JOIN leciona l
                    ON d.cdDisciplina = l.cdDisciplina
                LEFT JOIN professor p
                    ON l.cdProfessor = p.cdProfessor
                WHERE n.cdAluno = ?
                ORDER BY d.nmDisciplina
            """;

            try (PreparedStatement stmt =
                         conexao.prepareStatement(sqlNotas)) {

                stmt.setInt(1, cdAluno);

                try (ResultSet resultado =
                             stmt.executeQuery()) {

                    while (resultado.next()) {

                        modelo.addRow(new Object[]{
                                resultado.getString("disciplina"),
                                resultado.getString("professor"),
                                resultado.getDouble("nota")
                        });
                    }
                }
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao carregar o boletim."
            );

            e.printStackTrace();
        }
    }
}