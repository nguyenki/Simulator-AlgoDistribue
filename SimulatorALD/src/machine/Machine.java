package machine;

import java.util.Collections;
import java.util.LinkedList;
import message.Message;
import sequencer.Sequencer;

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
    private double capacCarte;
    // Number of message emis par le machine => on l'utilise pour calculer le debit
    private int nbMessSend = 0;
    // Queue qui contient les messages recus par d'autre machine. Ces messages ne sont pas encore traites
    private LinkedList<Message> buffer;
    
    
    private double momentAvaiableToSend;
    
    private double momentAvaiableToReceive;
    
    public Machine(int id, double capacite, int nbMessSend, LinkedList<Message> buffer) {
        this.id = id;
        this.capacCarte = capacite;
        this.nbMessSend = nbMessSend;
        this.buffer = buffer;
        this.momentAvaiableToReceive = 0;
        this.momentAvaiableToSend = 0;
    }
    
    public double getCapacCarte() {
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
   
    public Message firstMessage() {
        return this.buffer.element();
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

    public double getMomentAvaiableToSend() {
        return momentAvaiableToSend;
    }

    public void setMomentAvaiableToSend(double momentAvaiableToSend) {
        this.momentAvaiableToSend = momentAvaiableToSend;
    }

    public double getMomentAvaiableToReceive() {
        return momentAvaiableToReceive;
    }

    public void setMomentAvaiableToReceive(double momentAvaiableToReceive) {
        this.momentAvaiableToReceive = momentAvaiableToReceive;
    }
    
    public String toString() {
        return "ID:"+this.id+": Capacite:"+this.capacCarte+" : Nb message envoye: "+this.nbMessSend+" Nb message dans Buffer;"+this.buffer.size();
    }
    
    public void sendToSenquencer(Message mess, Sequencer seq) {
        seq.addMessToBuffer(mess);
    }
 }
