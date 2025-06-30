import java.io.Serializable;

public class PlanoPremiumTop implements PlanoSubscricao, Serializable {
    private static final long serialVersionUID = 1L;
    private static final int BONUS_INICIAL = 100;
    private static final double PERCENTAGEM_BONUS = 0.025; // 2.5%
    
    @Override
    public boolean permiteCriarPlaylist() {
        return true; // Plano Premium Top permite criar playlists
    }
    
    @Override
    public boolean permiteAvancaRetrocede() {
        return true; // Plano Premium Top permite avançar/retroceder
    }
    
    @Override
    public boolean permiteSalvarBiblioteca() {
        return true; // Plano Premium Top permite salvar na biblioteca
    }
    
    @Override
    public int calcularPontosPorMusica(int pontuacaoAtual) {
        // Utilizadores Premium Top recebem 2.5% dos pontos acumulados
        return (int) Math.round(pontuacaoAtual * PERCENTAGEM_BONUS);
    }
    
    /**
     * Retorna o bônus inicial de pontos concedido ao aderir ao plano
     * @return pontos de bônus
     */
    public int getBonusInicial() {
        return BONUS_INICIAL;
    }
    
    @Override
    public String getNome() {
        return "Premium Top";
    }
    
    @Override
    public String toString() {
        return "Plano Premium Top";
    }
}