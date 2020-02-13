class OXOController
{
    private String input;
    private OXOModel model;
    private int current = 1;
    private boolean GameOver = false;

    public OXOController(OXOModel model)
    {
        this.model = model;

        checkAssertionsEnabled();

        model.setCurrentPlayer(model.getPlayerByNumber(0));

        System.out.println(input);
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
                    swapPlayer(current);
                }
                else{
                    throw new CellAlreadyTakenException(x, y);
                }
            }

        }

        checkOutcome();
        moveToNextPlayer();

        if(command.contains("exit"))
        {
            System.exit(0);
        }

    }

    private void swapPlayer(int current)
    {
        int players = model.getNumberOfPlayers();
        model.setCurrentPlayer(model.getPlayerByNumber(current));
    }

    private void moveToNextPlayer()
    {
        if(current == 0) {
            if(current != model.getNumberOfPlayers()-1) {
                current++;
            }
        }

        else {
            current = 0;
        }

    }

    private void checkOutcome()
    {
        if (winnerCheck(model, 1) == 1)
        {
            model.setWinner(model.getPlayerByNumber(1));
            GameOver = true;
        }

        if (winnerCheck(model, 0) == 0)
        {
            model.setWinner(model.getPlayerByNumber(0));
            GameOver = true;
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

    private int winnerCheck(OXOModel model, int playerNumb)
    {
        // Top left, Bottom right
        if(model.getCellOwner(0,0) == model.getPlayerByNumber(playerNumb))
        {
            if(model.getCellOwner(1,1) == model.getPlayerByNumber(playerNumb))
            {
                if(model.getCellOwner(2,2) == model.getPlayerByNumber(playerNumb))
                {
                    return playerNumb;
                }
            }
        }

        // Top right, Bottom left
        if(model.getCellOwner(0,2) == model.getPlayerByNumber(playerNumb))
        {
            if(model.getCellOwner(1,1) == model.getPlayerByNumber(playerNumb))
            {
                if(model.getCellOwner(2,0) == model.getPlayerByNumber(playerNumb))
                {
                    return playerNumb;
                }
            }
        }


        // Right Column
        if(model.getCellOwner(0,2) == model.getPlayerByNumber(playerNumb))
        {
            if(model.getCellOwner(1,2) == model.getPlayerByNumber(playerNumb))
            {
                if(model.getCellOwner(2,2) == model.getPlayerByNumber(playerNumb))
                {
                    return playerNumb;
                }
            }
        }

        // Middle Column
        if(model.getCellOwner(0,1) == model.getPlayerByNumber(playerNumb))
        {
            if(model.getCellOwner(1,1) == model.getPlayerByNumber(playerNumb))
            {
                if(model.getCellOwner(2,1) == model.getPlayerByNumber(playerNumb))
                {
                    return playerNumb;
                }
            }
        }

        // Left Column
        if(model.getCellOwner(0,0) == model.getPlayerByNumber(playerNumb))
        {
            if(model.getCellOwner(1,0) == model.getPlayerByNumber(playerNumb))
            {
                if(model.getCellOwner(2,0) == model.getPlayerByNumber(playerNumb))
                {
                    return playerNumb;
                }
            }
        }

        // Bottom Row
        if(model.getCellOwner(2,0) == model.getPlayerByNumber(playerNumb))
        {
            if(model.getCellOwner(2,1) == model.getPlayerByNumber(playerNumb))
            {
                if(model.getCellOwner(2,2) == model.getPlayerByNumber(playerNumb))
                {
                    return playerNumb;
                }
            }
        }

        // Middle row
        if(model.getCellOwner(1,0) == model.getPlayerByNumber(playerNumb))
        {
            if(model.getCellOwner(1,1) == model.getPlayerByNumber(playerNumb))
            {
                if(model.getCellOwner(1,2) == model.getPlayerByNumber(playerNumb))
                {
                    return playerNumb;
                }
            }
        }

        // Top Row
        if(model.getCellOwner(0,0) == model.getPlayerByNumber(playerNumb))
        {
            if(model.getCellOwner(0,1) == model.getPlayerByNumber(playerNumb))
            {
                if(model.getCellOwner(0,2) == model.getPlayerByNumber(playerNumb))
                {
                    return playerNumb;
                }
            }
        }

        return -1;
    }

    public void testing(OXOModel model)
    {
        assert(model.getNumberOfPlayers() == 2);

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

        model.setCellOwner(0,0, model.getPlayerByNumber(1));
        model.setCellOwner(0,2, model.getPlayerByNumber(1));
        model.setCellOwner(0,1, model.getPlayerByNumber(1));
        assert(winnerCheck(model, 1) == 1);

        model.setCellOwner(0,0, model.getPlayerByNumber(1));
        model.setCellOwner(0,2, model.getPlayerByNumber(0));
        model.setCellOwner(0,1, model.getPlayerByNumber(1));
        assert(winnerCheck(model, 1) == -1);

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

        // Check for a draw
        model.setCellOwner(0,0, model.getPlayerByNumber(0));
        model.setCellOwner(0,1, model.getPlayerByNumber(0));
        model.setCellOwner(0,2, model.getPlayerByNumber(1));
        model.setCellOwner(1,0, model.getPlayerByNumber(1));
        model.setCellOwner(1,1, model.getPlayerByNumber(1));
        model.setCellOwner(1,2, model.getPlayerByNumber(0));
        assert(drawCheck(model) == 1);
    }


}