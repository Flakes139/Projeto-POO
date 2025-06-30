import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Playlist implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    
    protected String nome;
    protected List<Musica> musicas;
    protected boolean publica;
    protected int indiceAtual;
    
    public Playlist(String nome) {
        this.nome = nome;
        this.musicas = new ArrayList<>();
        this.publica = false;
        this.indiceAtual = 0;
    }
    
    /**
     * Adiciona uma música à playlist
     * @param musica Música a ser adicionada
     * @return true se a música foi adicionada com sucesso
     */
    public boolean adicionarMusica(Musica musica) {
        return musicas.add(musica);
    }
    
    /**
     * Remove uma música da playlist
     * @param musica Música a ser removida
     * @return true se a música foi removida com sucesso
     */
    public boolean removerMusica(Musica musica) {
        return musicas.remove(musica);
    }
    
    /**
     * Reproduz a próxima música da playlist
     * @return A música reproduzida ou null se não houver mais músicas
     */
    public abstract Musica reproduzirProxima();
    
    /**
     * Reproduz a música anterior da playlist
     * @return A música reproduzida ou null se não for possível retroceder
     */
    public abstract Musica reproduzirAnterior();
    
    /**
     * Verifica se é possível avançar para a próxima música
     * @return true se houver próxima música
     */
    public boolean temProxima() {
        return indiceAtual < musicas.size() - 1;
    }
    
    /**
     * Verifica se é possível retroceder para a música anterior
     * @return true se houver música anterior
     */
    public boolean temAnterior() {
        return indiceAtual > 0;
    }
    
    /**
     * Reinicia a playlist para o início
     */
    public void reiniciar() {
        indiceAtual = 0;
    }
    
    /**
     * Retorna a duração total da playlist em segundos
     * @return Duração total em segundos
     */
    public int getDuracaoTotal() {
        int duracaoTotal = 0;
        for (Musica musica : musicas) {
            duracaoTotal += musica.getDuracao();
        }
        return duracaoTotal;
    }
    
    @Override
    public Playlist clone() {
        try {
            Playlist clone = (Playlist) super.clone();
            // Clonar lista de músicas para evitar compartilhamento de referência
            clone.musicas = new ArrayList<>();
            for (Musica musica : this.musicas) {
                clone.musicas.add(musica.clone());
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Erro ao clonar Playlist", e);
        }
    }
    
    // Getters e Setters
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public List<Musica> getMusicas() {
        List<Musica> copiaMusicas = new ArrayList<>();
        for (Musica musica : musicas) {
            copiaMusicas.add(musica.clone());
        }
        return copiaMusicas;
    }
    
    public int getNumeroMusicas() {
        return musicas.size();
    }
    
    public boolean isPublica() {
        return publica;
    }
    
    public void setPublica(boolean publica) {
        this.publica = publica;
    }
    
    @Override
    public String toString() {
        return nome + " (" + getNumeroMusicas() + " música(s), " + 
               formatarDuracao(getDuracaoTotal()) + ")" + (publica ? " [PÚBLICA]" : "");
    }
    
    protected String formatarDuracao(int segundos) {
        int minutos = segundos / 60;
        int segRestantes = segundos % 60;
        int horas = minutos / 60;
        minutos = minutos % 60;
        
        if (horas > 0) {
            return String.format("%d:%02d:%02d", horas, minutos, segRestantes);
        } else {
            return String.format("%d:%02d", minutos, segRestantes);
        }
    }
}