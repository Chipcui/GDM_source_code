package org.gobiiproject.gobiitemplateviewer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;

public class TemplateViewer implements ActionListener {
	public File templateFile;
	public JFrame mainWindow;
	public static void main(String[] args) throws InterruptedException {
		new TemplateViewer();
		Thread.sleep(100000);
	}
	public TemplateViewer(){
		mainWindow = new JFrame("Template Viewer");
		mainWindow.setSize(800,600);
		JMenuBar mainMenu = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem loadMenu = new JMenuItem("Load");
		fileMenu.add(loadMenu);
		mainMenu.add(fileMenu);
		mainWindow.setJMenuBar(mainMenu);
		loadMenu.addActionListener(this);
		mainWindow.show();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Load")){
			JFileChooser chooser = new JFileChooser();
			chooser.showOpenDialog(mainWindow);
			this.templateFile = chooser.getSelectedFile();
			mainWindow.add(new JTextField(templateFile.getName()));
			mainWindow.repaint();
		}
	}

	public static List<GobiiLoaderInstruction> parseFile(File f){
		ObjectMapper objectMapper = new ObjectMapper();
		GobiiLoaderInstruction[] file = null;

		try {
			file = objectMapper.readValue(new FileInputStream(f), GobiiLoaderInstruction[].class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(file==null)return null;
		return Arrays.asList(file);
	}
}
