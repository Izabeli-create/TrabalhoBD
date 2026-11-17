package banco;

import java.sql.Connection;
import java.sql.Statement;

public class CriarBanco {

    public static void main(String[] args) {

        try (Connection conexao = Conexao.conectar();
             Statement stmt = conexao.createStatement()) {

            stmt.execute("PRAGMA foreign_keys = ON");

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS aluno (
                    cdAluno INTEGER PRIMARY KEY,
                    nmAluno VARCHAR(50),
                    mtrAluno VARCHAR(20),
                    totalNotas DECIMAL(5,2)
                )
            """);
            
            stmt.execute("""
            	    CREATE TABLE IF NOT EXISTS nota (
            	        cdAluno INTEGER NOT NULL,
            	        cdDisciplina INTEGER NOT NULL,
            	        nota DECIMAL(5,2),
            	        PRIMARY KEY (cdAluno, cdDisciplina),
            	        FOREIGN KEY (cdAluno) REFERENCES aluno(cdAluno),
            	        FOREIGN KEY (cdDisciplina) REFERENCES disciplina(cdDisciplina)
            	    )
            	""");

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS escola (
                    cdEscola INTEGER PRIMARY KEY,
                    nmEscola VARCHAR(50)
                )
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS disciplina (
                    cdDisciplina INTEGER PRIMARY KEY,
                    nmDisciplina VARCHAR(50),
                    nrDisciplina INTEGER
                )
            """);
            
            stmt.execute("""
            	    INSERT OR IGNORE INTO nota
            	    VALUES (1, 1, 9.5)
            	""");

            	stmt.execute("""
            	    INSERT OR IGNORE INTO nota
            	    VALUES (1, 2, 9.5)
            	""");

            	stmt.execute("""
            	    INSERT OR IGNORE INTO nota
            	    VALUES (2, 1, 8.7)
            	""");

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS professor (
                    cdProfessor INTEGER PRIMARY KEY,
                    nmProfessor VARCHAR(50),
                    totalNotas DECIMAL(5,2),
                    cdCtr VARCHAR(20),
                    emailProfessor VARCHAR(100)
                )
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS diretor (
                    cdDiretor INTEGER PRIMARY KEY,
                    nmDiretor VARCHAR(50),
                    emailDiretor VARCHAR(100)
                )
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS matricula (
                    cdMatricula INTEGER PRIMARY KEY,
                    tpMatricula VARCHAR(30),
                    cdAluno INTEGER NOT NULL,
                    cdEscola INTEGER NOT NULL,
                    FOREIGN KEY (cdAluno) REFERENCES aluno(cdAluno),
                    FOREIGN KEY (cdEscola) REFERENCES escola(cdEscola)
                )
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS cursa (
                    cdAluno INTEGER NOT NULL,
                    cdDisciplina INTEGER NOT NULL,
                    hrCursa TIME,
                    diaCursa VARCHAR(20),
                    PRIMARY KEY (cdAluno, cdDisciplina),
                    FOREIGN KEY (cdAluno) REFERENCES aluno(cdAluno),
                    FOREIGN KEY (cdDisciplina) REFERENCES disciplina(cdDisciplina)
                )
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS leciona (
                    cdProfessor INTEGER NOT NULL,
                    cdDisciplina INTEGER NOT NULL,
                    hrLeciona TIME,
                    diaLeciona VARCHAR(20),
                    PRIMARY KEY (cdProfessor, cdDisciplina),
                    FOREIGN KEY (cdProfessor) REFERENCES professor(cdProfessor),
                    FOREIGN KEY (cdDisciplina) REFERENCES disciplina(cdDisciplina)
                )
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS contrata (
                    cdContrato INTEGER PRIMARY KEY,
                    nmProfessor VARCHAR(50),
                    cdProfessor INTEGER NOT NULL,
                    cdEscola INTEGER NOT NULL,
                    FOREIGN KEY (cdProfessor) REFERENCES professor(cdProfessor),
                    FOREIGN KEY (cdEscola) REFERENCES escola(cdEscola)
                )
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS possui (
                    cdPossui INTEGER PRIMARY KEY,
                    cdEscola INTEGER NOT NULL,
                    cdDiretor INTEGER NOT NULL,
                    FOREIGN KEY (cdEscola) REFERENCES escola(cdEscola),
                    FOREIGN KEY (cdDiretor) REFERENCES diretor(cdDiretor)
                )
            """);

            stmt.execute("INSERT OR IGNORE INTO aluno VALUES (1, 'Alana', '2024001', 9.5)");
            stmt.execute("INSERT OR IGNORE INTO aluno VALUES (2, 'Danilo', '2024002', 8.7)");

            stmt.execute("INSERT OR IGNORE INTO escola VALUES (1, 'Escola Estadual')");

            stmt.execute("INSERT OR IGNORE INTO diretor VALUES (1, 'Carlos', 'carlos@escola.com')");

            stmt.execute("""
                INSERT OR IGNORE INTO professor
                VALUES (1, 'Marcos', 9.8, '11999999999', 'marcos@escola.com')
            """);

            stmt.execute("""
                INSERT OR IGNORE INTO professor
                VALUES (2, 'Fernanda', 9.5, '11888888888', 'fernanda@escola.com')
            """);

            stmt.execute("INSERT OR IGNORE INTO disciplina VALUES (1, 'Banco de Dados', 1)");
            stmt.execute("INSERT OR IGNORE INTO disciplina VALUES (2, 'Programação', 2)");

            stmt.execute("INSERT OR IGNORE INTO matricula VALUES (1, 'Regular', 1, 1)");
            stmt.execute("INSERT OR IGNORE INTO matricula VALUES (2, 'Regular', 2, 1)");

            stmt.execute("""
                INSERT OR IGNORE INTO cursa
                VALUES (1, 1, '08:00:00', 'Segunda')
            """);

            stmt.execute("""
                INSERT OR IGNORE INTO cursa
                VALUES (1, 2, '10:00:00', 'Quarta')
            """);

            stmt.execute("""
                INSERT OR IGNORE INTO cursa
                VALUES (2, 1, '08:00:00', 'Segunda')
            """);

            stmt.execute("""
                INSERT OR IGNORE INTO leciona
                VALUES (1, 1, '08:00:00', 'Segunda')
            """);

            stmt.execute("""
                INSERT OR IGNORE INTO leciona
                VALUES (2, 2, '10:00:00', 'Quarta')
            """);

            stmt.execute("""
                INSERT OR IGNORE INTO contrata
                VALUES (1, 'Marcos', 1, 1)
            """);

            stmt.execute("""
                INSERT OR IGNORE INTO contrata
                VALUES (2, 'Fernanda', 2, 1)
            """);

            stmt.execute("""
                INSERT OR IGNORE INTO possui
                VALUES (1, 1, 1)
            """);

            System.out.println("Banco de dados criado com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}