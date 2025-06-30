import java.io.Serializable;
import java.util.List;

public class ListaFavoritos extends PlaylistPersonalizada implements Serializable {
    private static final long serialVersionUID = 1L;
    private String genero; // Opcional para filtrar por gênero
    private int duracaoMaxima; // Opcional para limitar duração em segundos
    private boolean apenasExplicitas; // Opcional para mostrar apenas músicas explícitas
    
    /**
     * Cria uma lista de favoritos sem restrições
     * @param nome Nome da lista
     */
    public ListaFavoritos(String nome) {
        super(nome);
        this.genero = null;
        this.duracaoMaxima = -1; // Sem limite
        this.apenasExplicitas = false;
    }
    
    /**
     * Cria uma lista de favoritos com restrições
     * @param nome Nome da lista
     * @param genero Gênero para filtrar (null para não filtrar)
     * @param duracaoMaxima Duração máxima em segundos (-1 para sem limite)
     * @param apenasExplicitas True para incluir apenas músicas explícitas
     */
    public ListaFavoritos(String nome, String genero, int duracaoMaxima, boolean apenasExplicitas) {
        super(nome);
        this.genero = genero;
        this.duracaoMaxima = duracaoMaxima;
        this.apenasExplicitas = apenasExplicitas;
    }
    
    /**
     * Adiciona uma música à lista de favoritos se ela atender aos critérios
     * @param musica Música a ser adicionada
     * @return true se a música foi adicionada, false caso contrário
     */
    @Override
    public boolean adicionarMusica(Musica musica) {
        // Verificar se a música atende aos critérios de gênero
        if (genero != null && !musica.getGenero().equalsIgnoreCase(genero)) {
            return false;
        }
        
        // Verificar se a música é explícita (se necessário)
        if (apenasExplicitas && !(musica instanceof MusicaExplicita)) {
            return false;
        }
        
        // Verificar se adicionar essa música não ultrapassa a duração máxima
        if (duracaoMaxima > 0 && (getDuracaoTotal() + musica.getDuracao() > duracaoMaxima)) {
            return false;
        }
        
        return super.adicionarMusica(musica);
    }
    
    @Override
    public ListaFavoritos clone() {
        return (ListaFavoritos) super.clone();
    }
    
    // Getters e Setters
    
    public String getGenero() {
        return genero;
    }
    
    public void setGenero(String genero) {
        this.genero = genero;
    }
    
    public int getDuracaoMaxima() {
        return duracaoMaxima;
    }
    
    public void setDuracaoMaxima(int duracaoMaxima) {
        this.duracaoMaxima = duracaoMaxima;
    }
    
    public boolean isApenasExplicitas() {
        return apenasExplicitas;
    }
    
    public void setApenasExplicitas(boolean apenasExplicitas) {
        this.apenasExplicitas = apenasExplicitas;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append(" [FAVORITOS");
        
        if (genero != null) {
            sb.append(", Gênero: ").append(genero);
        }
        
        if (duracaoMaxima > 0) {
            sb.append(", Máx: ").append(formatarDuracao(duracaoMaxima));
        }
        
        if (apenasExplicitas) {
            sb.append(", Apenas Explícitas");
        }
        
        sb.append("]");
        return sb.toString();
    }
}