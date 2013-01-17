/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gestionmachine;

import config.Config;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
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
    
    
    
    public FixedSequencer(double limitTime, int nbMachines, double capaciteM, double tempsPropa) {
        this.limitTime = limitTime;
        this.nbMachines = nbMachines;
        this.capaciteM = capaciteM;
        this.machinesDefault = new Machine[nbMachines];
        this.messageArrives = new ArrayList<Message>();
        this.debit = 0;
        this.latence = 0;
        this.sequencer = new Sequencer();
        this.tempsPropa = tempsPropa;
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
        while (!getSequencer().getSequenceNbsOfMachine().isEmpty()) {
            Node node = new Node(getLastMinimumSequenceNumber(getSequencer().getSequenceNbsOfMachine()));
            node.buildListDependencies(node.getName(), this);
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
        System.out.println("*****************Delivred a messages in destination machine "+idDestinationMachine+" ********************\n"+m.toString());
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
    
    /*
     * IMPLEMENTATION DE 3 ALGORITHMES DANS LE COURS    
     * 
     */
    
    
    /*************************************************************************************
     * N emission successives
     * On suppose que le premiere machine va envoyer les messages pour les autres machines
     *************************************************************************************/
    public void EmissionSuccessive(double tailleMess, int nbMess) {
        System.out.println("I. N Emissions successives des messages ");
        int i=1; // Initialise l'identifier de la deuxieme machine
        int j=0; // Initialise le nb de message
        int dateSent = 0;
        // Envoyer messages aux sequencer
        while (j<nbMess) {
            while (i<getNbMachines()) {
                sendUnicast(getMachine(0), getMachine(i), tailleMess, dateSent);
                dateSent+=tailleMess/getMachine(0).getCapacCarte()+getTempsPropa();
                i++;
            }
            j++;
        }
        getSequencer().assignSequenceNumber(getSequencer().getBuffer());
        getSequencer().diffusionMessagesFromSequencerToDestinations();
        deliverMessages();
        setDebit(nbMess/(dateSent/getUniteTemps(0, tailleMess)));
        setLatence((dateSent/getUniteTemps(0, tailleMess))/nbMess);
    }
    
    /*************************************************************************************
     * PipeLine
     * On suppose que le premiere machine va envoyer les messages pour les autres machines
     *************************************************************************************/
     public void pipeLine(double tailleMess, int nbMess) {
        System.out.println("III. DisséminaHon des messages en utilisant la structure pipe line");
        int i = 0, j = 0;
        double latence=0;
        double dateSent = 0;  
        double temp = 0;
        while (j<nbMess) {
            dateSent = temp;
            while (i<getNbMachines()-1) {
                sendUnicast(getMachine(i), getMachine(i+1), tailleMess, dateSent);
                dateSent+= getUniteTemps(i, tailleMess);
                i++;
            }
            latence+=(dateSent-temp)/getUniteTemps(0, tailleMess);
            temp+= getUniteTemps(0, tailleMess);
            i=0;
            j++;
        }
        getSequencer().assignSequenceNumber(getSequencer().getBuffer());
        getSequencer().diffusionMessagesFromSequencerToDestinations();
        deliverMessages();
        setDebit(nbMess/(dateSent/getUniteTemps(0, tailleMess)));
        setLatence(latence/nbMess);
    }

    /*************************************************************************************
     * Arbre
     * On suppose que le premiere machine va envoyer les messages pour les autres machines
     *************************************************************************************/
     public void Arbre(double tailleMess, int nbMess) {
        System.out.println("II. DisséminaHon des messages en utilisant la structure de l'arbre");
        boolean messPasEnvoye = true;
        double latence = 0;
        List<Integer> idMachines = new ArrayList<Integer>();
        idMachines.add(0);
        int j = 0;
        double dateSent = 0;
        while (j < nbMess) {
            double temp = dateSent;
            while (messPasEnvoye) {
                int i = 0;
                int length = idMachines.size();
                while (i < length) {
                    if (length + i >= getNbMachines()) {
                        messPasEnvoye = false;
                        break;
                    } else {
                        sendUnicast(getMachine(i), getMachine(i+length), tailleMess, dateSent);
                        idMachines.add(i + length);
                    }
                    i++;
                }
                dateSent+= getUniteTemps(0, tailleMess);
                if (idMachines.size() == getNbMachines()) {
                    break;
                }
            }
            latence += (dateSent - temp) / getUniteTemps(0,tailleMess);
            messPasEnvoye = true;
            j++;
        }
        getSequencer().assignSequenceNumber(getSequencer().getBuffer());
        getSequencer().diffusionMessagesFromSequencerToDestinations();
        deliverMessages();
        setDebit(nbMess / (dateSent / getUniteTemps(0,tailleMess)));
        setLatence(latence / nbMess);
    }
    
    public double getUniteTemps(int idM, double tailleMess) {
        return (tailleMess/getMachine(idM).getCapacCarte()+getTempsPropa());
    }
    
    public boolean valideOrderTotal() {
        boolean isCorrect = true;
        for (int i=0; i< getNbMachines(); i++) {
            boolean isValideBySource = true;
            isValideBySource = valideOrderSequenceForSource(i);
            isCorrect = isCorrect && isValideBySource;
        }
        if (isCorrect) {
            System.out.println("Total order is CORRECT");
        } else {
            System.out.println("Total order is NOT correct");
        }
        return isCorrect;
    }
    
    public List<Message> getMessArrivedBySource(Integer idSource) {
        List<Message> list = new ArrayList<Message>();
        for (Message m: getMessageArrives()) {
            if (m.getSource().getId() == idSource) {
                list.add(m);
            }
        }
        return list;
    }
    
    public boolean valideOrderSequenceForSource(Integer idSource) {
        boolean isValide = true;
        List<Message> list = getMessArrivedBySource(idSource);
        Message temp = null;
        for (int i=0; i< list.size(); i++) {
            for (int j=i; j<list.size(); j++) {
                if (list.get(i).getDateMessDelivre()>= list.get(j).getMessDelivre()) {
                    temp = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, temp);
                }
            }
        }
        if (list.size()>=1) {
            for (int i=0; i<list.size()-1; i++) {
                if (list.get(i).getNumeroSequencer()>list.get(i+1).getNumeroSequencer()) {
                    isValide = false;
                    System.out.println("Source machine:"+list.get(i).getSource().getId() +"\n Total order is not correct for this 2 messages:\n"+list.get(i).toString()+"\n"+list.get(i+1).toString());
                }
            }
        }
        return isValide;
   }
    
    /* Calculer le debit du reseau 
     * Debit Total = sum (debit sur chaque machine)
     */
    public void calculateDebit() {
        Map<Integer, Integer> nbMessEachSource =  new HashMap<Integer, Integer>();
        Map<Integer, Double> sumEachSource = new HashMap<Integer, Double>();
        for (int i=0;i<getNbMachines();i++) {
            nbMessEachSource.put(i, 1);
            sumEachSource.put(i, 1.0);
        }
        
        for (Message m: getMessageArrives()) {
            int temp1 = nbMessEachSource.get(m.getSource().getId());
            double temps2 = sumEachSource.get(m.getSource().getId());
            temp1+=1;
            temps2+= (m.getDateMessDelivre()-m.getDate())/getUniteTemps(m.getSource().getId(), m.getSource().getCapacCarte());
            nbMessEachSource.put(m.getSource().getId(), temp1);
            sumEachSource.put(m.getSource().getId(), temps2);
        }
        
        double debit = 0;
        for (int i=0;i<getNbMachines();i++) {
            debit+= nbMessEachSource.get(i)/sumEachSource.get(i);
        }
       setDebit(debit);
    }
    /* Supposons qu'on a un liste de messages arrives. Definissons: 
     * dSi: le date qu'un message i est envoye'
     * dAi: la date qu'un message i est arrive'
     * latence = (sum[0->nbMessTotal](dAi-dSi)/uniteTemps(source qui envoie message i))/nbMessTotal
     */
    public void calculLatence() {
        double temp = 0;
        for (Message m: getMessageArrives()) {
            temp+=(m.getDateMessDelivre()-m.getDate())/getUniteTemps(m.getSource().getId(), m.getTaille());
        }
        setLatence(temp/(getSequencer().getBuffer().size()));
    }
    
    public static void main(String args[]) {
        // Config preparation
        Config config = new Config("config");
        // Initialiser N machine et l'etat initial du simulateur a partir du fichier config.properties
        FixedSequencer fixedSequencer = config.getExperimentationDB();
        
        //fixedSequencer.EmissionSuccessive(config.getTailleMess3Algo(), config.getNbMess3Algo());
        //fixedSequencer.pipeLine(config.getTailleMess3Algo(), config.getNbMess3Algo());
        fixedSequencer.Arbre(config.getTailleMess3Algo(), config.getNbMess3Algo());
        System.out.println("DEBIT:"+fixedSequencer.getDebit());
        System.out.println("LATENCE:"+fixedSequencer.getLatence());
    }
}
