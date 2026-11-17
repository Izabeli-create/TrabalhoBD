package tela;
import banco.Conexao;
import banco.DisciplinaDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
public class TelaDisciplinas extends JFrame {
    private JTextField txtCodigo;
    private JTextField txtNome;
    private JTextField txtNumero;
    private JTable tabela;
    private DefaultTableModel modelo;
    private DisciplinaDAO dao;
    public TelaDisciplinas() {
        dao = new DisciplinaDAO();
        setTitle("Cadastro de Disciplinas");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel painelCampos = new JPanel(new GridLayout(3, 2, 10, 10));
        painelCampos.add(new JLabel("Código:"));
        txtCodigo = new JTextField();
        painelCampos.add(txtCodigo);
        painelCampos.add(new JLabel("Nome:"));
        txtNome = new JTextField();
        painelCampos.add(txtNome);
        painelCampos.add(new JLabel("Número:"));
        txtNumero = new JTextField();
        painelCampos.add(txtNumero);
        JButton btnCadastrar = new JButton("Cadastrar");
        JButton btnListar = new JButton("Listar");
        JButton btnAlterar = new JButton("Alterar");
        JButton btnExcluir = new JButton("Excluir");
        JPanel painelBotoes = new JPanel();
        painelBotoes.add(btnCadastrar);
        painelBotoes.add(btnListar);
        painelBotoes.add(btnAlterar);
        painelBotoes.add(btnExcluir);
        modelo = new DefaultTableModel(
                new Object[]{"Código", "Nome", "Número"}, 0
        );
        tabela = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabela);
        setLayout(new BorderLayout(10, 10));
        add(painelCampos, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);
        btnCadastrar.addActionListener(e -> cadastrar());
        btnListar.addActionListener(e -> listar());
        btnAlterar.addActionListener(e -> alterar());
        btnExcluir.addActionListener(e -> excluir());
        listar();
    }
    private void cadastrar() {
        try {
            int codigo = Integer.parseInt(txtCodigo.getText());
            String nome = txtNome.getText();
            int numero = Integer.parseInt(txtNumero.getText());
            dao.cadastrarDisciplina(codigo, nome, numero);
            JOptionPane.showMessageDialog(this,
                    "Disciplina cadastrada com sucesso!");
            limparCampos();
            listar();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Preencha os dados corretamente.");
        }
    }
    private void listar() {
        modelo.setRowCount(0);
        String sql = """
                SELECT cdDisciplina, nmDisciplina, nrDisciplina
                FROM disciplina
                ORDER BY cdDisciplina
                """;
        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet resultado = stmt.executeQuery()) {
            while (resultado.next()) {
                modelo.addRow(new Object[]{
                        resultado.getInt("cdDisciplina"),
                        resultado.getString("nmDisciplina"),
                        resultado.getInt("nrDisciplina")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar disciplinas.");
            e.printStackTrace();
        }
    }
    private void alterar() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma disciplina na tabela.");
            return;
        }
        int codigo = (int) tabela.getValueAt(linha, 0);
        String novoNome = JOptionPane.showInputDialog(
                this,
                "Digite o novo nome da disciplina:"
        );
        if (novoNome == null) {
            return;
        }
        String novoNumeroTexto = JOptionPane.showInputDialog(
                this,
                "Digite o novo número da disciplina:"
        );
        if (novoNumeroTexto == null) {
            return;
        }
        try {
            int novoNumero = Integer.parseInt(novoNumeroTexto);
            dao.alterarDisciplina(
                    codigo,
                    novoNome,
                    novoNumero
            );
            JOptionPane.showMessageDialog(this,
                    "Disciplina alterada com sucesso!");
            listar();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Digite um número válido.");
        }
    }
    private void excluir() {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione uma disciplina na tabela.");
            return;
        }
        int codigo = (int) tabela.getValueAt(linha, 0);
        int resposta = JOptionPane.showConfirmDialog(
                this,
                "Deseja realmente excluir esta disciplina?",
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION
        );
        if (resposta == JOptionPane.YES_OPTION) {
            dao.excluirDisciplina(codigo);
            JOptionPane.showMessageDialog(this,
                    "Disciplina excluída com sucesso!");
            listar();
        }
    }
    private void limparCampos() {
        txtCodigo.setText("");
        txtNome.setText("");
        txtNumero.setText("");
    }
}