package academy.mindswap.server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReadyChecker implements Runnable{
    private List<Game> og;
    private List<Game> cg;
    private Server server;
    boolean someoneIsNotReady;
    private ExecutorService service;


    public ReadyChecker(Server server) {
        this.server = server;
        service = Executors.newCachedThreadPool();
    }

    @Override
    public void run() {
        while(true) {

            this.og= new ArrayList<>(server.getOpenGames());
            this.cg= new ArrayList<>(server.getClosedGames());

            if(!og.isEmpty()) {
                for (Game game:og) {
                    someoneIsNotReady = false;
                    for (Server.ClientConnectionHandler player : game.getPlayers()) {
                        if (!player.isReady()) {
                            someoneIsNotReady = true;
                            break;
                        }
                    }
                    if (!someoneIsNotReady) {
                        server.getClosedGames().add(game);
                        server.getOpenGames().remove(game);
                        service.submit(game);
                        someoneIsNotReady = true;
                    }
                }
            }
        }
    }
}
