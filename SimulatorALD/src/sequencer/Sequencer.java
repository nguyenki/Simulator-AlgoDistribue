/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sequencer;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import machine.Machine;
import message.Message;

/**
 *
 * @author Kim Thuat Nguyen
 */
public class Sequencer {
    private int seqNumber;
    private LinkedList<Message> buffer; // Contient tous les messages envoyes par les sources
    
    
    public Sequencer(int seqNumber) {
        this.seqNumber = seqNumber;
        this.buffer = new LinkedList<Message>();
    }
    
    public Sequencer() {
        this.seqNumber = 0;
        this.buffer = new LinkedList<Message>();
    }
    
    public int getSeqNumber() {
        return seqNumber;
    }

    public void setSeqNumber(int seqNumber) {
        this.seqNumber = seqNumber;
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
    
    public void assignSequenceNumber(LinkedList<Message> buffer) {
        int nbSequence = 0;
        Iterator<Message> it = buffer.iterator();
        while (it.hasNext()) {
            Message mess = it.next();
            mess.setNumeroSequencer(nbSequence);
            nbSequence++;
        }
        setSeqNumber(buffer.size());
    }
    
    public void sendMessageToDestination(Message mess) {
        List<Machine> machines = mess.getDestinations();
        Iterator<Machine> it = machines.iterator();
        while (it.hasNext()) {
            Machine dest = it.next();
             dest.addMessage(mess);
        }
    }
    
    public void diffusionMessagesFromSequencer() {
        Iterator<Message> it = getBuffer().iterator();
        while (it.hasNext()) {
            Message mess = it.next();
            sendMessageToDestination(mess);
        }
    }
 }
