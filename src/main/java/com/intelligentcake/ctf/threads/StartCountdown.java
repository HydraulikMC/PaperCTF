package com.intelligentcake.ctf.threads;

import com.intelligentcake.ctf.CTF;
import com.intelligentcake.ctf.game.GameState;
import com.intelligentcake.ctf.utils.ChatUtilities;
import org.bukkit.Bukkit;

public class StartCountdown implements Runnable {

    private int secondsLeft;

    @Override
    public void run() {
        while (true) {
            if (CTF.game.getGameState().isState(GameState.IN_LOBBY) && CTF.game.canStart()) {
                secondsLeft = 60;
                for (; secondsLeft >= 0; secondsLeft--) {
                    if (secondsLeft == 0) {
                        CTF.game.start();
                        break;
                    }

                    if (secondsLeft % 10 == 0 || secondsLeft < 10) {
                        ChatUtilities.broadcast(secondsLeft + " seconds until the games starts!");
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Bukkit.shutdown();
                    }
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Bukkit.shutdown();
            }
        }
    }

    public int getSecondsLeft() {
        return secondsLeft;
    }
}
