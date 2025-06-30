import java.io.Serializable;

public class MusicaExplicita extends Musica implements Serializable {
    private static final long serialVersionUID = 1L;
    private String avisoConteudo;
    
    public MusicaExplicita(String nome, String interprete, String editora, String letra, 
                          String genero, int duracao, String avisoConteudo) {
        super(nome, interprete, editora, letra, genero, duracao);
        this.avisoConteudo = avisoConteudo;
    }
    
    @Override
    public void reproduzir() {
        System.out.println("AVISO DE CONTEÚDO EXPLÍCITO: " + avisoConteudo);
        super.reproduzir();
    }
    
    @Override
    public MusicaExplicita clone() {
        MusicaExplicita clone = (MusicaExplicita) super.clone();
        return clone;
    }
    
    public String getAvisoConteudo() {
        return avisoConteudo;
    }
    
    public void setAvisoConteudo(String avisoConteudo) {
        this.avisoConteudo = avisoConteudo;
    }
    
    @Override
    public String toString() {
        return super.toString() + " [EXPLÍCITA]";
    }
}