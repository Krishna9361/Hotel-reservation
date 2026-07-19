# Hotel Reservation System

A console-based Java OOP project for searching, booking, cancelling, and viewing hotel room reservations.

## Features

- Room categories: Standard, Deluxe, Suite
- Search available rooms by date range and category
- Make reservations with simulated payment
- Cancel reservations and free room availability
- View booking details
- File I/O persistence for rooms and reservations

## Run

```powershell
javac -d out src\hotel\*.java
java -cp out hotel.Main
```

The app stores data in the `data` folder. If `data/rooms.csv` does not exist, sample rooms are created automatically.
