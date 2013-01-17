/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import gestionmachine.FixedSequencer;
import java.io.FileInputStream;
import java.util.Properties;

/**
 *
 * @author Kim Thuat Nguyen
 * @author Dang Chuan Nguyen
 */
public class Config {
    Properties properties;
    
    public Config(String filePath) {
        try {
            this.properties = new Properties();
            this.properties.load(new FileInputStream(System.getProperty("user.dir")+"/"+filePath+".properties"));
        } catch (Exception e) {
            System.out.println("PROPERTIES FILE NOT FOUND");
            e.printStackTrace();
        }
    }
    
    public FixedSequencer getExperimentationDB() {
        System.out.println("Starting simulator.....................................");
        return new FixedSequencer(getLimitTime(), getNbMachine(), getCapaciteMachine() , getTempsPropagation());
    }
    public double getLimitTime() {
        return Double.valueOf(this.properties.getProperty("tempsLimit"));
    }
    
    public int getNbMachine() {
        return Integer.parseInt(this.properties.getProperty("nbMachine"));
    }
    
    public double getCapaciteMachine() {
        return Double.valueOf(this.properties.getProperty("capaciteMachine"));
    }
    
    public double getTempsPropagation() {
        return Double.valueOf(this.properties.getProperty("tempsPropagation"));
    }
    
    public double getTailleMaxMess() {
        return Double.valueOf(this.properties.getProperty("tailleMax"));
    }
    
    public int getNbMessUnicast() {
        return Integer.parseInt(this.properties.getProperty("nbUnicast"));
    }
    
    public int getNbMessMulticast() {
        return Integer.parseInt(this.properties.getProperty("nbMulticast"));
    }
    
    public int getNbMessBroadcast() {
        return Integer.parseInt(this.properties.getProperty("nbBroadcast"));
    }
    
    public int getNbMess3Algo() {
        return Integer.parseInt(this.properties.getProperty("nbMess"));
    }
    
    public double getTailleMess3Algo() {
        return Double.valueOf(this.properties.getProperty("tailleMess"));
    }
}
