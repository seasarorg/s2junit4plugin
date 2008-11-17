package org.seasar.s2junit4plugin.preference;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;


public class DefaultFolderPreference extends Composite{
	
	private IPreferenceStore store;
	private Text mainTestPath;
	
	public DefaultFolderPreference(IPreferenceStore store, Composite parent) {
		super(parent, SWT.NONE);
		this.store=store;
		setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		setLayout(layout);
		Label mainTestPathLabel = new Label(this, SWT.NULL);
		mainTestPathLabel.setText("テストソースフォルダ");
		mainTestPath = new Text(this, SWT.BORDER);
		mainTestPath.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		mainTestPath.setText(store.getString("MainTestPath"));
	}
	

    public void store() {
    	store.setValue("MainTestPath", mainTestPath.getText());
    }
    
    public void loadDefault() {
    	mainTestPath.setText("src/test/java");
    }

}
