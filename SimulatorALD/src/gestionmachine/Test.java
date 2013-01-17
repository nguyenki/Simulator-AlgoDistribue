/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gestionmachine;

import config.Config;
import generateurMessage.GenerateurMessages;
import java.util.List;
import message.Message;

/**
 *
 * @author kimthuatnguyen
 */
public class Test {
    public static void main(String args[]) {
        // Config preparation
        Config config = new Config("config");
        // Initialiser N machine et l'etat initial du simulateur a partir du fichier config.properties
        FixedSequencer fixedSeq = config.getExperimentationDB();
        
        // Generer les messages de facon alleatoires utilisant le generateur.
        GenerateurMessages generateur = new GenerateurMessages(config.getLimitTime(), config.getTailleMaxMess());
        List<Message> messBroadcast = generateur.generateListMessageBroadcast(config.getNbMessBroadcast(), fixedSeq.getMachinesDefault());
        List<Message> messMulticast = generateur.generateListMessageMutilcast(config.getNbMessMulticast(), fixedSeq.getMachinesDefault());
        List<Message> messUnicast = generateur.generateListMessageUnicast(config.getNbMessUnicast(), fixedSeq.getMachinesDefault());
        
        // Envoyer les messages des sources vers le sequencer        
        fixedSeq.getSequencer().addMessToBuffer(messUnicast);
        fixedSeq.getSequencer().addMessToBuffer(messMulticast);
        fixedSeq.getSequencer().addMessToBuffer(messBroadcast);
        fixedSeq.getSequencer().assignSequenceNumber(fixedSeq.getSequencer().getBuffer());
        
        /*Diffuser les messages de sequencer vers les destination et
         * Validation de l'ordre total du simulateur et calcul de latence et debit
         */
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
