import java.io.Serializable;

public class PlanoPremiumBase implements PlanoSubscricao, Serializable {
    private static final long serialVersionUID = 1L;
    
    @Override
    public boolean permiteCriarPlaylist() {
        return true; // Plano Premium Base permite criar playlists
    }
    
    @Override
    public boolean permiteAvancaRetrocede() {
        return true; // Plano Premium Base permite avançar/retroceder
    }
    
    @Override
    public boolean permiteSalvarBiblioteca() {
        return true; // Plano Premium Base permite salvar na biblioteca
    }
    
    @Override
    public int calcularPontosPorMusica(int pontuacaoAtual) {
        return 10; // Utilizadores Premium Base recebem 10 pontos por música
    }
    
    @Override
    public String getNome() {
        return "Premium Base";
    }
    
    @Override
    public String toString() {
        return "Plano Premium Base";
    }
}