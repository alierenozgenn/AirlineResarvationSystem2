import java.awt.*;
import javax.swing.*;

public class ReservationSystemGUI extends JFrame {
    private final Database db;
    private JTextArea displayArea;
    private JTextField flightField;
    private JTextField dayField;
    private JTextField seatField;
    private JButton queryButton;
    private JButton reserveButton;
    private JButton cancelButton;

    public ReservationSystemGUI(Database db) {
        this.db = db;
        initializeUI();
        db.setGUI(this);
    }

    private void initializeUI() {
        setTitle("Havayolu Rezervasyon Sistemi");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Logo Ekleme
        JLabel logo = new JLabel(new ImageIcon("airline_logo.png"));
        logo.setHorizontalAlignment(JLabel.CENTER);
        add(logo, BorderLayout.NORTH);

        // Merkez Panel
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Serif", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(displayArea);
        add(scrollPane, BorderLayout.CENTER);

        // Kontrol Paneli
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(5, 2, 10, 10));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        controlPanel.add(new JLabel("Uçuş Kodu:"));
        flightField = new JTextField();
        controlPanel.add(flightField);

        controlPanel.add(new JLabel("Gün:"));
        dayField = new JTextField();
        controlPanel.add(dayField);

        controlPanel.add(new JLabel("Koltuk Numarası:"));
        seatField = new JTextField();
        controlPanel.add(seatField);

        queryButton = new JButton("Rezervasyonları Sorgula");
        reserveButton = new JButton("Rezervasyon Yap");
        cancelButton = new JButton("Rezervasyonu İptal Et");

        controlPanel.add(queryButton);
        controlPanel.add(reserveButton);
        controlPanel.add(cancelButton);

        add(controlPanel, BorderLayout.SOUTH);

        // Lambda ifadeleri ile buton işleyicileri
        queryButton.addActionListener(e -> {
            Reader reader = new Reader(db, flightField.getText());
            reader.start();
        });

        reserveButton.addActionListener(e -> {
            Writer writer = new Writer(db, flightField.getText(), dayField.getText(), seatField.getText(), true);
            writer.start();
        });

        cancelButton.addActionListener(e -> {
            Writer writer = new Writer(db, flightField.getText(), dayField.getText(), seatField.getText(), false);
            writer.start();
        });

        setVisible(true);
    }

    public void updateDisplay(String message) {
        SwingUtilities.invokeLater(() -> displayArea.append(message + "\n"));
    }

    public static void main(String[] args) {
        Database db = new Database();
        new ReservationSystemGUI(db);
    }
}
