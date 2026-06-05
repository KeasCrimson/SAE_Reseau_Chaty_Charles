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
        String[][] tabReponse = null ;

        int nbRacineNoeuds = racineNoeuds.getLength();
        int indiceCol = 0;
        for (int i = 0; i<nbRacineNoeuds; i++){
            if(racineNoeuds.item(i).getNodeType() == Node.ELEMENT_NODE){
                Element site = (Element) racineNoeuds.item(i);

                tabReponse = new String[3][5];
                int indiceLigne = 0;
                
                Element port = (Element) site.getElementsByTagName("port").item(0);
                Element docRoot = (Element) site.getElementsByTagName("DocumentRoot").item(0);
                Element defIndex = (Element) site.getElementsByTagName("Default").item(0);
                Element logAcc = (Element) site.getElementsByTagName("Acceslog").item(0);
                Element errLog = (Element) site.getElementsByTagName("Errorlog").item(0);
                
                tabReponse[indiceCol][indiceLigne++] = port.getTextContent();
                tabReponse[indiceCol][indiceLigne++] = docRoot.getTextContent();
                tabReponse[indiceCol][indiceLigne++] = defIndex.getTextContent();
                tabReponse[indiceCol][indiceLigne++] = logAcc.getTextContent();
                tabReponse[indiceCol][indiceLigne++] = errLog.getTextContent();

                indiceCol++;
            } 
        }
        return tabReponse;
    }
}