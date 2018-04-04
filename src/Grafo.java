
import java.util.ArrayList;
import java.util.LinkedList;

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
        int[][] estado_inicial = {{1,8,3},{0,5,4},{6,2,7}};
        
        //Objetivo 
        int[][] estado_objetivo = {{4,0,5},{3,8,1},{6,2,7}};
        
        /**
         4 0 5 

         3 8 1 

         6 2 7 
         */
        
        No inicial = new No(null, 0, estado_inicial);
        
        Grafo grafo = new Grafo(inicial, estado_objetivo);
     
        
        grafo.nextStep();
        grafo.nextStep();
        grafo.nextStep();
        grafo.nextStep();
        grafo.nextStep();
        grafo.nextStep();
        grafo.nextStep();
        grafo.nextStep();
        grafo.nextStep();
        grafo.nextStep();
        grafo.nextStep();
        
        grafo.nextStep();
        grafo.nextStep();
        grafo.nextStep();
        grafo.nextStep();
        
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

    private void nextStep() {
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
                if( v != null){
                    
                    if (!isDuplicada(v)){
                     No aux = new No(pai, step, v); 
                     nos.addLast(aux);
                        if (isObjetivo(v)){
                           imprimirCaminho(aux);
                           System.exit(0);
                        }
                    } 
                     
                    
                }
                
            }
        }   
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

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(aux.getV()[i][j]+ " ");
            }
            System.out.println("");
        }
        System.out.println("");
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