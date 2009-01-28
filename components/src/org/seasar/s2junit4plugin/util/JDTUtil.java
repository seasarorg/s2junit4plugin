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
