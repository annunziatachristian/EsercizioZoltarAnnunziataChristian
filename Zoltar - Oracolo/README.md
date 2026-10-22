# Zoltar - L'Oracolo

Progetto di TPSIT - Applicazione client-server che simula un oracolo
in grado di rispondere a qualsiasi domanda, con tre diverse personalità.

**Autore:** Annunziata Christian - Classe 5C

## Struttura del progetto

- `AnnunziataChristianZoltarOracolo/` → Applicazione **Server** (console)
- `AnnunziataChristianZoltarOracoloClient/` → Applicazione **Client** con interfaccia grafica (Swing)

## Tecnologie utilizzate

- Java SE
- Socket TCP (`ServerSocket`, `Socket`)
- Swing (Form grafica creata con NetBeans)
- Porta: **5555**

## Protocollo di comunicazione

| Comando | Descrizione | Esempio |
|---|---|---|
| `PERSONA_<tipo>` | Imposta la personalità dell'oracolo | `PERSONA_CATTIVO` |
| `DOMANDA_<testo>` | Pone una domanda all'oracolo | `DOMANDA_Troverò lavoro?` |
| `HELP` | Mostra la lista dei comandi disponibili | `HELP` |
| `BYE` | Chiude la connessione *(estensione del protocollo)* | `BYE` |

### Personalità disponibili
- `CATTIVO` → risposte aggressive
- `DOLCE` → risposte gentili
- `SCHERZOSO` → risposte ironiche

### Messaggi di errore gestiti
- Personalità non valida
- Domanda senza aver impostato prima una personalità
- Comando non riconosciuto

## Come eseguire il progetto

1. Aprire entrambi i progetti in NetBeans
2. Avviare prima il **Server** (mostra in console l'attesa di connessioni)
3. Avviare il **Client**
4. Nel client inserire `localhost` (se sulla stessa macchina) oppure l'IP del server
5. Premere **CONNETTI**
6. Scegliere una personalità e iniziare a fare domande