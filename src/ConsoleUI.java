import java.util.Scanner;

public class ConsoleUI {
    // Códigos ANSI para cores
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";
    
    // Negrito
    public static final String BOLD = "\u001B[1m";
    
    // Limpar tela
    public static final String CLEAR_SCREEN = "\033[H\033[2J";
    
    private static Scanner scanner = new Scanner(System.in);
    
    /**
     * Limpa a tela do console
     */
    public static void limparTela() {
        System.out.print(CLEAR_SCREEN);
        System.out.flush();
    }
    
    /**
     * Exibe um cabeçalho estilizado
     * @param titulo Título do cabeçalho
     */
    public static void exibirCabecalho(String titulo) {
        limparTela();
        System.out.println(CYAN + "╔═══════════════════════════════════════════════════════════╗" + RESET);
        System.out.printf(CYAN + "║" + BOLD + WHITE + " %-57s " + RESET + CYAN + "║" + RESET + "%n", titulo);
        System.out.println(CYAN + "╚═══════════════════════════════════════════════════════════╝" + RESET);
        System.out.println();
    }
    
    /**
     * Exibe um menu com opções numeradas
     * @param titulo Título do menu
     * @param opcoes Array com as opções
     */
    public static void exibirMenu(String titulo, String[] opcoes) {
        System.out.println(BLUE + BOLD + titulo + RESET);
        System.out.println();
        
        for (int i = 0; i < opcoes.length; i++) {
            if (i == opcoes.length - 1 && (opcoes[i].contains("Sair") || opcoes[i].contains("Voltar") || opcoes[i].contains("Logout"))) {
                System.out.printf(RED + "  [0] %s" + RESET + "%n", opcoes[i]);
            } else {
                System.out.printf(GREEN + "  [%d]" + RESET + " %s%n", i + 1, opcoes[i]);
            }
        }
        System.out.println();
    }
    
    /**
     * Solicita uma entrada do usuário
     * @param mensagem Mensagem a exibir
     * @return String com a entrada do usuário
     */
    public static String lerEntrada(String mensagem) {
        System.out.print(YELLOW + mensagem + RESET);
        return scanner.nextLine();
    }
    
