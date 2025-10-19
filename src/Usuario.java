import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nome;        // Atributo 1
    private String CPF;         // Atributo 2
    private String email;       // Atributo 3
    private String telefone;    // Atributo 4
    private LocalDate dataNascimento; // Atributo 5
    private String senha;      // Atributo extra

    private List<Evento> eventosConfirmados;

    public Usuario(String nome, String CPF, String email, String telefone, LocalDate dataNascimento, String senha) {
        this.nome = nome;
        this.CPF = CPF;
        this.email = email;
        this.telefone = telefone;
        this.dataNascimento = dataNascimento;
        this.senha = senha;
        this.eventosConfirmados = new ArrayList<>();
    }

    // --- Getters (Mínimos para o console) ---
    public String getNome() { return nome; }
    public List<Evento> getEventosConfirmados() { return eventosConfirmados; }

    // --- Métodos de Ação ---
    public void confirmarPresenca(Evento evento) {
        if (evento != null && !eventosConfirmados.contains(evento)) {
            eventosConfirmados.add(evento);
            System.out.println("Presença confirmada no evento: " + evento.getNome());
        } else {
            System.out.println("Você já está participando deste evento.");
        }
    }

    public void cancelarPresenca(Evento evento) {
        if (evento != null && eventosConfirmados.remove(evento)) {
            System.out.println("Participação cancelada no evento: " + evento.getNome());
        } else {
            System.out.println("Erro: Não foi possível cancelar a participação neste evento.");
        }
    }
}