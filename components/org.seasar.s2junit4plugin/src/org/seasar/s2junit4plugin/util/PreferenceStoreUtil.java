
package org.seasar.s2junit4plugin.util;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.seasar.s2junit4plugin.Activator;



/**
 * 設定ストアを取得するためのユーティリティクラスです
 */
public class PreferenceStoreUtil {

	/**
	 * プロジェクト固有の設定ストアを取得します。<br>
	 * プロジェクト固有設定が行われていない場合はワークスペースの設定ストアを取得します。
	 */
//	public static IPreferenceStore getPreferenceStore(IProject project) {
//		IPreferenceStore store = getPreferenceStoreOfProject(project);
//		boolean isPrjCustom = store.getBoolean(MARKER_SEVERITY_ENABLE_PROJECT_CUSTOM);
//		if (!isPrjCustom) {
//			return getPreferenceStoreOfWorkspace();
//		} else {
//			return store;
//		}
//	}

	/**
	 * プロジェクト固有の設定ストアを取得します。
	 */
	public static IPreferenceStore getPreferenceStoreOfProject(IProject project) {
		ProjectScope scope = new ProjectScope(project);
		String id = Activator.getDefault().getBundle().getSymbolicName();
		IPreferenceStore store = getStore(scope, id);
		return store;
	}

	/**
	 * ワークスペースの設定ストアを取得します。
	 */
	public static IPreferenceStore getPreferenceStoreOfWorkspace() {
		String id = Activator.getDefault().getBundle().getSymbolicName();
		return getStore(new InstanceScope(), id);
	}

	private static IPreferenceStore getStore(IScopeContext scope, String id) {
		ScopedPreferenceStore store = new ScopedPreferenceStore(scope, id);
		return store;
	}
}
