// Model should be able to handle:
// - Different sizes of grid (provided rows=columns) i.e. 2x2, 3x3, 4x3 etc.
// - Multiple players.
// - Different sizes of grid (where rows != columns) i.e. 4x5, 6x8 etc.
// - Different win thresholds.

// Definitely a lot of repetitive code here which can be made more efficient..
// though I understand we're not being graded on this as yet :)

class OXOController
{
    private OXOModel model;
    private int current = 0;
    private boolean GameOver = false;

    public OXOController(OXOModel model)
    {

        if(model.getNumberOfColumns() > 9 || model.getNumberOfRows() > 9)
        {
            System.out.println("Row/column size is too large. Max size should be 9. Exiting...");
            System.exit(0);
        }

        if(model.getNumberOfPlayers() <= 1)
        {
            System.out.println("Insufficient players for a game. Exiting...");
            System.exit(0);
        }

        this.model = model;
        checkAssertionsEnabled();
        model.setCurrentPlayer(model.getPlayerByNumber(0));
    }

    private void checkAssertionsEnabled() {

        boolean assertionsEnabled = false;
        assert(assertionsEnabled = true);

        if (assertionsEnabled)
        {
            basicTests();

            if(model.getNumberOfColumns() == 3 && model.getNumberOfRows() == 3 && model.getWinThreshold() == 3) {
                testing3by3(model);
            }

            if(model.getNumberOfColumns() == 4 && model.getNumberOfRows() == 4 && model.getWinThreshold() == 4) {
                testing4by4(model);
            }

            if(model.getNumberOfColumns() == 9 && model.getNumberOfRows() == 6 && model.getWinThreshold() == 3) {
                testing6by9(model);
            }

        }
    }

    private int convertCharToInt(char c)
    {
        int x;

        if(c >= 'a' & c <= 'z') {
            x = c - 'a';
            return x;
        }

        if(c >= 'A' & c <= 'Z') {
            x = c - 'A';
            return x;
        }

        if(Character.isDigit(c) == true) {
            x = Character.getNumericValue(c) - 1;
            return x;
        }

        return -1;
    }

    public void handleIncomingCommand(String command) throws InvalidCellIdentifierException,
            CellAlreadyTakenException, CellDoesNotExistException
    {
        char[] characters = command.toCharArray();
        int x; int y;

        // Flags if input is too long or short //
        if(command.length() == 1 || command.length() > 2)
        {
            throw new InvalidCellIdentifierException("", command);
        }

        if(Character.isLetter(characters[0])) {
            x = convertCharToInt(characters[0]);
        }
        else{
            throw new InvalidCellIdentifierException("", command);
        }

        if(Character.isDigit(characters[1])) {
            y = Character.getNumericValue(characters[1]) - 1;
        }
        else{
            throw new InvalidCellIdentifierException("", command);
        }

        if(x < 0 || y < 0)
        {
            throw new InvalidCellIdentifierException("", command);
        }

        if(x > model.getNumberOfRows()-1 || y  > model.getNumberOfColumns()-1) {
            throw new CellDoesNotExistException(x, y);
        }

        else {

            if(GameOver == false) {
                if(model.getCellOwner(x, y) == null) {
                    model.setCellOwner(x, y, model.getCurrentPlayer());
                    moveToNextPlayer();
                }
                else{
                    throw new CellAlreadyTakenException(x, y);
                }
            }

        }

        checkOutcome();

        if(command.contains("exit"))
        {
            System.exit(0);
        }

    }

    private void moveToNextPlayer()
    {
        if(current < model.getNumberOfPlayers()-1) {
            current++;
        }

        else {
            current = 0;
        }

        model.setCurrentPlayer(model.getPlayerByNumber(current));
    }

    private void checkOutcome()
    {
        int playerNumb = 0;

        while(playerNumb < model.getNumberOfPlayers())
        {
            if (winnerCheck(model, playerNumb) == playerNumb)
            {
                model.setWinner(model.getPlayerByNumber(playerNumb));
                GameOver = true;
                return;
            }

            playerNumb++;
        }

        if (drawCheck(model) == 1)
        {
            model.setGameDrawn();
            GameOver = true;
        }
    }

