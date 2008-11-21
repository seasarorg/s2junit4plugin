
package org.seasar.s2junit4plugin;

import java.util.Dictionary;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Shell;
import org.osgi.framework.Bundle;
import org.osgi.framework.Constants;

/**
 * Eclipseログ出力のための機能を提供します。
 */
public class Logger {

	/**
	 * このプラグインで使用するログメッセージ用の {@link IStatus} オブジェクトを生成します。
	 * @param severity 重要度
	 * @param message メッセージテキスト
	 * @param exception 例外オブジェクト(あれば)
	 */
	public static IStatus newStatus(int severity, String message, Throwable exception) {
		String statusMessage = message;
		if (message == null || message.trim().length() == 0) {
			if (exception == null) {
				throw new IllegalArgumentException();
			} else if (exception.getMessage() == null) {
				statusMessage = exception.toString();
			} else {
				statusMessage = exception.getMessage();
			}
		}
		return new Status(severity, Activator.PLUGIN_ID, severity,
				statusMessage, exception);
	}
	
    public static IStatus createSystemErrorStatus(int severity, Exception ex, Object caller) {
//        int severity = IStatus.ERROR;

        String message;
        message  = ex.getMessage();
        if (message == null)
            message = ""; //$NON-NLS-1$
        MultiStatus errorStatus = new MultiStatus(getID(), severity, message, ex);
        
        Dictionary headers = getBundle().getHeaders();

        String providerName = "" + headers.get(Constants.BUNDLE_VENDOR);
        message = Messages.getString("QuickJUnitPlugin.systemError.providerNameLabel") + providerName; //$NON-NLS-1$
        errorStatus.add(createStatus(severity, message));

        String pluginName = "" + headers.get(Constants.BUNDLE_NAME);
        message = Messages.getString("QuickJUnitPlugin.systemError.pluginNameLabel") + pluginName; //$NON-NLS-1$
        errorStatus.add(createStatus(severity, message));

        String pluginId = getBundle().getSymbolicName();
        message = Messages.getString("QuickJUnitPlugin.systemError.pluginIdLabel") + pluginId; //$NON-NLS-1$
        errorStatus.add(createStatus(severity, message));

        String version = "" + headers.get(Constants.BUNDLE_VERSION);
        message = Messages.getString("QuickJUnitPlugin.systemError.versionLabel") + version; //$NON-NLS-1$
        errorStatus.add(createStatus(severity, message));

        Class klass = caller instanceof Class ? (Class) caller : caller.getClass();
        message = Messages.getString("QuickJUnitPlugin.systemError.classLabel") + klass.getName(); //$NON-NLS-1$
        errorStatus.add(createStatus(severity, message, IStatus.ERROR, ex));

        return errorStatus;
    }
    
    public static IStatus createStatus(int severity, String message) {
        return createStatus(severity, message, 0, null);
    }

    private static IStatus createStatus(int severity, String message, int code, Exception ex) {
        return new Status(severity, getID(), code, message, ex);
    }
    
    public static String getID() {
        return getBundle().getSymbolicName();
    }
    
    public static Bundle getBundle() {
    	return Activator.getDefault().getBundle();
    }
    
    public static ILog getLog() {
    	return Activator.getDefault().getLog();
    }
    
    public static void error(Exception ex, Object caller) {
        IStatus status = createSystemErrorStatus(IStatus.ERROR, ex, caller);
        getLog().log(status);
        ErrorDialog.openError((Shell) null, Messages.getString("QuickJUnitPlugin.systemError.dialog.title"), Messages.getString("QuickJUnitPlugin.systemError.dialog.message"), status); //$NON-NLS-1$ //$NON-NLS-2$
    }

	/**
	 * デバッグログを出力します。
	 * @param message メッセージテキスト
	 */
	public static void debug(String message) {
		debug(message, null);
	}

	/**
	 * デバッグログを出力します。
	 * @param message メッセージテキスト
	 * @param t 例外オブジェクト
	 */
	public static void debug(String message, Throwable t) {
		if(Activator.getDefault().isDebugging()) {
			info(message, t);
		}
	}

	/**
	 * INFOログを出力します。
	 * @param message メッセージテキスト
	 */
	public static void info(String message) {
		info(message, null);
	}

	/**
	 * INFOログを出力します。
	 * @param message メッセージテキスト
	 * @param t 例外オブジェクト
	 */
	public static void info(String message, Throwable t) {
		Activator.getDefault().getLog()
				.log(newStatus(IStatus.INFO, message, t));
	}

	/**
	 * WARNログを出力します。
	 * @param message メッセージテキスト
	 */
	public static void warn(String message) {
		warn(message, null);
	}

	/**
	 * WARNログを出力します。
	 * @param message メッセージテキスト
	 * @param t 例外オブジェクト
	 */
	public static void warn(String message, Throwable t) {
		Activator.getDefault().getLog().log(
				newStatus(IStatus.WARNING, message, t));
	}

	/**
	 * ERRORログを出力します。
	 * @param message メッセージテキスト
	 */
	public static void error(String message) {
		error(message, null);
	}

	/**
	 * ERRORログを出力します。
	 * @param message メッセージテキスト
	 * @param t 例外オブジェクト
	 */
	public static void error(String message, Throwable t) {
		Activator.getDefault().getLog().log(
				newStatus(IStatus.ERROR, message, t));
	}
}
