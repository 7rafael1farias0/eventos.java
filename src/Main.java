import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    private static Usuario usuarioAtual;
    private static GerenciadorEventos gerenciador;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        gerenciador = new GerenciadorEventos();

        inicializarDadosSeVazio();

        System.out.println("--- Bem-vindo ao sistema de eventos da cidade ---");
        cadastrarUsuario();
        menuPrincipal();
    }



    private static void cadastrarUsuario() {
        System.out.println("\nPrimeiro, vamos cadastrar você!");

        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.println("CPF: ");
        String CPF = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();

        System.out.println("senha: ");
        String senha = scanner.nextLine();

        LocalDate dataNascimento = LocalDate.of(1990, 1, 1);

        usuarioAtual = new Usuario(nome, CPF, email, telefone, dataNascimento, senha);
        System.out.println("\nUsuário " + usuarioAtual.getNome() + " cadastrado com sucesso!");
    }

    private static void menuPrincipal() {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n--- Menu Principal ---");
            System.out.println("1. Consultar Eventos e Participar");
            System.out.println("2. Minha Agenda (Eventos Confirmados)");
            System.out.println("3. Cadastrar Novo Evento (Admin)");
            System.out.println("0. Sair e Salvar Dados");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());
                switch (opcao) {
                    case 1:
                        consultarEventosEPresenca();
                        break;
                    case 2:
                        menuMinhaAgenda();
                        break;
                    case 3:
                        cadastrarNovoEvento();
                        break;
                    case 0:
                        gerenciador.salvarEventos();
                        System.out.println("Obrigado por usar o sistema! Encerrando.");
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, digite o número da opção.");
                opcao = -1;
            }
        }
    }

    private static void cadastrarNovoEvento() {
        System.out.println("\n--- Cadastro de Novo Evento ---");

        System.out.print("Nome do Evento: ");
        String nome = scanner.nextLine();

        System.out.print("Endereço: ");
        String endereco = scanner.nextLine();

        Categoria categoria = lerCategoria();

        LocalDateTime horario = lerHorario();

        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();

        Evento novoEvento = new Evento(nome, endereco, categoria, horario, descricao);
        gerenciador.cadastrarEvento(novoEvento);
        System.out.println("\nEvento cadastrado e salvo com sucesso!");
    }

    private static void consultarEventosEPresenca() {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n--- Consulta de Eventos ---");
            System.out.println("1. Próximos Eventos (em ordem)");
            System.out.println("2. Eventos Ocorrendo Agora");
            System.out.println("3. Eventos que Já Ocorreram");
            System.out.println("4. Confirmar Participação em um Evento");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());
                List<Evento> lista;
                switch (opcao) {
                    case 1:
                        lista = gerenciador.consultarProximos();
                        exibirListaDeEventos(lista, "Próximos Eventos");
                        break;
                    case 2:
                        lista = gerenciador.consultarOcorrendoAgora();
                        exibirListaDeEventos(lista, "Eventos Ocorrendo Agora");
                        break;
                    case 3:
                        lista = gerenciador.consultarOcorridos();
                        exibirListaDeEventos(lista, "Eventos Passados");
                        break;
                    case 4:
                        confirmarParticipacao();
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, digite o número da opção.");
            }
        }
    }

    private static void menuMinhaAgenda() {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("\n--- Minha Agenda: Eventos Confirmados ---");
            List<Evento> confirmados = usuarioAtual.getEventosConfirmados();

            if (confirmados.isEmpty()) {
                System.out.println("Você ainda não confirmou presença em nenhum evento.");
            } else {
                exibirListaDeEventos(confirmados, "Seus Eventos Confirmados");
            }

            System.out.println("\n1. Cancelar Participação em um Evento");
            System.out.println("0. Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());
                if (opcao == 1) {
                    cancelarParticipacao();
                } else if (opcao != 0) {
                    System.out.println("Opção inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, digite o número da opção.");
            }
        }
    }

    // --- Métodos de Apoio ---

    private static void exibirListaDeEventos(List<Evento> lista, String titulo) {
        System.out.println("\n--- " + titulo + " (" + lista.size() + " eventos) ---");
        if (lista.isEmpty()) {
            System.out.println("Nenhum evento encontrado nesta categoria.");
            return;
        }
        for (Evento evento : lista) {
            System.out.println(evento);
            System.out.println("----------------------------------------");
        }
    }

    private static void confirmarParticipacao() {
        System.out.println("\n--- CONFIRMAR PARTICIPAÇÃO ---");
        exibirListaDeEventos(gerenciador.consultarProximos(), "Eventos Disponíveis (Próximos)");

        System.out.print("Digite o ID do evento para confirmar presença: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Evento evento = gerenciador.buscarEventoPorHashCode(id);
            if (evento != null) {
                usuarioAtual.confirmarPresenca(evento);
            } else {
                System.out.println("Evento com ID " + id + " não encontrado.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID inválido. Por favor, digite um número.");
        }
    }

    private static void cancelarParticipacao() {
        System.out.println("\n--- CANCELAR PARTICIPAÇÃO ---");
        List<Evento> confirmados = usuarioAtual.getEventosConfirmados();
        if (confirmados.isEmpty()) {
            System.out.println("Você não tem eventos para cancelar.");
            return;
        }

        exibirListaDeEventos(confirmados, "Seus Eventos Confirmados");

        System.out.print("Digite o ID do evento para CANCELAR presença: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Evento eventoParaCancelar = gerenciador.buscarEventoPorHashCode(id);


            if (eventoParaCancelar != null) {
                usuarioAtual.cancelarPresenca(eventoParaCancelar);
            } else {
                System.out.println("Evento com ID " + id + " não encontrado.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID inválido. Por favor, digite um número.");
        }
    }


    private static void inicializarDadosSeVazio() {

        int anoBase = 2025;


        if (gerenciador.consultarTodos().isEmpty()) {
            System.out.println("\n--- Inicializando o sistema com a lista de eventos de Porto Alegre... ---");

            DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            // --- CATEGORIA FESTA ---
            gerenciador.cadastrarEvento(new Evento("1KILO", "Opinião - Porto Alegre, RS", Categoria.FESTASESHOWS, LocalDateTime.of(anoBase, 11, 1, 23, 0), "Opinião - Porto Alegre, RS"));
            gerenciador.cadastrarEvento(new Evento("FESTA DA TAYLOR", "Opinião Bar - Porto Alegre, RS", Categoria.FESTASESHOWS, LocalDateTime.of(anoBase, 12, 5, 23, 0), "Festa temática"));
            gerenciador.cadastrarEvento(new Evento("MC LUANNA", "Opinião - Porto Alegre, RS", Categoria.FESTASESHOWS, LocalDateTime.of(anoBase, 10, 11, 20, 0), "Show de MC Luanna"));
            gerenciador.cadastrarEvento(new Evento("ZAZ", "Araújo Vianna - Porto Alegre, RS", Categoria.FESTASESHOWS, LocalDateTime.of(2026, 3, 3, 21, 0), "Show internacional"));
            gerenciador.cadastrarEvento(new Evento("PAGODE 90 DO OPINIÃO", "Opinião Bar - Porto Alegre, RS", Categoria.FESTASESHOWS, LocalDateTime.of(anoBase, 10, 25, 23, 0), "Clássicos do Pagode"));
            gerenciador.cadastrarEvento(new Evento("FBC - LANÇAMENTO ASSALTOS & BATIDAS", "Opinião - Porto Alegre, RS", Categoria.FESTASESHOWS, LocalDateTime.of(anoBase, 11, 8, 21, 0), "Lançamento de álbum"));
            gerenciador.cadastrarEvento(new Evento("ANA FRANGO ELÉTRICO", "Opinião - Porto Alegre, RS", Categoria.FESTASESHOWS, LocalDateTime.of(anoBase, 12, 13, 21, 0), "Show de Ana Frango Elétrico"));
            gerenciador.cadastrarEvento(new Evento("SULHOF 2025", "Araújo Vianna - Porto Alegre, RS", Categoria.FESTASESHOWS, LocalDateTime.of(anoBase, 10, 19, 9, 0), "Festival de Música"));
            gerenciador.cadastrarEvento(new Evento("BAILE EMO :: HALLOWEEN OPEN BAR", "Opinião Bar - Porto Alegre, RS", Categoria.FESTASESHOWS, LocalDateTime.of(anoBase, 10, 18, 23, 0), "Festa temática de Halloween"));
            gerenciador.cadastrarEvento(new Evento("VITOR KLEY", "Araújo Vianna - Porto Alegre, RS", Categoria.FESTASESHOWS, LocalDateTime.of(anoBase, 11, 1, 21, 0), "Show do Vitor Kley"));
            gerenciador.cadastrarEvento(new Evento("MASSACRATION", "Opinião - Porto Alegre, RS", Categoria.FESTASESHOWS, LocalDateTime.of(anoBase, 12, 6, 20, 30), "Show de Metal"));
            gerenciador.cadastrarEvento(new Evento("TOMA! :: POP & FUNK :: 11.10 @CUCKO", "CUCKO - Porto Alegre, RS", Categoria.FESTASESHOWS, LocalDateTime.of(anoBase, 10, 11, 22, 0), "Noite de Pop e Funk"));
            gerenciador.cadastrarEvento(new Evento("mEMOries: 20 ANOS DE TWILIGHT SAGA + PARAMORE", "CUCKO - Porto Alegre, RS", Categoria.FESTASESHOWS, LocalDateTime.of(anoBase, 10, 10, 22, 0), "Festa temática de sagas"));
            gerenciador.cadastrarEvento(new Evento("HARRY ROCKET - CLÁSSICOS DO ROCK", "NOSSO TAP ROOM - Porto Alegre, RS", Categoria.FESTASESHOWS, LocalDateTime.of(anoBase, 10, 25, 18, 0), "Clássicos do Rock"));
            gerenciador.cadastrarEvento(new Evento("Festa Goodbye Lenin", "Bar Ocidente - Porto Alegre, RS", Categoria.FESTASESHOWS, LocalDateTime.of(anoBase, 10, 10, 23, 0), "Festa no Ocidente"));
            gerenciador.cadastrarEvento(new Evento("GUSTAVO LINS E RDS DO DECK", "Av. Baltazar de Oliveira Garcia, 3868 - Porto Alegre, RS", Categoria.FESTASESHOWS, LocalDateTime.of(anoBase, 10, 10, 20, 0), "Show de Pagode e Samba"));
            gerenciador.cadastrarEvento(new Evento("GARDEN MUSIC FESTIVAL 2026", "Local a definir - Porto Alegre, RS", Categoria.FESTASESHOWS, LocalDateTime.of(2026, 3, 14, 18, 0), "Festival musical de dois dias"));
            gerenciador.cadastrarEvento(new Evento("Baile Funk + preços especiais", "Nuvem - Porto Alegre, RS", Categoria.FESTASESHOWS, LocalDateTime.of(anoBase, 11, 22, 23, 0), "Baile Funk"));
            gerenciador.cadastrarEvento(new Evento("OPEN Combinhos", "Nuvem - Porto Alegre, RS", Categoria.FESTASESHOWS, LocalDateTime.of(anoBase, 11, 21, 23, 0), "Festa Open Bar"));
            gerenciador.cadastrarEvento(new Evento("Noche de Los Mueros / OPEN Destilados", "Nuvem - Porto Alegre, RS", Categoria.FESTASESHOWS, LocalDateTime.of(anoBase, 11, 1, 23, 0), "Festa de Halloween/Dia dos Mortos"));
            gerenciador.cadastrarEvento(new Evento("Sunset da R&A", "Casa das Flores - Porto Alegre, RS", Categoria.FESTASESHOWS, LocalDateTime.of(anoBase, 10, 25, 14, 0), "Sunset Party"));

            // --- CATEGORIA CULTURAL --
            gerenciador.cadastrarEvento(new Evento("Sessão e Oficina - 31/10", "Planetário da UFRGS", Categoria.CULTURAL, LocalDateTime.of(anoBase, 10, 31, 14, 0), "Sessão no Planetário"));
            gerenciador.cadastrarEvento(new Evento("Sessão e Oficina - 15/10", "Planetário da UFRGS", Categoria.CULTURAL, LocalDateTime.of(anoBase, 10, 15, 14, 0), "Sessão no Planetário"));
            gerenciador.cadastrarEvento(new Evento("Paint & sparkles", "Poa Café Armazém", Categoria.CULTURAL, LocalDateTime.of(anoBase, 10, 10, 16, 0), "Pintura em taças de espumante"));
            gerenciador.cadastrarEvento(new Evento("Pedagogia em Movimento", "Praça da Alfândega", Categoria.CULTURAL, LocalDateTime.of(anoBase, 11, 8, 8, 0), "Connexão, Cultura e Conhecimento"));
            gerenciador.cadastrarEvento(new Evento("CAPACITA | Da rádio à IA", "Palácio do Comércio", Categoria.CULTURAL, LocalDateTime.of(anoBase, 10, 10, 10, 0), "Evolução do marketing digital"));
            gerenciador.cadastrarEvento(new Evento("Caminhada Cultural Museus Conectam", "Museu da UFRGS", Categoria.CULTURAL, LocalDateTime.of(anoBase, 10, 11, 10, 0), "Caminhada de Museus"));
            gerenciador.cadastrarEvento(new Evento("Caminhada Cultural Cemitérios", "Cemitério da Santa Casa", Categoria.CULTURAL, LocalDateTime.of(anoBase, 10, 10, 10, 0), "Caminhada em Cemitérios históricos"));
            gerenciador.cadastrarEvento(new Evento("A NOVIÇA MAIS REBELDE", "Salão de Atos PUC RS", Categoria.CULTURAL, LocalDateTime.of(anoBase, 11, 9, 20, 0), "Espetáculo de comédia"));
            gerenciador.cadastrarEvento(new Evento("Confessionário - Relatos de Casa", "Teatro da PUC RS", Categoria.CULTURAL, LocalDateTime.of(anoBase, 11, 1, 20, 0), "Peça de teatro"));


            // --- CATEGORIA ESPORTIVO ---
            gerenciador.cadastrarEvento(new Evento("II Encontro Gaúcho de Gestores", "Sede Famurs - Porto Alegre, RS", Categoria.ESPORTIVO, LocalDateTime.of(anoBase, 10, 16, 9, 0), "Encontro de Gestores Esportivos"));
            gerenciador.cadastrarEvento(new Evento("CORRIDA E CAMINHADA SEST SENAT", "Rótula das Cuias - Porto Alegre, RS", Categoria.ESPORTIVO, LocalDateTime.of(anoBase, 10, 12, 8, 0), "Corrida e Caminhada"));
            gerenciador.cadastrarEvento(new Evento("27ª Corrida para Vencer o Diabetes", "Parque Moinhos de Vento", Categoria.ESPORTIVO, LocalDateTime.of(anoBase, 10, 26, 9, 0), "Corrida Beneficente"));

            gerenciador.cadastrarEvento(new Evento("4° Treinão de GRAU / POA-RS", "Complexo Cultural do Porto Seco", Categoria.ESPORTIVO, LocalDateTime.of(anoBase, 10, 1, 10, 0), "Treinão que dura até 19/10"));
            gerenciador.cadastrarEvento(new Evento("Treinão MovKids", "Sítio 902 - Esporte e Lazer", Categoria.ESPORTIVO, LocalDateTime.of(anoBase, 10, 12, 14, 0), "Treinão para crianças"));


            // --- CATEGORIA GASTRONOMICO ---
            gerenciador.cadastrarEvento(new Evento("Experiência Sensorial Gastronômica", "Casa 408", Categoria.GASTRONOMICO, LocalDateTime.of(anoBase, 10, 18, 19, 0), "Degustação e experiência"));
            gerenciador.cadastrarEvento(new Evento("Fermentados: Kombucha, Kefir e Chucrute", "Casa 408", Categoria.GASTRONOMICO, LocalDateTime.of(anoBase, 10, 11, 10, 0), "Workshop de fermentação"));
            gerenciador.cadastrarEvento(new Evento("Domingo de Fogo de Chão", "Rancho Tabacaray", Categoria.GASTRONOMICO, LocalDateTime.of(anoBase, 11, 30, 12, 0), "Almoço com churrasco"));
            gerenciador.cadastrarEvento(new Evento("1° Festival Africano - Banquete", "Unisinos Porto Alegre", Categoria.GASTRONOMICO, LocalDateTime.of(anoBase, 10, 10, 12, 0), "Banquete de culinária africana"));


            // Garante que o arquivo events.data seja criado com os novos dados
            gerenciador.salvarEventos();
        }
    }



    // --- Métodos de Leitura de Dados Complexos ---

    private static Categoria lerCategoria() {
        System.out.println("\nCategorias disponíveis:");
        Categoria[] categorias = Categoria.values();
        for (int i = 0; i < categorias.length; i++) {
            System.out.println((i + 1) + ". " + categorias[i]);
        }

        while (true) {
            System.out.print("Escolha a categoria pelo número: ");
            try {
                int escolha = Integer.parseInt(scanner.nextLine());
                if (escolha > 0 && escolha <= categorias.length) {
                    return categorias[escolha - 1];
                } else {
                    System.out.println("Número de categoria inválido. Tente novamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, digite o número.");
            }
        }
    }

    private static LocalDateTime lerHorario() {
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        while (true) {
            System.out.print("Horário (Formato dd/MM/yyyy HH:mm, Ex: 31/12/2025 20:30): ");
            String input = scanner.nextLine();
            try {
                return LocalDateTime.parse(input, formatador);
            } catch (DateTimeParseException e) {
                System.out.println("Formato de data/hora inválido. Use o formato dd/MM/yyyy HH:mm.");
            }
        }
    }
}