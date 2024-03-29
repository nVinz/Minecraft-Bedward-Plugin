package my.nvinz.core.vnizcore.game;

import my.nvinz.core.vnizcore.VnizCore;
import my.nvinz.core.vnizcore.teams.Team;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class Stage{

    private VnizCore plugin;
    public enum Status {
        LOBBY,
        COUNTDOWN,
        INGAME,
        AFTERGAME
    }
    public Stage(VnizCore pl){
        plugin = pl;
    }

    // TODO Exp & game bars
    public void startCountdown(){
        plugin.stageStatus = Status.COUNTDOWN;
        int countdownSeconds = plugin.getConfig().getInt("countdown");
        Thread thread = new Thread(() -> {
            for (int i = countdownSeconds; i > 0; i--) {
                try {
                    if (plugin.stageStatus == Status.COUNTDOWN) {
                        plugin.playSound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
                        plugin.makeAnnouncement(ChatColor.GRAY + "Игра начнется через " + ChatColor.GREEN + i);
                    }
                    else {
                        plugin.makeAnnouncement(ChatColor.RED + "Недостаточно игроков для начала игры.");
                        return;
                    }
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) { }
            }
            plugin.players.forEach(player -> {
                plugin.playSound(Sound.ENTITY_PLAYER_LEVELUP);
                player.sendMessage(ChatColor.GREEN+"Игра начинается!");
                if (plugin.players_and_teams.get(player) == null){
                    plugin.teams.forEach(team -> {
                        if (team.hasFree() && !plugin.players_and_teams.containsKey(player)){
                            plugin.addPlayerToTeam(player, team);
                        }
                    });
                }
            });

            inGame();
        });
        thread.start();
    }

    private void inGame(){
        plugin.resourceSpawn.setupThreads();
        plugin.resourceSpawn.runThreads();
        plugin.stageStatus = Status.INGAME;

        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    for (Team team : plugin.teams) {
                        for (Player player : team.players) {
                            player.teleport(team.spawnPoint);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.runTaskLater(plugin, 20);

        for (Player p : plugin.players) {
            p.showPlayer(plugin, p);
        }
    }

    public void inAftergame(){
        plugin.stageStatus = Status.AFTERGAME;
        plugin.players.forEach( player -> {
            plugin.leavePlayer(player);
            player.sendMessage(ChatColor.GRAY+"Игра закончилась.");
        });
        plugin.restart();
    }
}
