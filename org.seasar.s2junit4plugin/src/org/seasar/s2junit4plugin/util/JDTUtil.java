
package org.seasar.s2junit4plugin.util;

import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;

/**
 * JDT 操作用のユーティリティです。
 */
public final class JDTUtil {

	/**
	 * ソースフォルダ一覧を取得します。
	 * @param project Javaプロジェクト
	 * @return ソースフォルダ一覧
	 * @throws JavaModelException JDTエラーが発生した場合
	 */
	public static List<IPath> getSourceFolders(IJavaProject project) throws JavaModelException {
		List<IPath> sourceFolders = new java.util.ArrayList<IPath>();
		for(IClasspathEntry entry : project.getResolvedClasspath(true)) {
			if(entry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
				sourceFolders.add(entry.getPath());
			}
		}
		return sourceFolders;
	}
}
