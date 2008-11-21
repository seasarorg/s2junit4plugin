package org.seasar.s2junit4plugin.preference;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.seasar.s2junit4plugin.util.PreferenceStoreUtil;

public class S2JUnit4PreferenceInitializer extends
		AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore preferenceStoreOfWorkspace = PreferenceStoreUtil.getPreferenceStoreOfWorkspace();
		preferenceStoreOfWorkspace.setDefault("MainTestPath", "src/test/java");
		preferenceStoreOfWorkspace.setDefault("TestResourcesPath", "src/test/resources");
	}

}