    private void resetBoard()
    {
        int x = 0; int y = 0;

        for(x = 0; x < model.getNumberOfRows(); x++){
            for(y = 0; y < model.getNumberOfColumns(); y++){

                model.setCellOwner(x,y, null);
            }
        }

    }

    private int drawCheck(OXOModel model)
    {
        int x = 0; int y = 0;

        for(x = 0; x < model.getNumberOfRows(); x++){
            for(y = 0; y < model.getNumberOfColumns(); y++){

                if(model.getCellOwner(x,y) == null)
                {
                    return -1;
                }

            }
        }

        System.out.println("The game is a draw!");
        return 1;
    }

    private int scanDiagonalLeft(int counter, int playerNumb)
    {
        int x = 0; int y = 0;

        while(counter < model.getNumberOfRows()) {

            if (model.getCellOwner(counter, y) != model.getPlayerByNumber(playerNumb)) {
                x = 0;
            }

            if (model.getCellOwner(counter, y) == model.getPlayerByNumber(playerNumb)) {
                x++;

                if(x == model.getWinThreshold() ) {
                    System.out.println("Win detected for " + playerNumb + ": Diagonal left to right.");
                    return playerNumb;
                }
            }

            counter++;
            y++;
        }

        return -1;

    }

    private int scanDiagonalLeftAgain(int counter, int playerNumb)
    {
        int x = 0; int y = model.getNumberOfColumns()-1;

        while(counter >= 0) {

            if (model.getCellOwner(counter, y) != model.getPlayerByNumber(playerNumb)) {
                x = 0;
            }

            if (model.getCellOwner(counter, y) == model.getPlayerByNumber(playerNumb)) {
                x++;

                if(x == model.getWinThreshold() ) {
                    System.out.println("Win detected for " + playerNumb + ": Diagonal left to right.");
                    return playerNumb;
                }
            }

            counter--;
            y--;
        }

        return -1;
    }

    private int scanDiagonalRight(int counter, int y, int playerNumb)
    {
        int x = 0;

        while(counter >= 0) {

            if (model.getCellOwner(counter, y) != model.getPlayerByNumber(playerNumb)) {
                x = 0;
            }

            if (model.getCellOwner(counter, y) == model.getPlayerByNumber(playerNumb)) {
                x++;

                if(x == model.getWinThreshold() ) {
                    System.out.println("Win detected for " + playerNumb + ": Diagonal right to left.");
                    return playerNumb;
                }
            }

            counter--;
            y++;
        }

        return -1;
    }

    private int scanDiagonalRightAgain(int playerNumb)
    {
        int x = 0; int y = 1; int z = 1;
        int counter = model.getNumberOfRows()-1;

        while(z < model.getNumberOfColumns()) {

            if (model.getCellOwner(counter, y) != model.getPlayerByNumber(playerNumb)) {
                x = 0;
            }

            if (model.getCellOwner(counter, y) == model.getPlayerByNumber(playerNumb)) {
                x++;

                if(x == model.getWinThreshold() ) {
                    System.out.println("Win detected for " + playerNumb + ": Diagonal right to left.");
                    return playerNumb;
                }
            }

            counter--;
            y++;

            if(counter < 0){
                counter = model.getNumberOfRows()-1;

                z++;
                y = z;
            }

        }

        return -1;
    }

    private int scanHorizontal(int playerNumb)
    {
        int column = 0; int row = 0; int x = 0;

        while(row < model.getNumberOfRows() ) {

            if(column == model.getNumberOfColumns())
            {
                row++;
                x = 0;
                column = 0;
            }

            if (model.getCellOwner(row, column) == model.getPlayerByNumber(playerNumb)) {
                x++;

                if(x == model.getWinThreshold() ) {
                    System.out.println("Win detected for " + playerNumb + ": Horizontal line.");
                    return playerNumb;
                }
            }

            if (model.getCellOwner(row, column) != model.getPlayerByNumber(playerNumb)) {
                x = 0;
            }

            column++;

        }

        return -1;
    }

