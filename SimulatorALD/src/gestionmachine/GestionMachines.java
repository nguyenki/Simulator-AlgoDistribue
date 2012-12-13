/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gestionmachine;

import machine.Machine;
import message.Message;
import message.TypeMessage;

/**
 *
 * @author nguyenki
 */
public class GestionMachines {
    // On suppose qu'il y un temps de propagation
    // Le temps de transmission = tailleMessage/capacite d'une machine
    private double tempsPropa;
    private int nbMachines;
    private double capaciteM;
    private static double sim_clock = 0;
    private Machine[] machinesDefault;
    
    public GestionMachines(int nbM,double tempsPropa, double capaciteM) {
        this.nbMachines = nbM;
        machinesDefault = new Machine[nbMachines];
        this.tempsPropa = tempsPropa;
        this.capaciteM = capaciteM;
    }

    /********************* 
     * Get and Set methods
     *********************/
    public double getTempsPropa() {
        return tempsPropa;
    }

    public void setTempsPropa(int tempsPropa) {
        this.tempsPropa = tempsPropa;
    }

    public int getNbMachines() {
        return nbMachines;
    }

    public void setNbMachines(int nbMachines) {
        this.nbMachines = nbMachines;
    }

    public double getCapaciteM() {
        return capaciteM;
    }

    public void setCapaciteM(double capaciteM) {
        this.capaciteM = capaciteM;
    }

    public static double getSim_clock() {
        return sim_clock;
    }

    public static void setSim_clock(double sim_clock) {
        GestionMachines.sim_clock = sim_clock;
    }

    public Machine[] getMachinesDefault() {
        return machinesDefault;
    }

    public void setMachinesDefault(Machine[] machinesDefault) {
        this.machinesDefault = machinesDefault;
    }
    
    
    /*
     * Methodes principales
     */
    public void sendUnicast(Machine source, Machine destination, int taille) {
        Message mess = new Message(source, destination, TypeMessage.UNICAST, taille,taille/this.capaciteM+this.tempsPropa);
        destination.addMessage(mess);
    }
    
    public void sendMulticast(Machine source, int taille) {
        Message mess = new Message(source, null, TypeMessage.MULTICAST, taille, taille/this.capaciteM+this.tempsPropa);
        for(int i=0;i<this.nbMachines;i++) {
            if (machinesDefault[i].getId()!=source.getId()) {
                machinesDefault[i].addMessage(mess);
            }
        }
    }
}
