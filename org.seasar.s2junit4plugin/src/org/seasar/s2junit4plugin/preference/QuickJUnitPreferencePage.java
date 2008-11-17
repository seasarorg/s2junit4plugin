package org.seasar.s2junit4plugin.preference;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.seasar.s2junit4plugin.util.PreferenceStoreUtil;
import org.seasar.s2junit4plugin.action.NamingRules;


public class QuickJUnitPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

    private NamingRulesPreference namingRulesPreference;
    
    private DefaultFolderPreference defaultFolderPreference;
    
    public void init(IWorkbench workbench)  {
		IPreferenceStore store = PreferenceStoreUtil.getPreferenceStoreOfWorkspace();
		setPreferenceStore(store);
	}

	protected Control createContents(Composite parent)  {
        IPreferenceStore store = getPreferenceStore();
        NamingRules namingRules = new NamingRules(store);
		TabFolder tabFolder = new TabFolder(parent, SWT.NULL);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
		TabItem item1 = new TabItem(tabFolder, SWT.NULL);
		item1.setText("ネーミングルール");
        int widthHint= convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
        namingRulesPreference = new NamingRulesPreference(store, tabFolder, widthHint);
        Dialog.applyDialogFont(namingRulesPreference);
        item1.setControl(namingRulesPreference);
        
		TabItem item2 = new TabItem(tabFolder, SWT.NULL);
		item2.setText("デフォルトフォルダ");
		
		defaultFolderPreference = new DefaultFolderPreference(store, tabFolder);
		item2.setControl(defaultFolderPreference);
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
