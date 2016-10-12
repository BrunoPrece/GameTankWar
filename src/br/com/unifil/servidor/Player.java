package br.com.unifil.servidor;

/**
 * Classe responsável por definir as propriedades de cada jogador.
 * 
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Player {

    /* identificação do jogador */
    public int id; 
    /* cada jogador terá um socket */
    public Socket socket; 
    /* canal de entrada */
    public DataInputStream entrada;
    /* canal de saída */
    public DataOutputStream saida; 
    
    /**
     * Contrutor Padrão do Player.
     * @param id do player
     * @param socket do player 
     */
    public Player(int id, Socket socket) {
        this.id = id;
        this.socket = socket;
        try {
            entrada = new DataInputStream(socket.getInputStream());
            saida = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
        }
    }
    
    
    
}
