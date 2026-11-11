import javax.swing.*;
import java.awt.*;

public class RandProductSearch extends JFrame
{
    JPanel mainPnl, headerPnl, dataEntryPnl, productCountPnl, displayDataPnl, buttonPnl;
    JLabel titleLbl, countLbl, displayLbl;
    JTextField idTF, nameTF, descriptionTF, costTF, productCountTF;
    JTextArea displayProductsTA;
    JScrollPane displayProductsSP;
    JButton addProductBtn, addAllProductsBtn, quitBtn;

    int numProducts;

    public RandProductSearch()
    {
        numProducts = 0;

        mainPnl = new JPanel();
        mainPnl.setLayout(new GridLayout(5, 1));

        buildHeaderPanel();
        mainPnl.add(headerPnl);
        buildDataEntryPanel();
        mainPnl.add(dataEntryPnl);
        buildProductCountPanel();
        mainPnl.add(productCountPnl);
        buildDisplayDataPanel();
        mainPnl.add(displayDataPnl);
        buildButtonPanel();
        mainPnl.add(buttonPnl);

        // Finalize main panel
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
    }

    private void buildDataEntryPanel()
    {
        dataEntryPnl = new JPanel();
    }

    private void buildProductCountPanel()
    {
        productCountPnl = new JPanel();
    }

    private void buildDisplayDataPanel()
    {
        displayDataPnl = new JPanel();
    }

    private void buildButtonPanel()
    {
        buttonPnl = new JPanel();
    }
}
