import java.io.Serializable;

public interface PlanoSubscricao extends Serializable {
    /**
     * Verifica se o plano permite criar playlists
     * @return true se o plano permite criar playlists, false caso contrário
     */
    boolean permiteCriarPlaylist();
    
    /**
     * Verifica se o plano permite avançar ou retroceder nas músicas
     * @return true se o plano permite avançar/retroceder, false caso contrário
     */
    boolean permiteAvancaRetrocede();
    
    /**
     * Verifica se o plano permite salvar músicas na biblioteca
     * @return true se o plano permite salvar músicas, false caso contrário
     */
    boolean permiteSalvarBiblioteca();
    
    /**
     * Calcula pontos por música reproduzida
     * @param pontuacaoAtual pontuação atual do utilizador
     * @return quantidade de pontos que o utilizador ganha
     */
    int calcularPontosPorMusica(int pontuacaoAtual);
    
    /**
     * Retorna o nome do plano
     * @return nome do plano de subscrição
     */
    String getNome();
}