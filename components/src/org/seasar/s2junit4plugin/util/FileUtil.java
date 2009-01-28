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

import java.io.InputStream;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IPackageFragment;
import org.seasar.s2junit4plugin.Logger;


public class FileUtil  {
	
	public static IFile createFile(IPackageFragment pack, String fileName,
			InputStream contents) {
		try {
			IContainer folder = (IContainer) pack.getUnderlyingResource();
			IFile file = folder.getFile(new Path(fileName));
			file.create(contents, false, null);
			return file;
		} catch (CoreException e) {
			Logger.error(e, FileUtil.class);
			return null;
		}
	}


}
