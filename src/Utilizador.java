import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

public class Utilizador implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    
    private String nome;
    private String email;
    private String morada;
    private PlanoSubscricao plano;
    private int pontos;
    private Map<String, Album> albunsGuardados;
    private Map<String, Playlist> playlistsGuardadas;
    private List<Playlist> playlistsCriadas;
    private Map<String, Integer> contagemPorMusica; // Música -> Número de reproduções
    private Map<String, Integer> contagemPorGenero; // Gênero -> Número de reproduções
    private Map<String, Integer> contagemPorInterprete; // Intérprete -> Número de reproduções
    private int numeroTotalReproducoes;
    private Map<LocalDateTime, String> historicoReproducoes; // Registro de quando cada música foi reproduzida
    
    /**
     * Construtor para novo utilizador com plano Free por padrão
     * @param nome Nome do utilizador
     * @param email Email do utilizador
     * @param morada Morada do utilizador
     */
    public Utilizador(String nome, String email, String morada) {
        this.nome = nome;
        this.email = email;
        this.morada = morada;
        this.plano = new PlanoFree();
        this.pontos = 0;
        this.albunsGuardados = new HashMap<>();
        this.playlistsGuardadas = new HashMap<>();
        this.playlistsCriadas = new ArrayList<>();
        this.contagemPorMusica = new HashMap<>();
        this.contagemPorGenero = new HashMap<>();
        this.contagemPorInterprete = new HashMap<>();
        this.numeroTotalReproducoes = 0;
        this.historicoReproducoes = new HashMap<>();
    }
    
    /**
     * Construtor com plano específico
     * @param nome Nome do utilizador
     * @param email Email do utilizador
     * @param morada Morada do utilizador
     * @param plano Plano de subscrição
     */
    public Utilizador(String nome, String email, String morada, PlanoSubscricao plano) {
        this(nome, email, morada);
        setPlano(plano);
    }
    
    /**
     * Registra que o utilizador ouviu uma música
     * @param musica Música ouvida
     */
    public void ouvirMusica(Musica musica) {
        // Incrementar contagem de reproduções
        numeroTotalReproducoes++;
        
        // Registrar reprodução da música específica
        String nomeMusicaKey = musica.getNome() + "-" + musica.getInterprete();
        contagemPorMusica.put(nomeMusicaKey, contagemPorMusica.getOrDefault(nomeMusicaKey, 0) + 1);
        
        // Registrar reprodução por gênero
        String genero = musica.getGenero();
        contagemPorGenero.put(genero, contagemPorGenero.getOrDefault(genero, 0) + 1);
        
        // Registrar reprodução por intérprete
        String interprete = musica.getInterprete();
        contagemPorInterprete.put(interprete, contagemPorInterprete.getOrDefault(interprete, 0) + 1);
        
        // Calcular e adicionar pontos
        int pontosGanhos = plano.calcularPontosPorMusica(pontos);
        pontos += pontosGanhos;
        
        // Registrar no histórico
        historicoReproducoes.put(LocalDateTime.now(), nomeMusicaKey);
        
        // Se for uma música nova, incrementar contagem da própria música
        musica.incrementarContagem();
    }
    
    /**
     * Cria uma nova playlist (somente permitido para utilizadores Premium)
     * @param nome Nome da playlist
     * @return Playlist criada ou null se não permitido
     */
    public Playlist criarPlaylist(String nome) {
        if (!plano.permiteCriarPlaylist()) {
            return null;
        }
        
        PlaylistPersonalizada playlist = new PlaylistPersonalizada(nome);
        playlistsCriadas.add(playlist);
        guardarPlaylist(playlist);
        return playlist;
    }
    
    /**
     * Guarda um álbum na biblioteca (somente permitido para utilizadores Premium)
     * @param album Álbum a ser guardado
     * @return true se guardado com sucesso, false caso contrário
     */
    public boolean guardarAlbum(Album album) {
        if (!plano.permiteSalvarBiblioteca()) {
            return false;
        }
        
        albunsGuardados.put(album.getNome(), album.clone());
        return true;
    }
    
    /**
     * Guarda uma playlist na biblioteca (somente permitido para utilizadores Premium)
     * @param playlist Playlist a ser guardada
     * @return true se guardada com sucesso, false caso contrário
     */
    public boolean guardarPlaylist(Playlist playlist) {
        if (!plano.permiteSalvarBiblioteca()) {
            return false;
        }
        
        playlistsGuardadas.put(playlist.getNome(), playlist.clone());
        return true;
    }
    
    /**
     * Torna uma playlist criada pelo utilizador pública
     * @param nome Nome da playlist
     * @return true se a operação foi bem sucedida, false caso contrário
     */
    public boolean tornarPlaylistPublica(String nome) {
        for (Playlist playlist : playlistsCriadas) {
            if (playlist.getNome().equals(nome)) {
                playlist.setPublica(true);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Obtém músicas mais ouvidas pelo utilizador
     * @param limite Número máximo de músicas a retornar
     * @return Lista de músicas mais ouvidas
     */
    public List<String> getMusicasMaisOuvidas(int limite) {
        return contagemPorMusica.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(limite)
            .map(Map.Entry::getKey)
            .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * Obtém gêneros mais ouvidos pelo utilizador
     * @return Mapa de gêneros e suas contagens
     */
    public Map<String, Integer> getGenerosMaisOuvidos() {
        return new HashMap<>(contagemPorGenero);
    }
    
    /**
     * Obtém intérpretes mais ouvidos pelo utilizador
     * @return Mapa de intérpretes e suas contagens
     */
    public Map<String, Integer> getInterpretesMaisOuvidos() {
        return new HashMap<>(contagemPorInterprete);
    }
    
    /**
     * Obtém o número de músicas ouvidas num período específico
     * @param inicio Data/hora inicial
     * @param fim Data/hora final
     * @return Número de músicas ouvidas no período
     */
    public int getNumeroBusicasOuvidasPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return (int) historicoReproducoes.keySet().stream()
            .filter(data -> data.isAfter(inicio) && data.isBefore(fim))
            .count();
    }
    
    /**
     * Define um novo plano de subscrição para o utilizador
     * @param novoPlano Novo plano de subscrição
     */
    public void setPlano(PlanoSubscricao novoPlano) {
        this.plano = novoPlano;
        
        // Se for upgrade para Premium Top, adicionar bônus inicial
        if (novoPlano instanceof PlanoPremiumTop) {
            pontos += ((PlanoPremiumTop) novoPlano).getBonusInicial();
        }
    }
    
    @Override
    public Utilizador clone() {
        try {
            Utilizador clone = (Utilizador) super.clone();
            
            // Clonar as estruturas de dados para evitar compartilhamento de referências
            clone.albunsGuardados = new HashMap<>();
            for (Map.Entry<String, Album> entry : this.albunsGuardados.entrySet()) {
                clone.albunsGuardados.put(entry.getKey(), entry.getValue().clone());
            }
            
            clone.playlistsGuardadas = new HashMap<>();
            for (Map.Entry<String, Playlist> entry : this.playlistsGuardadas.entrySet()) {
                clone.playlistsGuardadas.put(entry.getKey(), entry.getValue().clone());
            }
            
            clone.playlistsCriadas = new ArrayList<>();
            for (Playlist playlist : this.playlistsCriadas) {
                clone.playlistsCriadas.add(playlist.clone());
            }
            
            clone.contagemPorMusica = new HashMap<>(this.contagemPorMusica);
            clone.contagemPorGenero = new HashMap<>(this.contagemPorGenero);
            clone.contagemPorInterprete = new HashMap<>(this.contagemPorInterprete);
            clone.historicoReproducoes = new HashMap<>(this.historicoReproducoes);
            
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Erro ao clonar Utilizador", e);
        }
    }
    
    // Getters
    
    public String getNome() {
        return nome;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getMorada() {
        return morada;
    }
    
    public PlanoSubscricao getPlano() {
        return plano;
    }
    
    public int getPontos() {
        return pontos;
    }
    
    public int getNumeroTotalReproducoes() {
        return numeroTotalReproducoes;
    }
    
    public int getNumeroPlaylists() {
        return playlistsCriadas.size();
    }
    
    public List<Playlist> getPlaylistsCriadas() {
        List<Playlist> copia = new ArrayList<>();
        for (Playlist playlist : playlistsCriadas) {
            copia.add(playlist.clone());
        }
        return copia;
    }
    
    public Map<String, Album> getAlbunsGuardados() {
        Map<String, Album> copia = new HashMap<>();
        for (Map.Entry<String, Album> entry : albunsGuardados.entrySet()) {
            copia.put(entry.getKey(), entry.getValue().clone());
        }
        return copia;
    }
    
    public Map<String, Playlist> getPlaylistsGuardadas() {
        Map<String, Playlist> copia = new HashMap<>();
        for (Map.Entry<String, Playlist> entry : playlistsGuardadas.entrySet()) {
            copia.put(entry.getKey(), entry.getValue().clone());
        }
        return copia;
    }
    
    // Setters
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setMorada(String morada) {
        this.morada = morada;
    }
    
    @Override
    public String toString() {
        return nome + " (" + email + ") - Plano: " + plano.getNome() + 
               " - Pontos: " + pontos + " - Músicas ouvidas: " + numeroTotalReproducoes;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Utilizador outro = (Utilizador) obj;
        return email.equals(outro.email);
    }
    
    @Override
    public int hashCode() {
        return email.hashCode();
    }
}