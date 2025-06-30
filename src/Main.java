import java.time.LocalDateTime;
import java.util.*;


public class Main {
    private static Sistema sistema;
    private static Utilizador utilizadorAtual;
    
    public static void main(String[] args) {
        // Inicializar sistema
        inicializarSistema();
        
        // Mostrar logo e menu principal
        ConsoleUI.exibirLogo();
        ConsoleUI.pausar();
        
        menuPrincipal();
    }
    
    private static void inicializarSistema() {
        sistema = Sistema.carregarEstado();
        if (sistema == null) {
            sistema = new Sistema();
            ConsoleUI.exibirInfo("Sistema iniciado pela primeira vez.");
            criarDadosIniciais();
        } else {
            ConsoleUI.exibirSucesso("Estado do sistema carregado com sucesso.");
        }
    }
    
    private static void menuPrincipal() {
        while (true) {
            ConsoleUI.exibirCabecalho("SPOTIFUM - MENU PRINCIPAL");
            
            String[] opcoes = {
                "Login",
                "Registrar novo utilizador",
                "Estatísticas do sistema",
                "Salvar estado do sistema",
                "Carregar estado do sistema",
                "Sair"
            };
            
            ConsoleUI.exibirMenu("Escolha uma opção:", opcoes);
            
            int opcao = ConsoleUI.lerInteiro("Opção: ");
            
            switch (opcao) {
                case 1:
                    fazerLogin();
                    break;
                case 2:
                    registrarUtilizador();
                    break;
                case 3:
                    mostrarEstatisticas();
                    break;
                case 4:
                    salvarSistema();
                    break;
                case 5:
                    carregarSistema();
                    break;
                case 0:
                    sairDoSistema();
                    return;
                default:
                    ConsoleUI.exibirErro("Opção inválida!");
                    ConsoleUI.pausar();
            }
        }
    }
    
    private static void fazerLogin() {
        ConsoleUI.exibirCabecalho("LOGIN");
        
        String email = ConsoleUI.lerEntrada("Email: ");
        
        utilizadorAtual = sistema.getUtilizadores().get(email);
        if (utilizadorAtual == null) {
            ConsoleUI.exibirErro("Utilizador não encontrado!");
        } else {
            ConsoleUI.exibirSucesso("Bem-vindo, " + utilizadorAtual.getNome() + "!");
            ConsoleUI.pausar();
            menuUtilizador();
        }
    }
    
    private static void registrarUtilizador() {
        ConsoleUI.exibirCabecalho("REGISTRAR NOVO UTILIZADOR");
        
        String nome = ConsoleUI.lerEntrada("Nome: ");
        String email = ConsoleUI.lerEntrada("Email: ");
        String morada = ConsoleUI.lerEntrada("Morada: ");
        
        System.out.println();
        ConsoleUI.exibirMenu("Escolha o plano:", new String[]{
            "Free (Gratuito)",
            "Premium Base (Padrão)",
            "Premium Top (Avançado)"
        });
        
        int opcaoPlano = ConsoleUI.lerInteiro("Opção: ");
        PlanoSubscricao plano;
        
        switch (opcaoPlano) {
            case 2:
                plano = new PlanoPremiumBase();
                break;
            case 3:
                plano = new PlanoPremiumTop();
                break;
            default:
                plano = new PlanoFree();
        }
        
        Utilizador novoUtilizador = new Utilizador(nome, email, morada, plano);
        if (sistema.registrarUtilizador(novoUtilizador)) {
            ConsoleUI.exibirSucesso("Utilizador registrado com sucesso!");
        } else {
            ConsoleUI.exibirErro("Este email já está registrado!");
        }
        ConsoleUI.pausar();
    }
    