    private int scanVertical(int playerNumb)
    {
        int column = 0; int row = 0; int x = 0;

        while(column < model.getNumberOfColumns() ) {

            if(row == model.getNumberOfColumns())
            {
                column++;
                x = 0;
                row = 0;
            }

            if (model.getCellOwner(row, column) == model.getPlayerByNumber(playerNumb)) {
                x++;

                if(x == model.getWinThreshold() ) {
                    System.out.println("Win detected for " + playerNumb + ": Vertical line.");
                    return playerNumb;
                }

            }

            if (model.getCellOwner(row, column) != model.getPlayerByNumber(playerNumb)) {
                x = 0;
            }

            row++;

        }

        return -1;
    }

    private int winnerCheck(OXOModel model, int playerNumb)
    {
        int z = scanHorizontal(playerNumb);
        int a = scanVertical(playerNumb);

        if(z == playerNumb || a == playerNumb) {
            return playerNumb;
        }

        // Diagonal left
        int counter = 0; int x;

        while(counter < model.getNumberOfRows()) {

            x = scanDiagonalLeft(counter, playerNumb);

            if(x == playerNumb) {
                return playerNumb;
            }

            counter++;
        }

        counter = model.getNumberOfRows()-1;

        while(counter > 0) {

            x = scanDiagonalLeftAgain(counter, playerNumb);

            if (x == playerNumb) {
                return playerNumb;
            }

            counter--;
        }

        counter = 0;

        // Diagonal right
        while(counter < model.getNumberOfRows()) {

            x = scanDiagonalRight(counter, 0, playerNumb);

            if(x == playerNumb) {
                return playerNumb;
            }

            counter++;
        }

        x = scanDiagonalRightAgain(playerNumb);

        if (x == playerNumb) {
            return playerNumb;
        }

        return -1;
    }

