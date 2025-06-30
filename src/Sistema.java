import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Sistema implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String ARQUIVO_SISTEMA = "spotifum.dat";
    
    private Map<String, Utilizador> utilizadores; // Email -> Utilizador
    private Map<String, Album> albuns; // Nome -> Album
    private List<Playlist> playlistsPublicas;
    private Map<String, Integer> contagemPorGenero; // Gênero -> Contagem
    private Map<String, Integer> contagemPorInterprete; // Intérprete -> Contagem
    
    public Sistema() {
        this.utilizadores = new HashMap<>();
        this.albuns = new HashMap<>();
        this.playlistsPublicas = new ArrayList<>();
        this.contagemPorGenero = new HashMap<>();
        this.contagemPorInterprete = new HashMap<>();
    }
    
    /**
     * Registra um novo utilizador no sistema
     * @param utilizador Utilizador a ser registrado
     * @return true se registrado com sucesso, false se o email já existe
     */
    public boolean registrarUtilizador(Utilizador utilizador) {
        String email = utilizador.getEmail();
        if (utilizadores.containsKey(email)) {
            return false;
        }
        utilizadores.put(email, utilizador);
        return true;
    }
    
    /**
     * Adiciona um álbum ao sistema
     * @param album Álbum a ser adicionado
     * @return true se adicionado com sucesso, false se já existe
     */
    public boolean adicionarAlbum(Album album) {
        String nome = album.getNome();
        if (albuns.containsKey(nome)) {
            return false;
        }
        albuns.put(nome, album);
        return true;
    }
    
    /**
     * Adiciona uma playlist pública ao sistema
     * @param playlist Playlist a ser adicionada
     * @return true se adicionada com sucesso
     */
    public boolean adicionarPlaylistPublica(Playlist playlist) {
        if (playlist.isPublica()) {
            playlistsPublicas.add(playlist);
            return true;
        }
        return false;
    }
    
    /**
     * Registra a reprodução de uma música por um utilizador
     * @param utilizador Utilizador que reproduziu a música
     * @param musica Música reproduzida
     */
    public void registrarReproducao(Utilizador utilizador, Musica musica) {
        utilizador.ouvirMusica(musica);
        
        // Atualizar estatísticas globais
        String genero = musica.getGenero();
        contagemPorGenero.put(genero, contagemPorGenero.getOrDefault(genero, 0) + 1);
        
        String interprete = musica.getInterprete();
        contagemPorInterprete.put(interprete, contagemPorInterprete.getOrDefault(interprete, 0) + 1);
    }
    
    /**
     * Retorna a música mais reproduzida no sistema
     * @return A música mais reproduzida ou null se não houver músicas
     */
    public Musica getMusicaMaisReproduzida() {
        Musica musicaMaisReproduzida = null;
        int maxReproducoes = 0;
        
        for (Album album : albuns.values()) {
            for (Musica musica : album.getMusicas().values()) {
                if (musica.getContagemReproducoes() > maxReproducoes) {
                    maxReproducoes = musica.getContagemReproducoes();
                    musicaMaisReproduzida = musica;
                }
            }
        }
        
        return musicaMaisReproduzida;
    }
    
    /**
     * Retorna o intérprete mais escutado no sistema
     * @return O nome do intérprete mais escutado ou null se não houver dados
     */
    public String getInterpreteMaisEscutado() {
        return contagemPorInterprete.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);
    }
    
    /**
     * Retorna o utilizador que mais músicas ouviu
     * @return O utilizador que mais músicas ouviu ou null se não houver dados
     */
    public Utilizador getUtilizadorQueMaisOuviu() {
        return utilizadores.values().stream()
            .max(Comparator.comparingInt(Utilizador::getNumeroTotalReproducoes))
            .orElse(null);
    }
    
    /**
     * Retorna o utilizador que mais músicas ouviu num período específico
     * @param inicio Data/hora inicial
     * @param fim Data/hora final
     * @return O utilizador que mais músicas ouviu no período ou null se não houver dados
     */
    public Utilizador getUtilizadorQueMaisOuviuPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return utilizadores.values().stream()
            .max(Comparator.comparingInt(u -> u.getNumeroBusicasOuvidasPeriodo(inicio, fim)))
            .orElse(null);
    }
    
    /**
     * Retorna o utilizador com mais pontos
     * @return O utilizador com mais pontos ou null se não houver dados
     */
    public Utilizador getUtilizadorComMaisPontos() {
        return utilizadores.values().stream()
            .max(Comparator.comparingInt(Utilizador::getPontos))
            .orElse(null);
    }
    
    /**
     * Retorna o gênero musical mais reproduzido
     * @return O gênero mais reproduzido ou null se não houver dados
     */
    public String getGeneroMaisReproduzido() {
        return contagemPorGenero.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);
    }
    
    /**
     * Retorna o número de playlists públicas no sistema
     * @return Quantidade de playlists públicas
     */
    public int getNumeroPlaylistsPublicas() {
        return playlistsPublicas.size();
    }
    
    /**
     * Retorna o utilizador com mais playlists
     * @return O utilizador com mais playlists ou null se não houver dados
     */
    public Utilizador getUtilizadorComMaisPlaylists() {
        return utilizadores.values().stream()
            .max(Comparator.comparingInt(Utilizador::getNumeroPlaylists))
            .orElse(null);
    }
    
    /**
     * Busca por músicas de um determinado gênero
     * @param genero Gênero musical a ser buscado
     * @return Lista de músicas do gênero especificado
     */
    public List<Musica> buscarMusicasPorGenero(String genero) {
        List<Musica> resultado = new ArrayList<>();
        
        for (Album album : albuns.values()) {
            for (Musica musica : album.getMusicas().values()) {
                if (musica.getGenero().equalsIgnoreCase(genero)) {
                    resultado.add(musica);
                }
            }
        }
        
        return resultado;
    }
    
    /**
     * Busca por músicas explícitas
     * @return Lista de músicas explícitas
     */
    public List<Musica> buscarMusicasExplicitas() {
        List<Musica> resultado = new ArrayList<>();
        
        for (Album album : albuns.values()) {
            for (Musica musica : album.getMusicas().values()) {
                if (musica instanceof MusicaExplicita) {
                    resultado.add(musica);
                }
            }
        }
        
        return resultado;
    }
    
    /**
     * Gera uma playlist com base nas preferências do utilizador
     * @param utilizador Utilizador para quem gerar a playlist
     * @return ListaFavoritos gerada ou null se não for possível
     */
    public ListaFavoritos gerarPlaylistPreferencias(Utilizador utilizador) {
        if (!(utilizador.getPlano() instanceof PlanoPremiumTop)) {
            return null; // Apenas utilizadores Premium Top têm acesso a esta funcionalidade
        }
        
        ListaFavoritos listaFavoritos = new ListaFavoritos("Favoritos de " + utilizador.getNome());
        
        // Obter gêneros mais ouvidos
        Map<String, Integer> generosFavoritos = utilizador.getGenerosMaisOuvidos();
        
        // Adicionar músicas dos gêneros favoritos
        for (Map.Entry<String, Integer> entry : generosFavoritos.entrySet()) {
            String genero = entry.getKey();
            List<Musica> musicasGenero = buscarMusicasPorGenero(genero);
            
            // Ordenar por popularidade (número de reproduções)
            musicasGenero.sort((m1, m2) -> Integer.compare(m2.getContagemReproducoes(), m1.getContagemReproducoes()));
            
            // Adicionar até 5 músicas por gênero
            int count = 0;
            for (Musica musica : musicasGenero) {
                if (count >= 5) break;
                if (listaFavoritos.adicionarMusica(musica)) {
                    count++;
                }
            }
        }
        
        return listaFavoritos;
    }
    
    /**
     * Gera uma playlist com base nas preferências e tempo máximo
     * @param utilizador Utilizador para quem gerar a playlist
     * @param duracaoMaxima Duração máxima em segundos
     * @return ListaFavoritos gerada ou null se não for possível
     */
    public ListaFavoritos gerarPlaylistPreferenciasTempo(Utilizador utilizador, int duracaoMaxima) {
        if (!(utilizador.getPlano() instanceof PlanoPremiumTop)) {
            return null;
        }
        
        ListaFavoritos listaFavoritos = new ListaFavoritos("Favoritos Limitados de " + utilizador.getNome(),
                                                           null, duracaoMaxima, false);
        
        Map<String, Integer> generosFavoritos = utilizador.getGenerosMaisOuvidos();
        
        for (Map.Entry<String, Integer> entry : generosFavoritos.entrySet()) {
            String genero = entry.getKey();
            List<Musica> musicasGenero = buscarMusicasPorGenero(genero);
            
            musicasGenero.sort((m1, m2) -> Integer.compare(m2.getContagemReproducoes(), m1.getContagemReproducoes()));
            
            for (Musica musica : musicasGenero) {
                if (listaFavoritos.getDuracaoTotal() + musica.getDuracao() <= duracaoMaxima) {
                    listaFavoritos.adicionarMusica(musica);
                }
            }
        }
        
        return listaFavoritos;
    }
    
    /**
     * Gera uma playlist apenas com músicas explícitas baseada nas preferências
     * @param utilizador Utilizador para quem gerar a playlist
     * @return ListaFavoritos gerada ou null se não for possível
     */
    public ListaFavoritos gerarPlaylistPreferenciasExplicitas(Utilizador utilizador) {
        if (!(utilizador.getPlano() instanceof PlanoPremiumTop)) {
            return null;
        }
        
        ListaFavoritos listaFavoritos = new ListaFavoritos("Favoritos Explícitos de " + utilizador.getNome(),
                                                           null, -1, true);
        
        Map<String, Integer> generosFavoritos = utilizador.getGenerosMaisOuvidos();
        
        for (Map.Entry<String, Integer> entry : generosFavoritos.entrySet()) {
            String genero = entry.getKey();
            List<Musica> musicasGenero = buscarMusicasPorGenero(genero);
            
            // Filtrar apenas músicas explícitas
            musicasGenero = musicasGenero.stream()
                .filter(musica -> musica instanceof MusicaExplicita)
                .sorted((m1, m2) -> Integer.compare(m2.getContagemReproducoes(), m1.getContagemReproducoes()))
                .collect(Collectors.toList());
            
            for (Musica musica : musicasGenero) {
                listaFavoritos.adicionarMusica(musica);
            }
        }
        
        return listaFavoritos;
    }
    
    /**
     * Gera uma playlist aleatória para utilizadores Free
     * @return Playlist aleatória gerada
     */
    public PlaylistAleatoria gerarPlaylistAleatoria() {
        PlaylistAleatoria playlist = new PlaylistAleatoria("Playlist Aleatória");
        
        List<Musica> todasMusicas = new ArrayList<>();
        for (Album album : albuns.values()) {
            todasMusicas.addAll(album.getMusicas().values());
        }
        
        // Adicionar até 10 músicas aleatórias (ou todas, se forem menos que 10)
        Random random = new Random();
        int numMusicas = Math.min(10, todasMusicas.size());
        
        for (int i = 0; i < numMusicas; i++) {
            int indice = random.nextInt(todasMusicas.size());
            playlist.adicionarMusica(todasMusicas.get(indice));
            todasMusicas.remove(indice); // Evitar duplicatas
        }
        
        return playlist;
    }
    
    /**
     * Salva o estado atual do sistema em um arquivo
     * @return true se salvou com sucesso, false caso contrário
     */
    public boolean salvarEstado() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO_SISTEMA))) {
            oos.writeObject(this);
            return true;
        } catch (IOException e) {
            System.err.println("Erro ao salvar estado: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Carrega o estado do sistema a partir de um arquivo
     * @return O sistema carregado ou null se falhou
     */
    public static Sistema carregarEstado() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARQUIVO_SISTEMA))) {
            return (Sistema) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar estado: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Salva o estado atual do sistema em um arquivo específico
     * @param nomeArquivo Nome do arquivo para salvar
     * @return true se salvou com sucesso, false caso contrário
     */
    public boolean salvarEstado(String nomeArquivo) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nomeArquivo))) {
            oos.writeObject(this);
            return true;
        } catch (IOException e) {
            System.err.println("Erro ao salvar estado: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Carrega o estado do sistema a partir de um arquivo específico
     * @param nomeArquivo Nome do arquivo para carregar
     * @return O sistema carregado ou null se falhou
     */
    public static Sistema carregarEstado(String nomeArquivo) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(nomeArquivo))) {
            return (Sistema) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar estado: " + e.getMessage());
            return null;
        }
    }
    
    // Getters
    
    public Map<String, Utilizador> getUtilizadores() {
        return new HashMap<>(utilizadores);
    }
    
    public Map<String, Album> getAlbuns() {
        return new HashMap<>(albuns);
    }
    
    public List<Playlist> getPlaylistsPublicas() {
        return new ArrayList<>(playlistsPublicas);
    }
    
    public Map<String, Integer> getContagemPorGenero() {
        return new HashMap<>(contagemPorGenero);
    }
    
    public Map<String, Integer> getContagemPorInterprete() {
        return new HashMap<>(contagemPorInterprete);
    }
}