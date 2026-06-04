import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Classe qui lance le serveur web
 */
public class ServeurMain {
    public static void main(String[] args) throws IOException {
        // recupere le port
        int port = Integer.parseInt(args[0]);
        // ouvre le socket serveur avec ce port
        ServerSocket serv = new ServerSocket(port);

        // tant que vrai
        while (true){
            // le socket client accept le serveur
            Socket client = serv.accept();
            // on initialise la sortie
            PrintWriter out = new PrintWriter(client.getOutputStream(),true);
            // on initialise l'entree
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            // lecture de la requete
            String ligne = in.readLine();
            System.out.println(ligne); //

            // si la requete n'est pas nul et qu'elle contient "GET"
            if(ligne != null && ligne.contains("GET")){
                // split la ligne pour ne recuperer que le chemin demande
                String[] chemin = ligne.split(" ");
                // je rajoute un point devant pour construire un vrai chemin d'acces
                String file = "." + chemin[1];
                System.out.println(file);

                try{
                    // essaye de lire le fichier et de le mettre dans un tableau d'octet
                    byte[] tab = Files.readAllBytes(Paths.get(file));
                    // creation de la reponse
                    String mess =   "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: text/html; charset=iso-8859-1\r\n" +
                            "Connection : Keep-Alive\r\n" +
                            "File Data: 30 bytes\r\n";
                    
                    out.println(mess);
                    client.getOutputStream().write(tab);
                    // force l'envoi immediat des donnees
                    client.getOutputStream().flush();
                } catch (java.nio.file.NoSuchFileException e){
                    // si le fichier n'est pas trouve on lance l'erreur 404
                    System.out.println("Fichier non trouvé : " + file);
                    String erreur404 = "HTTP/1.1 404 Not Found\r\n\r\n<h1>Erreur 404 : Fichier introuvable</h1>";
                    out.print(erreur404);
                    out.flush();
                }
            }
            client.close();
        }
    }
}