    private static void menuUtilizador() {
        while (true) {
            ConsoleUI.exibirCabecalho("MENU DO UTILIZADOR - " + utilizadorAtual.getNome());
            ConsoleUI.exibirPerfil(utilizadorAtual);
            System.out.println();
            
            String[] opcoes = {
                "Reproduzir música",
                "Reproduzir playlist",
                "Criar playlist",
                "Guardar álbum/playlist",
                "Ver biblioteca",
                "Gerar playlist personalizada",
                "Estatísticas pessoais",
                "Atualizar plano",
                "Logout"
            };
            
            ConsoleUI.exibirMenu("Escolha uma opção:", opcoes);
            
            int opcao = ConsoleUI.lerInteiro("Opção: ");
            
            switch (opcao) {
                case 1:
                    reproduzirMusica();
                    break;
                case 2:
                    reproduzirPlaylist();
                    break;
                case 3:
                    criarPlaylist();
                    break;
                case 4:
                    guardarConteudo();
                    break;
                case 5:
                    verBiblioteca();
                    break;
                case 6:
                    gerarPlaylistPersonalizada();
                    break;
                case 7:
                    mostrarEstatisticasPessoais();
                    break;
                case 8:
                    atualizarPlano();
                    break;
                case 0:
                    utilizadorAtual = null;
                    return;
                default:
                    ConsoleUI.exibirErro("Opção inválida!");
                    ConsoleUI.pausar();
            }
        }
    }
    
    private static void reproduzirMusica() {
        ConsoleUI.exibirCabecalho("REPRODUZIR MÚSICA");
        
        Map<String, Album> albuns = sistema.getAlbuns();
        if (albuns.isEmpty()) {
            ConsoleUI.exibirErro("Não há álbuns disponíveis!");
            ConsoleUI.pausar();
            return;
        }
        
        List<Album> listaAlbuns = new ArrayList<>(albuns.values());
        String[] opcoesAlbuns = new String[listaAlbuns.size()];
        for (int i = 0; i < listaAlbuns.size(); i++) {
            opcoesAlbuns[i] = listaAlbuns.get(i).toString();
        }
        
        ConsoleUI.exibirMenu("Álbuns disponíveis:", opcoesAlbuns);
        
        int escolhaAlbum = ConsoleUI.lerInteiro("Escolha um álbum: ") - 1;
        if (escolhaAlbum < 0 || escolhaAlbum >= listaAlbuns.size()) {
            ConsoleUI.exibirErro("Álbum inválido!");
            ConsoleUI.pausar();
            return;
        }
        
        Album albumEscolhido = listaAlbuns.get(escolhaAlbum);
        Map<String, Musica> musicas = albumEscolhido.getMusicas();
        
        List<Musica> listaMusicas = new ArrayList<>(musicas.values());
        String[] opcoesMusicas = new String[listaMusicas.size()];
        for (int i = 0; i < listaMusicas.size(); i++) {
            opcoesMusicas[i] = listaMusicas.get(i).toString();
        }
        
        ConsoleUI.exibirMenu("Músicas do álbum " + albumEscolhido.getNome() + ":", opcoesMusicas);
        
        int escolhaMusica = ConsoleUI.lerInteiro("Escolha uma música: ") - 1;
        if (escolhaMusica < 0 || escolhaMusica >= listaMusicas.size()) {
            ConsoleUI.exibirErro("Música inválida!");
            ConsoleUI.pausar();
            return;
        }
        
        Musica musicaEscolhida = listaMusicas.get(escolhaMusica);
        sistema.registrarReproducao(utilizadorAtual, musicaEscolhida);
        
        // Reproduzir a música
        ConsoleUI.limparTela();
        ConsoleUI.exibirMusicaTocando(musicaEscolhida);
        ConsoleUI.exibirLetra(musicaEscolhida.getLetra());
        
        // Simular reprodução com barra de progresso
        System.out.println(ConsoleUI.GREEN + "Reproduzindo..." + ConsoleUI.RESET);
        for (int i = 0; i <= 100; i += 5) {
            ConsoleUI.exibirBarraProgresso(i, 100, 50);
            try {
                Thread.sleep(100); // Simulação de reprodução
            } catch (InterruptedException e) {
                // Ignorar
            }
        }
        System.out.println();
        
        ConsoleUI.exibirSucesso("Música reproduzida com sucesso!");
        ConsoleUI.pausar();
    }
    
