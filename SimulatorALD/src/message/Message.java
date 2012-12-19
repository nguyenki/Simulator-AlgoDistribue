package message;

import machine.Machine;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Kim Thuat Nguyen
 */
public class Message implements Comparable<Message> {

    public enum etatMessage {ARRIVE, SEND};
    
    private int taille;
    private Machine source;
    private Machine destination;
    private TypeMessage type; // Unicast ou Multicast
    private etatMessage etat;
    private double date; // Le temps que le message devrait normalement arriver a la destination
    
    public Message(Machine source, Machine destination, TypeMessage type, int taille, double date) {
        this.source = source;
        this.destination = destination;
        this.type = type;
        this.taille = taille;
        this.date = date;
        this.etat = etatMessage.SEND;
    }
    
    @Override
    public int compareTo(Message o) {
        if (this.date == o.date) {
            return 0;
        } else if (this.date > o.date) {
            return 1;
        } else {
            return -1;
        }
    }
    
    public int getTaille() {
        return taille;
    }

    public void setTaille(int taille) {
        this.taille = taille;
    }

    public Machine getDestination() {
        return destination;
    }

    public void setDestination(Machine destination) {
        this.destination = destination;
    }

    public Machine getSource() {
        return source;
    }

    public void setSource(Machine source) {
        this.source = source;
    }

    public TypeMessage getType() {
        return type;
    }

    public void setType(TypeMessage type) {
        this.type = type;
    }
    
}
