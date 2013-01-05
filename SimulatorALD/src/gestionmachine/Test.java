/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gestionmachine;

import java.util.LinkedList;

/**
 *
 * @author kimthuatnguyen
 */
public class Test {
    public static void main(String args[]) {
        // Initialize N machines
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
        
//        LinkedList<Integer> l = new LinkedList<Integer>();
//        l.addLast(2); l.addLast(0);
//        fixSequencer.getSequencer().removeSequenceNumberInList(l,1);
//        System.out.println("SEQUENCE NUMBERS OF EACH MACHINE:"+fixSequencer.getSequencer().getSequenceNbsOfMachine().toString());
//        
//        System.out.println("Smallest sequence number for a source:  "+fixSequencer.findSmallestSequenceNumberForASource(2));
//        System.out.println("Origin by sequence number:"+fixSequencer.findOriginBySequenceNumber(5).toString());
//        System.out.println("Last smaller sequence number for a source:"+fixSequencer.findLastSmallerSequenceNumberForASource(2, 1));
//        // Node test
//        Node n = new Node(fixSequencer.getLastMinimumSequenceNumber(fixSequencer.getSequencer().getSequenceNbsOfMachine()));
//        n.buildListDependencies(n.getName(), fixSequencer);
//        System.out.println("SEQUENCE NUMBERS OF EACH MACHINE:"+fixSequencer.getSequencer().getSequenceNbsOfMachine().toString());
//        System.out.println("BUFFER CONTENT"+fixSequencer.getMachine(2).printContentBuffer());
//        System.out.println("List dependencies: "+n.getDependences().toString());
//        LinkedList<Integer> l = new LinkedList<Integer>(); l.addLast(1); l.addLast(2);
//        System.out.println("SEQUENCE NUMBERS OF EACH MACHINE:"+fixSequencer.getSequencer().getSequenceNbsOfMachine().toString());
//        System.out.println("Last minimum sequence number:"+fixSequencer.getLastMinimumSequenceNumber(fixSequencer.getSequencer().getSequenceNbsOfMachine()).toString());
//        
        
        
    }
}