    private static void guardarConteudo() {
        ConsoleUI.exibirCabecalho("GUARDAR CONTEÚDO");
        
        if (!utilizadorAtual.getPlano().permiteSalvarBiblioteca()) {
            ConsoleUI.exibirErro("Seu plano não permite guardar conteúdo!");
            ConsoleUI.pausar();
            return;
        }
        
        String[] opcoes = {
            "Guardar álbum",
            "Guardar playlist",
            "Voltar"
        };
        
        ConsoleUI.exibirMenu("O que deseja guardar?", opcoes);
        
        int opcao = ConsoleUI.lerInteiro("Escolha: ");
        
        switch (opcao) {
            case 1:
                guardarAlbum();
                break;
            case 2:
                guardarPlaylist();
                break;
            case 0:
                return;
            default:
                ConsoleUI.exibirErro("Opção inválida!");
        }
        
        ConsoleUI.pausar();
    }
    
    private static void guardarAlbum() {
        Map<String, Album> albuns = sistema.getAlbuns();
        if (albuns.isEmpty()) {
            ConsoleUI.exibirErro("Não há álbuns disponíveis!");
            return;
        }
        
        List<Album> listaAlbuns = new ArrayList<>(albuns.values());
        String[] opcoesAlbuns = new String[listaAlbuns.size()];
        for (int i = 0; i < listaAlbuns.size(); i++) {
            opcoesAlbuns[i] = listaAlbuns.get(i).toString();
        }
        
        ConsoleUI.exibirMenu("Álbuns disponíveis:", opcoesAlbuns);
        
        int escolha = ConsoleUI.lerInteiro("Escolha um álbum: ") - 1;
        if (escolha < 0 || escolha >= listaAlbuns.size()) {
            ConsoleUI.exibirErro("Álbum inválido!");
            return;
        }
        
        Album albumEscolhido = listaAlbuns.get(escolha);
        if (utilizadorAtual.guardarAlbum(albumEscolhido)) {
            ConsoleUI.exibirSucesso("Álbum guardado com sucesso!");
        } else {
            ConsoleUI.exibirErro("Erro ao guardar álbum!");
        }
    }
    
    private static void guardarPlaylist() {
        List<Playlist> playlistsPublicas = sistema.getPlaylistsPublicas();
        if (playlistsPublicas.isEmpty()) {
            ConsoleUI.exibirErro("Não há playlists públicas disponíveis!");
            return;
        }
        
        String[] opcoesPlaylists = new String[playlistsPublicas.size()];
        for (int i = 0; i < playlistsPublicas.size(); i++) {
            opcoesPlaylists[i] = playlistsPublicas.get(i).toString();
        }
        
        ConsoleUI.exibirMenu("Playlists públicas disponíveis:", opcoesPlaylists);
        
        int escolha = ConsoleUI.lerInteiro("Escolha uma playlist: ") - 1;
        if (escolha < 0 || escolha >= playlistsPublicas.size()) {
            ConsoleUI.exibirErro("Playlist inválida!");
            return;
        }
        
        Playlist playlistEscolhida = playlistsPublicas.get(escolha);
        if (utilizadorAtual.guardarPlaylist(playlistEscolhida)) {
            ConsoleUI.exibirSucesso("Playlist guardada com sucesso!");
        } else {
            ConsoleUI.exibirErro("Erro ao guardar playlist!");
        }
    }
    
