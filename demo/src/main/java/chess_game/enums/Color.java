package chess_game.enums;

public enum Color {
    WHITE(-1),
    BLACK(1);

    private final int forwardDir;

    Color(int forwardDir) {
        this.forwardDir = forwardDir;
    }

    public int getForwardDir() {
        return forwardDir;
    }
}