    // Win threshold of 3
    private void testing6by9(OXOModel model)
    {
        // Horizontal
        model.setCellOwner(1,0, model.getPlayerByNumber(1));
        model.setCellOwner(1,1, model.getPlayerByNumber(1));
        model.setCellOwner(1,2, model.getPlayerByNumber(1));
        assert(winnerCheck(model, 1) == 1);
        resetBoard();

        // Horizontal - Last row
        model.setCellOwner(5,0, model.getPlayerByNumber(1));
        model.setCellOwner(5,1, model.getPlayerByNumber(0));
        model.setCellOwner(5,2, model.getPlayerByNumber(1));
        model.setCellOwner(5,3, model.getPlayerByNumber(1));
        assert(winnerCheck(model, 1) == -1);
        model.setCellOwner(5,4, model.getPlayerByNumber(1));
        assert(winnerCheck(model, 1) == 1);
        resetBoard();

        // Horizontal (with break)
        model.setCellOwner(1,0, model.getPlayerByNumber(1));
        model.setCellOwner(1,1, model.getPlayerByNumber(0));
        model.setCellOwner(1,2, model.getPlayerByNumber(1));
        model.setCellOwner(1,3, model.getPlayerByNumber(1));
        assert(winnerCheck(model, 1) == -1);
        model.setCellOwner(1,4, model.getPlayerByNumber(1));
        assert(winnerCheck(model, 1) == 1);
        resetBoard();

        // Vertical - no win
        model.setCellOwner(0,0, model.getPlayerByNumber(0));
        model.setCellOwner(1,0, model.getPlayerByNumber(0));
        model.setCellOwner(2,0, model.getPlayerByNumber(1));
        model.setCellOwner(2,1, model.getPlayerByNumber(0));
        model.setCellOwner(3,0, model.getPlayerByNumber(0));
        assert(winnerCheck(model, 0) == -1);
        resetBoard();

        // Vertical first column
        model.setCellOwner(0,0, model.getPlayerByNumber(1));
        model.setCellOwner(1,0, model.getPlayerByNumber(0));
        model.setCellOwner(2,0, model.getPlayerByNumber(0));
        model.setCellOwner(3,0, model.getPlayerByNumber(0));
        assert(winnerCheck(model, 0) == 0);
        resetBoard();

        // Vertical last column
        model.setCellOwner(0,8, model.getPlayerByNumber(1));
        model.setCellOwner(1,8, model.getPlayerByNumber(0));
        model.setCellOwner(2,8, model.getPlayerByNumber(0));
        model.setCellOwner(3,8, model.getPlayerByNumber(0));
        assert(winnerCheck(model, 0) == 0);
        resetBoard();

        // Diag left to right (Middle)
        model.setCellOwner(0,0, model.getPlayerByNumber(0));
        model.setCellOwner(1,1, model.getPlayerByNumber(0));
        model.setCellOwner(2,2, model.getPlayerByNumber(0));
        assert(winnerCheck(model, 0) == 0);
        resetBoard();

        // Diag left to right (Middle)
        model.setCellOwner(0,0, model.getPlayerByNumber(1));
        model.setCellOwner(1,1, model.getPlayerByNumber(0));
        model.setCellOwner(2,2, model.getPlayerByNumber(0));
        model.setCellOwner(3,3, model.getPlayerByNumber(0));
        assert(winnerCheck(model, 0) == 0);
        resetBoard();

        // Diag left to right
        model.setCellOwner(1,0, model.getPlayerByNumber(0));
        model.setCellOwner(2,1, model.getPlayerByNumber(0));
        model.setCellOwner(3,2, model.getPlayerByNumber(0));
        assert(winnerCheck(model, 0) == 0);
        resetBoard();

        // Diag left to right
        model.setCellOwner(1,0, model.getPlayerByNumber(0));
        model.setCellOwner(2,1, model.getPlayerByNumber(0));
        model.setCellOwner(3,2, model.getPlayerByNumber(0));
        assert(winnerCheck(model, 0) == 0);
        resetBoard();

        // Diag left to right (Weird Cases)
        model.setCellOwner(5,8, model.getPlayerByNumber(0));
        model.setCellOwner(4,7, model.getPlayerByNumber(0));
        model.setCellOwner(3,6, model.getPlayerByNumber(0));
        assert(winnerCheck(model, 0) == 0);
        resetBoard();

        // Diag left to right
        model.setCellOwner(3,3, model.getPlayerByNumber(0));
        model.setCellOwner(4,4, model.getPlayerByNumber(0));
        model.setCellOwner(5,5, model.getPlayerByNumber(0));
        assert(winnerCheck(model, 0) == 0);
        resetBoard();

        // RIGHT TO LEFT
        // Diag right to left (a3-c1)
        model.setCellOwner(2,0, model.getPlayerByNumber(0));
        model.setCellOwner(1,1, model.getPlayerByNumber(0));
        model.setCellOwner(0,2, model.getPlayerByNumber(0));
        assert(winnerCheck(model, 0) == 0);
        resetBoard();

        // Diag right to left (b3 to d1)
        model.setCellOwner(3,0, model.getPlayerByNumber(0));
        model.setCellOwner(2,1, model.getPlayerByNumber(0));
        model.setCellOwner(1,2, model.getPlayerByNumber(0));
        assert(winnerCheck(model, 0) == 0);

        // Diag right to left
        model.setCellOwner(4,1, model.getPlayerByNumber(0));
        model.setCellOwner(3,2, model.getPlayerByNumber(0));
        model.setCellOwner(2,3, model.getPlayerByNumber(0));
        assert(winnerCheck(model, 0) == 0);
        resetBoard();

        // Diag Right to Left D4 to F2
        model.setCellOwner(5,1, model.getPlayerByNumber(0));
        model.setCellOwner(4,2, model.getPlayerByNumber(0));
        model.setCellOwner(3,3, model.getPlayerByNumber(0));
        assert(winnerCheck(model, 0) == 0);
        resetBoard();

        // Diag Right to Left B6 to D4
        model.setCellOwner(1,5, model.getPlayerByNumber(0));
        model.setCellOwner(2,4, model.getPlayerByNumber(1));
        model.setCellOwner(3,3, model.getPlayerByNumber(0));
        model.setCellOwner(4,2, model.getPlayerByNumber(0));
        assert(winnerCheck(model, 0) == -1);
        model.setCellOwner(5,1, model.getPlayerByNumber(0));
        assert(winnerCheck(model, 0) == 0);
        resetBoard();

        // Diag Right to Left D5 to F3
        model.setCellOwner(5,2, model.getPlayerByNumber(0));
        model.setCellOwner(4,3, model.getPlayerByNumber(0));
        model.setCellOwner(3,4, model.getPlayerByNumber(0));
        assert(winnerCheck(model, 0) == 0);
        resetBoard();

        // Diag Right to Left (Bottom Corner)
        model.setCellOwner(5,6, model.getPlayerByNumber(0));
        model.setCellOwner(4,7, model.getPlayerByNumber(0));
        model.setCellOwner(3,8, model.getPlayerByNumber(0));
        assert(winnerCheck(model, 0) == 0);
        resetBoard();
    }

