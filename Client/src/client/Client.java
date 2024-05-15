package client;

import java.net.*;
import java.io.*;

public class Client {

    public static void main(String[] args) {
        String host = "localhost";
        int port = 0;
        try {
            port = Integer.parseInt("997");
        } catch (NumberFormatException e) {
            System.out.println("Nieprawidłowy argument: port");
            System.exit(-1);
        }
        //Inicjalizacja gniazda klienckiego
        Socket clientSocket = null;
        try {
            clientSocket = new Socket(host, port);
        } catch (UnknownHostException e) {
            System.out.println("Nieznany host.");
            System.exit(-1);
        } catch (ConnectException e) {
            System.out.println("Połączenie odrzucone.");
            System.exit(-1);
        } catch (IOException e) {
            System.out.println("Błąd wejścia-wyjścia: " + e);
            System.exit(-1);
        }
        System.out.println("Połączono z " + clientSocket);

        //Deklaracje zmiennych strumieniowych 
        String line = null; // Przechowuje linijkę tekstu od usera / serwera
        BufferedReader brSockInp = null; // Przechowuje referencję tekstu z serwera do odczytania przez BR
        BufferedReader brLocalInp = null; // Przechowuje referencję tekstu użytkownika do odczytania przez BR
        DataOutputStream output = null; // Wysyłanie danych do serwera

        //Utworzenie strumieni
        try {
            output = new DataOutputStream(clientSocket.getOutputStream());
            brSockInp = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            brLocalInp = new BufferedReader(new InputStreamReader(System.in)); //BR czyta w bajtach, a ISR konwertuje to na znaki
        } catch (IOException e) {
            System.out.println("Błąd przy tworzeniu strumieni: " + e);
            System.exit(-1);
        }
        System.out.println("Komendy do wyboru: zaloguj, rejestracja, lista, komendy");
        System.out.println("Komendy po zalogowaniu: saldo, wplata, wyplata, przelew, komendy, wyloguj");
        //Pętla główna klienta
        while (true) {
            try {
                line = brLocalInp.readLine();
                if (line != null) {
                    System.out.println("Wysyłam: " + line);
                    output.writeBytes(line + "\r");
                    output.flush(); // Przekazuje tekst do serwera
                }
                if ("quit".equals(line)) {
                    System.out.println("Kończenie pracy...");
                    clientSocket.close();
                    System.exit(0);
                }
                try {
                    line = brSockInp.readLine();
                    System.out.println("Otrzymano: " + line);  // Otrzymuje z powrotem tekst z serwera
                } catch (IOException e) {
                    System.out.println("Błąd wejścia-wyjścia: " + e);
                    System.exit(-1);
                }
            } catch (IOException e) {
                System.out.println("Błąd wejścia-wyjścia: " + e);
                System.exit(-1);
            }
        }
    }
}
