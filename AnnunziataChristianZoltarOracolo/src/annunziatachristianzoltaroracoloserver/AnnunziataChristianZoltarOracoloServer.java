/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package annunziatachristianzoltaroracoloserver;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
/**
 *
 * @author annunziata.christian
 */
public class AnnunziataChristianZoltarOracoloServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        final int PORT = 5555;
        
        String[] cattivo = {
            "Sparisci, non ho tempo da perdere con te.",
            "Che domanda stupida.",
            "Non mi interessa, arrangiati.",
            "Vattene, mi stai annoiando."
        };
        
        String[] dolce = {
            "Certo che si!.",
            "Sicuro al 100%.",
            "Non lo so, potrebbe essere.",
            "Probabilmente no, mi dispiace."
        };
        
        String[] scherzoso = {
             "Ahahah, seriamente mi stai chiedendo questo?",
            "La risposta e' ovviamente... 42.",
            "Dipende, hai mangiato la pizza oggi?",
            "Chiedilo di nuovo quando avrai smesso di essere buffo."
        };
        
        Random r = new Random();
        String personalita = "";
        String risposta = "";
        
        try {
            ServerSocket sSocket = new ServerSocket(PORT);
            System.out.println("In attesa di connessioni");
            
            Socket connessione = sSocket.accept();
            System.out.println("Connessione stabilita con " + connessione.getInetAddress());
            
            DataInputStream in = new DataInputStream(connessione.getInputStream());
            DataOutputStream out = new DataOutputStream(connessione.getOutputStream());
            
            boolean continua = true;
            
            while (continua) {
                String messaggio = in.readUTF();
                System.out.println("Messaggio dal client:" + messaggio);
                
                if(messaggio.equals("HELP")) {
                    risposta = "Comandi: PERSONA_CATTIVO - PERSONA_DOLCE - PERSONA_SCHERZOSO - DOMANDA_<testo> - HELP - BYE";
                } else if (messaggio.startsWith("PERSONA_")) {    
                    String tipo = messaggio.substring(8);
                    
                    if(tipo.equals("CATTIVO")|| tipo.equals("DOLCE") || tipo.equals("SCHERZOSO")) {
                        personalita = tipo;
                        risposta = "Ok, personalirà impostata su " + tipo;
                    } else {
                        risposta= "ERRORE: personalità npon valida";
                    }
                    
                } else if (messaggio.startsWith("DOMANDA_")) {
                    
                    if(personalita.equals("CATTIVO")) {
                        risposta = cattivo[r.nextInt(4)];
                    } else if (personalita.equals("DOLCE")) {
                        risposta = dolce[r.nextInt(4)];
                    } else if (personalita.equals("SCHERZOSO")) {
                        risposta = scherzoso[r.nextInt(4)];
                    } else {
                        risposta = "ERRORE: scegli prima una personalita'.";
                    }
                
                } else if(messaggio.equals("BYE")) {
                    risposta = "ARRIVEDERCI";
                    continua = false;
                    
                } else {
                    risposta = "ERRORE: comando non riconosciuto. Scrivi HELP.";
                }
                
                out.writeUTF(risposta);
            }
            
            connessione.close();
            sSocket.close();
            System.out.println("Connessione chiusa.");
            
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}