package message;

import machine.Machine;
import machine.TypeMessage;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Kim Thuat Nguyen
 */
public class Message {
    private int taille;
    private Machine source;
    private Machine destination;
    private TypeMessage type;
    
    
    public Message(Machine source, Machine destination, TypeMessage type, int taille) {
        this.source = source;
        this.destination = destination;
        this.type = type;
        this.taille = taille;
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
