package TGM.Warehouse;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;  // Import Jackson ObjectMapper

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class XMLGenerator {

    public static final String[] PRODUCTS = {
            "Kräuterlimonade",
            "Apfelkiste",
            "Smartphone",
            "Jeans",
            "Drahtlose Kopfhörer",
            "Kühlschrank",
            "Digitalkamera",
            "Laufschuhe",
            "Kaffeemaschine",
            "Bequemsessel",
            "Energy Drink",
            "Schokoladenaufstrich",
            "Mountainbike",
            "Sonnenbrille",
            "Schokoladentafel",
            "Elektrorasierer",
            "Armbanduhr",
            "Sportwagen",
            "Holzkohlegrill",
            "Staubsauger"
    };

    private static final String[] DESCRIPTORS_SET1 = {
            "Bio",
            "Nachhaltig",
            "Handgemacht",
            "Ökologisch",
            "Innovativ",
            "Premium",
            "Praktisch",
            "Effizient",
            "Kompakt",
            "Vielseitig"
    };

    private static final String[] DESCRIPTORS_SET2 = {
            "Energiesparend",
            "Langlebig",
            "Multifunktional",
            "Komfortabel",
            "Wasserfest",
            "Robust",
            "Stilvoll",
            "Leistungsstark",
            "Ergonomisch",
            "Preiswert"
    };

    private static final String[] DESCRIPTORS_SET3 = {
            "Bruchsicher",
            "Leicht",
            "Farbenfroh",
            "Geräuscharm",
            "Wiederverwendbar",
            "Tragbar",
            "Benutzerfreundlich",
            "Sicher",
            "Modern",
            "Pflegeleicht"
    };

    private static final String[] UNITS = {
            "Liter",
            "Kilogramm",
            "Stück",
            "Stück",
            "Stück",
            "Stück",
            "Stück",
            "Paar",
            "Stück",
            "Stück",
            "Milliliter",
            "Gramm",
            "Stück",
            "Stück",
            "Gramm",
            "Stück",
            "Stück",
            "Stück",
            "Stück",
            "Stück"
    };

    private static final String[] productCategories = {
            "Elektronik",
            "Kleidung",
            "Haushaltsgeräte",
            "Möbel",
            "Sportausrüstung",
            "Spielwaren",
            "Bücher und Medien",
            "Gartenbedarf",
            "Beauty und Gesundheit",
            "Schreibwaren und Bürobedarf",
            "Automobilzubehör",
            "Lebensmittel und Getränke",
            "Tierbedarf",
            "Bau- und Renovierungsmaterialien",
            "Computer und Zubehör",
            "Musikinstrumente",
            "Kunst und Handwerk",
            "Reiseausrüstung",
            "Schmuck und Uhren",
            "Küchenutensilien"
    };

    private static final  String[] countries = new String[]{"Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla", "Antarctica", "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia", "Bosnia and Herzegowina", "Botswana", "Bouvet Island", "Brazil", "British Indian Ocean Territory", "Brunei Darussalam", "Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde", "Cayman Islands", "Central African Republic", "Chad", "Chile", "China", "Christmas Island", "Cocos (Keeling) Islands", "Colombia", "Comoros", "Congo", "Congo, the Democratic Republic of the", "Cook Islands", "Costa Rica", "Cote d'Ivoire", "Croatia (Hrvatska)", "Cuba", "Cyprus", "Czech Republic", "Denmark", "Djibouti", "Dominica", "Dominican Republic", "East Timor", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia", "Falkland Islands (Malvinas)", "Faroe Islands", "Fiji", "Finland", "France", "France Metropolitan", "French Guiana", "French Polynesia", "French Southern Territories", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Heard and Mc Donald Islands", "Holy See (Vatican City State)", "Honduras", "Hong Kong", "Hungary", "Iceland", "India", "Indonesia", "Iran (Islamic Republic of)", "Iraq", "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Korea, Democratic People's Republic of", "Korea, Republic of", "Kuwait", "Kyrgyzstan", "Lao, People's Democratic Republic", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libyan Arab Jamahiriya", "Liechtenstein", "Lithuania", "Luxembourg", "Macau", "Macedonia, The Former Yugoslav Republic of", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia, Federated States of", "Moldova, Republic of", "Monaco", "Mongolia", "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands", "Netherlands Antilles", "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island", "Northern Mariana Islands", "Norway", "Oman", "Pakistan", "Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn", "Poland", "Portugal", "Puerto Rico", "Qatar", "Reunion", "Romania", "Russian Federation", "Rwanda", "Saint Kitts and Nevis", "Saint Lucia", "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Seychelles", "Sierra Leone", "Singapore", "Slovakia (Slovak Republic)", "Slovenia", "Solomon Islands", "Somalia", "South Africa", "South Georgia and the South Sandwich Islands", "Spain", "Sri Lanka", "St. Helena", "St. Pierre and Miquelon", "Sudan", "Suriname", "Svalbard and Jan Mayen Islands", "Swaziland", "Sweden", "Switzerland", "Syrian Arab Republic", "Taiwan, Province of China", "Tajikistan", "Tanzania, United Republic of", "Thailand", "Togo", "Tokelau", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States", "United States Minor Outlying Islands", "Uruguay", "Uzbekistan", "Vanuatu", "Venezuela", "Vietnam", "Virgin Islands (British)", "Virgin Islands (U.S.)", "Wallis and Futuna Islands", "Western Sahara", "Yemen", "Yugoslavia", "Zambia", "Zimbabwe", "Palestine"};
    private static int stringToNumberInRange(String str, int min, int max) {
        int hashCode = str.hashCode();
        int range = max - min + 1;
        int number = Math.abs(hashCode) % range + min;
        return Math.abs(number);
    }

    @CrossOrigin(origins = "*") // or "*" for all origins
    @GetMapping(value = "/XML/{city}/{name}", produces = MediaType.APPLICATION_XML_VALUE)
    public String getXML(@PathVariable String name, @PathVariable String city) throws ParserConfigurationException {
        return generateXML(name, city);
    }


    @CrossOrigin(origins = "*") // or "*" for all origins
    @GetMapping(value = "/JSON/{city}/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getJSON(@PathVariable String name, @PathVariable String city) throws ParserConfigurationException, IOException {
        return convertXmlToJson(generateXML(name, city));
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/HTML/{city}/{name}", produces = MediaType.TEXT_HTML_VALUE)
    public String getHTML(@PathVariable String name, @PathVariable String city) throws ParserConfigurationException {
        String xmlData = generateXML(name, city);
        return convertXmlToHtml(xmlData);
    }


    private static String convertXmlToHtml(String xmlData) {
        // Start with a basic HTML structure
        StringBuilder htmlData = new StringBuilder();
        htmlData.append("<html><head><title>Warehouse Data</title></head><body>");

        // Add a table for warehouse data
        htmlData.append("<table border='1'><tr><th>Warehouse ID</th><th>Name</th><th>Address</th><th>Postal Code</th><th>City</th><th>Country</th><th>Timestamp</th></tr>");

        // Extracting and adding warehouse information to the table
        // Assuming that xmlData string contains opening and closing tags for each data point
        htmlData.append("<tr>");
        htmlData.append("<td>").append(getDataBetweenTags(xmlData, "warehouseID")).append("</td>");
        htmlData.append("<td>").append(getDataBetweenTags(xmlData, "warehouseName")).append("</td>");
        htmlData.append("<td>").append(getDataBetweenTags(xmlData, "warehouseAddress")).append("</td>");
        htmlData.append("<td>").append(getDataBetweenTags(xmlData, "warehousePostalCode")).append("</td>");
        htmlData.append("<td>").append(getDataBetweenTags(xmlData, "warehouseCity")).append("</td>");
        htmlData.append("<td>").append(getDataBetweenTags(xmlData, "warehouseCountry")).append("</td>");
        htmlData.append("<td>").append(getDataBetweenTags(xmlData, "timestamp")).append("</td>");
        htmlData.append("</tr>");

        // Add a table for product data
        htmlData.append("<table border='1'><tr><th>Product ID</th><th>Name</th><th>Category</th><th>Quantity</th><th>Unit</th></tr>");

        // Extracting and adding product information to the table
        // This part needs to be adapted based on how products are structured in the XML
        // Assuming each product is enclosed in <product>...</product> tags
        String[] products = xmlData.split("<product>");
        for (String product : products) {
            if (!product.trim().isEmpty()) {
                htmlData.append("<tr>");
                htmlData.append("<td>").append(getDataBetweenTags(product, "productID")).append("</td>");
                htmlData.append("<td>").append(getDataBetweenTags(product, "productName")).append("</td>");
                htmlData.append("<td>").append(getDataBetweenTags(product, "productCategory")).append("</td>");
                htmlData.append("<td>").append(getDataBetweenTags(product, "productQuantity")).append("</td>");
                htmlData.append("<td>").append(getDataBetweenTags(product, "productUnit")).append("</td>");
                htmlData.append("</tr>");
            }
        }

        // Close the table and HTML tags
        htmlData.append("</table></body></html>");

        return htmlData.toString();
    }

    private static String getDataBetweenTags(String xml, String tagName) {
        Pattern pattern = Pattern.compile("<" + tagName + ">(.+?)</" + tagName + ">");
        Matcher matcher = pattern.matcher(xml);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "N/A"; // Return "N/A" if no data is found for the tag
    }



    public static String convertXmlToJson(String xmlData) throws IOException {
        // Create an XmlMapper object to read XML data
        XmlMapper xmlMapper = new XmlMapper();

        // Read the XML into a tree structure (like a Map or JsonNode)
        Object tree = xmlMapper.readValue(xmlData, Object.class);

        // Create an ObjectMapper object to write JSON data
        ObjectMapper jsonMapper = new ObjectMapper();

        // Convert the tree structure to a JSON string
        return jsonMapper.writeValueAsString(tree);
    }

    public static String generateXML(String name,String city) throws ParserConfigurationException {
        int nameNumber = stringToNumberInRange(name, 0, 100);
        int cityNumber = stringToNumberInRange(city, 0, 100);

        // Calculate the number of products
        int numberOfProducts = (nameNumber + cityNumber) / 2 + 1;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        Element root = doc.createElement("warehouseData");


        Element id = doc.createElement("warehouseID");
        Text idData = doc.createTextNode(String.valueOf(name.hashCode()));
        id.appendChild(idData);
        root.appendChild(id);

        Element houseName = doc.createElement("warehouseName");
        Text houseNameData = doc.createTextNode(city + " " +  name);
        houseName.appendChild(houseNameData);
        root.appendChild(houseName);

        Element address = doc.createElement("warehouseAddress");
        Text addressData = doc.createTextNode("Teststraße " + nameNumber);
        address.appendChild(addressData);
        root.appendChild(address);

        Element postalCode = doc.createElement("warehousePostalCode");
        Text postalCodeData = doc.createTextNode(String.valueOf(nameNumber + cityNumber));  // Assuming the postal code is 'Linz'
        postalCode.appendChild(postalCodeData);
        root.appendChild(postalCode);

        Element cityElement = doc.createElement("warehouseCity");
        Text cityData = doc.createTextNode(city);
        cityElement.appendChild(cityData);
        root.appendChild(cityElement);

        Element country = doc.createElement("warehouseCountry");
        Text countryData = doc.createTextNode(countries[countries.length % (nameNumber + cityNumber)]);
        country.appendChild(countryData);
        root.appendChild(country);

        Element time = doc.createElement("timestamp");
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        String formattedTimestamp = now.format(formatter);
        Text timeData = doc.createTextNode(formattedTimestamp);
        time.appendChild(timeData);
        root.appendChild(time);

        Element productData = doc.createElement("productData");

        for (int i = 0; i < numberOfProducts; i++) {
            Element product = doc.createElement("product");

            Element productID = doc.createElement("productID");
            productID.appendChild(doc.createTextNode("00-" + String.format("%06d", i)));
            product.appendChild(productID);

            Element productName = doc.createElement("productName");
            productName.appendChild(doc.createTextNode(DESCRIPTORS_SET1[Math.abs(nameNumber % DESCRIPTORS_SET1.length)] + " "
                    + DESCRIPTORS_SET2[Math.abs((cityNumber * i) % DESCRIPTORS_SET2.length)] + " "
                    + DESCRIPTORS_SET3[Math.abs((nameNumber * i) % DESCRIPTORS_SET3.length)] + " "
                    + PRODUCTS[Math.abs((cityNumber-i) % PRODUCTS.length)]));
            product.appendChild(productName);

            Element productCategory = doc.createElement("productCategory");
            productCategory.appendChild(doc.createTextNode( productCategories[((cityNumber*i) % productCategories.length)]));
            product.appendChild(productCategory);

            Element productQuantity = doc.createElement("productQuantity");
            productQuantity.appendChild(doc.createTextNode(String.valueOf((nameNumber +2*i) + (cityNumber+4*i))));
            product.appendChild(productQuantity);

            Element productUnit = doc.createElement("productUnit");
            productUnit.appendChild(doc.createTextNode(UNITS[(nameNumber * i) % UNITS.length]));
            product.appendChild(productUnit);

            productData.appendChild(product);
        }

        root.appendChild(productData);

        doc.appendChild(root);

        return convertDocumentToString(doc); // Convert the Document to a String
    }

    private static String convertDocumentToString(Document doc) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);

            transformer.transform(domSource, result);

            return writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
