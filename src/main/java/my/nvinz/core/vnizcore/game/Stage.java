package my.nvinz.core.vnizcore.game;

import my.nvinz.core.vnizcore.VnizCore;
import my.nvinz.core.vnizcore.teams.Team;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ListIterator;

public class Stage{

    VnizCore plugin;
    public enum Status {
        LOBBY,
        COUNTDOWN,
        INGAME,
        AFTERGAME
    }
    public Stage(VnizCore pl){
        plugin = pl;
    }

    public void inLobby(){

    }

    public void inGame(){
        /*ListIterator<Team> teamsIt = plugin.teams.listIterator();
        while (teamsIt.hasNext()){
            teamsIt.next().tpAllToSpawn();
<<<<<<< HEAD:src/main/java/my/nvinz/core/vnizcore/game/Stage.java
        }
=======
        }* /
        plugin.teams.forEach((n) -> {
            n.tpAllToSpawn();
            n.clearAllInventory();
        });
>>>>>>> 82ab46aa4d75fd801008f3abc4aef720e6b014e5:src/main/java/my/nvinz/core/vnizcore/Stage.java*/
    }

    public void startCountdown(){
        int countdownSeconds = 10;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = countdownSeconds; i >= 0; i--) {
                    try {
                        for (Player players: plugin.getServer().getOnlinePlayers()){
                            players.sendMessage(ChatColor.GRAY+"Игра начнется через " + ChatColor.GREEN+Integer.toString(i));
                        }
                        Thread.sleep(1000);
                    } catch (InterruptedException e) { }
                }
                for (Player players: plugin.getServer().getOnlinePlayers()){
                    players.sendMessage(ChatColor.GREEN+"Игра начинается!");
                }
                inGame();
            }
        });
        thread.start();
    }
}
