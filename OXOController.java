class OXOController
{
    private String input;
    private OXOModel model;
    private int current = 0;
    private boolean GameOver = false;

    public OXOController(OXOModel model)
    {
        this.model = model;

        checkAssertionsEnabled();

        model.setCurrentPlayer(model.getPlayerByNumber(0));

    }

    private void checkAssertionsEnabled() {

        boolean assertionsEnabled = false;
        assert(assertionsEnabled = true);

        int row = 1; int column = 1;

        if (assertionsEnabled)
        {
            testing(model);
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
        if(command.length() > 2)
        {
            throw new InvalidCellIdentifierException("", command);
        }

        // Flags invalid characters //
        if(x < 0 || y < 0)
        {
            throw new InvalidCellIdentifierException("", command);
        }

        if(x > model.getNumberOfColumns()-1 || y  > model.getNumberOfRows()-1) {
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

        System.out.println(current);
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

            if (model.getCellOwner(counter2, counter) == model.getPlayerByNumber(playerNumb)) {
                x++;
            }

            counter--;
            counter2++;
        }

        if(x == model.getWinThreshold() ) {
            return playerNumb;
        }
        else{
            return -1;
        }

    }

    private int scanStraightLines(int playerNumb)
    {
        int column = 0;
        int row = 0;
        int counter = 0;
        int x = 0;

        while(counter < model.getWinThreshold()*model.getNumberOfRows()) {

            if(column == model.getNumberOfColumns())
            {
                row++;
                column = 0;
            }

            if (model.getCellOwner(row, column) == model.getPlayerByNumber(playerNumb)) {
                x++;

                if(x == model.getWinThreshold() ) {
                    return playerNumb;
                }

            }

            column++;
            counter++;
        }

        return -1;
    }

    private int winnerCheck(OXOModel model, int playerNumb)
    {
        int x = scanDiagonalLeft(playerNumb);
        int y = scanDiagonalRight(playerNumb);
        int z = scanStraightLines(playerNumb);

        if(x == playerNumb || y == playerNumb || z == playerNumb) {
            return playerNumb;
        }

        return -1;
    }

    public void testing(OXOModel model)
    {
        assert(model.getNumberOfPlayers() == 3);

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

//        // Top left to bottom right
//        model.setCellOwner(0,0, model.getPlayerByNumber(1));
//        model.setCellOwner(1,1, model.getPlayerByNumber(1));
//        model.setCellOwner(2,2, model.getPlayerByNumber(1));
//        model.setCellOwner(3,3, model.getPlayerByNumber(1));
//        assert(winnerCheck(model, 1) == 1);

//        model.setCellOwner(0,3, model.getPlayerByNumber(1));
//        model.setCellOwner(1,2, model.getPlayerByNumber(1));
//        model.setCellOwner(2,1, model.getPlayerByNumber(1));
//        model.setCellOwner(3,0, model.getPlayerByNumber(1));
//        assert(winnerCheck(model, 1) == 1);

//        model.setCellOwner(0,3, model.getPlayerByNumber(1));
//        model.setCellOwner(0,2, model.getPlayerByNumber(1));
//        model.setCellOwner(0,1, model.getPlayerByNumber(1));
//        model.setCellOwner(0,0, model.getPlayerByNumber(1));
//        assert(winnerCheck(model, 1) == 1);

//        model.setCellOwner(3,3, model.getPlayerByNumber(1));
//        model.setCellOwner(3,2, model.getPlayerByNumber(1));
//        model.setCellOwner(3,1, model.getPlayerByNumber(1));
//        model.setCellOwner(3,0, model.getPlayerByNumber(1));
//        assert(winnerCheck(model, 1) == 1);

//        model.setCellOwner(2,3, model.getPlayerByNumber(1));
//        model.setCellOwner(2,2, model.getPlayerByNumber(1));
//        model.setCellOwner(2,1, model.getPlayerByNumber(1));
//        model.setCellOwner(2,0, model.getPlayerByNumber(1));
//        assert(winnerCheck(model, 1) == 1);

        model.setCellOwner(0,1, model.getPlayerByNumber(1));
        model.setCellOwner(1,1, model.getPlayerByNumber(1));
        model.setCellOwner(2,1, model.getPlayerByNumber(1));
        model.setCellOwner(3,1, model.getPlayerByNumber(1));
        assert(winnerCheck(model, 1) == 1);

//        // Top right to bottom left
//        model.setCellOwner(2,2, model.getPlayerByNumber(1));
//        model.setCellOwner(1,1, model.getPlayerByNumber(1));
//        model.setCellOwner(0,0, model.getPlayerByNumber(1));
//        assert(winnerCheck(model, 1) == 1);
//
//        model.setCellOwner(2,0, model.getPlayerByNumber(0));
//        model.setCellOwner(2,2, model.getPlayerByNumber(0));
//        model.setCellOwner(2,1, model.getPlayerByNumber(0));
//        assert(winnerCheck(model, 0) == 0);
//
//        model.setCellOwner(0,0, model.getPlayerByNumber(0));
//        model.setCellOwner(1,0, model.getPlayerByNumber(0));
//        model.setCellOwner(2,0, model.getPlayerByNumber(0));
//        assert(winnerCheck(model, 0) == 0);
//
//        model.setCellOwner(0,2, model.getPlayerByNumber(1));
//        model.setCellOwner(1,1, model.getPlayerByNumber(1));
//        model.setCellOwner(2,0, model.getPlayerByNumber(1));
//
//        assert(winnerCheck(model, 1) == 1);
//        assert(drawCheck(model) == -1);

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

