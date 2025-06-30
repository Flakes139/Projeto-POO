import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Musica implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    
    private String nome;
    private String interprete;
    private String editora;
    private String letra;
    private ArrayList<String> conteudoMusical;
    private String genero;
    private int duracao; // em segundos
    private int contagemReproducoes;

    public Musica(String nome, String interprete, String editora, String letra, String genero, int duracao) {
        this.nome = nome;
        this.interprete = interprete;
        this.editora = editora;
        this.letra = letra;
        this.conteudoMusical = new ArrayList<>();
        this.genero = genero;
        this.duracao = duracao;
        this.contagemReproducoes = 0;
    }

    public void adicionarLinhaMusical(String linha) {
        this.conteudoMusical.add(linha);
    }

    public void reproduzir() {
        System.out.println("\n=== Reproduzindo: " + nome + " ===");
        System.out.println("Intérprete: " + interprete);
        System.out.println("Género: " + genero);
        System.out.println("Duração: " + formatarDuracao());
        System.out.println("Editora: " + editora);
        System.out.println("\n--- Letra ---");
        System.out.println(letra);
        
        // Exibir conteúdo musical se existir
        if (!conteudoMusical.isEmpty()) {
            System.out.println("\n--- Conteúdo Musical ---");
            for (String linha : conteudoMusical) {
                System.out.println(linha);
            }
        }
        
        System.out.println("\n===========================\n");

        incrementarContagem();
    }

    public void incrementarContagem() {
        this.contagemReproducoes++;
    }

    private String formatarDuracao() {
        int minutos = duracao / 60;
        int segundos = duracao % 60;
        return String.format("%d:%02d", minutos, segundos);
    }

    @Override
    public Musica clone() {
        try {
            Musica clone = (Musica) super.clone();
            // Clonar lista de conteúdo musical para evitar compartilhamento de referência
            clone.conteudoMusical = new ArrayList<>(this.conteudoMusical);
            return clone;
        } catch (CloneNotSupportedException e) {
            // Isso não deveria acontecer já que implementamos Cloneable
            throw new RuntimeException("Erro ao clonar Musica", e);
        }
    }

    // Getters e Setters

    public String getNome() {
        return nome;
    }

    public String getInterprete() {
        return interprete;
    }

    public String getEditora() {
        return editora;
    }

    public String getLetra() {
        return letra;
    }

    public List<String> getConteudoMusical() {
        return new ArrayList<>(conteudoMusical); // Retorna uma cópia para proteger o encapsulamento
    }

    public String getGenero() {
        return genero;
    }

    public int getDuracao() {
        return duracao;
    }

    public int getContagemReproducoes() {
        return contagemReproducoes;
    }
    
    @Override
    public String toString() {
        return nome + " - " + interprete + " (" + formatarDuracao() + ")";
    }
}