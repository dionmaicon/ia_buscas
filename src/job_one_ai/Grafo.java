package job_one_ai;


import job_one_ai.No;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.JOptionPane;

class Grafo{
    private LinkedList<No> nos ;
    private int[][] objetivo;
    private static int step = 0;
    private Display display = null;
    public static final String ANSI_RED = "\u001B[31m";
    public Grafo(No no, int[][] objetivo) {
        this.objetivo = objetivo;
        nos = new LinkedList<>();
        nos.addFirst(no);
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

    public boolean BFS() {
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
                           return true;
                        }
                    } 
                     
                    
                }
                
            }
        }   
        return false;
    }
    
    public void DFS(No no) {
        step++;
        if (isObjetivo(no.getV())){
            JOptionPane.showConfirmDialog(null, "Objetivo encontrado", "Alvo", JOptionPane.PLAIN_MESSAGE);
             imprimirCaminho(no);
            step = 0;
            System.exit(0);
            nos.clear();            
            return;
        }if(no.getNivel() == 1500){
            JOptionPane.showConfirmDialog(null, "A busca com  1500 níveis de profundidade não retornou sucesso", "Alvo", JOptionPane.PLAIN_MESSAGE);

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
                DFS(filho);
                
            } catch (StackOverflowError e) {
                JOptionPane.showMessageDialog(null, "Estouro de Pilha", "Alvo Não encontrado", JOptionPane.ERROR_MESSAGE);
                System.out.println("");
                System.exit(0);
            }
        });
           
    }
    
    public boolean gulosa(String heuristica){
        step++;
        //Buscar cada estado com nível anterior a este passo.
        ArrayList<No> estadosAnteriores = new ArrayList<>();
        
        for (No no : nos) {
            if(no.getNivel() == (step - 1)){
                estadosAnteriores.add(no);
            }
        }

        //Ordenar estados anteriores e pegar o melhor.
        No pai = buscarMelhorHeuristicaGulosa(estadosAnteriores);
        //Para cada estado anterior abrir suas opções.
        if (pai == null){
            System.out.println("Não foi possível encontrar a solução para o caminho escolhido.");
            return false;
        }
        pai.setAberto(true);
        Position position = getXY(pai.getV()); // Busca a posição do zero

        for (int i = 0; i < 4; i++) {
            int[][] v = calcularEstado(pai.getV(), i, position);
            //Se v for diferente de null, posso criar o novo estado;
            String lado = getLado(i);
            if( v != null){

                if (!isDuplicada(v)){
                 No aux = new No(pai, step, lado, v); 
                 if(heuristica.equals("H1")) aux.setHeuristica(calcularNumeroDePecasForaDoLugar(v));
                 if(heuristica.equals("H2")) aux.setHeuristica(calcularDistanciaManhatan(v));
                 nos.addLast(aux);
                    if (isObjetivo(v)){
                        imprimirCaminho(aux);
                       step = 0;
                       return true;
                    }
                } 
            }

        }
        return false;   
    }
    
    public boolean estrela(String heuristica){
        step++;
        //Ordenar estados anteriores e pegar o melhor.
        No pai = buscarMelhorHeuristicaEstrela();
        //Para cada estado anterior abrir suas opções.
        
        if (pai == null){
            System.out.println("Não foi possível encontrar a solução para o caminho escolhido.");
            return false;
        }
        
        pai.setAberto(true);
        Position position = getXY(pai.getV()); // Busca a posição do zero

        for (int i = 0; i < 4; i++) {
            int[][] v = calcularEstado(pai.getV(), i, position);
            //Se v for diferente de null, posso criar o novo estado;
            String lado = getLado(i);
            if( v != null){

                if (!isDuplicada(v)){
                 No aux = new No(pai, step, lado, v); 
                 if(heuristica.equals("H1")) aux.setHeuristica(calcularNumeroDePecasForaDoLugar(v));
                 if(heuristica.equals("H2")) aux.setHeuristica(calcularDistanciaManhatan(v));
                 nos.addLast(aux);
                    if (isObjetivo(v)){
                        imprimirCaminho(aux);
                       step = 0;
                       return true;
                    }
                } 
            }

        }
        return false;
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

    public void imprimirCaminho(No aux) {
        if(aux.getNivel() == 0){
            
            
            display.setjTextAreaSaida(display.getjTextAreaSaida().getText() + "\n---------------------Saída--------------------\n");
            
            System.out.println("Total de nos expandidos: " +this.nos.size());
            display.setjTextAreaSaida(display.getjTextAreaSaida().getText() + "Total de nos expandidos: " +this.nos.size()+ "\n");
            
            System.out.println("Nível: " + aux.getNivel() + " Movimento: "+ aux.getNome()); 
            display.setjTextAreaSaida(display.getjTextAreaSaida().getText() + "Nível: " + aux.getNivel() + " Movimento: "+ aux.getNome()+"\n");
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    System.out.print(aux.getV()[i][j] +" ");
                    display.setjTextAreaSaida(display.getjTextAreaSaida().getText() + aux.getV()[i][j] + " ");
                }
                System.out.println("");
                display.setjTextAreaSaida(display.getjTextAreaSaida().getText() + "\n");
            }
            System.out.println("");
            display.setjTextAreaSaida(display.getjTextAreaSaida().getText() + "\n");
            return;
        } 
        imprimirCaminho(aux.getPai());
        System.out.println("Nível: "+aux.getNivel()+" Movimento: "+aux.getNome());
        display.setjTextAreaSaida(display.getjTextAreaSaida().getText() + "Nível: "+aux.getNivel()+" Movimento: "+aux.getNome() + "\n");
            
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(aux.getV()[i][j] +" ");
                display.setjTextAreaSaida(display.getjTextAreaSaida().getText() + aux.getV()[i][j] + " ");
            }
            System.out.println("");
            display.setjTextAreaSaida(display.getjTextAreaSaida().getText() + "\n");
        }
        
        return ;
    }

    private int calcularDistanciaManhatan(int[][] v) {
        int soma = 0;
        for (int i = 0; i < v.length; i++) {
            for (int j = 0; j < v.length; j++) {
                if(v[i][j] != objetivo[i][j] && v[i][j] != 0){ //Se for diferente calcular distancia
                    int valor = v[i][j];
                    for (int k = 0; k < v.length; k++) {
                        for (int l = 0; l < v.length; l++) {
                            if(objetivo[k][l] == valor){
                                 soma += Math.abs(i-k) + Math.abs(j-l);
                            }
                        }  
                    }
                }
            }
        }
        return soma;
    }
    
    private int calcularNumeroDePecasForaDoLugar(int[][] v){
        int cont = 0;
        for (int i = 0; i < v.length; i++) {
            for (int j = 0; j < v.length; j++) {
                if(v[i][j] != objetivo[i][j] && v[i][j]!= 0) cont++;
            }
        }
        return cont;
    }
    
    private No buscarMelhorHeuristicaGulosa(ArrayList<No> estadosAnteriores) {
        
        try {
            No melhor = estadosAnteriores.get(0);
            
            int menor = 1000;
        for (No no : estadosAnteriores) {
            if(no.getHeuristica() < menor && no.isAberto() == false){
            menor = no.getHeuristica();
            melhor = no;            
            }
        }
        return melhor;
        
        
        } catch (Exception e) {
            System.out.println("A busca não encontrou resultados.");
            return null;
        }
    }
    
    private No buscarMelhorHeuristicaEstrela() {
        
        try {
            No melhor = nos.get(0);
            
            int menor = 1000;
        for (No no : nos) {
            if(no.getHeuristica() + no.getNivel() < menor && no.isAberto() == false){
            menor = no.getHeuristica() + no.getNivel();
            melhor = no;            
            }
        }
        return melhor;
        
        
        } catch (Exception e) {
            System.out.println("A busca não encontrou resultados.");
            return null;
        }
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
     
    public LinkedList<No> getNos() {
        return nos;
    }

    void setDisplay(Display dis) {
         this.display = dis;
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