    private void testing4by4(OXOModel model)
    {
        // Diagonal - Left to right
        model.setCellOwner(0,0, model.getPlayerByNumber(0));
        model.setCellOwner(1,1, model.getPlayerByNumber(0));
        model.setCellOwner(2,2, model.getPlayerByNumber(0));
        model.setCellOwner(2,3, model.getPlayerByNumber(0));
        assert(winnerCheck(model, 0) == -1);
        model.setCellOwner(3,3, model.getPlayerByNumber(0));
        assert(winnerCheck(model, 0) == 0);
        resetBoard();

        // Diagonal - Right to left
        model.setCellOwner(0,3, model.getPlayerByNumber(0));
        model.setCellOwner(1,2, model.getPlayerByNumber(0));
        model.setCellOwner(2,1, model.getPlayerByNumber(0));
        model.setCellOwner(3,0, model.getPlayerByNumber(0));
        assert(winnerCheck(model, 0) == 0);
        resetBoard();

        // Diagonal - Right to left
        model.setCellOwner(0,3, model.getPlayerByNumber(1));
        model.setCellOwner(0,2, model.getPlayerByNumber(0));
        model.setCellOwner(1,2, model.getPlayerByNumber(1));
        model.setCellOwner(2,2, model.getPlayerByNumber(0));
        model.setCellOwner(3,3, model.getPlayerByNumber(1));
        model.setCellOwner(2,1, model.getPlayerByNumber(1));
        model.setCellOwner(3,0, model.getPlayerByNumber(1));
        assert(winnerCheck(model, 1) == 1);
        resetBoard();

        // Vertical first column
        model.setCellOwner(0,0, model.getPlayerByNumber(0));
        model.setCellOwner(1,0, model.getPlayerByNumber(0));
        model.setCellOwner(2,0, model.getPlayerByNumber(0));
        model.setCellOwner(3,0, model.getPlayerByNumber(0));
        assert(winnerCheck(model, 0) == 0);
        resetBoard();

        // Vertical second column
        model.setCellOwner(3,1, model.getPlayerByNumber(0));
        model.setCellOwner(2,2, model.getPlayerByNumber(0));
        model.setCellOwner(2,1, model.getPlayerByNumber(0));
        model.setCellOwner(1,1, model.getPlayerByNumber(0));
        assert(winnerCheck(model, 0) == -1);
        model.setCellOwner(0,1, model.getPlayerByNumber(0));
        assert(winnerCheck(model, 0) == 0);
        resetBoard();

        // Vertical last column
        model.setCellOwner(3,0, model.getPlayerByNumber(0));
        model.setCellOwner(2,1, model.getPlayerByNumber(0));
        model.setCellOwner(2,0, model.getPlayerByNumber(0));
        model.setCellOwner(1,0, model.getPlayerByNumber(0));
        assert(winnerCheck(model, 0) == -1);
        model.setCellOwner(0,0, model.getPlayerByNumber(0));
        assert(winnerCheck(model, 0) == 0);
        resetBoard();

        // Horizontal first row
        model.setCellOwner(0,3, model.getPlayerByNumber(0));
        model.setCellOwner(0,2, model.getPlayerByNumber(0));
        model.setCellOwner(0,1, model.getPlayerByNumber(0));
        model.setCellOwner(0,0, model.getPlayerByNumber(0));
        assert(winnerCheck(model, 0) == 0);
        resetBoard();

        // Horizontal second row
        model.setCellOwner(1,3, model.getPlayerByNumber(1));
        model.setCellOwner(3,2, model.getPlayerByNumber(0));
        model.setCellOwner(3,3, model.getPlayerByNumber(1));
        model.setCellOwner(0,0, model.getPlayerByNumber(0));
        model.setCellOwner(1,2, model.getPlayerByNumber(1));
        model.setCellOwner(1,1, model.getPlayerByNumber(1));
        model.setCellOwner(1,0, model.getPlayerByNumber(1));
        assert(winnerCheck(model, 1) == 1);
        resetBoard();

        // Horizontal third row
        model.setCellOwner(2,3, model.getPlayerByNumber(1));
        model.setCellOwner(2,2, model.getPlayerByNumber(1));
        model.setCellOwner(2,1, model.getPlayerByNumber(1));
        model.setCellOwner(2,0, model.getPlayerByNumber(1));
        assert(winnerCheck(model, 1) == 1);
        resetBoard();

        // Horizontal fourth row
        model.setCellOwner(3,3, model.getPlayerByNumber(0));
        model.setCellOwner(3,2, model.getPlayerByNumber(0));
        model.setCellOwner(2,3, model.getPlayerByNumber(1));
        model.setCellOwner(3,1, model.getPlayerByNumber(0));
        model.setCellOwner(3,0, model.getPlayerByNumber(0));
        assert(winnerCheck(model, 0) == 0);
        resetBoard();

    }

