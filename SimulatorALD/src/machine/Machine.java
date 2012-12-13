package machine;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
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
    private int id;
    private int capacCarte;
    // Queue qui contient les messages recus par d'autre machine
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
}
