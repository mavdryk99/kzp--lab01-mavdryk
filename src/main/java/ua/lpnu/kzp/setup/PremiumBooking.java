package ua.lpnu.kzp.setup;

/** Преміальне проживання: до тарифу кожної ночі додається плата за сервіс. */
public final class PremiumBooking extends Booking {
    private static final double SERVICE_FEE_PER_NIGHT = 200.0;

    /**
     * Створює преміальне проживання.
     *
     * @param room номер кімнати
     * @param guest ім'я гостя
     * @param nights кількість ночей
     * @param nightlyRate тариф за ніч
     * @param kind преміальна категорія (Напівлюкс або Люкс)
     */
    public PremiumBooking(int room, String guest, int nights, double nightlyRate, RoomKind kind) {
        super(room, guest, nights, nightlyRate, kind);
        if (!kind.isPremium()) {
            throw new IllegalArgumentException("Преміальне проживання потребує категорії Напівлюкс або Люкс");
        }
    }

    /** Повертає вартість: ночі × (тариф + плата за сервіс). */
    @Override
    public double stayCost() {
        return getNights() * (getNightlyRate() + SERVICE_FEE_PER_NIGHT);
    }
}