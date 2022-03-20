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

    // black
    String rookSymbol = "♜";
    String knightSymbol = "♞";
    String bishopSymbol = "♝";
    String kingSymbol = "♛";
    String queenSymbol = "♚";
    String pawnSymbol = "♟";
    // white
    String rookSymbolW = "♖";
    String knightSymbolW = "♘";
    String bishopSymbolW = "♗";
    String kingSymbolW = "♕";
    String queenSymbolW = "♔";
    String pawnSymbolW = "♙";

    // Populate white side
    populatedGameBoard[0][0] =
        new Rook(true, rookSymbolW, new Position(0,0));
    populatedGameBoard[0][1] =
        new Knight(true, knightSymbolW, new Position(0, 1));
    populatedGameBoard[0][2] =
        new Bishop(true, bishopSymbolW, new Position(0, 2));
    populatedGameBoard[0][3] =
        new King(true, kingSymbolW, new Position(0, 3));
    populatedGameBoard[0][4] =
        new Queen(true, queenSymbolW, new Position(0, 4));
    populatedGameBoard[0][5] =
        new Bishop(true, bishopSymbolW, new Position(0, 5));
    populatedGameBoard[0][6] =
        new Knight(true, knightSymbolW, new Position(0, 6));
    populatedGameBoard[0][7] =
        new Rook(true, rookSymbolW, new Position(0,7));

    populatedGameBoard[1][0] = new Pawn(true, pawnSymbolW, new Position(1, 0));
    populatedGameBoard[1][1] = new Pawn(true, pawnSymbolW, new Position(1, 1));
    populatedGameBoard[1][2] = new Pawn(true, pawnSymbolW, new Position(1, 2));
    populatedGameBoard[1][3] = new Pawn(true, pawnSymbolW, new Position(1, 3));
    populatedGameBoard[1][4] = new Pawn(true, pawnSymbolW, new Position(1, 4));
    populatedGameBoard[1][5] = new Pawn(true, pawnSymbolW, new Position(1, 5));
    populatedGameBoard[1][6] = new Pawn(true, pawnSymbolW, new Position(1, 6));
    populatedGameBoard[1][7] = new Pawn(true, pawnSymbolW, new Position(1, 7));

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

    for(Character userInputByChar : userInputOption.toCharArray()) {
      if(Character.isLetter(userInputByChar)) {
        isSquareOrUci = false;
      }
    }
    return isSquareOrUci;
  }

  private static void displayBoardCurrentState() {
    for (int idx = 7; idx >= 0; idx --) {
      System.out.println();
      for (int jdx = 7; jdx >= 0; jdx --) {
        if(board[idx][jdx] != null) {
          System.out.print(board[idx][jdx].getPieceSymbol() + " ");
        } else {
          System.out.print("・ ");
        }
      }
      System.out.print(" " + idx);
    }
    System.out.print("\n\n");

    for (int index = 0; index <= 7; index ++) {
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
    else System.out.println("Game over - 1-0 - 1-0 White won by resignation");
  }

  public static void displayPossibleMovements(String userOption) {
    int row = Integer.parseInt(userOption.substring(0,1));
    int column = Integer.parseInt(userOption.substring(1,2));

    try {
      // Do we need to pass position instance? Position instance should have current position data
      List<String> availableMovements = board[row][column].showAvailableMovements(new Position(row,column));

      System.out.printf("Possible moves for %s: \n",userOption);
      System.out.println(availableMovements.toString());

    } catch(NullPointerException e) {
      System.out.println("There is no pieces in the position.");
    }
  }

  public static void movePieces(String userOption){
  // [row,column]   5254 -> currPosition = [5,2], newPosition = [5,4]
    int[] currPosition = {
              Integer.parseInt(userOption.substring(0,1)),
              Integer.parseInt(userOption.substring(1,2))
    };

    int[] newPosition = {
            Integer.parseInt(userOption.substring(2,3)),
            Integer.parseInt(userOption.substring(3,4))
    };

    Piece selectedPiece = board[currPosition[0]][currPosition[1]];

    // validate position
    if(!selectedPiece.isValidMovement(new Position(newPosition[0],newPosition[1]))){
        System.out.println("Invalid position, Please try again");
        return;
    }

    // special move
    if(selectedPiece instanceof King && (((King) selectedPiece).getIsFirstMove())){


      //validate special move for rook



    } else if(selectedPiece instanceof Pawn && (((Pawn) selectedPiece).getIsFirstMove())) {
      Pawn pawn = (Pawn) selectedPiece;

      //validate special move for rook


      // update position



      pawn.setFirstMove(false);
    }

      /** tasks
       *  checkmate
       *  game ends
       *  special move
       */
  }

  public static void play() {
    boolean stillRunning = true;

    while(stillRunning) {
      displayBoardCurrentState();
      String userOption = manageMenuOptions(isWhiteTime);

      switch (userOption) {
        case "help": // Done
          System.out.println("* type 'help' for help");
          System.out.println("* type 'board' to see the board again");
          System.out.println("* type 'resign' to resign");
          System.out.println("* type 'moves' to list all possible moves");
          System.out.println("* type a square (e.g. b1, e2) to list possible moves for that square");
          System.out.println("* type UCI (e.g. b1c3, e7e8) to make a move");
          continue;

        case "board": // Done
          continue;

        case "moves": // Done
          displayAllPossibleMoves(isWhiteTime);
          continue;

        case "resign":
          // TODO: implement resign
          displayBoardCurrentState();
          displayResult();
          break;

        default:
          // TODO: implement here the logic for move piece
          if(userOption.length() == 2) {
            //square
            displayPossibleMovements(userOption);
          } else {
            //UCI
            movePieces(userOption);
          }
          continue;
      }
      stillRunning = false;
    }
  }


}
