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
import sequencer.Sequencer;

/**
 *
 * @author Kim Thuat Nguyen
 */
public class FixedSequencer {
    private double limitTime; // Temps limite pour la simulation
    private int nbMachines;
    private double capaciteM; // Capacite pour chaque machine. Le capacite pour chaque machine peut varier
    private Machine[] machinesDefault;
    private List<Message> messageArrives;
    
    private double debit;
    private double latence;
    private Sequencer sequencer;
    
    public FixedSequencer(double limitTime, int nbMachines, double capaciteM) {
        this.limitTime = limitTime;
        this.nbMachines = nbMachines;
        this.capaciteM = capaciteM;
        this.machinesDefault = new Machine[nbMachines];
        this.messageArrives = new ArrayList<Message>();
        this.debit = 0;
        this.latence = 0;
        this.sequencer = new Sequencer();
        initiateEtatMachines(this.machinesDefault, capaciteM);
    }

    
     public void initiateEtatMachines(Machine[] machineDefauts, double capacite) {
        for (int i=0;i<machineDefauts.length;i++) {
            machineDefauts[i] = new Machine(i+1, capacite , 0, new LinkedList<Message>());
        }
    }
 
    
    /****************************************************************************
     * Get and set methods
     ***************************************************************************/
    public double getLimitTime() {
        return limitTime;
    }

    public void setLimitTime(double limitTime) {
        this.limitTime = limitTime;
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

    public Machine[] getMachinesDefault() {
        return machinesDefault;
    }

    public void setMachinesDefault(Machine[] machinesDefault) {
        this.machinesDefault = machinesDefault;
    }

    public List<Message> getMessageArrives() {
        return messageArrives;
    }

    public void setMessageArrives(List<Message> messageArrives) {
        this.messageArrives = messageArrives;
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

    public Sequencer getSequencer() {
        return sequencer;
    }

    public void setSequencer(Sequencer sequencer) {
        this.sequencer = sequencer;
    }
    
    
    /*********************************************************************************
     *  PRINCIPLE METHODS
     *********************************************************************************/
    public void sendUnicast(Machine source, Machine destination, double taille, double date) {
        source.incrementerNbMessSend();
        List<Machine> machines = new ArrayList<Machine>();
        machines.add(destination);
        Message mess = new Message(source, machines, TypeMessage.UNICAST, taille, date);
        // Kiem tra may co ranh de cho gui hay khong
        if (source.getMomentAvaiableToSend()> mess.getDate()) { // ko ranh
            mess.setDate(source.getMomentAvaiableToSend());
        }
        source.setMomentAvaiableToSend(mess.getDate()+mess.getTaille()/source.getCapacCarte());
        
        getSequencer().addMessToBuffer(mess);
    }
    
    public void sendMulticast(Machine source, List<Machine> destinations, double taille, double date) {
        source.incrementerNbMessSend();
        Message mess = new Message(source, destinations, TypeMessage.MULTICAST, taille, date);
        // Kiem tra may co ranh de cho gui hay khong
        if (source.getMomentAvaiableToSend()> mess.getDate()) { // ko ranh
            mess.setDate(source.getMomentAvaiableToSend());
        }
        source.setMomentAvaiableToSend(mess.getDate()+mess.getTaille()/source.getCapacCarte());
        getSequencer().addMessToBuffer(mess);
    }
    
    public void send(Message mess) {
        mess.getSource().incrementerNbMessSend();
        if (mess.getSource().getMomentAvaiableToSend()>mess.getDate()) {
            mess.setDate(mess.getSource().getMomentAvaiableToSend());
        }
        mess.getSource().setMomentAvaiableToSend(mess.getDate()+mess.getTaille()/mess.getSource().getCapacCarte());
        getSequencer().addMessToBuffer(mess);
    }
    
    public void sendAllMessGeneratedToSequencer(List<Message> messages) {
        Iterator<Message> it = messages.iterator();
        while (it.hasNext()) {
            Message mess = it.next();
            send(mess);
        }
    }
    
    public void deliverMessages() {
        // Xay dung 1 list cac messages phu thuoc vao mot message trong qua trinh xu ly
        List<Message> dependances = new LinkedList<Message>();
         
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
                
        
    }
    
    public static void main(String args[]) {
        // Initialize N machines
        FixedSequencer fixSequencer = new FixedSequencer(10,10, 1);
        
        // Generate the messages
        
        
        // Send messages from sources to Sequencer
        
        
        // Send messages from Sequencer to Destinations
        
        
        // Destinations deliver the messages received


    }
    
}
