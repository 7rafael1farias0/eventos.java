import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GerenciadorEventos {

    private static final String NOME_ARQUIVO = "events.data";
    private List<Evento> eventosCadastrados;

    public GerenciadorEventos() {
        carregarEventos(); // Carrega os dados ao iniciar
    }

    // --- Persistência de Dados (Serialização) ---

    @SuppressWarnings("unchecked") // Para suprimir o warning da conversão
    public void carregarEventos() {
        File arquivo = new File(NOME_ARQUIVO);
        if (arquivo.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
                // Lê o objeto (a lista) do arquivo
                eventosCadastrados = (List<Evento>) ois.readObject();
                System.out.println("\nEventos carregados com sucesso do arquivo " + NOME_ARQUIVO + ".");
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("\nERRO ao carregar eventos. Iniciando com lista vazia. " + e.getMessage());
                eventosCadastrados = new ArrayList<>();
            }
        } else {
            eventosCadastrados = new ArrayList<>();
            System.out.println("\nArquivo de eventos não encontrado. Iniciando com lista vazia.");
        }
    }

    public void salvarEventos() {
        try (ObjectOutputStream oos = new  ObjectOutputStream(new FileOutputStream(NOME_ARQUIVO))) {
            // Escreve o objeto (a lista) no arquivo
            oos.writeObject(eventosCadastrados);
            System.out.println("Dados salvos com sucesso em " + NOME_ARQUIVO + ".");
        } catch (IOException e) {
            System.err.println("ERRO ao salvar eventos: " + e.getMessage());
        }
    }

    // --- Métodos de Gerenciamento e Consulta ---

    public void cadastrarEvento(Evento evento) {
        eventosCadastrados.add(evento);
        salvarEventos(); // Salva a alteração
    }

    /** Retorna todos os eventos, ordenados por horário (mais próximo primeiro). */
    public List<Evento> consultarTodos() {
        // Ordena usando o horário do evento
        return eventosCadastrados.stream()
                .sorted(Comparator.comparing(Evento::getHorario))
                .collect(Collectors.toList());
    }

    /** Retorna apenas os eventos futuros (próximos). */
    public List<Evento> consultarProximos() {
        return eventosCadastrados.stream()
                .filter(e -> !e.jaOcorreu())
                .sorted(Comparator.comparing(Evento::getHorario))
                .collect(Collectors.toList());
    }

    /** Retorna os eventos ocorrendo agora. */
    public List<Evento> consultarOcorrendoAgora() {
        return eventosCadastrados.stream()
                .filter(Evento::estaOcorrendo)
                .collect(Collectors.toList());
    }

    /** Retorna os eventos que já ocorreram. */
    public List<Evento> consultarOcorridos() {
        return eventosCadastrados.stream()
                .filter(Evento::jaOcorreu)
                .sorted(Comparator.comparing(Evento::getHorario).reversed()) // Mais recente primeiro
                .collect(Collectors.toList());
    }

    /** Busca um evento pelo seu HashCode (usado como 'ID' simples no console). */
    public Evento buscarEventoPorHashCode(int hashCode) {
        return eventosCadastrados.stream()
                .filter(e -> e.hashCode() == hashCode)
                .findFirst()
                .orElse(null);
    }
}