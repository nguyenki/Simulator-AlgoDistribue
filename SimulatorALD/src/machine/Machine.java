package machine;

import java.util.Collections;
import java.util.LinkedList;
import message.Message;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Kim Thuat Nguyen
 */
public class Machine {
    // Identifier de chaque machine
    private int id;
    // Capacite de la carte reseau pour chaque machine
    private int capacCarte;
    // Number of message emis par le machine => on l'utilise pour calculer le debit
    private int nbMessSend = 0;
    // Queue qui contient les messages recus par d'autre machine. Ces messages ne sont pas encore traites
    private LinkedList<Message> buffer;
    
    
    public int getCapacCarte() {
        return capacCarte;
    }

    public void setCapacCarte(int capacCarte) {
        this.capacCarte = capacCarte;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LinkedList<Message> getBuffer() {
        return buffer;
    }

    public void setBuffer(LinkedList<Message> buffer) {
        this.buffer = buffer;
    }
    
    
    public void addMessage(Message mess) {
        this.buffer.add(mess);
        Collections.sort(buffer);
    }
    // Enlever le message arrive'
    public void removeMessage() {
        this.buffer.removeFirst();
    }

    public int getNbMessSend() {
        return nbMessSend;
    }

    public void setNbMessSend(int nbMessSend) {
        this.nbMessSend = nbMessSend;
    }
    
    public void incrementerNbMessSend() {
        this.nbMessSend++;
    }
    
}
