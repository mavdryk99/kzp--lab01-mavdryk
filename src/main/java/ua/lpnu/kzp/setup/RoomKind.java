package ua.lpnu.kzp.setup;

import java.util.Objects;

/** Категорія номера в готелі. */
public enum RoomKind {
    STANDARD("Стандарт", false),
    SEMI_LUX("Напівлюкс", true),
    LUX("Люкс", true);

    private final String label;
    private final boolean premium;

    /* Конструктор enum створює фіксовані константи з підписом і ознакою преміальності. */
    RoomKind(String label, boolean premium) {
        this.label = label;
        this.premium = premium;
    }

    /** Повертає підпис категорії для звіту та CSV. */
    public String label() {
        return label;
    }

    /** Повертає true, якщо категорія відповідає преміальному проживанню. */
    public boolean isPremium() {
        return premium;
    }

    /**
     * Знаходить категорію за текстовим підписом.
     *
     * @param text підпис категорії, наприклад «Люкс»
     * @return відповідна категорія
     * @throws IllegalArgumentException якщо такої категорії немає
     */
    public static RoomKind fromLabel(String text) {
        Objects.requireNonNull(text, "Категорія не може бути null");
        for (RoomKind kind : values()) {
            if (kind.label.equalsIgnoreCase(text.trim())) {
                return kind;
            }
        }
        throw new IllegalArgumentException("Невідома категорія номера: %s".formatted(text.trim()));
    }
}