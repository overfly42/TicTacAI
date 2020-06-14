package MyTicTacAI2.Game;
/**
 * This Enum represents the different states of a single game.
 */
public enum GameState
{
    Init,//Setting everything up
    StartSession,//Clear Statistics and prepairs the games
    WaitForPlayer,//Waiting for players
    StartGame,//Starts a single game
    WaitForAction,//Awaits for the players actions and verifyes them
    CheckField,//Checks if a further move is possible 
    EndGame,//Terminates the single Game
    EndSession//Ends a session and sends the statistics
}