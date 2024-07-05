public class Reader extends Thread {
    private final Database db;
    private final String flightCode;

    public Reader(Database db, String flightCode) {
        this.db = db;
        this.flightCode = flightCode;
    }

    @Override
    public void run() {
        db.queryReservation(flightCode);
    }
}
