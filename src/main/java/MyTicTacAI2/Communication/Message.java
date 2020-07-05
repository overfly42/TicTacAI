package MyTicTacAI2.Communication;

public enum Message {
    StartSession, //With number of games
    RegisterOpen, 
    Register, //with ID
    RegisterSuccess, //With ID
    RegisterRejected, //With ID
    StartGame, 
    PlayerReady,//With ID
    Turn, //With ID
    Set,  //With ID,X,Y ->from Client or to all
    SetRejected, //With ID
    EndGame,//With Winner ID1/ID2/Tie
    EndSession//End of comunication
}