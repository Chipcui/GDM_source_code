package org.gobiiproject.gobiitemplateviewer;

import org.codehaus.jackson.map.ObjectMapper;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFileColumn;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;
import org.gobiiproject.gobiimodel.dto.instructions.loader.LoaderInstructionFilesDTO;
import org.gobiiproject.gobiimodel.types.GobiiColumnType;
import org.gobiiproject.gobiimodel.utils.HelperFunctions;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TemplateViewer implements ActionListener, ComponentListener, WindowListener {
	public File templateFile;
	public boolean isInstruction;
	public JFrame mainWindow;
	public JPanel mainPane;
	public JScrollPane scrollPane;
	public File referenceFile;
	public JMenuItem loadRMenu = new JMenuItem("Load Reference");


	public static void main(String[] args) throws InterruptedException {
		new TemplateViewer();
}

	public TemplateViewer() {
		mainWindow = new JFrame("Template Viewer");
		mainWindow.addComponentListener(this);
		mainWindow.addWindowListener(this);
		mainWindow.setSize(800, 600);


		JMenuBar mainMenu = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem loadMenu = new JMenuItem("Load Template");
		JMenuItem loadIMenu = new JMenuItem("Load Instruction");
		JMenuItem clearMenu = new JMenuItem("Clear");
		loadRMenu.setEnabled(false);//Can't load a reference until you've loaded a template

		loadMenu.addActionListener(this);
		loadIMenu.addActionListener(this);
		loadRMenu.addActionListener(this);
		clearMenu.addActionListener(this);
		fileMenu.add(loadMenu);
		fileMenu.add(loadIMenu);
		fileMenu.add(loadRMenu);
		mainMenu.add(fileMenu);
		mainMenu.add(clearMenu);
		mainWindow.setJMenuBar(mainMenu);
		mainWindow.repaint();
		mainWindow.setVisible(true);
	}
	private void newMainPanel(){
		if(mainPane!=null)mainWindow.remove(mainPane);
		FlowLayout layout = new FlowLayout();
		mainPane = new JPanel();
		mainPane.setLayout(layout);
		mainWindow.add(mainPane);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Load Template") || e.getActionCommand().equals("Load Instruction")) {
			newMainPanel();//Start a new 'main' panel
			JFileChooser chooser = new JFileChooser();
			FileNameExtensionFilter f = new FileNameExtensionFilter(".json", "json");
			chooser.addChoosableFileFilter(f);
			chooser.setFileFilter(f);
			chooser.showOpenDialog(mainWindow);

			//Set up template file and instruction variables
			this.templateFile = chooser.getSelectedFile();
			isInstruction = e.getActionCommand().equals("Load Instruction");

			loadFile();
			if(templateFile!=null) {
				loadRMenu.setEnabled(true);
			}
		}

		else if(e.getActionCommand().equals("Load Reference")){
			JFileChooser chooser = new JFileChooser();
			chooser.showOpenDialog(mainWindow);
			this.referenceFile = chooser.getSelectedFile();

			//Redo the load and display, this time with a reference set
			newMainPanel();
			loadFile();
		}
		else if(e.getActionCommand().equals("Clear")){
			templateFile=null; referenceFile=null;
			newMainPanel();
			mainWindow.setVisible(true);
		}
	}

	/**
	 * Assuming everything's set up correctly (isInstruction, mainPane, etc, this method places data into them based on
	 * the loaded isntruction file.
	 */
	private void loadFile() {
		if(templateFile==null)return;
		List<GobiiLoaderInstruction> instructions = getInstructions();
		System.out.println("Instruction length " + instructions.size());
		mainPane.setLayout(new GridLayout((instructions.size() + 1) / 2, 2));
		mainWindow.setName(templateFile.getName());
		for (GobiiLoaderInstruction instruction : instructions) {
			mainPane.add(new Table(instruction).panel);
		}

		//Repaint main window
		mainWindow.repaint();
		mainWindow.setVisible(true);//causes appropriate refresh? though mainWindow.repaint doesn't. I hate swing.
	}

	//Using templateFile and isInstruction, reads either a template or an instruction file, returning the GLI list
	private List<GobiiLoaderInstruction> getInstructions() {
		if (isInstruction) {
			return HelperFunctions.parseInstructionFile(templateFile.toString());
		} else {
			return getFromJobFile(templateFile.toString());
		}
	}

	@Override
	public void componentResized(ComponentEvent e) {
		//scrollPane.setMaximumSize(new Dimension(mainWindow.getWidth(),Integer.MAX_VALUE));
		//scrollPane.getViewport().setExtentSize(new Dimension(mainWindow.getWidth(),Integer.MAX_VALUE));

	}

	@Override
	public void componentMoved(ComponentEvent e) {

	}

	@Override
	public void componentShown(ComponentEvent e) {

	}

	@Override
	public void componentHidden(ComponentEvent e) {

	}

	public List<GobiiLoaderInstruction> getFromJobFile(String jobFileName) {
		ObjectMapper objectMapper = new ObjectMapper();
		LoaderInstructionFilesDTO file = null;

		try {
			file = objectMapper.readValue(new FileInputStream(jobFileName), LoaderInstructionFilesDTO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (file == null) return null;
		return file.getGobiiLoaderInstructions();
	}


	public static List<GobiiLoaderInstruction> read(File file) {
		Unmarshaller marshaller = null;
		try {
			marshaller = JAXBContext.newInstance(LoaderInstructionFilesDTO.class).createUnmarshaller();
			return ((LoaderInstructionFilesDTO) marshaller.unmarshal(file)).getGobiiLoaderInstructions();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void windowOpened(WindowEvent e) {

	}

	@Override
	public void windowClosing(WindowEvent e) {

	}

	@Override
	public void windowClosed(WindowEvent e) {
		System.exit(0);
	}

	@Override
	public void windowIconified(WindowEvent e) {

	}

	@Override
	public void windowDeiconified(WindowEvent e) {

	}

	@Override
	public void windowActivated(WindowEvent e) {

	}

	@Override
	public void windowDeactivated(WindowEvent e) {

	}

	class Table {
		public JPanel panel;

		public Table(GobiiLoaderInstruction inst) {
			panel = new JPanel();
			//'Pastel-like' color
			Color backgroundColor=new Color((float) (Math.random() *.33 + 0.67), (float) (Math.random() *.33 + 0.67), (float) (Math.random() *.33 + 0.67));
			panel.setBackground(backgroundColor);

			JPanel content = new JPanel();
			//JPanel content = panel;
			BoxLayout boxLeft=new BoxLayout(panel,BoxLayout.PAGE_AXIS);
			content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
			BorderLayout border = new BorderLayout();
			border.addLayoutComponent(content,BorderLayout.CENTER);

			JTextField tableName=new JTextField(inst.getTable());
			tableName.setEditable(false);
			tableName.setBackground(backgroundColor);
			tableName.setSelectedTextColor(Color.black);
			tableName.setFont(new Font(null,Font.BOLD,18));
			border.addLayoutComponent(tableName, BorderLayout.NORTH);
			panel.setLayout(border);

			panel.add(tableName);

			panel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
			panel.add(new JSeparator(JSeparator.HORIZONTAL));//Add a vertical separator
			boolean first=true;
			for (GobiiFileColumn c : inst.getGobiiFileColumns()) {
				if(first)first=false;
				else{
					content.add(new JSeparator(JSeparator.HORIZONTAL));//Add a vertical separator
				}
				content.add(new JLabel(c.getName()));
				String valueLabel;
				switch(c.getGobiiColumnType()) {
					case CSV_ROW:
						if (referenceFile != null) {
							valueLabel=readCSV_ROW(referenceFile,inst,c.getrCoord()-1,c.getcCoord());
						} else {
							valueLabel = "Column " + c.getcCoord();
						}
						break;
					case CSV_COLUMN:
						if (referenceFile != null) {
							valueLabel=readCSV_ROW(referenceFile,inst,c.getrCoord()-1,c.getcCoord());
						} else {
							valueLabel = "Row " + c.getrCoord();
						}
						break;
					case CSV_BOTH:
						valueLabel="Matrix from " + c.getrCoord() + " : " + c.getcCoord();
						break;
					case CONSTANT:
						valueLabel="Always " + c.getConstantValue();
						break;
					case AUTOINCREMENT:
						valueLabel="Increments Automatically";
						break;
						default:
							valueLabel="Type " + c.getGobiiColumnType();
						break;
					}
				content.add(new JLabel(valueLabel));
				String filterLabel=null;
				if (c.getFilterFrom() != null || c.getFilterTo()!=null) {
					if(c.getFilterFrom() == null){
						filterLabel="Filter to " + c.getFilterTo();
					}
					else if(c.getFilterTo()==null){
						filterLabel="Filter from "+ c.getFilterFrom();
					}
					else {
						filterLabel="Filter from " + c.getFilterFrom() + " to " + c.getFilterTo();
					}
				}
				if(filterLabel!=null){
					content.add(new JLabel(filterLabel));
				}
			}
			content.setBackground(backgroundColor);
			panel.add(content);
		}
	}

	//Stolen and reporpoised from CSVFileReaderV2
	private static String readCSV_ROW(File file, GobiiLoaderInstruction loaderInstruction,int row, int col){
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
			int rowNo = 0;
			String fileRow;
			while ((fileRow = bufferedReader.readLine()) != null) {
				if (rowNo >= row) {
					// All required rows read.
					break;
				}
				rowNo++;
			}
				String[] rowVal = fileRow.split(loaderInstruction.getGobiiFile().getDelimiter());
			return rowVal[col];
		}catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
		return "ERROR";
	}
}
