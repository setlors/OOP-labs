public class ShapeRegistry {

    private static final String[] NAMES =
            {"Крапка", "Лінія", "Прямокутник", "Еліпс", "Лінія з кружечками", "Куб"};

    public static int size() {
        return NAMES.length;
    }

    public static String name(int type) {
        return NAMES[type];
    }


        public static Shape create(int type, int x1, int y1, int x2, int y2) {
            return switch (type) {
                case 0 -> new PointShape(x1, y1, x2, y2);
                case 1 -> new LineShape(x1, y1, x2, y2);
                case 2 -> new RectShape(x1, y1, x2, y2);
                case 3 -> new EllipseShape(x1, y1, x2, y2);
                case 4 -> new LineOShape(x1, y1, x2, y2);
                default -> new CubeShape(x1, y1, x2, y2);
            };
    }

    public static int indexOf(String name) {
        for (int i = 0; i < NAMES.length; i++) {
            if (NAMES[i].equals(name)) return i;
        }
        return -1;
    }
}