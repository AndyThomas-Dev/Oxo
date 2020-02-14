// As it stands, controller can accommodate (beyond the normal spec):
// - Different sizes of grid (provided rows=columns) i.e. 2x2, 3x3, 4x3 etc.
// - Multiple players.
// As it stands, controller can accommodate (beyond the normal spec):
// - Different sizes of grid (provided rows=columns) i.e. 2x2, 3x3, 4x4 etc.
// - Different sizes of grid (where rows != columns) i.e. 4x5, 6x8 etc. (though not extensively tested).
// - Multiple players.

class OXOController
{
    private OXOModel model;
    private int current = 0;
    private boolean GameOver = false;

    public OXOController(OXOModel model)
    {

        // Move to seperate validation function
        if(model.getNumberOfColumns() > 9 || model.getNumberOfRows() > 9)
        {
            System.out.println("Row/column size is too large. Max size should be 9. Exiting...");
            System.exit(0);
        }

        if(model.getWinThreshold() > model.getNumberOfColumns() && model.getWinThreshold() > model.getNumberOfRows())
        {
            System.out.println("Win threshold is impossible for grid. Exiting...");
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

    // Deals with input string
    public void handleIncomingCommand(String command) throws InvalidCellIdentifierException,
            CellAlreadyTakenException, CellDoesNotExistException
    {
        char[] characters = command.toCharArray();
        int x = convertCharToInt(characters[0]);
        int y = convertCharToInt(characters[1]);

        // Flags if input is too long //
        if(command.length() > 2 || x < 0 || y < 0)
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

        return 1;
    }

    private int scanDiagonalLeft(int playerNumb)
    {
        int counter = 0; int x = 0;

        while(counter < model.getNumberOfRows()) {

            if (model.getCellOwner(counter, counter) == model.getPlayerByNumber(playerNumb)) {
                x++;
            }

            counter++;
        }

        if(x == model.getWinThreshold() ) {
            System.out.println("Win detected for Player" + playerNumb + ": Diagonal left to right.");
            return playerNumb;
        }
        else{
            return -1;
        }

    }

    private int scanDiagonalRight(int playerNumb)
    {
        int counter = model.getWinThreshold()-1;
        int counter2 = 0;
        int x = 0;

        while(counter2 < model.getWinThreshold()) {

            if (model.getCellOwner(counter2, counter) == model.getPlayerByNumber(playerNumb))
            {
                x++;
            }

            counter--;
            counter2++;
        }

        if(x == model.getWinThreshold() ) {
            System.out.println("Win detected for " + playerNumb + ": Diagonal right to left.");
            return playerNumb;
        }
        else{
            return -1;
        }

    }

    private int scanHoriztontal(int playerNumb)
    {
        int column = 0;
        int row = 0;
        int x = 0;

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

            column++;

        }

        return -1;
    }

    // Needs fixing
    private int scanVertical(int playerNumb)
    {
        int column = 0;
        int row = 0;
        int x = 0;

        while(column < model.getNumberOfRows() ) {

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

            row++;

        }

        return -1;
    }

    private int winnerCheck(OXOModel model, int playerNumb)
    {
        int x = scanDiagonalLeft(playerNumb);
        int y = scanDiagonalRight(playerNumb);
        int z = scanHoriztontal(playerNumb);
        int a = scanVertical(playerNumb);

        if(x == playerNumb || y == playerNumb || z == playerNumb || a == playerNumb) {
            return playerNumb;
        }

        return -1;
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
        assert(convertCharToInt('"') == -1);
        assert(convertCharToInt('%') == -1);
        assert(convertCharToInt('&') == -1);

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

        model.setCellOwner(0,1, model.getPlayerByNumber(1));
        model.setCellOwner(1,1, model.getPlayerByNumber(1));
        model.setCellOwner(2,1, model.getPlayerByNumber(1));
        assert(winnerCheck(model, 1) == 1);

        // Top right to bottom left
        model.setCellOwner(2,2, model.getPlayerByNumber(1));
        model.setCellOwner(1,1, model.getPlayerByNumber(1));
        model.setCellOwner(0,0, model.getPlayerByNumber(1));
        assert(winnerCheck(model, 1) == 1);

        model.setCellOwner(2,0, model.getPlayerByNumber(0));
        model.setCellOwner(2,2, model.getPlayerByNumber(0));
        model.setCellOwner(2,1, model.getPlayerByNumber(0));
        assert(winnerCheck(model, 0) == 0);

        model.setCellOwner(0,0, model.getPlayerByNumber(0));
        model.setCellOwner(1,0, model.getPlayerByNumber(0));
        model.setCellOwner(2,0, model.getPlayerByNumber(0));
        assert(winnerCheck(model, 0) == 0);

        model.setCellOwner(0,2, model.getPlayerByNumber(1));
        model.setCellOwner(1,1, model.getPlayerByNumber(1));
        model.setCellOwner(2,0, model.getPlayerByNumber(1));

        assert(winnerCheck(model, 1) == 1);
        assert(drawCheck(model) == -1);

//        // Check for a draw
//        model.setCellOwner(0,0, model.getPlayerByNumber(0));
//        model.setCellOwner(0,1, model.getPlayerByNumber(0));
//        model.setCellOwner(0,2, model.getPlayerByNumber(1));
//        model.setCellOwner(1,0, model.getPlayerByNumber(1));
//        model.setCellOwner(1,1, model.getPlayerByNumber(1));
//        model.setCellOwner(1,2, model.getPlayerByNumber(0));
//        assert(drawCheck(model) == 1);
    }

}