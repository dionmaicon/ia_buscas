package job_one_ai;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author dion
 */

import java.util.ArrayList;


class No {
    private No pai;
    private String nome;
    private ArrayList<No> filhos;
    private int nivel;
    private int[][] v;
    private int heuristica;
    private boolean aberto = false;

    public boolean isAberto() {
        return aberto;
    }

    public void setAberto(boolean aberto) {
        this.aberto = aberto;
    }

    public No(No pai, int nivel, String nome,  int[][] v) {
        this.pai = pai;
        this.nome = nome;
        this.nivel = nivel;
        this.heuristica = 0;
        this.v = v;
        this.filhos = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public No getPai() {
        return pai;
    }

    public void setPai(No pai) {
        this.pai = pai;
    }

    public ArrayList<No> getFilhos() {
        if(filhos.isEmpty()){
            return null;
        }
        return filhos;
    }

    public void addFilho(No filho) {
        this.filhos.add(filho);
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public int[][] getV() {
        return v;
    }

    public void setV(int[][] v) {
        this.v = v;
    }

    void setHeuristica(int heuristica) {
        this.heuristica = heuristica;
    }

    public int getHeuristica() {
        return heuristica;
    }
    
    
    
    
}
