package ua.lpnu.kzp.setup;

import java.util.Locale;
import java.util.Objects;

/** Спільний абстрактний тип запису про проживання в готелі. */
public abstract class Booking {
    private final int room;
    private final String guest;
    private final int nights;
    private final double nightlyRate;
    private final RoomKind kind;

    /**
     * Створює запис після перевірки спільних полів.
     *
     * @param room номер кімнати
     * @param guest ім'я гостя
     * @param nights кількість ночей
     * @param nightlyRate тариф за ніч
     * @param kind категорія номера
     */
    protected Booking(int room, String guest, int nights, double nightlyRate, RoomKind kind) {
        Objects.requireNonNull(guest, "Ім'я гостя не може бути null");
        Objects.requireNonNull(kind, "Категорія не може бути null");
        if (guest.isBlank()) {
            throw new IllegalArgumentException("Ім'я гостя не може бути порожнім");
        }
        if (room < 0 || nights < 0) {
            throw new IllegalArgumentException("Номер кімнати й кількість ночей не можуть бути від'ємними");
        }
        if (nightlyRate < 0 || !Double.isFinite(nightlyRate)) {
            throw new IllegalArgumentException("Тариф має бути скінченним і не від'ємним");
        }
        this.room = room;
        this.guest = guest;
        this.nights = nights;
        this.nightlyRate = nightlyRate;
        this.kind = kind;
    }

    /**
     * Створює запис потрібного підтипу з рядка CSV у форматі кімната;гість;ночі;тариф;категорія.
     *
     * @param line рядок CSV
     * @return стандартне або преміальне проживання залежно від категорії
     * @throws IllegalArgumentException якщо структура, число або категорія некоректні
     */
    public static Booking fromCsv(String line) {
        Objects.requireNonNull(line, "Рядок не може бути null");
        String[] fields = line.split(";", -1);
        if (fields.length != 5) {
            throw new IllegalArgumentException("Очікується 5 полів");
        }
        try {
            int room = Integer.parseInt(fields[0].trim());
            String guest = fields[1].trim();
            int nights = Integer.parseInt(fields[2].trim());
            double nightlyRate = Double.parseDouble(fields[3].trim());
            RoomKind kind = RoomKind.fromLabel(fields[4]);
            if (kind.isPremium()) {
                return new PremiumBooking(room, guest, nights, nightlyRate, kind);
            }
            return new StandardBooking(room, guest, nights, nightlyRate);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Числове поле має помилковий формат", exception);
        }
    }

    /**
     * Обчислює вартість проживання; кожен підтип має власну формулу.
     *
     * @return вартість проживання в гривнях
     */
    public abstract double stayCost();

    /** Повертає номер кімнати. */
    public int getRoom() {
        return room;
    }

    /** Повертає ім'я гостя. */
    public String getGuest() {
        return guest;
    }

    /** Повертає кількість ночей. */
    public int getNights() {
        return nights;
    }

    /** Повертає тариф за ніч. */
    public double getNightlyRate() {
        return nightlyRate;
    }

    /** Повертає категорію номера. */
    public RoomKind getKind() {
        return kind;
    }

    /** Порівнює записи за підтипом і всіма полями. */
    @Override
    public final boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Booking booking = (Booking) other;
        return room == booking.room
                && nights == booking.nights
                && Double.compare(nightlyRate, booking.nightlyRate) == 0
                && guest.equals(booking.guest)
                && kind == booking.kind;
    }

    /** Повертає хеш-код, узгоджений із {@link #equals(Object)}. */
    @Override
    public final int hashCode() {
        return Objects.hash(getClass(), room, guest, nights, nightlyRate, kind);
    }

    /** Повертає текстове подання запису. */
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "Кімната %d: %s, %d ночей, %.2f грн за ніч, %s",
                room, guest, nights, nightlyRate, kind.label());
    }
}