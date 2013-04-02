//package parser;
//
//import org.xml.sax.Attributes;
//import org.xml.sax.SAXException;
//import org.xml.sax.helpers.DefaultHandler;
//
//import java.util.ArrayList;
//
//public class XMLHandler extends DefaultHandler {
//
//    private Account account;
//    private String value;
//    private ArrayList<Account> accountList = new ArrayList<Account>();
//
//    /*
//     * When the parser encounters plain text (not XML elements),
//     * it calls(this method, which accumulates them in a string buffer
//     */
//    public void characters(char[] buffer, int start, int length) {
//        value = new String(buffer, start, length);
//    }
//
//    /*
//     * Every time the parser encounters the beginning of a new element,
//     * it calls this method, which resets the string buffer
//     */
//    public void startElement(String uri, String localName,
//                             String qName, Attributes attributes) throws SAXException {
//        System.out.println("QName is " + qName);
//        if (qName.equalsIgnoreCase("Account")) {
//            account = new Account();
//            account.setType(attributes.getValue("type"));
//        }
//    }
//
//    /*
//     * When the parser encounters the end of an element, it calls this method
//     */
//    public void endElement(String uri, String localName, String qName)
//            throws SAXException {
//
//        if (qName.equalsIgnoreCase("Account")) {
//            accountList.add(account);
//        } else if (qName.equalsIgnoreCase("Name")) {
//            account.setName(value);
//        } else if (qName.equalsIgnoreCase("Id")) {
//            account.setId(Integer.parseInt(value));
//        } else if (qName.equalsIgnoreCase("Amt")) {
//            account.setAmt(Integer.parseInt(value));
//        }
//    }
//
//    public void readList() {
//        for (Account anAccList : accountList) {
//            System.out.println(anAccList.toString());
//        }
//    }
//}
