package application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class MainController {
	private static FileChooser fileChooser = new FileChooser();
	private static BlackWhiteFilter bwFilter = new BlackWhiteFilter(127);
	private BufferedImage selectedImage;
	private int noiseReduction = 1;
	
    @FXML
    private ImageView originalImageView;

    @FXML
    private ImageView outlinedImageView;

    @FXML
    private ImageView blackWhiteImageView;
    
    @FXML
    private Slider thresholdSlider;
    
    @FXML
    private Slider noiseReductionSlider;

    public void initialize(){
    	thresholdSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				bwFilter.setThreshold(newValue.intValue());
				refreshImage();
			}
    	});
    	noiseReductionSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				noiseReduction = newValue.intValue();
				refreshImage();
			}
    	});
    }
    
    @FXML
    void handleOpenFile(ActionEvent event) {
    	File imageFile = fileChooser.showOpenDialog(null);
    	
    	if (imageFile != null) {
    		try {
    			selectedImage = ImageIO.read(imageFile);
    			refreshImage();
    		} catch (IOException e) {
				e.printStackTrace();
			}
    	} 
    }
    
    private void refreshImage() {
    	if (selectedImage == null) {
    		return;
    	}
		BufferedImage bwImage = bwFilter.applyToImage(selectedImage);
		
		originalImageView.setImage(SwingFXUtils.toFXImage(selectedImage, null));
		blackWhiteImageView.setImage(SwingFXUtils.toFXImage(bwImage, null));
		
		BirdAnalysis ba = new BirdAnalysis(bwImage);
		BufferedImage outlineImage = ba.outlineBirds(selectedImage);
		outlinedImageView.setImage(SwingFXUtils.toFXImage(outlineImage, null));
    }

    @FXML
    void handleQuit(ActionEvent event) {
    	Platform.exit();
    }

    public static void main(String[] args) {
    	Application.launch(args);
    }
}
