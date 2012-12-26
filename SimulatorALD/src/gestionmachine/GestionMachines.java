/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gestionmachine;

import java.lang.reflect.Array;
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
    
    private double debit;
    private double latence;
    
    public GestionMachines(int nbM,double tempsPropa, double capaciteM) {
        this.nbMachines = nbM;
        machinesDefault = new Machine[nbMachines];
        this.tempsPropa = tempsPropa;
        this.capaciteM = capaciteM;
        initiateEtatMachines(machinesDefault, capaciteM);
        messageArrives = new ArrayList<Message>();
        this.debit = 0;
        this.latence = 0;
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

    public List<Message> getMessageArrives() {
        return messageArrives;
    }

    public void setMessageArrives(List<Message> messageArrives) {
        this.messageArrives = messageArrives;
    }

    public double getTailleMaxMessage() {
        return tailleMaxMessage;
    }

    public void setTailleMaxMessage(double tailleMaxMessage) {
        this.tailleMaxMessage = tailleMaxMessage;
    }

    public double getDebit() {
        return debit;
    }

    public void setDebit(double debit) {
        this.debit = debit;
    }

    public double getLatence() {
        return latence;
    }

    public void setLatence(double latence) {
        this.latence = latence;
    }
    
    public double getUniteDeTemps(double tailleMess) {
        return (this.tempsPropa+tailleMess/this.capaciteM);
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
    public void sendUnicast(Machine source, Machine destination, double taille, double date) {
        source.incrementerNbMessSend();
        Message mess = new Message(source, destination, TypeMessage.UNICAST, taille, date);
        // Kiem tra may co ranh de cho gui hay khong
        if (source.getMomentAvaiableToSend()> mess.getDate()) { // ko ranh
            mess.setDate(source.getMomentAvaiableToSend());
        }
        source.setMomentAvaiableToSend(mess.getDate()+mess.getTaille()/source.getCapacCarte());
        // Kiem tra xem may co ranh de ma nhan hay khong
        if (destination.getMomentAvaiableToReceive()<mess.getDate()+this.tempsPropa) { // May ranh
            this.messageArrives.add(mess);
            destination.setMomentAvaiableToReceive(mess.getDate()+this.tempsPropa+mess.getTaille()/source.getCapacCarte());
        } else {
            destination.addMessage(mess);
        }
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
    
 
/************************************************************
 * Implementation pour les trois algorithmes                *
 ************************************************************/


/*
 * N emission successives
 * On suppose que le premiere machine va envoyer les messages pour les autres machines
 */
    public void EmissionSuccessive(double tailleMess, int nbMess) {
        int i=1, j=0; // initialise le identifier de machine
        double latence = 0;
        System.out.println("I. N Emissions successives des messages ");
        System.out.println("Time: "+ getSim_clock());
        while (j<nbMess) {
            System.out.println("Numero de message: "+j);
            double temps = getSim_clock();
            while (i<this.nbMachines) {
                sendUnicast(this.machinesDefault[0], this.machinesDefault[i] , tailleMess,getSim_clock()); //TODO
                setSim_clock(getSim_clock()+ getUniteDeTemps(tailleMess));
                System.out.println("Time:"+getSim_clock()+"\n"+this.machinesDefault[i].toString()+"\n a recu un message\n --------------------------");
                i++;
            }
            latence+=(getSim_clock()- temps)/getUniteDeTemps(tailleMess);
            i=1;
            j++;
        }
       setDebit(nbMess*getUniteDeTemps(tailleMess) /getSim_clock());
       setLatence(latence/nbMess);
    } 

    public void Arbre(double tailleMess, int nbMess) {
        System.out.println("II. DisséminaHon des messages en utilisant la structure de l'arbre");
        boolean messPasEnvoye = true;
        double latence = 0;
        List<Integer> idMachines = new ArrayList<>();
        idMachines.add(0);
        int j=0;
        while(j<nbMess) {
            double temp = getSim_clock();
            while(messPasEnvoye) {
                int i=0;
                int length = idMachines.size();
                while (i<length) {
                    if (length+i>= getNbMachines()) {
                        messPasEnvoye = false;
                        break;
                    } else {
                        sendUnicast(this.machinesDefault[i], this.machinesDefault[i+length], tailleMess, getSim_clock());
                        idMachines.add(i+length);
                    }
                    i++;
                }
                    setSim_clock(getSim_clock()+getUniteDeTemps(tailleMess));
                    if (idMachines.size()==getNbMachines()) break;
               }
            latence+= (getSim_clock()-temp)/getUniteDeTemps(tailleMess);
            messPasEnvoye = true;
            j++;
        }
        setDebit(nbMess/(getSim_clock()/getUniteDeTemps(tailleMess)));
        setLatence(latence/nbMess);
    }
    
    public void pipeLine(double tailleMess, int nbMess) {
        System.out.println("III. DisséminaHon des messages en utilisant la structure pipe line");
        int i=0, j=0; 
        double latence = 0;
        System.out.println("Time: "+ getSim_clock());
        while (j<nbMess) {
            System.out.println("Numero de message: "+j);
            double temps = getSim_clock();
            while (i<this.nbMachines-1) {
                sendUnicast(this.machinesDefault[i], this.machinesDefault[i+1] , tailleMess,getSim_clock()); //TODO
                setSim_clock(getSim_clock()+ getUniteDeTemps(tailleMess));
                System.out.println("Time:"+getSim_clock()+"\n"+this.machinesDefault[i].toString()+"\n a recu un message\n --------------------------");
                i++;
            }
            latence+=(getSim_clock()- temps)/getUniteDeTemps(tailleMess);
            i=0;
            j++;
            if (j!=(nbMess)) {
                setSim_clock(temps+getUniteDeTemps(tailleMess));
            }
        }
       setDebit(nbMess*getUniteDeTemps(tailleMess)/getSim_clock());
       setLatence(latence/nbMess);
    }
    
    /********************************************************************************************************
     *                           EXPERIMENTATION PARTIE                                                     *
     ********************************************************************************************************/
    public static void main(String args[]) {
        GestionMachines main = new GestionMachines(10, 10, 10);
        //main.EmissionSuccessive(1,1);
        //main.pipeLine(1, 200);
        //main.Arbre(1, 1);
        System.out.println("DEBIT: "+main.getDebit());
        System.out.println("LATENCE: "+main.getLatence());
    }
}

