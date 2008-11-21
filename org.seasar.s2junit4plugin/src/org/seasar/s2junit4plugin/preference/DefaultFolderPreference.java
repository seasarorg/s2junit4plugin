package org.seasar.s2junit4plugin.preference;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.seasar.s2junit4plugin.Constants;
import org.seasar.s2junit4plugin.Messages;


public class DefaultFolderPreference extends Composite{
	
	private IPreferenceStore store;
	private Text testJavaPath;
	private Text testResourcesPath;
	
	public DefaultFolderPreference(IPreferenceStore store, Composite parent) {
		super(parent, SWT.NONE);
		this.store=store;
		setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		setLayout(layout);
		Label mainTestPathLabel = new Label(this, SWT.NULL);
		mainTestPathLabel.setText(Messages.getString("Preference.TestSourceFolder")); //$NON-NLS-1$
		testJavaPath = new Text(this, SWT.BORDER);
		testJavaPath.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		testJavaPath.setText(store.getString(Constants.PREF_TEST_JAVA_PATH)); //$NON-NLS-1$
		Label testResourcesPathLabel = new Label(this, SWT.NULL);
		testResourcesPathLabel.setText(Messages.getString("Preference.TestResourceFolder")); //$NON-NLS-1$
		testResourcesPath = new Text(this, SWT.BORDER);
		testResourcesPath.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		testResourcesPath.setText(store.getString("TestResourcesPath")); //$NON-NLS-1$
	}
	

    public void store() {
    	store.setValue(Constants.PREF_TEST_JAVA_PATH, testJavaPath.getText()); //$NON-NLS-1$
    	store.setValue(Constants.PREF_TEST_RESOURCES_PATH, testResourcesPath.getText()); //$NON-NLS-1$
    }
    
    public void loadDefault() {
    	testJavaPath.setText(Constants.PREF_DEFAULT_TEST_JAVA_PATH); //$NON-NLS-1$
    	testResourcesPath.setText(Constants.PREF_DEFAULT_TEST_RESOURCES_PATH); //$NON-NLS-1$
    }

}
