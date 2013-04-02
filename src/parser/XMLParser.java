package parser;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;

public class XMLParser {

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {

        //Now use the parser factory to create a SAXParser object
        SAXParser sp = SAXParserFactory.newInstance().newSAXParser();

        //Create an instance of this class; it defines all the handler methods
//        XMLHandler handler = new XMLHandler();
//
//        Finally, tell the parser to parse the input and notify the handler
//        sp.parse("objectContainer.xml", handler);
//
//        handler.readList();
    }
}
