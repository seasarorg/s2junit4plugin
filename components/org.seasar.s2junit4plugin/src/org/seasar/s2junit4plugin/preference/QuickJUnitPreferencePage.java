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
import org.seasar.s2junit4plugin.Messages;
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
		item1.setText(Messages.getString("Preference.tabItem.defaultFolder")); //$NON-NLS-1$
		
		defaultFolderPreference = new DefaultFolderPreference(store, tabFolder);
		item1.setControl(defaultFolderPreference);
		
		TabItem item2 = new TabItem(tabFolder, SWT.NULL);
		item2.setText(Messages.getString("Preference.tabItem.namingRule")); //$NON-NLS-1$
        int widthHint= convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
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
