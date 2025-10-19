import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nome;
    private String CPF;
    private String email;
    private String telefone;
    private LocalDate dataNascimento;
    private String senha;

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