package ua.lpnu.kzp.setup;

/**
 * Незмінний набір підсумкових показників проживань.
 *
 * @param count кількість коректних записів
 * @param totalNights сумарна кількість ночей
 * @param totalRevenue загальний виторг
 * @param longestStay найдовше проживання в ночах
 */
public record BookingSummary(int count, int totalNights,
                             double totalRevenue, int longestStay) {

    /** Перевіряє допустимість підсумкових показників. */
    public BookingSummary {
        if (count < 0 || totalNights < 0 || longestStay < 0
                || totalRevenue < 0 || !Double.isFinite(totalRevenue)) {
            throw new IllegalArgumentException("Показники не можуть бути від'ємними");
        }
    }
}