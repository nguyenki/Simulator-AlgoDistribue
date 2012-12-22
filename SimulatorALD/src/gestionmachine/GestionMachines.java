/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gestionmachine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
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
    private List<Message> messageArrives;
    private double tailleMaxMessage;
    public GestionMachines(int nbM,double tempsPropa, double capaciteM) {
        this.nbMachines = nbM;
        machinesDefault = new Machine[nbMachines];
        this.tempsPropa = tempsPropa;
        this.capaciteM = capaciteM;
        initiateEtatMachines(machinesDefault, capaciteM);
        messageArrives = new ArrayList<Message>();
    }

    public void initiateEtatMachines(Machine[] machineDefauts, double capacite) {
        for (int i=0;i<machineDefauts.length;i++) {
            machineDefauts[i] = new Machine(i+1, capacite , 0, new LinkedList<Message>());
        }
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
    public Machine getMachine(int id) {
        return this.machinesDefault[id];
    }
    
    public double getTimeTransmission(double tailleMessage) {
        return tailleMessage/this.capaciteM;
    }
    
    /*
     * Generer le message de taille variable
     */
    public double tailleMess() {
        return 1;
    }
    
    /*
     * Methodes principales
     */
    public void sendUnicast(Machine source, Machine destination, double taille) {
        source.incrementerNbMessSend();
        Message mess = new Message(source, destination, TypeMessage.UNICAST, taille,this.tempsPropa);
        destination.addMessage(mess);
    }
    
    public void sendMulticast(Machine source, double taille) {
        source.incrementerNbMessSend();
        Message mess = new Message(source, null, TypeMessage.MULTICAST, taille, this.tempsPropa);
        for(int i=0;i<this.nbMachines;i++) {
            if (machinesDefault[i].getId()!=source.getId()) {
                machinesDefault[i].addMessage(mess);
            }
        }
    }
    
    /*
     * Calculer le debit du reseau
     */
    public double getDebit() {
        int nbMessSend = 0;
        for (int i=0;i<this.nbMachines; i++) {
            nbMessSend+=machinesDefault[i].getBuffer().size();
        }
        nbMessSend+=this.messageArrives.size();
        if (getSim_clock()==0) {
            return 0;
        } else {
            return nbMessSend/getSim_clock();
        }
    }

    public double getLatence() {
        Iterator<Message> it = this.messageArrives.iterator();
        double tempsTotal = 0;
        while (it.hasNext()) {
            Message mess = it.next();
            tempsTotal+= mess.getDate()+mess.getTaille()/mess.getSource().getCapacCarte();
        }
        return tempsTotal/getNbMachines();
    }

/************************************************************
 * Implementation pour les trois algorithmes                *
 ************************************************************/


/*
 * N emission successives
 * On suppose que le premiere machine va envoyer les messages pour les autres machines
 */
    public void EmissionSuccessive() {
        int i=1; // initialise le identifier de machine
        System.out.println("I. N Emissions successives des messages ");
        while (i<this.nbMachines) {
            sendUnicast(this.machinesDefault[0], this.machinesDefault[i] , tailleMess());
            setSim_clock(this.machinesDefault[i].firstMessage().getDate()+ tailleMess()/this.capaciteM);
            this.messageArrives.add(this.machinesDefault[i].firstMessage());
            this.machinesDefault[i].removeMessage();
            i++;
        }
    } 

    public void Arbre() {
        System.out.println("II. DisséminaHon des messages en utilisant la structure de l'arbre");
        
    }
    
    public void pipeLine() {
        
    }
    
    /********************************************************************************************************
     *                           EXPERIMENTATION PARTIE                                                     *
     ********************************************************************************************************/
    public static void main(String args[]) {
        GestionMachines main = new GestionMachines(15, 10, 10);
        main.EmissionSuccessive();
        System.out.println("DEBIT: "+main.getDebit());
        System.out.println("LATENCE: "+main.getLatence());
        
    }
}

