import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.File;
import java.util.Calendar;

import javax.swing.text.AbstractDocument.Content;

import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * Classe qui lance le serveur web
 */
public class ServeurMain {

    public static void main(String[] args) throws IOException, Exception {
        String[][] config = LectureXML.chargerConfig("../conf.d/serverWeb.conf");
        for(String[] s : config){
            for(String s2 : s)
            System.out.println(s2);
        }
        // recupere le port
        for(int nConfig = 0; nConfig<config.length; nConfig++){
            final int index = nConfig;
            int port = Integer.parseInt(config[nConfig][0]);
            // ouvre le socket serveur avec ce port
            Thread site = new Thread(()  -> {

                    File access = new File(config[index][3]);
                    File errors = new File(config[index][4]); 

                try{
                    System.out.println("Le serveur " + index + " est ouvert");
                    ServerSocket serv = new ServerSocket(port);
                    String serverRacine = "";
                // tant que vrai
                    while (true){
                        
                        
                        Socket client = serv.accept();
                        ecrireLog(access, client, "Nouvelle connexion sur le port" + config[index][0]);
                        System.out.println("La connection avec le serveur " + index + " est OK");
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
                            if(serverRacine.equals("")){
                                serverRacine = config[index][1];
                            }
                            String file = serverRacine + chemin[1];
                            System.out.println(file);
                            String listeFich = "";

                            File verifFichier = new File(file);
                            if (verifFichier.exists() && verifFichier.isDirectory()){
                                if (config[index][2] != null){
                                    file = config[index][1] + "/" + config[index][2];
                                } else {
                                    listeFich = contenuFichier(verifFichier);
                                }
                            }
                            
                            try{
                                // essaye de lire le fichier et de le mettre dans un tableau d'octet
                                byte[] tab = Files.readAllBytes(Paths.get(file));
                                // creation de la reponse
                                String mess =   "HTTP/1.1 200 OK\r\n" +
                                        "Content-Type: text/html; charset=iso-8859-1\r\n" +
                                        "Connection : Keep-Alive\r\n" +
                                        "File Data: "+ verifFichier.length() + " bytes\r\n";
                                
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
                                ecrireLog(errors, client, "Erreur 404");
                            } catch (IOException io){
                                System.out.println("Ceci est un dossier");
                                out.print("HTTP/1.1 200 OK\r\n" +
                                        "Content-Type: text/html; charset=iso-8859-1\r\n" +
                                        "Connection : Keep-Alive\r\n" +
                                        "File Data: 30 bytes\r\n\r\n" + listeFich);
                                out.flush();
                                serverRacine+= chemin[1];
                            }

                        }
                    
                    
                }
                }catch (IOException e){
                        System.out.println(e);

                    }
            });
            site.start();
        }
    }

    public static String contenuFichier(File f){
        String res = "<ul>";
        String[] files = f.list();
        for (String s : files){
            res += "<li><a href = \""+ s + "\">" + s +"</a></li>\n"; 
        }
        res += "</ul>";
        return res;
    }

    public static void ecrireLog(File f, Socket s, String m){
        try{
            if(!f.exists()){
            f.createNewFile();
            }
            BufferedWriter bf = new BufferedWriter(new FileWriter(f, true));
            Calendar date = Calendar.getInstance();
            String message = "";
            message+="IP: " + s.getInetAddress() + "  "
            +"Requête: " + m + "  "
            +"Date et heure: "+date.toInstant() + "  \n";

            bf.write(message);
            bf.close();
        } catch(IOException e){
            System.out.println("Problème de lecture de fichier");
        }
        
    }

    public static String ServerStatus(){
        String s = "<ul>";
        s+= "<li>Available Processor : " + Runtime.getRuntime().availableProcessors() + "</li>\n";
        s+= "<li>Free Memory : " + Runtime.getRuntime().freeMemory() + "</li>\n";
        s+= "<li>Total Memory : " + Runtime.getRuntime().totalMemory() + "</li>\n";
        s+= "<li>Max Memory : " + Runtime.getRuntime().maxMemory() + "</li>\n";
        s+= "<li>Nb de Processus : " + Thread.activeCount() + "</li>\n</ul>";
        return s;
    }
}