package org.seasar.s2junit4plugin.property;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.dialogs.PropertyPage;
import org.seasar.s2junit4plugin.Messages;
import org.seasar.s2junit4plugin.preference.DefaultFolderPreference;
import org.seasar.s2junit4plugin.preference.NamingRulesPreference;
import org.seasar.s2junit4plugin.util.PreferenceStoreUtil;


public class QuickJUnitPropertyPage extends PropertyPage {
	
	private NamingRulesPreference namingRulesPreference;
	
	private DefaultFolderPreference defaultFolderPreference;
	
	@Override
	protected Control createContents(Composite parent) {
		IPreferenceStore store = PreferenceStoreUtil.getPreferenceStoreOfProject(((IJavaProject) getElement()).getProject());
		TabFolder tabFolder = new TabFolder(parent, SWT.NULL);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		TabItem item1 = new TabItem(tabFolder, SWT.NULL);
		item1.setText(Messages.getString("Preference.tabItem.defaultFolder")); //$NON-NLS-1$
		
		defaultFolderPreference = new DefaultFolderPreference((IJavaProject) getElement(), tabFolder);
		item1.setControl(defaultFolderPreference);
		
		TabItem item2 = new TabItem(tabFolder, SWT.NULL);
		item2.setText(Messages.getString("Preference.tabItem.namingRule")); //$NON-NLS-1$
		int widthHint = convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
		namingRulesPreference = new NamingRulesPreference(store, tabFolder, widthHint);
		Dialog.applyDialogFont(namingRulesPreference);
		item2.setControl(namingRulesPreference);
		
		return parent;
	}
	
	protected void performDefaults() {
		super.performDefaults();
		namingRulesPreference.loadDefault();
		defaultFolderPreference.loadDefault();
	}
	
	public boolean performOk() {
		namingRulesPreference.store();
		defaultFolderPreference.store();
		return true;
	}
	
}
