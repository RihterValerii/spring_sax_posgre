package jobs.service;


import jobs.model.Job;
import org.springframework.stereotype.Service;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SaxService {
    private static ArrayList<Job> jobs = new ArrayList<>();
    private static long date;

    public List<Job> parseRequest(String request) {

        date = new Date().getTime();

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser;
        XMLHandler handler = new XMLHandler("jobs");
        try {
            parser = factory.newSAXParser();
            parser.parse(new InputSource(new ByteArrayInputStream(request.getBytes("utf-8"))), handler);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return XMLHandler.getList();
    }

    private static class XMLHandler extends DefaultHandler {
        private String type, user, amount, device, lastElementName;

        static List<Integer> listId = new ArrayList<>();
        private String element;
        private boolean isEntered;

        XMLHandler(String element) {
            this.element = element;
        }

        static List<Job> getList() {
            int i = 0;
            for (Job job : jobs) {
                job.setId(XMLHandler.listId.get(i));
                job.setLongDate(date);
                i++;
            }
            return jobs;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            lastElementName = qName;
            if (isEntered) {
                int length = attributes.getLength();
                for (int i = 0; i < length; i++) {
                    listId.add(Integer.parseInt(attributes.getValue(i)));
                }
            }

            if (qName.equals(element)) {
                isEntered = true;
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (qName.equals(element))
                isEntered = false;

            if ((type != null && !type.isEmpty()) && (user != null && !user.isEmpty()) && (device != null && !device.isEmpty()) && (amount != null && !amount.isEmpty())) {

                jobs.add(new Job(type, user, device, Integer.parseInt(amount)));
                type = null;
                user = null;
                device = null;
                amount = null;
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            String information = new String(ch, start, length);

            information = information.replace("\n", "").trim();

            if (!information.isEmpty()) {
                if (lastElementName.equals("type"))
                    type = information;
                if (lastElementName.equals("user"))
                    user = information;
                if (lastElementName.equals("device"))
                    device = information;
                if (lastElementName.equals("amount"))
                    amount = information;
            }
        }
    }
}
