package chess_game;

import java.util.ArrayList;
import java.util.List;

import chess_game.enums.Color;
import chess_game.pieces.Piece;

public class Player {

    private final Color color;
    private final List<Piece> pieces = new ArrayList<>();

    public Player(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public List<Piece> getPieces() {
        return pieces;
    }

    public void addPiece(Piece piece) {
        pieces.add(piece);
    }

    public void removePiece(Piece piece) {
        pieces.remove(piece);
    }
}
