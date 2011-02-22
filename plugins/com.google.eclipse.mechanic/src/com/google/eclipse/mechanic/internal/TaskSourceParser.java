package com.google.eclipse.mechanic.internal;

import java.io.File;
import java.util.List;
import java.util.StringTokenizer;

import com.google.eclipse.mechanic.plugin.core.MechanicPreferences;
import com.google.gson.Gson;

public class TaskSourceParser {

  private static final Gson gson = new Gson();

  public final String[] parse(String text) {
    if (!text.startsWith("[")) {
      // I would use Splitter, but I won't use split.
      StringTokenizer st = new StringTokenizer(text, File.pathSeparator);
      List<String> list = Util.newArrayList();
      while (st.hasMoreElements()) {
        String elem = (String) st.nextElement();
        // Historically paths named "null" somehow got added to default prefs
        if (elem == null) {
          continue;
        }
        String substituted = doVariableSubstitution(elem);
        list.add(substituted);
      }
      return list.toArray(new String[0]);
    } else {
      return gson.fromJson(text, String[].class);
    }
  }

  public final String unparse(String[] items) {
    return gson.toJson(items);
  }

  protected String doVariableSubstitution(String elem) {
    return MechanicPreferences.doVariableSubstitution(elem);
  }
}