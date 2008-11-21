
package org.seasar.s2junit4plugin.util;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;


public class AdaptableUtil {


	public static IResource toResource(Object adaptable) {
		IResource result = null;
		if (adaptable instanceof IResource) {
			result = (IResource) adaptable;
		} else if (adaptable instanceof IAdaptable) {
			IAdaptable a = (IAdaptable) adaptable;
			result = (IResource) a.getAdapter(IResource.class);
		}
		return result;
	}

}
