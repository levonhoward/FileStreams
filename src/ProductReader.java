import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static java.nio.file.StandardOpenOption.CREATE;

public class ProductReader
{
    static void main()
    {
        ArrayList<Product> products = new ArrayList<>();

        JFileChooser chooser = new JFileChooser();
        File selectedFile;

        // Read file
        try
        {
            File workingDirectory = new File(System.getProperty("user.dir"));
            chooser.setCurrentDirectory(workingDirectory);

            if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
            {
                selectedFile = chooser.getSelectedFile();
                Path file = selectedFile.toPath();

                InputStream in = new BufferedInputStream(Files.newInputStream(file, CREATE));
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                while(reader.ready())
                {
                    if (file.toString().endsWith(".csv") || file.toString().endsWith(".txt"))
                    {
                        String CSVString = reader.readLine();
                        String[] productString = CSVString.split(", ");
                        Product product = new Product(productString[0], productString[1], productString[2], Double.parseDouble(productString[3]));
                        products.add(product);
                    } else if (file.toString().endsWith(".json"))
                    {
                        String JSONString = reader.readLine();

                        // Don't read lines that don't have people data in them
                        if (!JSONString.equalsIgnoreCase("{\"products\":[") && !JSONString.equalsIgnoreCase("]}"))
                        {
                            // Isolating each piece of data from the String
                            int JSONIDStartIndex = JSONString.indexOf("\"ID\":") + 6;
                            int JSONIDEndIndex = JSONString.indexOf(", \"name\":") - 1;
                            String JSONID = JSONString.substring(JSONIDStartIndex, JSONIDEndIndex);

                            int JSONNameStartIndex = JSONString.indexOf("\"name\":") + 8;
                            int JSONNameEndIndex = JSONString.indexOf(", \"description\":") - 1;
                            String JSONName = JSONString.substring(JSONNameStartIndex, JSONNameEndIndex);

                            int JSONDescriptionStartIndex = JSONString.indexOf("\"description\":") + 15;
                            int JSONDescriptionEndIndex = JSONString.indexOf(", \"cost\":") - 1;
                            String JSONDescription = JSONString.substring(JSONDescriptionStartIndex, JSONDescriptionEndIndex);

                            int JSONCostStartIndex = JSONString.indexOf("\"cost\":") + 7;
                            int JSONCostEndIndex = JSONString.indexOf("}");
                            double JSONCost = Double.parseDouble(JSONString.substring(JSONCostStartIndex, JSONCostEndIndex));

                            // Create person object from the file and add it to people
                            Product product = new Product(JSONID, JSONName, JSONDescription, JSONCost);
                            products.add(product);
                        }
                    } else
                    {
                        String XMLString = reader.readLine();

                        // Don't read lines that don't have people data in them
                        if (!XMLString.equalsIgnoreCase("<products>") && !XMLString.equalsIgnoreCase("</products>"))
                        {
                            // Isolating each piece of data from the String
                            int XMLIDStartIndex = XMLString.indexOf("<ID>") + 4;
                            int XMLIDEndIndex = XMLString.indexOf("</ID>");
                            String XMLID = XMLString.substring(XMLIDStartIndex, XMLIDEndIndex);

                            int XMLNameStartIndex = XMLString.indexOf("<name>") + 6;
                            int XMLNameEndIndex = XMLString.indexOf("</name>");
                            String XMLName = XMLString.substring(XMLNameStartIndex, XMLNameEndIndex);

                            int XMLDescriptionStartIndex = XMLString.indexOf("<description>") + 13;
                            int XMLDescriptionEndIndex = XMLString.indexOf("</description>");
                            String XMLDescription = XMLString.substring(XMLDescriptionStartIndex, XMLDescriptionEndIndex);

                            int XMLCostStartIndex = XMLString.indexOf("<cost>") + 6;
                            int XMLCostEndIndex = XMLString.indexOf("</cost>");
                            double XMLCost = Double.parseDouble(XMLString.substring(XMLCostStartIndex, XMLCostEndIndex));

                            // Create person object from the file and add it to people
                            Product product = new Product(XMLID, XMLName, XMLDescription, XMLCost);
                            products.add(product);
                        }
                    }
                }
                reader.close();
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("File not found!");
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        // Print file data
        // Header
        System.out.println("ID\t\tName\t\tDescription\t\t\t\t\tCost");
        System.out.println("===========================================================");

        // Data
        for (Product product : products)
        {
            String[] productData = product.toCSV().split(", ");
            String lineData = "%-6s\t%-10s\t%-25s\t$%,.2f";
            System.out.println(String.format(lineData, productData[0], productData[1], productData[2], Double.parseDouble(productData[3])));
        }
    }
}

