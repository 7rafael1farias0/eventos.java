import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Evento implements Serializable {

    // SerialVersionUID é recomendado para serialização
    private static final long serialVersionUID = 1L;

    private String nome;
    private String endereco;
    private Categoria categoria;
    private LocalDateTime horario;
    private String descricao;

    // Construtor
    public Evento(String nome, String endereco, Categoria categoria, LocalDateTime horario, String descricao) {
        this.nome = nome;
        this.endereco = endereco;
        this.categoria = categoria;
        this.horario = horario;
        this.descricao = descricao;
    }

    // --- Getters e Setters (Necessários em Java POO) ---
    public String getNome() { return nome; }
    public LocalDateTime getHorario() { return horario; }
    // ... Adicionar outros getters conforme necessário ...

    // --- Lógica de Tempo ---

    /** Informa se o evento está ocorrendo (assume duração de 2 horas). */
    public boolean estaOcorrendo() {
        LocalDateTime agora = LocalDateTime.now();
        // Evento ocorre entre o horário inicial e duas horas após
        return (agora.isEqual(horario) || agora.isAfter(horario))
                && agora.isBefore(horario.plusHours(2));
    }

    /** Informa se o evento já ocorreu (passou da sua hora de término). */
    public boolean jaOcorreu() {
        return LocalDateTime.now().isAfter(horario.plusHours(2));
    }

    // --- Sobrescrita de Métodos ---

    @Override
    public String toString() {
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String status = "";
        if (estaOcorrendo()) {
            status = " [AGORA!]";
        } else if (jaOcorreu()) {
            status = " [Ocorreu]";
        }

        // Usa o hashcode como um 'ID' simples para referência no console
        return String.format("ID: %d | Nome: %s (%s) | Horário: %s%s\n    Endereço: %s | Descrição: %s",
                this.hashCode(), nome, categoria, horario.format(formatador), status, endereco, descricao);
    }

    // É crucial implementar equals e hashCode para que as coleções funcionem corretamente.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Evento evento = (Evento) o;
        // Consideramos eventos iguais se todos os seus atributos principais coincidirem
        return Objects.equals(nome, evento.nome) &&
                Objects.equals(endereco, evento.endereco) &&
                categoria == evento.categoria &&
                Objects.equals(horario, evento.horario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, endereco, categoria, horario);
    }
}