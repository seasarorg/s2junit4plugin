package org.seasar.s2junit4plugin.preference;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.seasar.s2junit4plugin.Activator;
import org.seasar.s2junit4plugin.action.NamingRules;

public class QuickJUnitPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

    private NamingRules namingRules;
    private NamingRulesPreference namingRulesPreference;

    public QuickJUnitPreferencePage() {
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
    }

    public void init(IWorkbench workbench)  {
        IPreferenceStore store = getPreferenceStore();
        namingRules = new NamingRules(store);
	}

	protected Control createContents(Composite parent)  {
        Composite composite= new Composite(parent, SWT.NULL);
        GridLayout layout= new GridLayout();
        layout.numColumns= 1;
        layout.marginWidth= 0;
        composite.setLayout(layout);
        GridData data= new GridData(GridData.FILL_BOTH);
        composite.setLayoutData(data);

        int widthHint= convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
        namingRulesPreference = new NamingRulesPreference(namingRules.get(), composite, widthHint);
        Dialog.applyDialogFont(composite);
        return composite;
	}

    protected void performDefaults() {
        super.performDefaults();
        namingRulesPreference.setValue(namingRules.getDefault());
    }

    public boolean performOk() {
        namingRules.set(namingRulesPreference.getValue());
        return true;
    }
}
