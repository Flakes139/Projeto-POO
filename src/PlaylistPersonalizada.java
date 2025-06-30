import java.io.Serializable;
import java.util.Collections;
import java.util.Random;

public class PlaylistPersonalizada extends Playlist implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean modoAleatorio;
    private Random random;
    
    public PlaylistPersonalizada(String nome) {
        super(nome);
        this.modoAleatorio = false;
        this.random = new Random();
    }
    
    @Override
    public Musica reproduzirProxima() {
        if (musicas.isEmpty()) {
            return null;
        }
        
        if (modoAleatorio) {
            // Seleciona uma música aleatória diferente da atual
            int novoIndice;
            if (musicas.size() > 1) {
                do {
                    novoIndice = random.nextInt(musicas.size());
                } while (novoIndice == indiceAtual);
                indiceAtual = novoIndice;
            } else {
                indiceAtual = 0;
            }
        } else {
            // Avança para a próxima música sequencialmente
            if (indiceAtual >= musicas.size() - 1) {
                indiceAtual = 0; // Volta ao início se chegou ao fim
            } else {
                indiceAtual++;
            }
        }
        
        Musica musica = musicas.get(indiceAtual);
        musica.reproduzir();
        return musica;
    }
    
    @Override
    public Musica reproduzirAnterior() {
        if (musicas.isEmpty() || indiceAtual <= 0) {
            return null;
        }
        
        indiceAtual--;
        Musica musica = musicas.get(indiceAtual);
        musica.reproduzir();
        return musica;
    }
    
    /**
     * Define se a playlist deve reproduzir em modo aleatório
     * @param modoAleatorio true para reproduzir aleatoriamente, false para sequencial
     */
    public void setModoAleatorio(boolean modoAleatorio) {
        this.modoAleatorio = modoAleatorio;
    }
    
    /**
     * Verifica se a playlist está em modo aleatório
     * @return true se estiver em modo aleatório
     */
    public boolean isModoAleatorio() {
        return modoAleatorio;
    }
    
    /**
     * Embaralha a ordem das músicas na playlist
     */
    public void embaralhar() {
        Collections.shuffle(musicas, random);
        indiceAtual = 0;
    }
    
    @Override
    public PlaylistPersonalizada clone() {
        PlaylistPersonalizada clone = (PlaylistPersonalizada) super.clone();
        clone.random = new Random();
        return clone;
    }
    
    @Override
    public String toString() {
        return super.toString() + (modoAleatorio ? " [MODO ALEATÓRIO]" : " [MODO SEQUENCIAL]");
    }
}