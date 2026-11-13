package logic;

import java.util.*;

public class TextOperations {
    private static final String[] NAMES = {
            "Верхній регістр",
            "Нижній регістр",
            "Сортувати рядки",
            "Видалити повтори рядків",
            "Зворотний порядок рядків",
            "Пронумерувати рядки",
            "Частотний словник слів"
    };
    public static int size() {
        return NAMES.length;
    }
    public static String name(int i) {
        return NAMES[i];
    }

    public static String apply(int i, String t) {
        String[] lines = t.split("\n");
        if (i == 0) {
            return t.toUpperCase();
        }
        if (i == 1) {
            return t.toLowerCase();
        }
        if (i == 2) {
            Arrays.sort(lines, String.CASE_INSENSITIVE_ORDER);
            return String.join("\n", lines);
        }
        if (i == 3) {
            return String.join("\n", new LinkedHashSet<>(Arrays.asList(lines)));
        }
        if (i == 4) {
            List<String> list = Arrays.asList(lines);
            Collections.reverse(list);
            return String.join("\n", list);
        }
        if (i == 5) {
            String result = "";
            for (int k = 0; k < lines.length; k++) {
                result += (k + 1) + ". " + lines[k] + "\n";
            }
            return result.trim();
        }
        return frequency(t);
    }

    private static String frequency(String t) {
        Map<String, Integer> map = new TreeMap<>();
        for (String w : t.toLowerCase().split("[^\\p{L}\\p{N}]+")) {
            if (!w.isEmpty()) map.merge(w, 1, Integer::sum);
        }
        List<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
        list.sort((a, b) -> b.getValue() - a.getValue());
        String result = "";
        for (Map.Entry<String, Integer> e : list) {
            result += e.getKey() + " - " + e.getValue() + "\n";
        }
        return result.trim();
    }
}