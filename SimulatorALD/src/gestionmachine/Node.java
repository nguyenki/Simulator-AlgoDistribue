/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gestionmachine;

import java.util.Iterator;
import java.util.LinkedList;
import message.Message;


/**
 *
 * @author kimthuatnguyen
 */
public class Node {
    private LinkedList<Integer> name;
    private LinkedList<LinkedList<Integer>> dependences;
    private boolean deadlock = false;
    
    public Node(LinkedList<Integer> name) {
        this.name = name;
        this.dependences = new LinkedList<LinkedList<Integer>>();  
    }
    
    public LinkedList<Integer> getName() {
        return name;
    }

    public void setName(LinkedList<Integer> name) {
        this.name = name;
    }

    public LinkedList<LinkedList<Integer>> getDependences() {
        return dependences;
    }

    public void setDependences(LinkedList<LinkedList<Integer>> dependences) {
        this.dependences = dependences;
    }

    public boolean isDeadlock() {
        return deadlock;
    }

    public void setDeadlock(boolean deadlock) {
        this.deadlock = deadlock;
    }
    
    /*
     * Methodes
     */
    public void addDependency(LinkedList<Integer> dependecy) {
        this.dependences.addLast(dependecy);
    }
    
    // TODO: a revoir
    public boolean buildListDependencies(LinkedList<Integer> list, FixedSequencer fixedSequencer) {
        setName(list);
        if (list.isEmpty()) {
            return false;
        }
        Integer nbSequence = list.getLast();
        int idMachineSource = list.get(0);
        int idMachineDestination = list.get(1);
        //System.out.println(idMachineSource+":-----:"+ idMachineDestination+":-----:"+nbSequence);
        LinkedList<Integer> temp = new LinkedList<Integer>();
        // neu gap 1 cai la header ma ko phu thuoc vao bat ky cai sequence nb nao thi cung co the dung luon
        LinkedList<Integer> sd = new LinkedList<Integer>();
        sd.addLast(idMachineSource);
        sd.addLast(idMachineDestination);
        if (nbSequence == fixedSequencer.findSmallestSequenceNumberForASource(idMachineSource)) {
           // Neu so sequence da la nho nhat thi chi can kiem tra thang header cua buffer va them vao list dependencies
           if (fixedSequencer.getMachine(idMachineDestination).getBuffer().isEmpty()) {
               return false;
           }
           Message headerInBuffer = fixedSequencer.getMachine(idMachineDestination).getBuffer().getFirst();
           if (headerInBuffer.getNumeroSequencer() == nbSequence) {
               addDependency(list);
               return false;
           }
           temp.addLast(headerInBuffer.getSource().getId());
           temp.addLast(idMachineDestination);
           temp.addLast(headerInBuffer.getNumeroSequencer());
           if (getDependences().contains(temp)) {
               setDeadlock(true);
               return true;
           } else {
               addDependency(temp);
           }
           buildListDependencies(temp, fixedSequencer);
        } else {
               // So sequence chua phai la nho nhat (xet theo tung may source) them so sequence gan nhat va goi lai ham
               temp = fixedSequencer.findLastSmallerSequenceNumberForASource(idMachineSource, nbSequence);
               if (getDependences().contains(temp)) {
                   setDeadlock(true);
                   return true;
               } else {
                   if (temp.isEmpty()) {
                       return false;
                   }
                    addDependency(temp);
               }
               buildListDependencies(temp, fixedSequencer);
           
        }
        return false;
    }
    
    public boolean resolveDependencies(FixedSequencer fixedSequencer) {
        Integer nbSequence = -1;
        if (getDependences().isEmpty()) {
            return true;
        }
        if (isDeadlock()) {
            LinkedList<Integer> smallestSequenceNb = findSmallestDependenciesSequenceNumber();
            nbSequence = smallestSequenceNb.removeLast();
            fixedSequencer.deliverAMessage(nbSequence, smallestSequenceNb.get(1));
            // Xoa sequence number trong list cac sequence tuong ung voi may nguon
            fixedSequencer.getSequencer().removeSequenceNumberInList(smallestSequenceNb, nbSequence);
            // Reset the state of deadlock
            setDeadlock(false);
            return true;
        } else {
            LinkedList<Integer> headerToResolve = this.dependences.removeLast();
            System.out.println("DEBUG: HEADER:"+headerToResolve.toString());
            fixedSequencer.deliverAMessage(headerToResolve.getLast(), headerToResolve.get(1));
            nbSequence = headerToResolve.removeLast();
            fixedSequencer.getSequencer().removeSequenceNumberInList(headerToResolve, nbSequence);
            System.out.println("In:resolveDependeicies: DEBUG dependencies:"+getDependences().toString());
            if (getDependences().isEmpty()) {
                return true;
            } else {
                buildListDependencies(this.dependences.getLast(), fixedSequencer);
            }
        }
        return false;
    }
    
    public LinkedList<Integer> findSmallestDependenciesSequenceNumber() {
        LinkedList<Integer> l = new LinkedList<Integer>();
        l.addLast(Integer.MAX_VALUE);
        Iterator<LinkedList<Integer>> it = this.dependences.iterator();
        while (it.hasNext()) {
            LinkedList<Integer> ll = it.next();
            if (ll.getLast()<=l.getLast()) {
                l = ll;
            }
            
        }
        this.dependences.remove(l);
        return l;
    } 
}
