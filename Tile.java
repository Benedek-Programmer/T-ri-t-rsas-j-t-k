public class Tile {
    private final int index;
    private final String title;
    private final String description;
    private final TileType type;
    private final Question question;

    public Tile(int index, String title, String description, TileType type, Question question) {
        this.index = index;
        this.title = title;
        this.description = description;
        this.type = type;
        this.question = question;
    }

    public int getIndex() {
        return index;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TileType getType() {
        return type;
    }

    public Question getQuestion() {
        return question;
    }
}
