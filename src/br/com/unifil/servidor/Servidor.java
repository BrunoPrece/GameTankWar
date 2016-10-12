/**
 * Classe responsável por ser o servidor de aplicação do Game Tank War.
 * Fernando Ortiz
 * Bruno Prece
 * Jonathan Albertoni
 */
package br.com.unifil.servidor;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Servidor {

    // variavel para contar a quantidade de conexões permitidas
    public static int contador = 0;
    // objetos da classe Player
    private ArrayList<Player> netPlayers = new ArrayList<Player>(); 
    
    /**
     * Construtor Padrão da Classe.
     */
    public Servidor() {
        /* Objeto do tipo ServerSocket, essa classe é responsável por esperar a conexão
            do cliente. */
        ServerSocket server;
        
        /* */
        DataInputStream entrada; 

        System.out.println("Servidor Game Tank War inicializado na porta 5000.");
        
        try {
            /* servidor recebendo a porta de entrada */
            server = new ServerSocket(5000); 
            
            /* Loop responsável por limitar a quantidade de conexões que o jogo pode ter */
            while (true) {
                /* Limitando as conexões */
                if(contador < 2){ 
                    /* Cria um socket que aceita a conexão de um servidor */
                    Socket socket = server.accept(); 
                    
                    /* Cria um objeto Player para o cliente que acabou de conectar, ele recebe
                        o número da conexão e o socket */
                    Player aux = new Player(contador, socket); 
                    
                    System.out.println(" *** Jogador " + contador + " acaba de conectar-se *** ");
                    
                    /* Depois da conexão, incrementa no contador */
                    contador++;
                    /* adiciona o jogador criado no arrayList de jogadores */
                    netPlayers.add(aux);
                    
                    /* Criação e inicialização de uma nova Thread para o objeto criado*/
                    new Thread(new Servidor.ThreadCliente(aux)).start();
                    
                    /* Se a quantidade de jogadores forem 2 */
                    if(contador == 2){ 
                        System.out.println("Servidor 100%. Nenhum jogador poderá se conectar; \n----------------------");
                         /* envia uma mensagem para todos inicializarem o jogo */
                        enviarMensagem("PLAAAAAAAAAAAAAAAY!");
                    }
                }
            }
        } catch (IOException ex) {
        }
        
    }                                    
    
    /**
     * Método responsável por encaminhar uma mensagem para todos os
     * participantes do jogo.
     * @param msg a ser enviada.
     */
    private void enviarMensagem(String msg){
        try {
            for(Player player : netPlayers){
                player.saida.writeUTF(msg);
            }
        } catch (IOException e) {
        }
    }
    
    /**
     * Classe onde cada cliente terá uma thread exclusiva.
     * Essa thread ouve as mensagens dos jogadores, e envia para o outro jogador.
     * 
     */
    private class ThreadCliente implements Runnable {

        Player jogador;

        /**
         * Construtor Padrão.
         * @param jogador 
         */
        public ThreadCliente(Player jogador) {
            this.jogador = jogador;
        }

        /**
         * Sobrescrita do metodo run, para executar a Thread.
         * 
         */
        @Override
        public void run() {
            String mensagem = "";
            
            try {
                while (true) {
                    /* leitura da mensagem, - entrada é referente a classe DataInput que le
                       dados primitivos */
                    mensagem = jogador.entrada.readUTF();
                    
                    /* Se o jogador desta thread for 0 */
                    if(jogador.id == 0){ 
                        /* Envia a mensagem para o adversário, que no caso é o 1 do parâmetro */
                        netPlayers.get(1).saida.writeUTF(mensagem); 
                    } else { /* Senão, vice-versa */
                        netPlayers.get(0).saida.writeUTF(mensagem); 
                    }
                    
                }
            } catch (IOException ex) {
            }
        }
    }
    
    
    /**
     * Método Main. Cria um novo Servidor.
     * @param args 
     */
    public static void main(String args[]) {
        new Servidor();
    }
           
}
