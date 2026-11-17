package tela;
import javax.swing.*;
import java.awt.*;
public class TelaPrincipal extends JFrame {
    public TelaPrincipal() {
        setTitle("Sistema Escolar");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        JLabel titulo = new JLabel("SISTEMA ESCOLAR", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        JButton btnAlunos = new JButton("Alunos");
        JButton btnProfessores = new JButton("Professores");
        JButton btnDisciplinas = new JButton("Disciplinas");
        JButton btnBI = new JButton("BI / WEKA");
        btnAlunos.addActionListener(e -> {
            TelaAlunos telaAlunos = new TelaAlunos();
            telaAlunos.setVisible(true);
        });
        btnProfessores.addActionListener(e -> {
            TelaProfessores telaProfessores = new TelaProfessores();
            telaProfessores.setVisible(true);
        });
        btnDisciplinas.addActionListener(e -> {
            TelaDisciplinas telaDisciplinas = new TelaDisciplinas();
            telaDisciplinas.setVisible(true);
        });
        btnBI.addActionListener(e -> {
            TelaBI telaBI = new TelaBI();
            telaBI.setVisible(true);
        });
        JPanel painelBotoes = new JPanel();
        painelBotoes.setLayout(new GridLayout(4, 1, 10, 10));
        painelBotoes.add(btnAlunos);
        painelBotoes.add(btnProfessores);
        painelBotoes.add(btnDisciplinas);
        painelBotoes.add(btnBI);
        setLayout(new BorderLayout(20, 20));
        add(titulo, BorderLayout.NORTH);
        add(painelBotoes, BorderLayout.CENTER);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaPrincipal tela = new TelaPrincipal();
            tela.setVisible(true);
        });
    }
}