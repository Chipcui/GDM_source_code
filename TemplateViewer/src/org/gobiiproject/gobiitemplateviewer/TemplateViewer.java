package org.gobiiproject.gobiitemplateviewer;

import javafx.scene.layout.Pane;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFileColumn;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;
import org.gobiiproject.gobiimodel.types.GobiiColumnType;
import org.gobiiproject.gobiimodel.utils.HelperFunctions;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.JTextComponent;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;

public class TemplateViewer implements ActionListener, ComponentListener {
	public File templateFile;
	public JFrame mainWindow;
	public JPanel mainPane;
	public JScrollPane scrollPane;

	public static void main(String[] args) throws InterruptedException {
		new TemplateViewer();
		Thread.sleep(100000);
	}
	public TemplateViewer(){
		FlowLayout layout = new FlowLayout();
		mainPane = new JPanel();
		mainPane.setLayout(layout);
		scrollPane = new JScrollPane();
		mainWindow = new JFrame("Template Viewer");
		mainWindow.addComponentListener(this);
		mainWindow.setSize(800,600);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setViewportView(mainPane);
		JMenuBar mainMenu = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem loadMenu = new JMenuItem("Load");
		fileMenu.add(loadMenu);
		mainMenu.add(fileMenu);
		mainWindow.setJMenuBar(mainMenu);
		loadMenu.addActionListener(this);
		mainWindow.add(mainPane);
		mainWindow.repaint();
		mainWindow.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Load")){
			JFileChooser chooser = new JFileChooser();
			FileNameExtensionFilter f = new FileNameExtensionFilter(".json","json");
			chooser.addChoosableFileFilter(f);
			chooser.setFileFilter(f);
			chooser.showOpenDialog(mainWindow);
			this.templateFile = chooser.getSelectedFile();
			System.out.println(templateFile.toString());
			List<GobiiLoaderInstruction> instructions = HelperFunctions.parseInstructionFile(templateFile.toString());
			mainPane.setLayout(new GridLayout((instructions.size()+1)/2,2));
			mainWindow.setName(templateFile.getName());
			for(GobiiLoaderInstruction instruction:instructions){
				System.out.println(instruction.getTable());
				//mainPane.add(new JTextField(instruction.getTable()));
				mainPane.add(new Table(instruction).panel);
			}
			mainWindow.repaint();
			mainWindow.setVisible(true);
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
	/*public static List<GobiiLoaderInstruction> parseFile(File f){
		//ObjectMapper objectMapper = new ObjectMapper();
		GobiiLoaderInstruction[] instructions = null;

		try {
			instructions = read(f);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(instructions==null)return null;
		return Arrays.asList(instructions);
	}
	public static GobiiLoaderInstruction[] read(File file) throws Exception {
		Unmarshaller marshaller = JAXBContext.newInstance(GobiiLoaderInstruction[].class).createUnmarshaller();
		return (GobiiLoaderInstruction[]) marshaller.unmarshal(file);
	}*/
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
				if(c.getGobiiColumnType().equals(GobiiColumnType.CSV_ROW)){
					content.add(new JLabel("Column " + c.getcCoord()));

				}
				else if(c.getGobiiColumnType().equals(GobiiColumnType.CSV_COLUMN)){
					content.add(new JLabel("Row "  + c.getrCoord()));
				}
				else if(c.getGobiiColumnType().equals(GobiiColumnType.CSV_BOTH)) {
					content.add(new JLabel("" + c.getrCoord() + " : " + c.getcCoord()));
				}
				else if(c.getGobiiColumnType().equals(GobiiColumnType.CONSTANT)){
					content.add(new JLabel("Always "+c.getConstantValue()));
				}
				else if(c.getGobiiColumnType().equals(GobiiColumnType.AUTOINCREMENT)){
					content.add(new JLabel("Increments Automatically"));
				}
				else{
					content.add(new JLabel("Type "+c.getGobiiColumnType()));
				}
				if (c.getFilterFrom() != null || c.getFilterTo()!=null) {
					if(c.getFilterFrom() == null){
						content.add(new JLabel("Filter to " + c.getFilterTo()));
					}
					else if(c.getFilterTo()==null){
						content.add(new JLabel("Filter from "+ c.getFilterFrom() ));
					}
					else {
						content.add(new JLabel("Filter from " + c.getFilterFrom() + " to " + c.getFilterTo()));
					}
				}
			}
			content.setBackground(backgroundColor);
			panel.add(content);
			//border.addLayoutComponent(content,BorderLayout.SOUTH);
		}
	}
}