    private static void verBiblioteca() {
        ConsoleUI.exibirCabecalho("SUA BIBLIOTECA");
        
        System.out.println(ConsoleUI.BLUE + ConsoleUI.BOLD + "📚 ÁLBUNS GUARDADOS" + ConsoleUI.RESET);
        Map<String, Album> albunsGuardados = utilizadorAtual.getAlbunsGuardados();
        if (albunsGuardados.isEmpty()) {
            System.out.println("  Nenhum álbum guardado.");
        } else {
            for (Album album : albunsGuardados.values()) {
                System.out.println("  • " + album);
            }
        }
        
        System.out.println();
        System.out.println(ConsoleUI.BLUE + ConsoleUI.BOLD + "📋 PLAYLISTS GUARDADAS" + ConsoleUI.RESET);
        Map<String, Playlist> playlistsGuardadas = utilizadorAtual.getPlaylistsGuardadas();
        if (playlistsGuardadas.isEmpty()) {
            System.out.println("  Nenhuma playlist guardada.");
        } else {
            for (Playlist playlist : playlistsGuardadas.values()) {
                System.out.println("  • " + playlist);
            }
        }
        
        ConsoleUI.pausar();
    }
    
    private static void gerarPlaylistPersonalizada() {
        ConsoleUI.exibirCabecalho("GERAR PLAYLIST PERSONALIZADA");
        
        if (!(utilizadorAtual.getPlano() instanceof PlanoPremiumTop)) {
            ConsoleUI.exibirErro("Esta funcionalidade é exclusiva para usuários Premium Top!");
            ConsoleUI.pausar();
            return;
        }
        
        String[] opcoes = {
            "Baseada em preferências",
            "Com tempo máximo",
            "Apenas músicas explícitas",
            "Voltar"
        };
        
        ConsoleUI.exibirMenu("Escolha o tipo de playlist:", opcoes);
        
        int opcao = ConsoleUI.lerInteiro("Opção: ");
        ListaFavoritos lista = null;
        
        switch (opcao) {
            case 1:
                lista = sistema.gerarPlaylistPreferencias(utilizadorAtual);
                break;
            case 2:
                int minutos = ConsoleUI.lerInteiro("Duração máxima em minutos: ");
                lista = sistema.gerarPlaylistPreferenciasTempo(utilizadorAtual, minutos * 60);
                break;
            case 3:
                lista = sistema.gerarPlaylistPreferenciasExplicitas(utilizadorAtual);
                break;
            case 0:
                return;
            default:
                ConsoleUI.exibirErro("Opção inválida!");
                ConsoleUI.pausar();
                return;
        }
        
        if (lista != null && lista.getNumeroMusicas() > 0) {
            utilizadorAtual.guardarPlaylist(lista);
            ConsoleUI.exibirSucesso("Playlist gerada e guardada com sucesso!");
            System.out.println("Detalhes: " + lista);
        } else {
            ConsoleUI.exibirErro("Não foi possível gerar a playlist! Verifique se você já ouviu músicas suficientes.");
        }
        
        ConsoleUI.pausar();
    }
    
    private static void mostrarEstatisticasPessoais() {
        ConsoleUI.exibirCabecalho("ESTATÍSTICAS PESSOAIS");
        
        String[][] dados = {
            {"🎵 Total de músicas ouvidas", String.valueOf(utilizadorAtual.getNumeroTotalReproducoes())},
            {"📋 Playlists criadas", String.valueOf(utilizadorAtual.getNumeroPlaylists())},
            {"🌟 Pontos acumulados", String.valueOf(utilizadorAtual.getPontos())},
            {"💎 Plano atual", utilizadorAtual.getPlano().getNome()}
        };
        
        ConsoleUI.exibirTabela("Estatísticas Gerais", dados);
        
        System.out.println();
        System.out.println(ConsoleUI.CYAN + ConsoleUI.BOLD + "TOP 5 MÚSICAS MAIS OUVIDAS" + ConsoleUI.RESET);
        List<String> musicasMaisOuvidas = utilizadorAtual.getMusicasMaisOuvidas(5);
        for (int i = 0; i < musicasMaisOuvidas.size(); i++) {
            System.out.printf("  %d. %s%n", i + 1, musicasMaisOuvidas.get(i));
        }
        
        System.out.println();
        System.out.println(ConsoleUI.CYAN + ConsoleUI.BOLD + "TOP 5 GÊNEROS MAIS OUVIDOS" + ConsoleUI.RESET);
        Map<String, Integer> generosMaisOuvidos = utilizadorAtual.getGenerosMaisOuvidos();
        generosMaisOuvidos.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(5)
            .forEach(entry -> System.out.printf("  • %s: %d vezes%n", entry.getKey(), entry.getValue()));
        
        System.out.println();
        System.out.println(ConsoleUI.CYAN + ConsoleUI.BOLD + "TOP 5 ARTISTAS MAIS OUVIDOS" + ConsoleUI.RESET);
        Map<String, Integer> interpretesMaisOuvidos = utilizadorAtual.getInterpretesMaisOuvidos();
        interpretesMaisOuvidos.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(5)
            .forEach(entry -> System.out.printf("  • %s: %d vezes%n", entry.getKey(), entry.getValue()));
        
        ConsoleUI.pausar();
    }
    
