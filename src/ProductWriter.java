import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import static java.nio.file.StandardOpenOption.CREATE;

public class ProductWriter
{
    static void main()
    {
        ArrayList<Product> products = new ArrayList<>();
        Scanner in = new Scanner(System.in);

        String ID = "";
        String name = "";
        String description = "";
        double cost = -1.0;

        boolean isDone = false;

        String fileName = "";
        boolean isValidDataFormat = false;
        String dataFormat = "";

        // Get input
        do
        {
            ID = SafeInput.getRegExString(in, "Enter the product's six-digit ID", "^\\d{6}$");
            name = SafeInput.getNonZeroLenString(in, "Enter the product's name");
            description = SafeInput.getNonZeroLenString(in, "Enter the product's description");
            cost = SafeInput.getDouble(in, "Enter the product's cost");

            Product nextProduct = new Product(ID, name, description, cost);
            products.add(nextProduct);

            isDone = !SafeInput.getYNConfirm(in, "Would you like to enter another product?");
        } while (!isDone);

        // Write to file
        fileName = SafeInput.getNonZeroLenString(in, "Enter the name of the file to save data to");

        // Have user choose file type
        do {
            String usersDataFormat = SafeInput.getNonZeroLenString(in, "Select either 'CSV', 'JSON', or 'XML' file type for your file");
            if (usersDataFormat.equalsIgnoreCase("csv") || usersDataFormat.equalsIgnoreCase("json") || usersDataFormat.equalsIgnoreCase("xml"))
            {
                isValidDataFormat = true;
                dataFormat = usersDataFormat.toLowerCase();
            }
            else
            {
                System.out.println("That is not a valid data format! Choose 'csv', 'json', or 'xml'!");
            }
        } while (!isValidDataFormat);

        // Write data
        File workingDirectory = new File(System.getProperty("user.dir"));
        Path file = Paths.get(workingDirectory.getPath() + "\\src\\" + fileName + "." + dataFormat);

        try
        {
            OutputStream out = new BufferedOutputStream(Files.newOutputStream(file, CREATE));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));

            if (dataFormat.equalsIgnoreCase("csv"))
            {
                for (Product nextProduct : products)
                {
                    writer.write(nextProduct.toCSV(), 0, nextProduct.toCSV().length());
                    writer.newLine();
                }
            } else if (dataFormat.equalsIgnoreCase("json"))
            {
                writer.write("{\"products\":[", 0, 13);
                writer.newLine();
                for (int i = 0; i < products.size(); i++)
                {
                    writer.write(products.get(i).toJSON(), 0, products.get(i).toJSON().length());

                    if ((products.size() -1) != i)
                    {
                        writer.write(",", 0, 1);
                    }

                    writer.newLine();
                }
                writer.write("]}", 0, 2);
            } else
            {
                writer.write("<products>", 0, 10);
                writer.newLine();
                for (Product nextProduct : products)
                {
                    writer.write(nextProduct.toXML(), 0, nextProduct.toXML().length());
                    writer.newLine();
                }
                writer.write("</products>", 0, 11);
                writer.newLine();
            }

            writer.close();
            System.out.println("Data saved to " + fileName + "." + dataFormat);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
