public class Main {
    public static void main(String[] args) {
        Database db = new Database();
        ReservationSystemGUI gui = new ReservationSystemGUI(db);

        // Örnek olarak bazı işlemler yapılıyor
        Reader reader = new Reader(db, "IST-ANK");
        reader.start();

        Writer writer = new Writer(db, "IST-ANK", "Monday", "1A", true);
        writer.start();

        // GUI'yi başlatmak için setVisible çağrısı
        gui.setVisible(true);
    }
}
