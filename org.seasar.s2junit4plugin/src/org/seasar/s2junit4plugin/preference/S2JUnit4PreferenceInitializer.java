package org.seasar.s2junit4plugin.preference;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.seasar.s2junit4plugin.Constants;
import org.seasar.s2junit4plugin.util.PreferenceStoreUtil;

public class S2JUnit4PreferenceInitializer extends
		AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore preferenceStoreOfWorkspace = PreferenceStoreUtil.getPreferenceStoreOfWorkspace();
		preferenceStoreOfWorkspace.setDefault(Constants.PREF_TEST_JAVA_PATH, Constants.PREF_DEFAULT_TEST_JAVA_PATH);
		preferenceStoreOfWorkspace.setDefault(Constants.PREF_TEST_RESOURCES_PATH, Constants.PREF_DEFAULT_TEST_RESOURCES_PATH);
		preferenceStoreOfWorkspace.setDefault(Constants.PREF_TEST_GENERATION_TYPE, Constants.PREF_DEFAULT_TEST_GENERATION_TYPE);
	}

}