    private static void atualizarPlano() {
        ConsoleUI.exibirCabecalho("ATUALIZAR PLANO");
        
        System.out.println(ConsoleUI.YELLOW + "Plano atual: " + utilizadorAtual.getPlano().getNome() + ConsoleUI.RESET);
        System.out.println();
        
        String[] opcoes = {
            "Free (Gratuito)",
            "Premium Base (Padrão)",
            "Premium Top (Avançado)",
            "Voltar"
        };
        
        ConsoleUI.exibirMenu("Escolha o novo plano:", opcoes);
        
        int opcaoPlano = ConsoleUI.lerInteiro("Opção: ");
        PlanoSubscricao novoPlano;
        
        switch (opcaoPlano) {
            case 1:
                novoPlano = new PlanoFree();
                break;
            case 2:
                novoPlano = new PlanoPremiumBase();
                break;
            case 3:
                novoPlano = new PlanoPremiumTop();
                break;
            case 0:
                return;
            default:
                ConsoleUI.exibirErro("Opção inválida!");
                ConsoleUI.pausar();
                return;
        }
        
        utilizadorAtual.setPlano(novoPlano);
        ConsoleUI.exibirSucesso("Plano atualizado com sucesso para: " + novoPlano.getNome());
        ConsoleUI.pausar();
    }
    
    private static void mostrarEstatisticas() {
        ConsoleUI.exibirCabecalho("ESTATÍSTICAS DO SISTEMA");
        
        Musica musicaMaisReproduzida = sistema.getMusicaMaisReproduzida();
        String interpreteMaisEscutado = sistema.getInterpreteMaisEscutado();
        Utilizador utilizadorMaisOuviu = sistema.getUtilizadorQueMaisOuviu();
        Utilizador utilizadorMaisPontos = sistema.getUtilizadorComMaisPontos();
        String generoMaisReproduzido = sistema.getGeneroMaisReproduzido();
        Utilizador utilizadorMaisPlaylists = sistema.getUtilizadorComMaisPlaylists();
        
        String[][] dados = {
            {"🎵 Música mais reproduzida", 
                musicaMaisReproduzida != null ? 
                    musicaMaisReproduzida.getNome() + " (" + musicaMaisReproduzida.getContagemReproducoes() + " vezes)" : 
                    "Nenhuma"},
            {"🎤 Intérprete mais escutado", 
                interpreteMaisEscutado != null ? interpreteMaisEscutado : "Nenhum"},
            {"👤 Utilizador que mais ouviu", 
                utilizadorMaisOuviu != null ? 
                    utilizadorMaisOuviu.getNome() + " (" + utilizadorMaisOuviu.getNumeroTotalReproducoes() + " músicas)" : 
                    "Nenhum"},
            {"🌟 Utilizador com mais pontos", 
                utilizadorMaisPontos != null ? 
                    utilizadorMaisPontos.getNome() + " (" + utilizadorMaisPontos.getPontos() + " pontos)" : 
                    "Nenhum"},
            {"🎸 Gênero mais reproduzido", 
                generoMaisReproduzido != null ? generoMaisReproduzido : "Nenhum"},
            {"📋 Playlists públicas", String.valueOf(sistema.getNumeroPlaylistsPublicas())},
            {"🏆 Utilizador com mais playlists", 
                utilizadorMaisPlaylists != null ? 
                    utilizadorMaisPlaylists.getNome() + " (" + utilizadorMaisPlaylists.getNumeroPlaylists() + " playlists)" : 
                    "Nenhum"}
        };
        
        ConsoleUI.exibirTabela("Estatísticas Globais", dados);
        ConsoleUI.pausar();
    }
    
