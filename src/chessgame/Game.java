package chessgame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Game {

  private static Piece[][] board;
  private static boolean isWhiteTime; // TODO: implement this variable value change
  private static String square; // TODO: to be used
  private static String uci; // TODO: to be used

  public Game() {
    board = populateGameBoard();
    isWhiteTime = true;
    square = null;
    uci = null;
    play();
  }

  private static Piece[][] populateGameBoard() {
    Piece[][] populatedGameBoard = new Piece[8][8];

    String rookSymbol = "♜";
    String knightSymbol = "♞";
    String bishopSymbol = "♝";
    String kingSymbol = "♛";
    String queenSymbol = "♚";
    String pawnSymbol = "♟";

    // Populate white side
    populatedGameBoard[0][0] =
        new Rook(true, rookSymbol, new Position(0,0));
    populatedGameBoard[0][1] =
        new Knight(true, knightSymbol, new Position(0, 1));
    populatedGameBoard[0][2] =
        new Bishop(true, bishopSymbol, new Position(0, 2));
    populatedGameBoard[0][3] =
        new King(true, kingSymbol, new Position(0, 3));
    populatedGameBoard[0][4] =
        new Queen(true, queenSymbol, new Position(0, 4));
    populatedGameBoard[0][5] =
        new Bishop(true, bishopSymbol, new Position(0, 5));
    populatedGameBoard[0][6] =
        new Knight(true, knightSymbol, new Position(0, 6));
    populatedGameBoard[0][7] =
        new Rook(true, pawnSymbol, new Position(0,7));

    populatedGameBoard[1][0] = new Pawn(true, pawnSymbol, new Position(1, 0));
    populatedGameBoard[1][1] = new Pawn(true, pawnSymbol, new Position(1, 1));
    populatedGameBoard[1][2] = new Pawn(true, pawnSymbol, new Position(1, 2));
    populatedGameBoard[1][3] = new Pawn(true, pawnSymbol, new Position(1, 3));
    populatedGameBoard[1][4] = new Pawn(true, pawnSymbol, new Position(1, 4));
    populatedGameBoard[1][5] = new Pawn(true, pawnSymbol, new Position(1, 5));
    populatedGameBoard[1][6] = new Pawn(true, pawnSymbol, new Position(1, 6));
    populatedGameBoard[1][7] = new Pawn(true, pawnSymbol, new Position(1, 7));

    // Populate black side
    populatedGameBoard[7][0] =
        new Rook(false, rookSymbol, new Position(7,0));
    populatedGameBoard[7][1] =
        new Knight(false, knightSymbol, new Position(7, 1));
    populatedGameBoard[7][2] =
        new Bishop(false, bishopSymbol, new Position(7, 2));
    populatedGameBoard[7][3] =
        new King(false, kingSymbol, new Position(7, 3));
    populatedGameBoard[7][4] =
        new Queen(false, queenSymbol, new Position(7, 4));
    populatedGameBoard[7][5] =
        new Bishop(false, bishopSymbol, new Position(7, 5));
    populatedGameBoard[7][6] =
        new Knight(false, knightSymbol, new Position(7, 6));
    populatedGameBoard[7][7] =
        new Rook(false, rookSymbol, new Position(7,7));

    populatedGameBoard[6][0] = new Pawn(false, pawnSymbol, new Position(6, 0));
    populatedGameBoard[6][1] = new Pawn(false, pawnSymbol,new Position(6, 1));
    populatedGameBoard[6][2] = new Pawn(false, pawnSymbol, new Position(6, 2));
    populatedGameBoard[6][3] = new Pawn(false, pawnSymbol, new Position(6, 3));
    populatedGameBoard[6][4] = new Pawn(false, pawnSymbol, new Position(6, 4));
    populatedGameBoard[6][5] = new Pawn(false, pawnSymbol, new Position(6, 5));
    populatedGameBoard[6][6] = new Pawn(false, pawnSymbol, new Position(6, 6));
    populatedGameBoard[6][7] = new Pawn(false, pawnSymbol, new Position(6, 7));

    return populatedGameBoard;
  }

  private static String manageMenuOptions(boolean isWhiteTime) {
    String userInputOption;
    Scanner userInputReader = new Scanner(System.in);

    while(true) {
      if(isWhiteTime) {
        System.out.println("\n\nWhite to move");
      } else {
        System.out.println("\n\nBlack to move");
      }

      System.out.print("Enter UCI (type 'help' for help): ");
      userInputOption = userInputReader.nextLine();

      boolean isValidMenuOption = isValidUserOption(userInputOption);

      if(isValidMenuOption) {
        return userInputOption;
      } else {
        System.err.println("Invalid input, please try again\n");
      }
    }
  }

  private static boolean isValidUserOption(String userInputOption) {
    ArrayList<String> options =
        new ArrayList<>(Arrays.asList("help", "board", "resign", "moves"));

    if(options.contains(userInputOption)) {
      return true;
    } else if(isSquareOrUci(userInputOption)) {
      return true;
    } else {
      return false;
    }
  }

  private static boolean isSquareOrUci(String userInputOption) {
    boolean isSquareOrUci = true;
    int countChars = 0;

    for(Character userInputByChar : userInputOption.toCharArray()) {
      if(Character.isLetter(userInputByChar)) {
        isSquareOrUci = false;
        countChars += 1;
      }
    }
    return isSquareOrUci && (countChars % 2 == 0);
  }

  private static void displayBoardCurrentState() {
    for (int idx = 7; idx >= 0; idx --) {
      System.out.println();
      for (int jdx = 7; jdx >= 0; jdx --) {
        if(board[idx][jdx] != null) {
          System.out.print(board[idx][jdx].getPieceSymbol() + " ");
        } else {
          System.out.print("* ");
        }
      }
      System.out.print(" " + idx);
    }
    System.out.print("\n\n");

    for (int index = 7; index >= 0; index --) {
      System.out.print(index + " ");
    }
  }

  private static void displayAllPossibleMoves(boolean isWhiteTime) {
    ArrayList<Piece> allPiecesFromCurrentColor = new ArrayList<>();
    List<String> allAvailableMovements = new LinkedList<>();

    if(isWhiteTime) {
      for(int idx = 0; idx <= 7; idx ++) {
        for(int jdx = 0; jdx <= 7; jdx ++) {
          if(board[idx][jdx] != null) {
            if(board[idx][jdx].isWhite()) {
              allPiecesFromCurrentColor.add(board[idx][jdx]);
            }
          }
        }
      }

      for (Piece piece : allPiecesFromCurrentColor) {
        allAvailableMovements =
            Stream
                .concat(allAvailableMovements.stream(),
                    piece.showAvailableMovements(piece.position).stream())
                .collect(Collectors.toList());
      }

    } else {
      for(int idx = 7; idx >= 0; idx ++) {
        for(int jdx = 7; jdx >= 0; jdx ++) {
          if(board[idx][jdx] != null) {
            if(!board[idx][jdx].isWhite()) {
              allPiecesFromCurrentColor.add(board[idx][jdx]);
            }
          }
        }
      }

      for (Piece piece : allPiecesFromCurrentColor) {
        allAvailableMovements =
            Stream
                .concat(allAvailableMovements.stream(),
                    piece.showAvailableMovements(piece.position).stream())
                .collect(Collectors.toList());
      }
    }
    System.out.println(allAvailableMovements);
  }

  public static void displayResult(){
    if(isWhiteTime) System.out.println("Game over - 0-1 - Black won by resignation");
    else System.out.println("Game over - 1-0 - White won by resignation");
  }

  public static void displayPossibleMovements(String userOption) {
    int row = Integer.parseInt(userOption.substring(0,1));
    int column = Integer.parseInt(userOption.substring(1,2));

    try {
      List<String> availableMovements = board[row][column].showAvailableMovements(new Position(row,column));

      System.out.printf("Possible moves for %s: \n",userOption);
      System.out.println(availableMovements.toString());

    } catch(NullPointerException e) {
      System.out.println("There is no pieces in the position.");
    } catch(ArrayIndexOutOfBoundsException e) {
      System.out.println("The position is out of range, please try again");
    }
  }

  public static void play() {
    boolean stillRunning = true;

    while(stillRunning) {
      displayBoardCurrentState();
      String userOption = manageMenuOptions(isWhiteTime);

      switch (userOption) {
        case "help":
          System.out.println("* type 'help' for help");
          System.out.println("* type 'board' to see the board again");
          System.out.println("* type 'resign' to resign");
          System.out.println("* type 'moves' to list all possible moves");
          System.out.println("* type a square (e.g. b1, e2) to list possible moves for that square");
          System.out.println("* type UCI (e.g. b1c3, e7e8) to make a move");
          continue;

        case "board":
          continue;

        case "moves":
          displayAllPossibleMoves(isWhiteTime);
          continue;

        case "resign":
          displayBoardCurrentState();
          displayResult();
          stillRunning = false;
          break;

        default:
          if (userOption.length() <= 2) {
            int requestedRow = Integer.parseInt(userOption.substring(0, userOption.length() / 2));
            int requestedColumn = Integer.parseInt(userOption.substring(userOption.length() / 2));
            Piece pieceInSquare = board[requestedRow][requestedColumn];

            System.out.println("Possible moves for " + userOption + ":");
            System.out.println(pieceInSquare.showAvailableMovements(pieceInSquare.position));
          } else {
            String basePieceSquare = userOption.substring(0, userOption.length() / 2);
            int basePieceRow =
                Integer.parseInt(basePieceSquare.substring(0, basePieceSquare.length() / 2));
            int basePieceColumn =
                Integer.parseInt(basePieceSquare.substring(basePieceSquare.length() / 2));
            Piece basePiece = board[basePieceRow][basePieceColumn];

            String targetPieceSquare = userOption.substring(userOption.length() / 2);
            int targetPieceRow =
                Integer.parseInt(targetPieceSquare.substring(0, targetPieceSquare.length() / 2));
            int targetPieceColumn =
                Integer.parseInt(targetPieceSquare.substring(targetPieceSquare.length() / 2));

            if(basePiece.isValidMovement(new Position(targetPieceRow, targetPieceColumn))) {
              basePiece.move(new Position(targetPieceRow, targetPieceColumn));
              board[targetPieceRow][targetPieceColumn] = basePiece;
              board[basePieceRow][basePieceColumn] = null;
              System.out.println("OK");
              isWhiteTime = !isWhiteTime;
            } else {
              System.err.println("Invalid movement, please try again");
            }
          }
        break;
      }
    }
  }

  public void board(){
    // 2D array for the board
    String [][] board = new String[8][8];
    // Black Pieces
    String rookB = "♜";
    String knightB = "♞";
    String bishopB = "♝";
    String kingB = "♛";
    String queenB = "♚";
    board[0][0] = rookB;
    board[0][1] = knightB;
    board[0][2] = bishopB;
    board[0][3] = kingB;
    board[0][4] = queenB;
    board[0][5] = bishopB;
    board[0][6] = knightB;
    board[0][7] = rookB;

    for (int i = 0; i < 8; i++){
      String pawnB = "♟";
      board[1][i] = pawnB;
    }
    // White Pieces
    for (int j = 0; j < 8; j++){
      String pawnW = "♙";
      board[6][j] = pawnW;
    }

    String rookW = "♖";
    String knightW = "♘";
    String bishopW = "♗";
    String kingW = "♕";
    String queenW = "♔";
    board[7][0] = rookW;
    board[7][1] = knightW;
    board[7][2] = bishopW;
    board[7][3] = kingW;
    board[7][4] = queenW;
    board[7][5] = bishopW;
    board[7][6] = knightW;
    board[7][7] = rookW;

    // Middle of the board
    for (int k = 2; k <= 5; k++){
      for (int l = 0; l < 8; l++){
        board[k][l] = "*";
      }
    }

    System.out.println(Arrays.deepToString(board));
  }

}
