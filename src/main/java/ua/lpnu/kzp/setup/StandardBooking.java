package ua.lpnu.kzp.setup;

/** Стандартне проживання: вартість дорівнює ночі, помноженій на тариф. */
public final class StandardBooking extends Booking {

    /**
     * Створює стандартне проживання.
     *
     * @param room номер кімнати
     * @param guest ім'я гостя
     * @param nights кількість ночей
     * @param nightlyRate тариф за ніч
     */
    public StandardBooking(int room, String guest, int nights, double nightlyRate) {
        super(room, guest, nights, nightlyRate, RoomKind.STANDARD);
    }

    /** Повертає вартість: ночі × тариф. */
    @Override
    public double stayCost() {
        return getNights() * getNightlyRate();
    }
}