    /**
     * Solicita um número inteiro do usuário
     * @param mensagem Mensagem a exibir
     * @return int com o número digitado ou -1 se inválido
     */
    public static int lerInteiro(String mensagem) {
        System.out.print(YELLOW + mensagem + RESET);
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    /**
     * Exibe uma mensagem de erro
     * @param mensagem Mensagem de erro
     */
    public static void exibirErro(String mensagem) {
        System.out.println(RED + "❌ " + mensagem + RESET);
    }
    
    /**
     * Exibe uma mensagem de sucesso
     * @param mensagem Mensagem de sucesso
     */
    public static void exibirSucesso(String mensagem) {
        System.out.println(GREEN + "✓ " + mensagem + RESET);
    }
    
    /**
     * Exibe uma mensagem de informação
     * @param mensagem Mensagem informativa
     */
    public static void exibirInfo(String mensagem) {
        System.out.println(BLUE + "ℹ " + mensagem + RESET);
    }
    
    /**
     * Pausa a execução até o usuário pressionar Enter
     */
    public static void pausar() {
        System.out.print(YELLOW + "\nPressione Enter para continuar..." + RESET);
        scanner.nextLine();
    }
    
    /**
     * Exibe uma música sendo reproduzida
     * @param musica Música sendo reproduzida
     */
    public static void exibirMusicaTocando(Musica musica) {
        System.out.println();
        System.out.println(PURPLE + "╔═══════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(PURPLE + "║" + BOLD + WHITE + "                    🎵 TOCANDO AGORA 🎵                    " + RESET + PURPLE + "║" + RESET);
        System.out.println(PURPLE + "╠═══════════════════════════════════════════════════════════╣" + RESET);
        System.out.printf(PURPLE + "║" + RESET + " ♫ %-55s " + PURPLE + "║" + RESET + "%n", musica.getNome());
        System.out.printf(PURPLE + "║" + RESET + " ► %-55s " + PURPLE + "║" + RESET + "%n", musica.getInterprete());
        System.out.printf(PURPLE + "║" + RESET + " ⌚ %-55s " + PURPLE + "║" + RESET + "%n", formatarDuracao(musica.getDuracao()));
        System.out.printf(PURPLE + "║" + RESET + " 🎸 %-55s " + PURPLE + "║" + RESET + "%n", musica.getGenero());
        System.out.println(PURPLE + "╚═══════════════════════════════════════════════════════════╝" + RESET);
        System.out.println();
    }
    
    /**
     * Exibe a letra da música
     * @param letra Letra da música
     */
    public static void exibirLetra(String letra) {
        System.out.println(CYAN + "╔═══════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(CYAN + "║" + BOLD + WHITE + "                         LETRA                             " + RESET + CYAN + "║" + RESET);
        System.out.println(CYAN + "╚═══════════════════════════════════════════════════════════╝" + RESET);
        System.out.println();
        
        String[] linhas = letra.split("\n");
        for (String linha : linhas) {
            System.out.println(WHITE + linha + RESET);
        }
        System.out.println();
    }
    
    /**
     * Exibe uma tabela com estatísticas
     * @param titulo Título da tabela
     * @param dados Array bidimensional com os dados
     */
    public static void exibirTabela(String titulo, String[][] dados) {
        System.out.println(BLUE + BOLD + titulo + RESET);
        System.out.println(BLUE + "┌───────────────────────────────────────────────────────────┐" + RESET);
        
        for (String[] linha : dados) {
            System.out.printf(BLUE + "│" + RESET + " %-30s" + BLUE + "│" + RESET + " %-25s " + BLUE + "│" + RESET + "%n", 
                linha[0], linha[1]);
        }
        
        System.out.println(BLUE + "└───────────────────────────────────────────────────────────┘" + RESET);
    }
    
    /**
     * Formata a duração de segundos para mm:ss
     * @param segundos Duração em segundos
     * @return String formatada
     */
    private static String formatarDuracao(int segundos) {
        int minutos = segundos / 60;
        int seg = segundos % 60;
        return String.format("%d:%02d", minutos, seg);
    }
    
    /**
     * Exibe uma barra de progresso
     * @param atual Valor atual
     * @param total Valor total
     * @param largura Largura da barra
     */
    public static void exibirBarraProgresso(int atual, int total, int largura) {
        int progresso = (int) ((double) atual / total * largura);
        StringBuilder barra = new StringBuilder("[");
        
        for (int i = 0; i < largura; i++) {
            if (i < progresso) {
                barra.append("█");
            } else {
                barra.append(" ");
            }
        }
        
        barra.append("] ");
        barra.append(String.format("%d/%d", atual, total));
        
        System.out.print(GREEN + "\r" + barra.toString() + RESET);
    }
    
    /**
     * Exibe o perfil do utilizador
     * @param utilizador Utilizador para exibir o perfil
     */
    public static void exibirPerfil(Utilizador utilizador) {
        System.out.println(CYAN + "╔═══════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(CYAN + "║" + BOLD + WHITE + "                    PERFIL DO UTILIZADOR                   " + RESET + CYAN + "║" + RESET);
        System.out.println(CYAN + "╠═══════════════════════════════════════════════════════════╣" + RESET);
        System.out.printf(CYAN + "║" + RESET + " 👤 Nome: %-48s " + CYAN + "║" + RESET + "%n", utilizador.getNome());
        System.out.printf(CYAN + "║" + RESET + " 📧 Email: %-47s " + CYAN + "║" + RESET + "%n", utilizador.getEmail());
        System.out.printf(CYAN + "║" + RESET + " 💎 Plano: %-47s " + CYAN + "║" + RESET + "%n", utilizador.getPlano().getNome());
        System.out.printf(CYAN + "║" + RESET + " 🌟 Pontos: %-46d " + CYAN + "║" + RESET + "%n", utilizador.getPontos());
        System.out.printf(CYAN + "║" + RESET + " 🎵 Músicas ouvidas: %-37d " + CYAN + "║" + RESET + "%n", utilizador.getNumeroTotalReproducoes());
        System.out.printf(CYAN + "║" + RESET + " 📋 Playlists criadas: %-35d " + CYAN + "║" + RESET + "%n", utilizador.getNumeroPlaylists());
        System.out.println(CYAN + "╚═══════════════════════════════════════════════════════════╝" + RESET);
    }
    
    /**
     * Exibe o logo do SpotifUM
     */
    public static void exibirLogo() {
        System.out.println(GREEN + "   _____             _   _  __ _    _ __  __ " + RESET);
        System.out.println(GREEN + "  / ____|           | | (_)/ _| |  | |  \\/  |" + RESET);
        System.out.println(GREEN + " | (___  _ __   ___ | |_ _| |_| |  | | \\  / |" + RESET);
        System.out.println(GREEN + "  \\___ \\| '_ \\ / _ \\| __| |  _| |  | | |\\/| |" + RESET);
        System.out.println(GREEN + "  ____) | |_) | (_) | |_| | | | |__| | |  | |" + RESET);
        System.out.println(GREEN + " |_____/| .__/ \\___/ \\__|_|_|  \\____/|_|  |_|" + RESET);
        System.out.println(GREEN + "        | |                                  " + RESET);
        System.out.println(GREEN + "        |_|                                  " + RESET);
        System.out.println();
    }
}