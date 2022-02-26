package academy.mindswap.server.commands;

import academy.mindswap.server.Game;
import academy.mindswap.server.Server;
import academy.mindswap.server.messages.Messages;

public class QuitRoomHandler implements CommandHandler {

    @Override
    public void execute(Server server, Server.ClientConnectionHandler clientConnectionHandler) {
        Game game = clientConnectionHandler.getGame();

        clientConnectionHandler.quitGame();

        if (game.getPlayers().isEmpty()){
            server.getOpenGames().remove(game);
            return;
        }

        for (Server.ClientConnectionHandler player: game.getPlayers()) {
            player.setReady(false);
            player.send("Someone left the room and you became unready.");
        }

    }


}
