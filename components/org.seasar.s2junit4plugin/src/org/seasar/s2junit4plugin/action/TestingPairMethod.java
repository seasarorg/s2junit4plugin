package org.seasar.s2junit4plugin.action;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TestingPairMethod {
	
	private List namingRules = new ArrayList();
	
	
	public void addNamingRule(String rule) {
		namingRules.add(rule);
	}
	
	public void clearNamingRules() {
		namingRules.clear();
	}
	
	public String[] getPairMethodNames(boolean isTest, String methodName) {
		Set result = new LinkedHashSet();
		if (isTest) {
			addTestMethodNames(methodName, result);
		} else {
			addTestedMethodNames(methodName, result);
		}
		return (String[]) result.toArray(new String[result.size()]);
	}
	
	private void addTestedMethodNames(String methodName, Set result) {
		for (int i = 0; i < namingRules.size(); ++i) {
			String testedMethodName = getTestedMethodName(methodName, (String) namingRules.get(i));
			if (testedMethodName != null)
				result.add(testedMethodName);
		}
	}
	
	private String escapeAllChars(String str) {
		// 正規表現の特殊文字
		final String escapeChars = "\\${}?+.[]-()><!|^:=*&,";
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < str.length(); ++i) {
			char c = str.charAt(i);
			if (escapeChars.indexOf(c) != -1)
				buf.append('\\');
			buf.append(c);
		}
		return buf.toString();
	}
	
	private String getTestedMethodName(String methodName, String namingRule) {
		// 正規表現で問題のある文字をすべてエスケープしておく
		namingRule = escapeAllChars(namingRule);
		// $, {, }の文字はエスケープされているため，\$, \{, \} と変換されている．
		// したがって，文字列中に埋め込む場合は， \$ => \\\\\\$, \{ => \\\\\\{
		// などとしなければならない．
		namingRule = namingRule.replaceAll("\\\\\\$\\\\\\{method\\\\\\}", "([\\\\w|\\$]+)");
		Pattern p = Pattern.compile(namingRule);
		Matcher m = p.matcher(methodName);
		if (m.matches()) {
			char chars[] = m.group(1).toCharArray();
			chars[0] = Character.toLowerCase(chars[0]);
			return new String(chars);
		}
		return null;
	}
	
	private void addTestMethodNames(String methodName, Set result) {
		for (int i = 0; i < namingRules.size(); ++i) {
			result.add(getTestMethodName(methodName, (String) namingRules.get(i)));
		}
	}
	
	private String getTestMethodName(String methodName, String namingRule) {
		String result = new String(namingRule);
		if (namingRule.startsWith("${method}")) {
			result = result.replaceAll("\\$\\{method\\}", methodName);
		} else {
			// 頭1文字目を大文字にする
			char chars[] = methodName.toCharArray();
			chars[0] = Character.toUpperCase(chars[0]);
			result = result.replaceAll("\\$\\{method\\}", new String(chars));
		}
		return result;
	}
	
}