    private static void salvarSistema() {
        if (sistema.salvarEstado()) {
            ConsoleUI.exibirSucesso("Estado do sistema salvo com sucesso!");
        } else {
            ConsoleUI.exibirErro("Erro ao salvar estado do sistema!");
        }
        ConsoleUI.pausar();
    }
    
    private static void carregarSistema() {
        Sistema sistemaCarregado = Sistema.carregarEstado();
        if (sistemaCarregado != null) {
            sistema = sistemaCarregado;
            ConsoleUI.exibirSucesso("Estado do sistema carregado com sucesso!");
        } else {
            ConsoleUI.exibirErro("Erro ao carregar estado do sistema!");
        }
        ConsoleUI.pausar();
    }
    
    private static void sairDoSistema() {
        if (sistema.salvarEstado()) {
            ConsoleUI.exibirSucesso("Estado salvo com sucesso!");
        }
        ConsoleUI.exibirInfo("Obrigado por usar o SpotifUM!");
        System.exit(0);
    }
    
    private static void criarDadosIniciais() {
        ConsoleUI.exibirInfo("Criando dados iniciais...");
        
        // Criar alguns álbuns com músicas
        Album album1 = new Album("Greatest Hits", "Queen", 1981);
        
        Musica musica1 = new Musica("Bohemian Rhapsody", "Queen", "EMI", 
            "Is this the real life?\nIs this just fantasy?\nCaught in a landslide\nNo escape from reality", 
            "Rock", 354);
        musica1.adicionarLinhaMusical("♪ ♫ ♪ Bohemian Rhapsody ♪ ♫ ♪");
        musica1.adicionarLinhaMusical("♪ ♫ ♪ Epic guitar solo ♪ ♫ ♪");
        album1.adicionarMusica(musica1);
        
        Musica musica2 = new Musica("We Will Rock You", "Queen", "EMI", 
            "Buddy you're a boy make a big noise\nPlaying in the street gonna be a big man some day", 
            "Rock", 122);
        musica2.adicionarLinhaMusical("♪ ♫ ♪ Stomp Stomp Clap ♪ ♫ ♪");
        album1.adicionarMusica(musica2);
        
        // Criar música explícita
        MusicaExplicita musicaExplicita = new MusicaExplicita("Killing in the Name", "Rage Against the Machine", 
            "Epic Records", "Some of those that work forces\nAre the same that burn crosses", 
            "Rock", 314, "Conteúdo com linguagem forte");
        musicaExplicita.adicionarLinhaMusical("♪ ♫ ♪ Aggressive guitar riff ♪ ♫ ♪");
        album1.adicionarMusica(musicaExplicita);
        
        sistema.adicionarAlbum(album1);
        
        // Criar outro álbum
        Album album2 = new Album("Thriller", "Michael Jackson", 1982);
        
        Musica musica3 = new Musica("Billie Jean", "Michael Jackson", "Epic Records", 
            "She was more like a beauty queen from a movie scene", "Pop", 294);
        musica3.adicionarLinhaMusical("♪ ♫ ♪ Iconic bassline ♪ ♫ ♪");
        album2.adicionarMusica(musica3);
        
        Musica musica4 = new Musica("Beat It", "Michael Jackson", "Epic Records", 
            "They told him don't you ever come around here", "Pop", 258);
        musica4.adicionarLinhaMusical("♪ ♫ ♪ Guitar solo by Eddie Van Halen ♪ ♫ ♪");
        album2.adicionarMusica(musica4);
        
        sistema.adicionarAlbum(album2);
        
        // Criar um utilizador de exemplo
        Utilizador admin = new Utilizador("Admin", "admin@spotifum.com", "Rua Principal", new PlanoPremiumTop());
        sistema.registrarUtilizador(admin);
        
        // Criar uma playlist pública de exemplo
        Playlist playlistPublica = admin.criarPlaylist("Rock Classics");
        playlistPublica.adicionarMusica(musica1);
        playlistPublica.adicionarMusica(musica2);
        playlistPublica.setPublica(true);
        sistema.adicionarPlaylistPublica(playlistPublica);
        
        ConsoleUI.exibirSucesso("Dados iniciais criados com sucesso!");
    }


