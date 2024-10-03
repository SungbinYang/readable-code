package cleancode.minesweeper.tobe.minesweeper.board;

public enum GameStatus {

    IN_PROGRESS("진행중"),
    WIN("승리"),
    LOOSE("패배"),
    ;

    private final String description;

    GameStatus(String description) {
        this.description = description;
    }
}
