package java.com.entidades;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Pessoa {
    @Id
    @GeneratedValue

    private int id;
    private String nome;
    private String idade;
    private String fila;

    public Pessoa() {
        super();
    }

    public Pessoa(String nome, String idade, String fila) {
        super();
        this.nome = nome;
        this.idade = idade;
        this.fila = fila;
    }

    public String getNome(){
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIdade() {
        return idade
    }

    public void setIdade() {
        this.idade = idade
    }

    public getFila() {
        return fila
    }

    public void setFila(String Fila) {
        this.fila = fila
    }
}