import org.w3c.dom.*;
import javax.xml.parsers.*;

/**
 * Classe permettant de lire un fichier .xml
 */
public class LectureXML {
    public static String[][] chargerConfig(String fichier) throws Exception{

        Document document = null;
        DocumentBuilderFactory factory = null;
        
        factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        document = builder.parse(fichier);

        Element racine = document.getDocumentElement();
        NodeList racineNoeuds = racine.getChildNodes();
        String[][] tabReponse = new String[3][5];

        int nbRacineNoeuds = racineNoeuds.getLength();
        int indiceCol = 0;
        for (int i = 0; i<nbRacineNoeuds; i++){
            if(racineNoeuds.item(i).getNodeType() == Node.ELEMENT_NODE){
                Element site = (Element) racineNoeuds.item(i);

                int indiceLigne = 0;
                
                Element port = (Element) site.getElementsByTagName("port").item(0);
                tabReponse[indiceCol][indiceLigne] = port.getTextContent();
                indiceLigne++;
                Element docRoot = (Element) site.getElementsByTagName("DocumentRoot").item(0);
                tabReponse[indiceCol][indiceLigne] = docRoot.getTextContent();
                indiceLigne++;
                try {
                    Element defIndex = (Element) site.getElementsByTagName("Default").item(0);
                    tabReponse[indiceCol][indiceLigne] = defIndex.getTextContent();
                    indiceLigne++;
                } catch (NullPointerException npe){
                    tabReponse[indiceCol][indiceLigne] = null;
                    indiceLigne++;
                }
                Element logAcc = (Element) site.getElementsByTagName("Acceslog").item(0);
                tabReponse[indiceCol][indiceLigne] = logAcc.getTextContent();
                indiceLigne++;
                Element errLog = (Element) site.getElementsByTagName("Errorlog").item(0);
                tabReponse[indiceCol][indiceLigne] = errLog.getTextContent();
                indiceCol++;
            } 
        }
        return tabReponse;
    }
}