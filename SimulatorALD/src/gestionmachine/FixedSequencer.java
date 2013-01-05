/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gestionmachine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    private double tempsPropa;
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
            machineDefauts[i] = new Machine(i, capacite , 0, new LinkedList<Message>());
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

    public double getTempsPropa() {
        return tempsPropa;
    }

    public void setTempsPropa(double tempsPropa) {
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

    public Machine[] getMachinesDefault() {
        return machinesDefault;
    }

    public void setMachinesDefault(Machine[] machinesDefault) {
        this.machinesDefault = machinesDefault;
    }

    public Machine getMachine(int id) {
        return this.machinesDefault[id];
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
        while (!getSequencer().getSequenceNbsOfMachine().isEmpty()) {
            Node node = new Node(getLastMinimumSequenceNumber(getSequencer().getSequenceNbsOfMachine()));
            System.out.println("DEBUG: SEQUENCES NUMBER POUR CHAQUE MACHINES:"+getSequencer().getSequenceNbsOfMachine().toString());
            System.out.println("DEBUG delivermessage: "+node.getName().toString());
            node.buildListDependencies(node.getName(), this);
            System.out.println("\n+++++++++++++++\n List dependencies:"+node.getDependences().toString());
            System.out.println("+++++++In deliver messages:"+node.isDeadlock());
            node.resolveDependencies(this);
        }
    }
    
    // Tim kiem so sequence nho nhat trong list cac sequence number doi voi moi machine
    public LinkedList<Integer> getLastMinimumSequenceNumber(Map<LinkedList<Integer>,LinkedList<Integer>> sequenceNbOfMachines) {
        LinkedList<Integer> list = new LinkedList<Integer>();
        int temp = Integer.MAX_VALUE;
        LinkedList<Integer> sd = new LinkedList<Integer>();
        Iterator<Map.Entry<LinkedList<Integer>, LinkedList<Integer>>> it = sequenceNbOfMachines.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<LinkedList<Integer>, LinkedList<Integer>> map = it.next();
            if (temp >= map.getValue().getFirst()) {
                temp = map.getValue().getFirst();
                sd = map.getKey();
            }
        }
        
        for (Integer s: sd) {
            list.add(s);
        }
        list.addLast(temp);
        return list; // sous forme [idMachineSource, idMachineDestination, nbSequence]
    }
    
    public void deliverAMessage(int nbSequence, int idDestinationMachine) {
        // Remove message from buffer
        Message m = this.machinesDefault[idDestinationMachine].removeMessFromBuffer(nbSequence);
        if (this.machinesDefault[idDestinationMachine].getMomentAvaiableToReceive()< (m.getDate()+getTempsPropa())) {
            this.machinesDefault[idDestinationMachine].setMomentAvaiableToReceive(getUniteTemps(m, this.machinesDefault[idDestinationMachine]));
        } else {
            this.machinesDefault[idDestinationMachine].setMomentAvaiableToReceive(getMachine(idDestinationMachine).getMomentAvaiableToReceive()+m.getTaille()/getMachine(idDestinationMachine).getCapacCarte());
        }
        m.setDateMessDelivre(getMachine(idDestinationMachine).getMomentAvaiableToReceive());
        System.out.println("*****************Delivred a messages:********************\n"+m.toString());
        this.messageArrives.add(m);
    }
    
    public double getUniteTemps(Message m, Machine machine) {
        return getTempsPropa()+m.getTaille()/machine.getCapacCarte()+m.getDate();
    }
    
    // Tim gia tri sequence number nho nhat cho 1 nguon xac dinh
    public int findSmallestSequenceNumberForASource(int idSource) {
        LinkedList<Integer> minimumListSequenceAccordingToDestination = new LinkedList<Integer>();
        Set<LinkedList<Integer>> keys = getSequencer().getSequenceNbsOfMachine().keySet();
        for (LinkedList<Integer> key: keys) {
            if (key.getFirst() == idSource) {
                if (getSequencer().getSequenceNbsOfMachine().containsKey(key)) {
                    minimumListSequenceAccordingToDestination.add(getSequencer().getSequenceNbsOfMachine().get(key).getFirst());
                }
            }
        }
        Collections.sort(minimumListSequenceAccordingToDestination);
        if (minimumListSequenceAccordingToDestination.isEmpty()) {
            return Integer.MAX_VALUE;
        } else {
            return minimumListSequenceAccordingToDestination.getFirst();
        }
    }
    
    // Tim gia tri sequence nho ngay sau 1 sequence nb khac cho 1 nguon xac dinh
    public LinkedList<Integer> findLastSmallerSequenceNumberForASource(int idSource, int nbSequence) {
        LinkedList<Integer> rs = new LinkedList<Integer>();
        LinkedList<Integer> listAllSequences = getAllSequenceNumberForASource(idSource);
        if (listAllSequences.contains(nbSequence)) {
            if (listAllSequences.size()>=2) {
                int index = listAllSequences.indexOf(nbSequence);
                if (index>=1) {
                    rs  = findOriginBySequenceNumber(listAllSequences.get(index-1));
                    rs.addLast(listAllSequences.get(index-1));
                }
            }
        }
        return rs;
    }
    
    public LinkedList<Integer> getAllSequenceNumberForASource(int idSource) {
        LinkedList<Integer> list = new LinkedList<Integer>();
        Set<LinkedList<Integer>> keys = getSequencer().getSequenceNbsOfMachine().keySet();
        for (LinkedList<Integer> key: keys) {
            if (key.getFirst() == idSource) {
                if (getSequencer().getSequenceNbsOfMachine().containsKey(key)) {
                    for (Integer sq: getSequencer().getSequenceNbsOfMachine().get(key)) {
                        list.add(sq);
                    }
                }
            }
        }
        Collections.sort(list);
        return list;
    }
    
    public LinkedList<Integer> findOriginBySequenceNumber(int sequenceNb) {
        LinkedList<Integer> rs = new LinkedList<Integer>();
        LinkedList<Integer> value = new LinkedList<Integer>();
        Collection<LinkedList<Integer>> values = getSequencer().getSequenceNbsOfMachine().values();
        for (LinkedList<Integer> l:values) {
            if (l.contains(sequenceNb)) {
                value = l;
            }
        }
        
        for (LinkedList<Integer> k: getSequencer().getSequenceNbsOfMachine().keySet()) {
            if (getSequencer().getSequenceNbsOfMachine().get(k).equals(value)) {
                rs = k;
            }
        }
        return rs;
    }
    
  
}
