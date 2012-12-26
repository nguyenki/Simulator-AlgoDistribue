package message;

import java.util.ArrayList;
import java.util.List;
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
    
    private double taille;
    private Machine source;
    private List<Machine> destinations;
    private TypeMessage type; // Unicast ou Multicast
    private double date; // Le temps que que le message est emis
    private double dateMessDelivre; // Le temps que le message est delivre
    private int numeroSequencer;
            
    public Message(Machine source, Machine destination, TypeMessage type, double taille, double date) {
        this.source = source;
        this.destinations = new ArrayList<Machine>();
        this.type = type;
        this.taille = taille;
        this.date = date;
        this.dateMessDelivre = date;
        this.numeroSequencer = 0;
     }
    
    public Message(Machine source, List<Machine> destinations, TypeMessage type, double taille, double date) {
        this.source = source;
        this.destinations = destinations;
        this.type = type;
        this.taille = taille;
        this.date = date;
        this.dateMessDelivre = date;
        this.numeroSequencer = 0;
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
    
    public double getTaille() {
        return taille;
    }

    public void setTaille(double taille) {
        this.taille = taille;
    }

    public List<Machine> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<Machine> destinations) {
        this.destinations = destinations;
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

    public double getDate() {
        return date;
    }

    public void setDate(double date) {
        this.date = date;
    }

    public double getMessDelivre() {
        return dateMessDelivre;
    }

    public void setMessDelivre(double messDelivre) {
        this.dateMessDelivre = messDelivre;
    }

    public int getNumeroSequencer() {
        return numeroSequencer;
    }

    public void setNumeroSequencer(int numeroSequencer) {
        this.numeroSequencer = numeroSequencer;
    }
    
    
}
