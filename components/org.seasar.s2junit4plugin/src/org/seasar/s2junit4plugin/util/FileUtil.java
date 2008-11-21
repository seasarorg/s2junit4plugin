
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
