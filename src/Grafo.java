
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.JOptionPane;

class Grafo{
    private LinkedList<No> nos ;
    private int[][] objetivo;
    private static int step = 0;
   
    
    
    public Grafo(No no, int[][] objetivo) {
        this.objetivo = objetivo;
        nos = new LinkedList<>();
        nos.addFirst(no);
    }
   
    public static void main(String[] args) {
        
        
        //Estado Inicial
        int[][] estado_inicial = {{0,1,2},{7,8,3},{6,5,4}};
        
        //Objetivo 
   int[][] estado_objetivo = {{1,2,3},{8,0,4},{7,6,5}};
//        int[][] estado_objetivo = {{4,2,5},{6,8,1},{3,7,0}};
        
        /**
            5 6 7 
            2 4 1 
            3 0 8 
         */
     
        /**
            4 2 5 
            6 8 1 
            3 7 0
        */
        
        No inicial = new No(null, 0, "Inicial",estado_inicial);
        
        Grafo grafo = new Grafo(inicial, estado_objetivo);
     
        
        grafo.nextBFS();
        grafo.nextBFS();
        grafo.nextBFS();
        grafo.nextBFS();
        grafo.nextBFS();
        grafo.nextBFS();
        grafo.nextBFS();
        grafo.nextBFS();
        grafo.nextBFS();
        
//        grafo.nextDFS(inicial);
        
//        grafo.imprimirEstados();
        //grafo.imprimirEstados();
        
        
    }
     //Retorna uma matriz com a o proximo estado, direcao Alto = 0, Direita = 1, Baixo = 2, Esquerda = 3
    int[][] calcularEstado(int vs[][], int direcao, Position position){
        int[][] v = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                       v[i][j]= vs[i][j];
            }
        }

        if(direcao == 0 && position.getX() != 2){ // Verificar se é possível mover para o alto
            v[position.getX()][position.getY()] = v[position.getX() + 1][position.getY()];
            v[position.getX() + 1][position.getY()] = 0;
            return v;
            
        }else if(direcao == 1 && position.getY() != 2){ // Verificar se é possível mover para  direita
            v[position.getX()][position.getY()] = v[position.getX()][position.getY() + 1];
            v[position.getX()][position.getY() + 1] =  0;
            return v;
      }else if(direcao == 2 && position.getX() != 0){ // Verificar se é possível mover para baixo
            v[position.getX()][position.getY()] = v[position.getX() - 1][position.getY()];
            v[position.getX() - 1][position.getY()] = 0;
            return v;
        }else if(direcao == 3 && position.getY() != 0){ // Verificar se é possível mover para esquerda
            v[position.getX()][position.getY()] = v[position.getX()][position.getY() - 1 ];
            v[position.getX()][position.getY() - 1 ] = 0;
            return v;
        }else{
            return null;
        }

    }

    private void nextBFS() {
        step++;
        //Buscar cada estado com nível anterior a este passo.
        ArrayList<No> estadosAnteriores = new ArrayList<>();
        for (No no : nos) {
            if(no.getNivel() == (step - 1)){
                estadosAnteriores.add(no);
            }
        }
        //Para cada estado anterior abrir suas opções.
        for (No pai : estadosAnteriores) {
            Position position = getXY(pai.getV()); // Busca a posição do zero
            
            for (int i = 0; i < 4; i++) {
                int[][] v = calcularEstado(pai.getV(), i, position);
                //Se v for diferente de null, posso criar o novo estado;
                String nome = getLado(i);
                if( v != null){
                    
                    if (!isDuplicada(v)){
                     No aux = new No(pai, step,nome, v); 
                     nos.addLast(aux);
                        if (isObjetivo(v)){
                           imprimirCaminho(aux);
                           step = 0;
                           return;
                        }
                    } 
                     
                    
                }
                
            }
        }   
    }
    
    private void nextDFS(No no) {
        step++;
        if (isObjetivo(no.getV())){
            JOptionPane.showConfirmDialog(null, "Objetivo encontrado", "Alvo", JOptionPane.PLAIN_MESSAGE);
            imprimirCaminho(no);
            step = 0;
            nos.clear();            
            System.exit(0);
            return;
        }if(no.getNivel() == 1500){
            JOptionPane.showConfirmDialog(null, "A busca com  1500 níveis de profundidade não retornou sucesso", "Alvo", JOptionPane.PLAIN_MESSAGE);
            imprimirEstados();
            System.exit(0);
            return;
        }

        Position position = getXY(no.getV()); // Busca a posição do zero
        
        for (int i = 0; i < 4; i++) {
            int[][] v = calcularEstado(no.getV(), i, position);
            //Se v for diferente de null, posso criar o novo estado;
            String nome = getLado(i);
            if( v != null){
                if (!isDuplicada(v)){
                   No aux = new No(no, step,nome, v); 
                   nos.addLast(aux);                   
                   no.addFilho(aux);
                }
            }
        }
        
        ArrayList<No> filhos = no.getFilhos();
        if(filhos == null){
            return;
        }
        
        filhos.forEach((No filho) -> {
            try {
                nextDFS(filho);
                
            } catch (StackOverflowError e) {
                JOptionPane.showMessageDialog(null, "Estouro de Pilha", "Alvo Não encontrado", JOptionPane.ERROR_MESSAGE);
                System.out.println("");
                System.exit(0);
            }
        });
           
    }
    
    private Position getXY(int[][] v){
        Position position = new Position();
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(v[i][j] == 0){
                        position.setX(i);
                        position.setY(j);
                    break;
                }
            }
        }
        return position;
    }

    private void imprimirEstados() {
        int cont = 1;
        for (No no : nos) {
            System.out.println("Nível: "+ no.getNivel() +", Id: "+cont++);
            int[][] v = no.getV();
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    System.out.print(""+ v[i][j]+" ");                    
                }
                System.out.println("\n");                    
            }
            System.out.println("\n");                    
        }
    }

    private boolean isObjetivo(int[][] v) {
        int cont = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(this.objetivo[i][j] == v[i][j]) cont++;
            }
        }
        if(cont == 9){
            System.out.println("Achou o objetivo!");
            return true;
        }
        return false;
    }
    /**Retorna true se a matriz já foi um estado anterior, falso caso contrario*/
    private boolean isDuplicada(int[][] v) {
        int cont;
       
        for (No no : nos) {
            cont = 0;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if(no.getV()[i][j] == v[i][j]) cont++;
                }
            }
            if (cont == 9){
                 return true;
            }
        }
        return false;
    }

    private void imprimirCaminho(No aux) {
        if(aux.getNivel() == 0) return;
        imprimirCaminho(aux.getPai());
        System.out.println("Nível: " + aux.getNivel() +" Movimento: "+ aux.getNome());
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(aux.getV()[i][j]+ " ");
            }
            System.out.println("");
        }
        System.out.println("");
    }

    private String getLado(int i) {
        switch(i){
            case 0 :
                return "BAIXO";
            case 1 :
                return "DIREITA";
            case 2 :
                return "CIMA";
            default:
                return "ESQUERDA";
        }
    }

    private static class Position {
        private int x, y;
        public Position() {
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }
        
    }
    
    
    
}