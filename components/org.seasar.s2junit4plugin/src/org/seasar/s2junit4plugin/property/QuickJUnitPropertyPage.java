package org.seasar.s2junit4plugin.property;


import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.dialogs.PropertyPage;
import org.seasar.s2junit4plugin.Constants;
import org.seasar.s2junit4plugin.Messages;
import org.seasar.s2junit4plugin.preference.DefaultFolderPreference;
import org.seasar.s2junit4plugin.preference.NamingRulesPreference;
import org.seasar.s2junit4plugin.util.PreferenceStoreUtil;


public class QuickJUnitPropertyPage extends PropertyPage {
	
	private Button projectSettingButton;

	private TabFolder tabFolder;
	
	private NamingRulesPreference namingRulesPreference;
	
	private DefaultFolderPreference defaultFolderPreference;
	
	@Override
	protected Control createContents(Composite parent) {
		IJavaProject javaProject = (IJavaProject) getElement();
		setPreferenceStore(PreferenceStoreUtil.getPreferenceStoreOfProject(javaProject.getProject()));
		
		IPreferenceStore store = PreferenceStoreUtil.getPreferenceStore(javaProject.getProject());
		
		createProjectSettingContent(parent);
		
		tabFolder = new TabFolder(parent, SWT.NULL);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		TabItem item1 = new TabItem(tabFolder, SWT.NULL);
		item1.setText(Messages.getString("Preference.tabItem.defaultFolder")); //$NON-NLS-1$
		
		defaultFolderPreference = new DefaultFolderPreference(javaProject, tabFolder);
		item1.setControl(defaultFolderPreference);
		
		TabItem item2 = new TabItem(tabFolder, SWT.NULL);
		item2.setText(Messages.getString("Preference.tabItem.namingRule")); //$NON-NLS-1$
		int widthHint = convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
		namingRulesPreference = new NamingRulesPreference(store, tabFolder, widthHint);
		Dialog.applyDialogFont(namingRulesPreference);
		item2.setControl(namingRulesPreference);
		
		tabFolder.setEnabled(projectSettingButton.getSelection());
		namingRulesPreference.setEnabled(projectSettingButton.getSelection());
		defaultFolderPreference.setEnabled(projectSettingButton.getSelection());
		
		return parent;
	}
	
	private void createProjectSettingContent(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginBottom = 0;
		composite.setLayout(layout);
		
		projectSettingButton = new Button(composite, SWT.CHECK);
		projectSettingButton.setText(Messages.getString("Property.EnableProjectSetting")); //$NON-NLS-1$
		projectSettingButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		projectSettingButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				boolean selection = ((Button) e.widget).getSelection();
				tabFolder.setEnabled(selection);
				namingRulesPreference.setEnabled(selection);
				defaultFolderPreference.setEnabled(selection);
			}
		});
		projectSettingButton.setSelection(getPreferenceStore().getBoolean(Constants.PREF_ENABLE_PROJECT_SETTING));
		
		Link workspaceSettingLink = new Link(composite, SWT.WRAP);
		workspaceSettingLink.setText(Messages.getString("Property.WorkspaceSettingLink"));
		workspaceSettingLink.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				String id = "org.seasar.s2junit4plugin.page.QuickJUnitPreferencePage";
				PreferencesUtil.createPreferenceDialogOn(getShell(), id, new String[] { id }, null).open();
				if (!projectSettingButton.getSelection()) {
					namingRulesPreference.loadWorkspaceSetting();
					defaultFolderPreference.loadWorkspaceSetting();
				}
			}
		});
		
		Label separator = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalSpan = 2;
		separator.setLayoutData(gd);
	}
	
	protected void performDefaults() {
		super.performDefaults();
		projectSettingButton.setSelection(false);
		namingRulesPreference.loadWorkspaceSetting();
		defaultFolderPreference.loadWorkspaceSetting();
	}
	
	public boolean performOk() {
		getPreferenceStore().setValue(Constants.PREF_ENABLE_PROJECT_SETTING, projectSettingButton.getSelection());
		namingRulesPreference.store(getPreferenceStore());
		defaultFolderPreference.store(getPreferenceStore());
		return true;
	}
	
}
