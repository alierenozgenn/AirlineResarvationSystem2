import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Database {
    private final Map<String, Map<String, Map<String, Seat>>> flights = new HashMap<>();
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private ReservationSystemGUI gui;

    public void setGUI(ReservationSystemGUI gui) {
        this.gui = gui;
    }

    public Database() {
        // Uçuşları ve koltukları başlat
        addFlight("IST-ANK", "Monday", "1A");
        addFlight("IST-ANK", "Monday", "1B");
        addFlight("NYC-LAX", "Tuesday", "2A");
        addFlight("NYC-LAX", "Tuesday", "2B");
        // Diğer uçuşlar...
    }

    private void addFlight(String flightCode, String day, String seatNumber) {
        flights.computeIfAbsent(flightCode, k -> new HashMap<>())
               .computeIfAbsent(day, k -> new HashMap<>())
               .put(seatNumber, new Seat(seatNumber));
    }

    public void queryReservation(String flightCode) {
        rwLock.readLock().lock();
        try {
            StringBuilder result = new StringBuilder();
            if (flights.containsKey(flightCode)) {
                result.append("Uçuş Kodu: ").append(flightCode).append("\n");
                for (Map.Entry<String, Map<String, Seat>> dayEntry : flights.get(flightCode).entrySet()) {
                    String day = dayEntry.getKey();
                    result.append("  Gün: ").append(day).append("\n");
                    for (Seat seat : dayEntry.getValue().values()) {
                        result.append("    Koltuk: ").append(seat.getSeatNumber())
                              .append(" - ").append(seat.isReserved() ? "Rezerve" : "Boş").append("\n");
                    }
                }
            } else {
                result.append("Uçuş bulunamadı: ").append(flightCode);
            }
            if (gui != null) {
                gui.updateDisplay(result.toString());
            } else {
                System.out.println(result.toString());
            }
        } finally {
            rwLock.readLock().unlock();
        }
    }

    public void makeReservation(String flightCode, String day, String seatNumber) {
        rwLock.writeLock().lock();
        try {
            if (flights.containsKey(flightCode) && flights.get(flightCode).containsKey(day)) {
                Seat seat = flights.get(flightCode).get(day).get(seatNumber);
                if (seat != null && !seat.isReserved()) {
                    seat.reserve();
                    if (gui != null) {
                        gui.updateDisplay("Rezervasyon yapıldı: " + flightCode + " " + day + " " + seatNumber);
                    } else {
                        System.out.println("Rezervasyon yapıldı: " + flightCode + " " + day + " " + seatNumber);
                    }
                } else {
                    if (gui != null) {
                        gui.updateDisplay("Koltuk zaten rezerve: " + flightCode + " " + day + " " + seatNumber);
                    } else {
                        System.out.println("Koltuk zaten rezerve: " + flightCode + " " + day + " " + seatNumber);
                    }
                }
            } else {
                if (gui != null) {
                    gui.updateDisplay("Uçuş bulunamadı veya geçersiz gün: " + flightCode + " " + day);
                } else {
                    System.out.println("Uçuş bulunamadı veya geçersiz gün: " + flightCode + " " + day);
                }
            }
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    public void cancelReservation(String flightCode, String day, String seatNumber) {
        rwLock.writeLock().lock();
        try {
            if (flights.containsKey(flightCode) && flights.get(flightCode).containsKey(day)) {
                Seat seat = flights.get(flightCode).get(day).get(seatNumber);
                if (seat != null && seat.isReserved()) {
                    seat.cancel();
                    if (gui != null) {
                        gui.updateDisplay("Rezervasyon iptal edildi: " + flightCode + " " + day + " " + seatNumber);
                    } else {
                        System.out.println("Rezervasyon iptal edildi: " + flightCode + " " + day + " " + seatNumber);
                    }
                } else {
                    if (gui != null) {
                        gui.updateDisplay("Koltuk rezerve değil: " + flightCode + " " + day + " " + seatNumber);
                    } else {
                        System.out.println("Koltuk rezerve değil: " + flightCode + " " + day + " " + seatNumber);
                    }
                }
            } else {
                if (gui != null) {
                    gui.updateDisplay("Uçuş bulunamadı veya geçersiz gün: " + flightCode + " " + day);
                } else {
                    System.out.println("Uçuş bulunamadı veya geçersiz gün: " + flightCode + " " + day);
                }
            }
        } finally {
            rwLock.writeLock().unlock();
        }
    }
}
