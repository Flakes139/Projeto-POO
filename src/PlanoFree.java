import java.io.Serializable;

public class PlanoFree implements PlanoSubscricao, Serializable {
    private static final long serialVersionUID = 1L;
    
    @Override
    public boolean permiteCriarPlaylist() {
        return false; // Plano Free não permite criar playlists
    }
    
    @Override
    public boolean permiteAvancaRetrocede() {
        return false; // Plano Free não permite avançar/retroceder
    }
    
    @Override
    public boolean permiteSalvarBiblioteca() {
        return false; // Plano Free não permite salvar na biblioteca
    }
    
    @Override
    public int calcularPontosPorMusica(int pontuacaoAtual) {
        return 5; // Utilizadores Free recebem 5 pontos por música
    }
    
    @Override
    public String getNome() {
        return "Free";
    }
    
    @Override
    public String toString() {
        return "Plano Free";
    }
}