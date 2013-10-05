package org.jpedal.objects.javascript;

public class AformDefaultJSscript
{
  public static String getViewerSettings()
  {
    return "ADBE.viewerVersion = 9.0;\nADBE.Reader_Need_Version = 9.0;\nADBE.Viewer_Need_Version = 9.0;\nxfa_installed = true;\nxfa_version = 2.6;\n\n";
  }

  public static String getstaticScript()
  {
    return getStaticVariable() + "function AFExtractNums(string) {\n" + "\t/* returns an array of numbers that it managed to extract from the given\n" + "\t * string or null on failure */\n" + "\tvar nums = new Array();\n" + "\tif (string.charAt(0) == '.' || string.charAt(0) == ',')\n" + "\t\tstring = \"0\" + string;\n" + "\twhile(AFDigitsRegExp.test(string)) {\n" + "\t\tnums.length++;\n" + "\t\tnums[nums.length - 1] = RegExp.lastMatch;\n" + "\t\tstring = RegExp.rightContext;\n" + "\t}\n" + "\tif(nums.length >= 1) return nums;\n" + "\treturn null;\n" + "}\n" + '\n' + "function AFMakeNumber(string)\n" + "{\t/* attempts to make a number out of a string that may not use '.' as the\n" + "\t * seperator; it expects that the number is fairly well-behaved other than\n" + "\t * possibly having a non-JavaScript friendly separator */\n" + "\tvar type = typeof string;\n" + "\tif (type == \"number\")\n" + "\t\treturn string;\n" + "\tif (type != \"string\")\n" + "\t\treturn null;\n" + "\tvar array = AFExtractNums(string);\n" + "\tif(array)\n" + "\t{\n" + "\t\tvar joined = array.join(\".\");\n" + "\t\tif (string.indexOf(\"-.\") >= 0)\n" + "\t\t\tjoined = \"0.\" + joined;\n" + "\t\treturn joined * (string.indexOf(\"-\") >= 0 ? -1.0 : 1.0);\n" + "\t}\n" + "\telse\n" + "\t\treturn null;\n" + "}\n" + '\n' + "function AFMergeChange(event){\t\n" + "/* merges the last change with the uncommitted change */\n" + "\tvar prefix, postfix;\n" + "\tvar value = event.value;\n" + "\tif(event.willCommit) return event.value;\n" + "\tif(event.selStart >= 0)\n" + "\t\tprefix = value.substring(0, event.selStart);\n" + "\telse prefix = \"\";\n" + "\tif(event.selEnd >= 0 && event.selEnd <= value.length)\n" + "\t\tpostfix = value.substring(event.selEnd, value.length);\n" + "\telse postfix = \"\";\n" + "\treturn prefix + event.change + postfix;\n" + "}\n" + '\n';
  }

  private static String getStaticVariable()
  {
    return "\n";
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.javascript.AformDefaultJSscript
 * JD-Core Version:    0.6.2
 */