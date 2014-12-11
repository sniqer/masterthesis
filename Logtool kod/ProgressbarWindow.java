package MasterThesisPackage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

public class ProgressbarWindow extends JFrame{

	private static final long serialVersionUID = 1L;
	private ProgressBar pb2;
	
	public ProgressbarWindow(){
		
		super("MemoryInformationWindow");
		JPanel pane = new JPanel();
		
//	    pb2 = new ProgressBar(pane, SWT.SMOOTH);
	    pb2.setBounds(100, 40, 200, 20);
	    	    
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	  //3. Create components and put them in the frame.
	  //...create emptyLabel...
//	  this.getContentPane().add("MemoryInformationWindow", BorderLayout.CENTER);

	  //4. Size the frame.
	  this.pack();

	  //5. Show it.
	  this.setVisible(true);
	  System.out.println("done");
	}
	
	public void setMaxValue(int max){
		pb2.setMaximum(max);
	}
	
	public void updateProgressBar(int currentValue){
		pb2.setSelection(currentValue);
	}
	
	public void close(){
		this.dispose();
	}
}
