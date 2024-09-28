package cleancode.minesweeper.tobe;

import java.util.Random;
import java.util.Scanner;

public class MinesweeperGame {

    private static String[][] board = new String[8][10]; // 게임판
    private static Integer[][] landMineCounts = new Integer[8][10]; // 지뢰 숫자
    private static boolean[][] landMines = new boolean[8][10]; // 지뢰유무
    private static int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배

    public static void main(String[] args) {
        showGameStartComments();

        Scanner scanner = new Scanner(System.in); 

        initializeGame();

        while (true) {
            showBoard();

            if (gameStatus == 1) {
                System.out.println("지뢰를 모두 찾았습니다. GAME CLEAR!");
                break;
            }
            
            if (gameStatus == -1) {
                System.out.println("지뢰를 밟았습니다. GAME OVER!");
                break;
            }

            System.out.println("선택할 좌표를 입력하세요. (예: a1)");

            String cellInput = scanner.nextLine(); // 이름 변경 (좌표에 대한 input)

            System.out.println("선택한 셀에 대한 행위를 선택하세요. (1: 오픈, 2: 깃발 꽂기)");

            String userActionInput = scanner.nextLine(); // 이름 변경 (행위에 대한 input)
            char cellInputCol = cellInput.charAt(0); // 이름 변경
            char cellInputRow  = cellInput.charAt(1); // 이름 변경
            int selectedColIndex = convertColFrom(cellInputCol);
            int selectedRowIndex = convertRowFrom(cellInputRow);

            if (userActionInput.equals("2")) {
                board[selectedRowIndex][selectedColIndex] = "⚑";
                checkIfGameIsOver();
            } else if (userActionInput.equals("1")) {
                if (landMines[selectedRowIndex][selectedColIndex]) {
                    board[selectedRowIndex][selectedColIndex] = "☼";
                    gameStatus = -1;
                    continue;
                } else {
                    open(selectedRowIndex, selectedColIndex);
                }
                checkIfGameIsOver();
            } else {
                System.out.println("잘못된 번호를 선택하셨습니다.");
            }
        }
    }

    private static void checkIfGameIsOver() {
        boolean isAllOpened = isAllCellOpened();
        if (isAllOpened) {
            gameStatus = 1;
        }
    }

    private static boolean isAllCellOpened() {
        boolean isAllOpened = true;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 10; col++) {
                if (board[row][col].equals("□")) {
                    isAllOpened = false; // 전체 판을 돌면서 "ㅁ"칸 존재 유무 확
                }
            }
        }
        return isAllOpened;
    }

    private static int convertRowFrom(char cellInputRow) {
        return Character.getNumericValue(cellInputRow) - 1;
    }

    private static int convertColFrom(char cellInputCol) {
        switch (cellInputCol) {
            case 'a':
                return 0;
            case 'b':
                return 1;
            case 'c':
                return 2;
            case 'd':
                return 3;
            case 'e':
                return 4;
            case 'f':
                return 5;
            case 'g':
                return 6;
            case 'h':
                return 7;
            case 'i':
                return 8;
            case 'j':
                return 9;
            default:
                return -1;
        }
    }

    private static void showBoard() {
        System.out.println("   a b c d e f g h i j");

        // 이름 변경
        for (int row = 0; row < 8; row++) {
            System.out.printf("%d  ", row + 1);
            for (int col = 0; col < 10; col++) {
                System.out.print(board[row][col] + " ");
            }
            System.out.println();
        }

        System.out.println();
    }

    private static void initializeGame() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 10; col++) {
                board[row][col] = "□";
            }
        }

        for (int i = 0; i < 10; i++) {
            int col = new Random().nextInt(10);
            int row = new Random().nextInt(8);
            landMines[row][col] = true; // 지뢰 10개 랜덤 생성
        }

        // 이름 수
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 10; col++) {
                int count = 0;
                if (!landMines[row][col]) {
                    // 해당 셀의 주위 8칸을 돌면서 지뢰가 몇개 가지고 있는지 체크하는 로직
                    if (row - 1 >= 0 && col - 1 >= 0 && landMines[row - 1][col - 1]) {
                        count++; // 왼쪽 대각선에 지뢰가 있는지 확인
                    }
                    if (row - 1 >= 0 && landMines[row - 1][col]) {
                        count++;
                    }
                    if (row - 1 >= 0 && col + 1 < 10 && landMines[row - 1][col + 1]) {
                        count++;
                    }
                    if (col - 1 >= 0 && landMines[row][col - 1]) {
                        count++;
                    }
                    if (col + 1 < 10 && landMines[row][col + 1]) {
                        count++;
                    }
                    if (row + 1 < 8 && col - 1 >= 0 && landMines[row + 1][col - 1]) {
                        count++;
                    }
                    if (row + 1 < 8 && landMines[row + 1][col]) {
                        count++;
                    }
                    if (row + 1 < 8 && col + 1 < 10 && landMines[row + 1][col + 1]) {
                        count++;
                    }
                    landMineCounts[row][col] = count;
                    continue;
                }
                landMineCounts[row][col] = 0; // 지뢰인 경우 주위를 확인할 필요가 없기에 초기화
            }
        }
    }

    private static void showGameStartComments() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("지뢰찾기 게임 시작!");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    // 재귀 함수
    // 주변의 인접한 열 수 있는 셀들을 열겠다는 로직
    private static void open(int row, int col) {
        if (row < 0 || row >= 8 || col < 0 || col >= 10) { // 판을 벗어났는지
            return;
        }
        if (!board[row][col].equals("□")) { // 이미 열린 셀인지
            return;
        }
        if (landMines[row][col]) { // 지뢰셀인지
            return;
        }
        if (landMineCounts[row][col] != 0) { // 지뢰숫자를 가졌는지
            board[row][col] = String.valueOf(landMineCounts[row][col]); // 보드의 지뢰 숫자 표기
            return;
        } else {
            board[row][col] = "■"; // 열린 빈 셀로 표기
        }

        // 지기 주변 8개 셀을 재귀로 쭉 돈다.
        open(row - 1, col - 1);
        open(row - 1, col);
        open(row - 1, col + 1);
        open(row, col - 1);
        open(row, col + 1);
        open(row + 1, col - 1);
        open(row + 1, col);
        open(row + 1, col + 1);
    }

}
