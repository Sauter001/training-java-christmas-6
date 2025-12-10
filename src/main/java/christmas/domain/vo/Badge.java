package christmas.domain.vo;

public enum Badge {
    SANTA("산타", 20000),
    TREE("트리", 10000),
    STAR("별", 5000),
    NONE("없음", 0);

    private final String name;
    private final int threshold;

    Badge(String name, int threshold) {
        this.name = name;
        this.threshold = threshold;
    }

    public static Badge from(int totalBenefit) {
        if (totalBenefit >= SANTA.threshold) {
            return SANTA;
        }
        if (totalBenefit >= TREE.threshold) {
            return TREE;
        }
        if (totalBenefit >= STAR.threshold) {
            return STAR;
        }
        return NONE;
    }

    public String getName() {
        return name;
    }
}
