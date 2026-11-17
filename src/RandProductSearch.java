import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;

public class RandProductSearch extends JFrame
{
    final int ID_LENGTH = 6;
    final int NAME_LENGTH = 35;
    final int DESCRIPTION_LENGTH = 75;
    final long TOTAL_RECORD_LENGTH = 124;

    JPanel mainPnl, headerPnl, bodyPnl, searchPnl, outputPnl, buttonPnl;
    JLabel titleLbl, searchLbl, outputLbl;
    JTextField searchTF;
    JTextArea outputTA;
    JScrollPane outputSP;
    JButton searchBtn, quitBtn;

    public RandProductSearch()
    {
        mainPnl = new JPanel();
        mainPnl.setLayout(new BorderLayout());

        buildHeaderPanel();
        mainPnl.add(headerPnl, BorderLayout.NORTH);

        bodyPnl = new JPanel();
        bodyPnl.setLayout(new BorderLayout());
        buildSearchPanel();
        bodyPnl.add(searchPnl, BorderLayout.NORTH);
        buildOutputPanel();
        bodyPnl.add(outputPnl, BorderLayout.CENTER);
        buildButtonPanel();
        bodyPnl.add(buttonPnl, BorderLayout.SOUTH);
        mainPnl.add(bodyPnl, BorderLayout.CENTER);

        add(mainPnl);
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        setSize((screenSize.width / 4) * 3, (screenSize.height / 4) * 3);
        setLocation(screenSize.width / 8, screenSize.height / 8);
        setTitle("Random Product Search");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void buildHeaderPanel()
    {
        headerPnl = new JPanel();
        titleLbl = new JLabel("Random Product Search");
        titleLbl.setFont(new Font("Times New Roman", Font.PLAIN, 72));
        headerPnl.add(titleLbl);
    }

    private void buildSearchPanel()
    {
        searchPnl = new JPanel();
        searchPnl.setLayout(new GridLayout(1, 2));

        searchLbl = new JLabel("Search products:");
        searchLbl.setFont(new Font("Times New Roman", Font.ITALIC, 36));
        searchPnl.add(searchLbl);

        searchTF = new JTextField();
        searchTF.setFont(new Font("Times New Roman", Font.PLAIN, 24));
        searchPnl.add(searchTF);
    }

    private void buildOutputPanel()
    {
        outputPnl = new JPanel();
        outputPnl.setLayout(new GridLayout(2, 1));

        outputLbl = new JLabel("Found Products:");
        outputLbl.setFont(new Font("Times New Roman", Font.PLAIN, 36));
        outputPnl.add(outputLbl);

        outputTA = new JTextArea(30, 20);
        outputTA.setFont(new Font("Times New Roman", Font.PLAIN, 24));
        outputTA.setEditable(false);
        outputSP = new JScrollPane(outputTA);
        outputPnl.add(outputSP);
    }

    private void buildButtonPanel()
    {
        Font buttonFont = new Font("Times New Roman", Font.PLAIN, 36);

        buttonPnl = new JPanel();
        buttonPnl.setLayout(new GridLayout(1, 2));

        searchBtn = new JButton("Search");
        searchBtn.setFont(buttonFont);
        searchBtn.addActionListener((ActionEvent ae) ->
        {
            searchProducts();
        });
        buttonPnl.add(searchBtn);

        quitBtn = new JButton("Quit");
        quitBtn.setFont(buttonFont);
        quitBtn.addActionListener((ActionEvent ae) ->
        {
            if (JOptionPane.showConfirmDialog(mainPnl, "Are you sure you want to quit?", "Quit?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
            {
                System.exit(0);
            }
        });
        buttonPnl.add(quitBtn);
    }

    private void searchProducts()
    {
        String search = searchTF.getText();
        if (search.isEmpty())
        {
            JOptionPane.showMessageDialog(mainPnl, "Enter something to search for.", "Empty Search Error", JOptionPane.ERROR_MESSAGE);
        }
        else
        {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
            {
                File selectedFile = chooser.getSelectedFile();
                outputTA.setText("");

                try (RandomAccessFile raf = new RandomAccessFile(selectedFile, "r"))
                {
                    long numRecords = raf.length() / TOTAL_RECORD_LENGTH;

                    for (int i = 0; i < numRecords; i++)
                    {
                        raf.seek(i * TOTAL_RECORD_LENGTH);

                        byte[] idBytes = new byte[ID_LENGTH];
                        raf.read(idBytes);
                        String id = new String(idBytes, StandardCharsets.UTF_8).trim();

                        byte[] nameBytes = new byte[NAME_LENGTH];
                        raf.read(nameBytes);
                        String name = new String(nameBytes, StandardCharsets.UTF_8).trim();

                        byte[] descriptionBytes = new byte[DESCRIPTION_LENGTH];
                        raf.read(descriptionBytes);
                        String description = new String(descriptionBytes, StandardCharsets.UTF_8).trim();

                        double cost = raf.readDouble();

                        if (name.contains(search))
                        {
                            outputTA.append(String.format("ID: %s\nName: %s\nDescription: %s\nCost: $%.2f\n\n", id, name, description, cost));
                        }
                    }
                }
                catch (IOException e)
                {
                    JOptionPane.showMessageDialog(mainPnl, ("Error loading file! " + e.getMessage()), "ERROR: IOException", JOptionPane.ERROR_MESSAGE);
                }
            }
            else
            {
                JOptionPane.showMessageDialog(mainPnl, "No file selected!", "ERROR: No File To Read!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
