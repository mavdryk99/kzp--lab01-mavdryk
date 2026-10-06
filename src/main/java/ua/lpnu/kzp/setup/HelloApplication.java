package ua.lpnu.kzp.setup;

/** Мінімальна консольна програма для перевірки Maven. */
public final class HelloApplication {

    private HelloApplication() {
    }

    /**
     * Запускає програму.
     *
     * @param args аргументи командного рядка
     */
    public static void main(String[] args) {
        System.out.printf("Hello from Maven%n");
    }
}