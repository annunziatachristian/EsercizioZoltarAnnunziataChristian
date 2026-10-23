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

    public static void main(String[] args) {

        // Porta su cui il server resta in ascolto (il client deve usare la stessa)
        final int PORT = 5555;

        // Array con le possibili risposte per la personalità CATTIVO
        String[] cattivo = {
            "Sparisci, non ho tempo da perdere con te.",
            "Che domanda stupida.",
            "Non mi interessa, arrangiati.",
            "Vattene, mi stai annoiando."
        };

        // Array con le possibili risposte per la personalità DOLCE
        String[] dolce = {
            "Certo che si!.",
            "Sicuro al 100%.",
            "Non lo so, potrebbe essere.",
            "Probabilmente no, mi dispiace."
        };

        // Array con le possibili risposte per la personalità SCHERZOSO
        String[] scherzoso = {
            "Ahahah, seriamente mi stai chiedendo questo?",
            "La risposta e' ovviamente... 42.",
            "Dipende, hai mangiato la pizza oggi?",
            "Chiedilo di nuovo quando avrai smesso di essere buffo."
        };

        // Generatore di numeri casuali, usato per scegliere una risposta a caso
        Random r = new Random();

        // Memorizza la personalita' scelta dal client (vuota = non ancora scelta)
        String personalita = "";

        // Conterra' la risposta da inviare al client ad ogni messaggio ricevuto
        String risposta = "";

        try {
            // Creazione del server: da qui in poi resta in ascolto sulla porta 5555
            ServerSocket sSocket = new ServerSocket(PORT);
            System.out.println("In attesa di connessioni");

            // Il programma si blocca qui finche' un client non si connette
            Socket connessione = sSocket.accept();
            System.out.println("Connessione stabilita con " + connessione.getInetAddress());

            // Stream per ricevere messaggi dal client
            DataInputStream in = new DataInputStream(connessione.getInputStream());

            // Stream per inviare messaggi al client
            DataOutputStream out = new DataOutputStream(connessione.getOutputStream());

            // Variabile che tiene in vita il ciclo di conversazione
            boolean continua = true;

            // Ciclo principale: continua a scambiare messaggi finche' non arriva BYE
            while (continua) {

                // Aspetta e legge il prossimo messaggio inviato dal client
                String messaggio = in.readUTF();
                System.out.println("Messaggio dal client:" + messaggio);

                // --- Comando HELP: restituisce la lista dei comandi disponibili ---
                if (messaggio.equals("HELP")) {
                    risposta = "Comandi: PERSONA_CATTIVO - PERSONA_DOLCE - PERSONA_SCHERZOSO - DOMANDA_<testo> - HELP - BYE";

                // --- Comando PERSONA_...: imposta la personalita' dell'oracolo ---
                } else if (messaggio.startsWith("PERSONA_")) {

                    // Toglie "PERSONA_" (8 caratteri) e tiene solo il tipo scelto
                    String tipo = messaggio.substring(8);

                    // Controlla che la personalita' richiesta sia una di quelle valide
                    if (tipo.equals("CATTIVO") || tipo.equals("DOLCE") || tipo.equals("SCHERZOSO")) {
                        personalita = tipo;   // la salvo per le domande future
                        risposta = "Ok, personalirà impostata su " + tipo;
                    } else {
                        risposta = "ERRORE: personalità non valida";
                    }

                // --- Comando DOMANDA_...: restituisce una risposta casuale ---
                } else if (messaggio.startsWith("DOMANDA_")) {

                    // In base alla personalita' gia' impostata, sceglie l'array giusto
                    // e pesca una risposta a caso (indice da 0 a 3)
                    if (personalita.equals("CATTIVO")) {
                        risposta = cattivo[r.nextInt(4)];
                    } else if (personalita.equals("DOLCE")) {
                        risposta = dolce[r.nextInt(4)];
                    } else if (personalita.equals("SCHERZOSO")) {
                        risposta = scherzoso[r.nextInt(4)];
                    } else {
                        // Se non e' ancora stata scelta una personalita'
                        risposta = "ERRORE: scegli prima una personalita'.";
                    }

                // --- Comando BYE: chiude la conversazione (estensione del protocollo) ---
                } else if (messaggio.equals("BYE")) {
                    risposta = "ARRIVEDERCI";
                    continua = false;   // il while si fermera' al prossimo controllo

                // --- Qualsiasi altro comando non previsto dal protocollo ---
                } else {
                    risposta = "ERRORE: comando non riconosciuto. Scrivi HELP.";
                }

                // Invia la risposta calcolata al client
                out.writeUTF(risposta);
            }

            // Chiusura delle connessioni, una volta usciti dal ciclo
            connessione.close();
            sSocket.close();
            System.out.println("Connessione chiusa.");

        } catch (IOException e) {
            // Gestisce eventuali errori di comunicazione senza far crashare il programma
            System.err.println(e);
        }
    }
}