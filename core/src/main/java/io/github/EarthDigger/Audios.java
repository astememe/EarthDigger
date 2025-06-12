package io.github.EarthDigger;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.*;

public class Audios implements Disposable {
    private static Audios instance;

    // Música
    private Music musicaFondo;
    private boolean sonando = true;

    // Sonidos
    private Sound ponerBloque;
    private Sound quitarBloque;
    private Sound cambioBloque;
    private Sound sonidoSalto;
    private Sound sonidoMuerte;
    private Sound sonidoGolpe;
    //private Sound iniciarJuego;

    private Audios() {
        musicaFondo = Gdx.audio.newMusic(Gdx.files.internal("Sonidos/musicaFondo.mp3"));

        ponerBloque = Gdx.audio.newSound(Gdx.files.internal("Sonidos/ColocarBloque.mp3"));
        quitarBloque = Gdx.audio.newSound(Gdx.files.internal("Sonidos/EliminarBloque.mp3"));
        cambioBloque = Gdx.audio.newSound(Gdx.files.internal("Sonidos/CambioBloques.mp3"));
        sonidoSalto = Gdx.audio.newSound(Gdx.files.internal("Sonidos/SonidoSalto.mp3"));
        sonidoMuerte = Gdx.audio.newSound(Gdx.files.internal("Sonidos/SonidoMuerte.mp3"));
        sonidoGolpe = Gdx.audio.newSound(Gdx.files.internal("Sonidos/SonidoGolpe.mp3"));
        //iniciarJuego = Gdx.audio.newSound(Gdx.files.internal("Sonidos/SonidoIniciarJuego"));

            musicaFondo.setLooping(true);
        musicaFondo.setVolume(0.5f);
    }

    public static Audios getInstance() {
        if (instance == null) {
            instance = new Audios();
        }
        return instance;
    }

    public void playMusicaFondo() {
        if (sonando) {
            musicaFondo.play();
        }
    }

    //public void sonidoIniciarJuego() {
    //    long id = iniciarJuego.play();
    //    iniciarJuego.setVolume(id, 1f);
    //}

    public void sonidoBloquePoner() {
        long id = ponerBloque.play();
        ponerBloque.setVolume(id, 0.5f);
    }

    public void sonidoBloqueQuitar() {
        long id = quitarBloque.play();
        quitarBloque.setVolume(id, 0.5f);
    }

    public void sonidoCambioBloque() {
        long id = cambioBloque.play();
        cambioBloque.setVolume(id, 0.4f);
    }

    public void playSonidoSalto() {
        long id = sonidoSalto.play();
        sonidoSalto.setVolume(id, 1f);
    }

    public void playSonidoMuerte() {
        long id = sonidoMuerte.play();
        sonidoMuerte.setVolume(id, 0.1f);
    }

    public void playSonidoGolpe() {
        long id = sonidoGolpe.play();
        sonidoGolpe.setVolume(id, 0.30f);
    }

    public void pausarMusica() {
        if (musicaFondo.isPlaying()) {
            musicaFondo.pause();
        }
    }

    public void reanudarMusica() {
        if (!musicaFondo.isPlaying()) {
            musicaFondo.play();
        }
    }

    public void setSonando(boolean activado) {
        this.sonando = activado;
        if (!activado) {
            musicaFondo.stop();
        } else if (!musicaFondo.isPlaying()) {
            musicaFondo.play();
        }
    }

    @Override
    public void dispose() {
        musicaFondo.dispose();
        ponerBloque.dispose();
        quitarBloque.dispose();
        cambioBloque.dispose();
        sonidoSalto.dispose();
        sonidoMuerte.dispose();
        sonidoGolpe.dispose();
    }
}
