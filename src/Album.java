import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Album implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    
    private String nome;
    private Map<String, Musica> musicas;
    private String artista;
    private int ano;

    public Album(String nome, String artista, int ano) {
        this.nome = nome;
        this.artista = artista;
        this.ano = ano;
        this.musicas = new HashMap<>();
    }
    
    /**
     * Adiciona uma música ao álbum
     * @param musica Música a ser adicionada
     * @return true se a música foi adicionada com sucesso, false se já existia uma música com o mesmo nome
     */
    public boolean adicionarMusica(Musica musica) {
        if (musicas.containsKey(musica.getNome())) {
            return false;
        }
        musicas.put(musica.getNome(), musica);
        return true;
    }
    
    /**
     * Remove uma música do álbum
     * @param nomeMusica Nome da música a ser removida
     * @return true se a música foi removida com sucesso, false se não existia
     */
    public boolean removerMusica(String nomeMusica) {
        return musicas.remove(nomeMusica) != null;
    }
    
    /**
     * Retorna uma música do álbum pelo nome
     * @param nomeMusica Nome da música
     * @return A música com o nome especificado ou null se não existir
     */
    public Musica getMusica(String nomeMusica) {
        Musica musica = musicas.get(nomeMusica);
        return musica != null ? musica.clone() : null;
    }

    /**
     * Retorna todas as músicas do álbum
     * @return Um mapa com todas as músicas (clones)
     */
    public Map<String, Musica> getMusicas() {
        Map<String, Musica> musicasClone = new HashMap<>();
        
        for (Map.Entry<String, Musica> entry : this.musicas.entrySet()) {
            musicasClone.put(entry.getKey(), entry.getValue().clone());
        }
        return musicasClone;
    }
    
    /**
     * Retorna a duração total do álbum em segundos
     * @return Duração total em segundos
     */
    public int getDuracaoTotal() {
        int duracaoTotal = 0;
        for (Musica musica : musicas.values()) {
            duracaoTotal += musica.getDuracao();
        }
        return duracaoTotal;
    }
    
    /**
     * Retorna o número de músicas no álbum
     * @return Quantidade de músicas
     */
    public int getNumeroMusicas() {
        return musicas.size();
    }
    
    @Override
    public Album clone() {
        try {
            Album clone = (Album) super.clone();
            // Clonar mapa de músicas para evitar compartilhamento de referência
            clone.musicas = new HashMap<>();
            for (Map.Entry<String, Musica> entry : this.musicas.entrySet()) {
                clone.musicas.put(entry.getKey(), entry.getValue().clone());
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            // Isso não deveria acontecer já que implementamos Cloneable
            throw new RuntimeException("Erro ao clonar Album", e);
        }
    }
    
    // Getters e Setters
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }
    
    @Override
    public String toString() {
        return nome + " (" + ano + ") - " + artista + " - " + getNumeroMusicas() + " música(s)";
    }
}