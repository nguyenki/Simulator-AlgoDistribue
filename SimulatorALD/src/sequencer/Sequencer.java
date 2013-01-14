/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sequencer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import machine.Machine;
import message.Message;

/**
 *
 * @author Kim Thuat Nguyen
 */
public class Sequencer {
    private LinkedList<Message> buffer; // Contient tous les messages envoyes par les sources
    private Map<LinkedList<Integer>, LinkedList<Integer>> sequenceNbsOfMachine; // Contient tous les listes de numeros de sequence pour chaque machine
    
    public Sequencer() {
        this.buffer = new LinkedList<Message>();
        this.sequenceNbsOfMachine = new HashMap<LinkedList<Integer>, LinkedList<Integer>>();
    }
    
    public Map<LinkedList<Integer>, LinkedList<Integer>> getSequenceNbsOfMachine() {
        return sequenceNbsOfMachine;
    }

    public void setSequenceNbsOfMachine(Map<LinkedList<Integer>, LinkedList<Integer>> sequenceNbsOfMachine) {
        this.sequenceNbsOfMachine = sequenceNbsOfMachine;
    }

    public void addSequenceNbToList(LinkedList<Integer> idList, Integer nbSequence) {
        if (getSequenceNbsOfMachine().containsKey(idList)) {
            if (!getSequenceNbsOfMachine().get(idList).contains(nbSequence)) {
                getSequenceNbsOfMachine().get(idList).add(nbSequence);
            }
        } else {
            LinkedList<Integer> listSequencesNB = new LinkedList<Integer>();
            listSequencesNB.add(nbSequence);
            getSequenceNbsOfMachine().put(idList, listSequencesNB);
        }
        Collections.sort(getSequenceNbsOfMachine().get(idList));
    }
    
    // Remove number sequence in the list of sequencer according to the source  machine
    public void removeSequenceNumberInList(LinkedList<Integer> idList, Integer nbSequence) {
        if (getSequenceNbsOfMachine().get(idList).contains(nbSequence)) {
            LinkedList<Integer> newList = getSequenceNbsOfMachine().get(idList);
            Integer index =-1;
            for (Integer i: newList) {
                if (i==nbSequence) {
                    index = i;
                }
            }
            if (index!=-1) {
                newList.remove(index);
            }
            System.out.println("DEBUG: REMOVED SEQUENCE NB:"+nbSequence+":  idList:"+idList.toString());
            getSequenceNbsOfMachine().put(idList, newList);
        }
        if (getSequenceNbsOfMachine().get(idList).isEmpty()) {
            getSequenceNbsOfMachine().remove(idList);
        }
    }
    
    public LinkedList<Message> getBuffer() {
        return buffer;
    }

    public void setBuffer(LinkedList<Message> buffer) {
        this.buffer = buffer;
    }
    
    public void addMessToBuffer(Message m) {
        this.buffer.add(m);
        Collections.sort(buffer);
    }
    
    public void addMessToBuffer(List<Message> listMess) {
        Iterator<Message> it = listMess.iterator();
        while (it.hasNext()) {
            addMessToBuffer(it.next());
        }
    }
    public void assignSequenceNumber(LinkedList<Message> buffer) {
        int nbSequence = 0;
        Iterator<Message> it = buffer.iterator();
        while (it.hasNext()) {
            Message mess = it.next();
            mess.setNumeroSequencer(nbSequence);
            // Construire la liste des sequence number pour chaque machine
            List<Machine> destionations = mess.getDestinations();
            Iterator<Machine> itM = destionations.iterator();
            while (itM.hasNext()) {
                Machine ma = itM.next();
                LinkedList<Integer> sd = new LinkedList<Integer>();
                sd.addLast(mess.getSource().getId());
                sd.addLast(ma.getId());
                addSequenceNbToList(sd, mess.getNumeroSequencer());
            }
            nbSequence++;
        }
    }
    
    public void sendMessageToDestination(Message mess) {
        List<Machine> machines = mess.getDestinations();
        Iterator<Machine> it = machines.iterator();
        while (it.hasNext()) {
            Machine dest = it.next();
             dest.addMessage(mess);
        }
    }
    
    /* TODO: need to review the order of messages in the buffer 
     * because each message have the different size so that maybe a smaller message can be arrived
     * before another one who have a smaller sequencer number
    */
    public void diffusionMessagesFromSequencerToDestinations() {
        assignSequenceNumber(getBuffer());
        Iterator<Message> it = getBuffer().iterator();
        while (it.hasNext()) {
            Message mess = it.next();
            sendMessageToDestination(mess);
        }
    }
    
    public void printAllSequenceListForEachMachine() {
        System.out.println(getBuffer().toString());
    }
 }
