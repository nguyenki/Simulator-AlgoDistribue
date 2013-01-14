/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gestionmachine;

import generateurMessage.GenerateurMessages;
import java.util.List;
import message.Message;

/**
 *
 * @author kimthuatnguyen
 */
public class Test {
    public static void main(String args[]) {
        // Initialize N machines

        
        /*
        FixedSequencer fixSequencer = new FixedSequencer(10,3, 1);
        
        
        // Generate the messages
        
        // Send messages from sources to Sequencer
        fixSequencer.sendUnicast(fixSequencer.getMachine(2), fixSequencer.getMachine(0), 10, 5);
        fixSequencer.sendUnicast(fixSequencer.getMachine(2), fixSequencer.getMachine(0), 10, 8);
        fixSequencer.sendUnicast(fixSequencer.getMachine(0), fixSequencer.getMachine(1), 40, 10);
        fixSequencer.sendUnicast(fixSequencer.getMachine(1), fixSequencer.getMachine(2), 20, 2);
        fixSequencer.sendUnicast(fixSequencer.getMachine(0), fixSequencer.getMachine(2), 20, 11);
        fixSequencer.sendUnicast(fixSequencer.getMachine(1), fixSequencer.getMachine(2), 20, 25);
        fixSequencer.sendUnicast(fixSequencer.getMachine(2), fixSequencer.getMachine(0), 10, 40);
        // Traiter messages dans le sequencer
        fixSequencer.getSequencer().assignSequenceNumber(fixSequencer.getSequencer().getBuffer());
        fixSequencer.getSequencer().diffusionMessagesFromSequencerToDestinations();
        System.out.println("Buffer content in destionation 0"+fixSequencer.getMachine(0).getBuffer().toString());
        System.out.println("Buffer content in destionation 1"+fixSequencer.getMachine(1).getBuffer().toString());
        System.out.println("Buffer content in destionation 2"+fixSequencer.getMachine(2).getBuffer().toString());

        //System.out.println("----------------------------");
        //System.out.println("Last minimum sequence number:"+fixSequencer.getLastMinimumSequenceNumber(fixSequencer.getSequencer().getSequenceNbsOfMachine()).toString());
        System.out.println("BUFFER SEQUENCER CONTENT:"+fixSequencer.getSequencer().getBuffer().toString());
        
        System.out.println("SEQUENCE NUMBERS OF EACH MACHINE:"+fixSequencer.getSequencer().getSequenceNbsOfMachine().toString());
        //System.out.println("Smallest sq number: "+fixSequencer.getLastMinimumSequenceNumber(fixSequencer.getSequencer().getSequenceNbsOfMachine())+"----"+fixSequencer.findSmallestSequenceNumberForASource(1));
        //System.out.println("Get header of a destionation buffer:  "+fixSequencer.getMachine(2).getBuffer().getFirst());
        fixSequencer.deliverMessages();
        System.out.println("Message arrived:"+fixSequencer.getMessageArrives().toString());
        
        *
        */
        
        FixedSequencer fixedSeq = new FixedSequencer(24, 14, 1000,2);
        GenerateurMessages generateur = new GenerateurMessages(10, 100);
        List<Message> messBroadcast = generateur.generateListMessageBroadcast(20, fixedSeq.getMachinesDefault());
        List<Message> messMulticast = generateur.generateListMessageMutilcast(12, fixedSeq.getMachinesDefault());
        List<Message> messUnicast = generateur.generateListMessageUnicast(60, fixedSeq.getMachinesDefault());
        
        //fixedSeq.sendUnicast(fixedSeq.getMachine(0), fixedSeq.getMachine(4), 1000, 0);
        //fixedSeq.sendUnicast(fixedSeq.getMachine(1), fixedSeq.getMachine(3), 10000, 2);
        
        
        fixedSeq.getSequencer().addMessToBuffer(messUnicast);
        fixedSeq.getSequencer().addMessToBuffer(messMulticast);
        fixedSeq.getSequencer().addMessToBuffer(messBroadcast);
        fixedSeq.getSequencer().assignSequenceNumber(fixedSeq.getSequencer().getBuffer());
        
        fixedSeq.getSequencer().printAllSequenceListForEachMachine();
        fixedSeq.getSequencer().diffusionMessagesFromSequencerToDestinations();
        fixedSeq.deliverMessages();
        fixedSeq.valideOrderTotal();
        fixedSeq.calculateDebit();
        fixedSeq.calculLatence();
        System.out.println("NUMBER OF MESS ARRIVED:"+fixedSeq.getMessageArrives().size());
        System.out.println("NUMBER OF MESS SEND:"+fixedSeq.getSequencer().getBuffer().size());
        System.out.println("DEBIT:"+fixedSeq.getDebit());
        System.out.println("LATENCE:"+fixedSeq.getLatence());
        
    }
}
