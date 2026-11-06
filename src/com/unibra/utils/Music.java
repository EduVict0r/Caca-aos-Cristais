package com.unibra.utils;

import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import com.unibra.game.Game;

public class Music {

	URL[] urls;
	Clip[] clips;	

	public Music() {

		urls = new URL[3];
		clips = new Clip[3];

		try {
			urls[0] = new File("res/voar.wav").toURI().toURL();
			urls[1] = new File("res/ponto.wav").toURI().toURL();
			urls[2] = new File("res/bateu.wav").toURI().toURL();

			AudioInputStream aisVoar = AudioSystem.getAudioInputStream(urls[0]);
			clips[0] = AudioSystem.getClip();
			clips[0].open(aisVoar);

			AudioInputStream aisPonto = AudioSystem.getAudioInputStream(urls[1]);
			clips[1] = AudioSystem.getClip();
			clips[1].open(aisPonto);
			clips[1].addLineListener(e -> {
				System.out.println(clips[1].getMicrosecondPosition());
			});

			AudioInputStream aisBateu = AudioSystem.getAudioInputStream(urls[2]);
			clips[2] = AudioSystem.getClip();
			clips[2].open(aisBateu);


		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void playSound(int i) {
		if(!Game.soundFX)
			return;

		try {

			FloatControl volume = (FloatControl) clips[i].getControl(FloatControl.Type.MASTER_GAIN);
			volume.setValue(-25.f);

			if(clips[i].isActive()) {
				clips[i].stop();
			}
			clips[i].setMicrosecondPosition(0);
			clips[i].start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
