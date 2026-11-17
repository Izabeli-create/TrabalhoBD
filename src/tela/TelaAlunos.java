package tela;
import banco.AlunoDAO;
import banco.Conexao;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
public class TelaAlunos extends JFrame {
    private JTextField txtCodigo;
    private JTextField txtNome;
    private JTextField txtMatricula;
    private JComboBox<String> comboDisciplina;
    private JTextField txtNota;
    private JTable tabela;
    private DefaultTableModel modelo;
    private AlunoDAO dao;
    public TelaAlunos() {
        dao = new AlunoDAO();

        setTitle("Cadastro de Alunos");
        setSize(900, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel painelCampos = new JPanel(
                new GridLayout(5, 2, 10, 10)
        );
        painelCampos.add(new JLabel("Código:"));
        txtCodigo = new JTextField();
        painelCampos.add(txtCodigo);
        painelCampos.add(new JLabel("Nome:"));
        txtNome = new JTextField();
        painelCampos.add(txtNome);
        painelCampos.add(new JLabel("Matrícula:"));
        txtMatricula = new JTextField();
        painelCampos.add(txtMatricula);
        painelCampos.add(new JLabel("Disciplina:"));
        comboDisciplina = new JComboBox<>();
        painelCampos.add(comboDisciplina);
        painelCampos.add(new JLabel("Nota:"));
        txtNota = new JTextField();
        painelCampos.add(txtNota);
        JButton btnCadastrar = new JButton("Cadastrar");
        JButton btnListar = new JButton("Listar");
        JButton btnAlterar = new JButton("Alterar Nota");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnBoletim = new JButton("Ver Boletim");
        JPanel painelBotoes = new JPanel();
        painelBotoes.add(btnCadastrar);
        painelBotoes.add(btnListar);
        painelBotoes.add(btnAlterar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnBoletim);
        modelo = new DefaultTableModel(
                new Object[]{
                        "Código",
                        "Nome",
                        "Matrícula",
                        "Disciplina",
                        "Nota"
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
        btnAlterar.addActionListener(e -> alterar());
        btnExcluir.addActionListener(e -> excluir());
        btnBoletim.addActionListener(e -> abrirBoletim());
        listar();
    }
    private void carregarDisciplinas() {
        String sql = """
                SELECT cdDisciplina, nmDisciplina
                FROM disciplina
                ORDER BY nmDisciplina
                """;
        try (
                Connection conexao = Conexao.conectar();
                PreparedStatement stmt =
                        conexao.prepareStatement(sql);
                ResultSet resultado =
                        stmt.executeQuery()
        ) {
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
            String matricula = txtMatricula.getText();
            String disciplinaSelecionada =
                    (String) comboDisciplina.getSelectedItem();
            if (disciplinaSelecionada == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Selecione uma disciplina."
                );
                return;
            }
            int cdDisciplina = Integer.parseInt(
                    disciplinaSelecionada.split(" - ")[0]
            );
            double nota = Double.parseDouble(
                    txtNota.getText().replace(",", ".")
            );
            if (nota < 0 || nota > 10) {
                JOptionPane.showMessageDialog(
                        this,
                        "A nota deve estar entre 0 e 10."
                );
                return;
            }
            dao.cadastrarAluno(
                    codigo,
                    nome,
                    matricula,
                    nota
            );
            String sqlCursa = """
                    INSERT OR IGNORE INTO cursa
                    (cdAluno, cdDisciplina)
                    VALUES (?, ?)
                    """;
            try (
                    Connection conexao = Conexao.conectar();
                    PreparedStatement stmt =
                            conexao.prepareStatement(sqlCursa)
            ) {
                stmt.setInt(1, codigo);
                stmt.setInt(2, cdDisciplina);
                stmt.executeUpdate();
            }
            String sqlNota = """
                    INSERT OR REPLACE INTO nota
                    (cdAluno, cdDisciplina, nota)
                    VALUES (?, ?, ?)
                    """;
            try (
                    Connection conexao = Conexao.conectar();
                    PreparedStatement stmt =
                            conexao.prepareStatement(sqlNota)
            ) {
                stmt.setInt(1, codigo);
                stmt.setInt(2, cdDisciplina);
                stmt.setDouble(3, nota);
                stmt.executeUpdate();
            }
            JOptionPane.showMessageDialog(
                    this,
                    "Aluno cadastrado com sucesso!"
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
                    a.cdAluno,
                    a.nmAluno,
                    a.mtrAluno,
                    d.nmDisciplina,
                    n.nota
                FROM aluno a
                LEFT JOIN nota n
                    ON a.cdAluno = n.cdAluno
                LEFT JOIN disciplina d
                    ON n.cdDisciplina = d.cdDisciplina
                ORDER BY a.cdAluno, d.nmDisciplina
                """;
        try (
                Connection conexao = Conexao.conectar();
                PreparedStatement stmt =
                        conexao.prepareStatement(sql);
                ResultSet resultado =
                        stmt.executeQuery()
        ) {
            while (resultado.next()) {
                Object nota = resultado.getObject("nota");
                modelo.addRow(new Object[]{
                        resultado.getInt("cdAluno"),
                        resultado.getString("nmAluno"),
                        resultado.getString("mtrAluno"),
                        resultado.getString("nmDisciplina"),
                        nota
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao carregar alunos."
            );
            e.printStackTrace();
        }
    }
    private void alterar() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um aluno na tabela."
            );
            return;
        }
        Object disciplina =
                tabela.getValueAt(linha, 3);
        if (disciplina == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Esse aluno ainda não possui uma disciplina."
            );
            return;
        }
        int codigo =
                (int) tabela.getValueAt(linha, 0);
        String nomeDisciplina =
                disciplina.toString();
        String novaNotaTexto =
                JOptionPane.showInputDialog(
                        this,
                        "Digite a nova nota de "
                        + nomeDisciplina + ":"
                );
        if (novaNotaTexto == null) {
            return;
        }
        try {
            double novaNota = Double.parseDouble(
                    novaNotaTexto.replace(",", ".")
            );
            if (novaNota < 0 || novaNota > 10) {
                JOptionPane.showMessageDialog(
                        this,
                        "A nota deve estar entre 0 e 10."
                );
                return;
            }
            int cdDisciplina =
                    obterCodigoDisciplina(nomeDisciplina);
            String sql = """
                    UPDATE nota
                    SET nota = ?
                    WHERE cdAluno = ?
                    AND cdDisciplina = ?
                    """;
            try (
                    Connection conexao = Conexao.conectar();
                    PreparedStatement stmt =
                            conexao.prepareStatement(sql)
            ) {
                stmt.setDouble(1, novaNota);
                stmt.setInt(2, codigo);
                stmt.setInt(3, cdDisciplina);
                stmt.executeUpdate();
            }
            JOptionPane.showMessageDialog(
                    this,
                    "Nota alterada com sucesso!"
            );
            listar();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Digite uma nota válida."
            );
            e.printStackTrace();
        }
    }
    private int obterCodigoDisciplina(
            String nomeDisciplina) throws Exception {
        String sql = """
                SELECT cdDisciplina
                FROM disciplina
                WHERE nmDisciplina = ?
                """;
        try (
                Connection conexao = Conexao.conectar();
                PreparedStatement stmt =
                        conexao.prepareStatement(sql)
        ) {
            stmt.setString(1, nomeDisciplina);
            ResultSet resultado =
                    stmt.executeQuery();
            if (resultado.next()) {
                return resultado.getInt("cdDisciplina");
            }
        }
        throw new Exception(
                "Disciplina não encontrada."
        );
    }
    private void excluir() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um aluno na tabela."
            );
            return;
        }
        int codigo =
                (int) tabela.getValueAt(linha, 0);
        int resposta =
                JOptionPane.showConfirmDialog(
                        this,
                        "Deseja realmente excluir este aluno?",
                        "Confirmar exclusão",
                        JOptionPane.YES_NO_OPTION
                );
        if (resposta == JOptionPane.YES_OPTION) {
            dao.excluirAluno(codigo);
            JOptionPane.showMessageDialog(
                    this,
                    "Aluno excluído com sucesso!"
            );
            listar();
        }
    }
    private void limparCampos() {
        txtCodigo.setText("");
        txtNome.setText("");
        txtMatricula.setText("");
        comboDisciplina.setSelectedIndex(-1);
        txtNota.setText("");
    }
    private void abrirBoletim() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um aluno na tabela."
            );
            return;
        }
        int codigo =
                (int) tabela.getValueAt(linha, 0);
        TelaBoletim boletim =
                new TelaBoletim(codigo);
        boletim.setVisible(true);
    }
}