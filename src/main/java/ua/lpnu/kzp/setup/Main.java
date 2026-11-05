package ua.lpnu.kzp.setup;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Консольна програма для обробки записів про проживання в готелі.
 * Читає CSV-файл, створює об'єкти Booking, обчислює чотири показники
 * та виводить звіт у консоль і у файл.
 */
public final class Main {

    /* Забороняє створення екземплярів службового класу. */
    private Main() {
    }

    /**
     * Точка входу до програми.
     *
     * @param args аргументи командного рядка: --help, --input і --output
     * @throws IOException якщо вхідний файл неможливо прочитати
     *         або вихідний файл неможливо записати
     */
    public static void main(String[] args) throws IOException {
        Path input = Path.of("data", "input.csv");
        Path output = Path.of("out", "report.txt");

        for (int a = 0; a < args.length; a++) {
            if ("--help".equals(args[a])) {
                System.out.printf("Використання: java -jar lab02.jar [--help] [--input <файл>] [--output <файл>]%n");
                return;
            } else if ("--input".equals(args[a]) && a + 1 < args.length) {
                input = Path.of(args[a + 1]);
                a++;
            } else if ("--output".equals(args[a]) && a + 1 < args.length) {
                output = Path.of(args[a + 1]);
                a++;
            } else {
                System.out.printf("Невідомий або неповний аргумент: %s%n", args[a]);
                return;
            }
        }

        List<String> lines = Files.readAllLines(input, StandardCharsets.UTF_8);

        List<Booking> bookings = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        for (int lineNumber = 1; lineNumber <= lines.size(); lineNumber++) {
            // Помилка одного рядка не зупиняє обробку решти; номер рядка додаємо тут.
            try {
                bookings.add(Booking.fromCsv(lines.get(lineNumber - 1)));
            } catch (IllegalArgumentException exception) {
                errors.add("Рядок %d: %s".formatted(lineNumber, exception.getMessage()));
            }
        }

        BookingSummary summary = summarize(bookings);

        StringBuilder report = new StringBuilder();
        report.append(String.format(Locale.ROOT, "Коректних записів: %d%n", summary.count()));
        report.append(String.format(Locale.ROOT, "Сумарна кількість ночей: %d%n", summary.totalNights()));
        report.append(String.format(Locale.ROOT, "Загальний виторг: %.2f%n", summary.totalRevenue()));
        report.append(String.format(Locale.ROOT, "Найдовше проживання: %d%n", summary.longestStay()));
        report.append(String.format(Locale.ROOT, "Помилок: %d%n", errors.size()));
        for (String error : errors) {
            report.append(error).append(System.lineSeparator());
        }

        String reportText = report.toString();
        System.out.print(reportText);

        Path parent = output.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.writeString(output, reportText, StandardCharsets.UTF_8);
    }

    /**
     * Обчислює підсумкові показники за списком коректних записів.
     *
     * @param bookings список коректних записів
     * @return підсумок із кількістю, ночами, виторгом і найдовшим проживанням
     */
    private static BookingSummary summarize(List<Booking> bookings) {
        int totalNights = 0;
        double totalRevenue = 0.0;
        int longestStay = 0;
        for (Booking booking : bookings) {
            totalNights += booking.getNights();
            totalRevenue += booking.getNights() * booking.getNightlyRate();
            longestStay = Math.max(longestStay, booking.getNights());
        }
        return new BookingSummary(bookings.size(), totalNights, totalRevenue, longestStay);
    }
}