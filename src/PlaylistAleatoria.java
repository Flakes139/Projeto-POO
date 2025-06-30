import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PlaylistAleatoria extends Playlist implements Serializable {
    private static final long serialVersionUID = 1L;
    private Random random;
    
    public PlaylistAleatoria(String nome) {
        super(nome);
        this.random = new Random();
    }
    
    @Override
    public Musica reproduzirProxima() {
        if (musicas.isEmpty()) {
            return null;
        }
        
        // Seleciona uma música aleatória
        indiceAtual = random.nextInt(musicas.size());
        Musica musica = musicas.get(indiceAtual);
        musica.reproduzir();
        return musica;
    }
    
    @Override
    public Musica reproduzirAnterior() {
        // Playlists aleatórias não permitem retroceder
        return null;
    }
    
    /**
     * Embaralha a ordem das músicas na playlist
     */
    public void embaralhar() {
        Collections.shuffle(musicas, random);
        indiceAtual = 0;
    }
    
    @Override
    public PlaylistAleatoria clone() {
        PlaylistAleatoria clone = (PlaylistAleatoria) super.clone();
        clone.random = new Random();
        return clone;
    }
    
    @Override
    public String toString() {
        return super.toString() + " [ALEATÓRIA]";
    }
}