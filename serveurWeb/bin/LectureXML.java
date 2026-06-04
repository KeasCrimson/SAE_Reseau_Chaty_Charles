import org.w3c.dom.*;
import javax.xml.parsers.*;

/**
 * Classe permettant de lire un fichier .xml
 */
public class LectureXML {
    public static String[] chargerConfig(String fichier) throws Exception{

        Document document = null;
        DocumentBuilderFactory factory = null;
        
        factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        document = builder.parse(fichier);

        Element racine = document.getDocumentElement();

        NodeList racineNoeuds = racine.getChildNodes();
        
        String[] tabReponse = new String[5];

        Element port = (Element) racine.getElementsByTagName("port").item(0);
        tabReponse[0] = port.getTextContent();

        Element docRoot = (Element) racine.getElementsByTagName("DocumentRoot").item(0);
        tabReponse[1] = docRoot.getTextContent();

        Element defIndex = (Element) racine.getElementsByTagName("Default").item(0);
        tabReponse[2] = defIndex.getTextContent();

        Element logAcc = (Element) racine.getElementsByTagName("Acceslog").item(0);
        tabReponse[3] = logAcc.getTextContent();

        Element errLog = (Element) racine.getElementsByTagName("Errorlog").item(0);
        tabReponse[4] = errLog.getTextContent();

        return tabReponse;
    }
}