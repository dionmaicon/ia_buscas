/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author dion
 */

import java.util.LinkedList;

class No {
    private No pai;
    private LinkedList<No> filhos;
    private int nivel;
    private int[][] v;

    public No(No pai, int nivel, int[][] v) {
        this.pai = pai;
        this.nivel = nivel;
        this.v = v;
    }

    public No getPai() {
        return pai;
    }

    public void setPai(No pai) {
        this.pai = pai;
    }

    public LinkedList<No> getFilhos() {
        return filhos;
    }

    public void setFilhos(LinkedList<No> filhos) {
        this.filhos = filhos;
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
    
    
    
    
}