    private static void reproduzirPlaylist() {
        ConsoleUI.exibirCabecalho("REPRODUZIR PLAYLIST");
        
        if (utilizadorAtual.getPlano() instanceof PlanoFree) {
            // Usuários free só podem reproduzir playlists aleatórias
            ConsoleUI.exibirInfo("Como usuário Free, você só pode reproduzir playlists aleatórias.");
            ConsoleUI.pausar();
            
            PlaylistAleatoria playlist = sistema.gerarPlaylistAleatoria();
            reproduzirPlaylistAleatoria(playlist);
        } else {
            // Usuários premium podem escolher playlists
            List<Playlist> todasPlaylists = new ArrayList<>();
            todasPlaylists.addAll(utilizadorAtual.getPlaylistsCriadas());
            todasPlaylists.addAll(sistema.getPlaylistsPublicas());
            
            if (todasPlaylists.isEmpty()) {
                ConsoleUI.exibirErro("Não há playlists disponíveis!");
                ConsoleUI.pausar();
                return;
            }
            
            String[] opcoesPlaylists = new String[todasPlaylists.size()];
            for (int i = 0; i < todasPlaylists.size(); i++) {
                opcoesPlaylists[i] = todasPlaylists.get(i).toString();
            }
            
            ConsoleUI.exibirMenu("Playlists disponíveis:", opcoesPlaylists);
            
            int escolha = ConsoleUI.lerInteiro("Escolha uma playlist: ") - 1;
            if (escolha < 0 || escolha >= todasPlaylists.size()) {
                ConsoleUI.exibirErro("Playlist inválida!");
                ConsoleUI.pausar();
                return;
            }
            
            Playlist playlist = todasPlaylists.get(escolha);
            if (playlist instanceof PlaylistPersonalizada) {
                PlaylistPersonalizada playlistPers = (PlaylistPersonalizada) playlist;
                
                String resposta = ConsoleUI.lerEntrada("Ativar modo aleatório? (s/n): ");
                if (resposta.equalsIgnoreCase("s")) {
                    playlistPers.setModoAleatorio(true);
                    ConsoleUI.exibirSucesso("Modo aleatório ativado!");
                }
            }
            
            reproduzirPlaylistComControles(playlist);
        }
    }
    
    private static void reproduzirPlaylistAleatoria(PlaylistAleatoria playlist) {
        ConsoleUI.exibirCabecalho("REPRODUZINDO PLAYLIST ALEATÓRIA");
        
        while (true) {
            Musica musica = playlist.reproduzirProxima();
            if (musica == null) {
                ConsoleUI.exibirInfo("Fim da playlist!");
                break;
            }
            
            sistema.registrarReproducao(utilizadorAtual, musica);
            ConsoleUI.exibirMusicaTocando(musica);
            
            String resposta = ConsoleUI.lerEntrada("Continuar para próxima música? (s/n): ");
            if (!resposta.equalsIgnoreCase("s")) {
                break;
            }
        }
        
        ConsoleUI.pausar();
    }
    
