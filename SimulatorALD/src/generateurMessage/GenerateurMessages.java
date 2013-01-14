/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package generateurMessage;

import gestionmachine.FixedSequencer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import machine.Machine;
import message.Message;
import message.TypeMessage;
import sequencer.Sequencer;

/**
 *
 * @author kimthuatnguyen
 */
public class GenerateurMessages {

    private double limitTime;
    private double tailleMessageMax;

    public GenerateurMessages(double limitTime, double tailleMessageMax) {
        this.limitTime = limitTime;
        this.tailleMessageMax = tailleMessageMax;
    }

    public GenerateurMessages() {
    }
    
    
    public double getLimitTime() {
        return limitTime;
    }

    public void setLimitTime(double limitTime) {
        this.limitTime = limitTime;
    }

    public double getTailleMessageMax() {
        return tailleMessageMax;
    }

    public void setTailleMessageMax(double tailleMessageMax) {
        this.tailleMessageMax = tailleMessageMax;
    }
    
    

    public List<Message> generateListMessageUnicast(int n, Machine[] machines) {
        List<Message> listMessages = new ArrayList<Message>();
        int i = 0;
        int length = machines.length;
        Random randomGenerator = new Random();
        while (i < n) {
            double date = limitTime * randomGenerator.nextDouble();
            double tailleMessage = tailleMessageMax * randomGenerator.nextDouble();
            int idSource = randomGenerator.nextInt(length);
            int idDestination = randomGenerator.nextInt(length);
            while (idDestination == idSource) {
                idDestination = randomGenerator.nextInt(length);
            }
            if (listMessages != null) {
                while (listMessages.contains(new Message(machines[idSource], machines[idDestination], TypeMessage.UNICAST, tailleMessage, date))) {
                    date = limitTime * randomGenerator.nextDouble();
                    tailleMessage = tailleMessageMax * randomGenerator.nextDouble();
                    idSource = randomGenerator.nextInt(length);
                    idDestination = randomGenerator.nextInt(length);
                    while (idDestination == idSource) {
                        idDestination = randomGenerator.nextInt(length);
                    }
                }
            }
            listMessages.add(new Message(machines[idSource], machines[idDestination], TypeMessage.UNICAST, tailleMessage, date));
            i++;
        }
        Collections.sort(listMessages);
        return listMessages;

    }

    public List<Message> generateListMessageMutilcast(int n, Machine[] machines) {
        List<Message> listMessages = new ArrayList<Message>();
        int i = 0;
        int length = machines.length;
        Random randomGenerator = new Random();
        while (i < n) {
            double date = limitTime * randomGenerator.nextDouble();
            double tailleMessage = tailleMessageMax * randomGenerator.nextDouble();
            int numberOfDestination = randomGenerator.nextInt(length);
            while(numberOfDestination==0){
                numberOfDestination = randomGenerator.nextInt(length);
            }
            List<Machine> listDestinations = new ArrayList<Machine>();
            int idSource = randomGenerator.nextInt(length);
            for (int j = 0; j < numberOfDestination; j++) {
                int idDestination = randomGenerator.nextInt(length);
                while (idDestination == idSource || listDestinations.contains(machines[idDestination])) {
                    idDestination = randomGenerator.nextInt(length);
                }
                listDestinations.add(machines[idDestination]);
            }

            if (listMessages != null) {
                while (listMessages.contains(new Message(machines[idSource], listDestinations, TypeMessage.MULTICAST, tailleMessage, date))) {
                    date = limitTime * randomGenerator.nextDouble();
                    tailleMessage = tailleMessageMax * randomGenerator.nextDouble();
                    numberOfDestination = randomGenerator.nextInt(length);
                    idSource = randomGenerator.nextInt(length);
                    for (int j = 0; j < numberOfDestination; j++) {
                        int idDestination = randomGenerator.nextInt(length);
                        while (idDestination == idSource || listDestinations.contains(machines[idDestination])) {
                            idDestination = randomGenerator.nextInt(length);
                        }
                        listDestinations.add(machines[idDestination]);
                    }
                }
            }

            listMessages.add(new Message(machines[idSource], listDestinations, TypeMessage.MULTICAST, tailleMessage, date));
            i++;
        }
        Collections.sort(listMessages);
        return listMessages;
    }

    public List<Message> generateListMessageBroadcast(int n, Machine[] machines) {
        
        List<Message> listMessages = new ArrayList<Message>();
        List<Machine> listDestinations = Arrays.asList(machines);
        int i = 0;
        int length = machines.length;
        Random randomGenerator = new Random();
        while (i < n) {
            double date = limitTime * randomGenerator.nextDouble();
            double tailleMessage = tailleMessageMax * randomGenerator.nextDouble();
            int idSource = randomGenerator.nextInt(length);
            List<Machine> newList = new ArrayList<Machine>();
            for (Machine m: listDestinations) {
                if (m.getId()!=idSource) {
                    newList.add(m);
                }
            }
            if (listMessages != null) {
                
                while (listMessages.contains(new Message(machines[idSource], newList, TypeMessage.BROADCAST, tailleMessage, date))) {
                    date = limitTime * randomGenerator.nextDouble();
                    tailleMessage = tailleMessageMax * randomGenerator.nextDouble();
                    idSource = randomGenerator.nextInt(length);
                }
            }
            listMessages.add(new Message(machines[idSource], newList, TypeMessage.BROADCAST, tailleMessage, date));
            i++;
        }
        Collections.sort(listMessages);
        return listMessages;
    }
    
    public static void  main(String args[]) {
        FixedSequencer fx = new FixedSequencer(10,3, 1000,2);
        GenerateurMessages generateur = new GenerateurMessages(10, 1000);
        
        List<Message> messUnicast = generateur.generateListMessageUnicast(4, fx.getMachinesDefault());
        List<Message> messMulticast = generateur.generateListMessageMutilcast(8, fx.getMachinesDefault());
        List<Message> messBroadcast = generateur.generateListMessageBroadcast(2, fx.getMachinesDefault());
        System.out.println("UNICAST:"+messUnicast.toString());
        System.out.println("MULTICAST:"+messMulticast.toString());
        System.out.println("BROADCAST:"+messBroadcast.toString());
        
    }
}