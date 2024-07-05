public class Writer extends Thread {
    private final Database db;
    private final String flightCode;
    private final String day;
    private final String seatNumber;
    private final boolean reserve;

    public Writer(Database db, String flightCode, String day, String seatNumber, boolean reserve) {
        this.db = db;
        this.flightCode = flightCode;
        this.day = day;
        this.seatNumber = seatNumber;
        this.reserve = reserve;
    }

    @Override
    public void run() {
        if (reserve) {
            db.makeReservation(flightCode, day, seatNumber);
        } else {
            db.cancelReservation(flightCode, day, seatNumber);
        }
    }
}
