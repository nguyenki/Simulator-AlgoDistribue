/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gestionmachine;

import machine.Machine;
import machine.TypeMessage;
import message.Message;

/**
 *
 * @author nguyenki
 */
public class GestionMachines {
    // On suppose qu'il y un temps de propagation
    // Le temps de transmission est 0 par default
    private int tempsPropa;
    private int nbMachines;
    
    private Machine[] machinesDefault;
    
    public GestionMachines(int nbM) {
        this.nbMachines = nbM;
        machinesDefault = new Machine[nbMachines];
    }


    public int getTempsPropa() {
        return tempsPropa;
    }

    public void setTempsPropa(int tempsPropa) {
        this.tempsPropa = tempsPropa;
    }
    
    public void sendUnicast(Machine source, Machine destination, int taille) {
        Message mess = new Message(source, destination, TypeMessage.UNICAST, taille);
        // TODO: revoir
    }
    
    public void sendMulticast(Machine source) {
        
    }
}
