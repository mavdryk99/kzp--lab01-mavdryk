package ua.lpnu.kzp.setup;

import java.util.Locale;
import java.util.Objects;

/** Описує коректний запис про проживання гостя в готелі. */
public final class Booking {
    private final int room;
    private final String guest;
    private final int nights;
    private final double nightlyRate;
    private final String category;

    /**
     * Створює коректний запис після перевірки всіх полів.
     *
     * @param room номер кімнати
     * @param guest ім'я гостя
     * @param nights кількість ночей
     * @param nightlyRate тариф за ніч
     * @param category категорія номера
     */
    public Booking(int room, String guest, int nights, double nightlyRate, String category) {
        Objects.requireNonNull(guest, "Ім'я гостя не може бути null");
        Objects.requireNonNull(category, "Категорія не може бути null");
        if (guest.isBlank() || category.isBlank()) {
            throw new IllegalArgumentException("Ім'я гостя і категорія не можуть бути порожніми");
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
        this.category = category;
    }
        /**
     * Створює запис із рядка CSV у форматі кімната;гість;ночі;тариф;категорія.
     *
     * @param line рядок CSV
     * @return створений запис
     * @throws IllegalArgumentException якщо структура або число некоректні
     */
    public static Booking fromCsv(String line) {
        Objects.requireNonNull(line, "Рядок не може бути null");
        String[] fields = line.split(";", -1);
        if (fields.length != 5) {
            throw new IllegalArgumentException("Очікується 5 полів");
        }
        try {
            return new Booking(
                    Integer.parseInt(fields[0].trim()),
                    fields[1].trim(),
                    Integer.parseInt(fields[2].trim()),
                    Double.parseDouble(fields[3].trim()),
                    fields[4].trim());
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Числове поле має помилковий формат", exception);
        }
    }

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
    public String getCategory() {
        return category;
    }

    /** Повертає текстове подання запису. */
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "Кімната %d: %s, %d ночей, %.2f грн за ніч, %s",
                room, guest, nights, nightlyRate, category);
    }
}