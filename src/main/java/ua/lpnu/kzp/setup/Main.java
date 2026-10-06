package ua.lpnu.kzp.setup;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Консольна програма для обробки записів про матчі спортивної ліги.
 * Читає CSV-файл, перевіряє рядки, обчислює чотири показники
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
        // Шляхи за замовчуванням; Path сам обирає правильний роздільник для ОС.
        Path input = Path.of("data", "input.csv");
        Path output = Path.of("out", "report.txt");

        // Розбір аргументів: після --input або --output йде значення, тому a++ його пропускає.
        for (int a = 0; a < args.length; a++) {
            if ("--help".equals(args[a])) {
                System.out.printf("Використання: java -jar lab01.jar [--help] [--input <файл>] [--output <файл>]%n");
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

        // Кодування задаємо явно, щоб український текст однаково читався на всіх ОС.
        List<String> lines = Files.readAllLines(input, StandardCharsets.UTF_8);

        // Лічильники оголошені до циклу, щоб накопичувати значення по всіх рядках.
        List<String> errors = new ArrayList<>();
        int validCount = 0;
        int totalGoals = 0;
        int maxAttendance = 0;
        long totalAttendance = 0;

        for (int i = 0; i < lines.size(); i++) {
            // -1 зберігає порожнє останнє поле.
            String[] fields = lines.get(i).split(";", -1);

            // Спочатку перевіряємо структуру: інакше fields[4] може не існувати.
            if (fields.length != 5) {
                errors.add("Рядок %d: очікується 5 полів".formatted(i + 1));
                continue;
            }

            try {
                // Перетворення тексту на числа; хибне поле кине NumberFormatException.
                int homeScore = Integer.parseInt(fields[2]);
                int awayScore = Integer.parseInt(fields[3]);
                int attendance = Integer.parseInt(fields[4]);

                if (homeScore < 0 || awayScore < 0 || attendance < 0) {
                    errors.add("Рядок %d: від'ємне числове значення".formatted(i + 1));
                    continue;
                }

                // До показників потрапляють лише записи, які пройшли всі перевірки.
                validCount++;
                totalGoals += homeScore + awayScore;
                totalAttendance += attendance;
                maxAttendance = Math.max(maxAttendance, attendance);
            } catch (NumberFormatException e) {
                errors.add("Рядок %d: числове поле має помилковий формат".formatted(i + 1));
            }
        }

        // (double) не дає цілочисловому діленню відкинути дробову частину;
        // порожній набір обробляємо окремо, щоб не ділити на нуль.
        double averageGoals = validCount == 0 ? 0.0 : (double) totalGoals / validCount;

        // Звіт збираємо один раз, а виводимо двома способами. Locale.ROOT дає крапку в дробах.
        StringBuilder report = new StringBuilder();
        report.append(String.format(Locale.ROOT, "Коректних записів: %d%n", validCount));
        report.append(String.format(Locale.ROOT, "Середня кількість голів: %.2f%n", averageGoals));
        report.append(String.format(Locale.ROOT, "Найбільша відвідуваність: %d%n", maxAttendance));
        report.append(String.format(Locale.ROOT, "Сумарна відвідуваність: %d%n", totalAttendance));
        report.append(String.format(Locale.ROOT, "Помилок: %d%n", errors.size()));
        for (String error : errors) {
            report.append(error).append(System.lineSeparator());
        }

        String reportText = report.toString();
        System.out.print(reportText);

        // Каталог призначення може ще не існувати, тому створюємо його перед записом.
        Path parent = output.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.writeString(output, reportText, StandardCharsets.UTF_8);
    }
}