    private void testing3by3(OXOModel model)
    {
        // Top left to bottom right
        model.setCellOwner(0,0, model.getPlayerByNumber(1));
        model.setCellOwner(1,1, model.getPlayerByNumber(1));
        model.setCellOwner(2,2, model.getPlayerByNumber(1));
        assert(winnerCheck(model, 1) == 1);
        resetBoard();

        model.setCellOwner(0,1, model.getPlayerByNumber(1));
        model.setCellOwner(1,1, model.getPlayerByNumber(1));
        model.setCellOwner(2,1, model.getPlayerByNumber(1));
        assert(winnerCheck(model, 1) == 1);
        resetBoard();

        // Top right to bottom left
        model.setCellOwner(2,2, model.getPlayerByNumber(1));
        model.setCellOwner(1,1, model.getPlayerByNumber(1));
        model.setCellOwner(0,0, model.getPlayerByNumber(1));
        assert(winnerCheck(model, 1) == 1);
        resetBoard();

        model.setCellOwner(2,0, model.getPlayerByNumber(0));
        model.setCellOwner(2,2, model.getPlayerByNumber(0));
        model.setCellOwner(2,1, model.getPlayerByNumber(0));
        assert(winnerCheck(model, 0) == 0);
        resetBoard();

        model.setCellOwner(0,0, model.getPlayerByNumber(0));
        model.setCellOwner(1,0, model.getPlayerByNumber(0));
        model.setCellOwner(2,0, model.getPlayerByNumber(0));
        assert(winnerCheck(model, 0) == 0);
        resetBoard();

        model.setCellOwner(0,2, model.getPlayerByNumber(1));
        model.setCellOwner(1,1, model.getPlayerByNumber(1));
        model.setCellOwner(2,0, model.getPlayerByNumber(1));
        assert(winnerCheck(model, 1) == 1);
        assert(drawCheck(model) == -1);
        resetBoard();

        // Check for a draw
        model.setCellOwner(0,0, model.getPlayerByNumber(0));
        model.setCellOwner(0,2, model.getPlayerByNumber(1));
        model.setCellOwner(0,1, model.getPlayerByNumber(0));
        model.setCellOwner(1,1, model.getPlayerByNumber(1));
        model.setCellOwner(1,2, model.getPlayerByNumber(0));
        model.setCellOwner(1,0, model.getPlayerByNumber(1));
        model.setCellOwner(2,0, model.getPlayerByNumber(0));
        model.setCellOwner(2,1, model.getPlayerByNumber(1));
        model.setCellOwner(2,2, model.getPlayerByNumber(0));
        assert(drawCheck(model) == 1);
        resetBoard();
    }

    private void basicTests()
    {
        // Character conversion
        assert(convertCharToInt('a') == 0);
        assert(convertCharToInt('b') == 1);
        assert(convertCharToInt('c') == 2);
        assert(convertCharToInt('Z') == 25);

        // Integer conversion
        assert(convertCharToInt('1') == 0);
        assert(convertCharToInt('2') == 1);
        assert(convertCharToInt('9') == 8);

        // Flag invalid chars
        assert(convertCharToInt('0') == -1);
        assert(convertCharToInt('@') == -1);
        assert(convertCharToInt('"') == -1);
        assert(convertCharToInt('%') == -1);
        assert(convertCharToInt('&') == -1);

    }

}