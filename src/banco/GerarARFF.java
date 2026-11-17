package banco;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GerarARFF {

    public static void executar() {

        String sql = """
            SELECT
                a.cdAluno,
                a.nmAluno,
                a.mtrAluno,
                e.nmEscola,
                d.nmDisciplina,
                p.nmProfessor,
                c.diaCursa,
                c.hrCursa,
                n.nota
            FROM aluno a
            JOIN matricula m ON a.cdAluno = m.cdAluno
            JOIN escola e ON m.cdEscola = e.cdEscola
            JOIN cursa c ON a.cdAluno = c.cdAluno
            JOIN nota n
            ON n.cdAluno = a.cdAluno
            AND n.cdDisciplina = c.cdDisciplina
            JOIN disciplina d ON c.cdDisciplina = d.cdDisciplina
            JOIN leciona l ON d.cdDisciplina = l.cdDisciplina
            JOIN professor p ON l.cdProfessor = p.cdProfessor
            ORDER BY a.nmAluno, d.nmDisciplina
        """;

        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet resultado = stmt.executeQuery();
             PrintWriter arquivo = new PrintWriter(
                     new FileWriter("dados_escola.arff"))) {

            arquivo.println("@relation dados_escola");
            arquivo.println();

            arquivo.println("@attribute cdAluno numeric");
            arquivo.println("@attribute nmAluno string");
            arquivo.println("@attribute mtrAluno string");
            arquivo.println("@attribute escola string");
            arquivo.println("@attribute disciplina string");
            arquivo.println("@attribute professor string");
            arquivo.println("@attribute dia string");
            arquivo.println("@attribute horario string");
            arquivo.println("@attribute nota numeric");
            arquivo.println("@attribute desempenho {Baixo,Medio,Alto}");

            arquivo.println();
            arquivo.println("@data");

            int quantidade = 0;

            while (resultado.next()) {
            	
            	quantidade++;
                
            	String nome = resultado.getString("nmAluno");
            	if (nome == null) nome = "Desconhecido";
            	nome = nome.replace(" ", "_");

            	String escola = resultado.getString("nmEscola");
            	if (escola == null) escola = "Desconhecida";
            	escola = escola.replace(" ", "_");

            	String disciplina = resultado.getString("nmDisciplina");
            	if (disciplina == null) disciplina = "Desconhecida";
            	disciplina = disciplina.replace(" ", "_");

            	String professor = resultado.getString("nmProfessor");
            	if (professor == null) professor = "Desconhecido";
            	professor = professor.replace(" ", "_");

            	String dia = resultado.getString("diaCursa");
            	if (dia == null) dia = "Nao_informado";
            	dia = dia.replace(" ", "_");

                String horario = resultado.getString("hrCursa");

                double nota = resultado.getDouble("nota");

                String desempenho;

                if (nota >= 9) {
                    desempenho = "Alto";
                } else if (nota >= 7) {
                    desempenho = "Medio";
                } else {
                    desempenho = "Baixo";
                }

                arquivo.println(
                    resultado.getInt("cdAluno") + "," +
                    nome + "," +
                    resultado.getString("mtrAluno") + "," +
                    escola + "," +
                    disciplina + "," +
                    professor + "," +
                    dia + "," +
                    horario + "," +
                    nota + "," +
                    desempenho
                );
            }
            
            System.out.println("Quantidade encontrada: " + quantidade);

            System.out.println(
                    "Arquivo dados_escola.arff criado com sucesso!"
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}