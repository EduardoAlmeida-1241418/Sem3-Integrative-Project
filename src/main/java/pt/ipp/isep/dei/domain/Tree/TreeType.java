package pt.ipp.isep.dei.domain.Tree;

public enum TreeType {
    LAT_TREE("Latitude Tree"),
    LON_TREE("Longitude Tree"),
    TZG_TREE("Time Zone Group Tree");

    private final String label;

    TreeType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}

