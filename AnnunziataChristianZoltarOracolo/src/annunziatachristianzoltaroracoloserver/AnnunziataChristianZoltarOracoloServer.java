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
        ServerSocket sSocket;
        Socket connessione;

        final int PORT = 5000; // (final significa che è 1 costante)

        // Personalità scelta dal client (inizialmente vuota)
        String personalitaCorrente = "";

        try {
            // Il Server risponde sulla porta 5000
            sSocket = new ServerSocket(PORT);

            // Ciclo in attesa delle connessioni
            while (true) {

                System.out.println("In attesa di connessioni...");
                connessione = sSocket.accept();
                System.out.println("Connessione stabilita.");

                // Stampo l'Indirizzo IP di chi si è collegato
                System.out.println(connessione.getInetAddress());

                // Dichiaro l'InputStream per leggere i dati dal CLIENT
                InputStream inputStream = connessione.getInputStream();
                DataInputStream dataInputStream = new DataInputStream(inputStream);

                // Dichiaro l'OutputStream per inviare i dati al CLIENT
                OutputStream outputStream = connessione.getOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

                boolean continuaConversazione = true;

                // Ciclo per gestire più messaggi dallo stesso client
                while (continuaConversazione) {

                    // Ricevo dati dal client
                    String messaggio = dataInputStream.readUTF();
                    System.out.println("Messaggio dal client: " + messaggio);

                    String risposta;

                    // --- Comando HELP ---
                    if (messaggio.equals("HELP")) {
                        risposta = "Comandi disponibili: PERSONA_CATTIVO, PERSONA_DOLCE, PERSONA_SCHERZOSO, DOMANDA_<testo>, HELP, BYE";
                    }
                    // --- Comando PERSONA_... ---
                    else if (messaggio.startsWith("PERSONA_")) {
                        String tipo = messaggio.substring(8); // tolgo "PERSONA_"

                        if (tipo.equals("CATTIVO") || tipo.equals("DOLCE") || tipo.equals("SCHERZOSO")) {
                            personalitaCorrente = tipo;
                            risposta = "OK, personalità impostata su " + tipo;
                        } else {
                            risposta = "ERRORE: personalità non valida. Usa CATTIVO, DOLCE o SCHERZOSO.";
                        }
                    }
                    // --- Comando DOMANDA_... ---
                    else if (messaggio.startsWith("DOMANDA_")) {

                        if (personalitaCorrente.equals("")) {
                            risposta = "ERRORE: devi prima impostare una personalità con PERSONA_...";
                        } else {

                            Random r = new Random();

                            String[] rispCattivo = {
                                "Sparisci, non ho tempo da perdere con te.",
                                "Che domanda stupida.",
                                "Non mi interessa, arrangiati.",
                                "Vattene, mi stai annoiando."
                            };

                            String[] rispDolce = {
                                "Tutto andrà bene, tesoro.",
                                "Non preoccuparti, la vita ti sorprenderà.",
                                "Sei una persona speciale, le cose si risolveranno.",
                                "Abbi fiducia, il meglio deve ancora venire."
                            };

                            String[] rispScherzoso = {
                                "Ahahah, seriamente mi stai chiedendo questo?",
                                "La risposta è ovviamente... 42.",
                                "Dipende, hai mangiato la pizza oggi?",
                                "Chiedilo di nuovo quando avrai smesso di essere buffo."
                            };

                            if (personalitaCorrente.equals("CATTIVO")) {
                                risposta = rispCattivo[r.nextInt(rispCattivo.length)];
                            } else if (personalitaCorrente.equals("DOLCE")) {
                                risposta = rispDolce[r.nextInt(rispDolce.length)];
                            } else {
                                risposta = rispScherzoso[r.nextInt(rispScherzoso.length)];
                            }
                        }
                    }
                    // --- Comando BYE ---
                    else if (messaggio.equals("BYE")) {
                        risposta = "ARRIVEDERCI!";
                        continuaConversazione = false;
                    }
                    // --- Comando non riconosciuto ---
                    else {
                        risposta = "ERRORE: comando non riconosciuto. Scrivi HELP per la lista comandi.";
                    }

                    // Invio la risposta al client
                    dataOutputStream.writeUTF(risposta);
                }

                connessione.close();
                System.out.println("Connessione chiusa.");
            }

        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
