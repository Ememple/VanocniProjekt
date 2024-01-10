package com.example.vanocniprojekt;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;


import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class Controller implements Initializable {

    @FXML
    private Pane pane;
    @FXML
    private Label songLabel;
    @FXML
    private Button playButton, pauseButton, resetButton, previousButton, nextButton;
    @FXML
    private Slider volumeSlider;
    @FXML
    private ProgressBar songProgressBar;

    private Media media;

    private MediaPlayer mediaPlayer;

    private File directory;
    private File[] files;
    private ArrayList<File> songs;

    private int songNumber;

    private Timer timer;

    private TimerTask task;

    private boolean running;




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        songs= new ArrayList<File>();

        directory= new File("Music");

        files= directory.listFiles();

        if (files != null){
            for (File file : files){
                songs.add(file);
            }
        }

        media= new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        songLabel.setText(songs.get(songNumber).getName());

        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                mediaPlayer.setVolume(volumeSlider.getValue()*0.01);
            }
        });

    }

    public void playMedia() {
        beginTimer();
        mediaPlayer.setVolume(volumeSlider.getValue()*0.01);
        mediaPlayer.play();
    }

    public void pauseMedia() {
        cancelTimer();
        mediaPlayer.pause();
    }

    public void resetMedia() {
        songProgressBar.setProgress(0);
        mediaPlayer.seek(Duration.seconds(0));
    }

    public void previousMedia() {
        if (songNumber>0){
            songNumber--;
            mediaPlayer.stop();
            if (running== true){
                cancelTimer();
            }

            media= new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            songLabel.setText(songs.get(songNumber).getName());
            playMedia();
        }
        else {
            songNumber=songs.size()-1;
            mediaPlayer.stop();
            if (running== true){
                cancelTimer();
            }

            media= new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            songLabel.setText(songs.get(songNumber).getName());
            playMedia();
        }

    }

    public void nextMedia() {
        if (songNumber<songs.size()-1){
            songNumber++;
            mediaPlayer.stop();
            if (running== true){
                cancelTimer();
            }

            media= new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            songLabel.setText(songs.get(songNumber).getName());
            playMedia();
        }
        else {
            songNumber=0;
            mediaPlayer.stop();
            if (running== true){
                cancelTimer();
            }

            media= new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            songLabel.setText(songs.get(songNumber).getName());
            playMedia();
        }

    }

    public void beginTimer(){
        timer=new Timer();
        task = new TimerTask() {
            public void run() {
                running=true;
                double current=mediaPlayer.getCurrentTime().toSeconds();
                double end=media.getDuration().toSeconds();
                songProgressBar.setProgress(current/end);

                if (current/end == 1){
                    cancelTimer();
                }
            }
        };

        timer.scheduleAtFixedRate(task, 100,100);

    }

    public void cancelTimer(){
        running=false;
        timer.cancel();
    }
}
