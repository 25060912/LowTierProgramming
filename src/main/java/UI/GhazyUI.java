package UI;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import logic.ghazy;

public class GhazyUI extends JFrame {
    
    private JTextArea journalArea;
    private JButton analyzeButton;
    private JLabel resultLabel;

    private ghazy analyzer;

    public GhazyUI() {

        analyzer = new ghazy();

        setTitle("Ghazy Sentiment Analyzer");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        journalArea = new JTextArea(5, 30);
        JScrollPane scrollPane = new JScrollPane(journalArea);

        analyzeButton = new JButton("Analyze");
        resultLabel = new JLabel("Result will appear here");

        JPanel panel = new JPanel();
        panel.add(scrollPane);
        panel.add(analyzeButton);
        panel.add(resultLabel);

        add(panel);

        analyzeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                analyzeJournal();
            }
        });
    }

    private void analyzeJournal() {
        String journal = journalArea.getText();
        String result = analyzer.analyze(journal);
        resultLabel.setText(result);
    }

    public static void main(String[] args) {
        new GhazyUI().setVisible(true);
    }
}

