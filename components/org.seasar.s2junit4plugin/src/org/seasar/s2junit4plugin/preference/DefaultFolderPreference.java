/*
 * Copyright 2008 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.s2junit4plugin.preference;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.seasar.s2junit4plugin.Constants;
import org.seasar.s2junit4plugin.Messages;
import org.seasar.s2junit4plugin.util.PreferenceStoreUtil;
import org.seasar.s2junit4plugin.wiget.SourceFolderTreeSelectionDialog;


public class DefaultFolderPreference extends Composite{
	
	private IPreferenceStore store;
	private Text testJavaPath;
	private Text testResourcesPath;
	private Button junti3Toggle;
	private Button jnit4Toggle;
	private Button s2unitToggle;
	private Button s2junit4Toggle;
	
	private IJavaProject javaProject;
	private Button testJavaPathButton;
	private Button testResourcesPathButton;
	
	
	public DefaultFolderPreference(IJavaProject javaProject, Composite parent) {
		this(PreferenceStoreUtil.getPreferenceStore(javaProject.getProject()), parent);
		this.javaProject = javaProject;
		testJavaPathButton.setEnabled(true);
		testResourcesPathButton.setEnabled(true);
	}
	
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
		gridLayout.numColumns = 3;
		group.setLayout(gridLayout);
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Label mainTestPathLabel = new Label(group, SWT.NULL);
		mainTestPathLabel.setText(Messages.getString("Preference.TestSourceFolder")); //$NON-NLS-1$
		testJavaPath = new Text(group, SWT.BORDER);
		testJavaPath.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		testJavaPath.setText(store.getString(Constants.PREF_TEST_JAVA_PATH)); //$NON-NLS-1$
		testJavaPathButton = new Button(group, SWT.BUTTON1);
		testJavaPathButton.setText(Messages.getString("Preference.TestSourceBrowse")); //$NON-NLS-1$
		testJavaPathButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				chooseFolder(testJavaPath, Messages.getString("Preference.TestSourceFolderSelection.title"), Messages.getString("Preference.TestSourceFolderSelection.message"));
			}
		});
		testJavaPathButton.setEnabled(false);
		Label testResourcesPathLabel = new Label(group, SWT.NULL);
		testResourcesPathLabel.setText(Messages.getString("Preference.TestResourceFolder")); //$NON-NLS-1$
		testResourcesPath = new Text(group, SWT.BORDER);
		testResourcesPath.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		testResourcesPath.setText(store.getString("TestResourcesPath")); //$NON-NLS-1$
		testResourcesPathButton = new Button(group, SWT.BUTTON1);
		testResourcesPathButton.setText(Messages.getString("Preference.TestResourceBrowse")); //$NON-NLS-1$
		testResourcesPathButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				chooseFolder(testResourcesPath, Messages.getString("Preference.TestResourceFolderSelection.title"), Messages.getString("Preference.TestResourceFolderSelection.message"));
			}
		});
		testResourcesPathButton.setEnabled(false);
		
		new Label(this, SWT.NULL);
		
		Group group2 = new Group(this,SWT.NONE);
		group2.setText(Messages.getString("Preference.GenerationType")); //$NON-NLS-1$
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 4;
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
	

    public void store(IPreferenceStore preferenceStore) {
    	preferenceStore.setValue(Constants.PREF_TEST_JAVA_PATH, testJavaPath.getText()); //$NON-NLS-1$
    	preferenceStore.setValue(Constants.PREF_TEST_RESOURCES_PATH, testResourcesPath.getText()); //$NON-NLS-1$
    	if(junti3Toggle.getSelection()) {
    		preferenceStore.setValue(Constants.PREF_TEST_GENERATION_TYPE, Messages.getString("Preference.JUnit3"));
    	} else if(jnit4Toggle.getSelection()) {
    		preferenceStore.setValue(Constants.PREF_TEST_GENERATION_TYPE, Messages.getString("Preference.JUnit4"));
    	} else if(s2unitToggle.getSelection()) {
    		preferenceStore.setValue(Constants.PREF_TEST_GENERATION_TYPE, Messages.getString("Preference.S2Unit"));
    	} else if(s2junit4Toggle.getSelection()) {
    		preferenceStore.setValue(Constants.PREF_TEST_GENERATION_TYPE, Messages.getString("Preference.S2JUnit4"));
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

    public void loadWorkspaceSetting() {
    	IPreferenceStore workspaceStore = PreferenceStoreUtil.getPreferenceStoreOfWorkspace();
    	testJavaPath.setText(workspaceStore.getString(Constants.PREF_TEST_JAVA_PATH));
    	testResourcesPath.setText(workspaceStore.getString(Constants.PREF_TEST_RESOURCES_PATH));
    	String storedValue = workspaceStore.getString(Constants.PREF_TEST_GENERATION_TYPE);
    	junti3Toggle.setSelection(Messages.getString("Preference.JUnit3").equals(storedValue));
    	jnit4Toggle.setSelection(Messages.getString("Preference.JUnit4").equals(storedValue));
    	s2unitToggle.setSelection(Messages.getString("Preference.S2Unit").equals(storedValue));
    	s2junit4Toggle.setSelection(Messages.getString("Preference.S2JUnit4").equals(storedValue));
    }
    
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		testJavaPath.setEnabled(enabled);
		testJavaPathButton.setEnabled(enabled);
		testResourcesPath.setEnabled(enabled);
		testResourcesPathButton.setEnabled(enabled);
		junti3Toggle.setEnabled(enabled);
		jnit4Toggle.setEnabled(enabled);
		s2unitToggle.setEnabled(enabled);
		s2junit4Toggle.setEnabled(enabled);
	}

	private void chooseFolder(Text txt, String title, String message) {
		SourceFolderTreeSelectionDialog dialog = new SourceFolderTreeSelectionDialog(getShell(), javaProject, title, message);
		if (dialog.open() == Dialog.OK) {
			txt.setText(dialog.getSelectedSourceFolder());
		}
	}

}
