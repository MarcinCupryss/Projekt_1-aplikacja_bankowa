package client;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

/*
Autorzy:
Marcin Cupryś 89529
Piotr Solecki 88349
 */

public class Client {

    public static void main(String[] args) {
        String host = "localhost";
        int port = 997;

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

        String line = null;
        BufferedReader brSockInp = null; // Przechowuje referencję tekstu z serwera do odczytania przez BufferedReader
        BufferedReader brLocalInp = null; // Przechowuje referencję tekstu użytkownika do odczytania przez BufferedReader
        BufferedWriter writer = null;

        try {
            OutputStream outputStream = clientSocket.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
            writer = new BufferedWriter(outputStreamWriter);

            brSockInp = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
            brLocalInp = new BufferedReader(new InputStreamReader(System.in)); // BufferedReader czyta w bajtach, a InputStreamReader konwertuje to na znaki
        } catch (IOException e) {
            System.out.println("Błąd przy tworzeniu strumieni: " + e);
            System.exit(-1);
        }

        System.out.println("""
                \nKomendy w aplikacji dla użytkownika niezalogowanego:
                - zaloguj (loguje na konto bankowe)
                - rejestracja (pozwala na utworzenie nowego konta bankowego)
                - lista (wyświetla listę loginów istniejących w bazie danych)
                - komendy (wyświetla skróconą listę komend)
                \nKomendy w aplikacji dla użytkownika zalogowanego:
                - dane (wyświetla wszystkie dane zalogowanego użytkownika)
                - saldo (wyświetla środki na koncie)
                - wpłata (pozwala dodać środki na konto)
                - wypłata (pozwala wypłacić środki z konta)
                - przelew (pozwala wykonać przelew na inne konto bankowe)
                - komendy (wyświetla skróconą listę komend)
                - wyloguj (wylogowuje z konta)
                \nKomendy w aplikacji dla admina:
                - zmień login (zmiana loginu)
                - zmień imię (zmiana imienia)
                - zmień nazwisko (zmiana nazwiska)
                - zmień pesel (zmiana PESELu)
                - zmień hasło (zmiana hasła)
                - komendy (wyświetla skróconą listę komend)
                - wyloguj (wylogowuje z konta)
                """);

        while (true) {
            try {
                line = brLocalInp.readLine();
                if (line != null) {
                    line = line.trim();
                    System.out.println("Wysyłam: " + line);
                    writer.write(line + System.lineSeparator());
                    writer.flush();
                }
                if ("zamknij".equals(line)) {
                    System.out.println("Kończenie pracy...");
                    clientSocket.close();
                    System.exit(0);
                }
                try {
                    String receivedLine = brSockInp.readLine();
                    System.out.println("Otrzymano: " + receivedLine);
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