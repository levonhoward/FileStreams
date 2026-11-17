import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

public class RandProductMaker extends JFrame
{
    final int ID_LENGTH = 6;
    final int NAME_LENGTH = 35;
    final int DESCRIPTION_LENGTH = 75;
    final long TOTAL_RECORD_LENGTH = 124;

    JPanel mainPnl, headerPnl, dataEntryPnl, productCountPnl, buttonPnl;
    JLabel titleLbl, numProductsLbl, idLbl, nameLbl, descriptionLbl, costLbl;
    JTextField numProductsTF, idTF, nameTF, descriptionTF, costTF;
    JButton addProductBtn, quitBtn;

    int numProducts;

    public RandProductMaker()
    {
        numProducts = 0;

        mainPnl = new JPanel();
        mainPnl.setLayout(new GridLayout(4, 1));

        buildHeaderPanel();
        mainPnl.add(headerPnl);
        buildProductCountPanel();
        mainPnl.add(productCountPnl);
        buildDataEntryPanel();
        mainPnl.add(dataEntryPnl);
        buildButtonPanel();
        mainPnl.add(buttonPnl);

        // Finalize main panel
        add(mainPnl);
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        setSize((screenSize.width / 4) * 3, (screenSize.height / 4) * 3);
        setLocation(screenSize.width / 8, screenSize.height / 8);
        setTitle("Random Product Maker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void buildHeaderPanel()
    {
        headerPnl = new JPanel();
        titleLbl = new JLabel("Random Product Maker");
        titleLbl.setFont(new Font("Times New Roman", Font.PLAIN, 72));
        headerPnl.add(titleLbl);
    }

    private void buildProductCountPanel()
    {
        productCountPnl = new JPanel();
        productCountPnl.setLayout(new GridLayout(1, 2));

        Font productCountFont = new Font("Times New Roman", Font.BOLD, 36);

        numProductsLbl = new JLabel("Products Entered:");
        numProductsLbl.setFont(productCountFont);
        productCountPnl.add(numProductsLbl);

        numProductsTF = new JTextField();
        numProductsTF.setEditable(false);
        numProductsTF.setFont(productCountFont);
        numProductsTF.setText("" + numProducts);
        productCountPnl.add(numProductsTF);
    }

    private void buildDataEntryPanel()
    {
        dataEntryPnl = new JPanel();
        dataEntryPnl.setLayout(new GridLayout(2, 5));

        Font dataEntryLabelFont = new Font("Times New Roman", Font.PLAIN, 36);
        Font dataEntryTextFieldFont = new Font("Times New Roman", Font.PLAIN, 24);

        // Labels //
        idLbl = new JLabel("Product ID:");
        idLbl.setFont(dataEntryLabelFont);
        dataEntryPnl.add(idLbl);

        nameLbl = new JLabel("Product Name:");
        nameLbl.setFont(dataEntryLabelFont);
        dataEntryPnl.add(nameLbl);

        descriptionLbl = new JLabel("Product Description:");
        descriptionLbl.setFont(dataEntryLabelFont);
        dataEntryPnl.add(descriptionLbl);

        costLbl = new JLabel("Product Cost:");
        costLbl.setFont(dataEntryLabelFont);
        dataEntryPnl.add(costLbl);

        // Text Fields //
        idTF = new JTextField();
        idTF.setFont(dataEntryTextFieldFont);
        dataEntryPnl.add(idTF);

        nameTF = new JTextField();
        nameTF.setFont(dataEntryTextFieldFont);
        dataEntryPnl.add(nameTF);

        descriptionTF = new JTextField();
        descriptionTF.setFont(dataEntryTextFieldFont);
        dataEntryPnl.add(descriptionTF);

        costTF = new JTextField();
        costTF.setFont(dataEntryTextFieldFont);
        dataEntryPnl.add(costTF);
    }

    private void buildButtonPanel()
    {
        buttonPnl = new JPanel();
        buttonPnl.setLayout(new GridLayout(1, 2));

        Font buttonsFont = new Font("Times New Roman", Font.PLAIN, 36);

        addProductBtn = new JButton("Add Product");
        addProductBtn.setFont(buttonsFont);
        addProductBtn.addActionListener((ActionEvent ae) ->
        {
            int addProductChoice = JOptionPane.showConfirmDialog(mainPnl, "Do you want to add this product?", "Add product?", JOptionPane.YES_NO_OPTION);
            if (addProductChoice == JOptionPane.YES_OPTION && textFieldsAreValid())
                {
                    writeProduct();
                }
        });
        buttonPnl.add(addProductBtn);

        quitBtn = new JButton("Quit");
        quitBtn.setFont(buttonsFont);
        quitBtn.addActionListener((ActionEvent ae) ->
        {
            if (JOptionPane.showConfirmDialog(mainPnl, "Are you sure you want to quit?", "Quit?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
            {
                System.exit(0);
            }
        });
        buttonPnl.add(quitBtn);
    }

    private boolean textFieldsAreValid()
    {
        boolean textFieldsAreValid = true;

        String id = idTF.getText();
        String name = nameTF.getText();
        String description = descriptionTF.getText();
        String cost = costTF.getText();

        if (id.isEmpty() || !id.matches("\\d{" + ID_LENGTH + "}"))
        {
            textFieldsAreValid = false;
            JOptionPane.showMessageDialog(mainPnl, "You must enter a 6-digit ID.", "ERROR: Invalid ID", JOptionPane.ERROR_MESSAGE);
        }
        else if (name.isEmpty() || name.length() > NAME_LENGTH)
        {
            textFieldsAreValid = false;
            JOptionPane.showMessageDialog(mainPnl, "Product name must be between 1 and 35 characters long (inclusive).", "ERROR: Invalid Name", JOptionPane.ERROR_MESSAGE);
        }
        else if (description.isEmpty() || description.length() > DESCRIPTION_LENGTH)
        {
            textFieldsAreValid = false;
            JOptionPane.showMessageDialog(mainPnl, "Product description must be between 1 and 75 characters long (inclusive).", "ERROR: Invalid Description", JOptionPane.ERROR_MESSAGE);
        }
        else if (cost.isEmpty() || !cost.matches("(0|[1-9]\\d*)\\.\\d{2}"))
        {
            textFieldsAreValid = false;
            JOptionPane.showMessageDialog(mainPnl, "Product cost must have some number of digits, followed by a decimal, followed by two more digits. Leading zeros, commas, and dollar signs are not allowed.", "ERROR: Invalid Cost", JOptionPane.ERROR_MESSAGE);
        }

        return textFieldsAreValid;
    }

    private void writeProduct()
    {
        String id = idTF.getText();
        String name = nameTF.getText();
        String description = descriptionTF.getText();
        double cost = Double.parseDouble(costTF.getText());

        File workingDirectory = new File(System.getProperty("user.dir"));
        Path file = Paths.get(workingDirectory.getPath() + "\\src\\randProductFile.bin");

        try (RandomAccessFile raf = new RandomAccessFile(file.toFile(), "rw"))
        {
            raf.seek(TOTAL_RECORD_LENGTH * numProducts);

            raf.write(String.format("%6s", id).getBytes(StandardCharsets.UTF_8));
            raf.write(String.format("%35s", name).getBytes(StandardCharsets.UTF_8));
            raf.write(String.format("%75s", description).getBytes(StandardCharsets.UTF_8));
            raf.writeDouble(cost);

            numProducts++;
            JOptionPane.showMessageDialog(mainPnl, ("Added " + name + "!"), "Product Added", JOptionPane.INFORMATION_MESSAGE);
            idTF.setText("");
            nameTF.setText("");
            descriptionTF.setText("");
            costTF.setText("");
            numProductsTF.setText("" + numProducts);
        }
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(mainPnl, ("Error writing file! " + e.getMessage()), "ERROR: IOException", JOptionPane.ERROR_MESSAGE);
        }
    }
}
