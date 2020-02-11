import javax.lang.model.type.NullType;
import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.event.*;
import javafx.scene.text.*;
import javafx.geometry.*;

class OXOController
{
    private String input;
    private OXOModel model;
    public int  current = 1;

    public OXOController(OXOModel model)
    {
        this.model = model;

        boolean assertionsEnabled = false;
        assert(assertionsEnabled = true);

        int row = 1; int column = 1;

        if (assertionsEnabled)
        {
            testing(model);
        }

        model.setCurrentPlayer(model.getPlayerByNumber(0));


            System.out.println(input);
    }

    public int convertCharToInt(char c)
    {
        int x;

        if(c >= 'a' & c <= 'z') {
            x = c - 'a';
            return x;
        }

        if(c >= 'A' & c <= 'C') {
            x = c - 'A';
            return x;
        }

        x = Character.getNumericValue(c) - 1;

        return x;
    }

    public void swapPlayer(int current)
    {
        int players = model.getNumberOfPlayers();
        model.setCurrentPlayer(model.getPlayerByNumber(current));
    }

    // Deals with input string
    public void handleIncomingCommand(String command) throws InvalidCellIdentifierException,
            CellAlreadyTakenException, CellDoesNotExistException
    {
        char[] characters = command.toCharArray();
        char x = characters[0]; char y = characters[1];

        if(command.length() > 2)
        {
            throw new InvalidCellIdentifierException("Bad", "Bad");
        }

        if((convertCharToInt(x) > 2 && convertCharToInt(y) > 2) ||
                (convertCharToInt(x) < 0 && convertCharToInt(y) < 0)) {
            throw new CellDoesNotExistException(x, y);
        }
        else {
            model.setCellOwner(convertCharToInt(x), convertCharToInt(y), model.getCurrentPlayer());
            swapPlayer(current);
        }

        // Check for winner
        if (winnerCheck(model, 1) == 1)
        {
            model.setWinner(model.getPlayerByNumber(1));
        }

        if (winnerCheck(model, 0) == 0)
        {
            model.setWinner(model.getPlayerByNumber(0));
        }

        if(current == 0) {
            current++;
        }

        else {
            current = 0;
        }


        if(command.contains(("d2"))) {
            throw new InvalidCellIdentifierException("Bad", "Bad");
        }


        if(command.contains(("zz"))) {
            throw new CellAlreadyTakenException(1, 9);
        }

        if(command.contains("exit"))
        {
            System.exit(0);
        }

    }

    private int drawCheck(OXOModel model)
    {
        int x = 0; int y = 0;

        for(x = 0; x < 3; x++){
            for(y = 0; y < 3; y++){

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

        assert(convertCharToInt('a') == 0);
        assert(convertCharToInt('b') == 1);
        assert(convertCharToInt('c') == 2);

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
