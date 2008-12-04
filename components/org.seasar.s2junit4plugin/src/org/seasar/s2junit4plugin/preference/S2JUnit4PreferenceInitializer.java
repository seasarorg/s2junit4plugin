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
