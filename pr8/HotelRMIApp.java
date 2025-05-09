import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;
import java.util.*;

// Remote Interface
interface HotelServiceInterface extends Remote {
    boolean bookRoom(String guestName, int roomNumber) throws RemoteException;
    boolean cancelBooking(String guestName) throws RemoteException;
    Map<Integer, String> showBookedRoomDetails() throws RemoteException;
}

// Server Implementation
class HotelServer extends UnicastRemoteObject implements HotelServiceInterface {
    private Map<Integer, String> bookedRooms;

    public HotelServer() throws RemoteException {
        bookedRooms = new HashMap<>();
    }

    @Override
    public synchronized boolean bookRoom(String guestName, int roomNumber) throws RemoteException {
        if (!bookedRooms.containsKey(roomNumber)) {
            bookedRooms.put(roomNumber, guestName);
            System.out.println("Room " + roomNumber + " booked for guest: " + guestName);
            return true;
        } else {
            System.out.println("Room " + roomNumber + " is already booked.");
            return false;
        }
    }

    @Override
    public synchronized boolean cancelBooking(String guestName) throws RemoteException {
        for (Map.Entry<Integer, String> entry : bookedRooms.entrySet()) {
            if (entry.getValue().equals(guestName)) {
                bookedRooms.remove(entry.getKey());
                System.out.println("Booking for guest " + guestName + " canceled.");
                return true;
            }
        }
        System.out.println("No booking found for guest " + guestName);
        return false;
    }

    @Override
    public synchronized Map<Integer, String> showBookedRoomDetails() throws RemoteException {
        return new HashMap<>(bookedRooms);
    }

    // Start the server
    public static void startServer() {
        try {
            HotelServer server = new HotelServer();
            LocateRegistry.createRegistry(1099);
            Naming.rebind("HotelService", server);
            System.out.println("Hotel Server is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

// Client Implementation
class HotelClient {
    public static void startClient() {
        try {
            HotelServiceInterface hotelService = (HotelServiceInterface) Naming.lookup("rmi://localhost/HotelService");
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("1. Book a room");
                System.out.println("2. Cancel booking");
                System.out.println("3. Show booked room details");
                System.out.println("4. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline
                switch (choice) {
                    case 1:
                        System.out.print("Enter guest name: ");
                        String guestName = scanner.nextLine();
                        System.out.print("Enter room number: ");
                        int roomNumber = scanner.nextInt();
                        boolean booked = hotelService.bookRoom(guestName, roomNumber);
                        if (booked) {
                            System.out.println("Room booked successfully!");
                        } else {
                            System.out.println("Room booking failed.");
                        }
                        break;
                    case 2:
                        System.out.print("Enter guest name for cancellation: ");
                        String cancelGuestName = scanner.nextLine();
                        boolean canceled = hotelService.cancelBooking(cancelGuestName);
                        if (canceled) {
                            System.out.println("Booking canceled successfully!");
                        } else {
                            System.out.println("Booking cancellation failed.");
                        }
                        break;
                    case 3:
                        Map<Integer, String> bookedRooms = hotelService.showBookedRoomDetails();
                        System.out.println("Room No.\t|\tGuest Name");
                        System.out.println("-----------------------------------------");
                        for (Map.Entry<Integer, String> entry : bookedRooms.entrySet()) {
                            System.out.println(entry.getKey() + "\t\t|\t" + entry.getValue());
                        }
                        break;
                    case 4:
                        System.out.println("Exiting the client application.");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a valid option.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

// Main class to run server or client based on argument
public class HotelRMIApp {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java HotelRMIApp server|client");
            return;
        }
        if (args[0].equalsIgnoreCase("server")) {
            HotelServer.startServer();
        } else if (args[0].equalsIgnoreCase("client")) {
            HotelClient.startClient();
        } else {
            System.out.println("Invalid argument. Use 'server' or 'client'.");
        }
    }
}
