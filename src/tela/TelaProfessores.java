package tela;
import banco.ProfessorDAO;
import banco.Conexao;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
public class TelaProfessores extends JFrame {
    private JTextField txtCodigo;
    private JTextField txtNome;
    private JComboBox<String> comboDisciplina;
    private JTextField txtTelefone;
    private JTextField txtEmail;
    private JTable tabela;
    private DefaultTableModel modelo;
    private ProfessorDAO dao;
    public TelaProfessores() {
        dao = new ProfessorDAO();
        setTitle("Cadastro de Professores");
        setSize(900, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel painelCampos = new JPanel(new GridLayout(5, 2, 10, 10));
        painelCampos.add(new JLabel("Código:"));
        txtCodigo = new JTextField();
        painelCampos.add(txtCodigo);
        painelCampos.add(new JLabel("Nome:"));
        txtNome = new JTextField();
        painelCampos.add(txtNome);
        painelCampos.add(new JLabel("Disciplina que leciona:"));
        comboDisciplina = new JComboBox<>();
        painelCampos.add(comboDisciplina);
        painelCampos.add(new JLabel("Telefone:"));
        txtTelefone = new JTextField();
        painelCampos.add(txtTelefone);
        painelCampos.add(new JLabel("E-mail:"));
        txtEmail = new JTextField();
        painelCampos.add(txtEmail);
        JButton btnCadastrar = new JButton("Cadastrar");
        JButton btnListar = new JButton("Listar");
        JButton btnExcluir = new JButton("Excluir");
        JPanel painelBotoes = new JPanel();
        painelBotoes.add(btnCadastrar);
        painelBotoes.add(btnListar);
        painelBotoes.add(btnExcluir);
        modelo = new DefaultTableModel(
                new Object[]{
                        "Código",
                        "Nome",
                        "Disciplina",
                        "Telefone",
                        "E-mail"
                }, 0
        );
        tabela = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabela);
        setLayout(new BorderLayout(10, 10));
        add(painelCampos, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);
        carregarDisciplinas();
        btnCadastrar.addActionListener(e -> cadastrar());
        btnListar.addActionListener(e -> listar());
        btnExcluir.addActionListener(e -> excluir());
        listar();
    }
    private void carregarDisciplinas() {
        String sql = """
                SELECT cdDisciplina, nmDisciplina
                FROM disciplina
                ORDER BY nmDisciplina
                """;
        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet resultado = stmt.executeQuery()) {
            comboDisciplina.removeAllItems();
            while (resultado.next()) {
                comboDisciplina.addItem(
                        resultado.getInt("cdDisciplina")
                        + " - "
                        + resultado.getString("nmDisciplina")
                );
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao carregar disciplinas."
            );
            e.printStackTrace();
        }
    }
    private void cadastrar() {
        try {
            int codigo = Integer.parseInt(
                    txtCodigo.getText()
            );
            String nome = txtNome.getText();
            String disciplinaSelecionada =
                    (String) comboDisciplina.getSelectedItem();
            String telefone = txtTelefone.getText();
            String email = txtEmail.getText();
            if (disciplinaSelecionada == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Cadastre uma disciplina primeiro."
                );
                return;
            }
            int cdDisciplina = Integer.parseInt(
                    disciplinaSelecionada.split(" - ")[0]
            );
            dao.cadastrarProfessor(
                    codigo,
                    nome,
                    0,
                    telefone,
                    email
            );
            String sql = """
                    INSERT OR IGNORE INTO leciona
                    (cdProfessor, cdDisciplina)
                    VALUES (?, ?)
                    """;
            try (Connection conexao = Conexao.conectar();
                 PreparedStatement stmt =
                         conexao.prepareStatement(sql)) {
                stmt.setInt(1, codigo);
                stmt.setInt(2, cdDisciplina);
                stmt.executeUpdate();
            }
            JOptionPane.showMessageDialog(
                    this,
                    "Professor cadastrado com sucesso!"
            );
            limparCampos();
            listar();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Preencha os dados corretamente."
            );
            e.printStackTrace();
        }
    }
    private void listar() {
        modelo.setRowCount(0);
        String sql = """
                SELECT
                    p.cdProfessor,
                    p.nmProfessor,
                    d.nmDisciplina,
                    p.cdCtr,
                    p.emailProfessor
                FROM professor p
                LEFT JOIN leciona l
                    ON p.cdProfessor = l.cdProfessor
                LEFT JOIN disciplina d
                    ON l.cdDisciplina = d.cdDisciplina
                ORDER BY p.cdProfessor
                """;
        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt =
                     conexao.prepareStatement(sql);
             ResultSet resultado = stmt.executeQuery()) {
            while (resultado.next()) {
                modelo.addRow(new Object[]{
                        resultado.getInt("cdProfessor"),
                        resultado.getString("nmProfessor"),
                        resultado.getString("nmDisciplina"),
                        resultado.getString("cdCtr"),
                        resultado.getString("emailProfessor")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao carregar professores."
            );
            e.printStackTrace();
        }
    }
    private void excluir() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um professor na tabela."
            );
            return;
        }
        int codigo = (int) tabela.getValueAt(linha, 0);
        int resposta = JOptionPane.showConfirmDialog(
                this,
                "Deseja realmente excluir este professor?",
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION
        );
        if (resposta == JOptionPane.YES_OPTION) {
            dao.excluirProfessor(codigo);
            JOptionPane.showMessageDialog(
                    this,
                    "Professor excluído com sucesso!"
            );
            listar();
        }
    }
    private void limparCampos() {
        txtCodigo.setText("");
        txtNome.setText("");
        comboDisciplina.setSelectedIndex(-1);
        txtTelefone.setText("");
        txtEmail.setText("");
    }
}