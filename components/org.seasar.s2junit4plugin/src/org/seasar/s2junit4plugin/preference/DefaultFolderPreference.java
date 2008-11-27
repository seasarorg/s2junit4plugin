package org.seasar.s2junit4plugin.preference;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.seasar.s2junit4plugin.Constants;
import org.seasar.s2junit4plugin.Messages;


public class DefaultFolderPreference extends Composite{
	
	private IPreferenceStore store;
	private Text testJavaPath;
	private Text testResourcesPath;
	private Button junti3Toggle;
	private Button jnit4Toggle;
	private Button s2unitToggle;
	private Button s2junit4Toggle;
	
	public DefaultFolderPreference(IPreferenceStore store, Composite parent) {
		super(parent, SWT.NONE);
		this.store=store;

		setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout();
//		layout.numColumns = 4;
		setLayout(layout);
		
		Group group = new Group(this,SWT.NONE);
		group.setText(Messages.getString("Preference.CodeGeneration")); //$NON-NLS-1$
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		group.setLayout(gridLayout);
		
		Label mainTestPathLabel = new Label(group, SWT.NULL);
		mainTestPathLabel.setText(Messages.getString("Preference.TestSourceFolder")); //$NON-NLS-1$
		testJavaPath = new Text(group, SWT.BORDER);
		testJavaPath.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		testJavaPath.setText(store.getString(Constants.PREF_TEST_JAVA_PATH)); //$NON-NLS-1$
		Label testResourcesPathLabel = new Label(group, SWT.NULL);
		testResourcesPathLabel.setText(Messages.getString("Preference.TestResourceFolder")); //$NON-NLS-1$
		testResourcesPath = new Text(group, SWT.BORDER);
		testResourcesPath.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		testResourcesPath.setText(store.getString("TestResourcesPath")); //$NON-NLS-1$
		
		new Label(this, SWT.NULL);
		
		Group group2 = new Group(this,SWT.NONE);
		group2.setText(Messages.getString("Preference.GenerationType")); //$NON-NLS-1$
		GridLayout gridLayout2 = new GridLayout();
//		gridLayout2.numColumns = 4;
		group2.setLayout(gridLayout2);
		
		junti3Toggle= new Button(group2, SWT.RADIO);
		junti3Toggle.setText(Messages.getString("Preference.JUnit3")); //$NON-NLS-1$
		junti3Toggle.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 1, 1));
		if(store.getString(Constants.PREF_TEST_GENERATION_TYPE).equals(Messages.getString("Preference.JUnit3"))) {
			junti3Toggle.setSelection(true);
		}
		
		jnit4Toggle= new Button(group2, SWT.RADIO);
		jnit4Toggle.setText(Messages.getString("Preference.JUnit4")); //$NON-NLS-1$
		if(store.getString(Constants.PREF_TEST_GENERATION_TYPE).equals(Messages.getString("Preference.JUnit4"))) {
			jnit4Toggle.setSelection(true);
		}
		jnit4Toggle.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 1, 1));
		
		s2unitToggle= new Button(group2, SWT.RADIO);
		s2unitToggle.setText(Messages.getString("Preference.S2Unit")); //$NON-NLS-1$
		s2unitToggle.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 1, 1));
		if(store.getString(Constants.PREF_TEST_GENERATION_TYPE).equals(Messages.getString("Preference.S2Unit"))) {
			s2unitToggle.setSelection(true);
		}
		
		s2junit4Toggle= new Button(group2, SWT.RADIO);
		s2junit4Toggle.setText(Messages.getString("Preference.S2JUnit4")); //$NON-NLS-1$
		s2junit4Toggle.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 1, 1));
		if(store.getString(Constants.PREF_TEST_GENERATION_TYPE).equals(Messages.getString("Preference.S2JUnit4"))) {
			s2junit4Toggle.setSelection(true);
		}
		s2junit4Toggle.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, false, false, 1, 1));
	}
	

    public void store() {
    	store.setValue(Constants.PREF_TEST_JAVA_PATH, testJavaPath.getText()); //$NON-NLS-1$
    	store.setValue(Constants.PREF_TEST_RESOURCES_PATH, testResourcesPath.getText()); //$NON-NLS-1$
    	if(junti3Toggle.getSelection()) {
    		store.setValue(Constants.PREF_TEST_GENERATION_TYPE, Messages.getString("Preference.JUnit3"));
    	} else if(jnit4Toggle.getSelection()) {
    		store.setValue(Constants.PREF_TEST_GENERATION_TYPE, Messages.getString("Preference.JUnit4"));
    	} else if(s2unitToggle.getSelection()) {
    		store.setValue(Constants.PREF_TEST_GENERATION_TYPE, Messages.getString("Preference.S2Unit"));
    	} else if(s2junit4Toggle.getSelection()) {
    		store.setValue(Constants.PREF_TEST_GENERATION_TYPE, Messages.getString("Preference.S2JUnit4"));
    	} else {
    		throw new IllegalArgumentException("JUnit3, JUnit4, S2Unit, S2JUnit4 are not selected");
    	}
    }
    
    public void loadDefault() {
    	testJavaPath.setText(Constants.PREF_DEFAULT_TEST_JAVA_PATH); //$NON-NLS-1$
    	testResourcesPath.setText(Constants.PREF_DEFAULT_TEST_RESOURCES_PATH); //$NON-NLS-1$
    	junti3Toggle.setSelection(false);
    	jnit4Toggle.setSelection(false);
    	s2unitToggle.setSelection(false);
    	s2junit4Toggle.setSelection(true);
    }

}
