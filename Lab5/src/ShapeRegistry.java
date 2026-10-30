public class ShapeRegistry {
    interface Factory {
        Shape create(int x1, int y1, int x2, int y2);
    }

    private static final String[] NAMES =
            {"Крапка", "Лінія", "Прямокутник", "Еліпс", "Лінія з кружечками", "Куб"};

    private static final Factory[] FACTORIES = {
            PointShape::new, LineShape::new, RectShape::new,
            EllipseShape::new, LineOShape::new, CubeShape::new};

    public static int size() {
        return NAMES.length;
    }

    public static String name(int type) {
        return NAMES[type];
    }

    public static Shape create(int type, int x1, int y1, int x2, int y2) {
        return FACTORIES[type].create(x1, y1, x2, y2);
    }

    public static int indexOf(String name) {
        for (int i = 0; i < NAMES.length; i++) {
            if (NAMES[i].equals(name)) return i;
        }
        return -1;
    }
}