    private static void reproduzirPlaylistComControles(Playlist playlist) {
        while (true) {
            Musica musica = playlist.reproduzirProxima();
            if (musica == null) {
                ConsoleUI.exibirInfo("Fim da playlist!");
                break;
            }
            
            sistema.registrarReproducao(utilizadorAtual, musica);
            ConsoleUI.exibirMusicaTocando(musica);
            
            if (utilizadorAtual.getPlano().permiteAvancaRetrocede()) {
                String[] opcoes = {
                    "Próxima música",
                    "Música anterior",
                    "Sair"
                };
                
                ConsoleUI.exibirMenu("Controles:", opcoes);
                int opcao = ConsoleUI.lerInteiro("Escolha: ");
                
                if (opcao == 0) break;
                
                if (opcao == 2) {
                    musica = playlist.reproduzirAnterior();
                    if (musica != null) {
                        sistema.registrarReproducao(utilizadorAtual, musica);
                        ConsoleUI.exibirMusicaTocando(musica);
                    } else {
                        ConsoleUI.exibirErro("Não é possível retroceder!");
                    }
                }
            } else {
                String resposta = ConsoleUI.lerEntrada("Continuar para próxima música? (s/n): ");
                if (!resposta.equalsIgnoreCase("s")) break;
            }
        }
        
        ConsoleUI.pausar();
    }
    
    private static void criarPlaylist() {
        ConsoleUI.exibirCabecalho("CRIAR PLAYLIST");
        
        if (!utilizadorAtual.getPlano().permiteCriarPlaylist()) {
            ConsoleUI.exibirErro("Seu plano não permite criar playlists!");
            ConsoleUI.pausar();
            return;
        }
        
        String nome = ConsoleUI.lerEntrada("Nome da playlist: ");
        
        Playlist playlist = utilizadorAtual.criarPlaylist(nome);
        if (playlist == null) {
            ConsoleUI.exibirErro("Erro ao criar playlist!");
            ConsoleUI.pausar();
            return;
        }
        
        ConsoleUI.exibirSucesso("Playlist criada com sucesso!");
        
        while (true) {
            String[] opcoes = {
                "Adicionar música",
                "Tornar playlist pública",
                "Finalizar"
            };
            
            ConsoleUI.exibirMenu("Opções:", opcoes);
            int opcao = ConsoleUI.lerInteiro("Escolha: ");
            
            if (opcao == 0) break;
            
            switch (opcao) {
                case 1:
                    adicionarMusicaPlaylist(playlist);
                    break;
                case 2:
                    if (utilizadorAtual.tornarPlaylistPublica(nome)) {
                        sistema.adicionarPlaylistPublica(playlist);
                        ConsoleUI.exibirSucesso("Playlist tornada pública!");
                    } else {
                        ConsoleUI.exibirErro("Erro ao tornar playlist pública!");
                    }
                    break;
            }
        }
    }
    
    private static void adicionarMusicaPlaylist(Playlist playlist) {
        // Reutilizar código similar ao reproduzirMusica, mas adicionando à playlist
        Map<String, Album> albuns = sistema.getAlbuns();
        if (albuns.isEmpty()) {
            ConsoleUI.exibirErro("Não há álbuns disponíveis!");
            return;
        }
        
        List<Album> listaAlbuns = new ArrayList<>(albuns.values());
        String[] opcoesAlbuns = new String[listaAlbuns.size()];
        for (int i = 0; i < listaAlbuns.size(); i++) {
            opcoesAlbuns[i] = listaAlbuns.get(i).toString();
        }
        
        ConsoleUI.exibirMenu("Álbuns disponíveis:", opcoesAlbuns);
        
        int escolhaAlbum = ConsoleUI.lerInteiro("Escolha um álbum: ") - 1;
        if (escolhaAlbum < 0 || escolhaAlbum >= listaAlbuns.size()) {
            ConsoleUI.exibirErro("Álbum inválido!");
            return;
        }
        
        Album albumEscolhido = listaAlbuns.get(escolhaAlbum);
        Map<String, Musica> musicas = albumEscolhido.getMusicas();

    }
}